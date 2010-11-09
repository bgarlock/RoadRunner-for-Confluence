package biz.artemis.roadrunner.model2;

import biz.artemis.confluence.xmlrpcwrapper.AttachmentForXmlRpc;
import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;

import java.util.Hashtable;

/**
 *
 * Wrap PageForXmlRpc and is what we'll persist out to XML.
 *
 * Part of the purpose of this class is so that we can easily add metadata associated with the object
 * page without having to hunt around for a place to store it.
 *
 */
public class RRAttachment {

    AttachmentForXmlRpc attachmentForXmlRpc;

    public RRAttachment(AttachmentForXmlRpc attachmentForXmlRpc) {
        this.attachmentForXmlRpc = attachmentForXmlRpc;
    }

    ////////////////////////////////////////////
    // everything below here is wrapper code for the AttachmentForXmlRpc functionality
    ////////////////////////////////////////////

    public String getComment() {
        return attachmentForXmlRpc.getComment();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setComment(String comment) {
        attachmentForXmlRpc.setComment(comment);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getContentType() {
        return attachmentForXmlRpc.getContentType();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setContentType(String contentType) {
        attachmentForXmlRpc.setContentType(contentType);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getCreated() {
        return attachmentForXmlRpc.getCreated();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setCreated(String created) {
        attachmentForXmlRpc.setCreated(created);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getCreator() {
        return attachmentForXmlRpc.getCreator();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setCreator(String creator) {
        attachmentForXmlRpc.setCreator(creator);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getFileName() {
        return attachmentForXmlRpc.getFileName();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setFileName(String fileName) {
        attachmentForXmlRpc.setFileName(fileName);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getFileSize() {
        return attachmentForXmlRpc.getFileSize();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setFileSize(String fileSize) {
        attachmentForXmlRpc.setFileSize(fileSize);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getId() {
        return attachmentForXmlRpc.getId();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setId(String id) {
        attachmentForXmlRpc.setId(id);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getPageId() {
        return attachmentForXmlRpc.getPageId();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setPageId(String pageId) {
        attachmentForXmlRpc.setPageId(pageId);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getTitle() {
        return attachmentForXmlRpc.getTitle();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setTitle(String title) {
        attachmentForXmlRpc.setTitle(title);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getUrl() {
        return attachmentForXmlRpc.getUrl();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setUrl(String url) {
        attachmentForXmlRpc.setUrl(url);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getFileLocation() {
        return attachmentForXmlRpc.getFileLocation();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setFileLocation(String fileLocation) {
        attachmentForXmlRpc.setFileLocation(fileLocation);    //To change body of overridden methods use File | Settings | File Templates.
    }

/**
 * can't remember what these 3 methods were for. may need them back - 2008-08-22
  */
//    public Hashtable<String, String> getAttachmentParams() {
//        return attachmentForXmlRpc.getAttachmentParams();    //To change body of overridden methods use File | Settings | File Templates.
//    }

//    public void setAttachmentParams(Object attachmentParams) {
//        attachmentForXmlRpc.setAttachmentParams(attachmentParams);    //To change body of overridden methods use File | Settings | File Templates.
//    }

//    public AttachmentForXmlRpc cloneForNewAttachment(String spaceKey) {
//        return attachmentForXmlRpc.cloneForNewAttachment(spaceKey);    //To change body of overridden methods use File | Settings | File Templates.
//    }
}
