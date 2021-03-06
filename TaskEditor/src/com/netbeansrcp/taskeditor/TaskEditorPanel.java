

/*
 * TaskEditorPanel.java
 *
 * Created on Jul 26, 2010, 4:10:01 PM
 */
package com.netbeansrcp.taskeditor;

import com.netbeansrcp.taskmodel.api.Task;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author nbuser
 */
public class TaskEditorPanel extends javax.swing.JPanel {

//    private TaskManager taskMgr;
//    private Task task = new TaskImpl();
//    public Task task = new TaskImpl();
    public Task task;
    private boolean avoidUpdataTask = false;  //In order to avoid dead loop
    public static final String PROP_TASK = "TASK";
    private PropertyChangeSupport pcs;

    /** Creates new form TaskEditorPanel */
    public TaskEditorPanel() {
//        if (null == this.taskMgr) {
//            this.taskMgr = Lookup.getDefault().lookup(TaskManager.class);
//        }
//        if (null != this.taskMgr) {
//            this.task = this.taskMgr.createTask();
//        }
        initComponents();
//        this.updateForm();
        this.pcs = new PropertyChangeSupport(this);
    }//public TaskEditorPanel()

    @Override
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (pcs == null) {
            pcs = new PropertyChangeSupport(this);
        }
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (pcs != null) {
            this.pcs.removePropertyChangeListener(listener);
        }
    }

    private void updateForm() {
        this.avoidUpdataTask = true;
        this.idTextField.setText(this.task.getId());
        this.parentIdTextField.setText(this.task.getParentId());
        this.nameTextField.setText(this.task.getName());
        this.dueDateTextField.setText(DateFormat.getDateInstance().format(this.task.getDue()));
        this.descriptionTextArea.setText(this.task.getDescr());
        if (Task.Priority.LOW.equals(this.task.getPrio())) {
            this.prioritySlider.setValue(0);
        } else if (Task.Priority.MEDIUM.equals(this.task.getPrio())) {
            this.prioritySlider.setValue(1);
        } else {
            this.prioritySlider.setValue(2);
        }
        this.progressSlider.setValue(this.task.getProgr());
        this.avoidUpdataTask = false;
    }//private void updateForm() 

    private void updateTask() {
        if (this.avoidUpdataTask) {
            return;
        }
        this.task.setName(this.nameTextField.getText());
        Date due = null;
        try {
            due =
                    DateFormat.getDateInstance().parse(this.dueDateTextField.getText());
        } catch (ParseException exception) {
            due = new Date();
        }
        this.task.setDue(due);
        if (!this.prioritySlider.getValueIsAdjusting()) {
            switch (this.prioritySlider.getValue()) {
                case 0:
                    this.task.setPrio(Task.Priority.LOW);
                    break;
                case 1:
                    this.task.setPrio(Task.Priority.MEDIUM);
                    break;
                case 2:
                    this.task.setPrio(Task.Priority.HIGH);
            }
        }
        this.task.setProgr(this.progressSlider.getValue());
        this.task.setDescr(this.descriptionTextArea.getText());
    }//updateTask()

    
    private DocumentListener docListener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent evt) {
            TaskEditorPanel.this.updateTask();
        }

        @Override
        public void removeUpdate(DocumentEvent evt) {
            TaskEditorPanel.this.updateTask();
        }

        @Override
        public void changedUpdate(DocumentEvent evt) {
            TaskEditorPanel.this.updateTask();
        }
    };//private DocumentListener docListener 


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField5 = new javax.swing.JTextField();
        idLabel = new javax.swing.JLabel();
        parentIdLabel = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        dueDateLabel = new javax.swing.JLabel();
        prirotiyLabel = new javax.swing.JLabel();
        progressLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        idTextField = new javax.swing.JTextField();
        parentIdTextField = new javax.swing.JTextField();
        nameTextField = new javax.swing.JTextField();
        dueDateTextField = new javax.swing.JTextField();
        prioritySlider = new javax.swing.JSlider();
        progressSlider = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();

        jTextField5.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.jTextField5.text")); // NOI18N

        idLabel.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.idLabel.text")); // NOI18N

        parentIdLabel.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.parentIdLabel.text")); // NOI18N

        nameLabel.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.nameLabel.text")); // NOI18N

        dueDateLabel.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.dueDateLabel.text")); // NOI18N

        prirotiyLabel.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.prirotiyLabel.text")); // NOI18N

        progressLabel.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.progressLabel.text")); // NOI18N

        descriptionLabel.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.descriptionLabel.text")); // NOI18N

        idTextField.setEditable(false);
        idTextField.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.idTextField.text")); // NOI18N
        idTextField.setEnabled(false);
        this.idTextField.getDocument().addDocumentListener(this.docListener);

        parentIdTextField.setEditable(false);
        parentIdTextField.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.parentIdTextField.text")); // NOI18N
        parentIdTextField.setEnabled(false);
        this.parentIdTextField.getDocument().addDocumentListener(this.docListener);

        nameTextField.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.nameTextField.text")); // NOI18N
        this.nameTextField.getDocument().addDocumentListener(this.docListener);

        dueDateTextField.setText(org.openide.util.NbBundle.getMessage(TaskEditorPanel.class, "TaskEditorPanel.dueDateTextField.text")); // NOI18N
        this.dueDateTextField.getDocument().addDocumentListener(this.docListener);

        prioritySlider.setMajorTickSpacing(1);
        prioritySlider.setMaximum(2);
        prioritySlider.setPaintTicks(true);
        prioritySlider.setSnapToTicks(true);
        prioritySlider.setValue(0);
        prioritySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                prioritySliderStateChanged(evt);
            }
        });

        progressSlider.setMajorTickSpacing(25);
        progressSlider.setMinorTickSpacing(5);
        progressSlider.setPaintLabels(true);
        progressSlider.setPaintTicks(true);
        progressSlider.setSnapToTicks(true);
        progressSlider.setValue(0);
        progressSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                progressSliderStateChanged(evt);
            }
        });

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        jScrollPane1.setViewportView(descriptionTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dueDateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dueDateTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(prirotiyLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(prioritySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(progressLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progressSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(descriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(parentIdLabel)
                            .addComponent(idLabel)
                            .addComponent(nameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(idTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(nameTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                            .addComponent(parentIdTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(parentIdLabel)
                    .addComponent(parentIdTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dueDateLabel)
                    .addComponent(dueDateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(prirotiyLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(prioritySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(progressLabel))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void prioritySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_prioritySliderStateChanged
        this.updateTask();
    }//GEN-LAST:event_prioritySliderStateChanged

    private void progressSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_progressSliderStateChanged
        this.updateTask();
    }//GEN-LAST:event_progressSliderStateChanged

    public void updateTask(Task task) {
        Task oldTask = this.task;
        this.task = task;
        this.pcs.firePropertyChange(PROP_TASK, oldTask, this.task);
        this.updateForm();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JLabel dueDateLabel;
    private javax.swing.JTextField dueDateTextField;
    private javax.swing.JLabel idLabel;
    private javax.swing.JTextField idTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JLabel parentIdLabel;
    private javax.swing.JTextField parentIdTextField;
    private javax.swing.JSlider prioritySlider;
    private javax.swing.JLabel prirotiyLabel;
    private javax.swing.JLabel progressLabel;
    private javax.swing.JSlider progressSlider;
    // End of variables declaration//GEN-END:variables
}
