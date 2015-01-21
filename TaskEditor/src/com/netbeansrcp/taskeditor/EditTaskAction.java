/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskeditor;

import com.netbeansrcp.taskmodel.api.Task;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

/**
 *
 * @author Administrator
 */
public class EditTaskAction extends AbstractAction
	implements LookupListener, Presenter.Toolbar, ActionListener {

    private Lookup.Result<Task> result;
    private JButton toolbarBtn;

    public EditTaskAction() {
	this(Utilities.actionsGlobalContext());
    }

    public EditTaskAction(Lookup lookup) {
	super("Edit Task...", new ImageIcon(ImageUtilities.loadImage(
		"com/netbeansrcp/taskeditor/Edit_16_16.png")));
	this.result = lookup.lookupResult(Task.class);
	this.result.addLookupListener(this);
	this.resultChanged(new LookupEvent(result));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	if (null != this.result && 0
		< this.result.allInstances().size()) {
	    Task task =
		    this.result.allInstances().iterator().next();
	    EditTaskAction.openInTaskEditor(task);
	}
    }

    public static void openInTaskEditor(Task task) {
	TaskEditorTopComponent win = TaskEditorTopComponent.findInstance(task);
	win.open();
	win.requestActive();
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
    public Component getToolbarPresenter() {
	if (null == this.toolbarBtn) {
	    this.toolbarBtn = new JButton(this);
	}
	return this.toolbarBtn;
    }

    public Action createContextAwareInstance(Lookup lkp) {
	return new EditTaskAction(lkp);
    }

    public JMenuItem getPopupPresenter() {
	return new JMenuItem(this);
    }
}
