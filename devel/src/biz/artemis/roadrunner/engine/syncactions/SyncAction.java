package biz.artemis.roadrunner.engine.syncactions;

import biz.artemis.confluence.xmlrpcwrapper.RemoteWikiBroker;
import biz.artemis.roadrunner.ui.RRProgressDashboard;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

/**
 * Abstract class which represents a synchronization action such as
 * synchronizing a page or attachment. These should be instantiated
 * by the SyncActionFactory.
 *
 *
 */
public abstract class SyncAction {
    Logger log = Logger.getLogger(this.getClass().getName());    
    List<SyncAction> subActions = new ArrayList<SyncAction>();
    /**
     * remote wiki broker instance, static inititalizer initialized
     */
    RemoteWikiBroker rwb = null;
    Logger pLog = null;
    {
        if (rwb==null) {
            rwb = RemoteWikiBroker.getInstance();
        }
        if (pLog==null) {
            RRProgressDashboard progressDashboard = RRProgressDashboard.getInstance();
            pLog = progressDashboard.getLogger();            
        }
    }

    /**
     * implementations of this should invoke executeSubactions() 
     */
    public abstract SyncActionResult execute() throws IOException, XmlRpcException;

    /**
     * for progress reporting
     * @return
     */
    public abstract String getSpaceMessage();
    /**
     * since we're setting all the required
     * data after we create the object we need to validate
     * it as part of the execution process
     * @return
     */
    public abstract void validateData();
    /**
     * for progress reporting
     * @return
     */
    public abstract String getPageName();
    /**
     * for progress reporting
     * @return
     */
    public abstract String getWikiObjectType();

    public void addSyncAction(SyncAction action) {
        subActions.add(action);
    }

    /**
     *
     */
    public void executeSubactions() {
        for(SyncAction action: subActions) {
            try {
                action.execute();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (XmlRpcException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public List<SyncAction> getSubActions() {
        return subActions;
    }
}
