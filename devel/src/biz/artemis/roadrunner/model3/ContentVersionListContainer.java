package biz.artemis.roadrunner.model3;

import biz.artemis.roadrunner.persistence.PersistanceBroker;
import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;

import java.util.*;
import java.io.File;

import org.apache.log4j.Logger;

/**
 * Contains the recent syncs which will be used for comparison
 * determining what content needs updating on the local server
 */
public class ContentVersionListContainer extends Persistable {
    transient Logger log  = Logger.getLogger(this.getClass().getName());
    
    /**
     * HashMap(String, String) - (serverId :: sourceSpaceName :: targetServerId :: CONTENT_TYPE :: resourceId(page or attachment), String versionId)
     * <p/>
     * this map is useful becuase it will make it easy to match up content pointers on a live server and their versions with what they used to be in the 'recentSyncs'
     */
    Map<String, String> syncsContentStringVersionsMap = null;


    /**
     * this List gives the ContentEntityForSync object which contain metadata for the final content entities that we want to sync (note ContentEntity here
     * has nothing to do with the Confluence objects which let you persist data, just using it as a good name)
     */
    transient Map<String, ContentEntityForSync> contentEntitiesForSyncMap = null;

    private static final String RECENT_SYNCS_FILE = "recentSyncsFile.xml";
    private List<List> contentBySpace;


//    public static ContentVersionListContainer getRecentSyncs() {
//        return loadRecentSyncsCVC();
//    }


    /**
     * creates entries of ContentEntityForSync
     *
     * @param spaceSyncDefinition
     * @param contentId
     * @param contentType
     * @param version
     */
    public void addSourceContent(SpaceSyncDefinition spaceSyncDefinition, String contentId, String contentType, Object contentForXmlRpc, String version) {
        String contentKeyString = createContentKeyString(spaceSyncDefinition, contentId, contentType, version);
        getSyncsContentStringVersionsMap().put(contentKeyString, version);
        ContentEntityForSync entity = ContentEntityForSync.createFromSyncStorageString(spaceSyncDefinition, contentId, contentType, contentForXmlRpc);
        getContentEntitiesForSyncMap().put(contentKeyString, entity);

    }

    /**
     * create entries for the contentEntity and version
     * @param contentForSyncKey
     * @param contentEntityForSync
     * @param version
     */
    public void addSourceContent(String contentForSyncKey, ContentEntityForSync contentEntityForSync, String version) {
        getContentEntitiesForSyncMap().put(contentForSyncKey, contentEntityForSync);
        getSyncsContentStringVersionsMap().put(contentForSyncKey, version);
    }


    public String createContentKeyString(SpaceSyncDefinition spaceSyncDefinition, String contentId, String contentType, String version) {
        String key = spaceSyncDefinition.toStorageString() + "::" + contentId + "::" + contentType;
        return key;
    }

    /**
     * load the UserSettings from the file, or return a new empty
     * obj if the file doens't exist
     *
     * @return
     */
    public static ContentVersionListContainer loadRecentSyncsCVC() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        File recentSyncsFile = persistenceBroker.getDataFile(RECENT_SYNCS_FILE);
        ContentVersionListContainer recentSyncVersionsInstance = null;
        if (!recentSyncsFile.exists()) {
            recentSyncVersionsInstance = new ContentVersionListContainer();
            return recentSyncVersionsInstance;
        }
        ContentVersionListContainer deserializedRecentSyncs = (ContentVersionListContainer) persistenceBroker.load(RECENT_SYNCS_FILE);
        if (deserializedRecentSyncs != null) {
            recentSyncVersionsInstance = deserializedRecentSyncs;
        } else {
            recentSyncVersionsInstance = new ContentVersionListContainer();
        }
        return recentSyncVersionsInstance;
    }

    /**
     * save this RecentSyncs instance which is a singleton
     */
    public void save() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        persistenceBroker.save(this, RECENT_SYNCS_FILE);
    }


    private Map<String, String> getSyncsContentStringVersionsMap() {
        if (syncsContentStringVersionsMap == null) {
            syncsContentStringVersionsMap = new HashMap<String, String>();
        }
        return syncsContentStringVersionsMap;
    }

    public Map<String, ContentEntityForSync> getContentEntitiesForSyncMap() {
        if (contentEntitiesForSyncMap == null) {
            contentEntitiesForSyncMap = new HashMap<String, ContentEntityForSync>();
        }
        return contentEntitiesForSyncMap;
    }

    /**
     * create and hand back a map of just the source pages - PageForXMLRpc objects, mapped by id
     * @return map of <String, PageForXmlRpc> for the source pages
     */
    public Map<String, PageForXmlRpc> getSourcePagesMapById() {
        Iterator<ContentEntityForSync> it = getContentEntitiesForSyncMap().values().iterator();
        Map<String, PageForXmlRpc> retMap = new HashMap<String, PageForXmlRpc>();
        while (it.hasNext()) {
            ContentEntityForSync contentEntityForSync = it.next();
            PageForXmlRpc page = contentEntityForSync.getSourcePageForXmlRpc();
            if (page==null) continue;
            retMap.put(page.getId(), page);
        }
        return retMap;
    }

    /**
     * handed a syncKey does a lookup in the map and hands back
     * an int of the version
     *
     * @param syncKey
     * @return
     */
    public int getVersionForSyncAsInt(String syncKey) {
        String versionStr = getSyncsContentStringVersionsMap().get(syncKey);
        if (versionStr == null) return -1;
        int currentVersion = Integer.valueOf(versionStr).intValue();
        return currentVersion;
    }

    /**
     * handed a syncKey does a lookup in the map and hands back
     * an int of the version
     *
     * @param syncKey
     * @return
     */
    public String getVersionForSync(String syncKey) {
        String versionStr = getSyncsContentStringVersionsMap().get(syncKey);
        return versionStr;
    }

    /**
     * handed a syncKey does a lookup in the map and hands back
     * an int of the version
     *
     * @param syncKey
     * @return
     */
    public String getVersionForSyncAsString(String syncKey) {
        String versionStr = getSyncsContentStringVersionsMap().get(syncKey);
        if (versionStr == null) return "-1";
        return versionStr;
    }


    /**
     * puts a value into the map
     *
     * @param syncKey
     * @param version
     */
    public void put(String syncKey, String version) {
        getSyncsContentStringVersionsMap().put(syncKey, version);
    }

    public Collection<String> keySet() {
        return getSyncsContentStringVersionsMap().keySet();
    }

    /**
     * returns a Map of < space name, list of ContentEntityForSync>
     *
     * @return
     */
    public Map<String, List<ContentEntityForSync>> getContentBySpace() {
        Map<String, List<ContentEntityForSync>> map = new HashMap<String, List<ContentEntityForSync>>();
        Collection<ContentEntityForSync> entitiesList = getContentEntitiesForSyncMap().values();
        for (Iterator<ContentEntityForSync> contentEntityForSyncIterator = entitiesList.iterator(); contentEntityForSyncIterator.hasNext();) {
            ContentEntityForSync contentEntityForSync = contentEntityForSyncIterator.next();
            String serverAlias = contentEntityForSync.getSourceServerAlias();
            String spaceName = contentEntityForSync.getSpaceName();
            String key = serverAlias + " : " + spaceName;
            if (map.get(key) == null) {
                map.put(key, new ArrayList<ContentEntityForSync>());
            }
            List list = map.get(key);
            list.add(contentEntityForSync);
        }
        return map;
    }

    public ContentEntityForSync getContentntentEntity(String currentSyncKey) {
        return contentEntitiesForSyncMap.get(currentSyncKey);
    }

    public static void clearForRefresh() {
        File persistantFile = new File(PersistanceBroker.getDataDirectoryLocation() + File.separator + RECENT_SYNCS_FILE);
        persistantFile.delete();
    }
}
