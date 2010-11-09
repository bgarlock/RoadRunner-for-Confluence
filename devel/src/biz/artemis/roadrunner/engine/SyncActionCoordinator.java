package biz.artemis.roadrunner.engine;

import biz.artemis.roadrunner.engine.syncactions.SyncAction;
import biz.artemis.roadrunner.engine.syncactions.SyncActionResult;
import biz.artemis.roadrunner.ui.RRProgressDashboard;
import biz.artemis.util.Util;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

/**
 * The SyncActionCoordinator maintains the top level of syncations
 * and also contains the code which walks through the tree of actions
 * executing each one in the correct tree ordering
 */
public class SyncActionCoordinator {
    private static SyncActionCoordinator instance;
    /**
     * list of all top level actions
     */
    List<SyncAction> actionsList = new ArrayList<SyncAction>();

    public static SyncActionCoordinator getInstance() {
        if (instance==null) {
            instance = new SyncActionCoordinator();
        }
        return instance;
    }

    /**
     * add top level sync actions
     */
    public void addSyncAction(SyncAction action) {
        actionsList.add(action);
    }

    /**
     * recursively executes all the actions recursing all the way down the tree,
     * executing a branch at a time (depth first), executing top down.
     *
     * This algorithm is supposedly the most optimized way to traverse a tree in Java
     * http://www.ahmadsoft.org/articles/recursion/index.html
     *
     * @param action exectute this action and then all its decendants
     * @param progressDashboard
     */
    public void execute(SyncAction action, RRProgressDashboard progressDashboard) throws IOException, XmlRpcException {
        SyncActionResult result = action.execute();
        progressDashboard.incrementWikiObjectProgressBar();
        Util.sleepForTesting();
        Logger pLog = progressDashboard.getLogger();
        List<SyncAction> subActions = action.getSubActions();
        int size = subActions.size();
        for (int i=0; i<size; i++){
            SyncAction subAction = subActions.get(i);
            // start - progress reporting
            if (subAction.getSpaceMessage()!=null) {progressDashboard.setSpaceTextField(subAction.getSpaceMessage());}
            progressDashboard.setWikiObjectTextField(subAction.getPageName()+" : "+subAction.getWikiObjectType());
            pLog.info("synchronizing "+ subAction.getPageName()+" : "+subAction.getWikiObjectType());
            // end - progress reporting
            this.execute(subAction, progressDashboard);
        }
        // remove all the subAction that have been run
        subActions.clear();
    }

    /**
     * executes the top level sync actions and traverses all the way down
     * @param progressDashboard
     */
    public void executeAll(RRProgressDashboard progressDashboard) {
        // start progress bar setting
        int totalTopLevelActions = actionsList.size();
        progressDashboard.resetSpaceProgressBar(totalTopLevelActions);
        // end progress bar setting
        for(SyncAction topLevelAction : actionsList) {
            // start progress bar setting
            int totalSubActions = getTotalSubActions(topLevelAction);
            progressDashboard.resetWikiObjectProgressBar(totalSubActions);
            progressDashboard.incrementSpaceProgressBar();
            // end progress bar setting
            try {
                this.execute(topLevelAction, progressDashboard);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (XmlRpcException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        actionsList.clear();
    }

    /**
     * recurse to find total number of items in a tree list
     * @param topLevelAction
     * @return
     */
    private int getTotalSubActions(SyncAction topLevelAction) {
        int totalCount = 0;
        LinkedList<SyncAction> allActions = new LinkedList<SyncAction>();
        List<SyncAction> subActions = topLevelAction.getSubActions();
        allActions.addAll(subActions);
        while (!allActions.isEmpty()) {
            // look at next item in list
            SyncAction item = allActions.getFirst();
            if (item.getSubActions().size()>0) {
                allActions.addAll(item.getSubActions());
            }
            totalCount++;
            allActions.removeFirst();
        }
        return totalCount;  //To change body of created methods use File | Settings | File Templates.
    }

}
