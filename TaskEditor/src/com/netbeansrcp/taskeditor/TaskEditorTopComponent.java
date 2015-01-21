/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.taskeditor;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//com.netbeansrcp.taskeditor//TaskEditor//EN",
autostore = false)
public final class TaskEditorTopComponent extends TopComponent {

    private static TaskEditorTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "TaskEditorTopComponent";
    private InstanceContent ic = new InstanceContent();
    private PropertyChangeListener taskChangeListener = new ListenForTaskChanges();
    private static TaskManager taskMgr;
    private static Map<Task, TaskEditorTopComponent> tcByTask = new HashMap<Task, TaskEditorTopComponent>();

    private TaskEditorTopComponent() {
        this(Lookup.getDefault().lookup(TaskManager.class));
    }

    private TaskEditorTopComponent(TaskManager taskMgr) {
        this((taskMgr != null) ? taskMgr.createTask().getLookup().lookup(Task.class) : null);
    }

    public TaskEditorTopComponent(Task task) {
	fdbg("TaskEditorTopComponent");
        initComponents();
        setName(NbBundle.getMessage(TaskEditorTopComponent.class, "CTL_TaskEditorTopComponent"));
        setToolTipText(NbBundle.getMessage(TaskEditorTopComponent.class, "HINT_TaskEditorTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
//        this.taskEditorPanel1.addPropertyChangeListener(taskChangeListener);
        this.taskEditorPanel1.updateTask(task);
        this.ic.add(this.taskEditorPanel1.task);
        this.associateLookup(new AbstractLookup(this.ic));
        tcByTask.put(task, this);
    }

    private class ListenForTaskChanges implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent arg0) {
	    fdbg("ListenForTaskChanges propertyChange");
            if (TaskEditorPanel.PROP_TASK.equals(arg0.getPropertyName())) {
                ArrayList<Task> newContent = new ArrayList<Task>();
                newContent.add(TaskEditorTopComponent.this.taskEditorPanel1.task);
                TaskEditorTopComponent.this.ic.set(newContent, null);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        taskEditorPanel1 = new com.netbeansrcp.taskeditor.TaskEditorPanel();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(taskEditorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(taskEditorPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.netbeansrcp.taskeditor.TaskEditorPanel taskEditorPanel1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized TaskEditorTopComponent getDefault() {
	fdbg("getDefault");
        if (instance == null) {
            instance = new TaskEditorTopComponent();
        }
        return instance;
    }

    public static TaskEditorTopComponent findInstance(Task task) {
	fdbg("findInstance(Task task)");
        TaskEditorTopComponent tc = tcByTask.get(task);
        if (null == tc) {
            tc = new TaskEditorTopComponent(task);
        }
        if (null == taskMgr) {
            taskMgr = Lookup.getDefault().lookup(TaskManager.class);
            taskMgr.addPropertyChangeListener(new ListenForRemovedNodes());
        }
        return tc;
    }

    private static class ListenForRemovedNodes implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent arg0) {
	    fdbg("ListenForRemovedNodes  propertyChange");
            if (TaskManager.PROP_TASKLIST_REMOVE.equals(arg0.getPropertyName())) {
                Task task = (Task) arg0.getNewValue();
                TaskEditorTopComponent tc = tcByTask.get(task);
                if (null != tc) {
                    tc.close();
                    tcByTask.remove(task);
                }//if (null != tc) {
            }//if (TaskManager.PROP_TASKLIST_REMOVE
        }//public void propertyChange(PropertyChangeEvent arg0)
    }

    /**
     * Obtain the TaskEditorTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized TaskEditorTopComponent findInstance() {
	fdbg("findInstance()");
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(TaskEditorTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof TaskEditorTopComponent) {
            return (TaskEditorTopComponent) win;
        }
        Logger.getLogger(TaskEditorTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID
                + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
	fdbg("componentOpened");
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    private static void fdbg(String info){
	System.out.println(" ********* " + info);
    }

}
