/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskfilesupport;

import com.netbeansrcp.overview.TaskNode;
import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

public class TopLevelTaskDataObject extends MultiDataObject
    implements PropertyChangeListener{

    private InstanceContent ic;
    private Lookup lookup;
    private TaskManager taskMgr;

    //TODO: Book Code has Some Problem, No "TopLevelTaskDataLoader" Exist
    //INFO: replace "TopLevelTaskDataLoader" with "MultiFileLoader"
    public TopLevelTaskDataObject(FileObject pf, MultiFileLoader loader)
	    throws DataObjectExistsException, IOException {

	super(pf, loader);
	this.ic = new InstanceContent();
	this.lookup = new AbstractLookup(this.ic);
	this.taskMgr = Lookup.getDefault().
		lookup(TaskManager.class);
	if (null != this.taskMgr) {
	    Task task = this.taskMgr.load(pf);
	    task.addPropertyChangeListener(this);
	    this.ic.add(task);
	}//if
    }

    @Override
    protected Node createNodeDelegate() {
	return new TaskNode(this.lookup.lookup(Task.class), this.lookup);
    }

    @Override
    public Lookup getLookup() {
	return this.lookup;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	SaveCookie saveCookie = this.getLookup().lookup(SaveCookie.class);
	if (null == saveCookie) {
	    this.ic.add(new TopLevelTaskSaveCookie());
	}
    }

    private class TopLevelTaskSaveCookie implements SaveCookie {

	@Override
	public void save() throws IOException {
	    FileObject fo = TopLevelTaskDataObject.this.getPrimaryFile();
	    Task task = TopLevelTaskDataObject.this.getLookup().lookup(Task.class);
	    TopLevelTaskDataObject.this.taskMgr.save(task, fo);
	    TopLevelTaskDataObject.this.ic.remove(this);
	}
    }//class TopLevelTaskSaveCookie


}//class TopLevelTaskDataObject
