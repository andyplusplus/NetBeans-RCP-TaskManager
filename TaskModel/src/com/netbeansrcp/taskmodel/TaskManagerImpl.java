/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskmodel;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.LocalFileSystem;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author nbuser
 */
@ServiceProvider(service = TaskManager.class)
public final class TaskManagerImpl implements TaskManager {

    private List<Task> topLevelTasks;
    private PropertyChangeSupport pss;
    private Map<Task, DataObject> doByTask = /*TODO: Added by WZD*/ new HashMap<Task, DataObject>();

    //********************* Virtual File System *********************
    private FileObject root;
    static final String PROP_ADDITIONAL_ATTR = "SAVED_DATE";
    //****************************
    private boolean tasksLoaded;    // Used only by  "private void loadTasks() "


    public TaskManagerImpl() {
	this.topLevelTasks = new ArrayList<Task>();
	this.pss = new PropertyChangeSupport(this);

	//********************* Virtual File System *********************
	try {
            String pathName = "/Users/jpe/tasks/";
            //get the current path
            Path currentRelativePath = Paths.get("");
            pathName = currentRelativePath.toAbsolutePath().toString();            
            //
            File file = new File(pathName);
	    LocalFileSystem fs = new LocalFileSystem();
	    fs.setRootDirectory(file);
	    this.root = fs.getRoot();
	} catch (Exception ex) {
	    Exceptions.printStackTrace(ex);
	}

	//loadTasks() ; //LoadTask by Data Object

//	TODO: UnComment Or Move the following code
//	Task t1 = this.createTask();
//		t1.setName("Todo 1");
//	Task t2 = this.createTask("Todo 1.1", t1.getId());
//		t2 = this.createTask("Todo 1.2", t1.getId());
//		t2 = this.createTask("Todo 1.3", t1.getId());
//			    this.createTask("Todo 1.3.1", t2.getId());
//
//	Task t3 = this.createTask();
//	t3.setName("Todo 2");
//	    t2 = this.createTask("Todo 2.1", t3.getId());
//	    t2 = this.createTask("Todo 2.2", t3.getId());
//	    t2 = this.createTask("Todo 2.3", t3.getId());
//	Task t4 = this.createTask("Todo 2.3.1", t2.getId());
//		    t2 = this.createTask("Todo 2.3.1.1", t4.getId());
//		    t2 = this.createTask("Todo 2.3.1.2", t4.getId());
//
//	//********************* Virtual File System *********************
//	this.save(t1);
//	this.save(t3);
//	System.out.println("Count of known TopLevelTasks before deleting:" + this.topLevelTasks.size());
//	this.removeTask(t1.getId());
//
//	this.removeTask(t3.getId());
//	System.out.println("Count of known TopLevelTasks after deleting: "
//		+ this.topLevelTasks.size());
//	this.loadTasks();
//	System.out.println("Count of known TopLevelTasks after loading: "
//		+ this.topLevelTasks.size());
    }


    @Override
    public synchronized DataObject createTask() {
	DataObject dao = null;
	try{
	    Task task = new TaskImpl();
	    FileObject fo = this.save(task);
	    dao = DataObject.find(fo);
	    if(null != dao){
		this.doByTask.put(task, dao);
	    }
	    this.topLevelTasks.add(task);
	    this.pss.firePropertyChange(PROP_TASKLIST_ADD, null, task);
	    
	}catch(DataObjectNotFoundException ex){
	    Exceptions.printStackTrace(ex);
	}//try
	return dao;
    }//public synchronized DataObject createTask() 


    @Override
    public synchronized Task createTask(String name, String parentId) {
	Task task = new TaskImpl(name, parentId);
	Task parent = this.getTask(parentId);
	if (null != parent) {
	    parent.addChild(task);
	}
	this.pss.firePropertyChange(PROP_TASKLIST_ADD, parent, task);
	return task;
    }

    @Override
    public synchronized void removeTask(String id) {
	Task task = this.getTask(id);
	if (null != task) {
	    Task parent = this.getTask(task.getParentId());
	    if (null != parent) {
		parent.remove(task);
	    } else {
		DataObject dao = this.doByTask.get(task);
		if (null != dao){
			    try{
				dao.getPrimaryFile().delete();
				this.doByTask.remove(task);
			    }catch(IOException ex){
				Exceptions.printStackTrace(ex);
			    }//try Catch
		}//if (null != dao){
	    }//if (null != parent) {
	    this.topLevelTasks.remove(task);
	    this.pss.firePropertyChange(PROP_TASKLIST_REMOVE,
		    parent, task);
	}//if (null != task) {
    }//public synchronized void removeTask(String id)

    @Override
    public List<DataObject> getTopLevelTasks() {
	    return Collections.unmodifiableList(new ArrayList<DataObject> (this.doByTask.values() )	);
    }

    @Override
    public Task getTask(String id) {
	for (Task task : this.topLevelTasks) {
	    Task found = this.findTask(task, id);
	    if (null != found) {
		return found;
	    }
	}
	return null;
    }

    private Task findTask(Task task, String id) {
	if (id.equals(task.getId())) {
	    return task;
	}
	for (Task child : task.getChildren()) {
	    Task found = this.findTask(child, id);
	    if (null != found) {
		return found;
	    }
	}
	return null;
    }

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
	this.pss.addPropertyChangeListener(listener);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
	this.pss.removePropertyChangeListener(listener);
    }

    //********************* Virtual File System *********************
    //*************************************************************
    // Virtual File System
    //*************************************************************
    @Override
    public FileObject save(Task task) {
	FileObject fo = null;
	try {
	    fo = this.root.createData(task.getId() + ".tsk");
	    this.save(task, fo);
	} catch (IOException ex) {
	    Exceptions.printStackTrace(ex);
	}
	return fo;
    }//public FileObject save(Task task)

    @Override
    public void save(Task task, FileObject fo) {
	ObjectOutputStream oOut = null;
	try {
	    oOut = new ObjectOutputStream(new BufferedOutputStream(fo.getOutputStream()));
	    oOut.writeObject(task);
	    //providing additional attributes
	    fo.setAttribute(PROP_ADDITIONAL_ATTR, SimpleDateFormat.getInstance().format( new Date() ));
	} catch (Exception ex) {
	    Exceptions.printStackTrace(ex);
	} finally {
	    try {
		oOut.close();
	    } catch (IOException ex) {
		Exceptions.printStackTrace(ex);
	    }
	}
    }//public void save(Task task, FileObject fo)

    @Override
    public Task load(FileObject fo) {
	Task task = null;
	ObjectInputStream oIn = null;
	try {
	    oIn = new ObjectInputStream(new BufferedInputStream(fo.getInputStream()));
	    task = (Task) oIn.readObject();
	    System.out.println("Loaded: " + task + "[" + fo.getAttribute(PROP_ADDITIONAL_ATTR) + "]");
	    if (!this.topLevelTasks.contains(task)) {
		this.topLevelTasks.add(task);
		this.pss.firePropertyChange(PROP_TASKLIST_ADD, null, task);
	    }

	} catch (Exception ex) {
	    Exceptions.printStackTrace(ex);
	} finally {
	    try {
		oIn.close();
	    } catch (IOException ex) {
		Exceptions.printStackTrace(ex);
	    }
	}
	return task;
    }//public Task load(FileObject fo) 

    private void loadTasks() {

	if (this.tasksLoaded) {
	    return;
	}
	
	this.tasksLoaded = true;
	FileObject[] entries = this.root.getChildren();
	for (FileObject fo : entries) {
	    try {
		DataObject dao = DataObject.find(fo);
		if (null != dao) {
		    this.doByTask.put(	dao.getLookup().lookup(Task.class), dao	);
		}
	    } catch (DataObjectNotFoundException ex) {
		Exceptions.printStackTrace(ex);
	    }//try
	}//for
    }//loadTasks()

    private void deleteTasks() {
	for (FileObject fo : this.root.getChildren()) {
	    if ("tsk".equalsIgnoreCase(fo.getExt())) {
		try {
		    fo.delete();
		} catch (IOException ex) {
		    Exceptions.printStackTrace(ex);
		}
	    }//if
	}//for
    }//private void deleteTasks()


}//class
