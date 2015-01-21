/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskmodel.api;

/**
 *
 * @author nbuser
 */
public interface TaskManager {

    org.openide.loaders.DataObject createTask();

    Task createTask(String name, String parentId);

    void removeTask(String id);

    java.util.List<org.openide.loaders.DataObject> getTopLevelTasks();

    Task getTask(String id);

    // Virutal File System
    org.openide.filesystems.FileObject	save(Task task) ;

    void save(Task task,  org.openide.filesystems.FileObject fo);
    Task load(org.openide.filesystems.FileObject fo);


    // PROP
    static final String PROP_TASKLIST_ADD = "TASK_LIST_ADD";
    static final String PROP_TASKLIST_REMOVE = "TASK_LIST_REMOVE";

    public void addPropertyChangeListener(java.beans.PropertyChangeListener listener);
    public void removePropertyChangeListener(java.beans.PropertyChangeListener listener);

}
