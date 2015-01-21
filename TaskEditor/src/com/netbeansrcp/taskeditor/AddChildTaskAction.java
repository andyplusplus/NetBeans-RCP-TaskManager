/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskeditor;

import com.netbeansrcp.taskactions.wizards.NewTaskWizardAction;
import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.actions.Presenter;
import org.openide.util.actions.SystemAction;

public class AddChildTaskAction extends AbstractAction implements
	ContextAwareAction, LookupListener, Presenter.Popup, Presenter.Toolbar, ActionListener {

    private Lookup.Result<Task> result;
    private static final boolean usingWizardAction = true;

    public AddChildTaskAction() {
	this("Create New Task...");
    }

    private AddChildTaskAction(Lookup lookup) {
	this("Create New Subtask...");
	this.result = lookup.lookupResult(Task.class);
	this.result.addLookupListener(this);
	this.resultChanged(new LookupEvent(result));
    }

    private AddChildTaskAction(String label) {
	super(label, new ImageIcon(ImageUtilities.loadImage("com/netbeansrcp/taskeditor/AddChild_16_16.png")));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (usingWizardAction){
	    wizardAction(e);
	}else{
	    directAction(e);
	}
    }//function

    private void wizardAction(ActionEvent e) {
	Task task = null;
	if (null != this.result && 0 < this.result.allInstances().size()) {
	    task = this.result.allInstances().iterator().next();
	    SystemAction.get(NewTaskWizardAction.class).performAction(task.getId());
	} else {
	    SystemAction.get(NewTaskWizardAction.class).performAction();
	}
    }

    //Add action directly
    private void directAction(ActionEvent e){
	TaskManager taskMgr = Lookup.getDefault().lookup(TaskManager.class);
	Task task = null;
	if (null != this.result && 0
		< this.result.allInstances().size()) {
	    task = this.result.allInstances().iterator().next();
	    task = taskMgr.createTask("", task.getId());
	} else {
	    task = taskMgr.createTask().getLookup().lookup(Task.class);
	}
	EditTaskAction.openInTaskEditor(task);
    }//function



    @Override
    public Action createContextAwareInstance(Lookup arg0) {
	return new AddChildTaskAction(arg0);
    }

    @Override
    public void resultChanged(LookupEvent arg0) {
	if (this.result.allInstances().size() > 0) {
	    this.setEnabled(true);
	} else {
	    this.setEnabled(false);
	}
    }

    @Override
    public JMenuItem getPopupPresenter() {
	return new JMenuItem(this);
    }

    @Override
    public Component getToolbarPresenter() {
	return new JButton(this);
    }
}
