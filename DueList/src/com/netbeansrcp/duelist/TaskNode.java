/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.duelist;

import com.netbeansrcp.taskmodel.api.Task;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author geertjan
 */
public class TaskNode extends AbstractNode {

    public TaskNode(Task task) {
        super(Children.LEAF, Lookups.singleton(task));
        this.setName(task.getId());
        this.setDisplayName(task.getName());
        task.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (Task.PROP_NAME.equals(evt.getPropertyName())) {
                    setDisplayName(evt.getNewValue() + "");
                }
            }
        });
    }

}
