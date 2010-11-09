package biz.artemis.roadrunner.engine;

import biz.artemis.confluence.xmlrpcwrapper.*;
import biz.artemis.util.Util;
import biz.artemis.roadrunner.RoadRunner;
import biz.artemis.roadrunner.model3.*;
import biz.artemis.roadrunner.ui.RRProgressDashboard;
import biz.artemis.roadrunner.engine.syncactions.SyncAction;
import biz.artemis.roadrunner.engine.syncactions.SyncActionGroup;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.util.*;
import java.util.List;
import java.io.IOException;
import java.awt.*;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

/**
 * This is the RoadRunner's engine front end, first class called.
 * This is a 'director' class if you will.
 * <p/>
 * responsible for determining which pages need schronization and
 * then kicking that off
 * <p/>
 * primary starting method is synchronizeConfluence_old()
 */
public class ConfluenceSynchronizer {
    Logger log = Logger.getLogger(this.getClass().getName());
    public static final String REMOTE_PAGE_VERSION_STAMPS = "remotePageVersionStamps.properties";
    public static final String LOCAL_PAGE_VERSION_STAMPS = "localPageVersionStamps.properties";
    Map localPageVersionStamps;
    Map remotePageVersionStamps;
    RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
//    RoadRunnerUserSettings rrSettings = RoadRunnerUserSettings.getInstance();

    private static ConfluenceSynchronizer instance = new ConfluenceSynchronizer();


    /**
     * kick of synchronization
     * <p/>
     * * @todo - refactor this method so it's just high level calls which make the algorithm clear
     */
    public void synchConfluence() throws IOException, XmlRpcException {
        // set up synch progress window
        RRProgressDashboard progressDashboard = setUpRRProgressDashboard();
        Logger pLog = RRProgressDashboard.getInstance().getLogger();
        RoadRunner rr = RoadRunner.getInstance();
        // check connectivity - that both servers are reachable
        UserSettings userSettings = UserSettings.getInstance();
        StringBuilder userErrorMessage = new StringBuilder();
        List<ConfluenceServer> networkedServers = verifyConnections(userSettings, userErrorMessage);
        if (userErrorMessage.length() > 0) {
            //     if not then post error message with details about what is not reachable
            JOptionPane.showMessageDialog(null, userErrorMessage.toString(), "alert", JOptionPane.ERROR_MESSAGE);
            pLog.error(userErrorMessage.toString());
            // for now just kill the process if servers that should be reachable are not reachable
            // @todo v0.3 - give user the option of continuting
            progressDashboard.progressComplete();
            return;
        }
        if (networkedServers.size() == 0) {
            progressDashboard.progressComplete();
            return;
        }
        // get recently synced info
        ContentVersionListContainer recentlySynchedRemoteContentList = ContentVersionListContainer.loadRecentSyncsCVC();
        // getAllSpaceSyncDefs
        //////////////////////////
        // @todo - <breakOutToMethod> this should be broken out into a method
        //////////////////////////
        List<SpaceSyncDefinition> allSpaceSyncs = userSettings.getAllSyncs();
        // iterate through, retrieve all content versions and build realTimeVersions map
        RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
        ContentVersionListContainer liveContentVersionsListContainer = new ContentVersionListContainer();
        for (int i = 0; i < allSpaceSyncs.size(); i++) {
            SpaceSyncDefinition spaceSyncDefinition = allSpaceSyncs.get(i);
            ConfluenceServer sourceServer = spaceSyncDefinition.getSourceServer();
            ConfluenceServerSettings xmlRpcSettings = sourceServer.getConfluenceServerSettingsForXmlRpc();
            List<PageForXmlRpc> pageSummaries = rwb.getAllServerPageSummaries(xmlRpcSettings, spaceSyncDefinition.getSourceSpaceKey());
            for (int j = 0; j < pageSummaries.size(); j++) {
                PageForXmlRpc pageForXmlRpc = pageSummaries.get(j);
                List<PageHistorySummaryForXmlRpc> pageHistories = rwb.getPageHistory(xmlRpcSettings, pageForXmlRpc.getId());
                // @todo - leaving off here 2008-11-27 10:20p.m.
                String currentPageVersion = getCurrentPageVersion(pageHistories);
                liveContentVersionsListContainer.addSourceContent(spaceSyncDefinition, pageForXmlRpc.getId(), ContentEntityForSync.CONTENT_TYPE_PAGE, pageForXmlRpc, currentPageVersion);

                // add the attachments to the list
                List<AttachmentForXmlRpc> pageAttachmentsList = rwb.getAttachments(sourceServer.getConfluenceServerSettingsForXmlRpc(), pageForXmlRpc.getId());
                for (AttachmentForXmlRpc attachmentForXmlRpc : pageAttachmentsList) {
                    liveContentVersionsListContainer.addSourceContent(spaceSyncDefinition, attachmentForXmlRpc.getId(), ContentEntityForSync.CONTENT_TYPE_ATTACHMENT, attachmentForXmlRpc,
                            attachmentForXmlRpc.getCreated());

                }

            }
        }
        //////////////////////////
        // @todo - </breakOutToMethod> this should be broken out into a method
        //////////////////////////

        // compare maps and if realtime version is more up to date than recentSycn versions create a SyncAction
        ContentVersionListContainer contentNeedingUpdates = getContentSyncsNeedingUpdates(recentlySynchedRemoteContentList, liveContentVersionsListContainer);
        // execute all sync actions
        try {
            createSyncActionsFromContentList(contentNeedingUpdates, progressDashboard);
            createSetPageParentsAction(liveContentVersionsListContainer);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XmlRpcException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Util.sleepForTesting();
        SyncActionCoordinator.getInstance().executeAll(progressDashboard);
        progressDashboard.progressComplete();

        // persist objects that were updated during the sync process
        ContentIdMappingContainer.getInstance().save();
        liveContentVersionsListContainer.save();
    }

    /**
     * convenience method to set up the RRProgress dashboard, center it, make it visible, etc.
     *
     * @return
     */
    private RRProgressDashboard setUpRRProgressDashboard() {
        RRProgressDashboard progressDashboard = RRProgressDashboard.getInstance();
        progressDashboard.setSize(406, 469);
        // center on the screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension size = toolkit.getScreenSize();
        progressDashboard.setLocation(size.width / 2 - progressDashboard.getWidth() / 2,
                size.height / 2 - progressDashboard.getHeight() / 2);

        progressDashboard.setVisible(true);
        progressDashboard.startProgress();
        return progressDashboard;

    }

    /**
     * this is responsible for kicking off the manual page copy from the checkbox tree
     *
     * @param localServer
     * @param remoteServer
     * @param model
     * @param checkedPaths
     * @param sendAttachments
     */
    public void pageCopySync(ConfluenceServer localServer, ConfluenceServer remoteServer, TreeModel treeModel, TreePath[] checkedPaths, boolean sendAttachments) {
        // make a list of the checked pages
        ContentVersionListContainer pageCopyContentContainer = new ContentVersionListContainer();
        SpaceSyncDefinition ssd = null;
        for (int i = 0; i < checkedPaths.length; i++) {
            TreePath treePath = checkedPaths[i];
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            PageForXmlRpc page = (PageForXmlRpc) node.getUserObject();
            System.out.println("checked = " + node);

            ssd = new SpaceSyncDefinition();
            ssd.setSourceServer(localServer);
            ssd.setTargetServer(remoteServer);
            ssd.setSourceSpaceKey(page.getSpace());
            ssd.setSourceSpaceName("space-name-filler");

            pageCopyContentContainer.addSourceContent(ssd, page.getId(), ContentEntityForSync.CONTENT_TYPE_PAGE, page, "1");
            RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
            // add the attachments to the list
            if (sendAttachments) {
                List<AttachmentForXmlRpc> pageAttachmentsList = null;
                try {
                    pageAttachmentsList = rwb.getAttachments(localServer.getConfluenceServerSettingsForXmlRpc(), page.getId());
                    for (AttachmentForXmlRpc attachmentForXmlRpc : pageAttachmentsList) {
                        pageCopyContentContainer.addSourceContent(ssd, attachmentForXmlRpc.getId(), ContentEntityForSync.CONTENT_TYPE_ATTACHMENT, attachmentForXmlRpc,
                                attachmentForXmlRpc.getCreated());
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (XmlRpcException e1) {
                    e1.printStackTrace();
                }
            }

        }

        // set up synch progress window
        RRProgressDashboard progressDashboard = setUpRRProgressDashboard();
        // create the syncActions and run the sync
        try {
            createSyncActionsFromContentList(pageCopyContentContainer, progressDashboard);
            createSetPageParentsByTitleAction(pageCopyContentContainer, checkedPaths, treeModel, progressDashboard);
            SyncActionCoordinator.getInstance().executeAll(progressDashboard);
            progressDashboard.progressComplete();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlRpcException e) {
            e.printStackTrace();
        }


    }

    // @todo - pick up here - going o
    /**
     * 
     * @param pageCopyContentContainer
     * @param treeModel
     * @param progressDashboard
     */
    private void createSetPageParentsByTitleAction(ContentVersionListContainer pageCopyContentContainer, TreePath[] checkedPaths, TreeModel treeModel, RRProgressDashboard progressDashboard) {
        SyncActionFactory actionFactory = SyncActionFactory.getInstance();
        SyncActionGroup syncActionGroup = actionFactory.createSyncActionGroup();
        SyncAction action = actionFactory.createSetPageParentsByTitleAction(pageCopyContentContainer, checkedPaths, treeModel);
        syncActionGroup.addSyncAction(action);
    }


    /**
     * runs throught the two ContentVersionContainers and creates new
     * ContentVersionListContainer storing just what needs updating
     * <p/>
     * also needs to be sure what we return has the List of ContentEntityForSync object populated
     *
     * @param recentSyncVersionsCont
     * @param currentSyncVersionsCont
     * @return List<SyncDefinition> which are all of the ones needing updates
     */
    public ContentVersionListContainer getContentSyncsNeedingUpdates(ContentVersionListContainer recentSyncVersionsCont, ContentVersionListContainer currentSyncVersionsCont) {
        ContentVersionListContainer retContainer = new ContentVersionListContainer();
        Collection<String> currentSyncKeys = currentSyncVersionsCont.keySet();
        for (Iterator<String> stringIterator = currentSyncKeys.iterator(); stringIterator.hasNext();) {
            String currentSyncKey = stringIterator.next();
            String currentVersion = currentSyncVersionsCont.getVersionForSync(currentSyncKey);
            String recentVersion = recentSyncVersionsCont.getVersionForSync(currentSyncKey);
            if (!currentVersion.equals(recentVersion)) {
                retContainer.addSourceContent(currentSyncKey, currentSyncVersionsCont.getContentntentEntity(currentSyncKey), currentSyncVersionsCont.getVersionForSyncAsString(currentSyncKey));
            }
        }
        return retContainer;
    }

    /**
     * just gets out the largest version number which is stored as a string in an unordered List
     *
     * @param pageHistories
     * @return
     */
    private String getCurrentPageVersion(List<PageHistorySummaryForXmlRpc> pageHistories) {
        if (pageHistories == null || pageHistories.size() == 0) {
            // if no history is returned it is because there is only 1 version
            return "1";
        }
        Integer currentVersion = Integer.valueOf("0");
        for (int i = 0; i < pageHistories.size(); i++) {
            PageHistorySummaryForXmlRpc pageHistorySummaryForXmlRpc = pageHistories.get(i);
            Integer version = Integer.valueOf(pageHistorySummaryForXmlRpc.getVersion());
            if (version.intValue() > currentVersion.intValue()) {
                currentVersion = version;

            }
        }
        currentVersion = currentVersion + 1; // because only histories are returned and the current version is +1
        return currentVersion.toString();
    }


    private void createSetPageParentsAction(ContentVersionListContainer liveContentVersionsListContainer) throws IOException, XmlRpcException {
        SyncActionFactory actionFactory = SyncActionFactory.getInstance();
        SyncActionGroup syncActionGroup = actionFactory.createSyncActionGroup();
        SyncAction action = actionFactory.createSetAllPageParentsAction(liveContentVersionsListContainer);
        syncActionGroup.addSyncAction(action);
    }


    /**
     * this method creates all the sync actions from the list of SyncDefinitions
     * <p/>
     * A SyncAction depends on more than just the definition but also on user preferences about
     * what to do for conflicts, etc.
     *
     * @param progressDashboard
     */
//    private void createSyncActionsFromContentList(List<ServerSyncLink> firstSpaceSyncsList, RRProgressDashboard progressDashboard) throws IOException, XmlRpcException {
    private void createSyncActionsFromContentList(ContentVersionListContainer contentNeedingUpdatesContainer, RRProgressDashboard progressDashboard) throws IOException, XmlRpcException {


        // get content by source space
        Map<String, List<ContentEntityForSync>> contentListsGroupedBySpace = contentNeedingUpdatesContainer.getContentBySpace();
        SyncActionFactory actionFactory = SyncActionFactory.getInstance();

        progressDashboard.setTotalSpacesToSync(contentListsGroupedBySpace.size());

        for (String serverAliasAndSpaceName : contentListsGroupedBySpace.keySet()) {
            List<ContentEntityForSync> contentEntitiesInSpace = contentListsGroupedBySpace.get(serverAliasAndSpaceName);
            // create SyncActionGroup
            SyncActionGroup syncActionGroup = actionFactory.createSyncActionGroup();


            // create local space if necessary
            if (contentEntitiesInSpace.size() > 0) {
                ContentEntityForSync ce = contentEntitiesInSpace.get(0);
                ConfluenceServerSettings css = ce.getTargetServer();
                SpaceForXmlRpc targetSpace = rwb.getSpace(css, ce.getTargetSpaceKey());
                if (targetSpace == null) {
                    SpaceForXmlRpc sourceSpace = rwb.getSpace(ce.getSourceServer(), ce.getTargetSpaceKey());
                    //then create space
                    targetSpace = new SpaceForXmlRpc();
                    targetSpace.setSpaceKey(ce.getTargetSpaceKey());
                    targetSpace.setSpaceName(sourceSpace.getSpaceName());
                    String description = sourceSpace.getDescription();
                    description = cleanSpaceDescription(description);
                    targetSpace.setDescription(description);
                    rwb.addSpace(ce.getTargetServer(), targetSpace);
                }

            }

            // create SyncAction for each page from SyncActionFactory
            for (Object o : contentEntitiesInSpace) {
                ContentEntityForSync entity = (ContentEntityForSync) o;
                // only add pages first
                if (!entity.getContentType().equals(ContentEntityForSync.CONTENT_TYPE_PAGE)) continue;
                SyncAction action = actionFactory.createPageSyncAction(entity);
                // add to SyncActionGroup
                syncActionGroup.addSyncAction(action);

            }

            for (Object o : contentEntitiesInSpace) {
                ContentEntityForSync entity = (ContentEntityForSync) o;
                // only add attachments
                if (!entity.getContentType().equals(ContentEntityForSync.CONTENT_TYPE_ATTACHMENT)) continue;
                SyncAction action = actionFactory.createPageSyncAction(entity);
                // add to SyncActionGroup
                syncActionGroup.addSyncAction(action);

            }


        }

    }

    /**
     * the space description is handed back with some issues.
     * this method cleans it up
     *
     * @param description
     * @return
     */
    private String cleanSpaceDescription(String description) {
        if (description == null) return " ";
        description = description.replaceAll("\\<p\\>", "");
        description = description.replaceAll("\\</p\\>", "");
        return description;  //To change body of created methods use File | Settings | File Templates.
    }


    public static ConfluenceSynchronizer getInstance() {
        return instance;
    }

    private ConfluenceSynchronizer() {
    }

    /**
     * verifies which spaceSynchPref objects can establish valid connections and returns a list
     * of just those.
     * <p/>
     * also gathers user messages for connection errors.
     *
     * @param userSettings
     * @param userMessage
     * @return
     */
    protected List<ConfluenceServer> verifyConnections(UserSettings userSettings, StringBuilder userMessage) {


        List<ConfluenceServer> allServersBeingSynched = UserSettings.getInstance().getAllServersBeingSynced();
        List<ConfluenceServer> successfulConnections = new ArrayList<ConfluenceServer>();
        RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
        for (int i = 0; i < allServersBeingSynched.size(); i++) {
            ConfluenceServer confluenceServer = allServersBeingSynched.get(i);
            ConfluenceServerSettings css = confluenceServer.getConfluenceServerSettingsForXmlRpc();
            boolean connectionValid = false;
            String connectionInfoMessage = rwb.checkConnectivity(css);
            if (RemoteWikiBroker.USER_MESSAGE_CONNECTIVTY_SUCCESS.equals(connectionInfoMessage)) {
                connectionValid = true;
            } else {
                userMessage.append("there was an isue with the server: " + confluenceServer.getServerAlias() + ". \n");
                userMessage.append(connectionInfoMessage);
                userMessage.append("\n");
                userMessage.append("\n");
            }
            if (connectionValid) {
                successfulConnections.add(confluenceServer);
            }

        }
        return successfulConnections;
    }

}

