package biz.artemis.roadrunner.engine.syncactions;

import biz.artemis.roadrunner.engine.syncactions.SyncAction;
import biz.artemis.roadrunner.model3.ContentEntityForSync;
import biz.artemis.roadrunner.model3.ContentIdMappingContainer;
import biz.artemis.roadrunner.persistence.PersistanceBroker;
import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;
import biz.artemis.confluence.xmlrpcwrapper.AttachmentForXmlRpc;
import biz.artemis.util.Util;
import biz.artemis.util.FileDownload;

import java.io.IOException;
import java.io.File;

import org.apache.xmlrpc.XmlRpcException;

/**
 * SyncAction which creates a new page.
 */
public class CreatePageSyncAction extends SyncAction {

    // @todo finish adding space wrapper
    /**
     * This represents both the proto-page before it is sent to the server and then the
     * pointer becomes the page that the server returns with the id filled in
     */
    public PageForXmlRpc originPage;
    public int pageOrigin = 0;
    public String originPageId = null;

    public static final int PAGE_ORIGIN_IS_REMOTE = 1;
    public static final int PAGE_ORIGIN_IS_LOCAL = 2;
    public ContentEntityForSync pageEntity;
    private String sourcePageName = null;

    public SyncActionResult execute() {
        //RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
        PageForXmlRpc newPage = null;
        ContentIdMappingContainer idMapContainer = ContentIdMappingContainer.getInstance();
        PageForXmlRpc sourcePage = null;
        PageForXmlRpc targetPage = null;
        try {
            Util.sleepForTesting();

            if (pageEntity.getContentType().equals(ContentEntityForSync.CONTENT_TYPE_ATTACHMENT)) {
                return syncAttachment(pageEntity, idMapContainer);
            }


            sourcePage = pageEntity.getSourcePageForXmlRpc();
            targetPage = sourcePage.cloneForNewPage(sourcePage.getSpace());
            String localId = idMapContainer.getIdMappingFromSourceToTarget(pageEntity.getSourceServerId(), sourcePage.getId());
            if (localId != null) targetPage.setId(localId);
            String pageName = sourcePage.getTitle();
            setPageName(pageName);
            newPage = rwb.storeNewOrUpdatePage(pageEntity.getTargetServer(), pageEntity.getTargetSpaceKey(), targetPage);
            idMapContainer.putIdMappingFromSourceToTarget(pageEntity.getSourceServerId(), sourcePage.getId(), newPage.getId());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (XmlRpcException e) {
            log.info("page added may already exist or there was an issue such as a bad local page id:  " + sourcePageName);
            // try again - this could happen if the local page was removed
            //      and an old mapped local id doesn't exist so
            try {
                targetPage.removeId();
                newPage = rwb.storeNewOrUpdatePage(pageEntity.getTargetServer(), pageEntity.getTargetSpaceKey(), targetPage);
                idMapContainer.putIdMappingFromSourceToTarget(pageEntity.getSourceServerId(), sourcePage.getId(), newPage.getId());
            } catch (IOException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (XmlRpcException e1) {
                log.info("page added may already exist or there was an issue such as a bad local page id:  " + sourcePageName);
            }

        }


        // @todo - insert logic here to link the new page id and the old page id - OR maybe we don't need this??


        return SyncActionResult.getSuccessfulResult();
    }

    SyncActionResult syncAttachment(ContentEntityForSync attachmentEntityForSync, ContentIdMappingContainer idMapContainer) {
        //      this is probably part of the data that will get persisted to XML

        // get parent page id

        // 
        // get attachment URL
        AttachmentForXmlRpc attachment = (AttachmentForXmlRpc) attachmentEntityForSync.getContentForXmlRpc();
        String downloadURL = attachment.getUrl();
        if (downloadURL == null || downloadURL.length() < 1) {
            String errorMessage = "problem downloading attachment: " + attachment.getFileName();
            pLog.error(errorMessage);
            return SyncActionResult.createError(errorMessage);
        }

        // download the attachment via the URL from the origin page
        // @todo - needs to be replaced with preference location
        //UserSettings.getInstance().getAttachmentTargetLoc(attachment.getFileName());
        String directoryPath = PersistanceBroker.getDataDirectoryLocation() + File.separator + attachmentEntityForSync.getSourceServerAlias() ;
        String fileLoc = directoryPath + File.separator + attachment.getFileName();
        createDirectory(directoryPath);
        downloadURL += "&os_username=" + attachmentEntityForSync.getSourceServerLogin() + "&os_password=" + attachmentEntityForSync.getSourceServerPassword();
        FileDownload.download(downloadURL, fileLoc);
        AttachmentForXmlRpc newAttachment = attachment.cloneForNewAttachment();
        newAttachment.setFileLocation(fileLoc);

        // send attachment to target page
        try {
            String sourceParentPageId = attachment.getPageId();
            String targetPageId = idMapContainer.getIdMappingFromSourceToTarget(attachmentEntityForSync.getSourceServerId(), sourceParentPageId);
            rwb.storeAttachment(attachmentEntityForSync.getTargetServer(),
                    targetPageId,
                    newAttachment);
        } catch (IOException e) {
            String errorMessage = e.getMessage();
            pLog.error(errorMessage);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return SyncActionResult.createError(errorMessage);
        } catch (XmlRpcException e) {
            String errorMessage = e.getMessage();
            pLog.error(errorMessage);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return SyncActionResult.createError(errorMessage);
        }

        return SyncActionResult.getSuccessfulResult();

    }

    private void createDirectory(String directoryPath) {
        File dirPath = new File(directoryPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }


    private void setPageName(String title) {
        sourcePageName = title;
    }

    public String getSpaceMessage() {
        return pageEntity.getSpaceName();
//        return "xxx";
//        return pageEntity.getSpaceName();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void validateData() {
//        if (spaceWrapper == null || spaceWrapper.getSpaceSettings() == null) {
//            log.error("bad space settings");
//        }
        if (pageOrigin == 0) {
            log.error("pageOrigin not set");
        }
        if (originPageId == null) {
            log.error("originPageId not set");
        }
    }

    public String getPageName() {
        if (pageEntity.getContentType().equals(ContentEntityForSync.CONTENT_TYPE_PAGE)) {
            return pageEntity.getSourcePageForXmlRpc().getTitle();
        }
        return ".";
    }

    public String getWikiObjectType() {
        return "page";  //To change body of implemented methods use File | Settings | File Templates.
    }


}
