package biz.artemis.roadrunner.engine;

import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;
import biz.artemis.confluence.xmlrpcwrapper.AttachmentForXmlRpc;
import biz.artemis.roadrunner.engine.syncactions.*;
import biz.artemis.roadrunner.model3.ContentEntityForSync;
import biz.artemis.roadrunner.model3.ContentVersionListContainer;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Collection;

/**
 * Generates the apropriate SyncAction objects
 */
public class SyncActionFactory {
    private static SyncActionFactory instance;

    public static SyncActionFactory getInstance() {
        if (instance==null) {
            instance = new SyncActionFactory();
        }
        return instance;
    }

    public SyncActionGroup createSyncActionGroup() {
        SyncActionGroup sag = new SyncActionGroup() ;
        SyncActionCoordinator.getInstance().addSyncAction(sag);
        return sag;
    }

    public SyncAction createSetAllPageParentsAction() {
        SetAllPageParentsAction action = new SetAllPageParentsAction();
        return action;
    }

    public SyncAction createPageSyncAction(ContentEntityForSync entity) {
        CreatePageSyncAction action = new CreatePageSyncAction();
        action.pageEntity = entity;
        return action;
    }

    public SyncAction createSetAllPageParentsAction(ContentVersionListContainer liveContentVersionsListContainer) {
        SetAllPageParentsAction action = new SetAllPageParentsAction();
        action.liveContentContainer = liveContentVersionsListContainer;
        return action;
    }

    public SyncAction createSetPageParentsByTitleAction(ContentVersionListContainer pageCopyContentContainer, TreePath[] checkedPaths, TreeModel treeModel) {
        SetAllPageParentsByTitleAction action = new SetAllPageParentsByTitleAction();
        action.pageCopyContentContainer = pageCopyContentContainer;
        action.treeModel = treeModel;
        action.checkedPaths = checkedPaths;
        return action;
    }
}
