/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeansrcp.preferences.api;

import java.beans.PropertyChangeListener;

/**
 *
 * @author Administrator
 */
public interface Preferences {

    String PATH_CHANGED = "path_changed";

    void setPersistencePath(String path);

    java.io.File getPersistencePath();

    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
