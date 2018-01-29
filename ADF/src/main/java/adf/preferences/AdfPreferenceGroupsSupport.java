package adf.preferences;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Admin on 9/27/2016.
 */
abstract public class AdfPreferenceGroupsSupport {

    private Map<String, Enum> prefToGroupIDMap = new ConcurrentHashMap();

    private Map<Enum, List<GroupChangeListener>> groupIdToGroupChangeListenersMap = new ConcurrentHashMap();

    //
    //  Creating Instance
    //

    public AdfPreferenceGroupsSupport(Class clazz2) {
        Class<Enum> clazz = clazz2;
        for (Enum e : clazz.getEnumConstants()) {
            String value = e.toString();
            List<GroupChangeListener> groupChangeListeners = new CopyOnWriteArrayList<>();
            groupIdToGroupChangeListenersMap.put(e, groupChangeListeners);
            System.out.println(value);
        }

        for (Enum e : clazz.getEnumConstants()) {
            String value = e.toString();
            List<GroupChangeListener> groupChangeListeners = groupIdToGroupChangeListenersMap.get(e);
            System.out.println(value);
        }
    }

    protected void addGroupChangeListener(String pref, Enum groupID, GroupChangeListener groupChangeListener) {
        prefToGroupIDMap.put(pref, groupID);
        List<GroupChangeListener> groupChangeListeners = groupIdToGroupChangeListenersMap.get(groupID);
        groupChangeListeners.add(groupChangeListener);
    }

    protected void removeGroupChangeListener(Enum groupID, GroupChangeListener groupChangeListener) {
        List<GroupChangeListener> groupChangeListeners = groupIdToGroupChangeListenersMap.get(groupID);
        groupChangeListeners.remove(groupChangeListener);
    }

    protected void firePreferenceChanged(String preferenceName) {
        Enum groupID = prefToGroupIDMap.get(preferenceName);
        if (groupID == null) {
            // no listeners added yet
            return;
        }
        List<GroupChangeListener> groupChangeListeners = groupIdToGroupChangeListenersMap.get(groupID);
        for (GroupChangeListener groupChangeListener : groupChangeListeners) {
            groupChangeListener.groupPreferenceChanged(preferenceName);
        }
    }
}
