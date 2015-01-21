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
public class PriorityFilterNode extends FilterNode{
    public PriorityFilterNode(Node arg0, Task.Priority prio){
	super(arg0, new PriorityFilterChildren(arg0, prio));
    }

    @Override
    public Node getOriginal(){
	return super.getOriginal();
    }
}
