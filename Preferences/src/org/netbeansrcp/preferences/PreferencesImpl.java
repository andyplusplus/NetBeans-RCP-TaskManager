/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeansrcp.preferences;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import org.netbeansrcp.preferences.api.Preferences;
import org.openide.filesystems.LocalFileSystem;
import org.openide.util.NbPreferences;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Administrator
 */
@ServiceProvider(service=Preferences.class)
public class PreferencesImpl implements Preferences{

    private PropertyChangeSupport pss = new PropertyChangeSupport(this);

    @Override
    public void setPersistencePath(String path) {
	this.pss.firePropertyChange(PATH_CHANGED, this.getPersistencePath(), path);
	NbPreferences.forModule(PreferencesPanel.class).put("DIRECTORY", path);
    }

    @Override
    public File getPersistencePath() {
	String taskDir = NbPreferences.forModule(PreferencesPanel.class).
		get("DIRECTORY", new LocalFileSystem().getRootDirectory().getAbsolutePath());
	File file = new File(taskDir);
	return file;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
	this.pss.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
	this.pss.removePropertyChangeListener(listener);
    }

}
