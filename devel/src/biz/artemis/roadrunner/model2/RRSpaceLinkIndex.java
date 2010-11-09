package biz.artemis.roadrunner.model2;

import biz.artemis.roadrunner.persistence.PersistanceBroker;
import biz.artemis.roadrunner.model3.Persistable;

import java.util.ArrayList;

/**
 * This collection is to keep track of the different spaces
 * being monitored and persisted.
 *
 * Presumably if a space is in the SpaceIndex it has already been synched
 * at least once.
 *
 * This class basically keeps pointers to all of the local and remote space's metadata
 *
 * For now this is going to be the uber object which is the root node of the
 * entire persistabl data tree. We may refactor this later to split up the SpaceLinks
 * into individual files.
 */
public class RRSpaceLinkIndex extends Persistable {

//    /** a map which connects the spaces unique id to their xml persisted filename */
//    Map<String, String> spacesFileNameMap = new HashMap<String, String>();

//    /**
//     * a map which provides pointers to all the actual persisted spaces
//     * once they've been loaded.
//     */
//    DualHashBidiMap spacesLinkMap = new DualHashBidiMap();

    ArrayList<RRSpaceLink> spaceLinkList = new ArrayList<RRSpaceLink>();


    /** singleton instance reference */
    private static RRSpaceLinkIndex instance = null;
    private static final String SPACE_INDEX_FILE = "spaceIndex.xml";

    public static RRSpaceLinkIndex getInstance() {
        if (instance!=null) {
            return instance;
        }
        return load();
    }

    /**
     * load the UserSettings from the file, or return a new empty
     * obj if the file doens't exist
     * @return
     */
    public static RRSpaceLinkIndex load() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        RRSpaceLinkIndex deserializedSpaceIndex = (RRSpaceLinkIndex) persistenceBroker.load(SPACE_INDEX_FILE);
        if (deserializedSpaceIndex != null) {
            instance = deserializedSpaceIndex;
        } else {
            instance = new RRSpaceLinkIndex();
        }
        return instance;
    }

    /**
     * save this UserSettings instance which is a singleton
     */
    public void save() {
        PersistanceBroker persistenceBroker = PersistanceBroker.getInstance();
        persistenceBroker.save(this, SPACE_INDEX_FILE);
    }

    public void addRRSpaceLink(RRSpaceLink spaceLink) {
        spaceLinkList.add(spaceLink);

    }
}
