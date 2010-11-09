package biz.artemis.roadrunner.model2;

import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;

import java.util.Hashtable;

import org.apache.commons.collections15.bidimap.DualHashBidiMap;
import org.apache.commons.collections15.map.HashedMap;

/**
 * Wrap PageForXmlRpc and is what we'll persist out to XML.
 *
 * Part of the purpose of this class is so that we can easily add metadata associated with the object
 * page without having to hunt around for a place to store it.
 *
 */
public class RRPage {
    PageForXmlRpc pageForXmlRpc;
    HashedMap<String, RRAttachment> attachments;

    public RRPage(PageForXmlRpc pageForXmlRpc) {
        this.pageForXmlRpc = pageForXmlRpc;
    }

    public HashedMap<String, RRAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(HashedMap<String, RRAttachment> attachments) {
        this.attachments = attachments;

    }

    ////////////////////////////////////////////
    // START -  PageForXmlRpc wrapper code
    ////////////////////////////////////////////
    public Hashtable<String, String> getPageParams() {
        return pageForXmlRpc.getPageParams();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setPageParams(Object pageParams) {
        pageForXmlRpc.setPageParams(pageParams);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getId() {
        return pageForXmlRpc.getId();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setId(String idVal) {
        pageForXmlRpc.setId(idVal);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getSpace() {
        return pageForXmlRpc.getSpace();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setSpace(String spaceVal) {
        pageForXmlRpc.setSpace(spaceVal);
    }

    public String getParentId() {
        return pageForXmlRpc.getParentId();
    }

    public void setParentId(String parentIdVal) {
        pageForXmlRpc.setParentId(parentIdVal);
    }

    public String getTitle() {
        return pageForXmlRpc.getTitle();
    }

    public void setTitle(String titleVal) {
        pageForXmlRpc.setTitle(titleVal);
    }

    public String getUrl() {
        return pageForXmlRpc.getUrl();
    }

    public void setUrl(String urlVal) {
        pageForXmlRpc.setUrl(urlVal);
    }

    public String getVersion() {
        return pageForXmlRpc.getVersion();
    }

    public void setVersion(String versionVal) {
        pageForXmlRpc.setVersion(versionVal);
    }

    public String getContent() {
        return pageForXmlRpc.getContent();
    }

    public void setContent(String contentVal) {
        pageForXmlRpc.setContent(contentVal);
    }

    public String getCreated() {
        return pageForXmlRpc.getCreated();
    }

    public void setCreated(String createdVal) {
        pageForXmlRpc.setCreated(createdVal);
    }

    public String getCreator() {
        return pageForXmlRpc.getCreator();
    }

    public void setCreator(String creatorVal) {
        pageForXmlRpc.setCreator(creatorVal);
    }

    public String getModified() {
        return pageForXmlRpc.getModified();
    }

    public void setModified(String modifiedVal) {
        pageForXmlRpc.setModified(modifiedVal);
    }

    public String getModifier() {
        return pageForXmlRpc.getModifier();
    }

    public void setModifier(String modifierVal) {
        pageForXmlRpc.setModifier(modifierVal);
    }

    public String getHomePage() {
        return pageForXmlRpc.getHomePage();
    }

    public void setHomePage(String homePageVal) {
        pageForXmlRpc.setHomePage(homePageVal);
    }

    public String getLocks() {
        return pageForXmlRpc.getLocks();
    }

    public void setLocks(String locksVal) {
        pageForXmlRpc.setLocks(locksVal);
    }

    public String getContentStatus() {
        return pageForXmlRpc.getContentStatus();
    }

    public void setContentStatus(String contentStatusVal) {
        pageForXmlRpc.setContentStatus(contentStatusVal);
    }

    public String getCurrent() {
        return pageForXmlRpc.getCurrent();
    }

    public void setCurrent(String currentVal) {
        pageForXmlRpc.setCurrent(currentVal);
    }

    public PageForXmlRpc cloneForNewPage(String spaceKey) {
        return pageForXmlRpc.cloneForNewPage(spaceKey);
    }

    public void setParentTitle(String parentTitle) {
        pageForXmlRpc.setParentTitle(parentTitle);
    }

    public String getParentTitle() {
        return pageForXmlRpc.getParentTitle();
    }
    ////////////////////////////////////////////
    // END -  PageForXmlRpc wrapper code 
    ////////////////////////////////////////////
}
