package biz.artemis.roadrunner.model3;

import biz.artemis.util.UniqueID;
import biz.artemis.confluence.xmlrpcwrapper.SpaceSummaryForXmlRpc;
import biz.artemis.confluence.xmlrpcwrapper.ConfluenceServerSettings;
import biz.artemis.confluence.xmlrpcwrapper.SpaceForXmlRpc;
import biz.artemis.roadrunner.model3.Persistable;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * this stores server info and also all the syncs selected with this server as the source
 */
public class ConfluenceServer extends Persistable {
    public java.lang.String id;
    public java.lang.String login;
    public java.lang.String password;
    public java.lang.String serverAlias;
    public java.lang.String url;

    

    /**
     * list of all known spaces for this server
     */
    public List<SpaceSummaryForXmlRpc> allKnownSpaces;

    /**
     * list of spaces to be synced, but these are stored as 'spaceName :: spaceKey'
     */
    public List<SpaceSyncDefinition> spacesToSync;

    public ConfluenceServer() {
        id = UniqueID.getInstance().createUniqueId();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerAlias() {
        return serverAlias;
    }

    public void setServerAlias(String serverAlias) {
        this.serverAlias = serverAlias;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * returns the list sorted alphabetically by space name
     * @return
     */
    public List<SpaceSummaryForXmlRpc> getAllKnownSpaces() {
//        Collections.sort(allKnownSpaces, String.CASE_INSENSITIVE_ORDER);
        TreeMap<String, SpaceSummaryForXmlRpc> map =  new TreeMap<String, SpaceSummaryForXmlRpc>();
        if (allKnownSpaces==null) { return null; }
        for (SpaceSummaryForXmlRpc allKnownSpace : allKnownSpaces) {
            SpaceSummaryForXmlRpc spaceForXmlRpc = allKnownSpace;
            map.put(spaceForXmlRpc.toString(), spaceForXmlRpc);
        }
        ArrayList<SpaceSummaryForXmlRpc> retList = new ArrayList<SpaceSummaryForXmlRpc>(map.values());

        return retList;
    }

    public void setAllKnownSpaces(List allKnownSpaces) {
        this.allKnownSpaces = allKnownSpaces;
    }

    public List<SpaceSyncDefinition> getSpacesToSync() {
        if (spacesToSync==null) {
            spacesToSync = new ArrayList(); 
        }
        return spacesToSync;
    }

    public void setSpacesToSync(List<SpaceSyncDefinition> spacesToSync) {
        this.spacesToSync = spacesToSync;
    }

    public String getId() {
        return id;
    }

    public boolean isBeingSyncedAsRemoteServer() {
        if (spacesToSync!=null && spacesToSync.size()>0) {
            return true;
        }
        return false;
    }

    public ConfluenceServerSettings getConfluenceServerSettingsForXmlRpc() {
        ConfluenceServerSettings xmlRpcSettings = new ConfluenceServerSettings();
        xmlRpcSettings.setLogin(this.getLogin());
        xmlRpcSettings.setPassword(this.getPassword());
        xmlRpcSettings.setUrl(this.getUrl());
//        xmlRpcSettings.setLogin(this.getLogin());
        return xmlRpcSettings;
    }

//    List getAliasesOfSyncTargetLocalServers() {
//
//    }
}
