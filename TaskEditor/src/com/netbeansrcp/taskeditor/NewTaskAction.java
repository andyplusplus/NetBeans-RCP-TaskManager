/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskeditor;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.util.Lookup;

public final class NewTaskAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
	directAction(e);
    }//public

    private void directAction(ActionEvent e){
        TaskManager taskMgr = Lookup.getDefault().lookup(TaskManager.class);
        if (null != taskMgr) {
            Task task = taskMgr.createTask().getLookup().lookup(Task.class);
            TaskEditorTopComponent win = TaskEditorTopComponent.findInstance(task);
            win.open();
            win.requestActive();
        }//if
    }

    
}
