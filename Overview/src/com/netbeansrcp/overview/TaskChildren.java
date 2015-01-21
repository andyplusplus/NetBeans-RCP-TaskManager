/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.overview;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author geertjan
 */
class TaskChildren extends Children.Keys<Task> implements PropertyChangeListener {

    private Task task;

    public TaskChildren(Task task) {
        this.task = task;
        task.addPropertyChangeListener(this);
    }

    @Override
    protected void addNotify() {
        super.addNotify();
	try{Thread.sleep(30000);}catch(InterruptedException ex){};
        this.setKeys(task.getChildren());
    }

    @Override
    protected Node[] createNodes(Task arg0) {
        return new TaskNode[]{new TaskNode(arg0)};
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        if (TaskManager.PROP_TASKLIST_ADD.equals(arg0.getPropertyName())
                || TaskManager.PROP_TASKLIST_REMOVE.equals(arg0.getPropertyName())) {
            this.setKeys(this.task.getChildren());
        }
    }
    
}
