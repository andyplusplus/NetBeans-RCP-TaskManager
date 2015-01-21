/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.duelist;

import com.netbeansrcp.taskmodel.api.Task;
import com.netbeansrcp.taskmodel.api.TaskManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.loaders.DataObject;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author geertjan
 */
public class TaskChildren extends Children.Array implements PropertyChangeListener, ChangeListener {

    private long startTime;
    private long endTime;
    private JSpinner spinner;
    private TaskManager taskMgr;

    public TaskChildren(JSpinner spinner) {
        this.spinner = spinner;
        this.spinner.addChangeListener(this);
        this.taskMgr = Lookup.getDefault().lookup(TaskManager.class);
        if (null != this.taskMgr) {
            this.taskMgr.addPropertyChangeListener(this);
        }
    }

    @Override
    protected Collection<Node> initCollection() {
        Calendar cal = Calendar.getInstance();
        System.out.println((Integer) this.spinner.getValue());

        cal.set(Calendar.WEEK_OF_YEAR, (Integer) this.spinner.getValue());
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        this.startTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        this.endTime = cal.getTimeInMillis();
        List<Task> dueTasks = new ArrayList<Task>();
        if (null != this.taskMgr) {
            List<DataObject> topLevelTaskDaos = this.taskMgr.getTopLevelTasks();
            for (DataObject dao : topLevelTaskDaos) {
		Task topLevelTask = dao.getLookup().lookup(Task.class);
                this.findDueTasks(topLevelTask, dueTasks);
            }
        }
        Collection<Node> dueNodes = new ArrayList<Node>(dueTasks.size());
        for (Task task : dueTasks) {
            dueNodes.add(new TaskNode(task));
            task.addPropertyChangeListener(this);
        }
        return dueNodes;
    }

    private void findDueTasks(Task task, List<Task> dueTasks) {
        long dueTime = task.getDue().getTime();
        if (dueTime >= this.startTime && dueTime <= this.endTime) {
            dueTasks.add(task);
        }
        for (Task subTask : task.getChildren()) {
            this.findDueTasks(subTask, dueTasks);
        }
    }

    private void updateNodes() {
        remove(getNodes());
        add(initCollection().toArray(new Node[]{}));
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        if ((arg0.getSource() instanceof Task)
                && TaskManager.PROP_TASKLIST_ADD.equals(arg0.getPropertyName())
                || TaskManager.PROP_TASKLIST_REMOVE.equals(arg0.getPropertyName())) {
            this.updateNodes();
        }
        if ((arg0.getSource() instanceof TaskManager)
                && TaskManager.PROP_TASKLIST_ADD.equals(arg0.getPropertyName())
                || TaskManager.PROP_TASKLIST_REMOVE.equals(arg0.getPropertyName())) {
            this.updateNodes();
        }
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
        this.updateNodes();
    }

}
