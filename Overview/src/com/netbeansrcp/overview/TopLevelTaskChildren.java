/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.overview;

import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author geertjan
 */
public class TopLevelTaskChildren extends Children.Keys<DataObject> implements PropertyChangeListener {

    private TaskManager taskMgr;

    public TopLevelTaskChildren() {
        this.taskMgr = Lookup.getDefault().lookup(TaskManager.class);
        if (null != this.taskMgr) {
            this.taskMgr.addPropertyChangeListener(this);
        }
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        if (null != this.taskMgr) {
            this.setKeys(this.taskMgr.getTopLevelTasks());
        }
    }

    //protected Node[] createNodes(Task arg0) {
    @Override
    protected Node[] createNodes(DataObject arg0) {
        //return new Node[]{new TaskNode(arg0)};
	return new Node[] {arg0.getNodeDelegate() };
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        if (null != this.taskMgr) {
            if (TaskManager.PROP_TASKLIST_ADD.equals(arg0.getPropertyName())
                    || TaskManager.PROP_TASKLIST_REMOVE.equals(arg0.getPropertyName())) {
                this.setKeys(this.taskMgr.getTopLevelTasks());
            }
        }//if (null != this.taskMgr)
    }//public void propertyChange(PropertyChangeEvent arg0)
    
}//class
