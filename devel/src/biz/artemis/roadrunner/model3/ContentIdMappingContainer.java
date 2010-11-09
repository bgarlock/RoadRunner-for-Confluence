package biz.artemis.roadrunner.model3;

import biz.artemis.roadrunner.model3.Persistable;
import biz.artemis.roadrunner.persistence.PersistanceBroker;

import java.util.Map;
import java.util.HashMap;
import java.io.File;

import org.apache.commons.collections15.bidimap.DualHashBidiMap;

/**
 * Created by IntelliJ IDEA.
 * User: brendan
 * Date: Nov 28, 2008
 * Time: 1:20:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class ContentIdMappingContainer extends Persistable {

    static ContentIdMappingContainer instance = null;

    /**
     * this is the map which maps remote page ids and other content to local page ids
     * this is needed for instance if someone renames a page - we'll only know it's the right page if we know the id
     *
     * <serverId + ":" +pageId , pageId>
     */
    Map<String, String> idMap = new HashMap<String,String>();
    private static final String CONTENT_ID_MAPPING_FILE = "contentIdMapping.xml";


    private ContentIdMappingContainer() {}

    public static ContentIdMappingContainer getInstance() {
        if (instance != null) {
            return instance;
        }
        return load();
    }

    /**
     * returns a bidi map version of the id map
     * @return
     */
    public DualHashBidiMap<String,String> getIdMapAsBidiMap() {
        DualHashBidiMap<String,String> remotePageIdToLocalMap= new DualHashBidiMap<String, String>(idMap);
        return remotePageIdToLocalMap;
    }

    public String getIdMappingFromSourceToTarget(String sourceServerId, String sourcePageId) {
        String key = createIdMapKey(sourceServerId, sourcePageId);        
        String ret = idMap.get(key);
        return ret;
    }

    public void putIdMappingFromSourceToTarget(String sourceServerId, String sourcePageId, String targetPageId) {
        String key = createIdMapKey(sourceServerId, sourcePageId);
        idMap.put(key, targetPageId);
    }

    public String createIdMapKey(String sourceServerId, String sourcePageId) {
        return sourceServerId+":"+sourcePageId;
    }


    /**
     * load the UserSettings from the file, or return a new empty
     * obj if the file doens't exist
     *
     * @return
     */
    public static ContentIdMappingContainer load() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        File userSettingsFile = persistenceBroker.getDataFile(CONTENT_ID_MAPPING_FILE);
        if (!userSettingsFile.exists()) {
            instance = new ContentIdMappingContainer();
            return instance;
        }
            ContentIdMappingContainer deserializedSettings = (ContentIdMappingContainer) persistenceBroker.load(CONTENT_ID_MAPPING_FILE);
        if (deserializedSettings != null) {
            instance = deserializedSettings;
        } else {
            instance = new ContentIdMappingContainer();
        }
        return instance;
    }

    /**
     * save this singleton instance
     */
    public void save() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        persistenceBroker.save(this, CONTENT_ID_MAPPING_FILE);
    }


    public static void clearForRefresh() {
        File persistantFile = new File(PersistanceBroker.getDataDirectoryLocation() + File.separator + CONTENT_ID_MAPPING_FILE);
        persistantFile.delete();
        instance = null;

    }
}
