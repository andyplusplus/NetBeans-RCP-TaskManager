/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskactions.wizards;

import com.netbeansrcp.taskmodel.api.Task.Priority;
import java.awt.Component;
import java.text.DateFormat;
import java.util.Date;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.HelpCtx;

public class NewTaskWizardPanel2 implements WizardDescriptor.Panel, DocumentListener {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    //private Component component;
    private NewTaskVisualPanel2 component;
    //
    private WizardDescriptor wd;
    private boolean valid = true;
    private ChangeSupport cs = new ChangeSupport(this);

    // Get the visual component for the panel. In this template, the component
    // is kept separate. This can be more efficient: if the wizard is created
    // but never displayed, or not all panels are displayed, it is better to
    // create only those which really need to be visible.
    @Override
    public Component getComponent() {
        if (component == null) {
            component = new NewTaskVisualPanel2();
            this.component.getDue().setText(DateFormat.getDateInstance().format(new Date()));
            //addChangeListener, removeChangeListener
            this.component.getDue().getDocument().addDocumentListener(this);
        }
        return component;
    }

    @Override
    public HelpCtx getHelp() {
        // Show no Help button for this panel:
        return HelpCtx.DEFAULT_HELP;
        // If you have context help:
        // return new HelpCtx(SampleWizardPanel1.class);
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return this.valid;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:
        // fireChangeEvent();
        // and uncomment the complicated stuff below.
    }

    @Override
    public final void addChangeListener(ChangeListener l) {
        this.cs.addChangeListener(l);
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
        this.cs.removeChangeListener(l);
    }
    /*
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
    public final void addChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.add(l);
    }
    }
    public final void removeChangeListener(ChangeListener l) {
    synchronized (listeners) {
    listeners.remove(l);
    }
    }
    protected final void fireChangeEvent() {
    Iterator<ChangeListener> it;
    synchronized (listeners) {
    it = new HashSet<ChangeListener>(listeners).iterator();
    }
    ChangeEvent ev = new ChangeEvent(this);
    while (it.hasNext()) {
    it.next().stateChanged(ev);
    }
    }
     */

    // You can use a settings object to keep track of state. Normally the
    // settings object will be the WizardDescriptor, so you can use
    // WizardDescriptor.getProperty & putProperty to store information entered by the user.
    @Override
    public void readSettings(Object settings) {
        this.wd = (WizardDescriptor) settings;
    }

    @Override
    public void storeSettings(Object settings) {

        this.wd.putProperty(NewTaskConstants.NAME, this.component.getNameField().getText());

        try {
            this.wd.putProperty(NewTaskConstants.DUE, DateFormat.getDateInstance().
                    parse(this.component.getDue().getText()));
        } catch (Exception e) {
        }

        Priority prio = null;
        switch (this.component.getPriority().getValue()) {
            case 0:
                prio = Priority.LOW;
                break;
            case 1:
                prio = Priority.MEDIUM;
                break;
            case 2:
                prio = Priority.HIGH;
                break;
        }
        this.wd.putProperty(NewTaskConstants.PRIORITY, prio);
        this.wd.putProperty(NewTaskConstants.PROGRESS, this.component.getProgress().getValue());
    }

    @Override//DocumentListener
    public void insertUpdate(DocumentEvent e) {
        this.changeDue();
    }

    @Override//DocumentListener
    public void removeUpdate(DocumentEvent e) {
        this.changeDue();
    }

    @Override//DocumentListener
    public void changedUpdate(DocumentEvent e) {
        this.changeDue();
    }

    private void changeDue() {
        boolean old = this.valid;
        try {
            DateFormat.getDateInstance().parse(this.component.getDue().getText());
            this.wd.putProperty("WizardPanel_errorMessage", null);
            this.valid = true;
        } catch (Exception ex) {
            this.wd.putProperty("WizardPanel_errorMessage", "Due date not formatted correctly!");
            this.valid = false;
        }
        if (old != this.valid) {
            this.cs.fireChange();
        }
    }//public
}
