package biz.artemis.roadrunner.model3;

import biz.artemis.confluence.xmlrpcwrapper.ConfluenceServerSettings;
import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;
import biz.artemis.confluence.xmlrpcwrapper.RemoteWikiBroker;
import biz.artemis.confluence.xmlrpcwrapper.AttachmentForXmlRpc;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;

/**
 * this class represents all the metadata about a 'content entity' (page, attachment, etc) which we know
 * about.
 *
 * this class is somewhat complex in that parts of it's data are filled in at different times during the
 * sync process
 * 
 *
 * we're storing this metadata in string form in other places but need a way to 
 *
 * currently this class is transient an will not be persisted.
 */
public class ContentEntityForSync {


    /** content entity types */
    public static final String CONTENT_TYPE_PAGE = "CONTENT_TYPE_PAGE";
    public static final String CONTENT_TYPE_ATTACHMENT = "CONTENT_TYPE_ATTACHMENT";


    SpaceSyncDefinition spaceSyncDefinition;
    String contentSourceId;
    /** this type should be one of the static types such as
     * CONTENT_TYPE_ATTACHMENT or CONTENT_TYPE_PAGE
     */
    String contentType;

    /**
     * the page retrieved from the source server
     */
    PageForXmlRpc sourcePageForXmlRpc = null;
    private AttachmentForXmlRpc sourceAttachmentForXmlRpc = null;

    /**
     * This should be a paritally filled int content object such as PageForXmlRpc or AttachmentForXmlRpc 
     */
    private Object contentForXmlRpc = null;

    public static ContentEntityForSync createFromSyncStorageString(SpaceSyncDefinition spaceSyncDef, String contentId, String contentType, Object contentForXmlRpc) {
        ContentEntityForSync ceForSync = new ContentEntityForSync();
        ceForSync.spaceSyncDefinition = spaceSyncDef;
        ceForSync.contentSourceId = contentId;
        ceForSync.contentType = contentType;
        ceForSync.contentForXmlRpc = contentForXmlRpc;
        return ceForSync;
    }


    public Object getContentForXmlRpc() {
        return contentForXmlRpc;
    }

    public String getSpaceName() {
        return spaceSyncDefinition.getSourceSpaceName();
    }

    public String getContentSourceId() {
        return contentSourceId;
    }

    public void setContentSourceId(String contentSourceId) {
        this.contentSourceId = contentSourceId;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getSourceServerAlias() {
        return spaceSyncDefinition.getSourceServer().getServerAlias();
    }

    public String getSourceServerId() {
        return spaceSyncDefinition.getSourceServer().getId();
    }

    /**
     * sends back an object of type ConfluenceServerSettings for use with the Confluence Java remote
     * @return
     */
    public ConfluenceServerSettings getTargetServer() {
        return spaceSyncDefinition.getTargetServer().getConfluenceServerSettingsForXmlRpc();
    }

    /**
     * sends back an object of type ConfluenceServerSettings for use with the Confluence Java remote
     * @return
     */
    public ConfluenceServerSettings getSourceServer() {
        return spaceSyncDefinition.getSourceServer().getConfluenceServerSettingsForXmlRpc();
    }

    public String getTargetSpaceKey() {
        return spaceSyncDefinition.getSourceSpaceKey();
    }

    /**
     * get the source page from the server
     * @return
     */
    public PageForXmlRpc getSourcePageForXmlRpc() {

        if (this.sourcePageForXmlRpc!=null) return sourcePageForXmlRpc; 
        RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
        ConfluenceServerSettings sourceServerSettings = spaceSyncDefinition.getSourceServer().getConfluenceServerSettingsForXmlRpc();

        try {
            sourcePageForXmlRpc = rwb.getPage(sourceServerSettings, contentSourceId);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XmlRpcException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return sourcePageForXmlRpc;
    }

    /**
     * call this first to force a new get
     */
    public void resetSourcePage() {
        this.sourcePageForXmlRpc = null;
    }

    public String getTitle() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public String getSourceServerPassword() {
        return spaceSyncDefinition.getSourceServer().getPassword();
    }

    public String getSourceServerLogin() {
        return spaceSyncDefinition.getSourceServer().getLogin();
    }
}
