/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskeditor;

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
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

public class DelTaskAction extends AbstractAction implements
	ContextAwareAction, LookupListener, Presenter.Popup,
	Presenter.Toolbar, ActionListener {

    private Lookup.Result<Task> result;
    private JButton toolbarBtn;

    public DelTaskAction() {
	this(Utilities.actionsGlobalContext());
    }

    public DelTaskAction(Lookup lookup) {
	super("Delete Task...", new ImageIcon(ImageUtilities.loadImage("com/netbeansrcp/taskeditor/Delx_16_16.png")));
	this.result = lookup.lookupResult(Task.class);
	this.result.addLookupListener(this);
	this.resultChanged(new LookupEvent(result));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	if (null != this.result && 0 < this.result.allInstances().size()) {
	    Task task = this.result.allInstances().iterator().next();
	    TaskManager taskMgr = Lookup.getDefault().lookup(TaskManager.class);

	    if (null != taskMgr && null != task) {
		if (DialogDescriptor.YES_OPTION == isDelTaskDialogConfirmed(task)  ){
		    taskMgr.removeTask(task.getId());
		}
	    }//if

	}//if
    }//public

    //p206 Dialog Confirm
    private Object isDelTaskDialogConfirmed(Task task) {
	String msg = "Do you really want to delete the task " + task.getId() + " ?";
	NotifyDescriptor d = new NotifyDescriptor.Confirmation(msg, "Delete Task",
		NotifyDescriptor.YES_NO_OPTION, NotifyDescriptor.WARNING_MESSAGE);

	d.setValue(NotifyDescriptor.NO_OPTION);
	Object res = DialogDisplayer.getDefault().notify(d);
	if ( !(null != res && DialogDescriptor.YES_OPTION == res)   ) {
	    res = DialogDescriptor.NO_OPTION;
	}
	return res;
    }//public

    @Override
    public Action createContextAwareInstance(Lookup arg0) {
	return new DelTaskAction(arg0);
    }

    @Override
    public void resultChanged(LookupEvent arg0) {
	if (this.result.allInstances().size() > 0) {
	    this.setEnabled(true);
	    if (null != this.toolbarBtn) {
		this.toolbarBtn.setEnabled(true);
	    }
	} else {
	    this.setEnabled(false);
	    if (null != this.toolbarBtn) {
		this.toolbarBtn.setEnabled(false);
	    }
	}
    }

    @Override
    public JMenuItem getPopupPresenter() {
	return new JMenuItem(this);
    }

    @Override
    public Component getToolbarPresenter() {
	if (null == this.toolbarBtn) {
	    this.toolbarBtn = new JButton(this);
	}
	return this.toolbarBtn;
    }
}
