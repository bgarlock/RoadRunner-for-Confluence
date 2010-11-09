package biz.artemis.roadrunner.engine.syncactions;

import biz.artemis.roadrunner.engine.syncactions.SyncAction;

/**
 * Collection of SyncActions grouped logically
 */
public class SyncActionGroup extends SyncAction {
    public SyncActionResult execute() {
        return null;
    }

    /**
     * for progress reporting, not currently used
     * @return null
     */
    public String getSpaceMessage() {
        return null;
    }

    public void validateData() {
        // nothing to validate for this method at this time
        return;
    }

    /**
     * for progress reporting  not currently used
     * @return null
     */
    public String getPageName() {
        return null;
    }

    /**
     * for progress reporting, not currently used
     * @return null
     */
    public String getWikiObjectType() {
        return null;  
    }
}
