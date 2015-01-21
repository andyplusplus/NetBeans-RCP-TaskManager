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
import org.openide.util.ContextAwareAction;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.actions.Presenter;

public class CloneTaskAction extends AbstractAction implements
	ContextAwareAction, LookupListener, Presenter.Popup,
	Presenter.Toolbar, ActionListener {

    private Lookup.Result<Task> result;

    public CloneTaskAction() {
	this(Utilities.actionsGlobalContext());
    }

    private CloneTaskAction(Lookup lookup) {
	this("Copy Task...");
	this.result = lookup.lookupResult(Task.class);
	this.result.addLookupListener(this);
	this.resultChanged(new LookupEvent(result));
	this.resultChanged(null);
    }

    private CloneTaskAction(String label) {
	super(label, new ImageIcon(ImageUtilities.loadImage("com/netbeansrcp/taskeditor/Clone_16_16.png")));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	if (null != this.result && 0
		< this.result.allInstances().size()) {
	    Task original =
		    this.result.allInstances().iterator().next();
	    TaskManager taskMgr =
		    Lookup.getDefault().lookup(TaskManager.class);
	    Task newTask = null;
	    String pId = original.getParentId();
	    if (null != pId && !"".equals(pId)) {
		newTask = taskMgr.createTask(original.getName(), original.getParentId());
	    } else {
		newTask = taskMgr.createTask().getLookup().lookup(Task.class);
		newTask.setName(original.getName());
	    }
	    newTask.setDescr(original.getDescr());
	    newTask.setDue(original.getDue());
	    newTask.setPrio(original.getPrio());
	    newTask.setProgr(original.getProgr());
	    EditTaskAction.openInTaskEditor(newTask);
	}
    }

    @Override
    public Action createContextAwareInstance(Lookup arg0) {
	return new CloneTaskAction(arg0);
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
