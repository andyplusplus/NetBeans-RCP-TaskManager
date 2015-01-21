/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.priorityfilter;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.actions.Presenter;

public final class FilterTasksAction extends AbstractAction implements
	Presenter.Toolbar {

    private JButton toolbarBtn;

    @Override
    public void actionPerformed(ActionEvent e) {
	FilterPanel filterPanel = new FilterPanel();
	DialogDescriptor dd = new DialogDescriptor(filterPanel, "Priority Filter", true,
		DialogDescriptor.OK_CANCEL_OPTION, DialogDescriptor.OK_OPTION, null);

	Object result = DialogDisplayer.getDefault().notify(dd);
	if (null != result && DialogDescriptor.OK_OPTION == result) {
	    Filter.filter(filterPanel.getPriority());
	}
    }//public

    @Override
    public Component getToolbarPresenter() {
	if (null == this.toolbarBtn) {
	    this.toolbarBtn = new JButton(this);
	}
	return this.toolbarBtn;
    }//public
    
}
