package biz.artemis.util;

import biz.artemis.roadrunner.model3.ConfluenceServer;
import biz.artemis.confluence.xmlrpcwrapper.ConfluenceServerSettings;
import biz.artemis.confluence.xmlrpcwrapper.RemoteWikiBroker;

import javax.swing.*;

/**
 * some general util methods
 */
public class Util {
    /**
     * just a short pause for testing
     */
    public static void sleepForTesting() {
        /////////////
        // currently skip the sleep, to re-enable remove this line
        if (true) return;
        ////////////
        try {
            // sleep a second for testing
            Thread.sleep(100);
        } catch (InterruptedException e) {}

    }


    /**
     * verifies that a Confluence Server is accessible and pops a message dialog window with
     * any errors.
     * @param confluenceServer
     * @return
     */
    public static boolean verifyServerConnection(ConfluenceServer confluenceServer) {
        StringBuilder errorMessage = null;
        ConfluenceServerSettings css = confluenceServer.getConfluenceServerSettingsForXmlRpc();
         boolean connectionValid = false;

         String connectionInfoMessage = RemoteWikiBroker.getInstance().checkConnectivity(css);
         if (RemoteWikiBroker.USER_MESSAGE_CONNECTIVTY_SUCCESS.equals(connectionInfoMessage)) {
             connectionValid = true;
         } else {
             if (errorMessage==null) errorMessage = new StringBuilder();
             errorMessage.append("there was an isue with the server: "+confluenceServer.getServerAlias()+". \n");
             errorMessage.append(connectionInfoMessage);
             errorMessage.append("\n");
         }
         if (errorMessage!=null) {
             JOptionPane.showMessageDialog(null, errorMessage, "alert", JOptionPane.ERROR_MESSAGE);
             return false;
         }
        return true;

    }


}
