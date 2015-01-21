/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.priorityfilter;

import com.netbeansrcp.taskmodel.api.Task;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;

/**
 *
 * @author Administrator
 */
public class PriorityFilterChildren extends FilterNode.Children {

    private Task.Priority prio;

    public PriorityFilterChildren(Node arg0, Task.Priority prio) {
	super(arg0);
	this.prio = prio;
    }

    @Override
    protected Node copyNode(Node arg0) {
	return new PriorityFilterNode(arg0, this.prio);
    }

    @Override
    protected Node[] createNodes(Node arg0) {
	Task task = arg0.getLookup().lookup(Task.class);
	if (null != task && 0
		>= this.prio.compareTo(task.getPrio())) {
	    return new Node[]{this.copyNode(arg0)};
	}
	return new Node[]{};
    }//protected Node[] createNodes(Node arg0)
    
}//class
