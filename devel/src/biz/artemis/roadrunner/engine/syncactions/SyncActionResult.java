package biz.artemis.roadrunner.engine.syncactions;

/**
 * Encapsulate the results of a SyncAction
 */
public class SyncActionResult {
    private String resultMessage;
    private static SyncActionResult successfulResult = null;

    public SyncActionResult(String message) {
        this.resultMessage = message;
    }

    /**
     * sends back a successful result object which should be the same
     * object each time to reduce the object stack.
     * @return
     */
    public static SyncActionResult getSuccessfulResult() {
        if (successfulResult==null) {
            successfulResult = new SyncActionResult("success");
        }
        return successfulResult;
    }

    public static SyncActionResult createError(String message) {
        return new SyncActionResult(message);
    }

    public String toString() {
        return resultMessage;
    }
}
