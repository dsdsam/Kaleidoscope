package adf.app;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.SwingPropertyChangeSupport;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.concurrent.ConcurrentHashMap;


/**
 * A set of application run-time properties.
 * Applications can set/get the properties via
 * the set of methods available.
 */
abstract class AppProperties {

    // System keys
    public static final String USER_HOME = "user.home";
    public static final String USER_DIR = "user.dir";

    // System properties
    public static final String USER_DATA_HOME = System.getProperty(USER_HOME);
    public static final String INSTALLATION_DIRECTORY = System.getProperty(USER_DIR);
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");


    private static ConcurrentHashMap properties = new ConcurrentHashMap();
    private SwingPropertyChangeSupport changeSupport;

    public static void removeAllProperties() {
        properties.clear();
    }

    /**
     * Returns the value for key.
     *
     * @param key the desired key
     * @return the value for <code>key</code>
     * @see java.util.Hashtable#get
     */
    public static Object get(Object key) {
        Object value = properties.get(key);
        return value;
    }

    /**
     * Sets the value of <code>key</code> to <code>value</code>.
     * If <code>key</code> is a string and the new value isn't
     * equal to the old one, fire a <code>PropertyChangeEvent</code>.
     * If value is <code>null</code>, the key is removed from the table.
     *
     * @param key   the unique <code>Object</code> who's value will be used
     *              to retrieve the mcln.data value associated with it
     * @param value the new <code>Object</code> to store as mcln.data under
     *              that key
     * @return the previous <code>Object</code> value, or <code>null</code>
     * @see java.util.Hashtable#put
     */
    public static Object put(Object key, Object value) {
        Object oldValue = (value == null) ?
                properties.remove(key) : properties.put(key, value);
        return oldValue;
    }

    /**
     * Puts all of the key/value pairs in the database and
     * unconditionally generates one <code>PropertyChangeEvent</code>.
     * The events oldValue and newValue will be <code>null</code> and its
     * <code>propertyName</code> will be "UIDefaults".
     *
     * @param keyValueList an array of key/value pairs
     * @see #put
     * @see java.util.Hashtable#put
     */
    public static void putProps(Object[] keyValueList) {
        for (int i = 0; i < keyValueList.length; i += 2) {
            put(keyValueList[i], keyValueList[i + 1]);
        }
    }

    // ===========================================================
    //   c o n v e n i e n c e    m e t h o d s
    // ===========================================================

    /**
     * If the value of <code>key</code> is a <code>Font</code> return it,
     * otherwise return <code>null</code>.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is a <code>Font</code>,
     * return the <code>Font</code> object; otherwise return
     * <code>null</code>
     */
    public static Font getFont(Object key) {
        Object value = get(key);
        return (value instanceof Font) ? (Font) value : null;
    }

    /**
     * If the value of <code>key</code> is a <code>Color</code> return it,
     * otherwise return <code>null</code>.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is a <code>Color</code>,
     * return the <code>Color</code> object; otherwise return
     * <code>null</code>
     */
    public static Color getColor(Object key) {
        Object value = get(key);
        return (value instanceof Color) ? (Color) value : null;
    }

    /**
     * If the value of <code>key</code> is an <code>Icon</code> return it,
     * otherwise return <code>null</code>.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is an <code>Icon</code>,
     * return the <code>Icon</code> object; otherwise return
     * <code>null</code>
     */
    public static Icon getIcon(Object key) {
        Object value = get(key);
        return (value instanceof Icon) ? (Icon) value : null;
    }

    /**
     * If the value of <code>key</code> is a <code>Border</code> return it,
     * otherwise return <code>null</code>.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is a <code>Border</code>,
     * return the <code>Border</code> object; otherwise return
     * <code>null</code>
     */
    public static Border getBorder(Object key) {
        Object value = get(key);
        return (value instanceof Border) ? (Border) value : null;
    }

    /**
     * If the value of <code>key</code> is a <code>String</code> return it,
     * otherwise return <code>null</code>.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is a <code>String</code>,
     * return the <code>String</code> object; otherwise return
     * <code>null</code>
     */
    public static String getString(Object key) {
        Object value = get(key);
        return (value instanceof String) ? (String) value : null;
    }

    public static boolean getBoolean(Object key) {
        Object value = get(key);
        if (!(value instanceof String) || (((String) value).equals("false")))
            return false;
        else
            return true;
    }

    /**
     * If the value of <code>key</code> is an <code>Integer</code> return its
     * integer value, otherwise return 0.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is an <code>Integer</code>,
     * return its value, otherwise return 0
     */
    public static int getInt(Object key) {
        Object value = get(key);
        return (value instanceof Integer) ? ((Integer) value).intValue() : 0;
    }

    /**
     * If the value of <code>key</code> is an <code>Insets</code> return it,
     * otherwise return <code>null</code>.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is an <code>Insets</code>,
     * return the <code>Insets</code> object; otherwise return
     * <code>null</code>
     */
    public static Insets getInsets(Object key) {
        Object value = get(key);
        return (value instanceof Insets) ? (Insets) value : null;
    }

    /**
     * If the value of <code>key</code> is a <code>Dimension</code> return it,
     * otherwise return <code>null</code>.
     *
     * @param key the desired key
     * @return if the value for <code>key</code> is a <code>Dimension</code>,
     * return the <code>Dimension</code> object; otherwise return
     * <code>null</code>
     */
    public static Dimension getDimension(Object key) {
        Object value = get(key);
        return (value instanceof Dimension) ? (Dimension) value : null;
    }

    // ===========================================================
    //   adding Property Change Listeners
    // ===========================================================

    /**
     * Adds a <code>PropertyChangeListener</code> to the listener list.
     * The listener is registered for all properties.
     * <p>
     * A <code>PropertyChangeEvent</code> will get fired whenever a default
     * is changed.
     *
     * @param listener the <code>PropertyChangeListener</code> to be added
     * @see java.beans.PropertyChangeSupport
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new SwingPropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a <code>PropertyChangeListener</code> from the listener list.
     * This removes a <code>PropertyChangeListener</code> that was registered
     * for all properties.
     *
     * @param listener the <code>PropertyChangeListener</code> to be removed
     * @see java.beans.PropertyChangeSupport
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    /**
     * Support for reporting bound property changes.  If oldValue and
     * newValue are not equal and the <code>PropertyChangeEvent</code>x
     * listener list isn't empty, then fire a
     * <code>PropertyChange</code> event to each listener.
     *
     * @param propertyName the programmatic name of the property
     *                     that was changed
     * @param oldValue     the old value of the property
     * @param newValue     the new value of the property
     * @see java.beans.PropertyChangeSupport
     */

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }
}
