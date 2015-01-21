/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.overview;

import com.netbeansrcp.taskmodel.api.Task;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Administrator
 */
public class TaskChildFactory extends ChildFactory<Task> implements PropertyChangeListener {

    private Task task;

    TaskChildFactory(Task task) {
	this.task = task;
	task.addPropertyChangeListener(this);
    }

    @Override
    protected boolean createKeys(List<Task> arg0) {
	final long delay = 500;
	ProgressHandle handle = ProgressHandleFactory.createHandle("Creating subtasks...");

//	handle.start(100);
//	try {
//	    Thread.sleep(delay);
//	} catch (InterruptedException ex) {
//	}
//	handle.progress(25);
//	try {
//	    Thread.sleep(delay);
//	} catch (InterruptedException ex) {
//	}
//
//	handle.progress(50);
//	try {
//	    Thread.sleep(delay);
//	} catch (InterruptedException ex) {
//	}
//	handle.progress(75);
//	try {
//	    Thread.sleep(delay);
//	} catch (InterruptedException ex) {
//	}
//	handle.finish();
	arg0.addAll(this.task.getChildren());
	return true;
    }

    @Override
    protected Node[] createNodesForKey(Task task) {
	return new TaskNode[]{new TaskNode(task)};
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	if (Task.PROP_CHILDREN_ADD.equals(evt.getPropertyName())
		|| Task.PROP_CHILDREN_REMOVE.equals(evt.getPropertyName())) {

	    this.refresh(true);
	    
	}//if
    }//propertyChange

}//class
