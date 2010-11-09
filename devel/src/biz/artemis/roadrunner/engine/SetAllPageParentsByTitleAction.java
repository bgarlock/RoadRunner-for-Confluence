package biz.artemis.roadrunner.engine;

import biz.artemis.roadrunner.model3.ContentVersionListContainer;
import biz.artemis.roadrunner.model3.ContentEntityForSync;
import biz.artemis.roadrunner.engine.syncactions.SyncAction;
import biz.artemis.roadrunner.engine.syncactions.SyncActionResult;
import biz.artemis.confluence.xmlrpcwrapper.RemoteWikiBroker;
import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.IOException;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

/**
 * Created by IntelliJ IDEA.
 * User: brendan
 * Date: Dec 28, 2008
 * Time: 1:19:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class SetAllPageParentsByTitleAction extends SyncAction {
    public ContentVersionListContainer pageCopyContentContainer;
    public TreeModel treeModel;
    public TreePath[] checkedPaths;


    /**
     * sets the parent pages by title going from local data to the remote server
     *
     * this has to be the second step in a page copy since before the pages exist on the remote server we can't know their ids
     * @return
     * @throws IOException
     * @throws XmlRpcException
     */
    public SyncActionResult execute() throws IOException, XmlRpcException {


        // grab remote id to name map
        RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
        // grabbing any entity from this list for it's server info since they should all be the same
         ContentEntityForSync entity = pageCopyContentContainer.getContentEntitiesForSyncMap().values().iterator().next();

        Map serverPagesByTitle = rwb.getAllServerPagesMapByTitle(entity.getTargetServer(), entity.getTargetSpaceKey());
        Map serverPagesById = rwb.getAllServerPagesMapById(entity.getTargetServer(), entity.getTargetSpaceKey());

        Map<String, PageForXmlRpc> localPagesToCopyMapById = pageCopyContentContainer.getSourcePagesMapById();
        // iterate through all checked node's TreePaths
        for (int i = 0; i < checkedPaths.length; i++) {
            TreePath checkedPath = checkedPaths[i];
            // get page name and parent's name
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) checkedPath.getLastPathComponent();
            PageForXmlRpc localPage = (PageForXmlRpc) node.getUserObject();
            String localPageName = localPage.getTitle();
            // if no parent nodes then continue
            if (checkedPath.getPath().length == 1) continue;
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) checkedPath.getParentPath().getLastPathComponent();
            PageForXmlRpc localParentPage = (PageForXmlRpc) parentNode.getUserObject();
            String localParentName = localParentPage.getTitle();

            // get remote page
            PageForXmlRpc remotePage = (PageForXmlRpc) serverPagesByTitle.get(localPageName);
            PageForXmlRpc remoteParent = (PageForXmlRpc) serverPagesById.get(remotePage.getParentId());

            if (!remoteParent.getTitle().equalsIgnoreCase(localParentName)) {
                // get local version of page with contents for sending
                PageForXmlRpc localPageWithContent = localPagesToCopyMapById.get(localPage.getId());


                // set the page's id to the remote id

                localPageWithContent.setId(remotePage.getId());
                PageForXmlRpc newRemoteParentPage = (PageForXmlRpc) serverPagesByTitle.get(localParentName);
                // set the page's parent id to the remote parent id
                localPageWithContent.setParentId(newRemoteParentPage.getId());
                localPageWithContent.setContent(localPageWithContent.getContent()+"Y");

                // send the page
                rwb.storeNewOrUpdatePage(entity.getTargetServer(), entity.getTargetSpaceKey(), localPageWithContent);
            }
            return SyncActionResult.getSuccessfulResult();

        }


        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getSpaceMessage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void validateData() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getPageName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getWikiObjectType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
