package biz.artemis.roadrunner.model2;

import org.apache.commons.collections15.bidimap.DualHashBidiMap;
import org.apache.commons.collections15.MapIterator;
import org.apache.commons.collections15.map.HashedMap;

import java.text.Bidi;
import java.util.HashMap;

/**
 * RRSpaceLink encapsulates everything related to the two spaces which
 * are linked and to be synchronized.
 * <p/>
 * This data will be persisted to XML so we need to make sure
 * convenience members are marked transient
 * <p/>
 * We also need to make sure there are not dual copies of the same data anywhere
 * in this object.
 */
public class RRSpaceLink {
    /**
     * everything related to the preferences screen
     */
    RRSpaceLinkPreferences preferences;
    RRSpace localSpace;
    RRSpace remoteSpace;
    DualHashBidiMap<String, String> localToRemoteIdMap;
    transient DualHashBidiMap<RRPage, RRPage> localToRemotePageMap;

    public static RRSpaceLink createRRSpaceLink() {
        RRSpaceLink spaceLink = new RRSpaceLink();
        RRSpaceLinkIndex.getInstance().addRRSpaceLink(spaceLink);
        return spaceLink;
    }

    /////////////////////
    // getters and setters
    /////////////////////
    public RRSpaceLinkPreferences getPreferences() {
        return preferences;
    }

    public void setPreferences(RRSpaceLinkPreferences preferences) {
        this.preferences = preferences;
    }

    public RRSpace getLocalSpace() {
        return localSpace;
    }

    public void setLocalSpace(RRSpace localSpace) {
        this.localSpace = localSpace;
    }

    public RRSpace getRemoteSpace() {
        return remoteSpace;
    }

    public void setRemoteSpace(RRSpace remoteSpace) {
        this.remoteSpace = remoteSpace;
    }

    public DualHashBidiMap<String, String> getLocalToRemoteIdMap() {
        return localToRemoteIdMap;
    }

    public void setLocalToRemoteIdMap(DualHashBidiMap<String, String> localToRemoteIdMap) {
        this.localToRemoteIdMap = localToRemoteIdMap;
    }

    /**
     * This is a lot of code but basically just returns a transient
     * mapping of localPage object linked to their peer remotePage objects.
     * If the collection has not been created but everything else is in
     * place then the getter method creates it on request.
     *
     * @return
     */
    public DualHashBidiMap<RRPage, RRPage> getLocalToRemotePageMap() {
        // if it exists and is populated then return it
        if (localToRemotePageMap != null && localToRemotePageMap.size() > 0) {
            return localToRemotePageMap;
        }
        // otherwise create it
        if (localToRemotePageMap == null) {
            localToRemotePageMap = new DualHashBidiMap<RRPage, RRPage>();
        }
        // and populate it
        if (localSpace != null &&
                remoteSpace != null &&
                getLocalPageMap() != null &&
                getRemotePageMap() != null &&
                localToRemoteIdMap != null) {
            MapIterator<String, String> it = localToRemoteIdMap.mapIterator();
            // get local references to speed things up
            HashedMap<String, RRPage> localPageMap = getLocalPageMap();
            HashedMap<String, RRPage> remotePageMap = getRemotePageMap();

            while (it.hasNext()) {
                String localId = it.getKey();
                String remoteId = it.getValue();
                localToRemotePageMap.put(localPageMap.get(localId), remotePageMap.get(remoteId));
            }
        }
        return localToRemotePageMap;
    }

    /**
     * reset the transient mapping so it will be created next time
     */
    public void resetLocalToRemotePageMap() {
        localToRemotePageMap = null;
    }

    /**
     * convenience getter method so that we don't necessarily have to
     * dig through the localSpace obj .
     *
     * @return
     */
    public HashedMap<String, RRPage> getLocalPageMap() {
        if (localSpace == null) {
            return null;
        }
        return localSpace.getPageMap();
    }


    /**
     * convenience getter method so that we don't necessarily have to
     * dig through the remoteSpace obj .
     *
     * @return
     */
    public HashedMap<String, RRPage> getRemotePageMap() {
        if (remoteSpace == null) {
            return null;
        }
        return remoteSpace.getPageMap();
    }

}
