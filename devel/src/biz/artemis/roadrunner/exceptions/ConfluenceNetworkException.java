package biz.artemis.roadrunner.exceptions;

/**
 * Shows there is a problem with the network connection
 */
public class ConfluenceNetworkException extends Exception {
    public ConfluenceNetworkException(String message) {
        super(message);
    }
}
