package biz.artemis.roadrunner.model3;

import biz.artemis.roadrunner.persistence.PersistanceBroker;
import biz.artemis.roadrunner.model3.ConfluenceServer;
import biz.artemis.roadrunner.model3.SpaceSyncDefinition;
import biz.artemis.roadrunner.model3.Persistable;

import javax.swing.*;
import java.util.*;
import java.io.File;

import org.apache.log4j.Logger;

/**
 * Settings that the user has selected including local and remote servers
 * and anything othe preferences.
 * <p/>
 * An external class should really only be calling getInstance() and save()
 * in terms of persistence.
 */
public class UserSettings extends Persistable {
    transient Logger log  = Logger.getLogger(this.getClass().getName());

    transient protected static final String USER_SETTINGS_FILE = "userSettings.xml";
    /**
     * singleton instance reference
     */
    private static UserSettings instance = null;
    protected TreeMap<String, ConfluenceServer> confluenceServersAliasMap = new TreeMap<String, ConfluenceServer>();
    protected TreeMap<String, ConfluenceServer> confluenceServersIdMap = new TreeMap<String, ConfluenceServer>();


    public static UserSettings getInstance() {
        if (instance != null) {
            return instance;
        }
        return load();
    }

    /**
     * load the UserSettings from the file, or return a new empty
     * obj if the file doens't exist
     *
     * @return
     */
    public static UserSettings load() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        File userSettingsFile = persistenceBroker.getDataFile(USER_SETTINGS_FILE);
        if (!userSettingsFile.exists()) {
            instance = new UserSettings();
            return instance;
        }
        UserSettings deserializedSettings = (UserSettings) persistenceBroker.load(USER_SETTINGS_FILE);
        if (deserializedSettings != null) {
            instance = deserializedSettings;
        } else {
            instance = new UserSettings();
        }
        return instance;
    }

    /**
     * save this UserSettings instance which is a singleton
     */
    public void save() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        persistenceBroker.save(this, USER_SETTINGS_FILE);
    }

    private UserSettings() {
    }

    public TreeMap<String, ConfluenceServer> getConfluenceServersAliasMap() {
        if (confluenceServersAliasMap == null) {
            confluenceServersAliasMap = new TreeMap<String, ConfluenceServer>();
        }
        return confluenceServersAliasMap;
    }

    public TreeMap<String, ConfluenceServer> getConfluenceServersIdMap() {
        if (confluenceServersIdMap == null) {
            confluenceServersIdMap = new TreeMap<String, ConfluenceServer>();
        }
        return confluenceServersIdMap;
    }

    public ConfluenceServer getConfluenceServerFromAlias(String serverAlias) {
        TreeMap<String, ConfluenceServer> serversMap = getConfluenceServersAliasMap();
        ConfluenceServer confServer = serversMap.get(serverAlias);
        return confServer;

    }

    public ConfluenceServer getConfluenceServer(JTextField serverAliasField) {
        String aliasText = serverAliasField.getText();
        if (aliasText == null) return null;
        return getConfluenceServerFromAlias(aliasText);
    }

    public List<SpaceSyncDefinition> getAllSyncs() {
        List<SpaceSyncDefinition> allSpaceSyncs = new ArrayList<SpaceSyncDefinition>();
        Map<String, ConfluenceServer> confServerMap = getConfluenceServersAliasMap();
        Collection<ConfluenceServer> allServers = confServerMap.values();
        for (Iterator<ConfluenceServer> confluenceServerIterator = allServers.iterator(); confluenceServerIterator.hasNext();) {
            ConfluenceServer confluenceServer = confluenceServerIterator.next();
            List<SpaceSyncDefinition> spaceSyncList = confluenceServer.getSpacesToSync();
            allSpaceSyncs.addAll(spaceSyncList);
        }
        return allSpaceSyncs;

    }

    /**
     * cycles through all the different servers and syncs to see which servers are involved in a sync
     * @return
     */
    public List<ConfluenceServer> getAllServersBeingSynced() {
        // gather all the servers which are being synced both from and to

        // the bag lets us add the same element multiple times but makes sure
        // we just have on coming out the other side
        Map<String, ConfluenceServer> serversBeingSyncedMap = new HashMap<String, ConfluenceServer>();
        List<SpaceSyncDefinition> allSpaceSyncs = new ArrayList<SpaceSyncDefinition>();

        Collection<ConfluenceServer> allConfluenceServers = confluenceServersIdMap.values();
        for (Iterator<ConfluenceServer> confluenceServerIterator = allConfluenceServers.iterator(); confluenceServerIterator.hasNext();) {
            ConfluenceServer confluenceServer = confluenceServerIterator.next();
            allSpaceSyncs.addAll(confluenceServer.getSpacesToSync());
        }

        for (int i = 0; i < allSpaceSyncs.size(); i++) {
            SpaceSyncDefinition spaceSyncDefinition = allSpaceSyncs.get(i);
            ConfluenceServer sourceServer = spaceSyncDefinition.getSourceServer();
            serversBeingSyncedMap.put(sourceServer.getId(), sourceServer);
            ConfluenceServer targetServer = spaceSyncDefinition.getTargetServer();
            serversBeingSyncedMap.put(targetServer.getId(), targetServer);
        }

        // finally convert to list
        List<ConfluenceServer> retList = new ArrayList(serversBeingSyncedMap.values());
        return retList;
    }


    /**
     * update the maps of the Confluence servers
     * @param confServer
     */
    public void updateServersMap(ConfluenceServer confServer) {
        confluenceServersAliasMap.put(confServer.getServerAlias(), confServer);
        confluenceServersIdMap.put(confServer.getId(), confServer);
    }

    public void removeSeverByAlias(String selectedAlias) {
        ConfluenceServer server = confluenceServersAliasMap.get(selectedAlias);
        if (server==null) {
            log.error("server alias entry was null when it should not have been: "+selectedAlias);
            return;
        }
        confluenceServersAliasMap.remove(selectedAlias);
        confluenceServersIdMap.remove(server.getId());

    }
}
