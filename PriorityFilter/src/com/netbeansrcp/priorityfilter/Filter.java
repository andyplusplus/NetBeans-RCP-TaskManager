/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.netbeansrcp.priorityfilter;

import com.netbeansrcp.overview.OverviewTopComponent;
import com.netbeansrcp.taskmodel.api.Task;
import org.openide.nodes.Node;
import org.openide.windows.WindowManager;

/**
 *
 * @author Administrator
 */
public class Filter {
    public static void filter(Task.Priority prio){
	OverviewTopComponent tc = (OverviewTopComponent) WindowManager.getDefault().findTopComponent("OverviewTopComponent");
	Node root = tc.getExplorerManager().getRootContext();
	if (root instanceof PriorityFilterNode) {
	    root = ((PriorityFilterNode) root).getOriginal();
	}
	Node newRoot = (null != prio) ? new PriorityFilterNode(root, prio) : root;
	tc.getExplorerManager().setRootContext(newRoot);
    }
}
