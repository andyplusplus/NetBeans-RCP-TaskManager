/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.netbeansrcp.overview;

import com.netbeansrcp.taskactions.SaveAction;
import com.netbeansrcp.taskmodel.api.Task;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Action;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author geertjan
 */
public class TaskNode extends AbstractNode implements PropertyChangeListener, LookupListener {
    //For Icon Badging
    private Lookup.Result<SaveCookie> result;
    private boolean saveable = false;

    public TaskNode(Task task) {
	this(task, Lookups.singleton(task));
    }

    public TaskNode(Task task, Lookup lookup) {
        //super(new TaskChildren(task), Lookups.singleton(task));
	super(	    Children.create(new TaskChildFactory(task), true),	    lookup);
        this.setName(task.getId());
        this.setDisplayName(task.getName());
        this.setIconBaseWithExtension("com/netbeansrcp/overview/Task.png");
        task.addPropertyChangeListener(this);
	//For Icon Badging //TODO: ERROR code
//	this.result = lookup.lookup(SaveCookie.class);//TODO: P200, code difference here
//	this.result.addLookupListener(this);
    }

    @Override
    public String getHtmlDisplayName() {
        String html = "<font color='";
        Task task = this.getLookup().lookup(Task.class);
        switch (task.getPrio()) {
            case LOW:
                html += "0000FF";//blue
                break;
            case MEDIUM:
                html += "000000";//black
                break;
            case HIGH:
                html += "FF0000";//red
                break;
        }
        html += "'>" + task.getName() + "</font>";
        return html;
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        if (Task.PROP_NAME.equals(arg0.getPropertyName())
                || Task.PROP_PRIO.equals(arg0.getPropertyName())) {
            setDisplayName(arg0.getNewValue() + "");
        }
    }


    static List<? extends Action> registredActions;

    protected static List<? extends Action> getRegistredActions() {
	if (registredActions == null) {
	    registredActions =
		    Utilities.actionsForPath("Tasks/Nodes/Task/Actions");
	}
	return registredActions;
    }

    @Override
    public Action getPreferredAction() {
	return Utilities.actionsForPath("Tasks/Nodes/Task/PreferredAction").get(0);
    }

    @Override
    public Action[] getActions(boolean context) {
	List<Action> actions = new ArrayList<Action>();
	actions.addAll(getRegistredActions());
	actions.addAll(Arrays.asList(super.getActions(context)));
	//For Icon Badging
//	actions.add(new SaveAction());	//TODO: ErrorCode
//	actions.add(null);
	//End Icon Badging
	return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public void resultChanged(LookupEvent le) {
	SaveCookie save = this.getLookup().lookup(SaveCookie.class);
	if (false == this.saveable && null != save) {
	    this.saveable = true;
	    this.fireIconChange();
	}
	if (this.saveable && null == save) {
	    this.saveable = false;
	    this.fireIconChange();
	}
    }//public

    @Override
    public Image getIcon(int arg0) {
	Image std = super.getIcon(arg0);
	if (this.saveable) {
	    Image badge = ImageUtilities.loadImage("com/netbeansrcp/overview/Orange Ball small.png");
	    return ImageUtilities.mergeImages(std, badge, 5, 5);
	}
	return std;
    }//public

}//class
