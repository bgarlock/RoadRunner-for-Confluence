package biz.artemis.roadrunner.model3;

import biz.artemis.roadrunner.model3.UserSettings;

/**
 * This class defines a Sync which are the components to synchronize a space on one server to another.
 * Although this definition should be complete it references other objects. It contains both data
 * and convenience methods on how to retrieve and represent that data.
 *
 */
public class SpaceSyncDefinition {

    ConfluenceServer sourceServer;
    ConfluenceServer targetServer;
    String sourceSpaceName;
    String sourceSpaceKey;

    public String getDefinitionAsString() {
        return sourceServer.getServerAlias() + " >> " + sourceSpaceName + " :: " + sourceSpaceKey + " >> " + targetServer.getServerAlias();
    }


    /**
     * persist this definition but using server ids which won't change as opposed to aliases which could
     * @return
     */
    public String toStorageString() {
        return sourceServer.getId() + "::" + sourceSpaceName + "::" + sourceSpaceKey + "::" + targetServer.getId();
    }

    public static SpaceSyncDefinition objectFromStr(String currentSyncKey) {
        String split[] = currentSyncKey.split("::");
        SpaceSyncDefinition ret = new SpaceSyncDefinition();
        ret.setSourceSpaceName(split[1]);
        ret.setSourceSpaceKey(split[2]);

        UserSettings userSettings = UserSettings.getInstance();
        String sourceServerId = split[0];
        String targetServerId = split[3];
        ret.sourceServer = userSettings.getConfluenceServersIdMap().get(sourceServerId);
        ret.targetServer = userSettings.getConfluenceServersIdMap().get(targetServerId);

        return ret;
    }


    public ConfluenceServer getSourceServer() {
        return sourceServer;
    }

    public void setSourceServer(ConfluenceServer sourceServer) {
        this.sourceServer = sourceServer;
    }

    public ConfluenceServer getTargetServer() {
        return targetServer;
    }

    public void setTargetServer(ConfluenceServer targetServer) {
        this.targetServer = targetServer;
    }

    public String getSourceSpaceName() {
        return sourceSpaceName;
    }

    public void setSourceSpaceName(String sourceSpaceName) {
        this.sourceSpaceName = sourceSpaceName;
    }

    public String getSourceSpaceKey() {
        return sourceSpaceKey;
    }

    public void setSourceSpaceKey(String sourceSpaceKey) {
        this.sourceSpaceKey = sourceSpaceKey;
    }

    public void setSpaceNameAndKey(String value) {
        String parts[] =  value.split(" :: ");
        this.sourceSpaceName = parts[0];
        this.sourceSpaceKey = parts[1];

    }

    public String toString() {
        return getDefinitionAsString();
    }

}
