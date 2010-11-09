package biz.artemis.roadrunner.engine.syncactions;

import biz.artemis.confluence.xmlrpcwrapper.ConfluenceServerSettings;
import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;
import biz.artemis.roadrunner.model3.ContentVersionListContainer;
import biz.artemis.roadrunner.model3.ContentIdMappingContainer;
import biz.artemis.roadrunner.model3.ContentEntityForSync;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.io.IOException;

/**
 *
 */
public class SetAllPageParentsAction extends SyncAction {
    /**
     * server with list of pages lacking parent ids or needing those set
     */
    public ConfluenceServerSettings originServer;
    public Collection<PageForXmlRpc> originPages;
    Logger log = Logger.getLogger(this.getClass());
    public ContentVersionListContainer liveContentContainer;

    /**
     * walk through all the pages in the targetSpaceWrapper
     *
     * @return
     */
    public SyncActionResult execute() throws IOException, XmlRpcException {
        DualHashBidiMap<String, String> idMapRemoteToLocal = ContentIdMappingContainer.getInstance().getIdMapAsBidiMap();
        Map<String, ContentEntityForSync> sourcePageMap = liveContentContainer.getContentEntitiesForSyncMap();


        Map<String, List<ContentEntityForSync>> contentListsGroupedBySpace = liveContentContainer.getContentBySpace();
        Map<String, PageForXmlRpc> targetPageMap = null;
        // iterate through all of the spaces with pages to check
        for (String serverAliasAndSpaceName : contentListsGroupedBySpace.keySet()) {
            List<ContentEntityForSync> contentEntitiesInSpace = contentListsGroupedBySpace.get(serverAliasAndSpaceName);
            // iterate through all of the pages in each space
            for (ContentEntityForSync sourceContentEntity : contentEntitiesInSpace) {
                if (!sourceContentEntity.getContentType().equals(ContentEntityForSync.CONTENT_TYPE_PAGE)) {
                    // if this is not a Page but rather something like an attachment then skip
                    continue;

                }
                if (targetPageMap == null) {
                    targetPageMap = rwb.getAllServerPageSummariesMapById(sourceContentEntity.getTargetServer(), sourceContentEntity.getTargetSpaceKey());
                }

                String sourceServerId = sourceContentEntity.getSourceServerId();
                String sourcePageId = sourceContentEntity.getSourcePageForXmlRpc().getId();
                String sourceParentPageId = sourceContentEntity.getSourcePageForXmlRpc().getParentId();


                // get mapped target page id
                String mappedTargetPageId = idMapRemoteToLocal.get(sourceServerId+":"+sourcePageId);
                String mappedTargetParentId = idMapRemoteToLocal.get(sourceServerId+":"+sourceParentPageId);

                if (sourceParentPageId.equals("0")) {
                    updatePageParentId(sourceContentEntity, mappedTargetPageId, "0");
                    continue;
                }


                // cross reference the source page's parent id with what is mapped
                String realTargetParentPageId = targetPageMap.get(mappedTargetPageId).getParentId();

                if (realTargetParentPageId.equals(mappedTargetParentId)) {
                    // no update needed
                } else {
                    // set the new page's parent
                    updatePageParentId(sourceContentEntity, mappedTargetPageId, mappedTargetParentId);
                }

            }
            // reset the map with the page summaries of the target server to null when we've gone through that space's pages
            targetPageMap = null;

        }
        return SyncActionResult.getSuccessfulResult();
    }

    public void updatePageParentId(ContentEntityForSync sourceContentEntity, String mappedTargetPageId, String mappedTargetParentId) throws IOException, XmlRpcException {
        PageForXmlRpc targetPage = rwb.getPage(sourceContentEntity.getTargetServer(), mappedTargetPageId);
        targetPage.setParentId(mappedTargetParentId);
        rwb.storeNewOrUpdatePage(sourceContentEntity.getTargetServer(), targetPage.getSpace(), targetPage);

    }

//    /**
//     * walk through all the pages in the targetSpaceWrapper
//     *
//     * @return
//     */
//    public SyncActionResult executeOrig() {
//        // need target pages which should now have page summaries
//        Map<String, PageForXmlRpc> targetPagesByIdMap = targetSpaceWrapper.getPagesByIdMap();
//        DualHashBidiMap<String, String> pageIdToPeerPageIdMap = new DualHashBidiMap<String, String>(targetSpaceWrapper.getSpaceSyncData().pageIdToPeerPageIdMap);
//        DualHashBidiMap originPageMap = createPageIdMapFromCollection(originPages);
////        pageIdToPeerPageIdMap.
//        // iterate through page
//        // @todo - ======================================
//        // @todo - redo this stuff with jakarta commons collections, bidimap and mapiterator woot!
//        // @todo - ======================================
//
//        for (PageForXmlRpc targetPage : targetPagesByIdMap.values()) {
//            //PageForXmlRpc targetPage = pageForXmlRpc1;
//            // get page id
//            String targetPageId = targetPage.getId();
//            // get matching peer page
//            String peerPageId = (String) pageIdToPeerPageIdMap.get(targetPageId);
//            PageForXmlRpc peerPage = (PageForXmlRpc) originPageMap.get(peerPageId);
//            // get the parent id of peer
//            // @todo - target page does not have id...needs collection with page ids
//            if (peerPage == null) {
//                log.error("targetPage = " + targetPage.getId() + " : " + targetPage.getTitle());
//            }
//            log.debug("peer page = " + peerPage.getTitle());
//            String peerParentPageId = peerPage.getParentId();
//            if (peerParentPageId.equals("0")) {
//                // this is the home page for the space, set accordingly
//                targetPage.setParentId("0");
//                continue;
//            }
////            PageForXmlRpc peerParentPage = (PageForXmlRpc) originPageMap.get(peerParentPageId);
//            // get target page id matching parent peer
//            String targetParentId = (String) pageIdToPeerPageIdMap.getKey(peerParentPageId);
//            // set parent page id to target parent page id
//            targetPage.setParentId(targetParentId);
//        }
//
//        for (PageForXmlRpc targetPage : targetPagesByIdMap.values()) {
//            try {
//                PageForXmlRpc page = rwb.storeNewOrUpdatePage(targetSpaceWrapper.getSpaceSettings(),
//                        targetSpaceWrapper.getSpaceKey(),
//                        targetPage);
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            } catch (XmlRpcException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//        return SyncActionResult.getSuccessfulResult();
//    }


    /**
     * create a <pageId,page> DualHashBidMap from a collection of pages
     *
     * @param originPages
     * @return
     */
    public DualHashBidiMap createPageIdMapFromCollection(Collection<PageForXmlRpc> originPages) {
        DualHashBidiMap retMap = new DualHashBidiMap();
        for (PageForXmlRpc originPage : originPages) {
            retMap.put(originPage.getId(), originPage);
        }
        return retMap;  //To change body of created methods use File | Settings | File Templates.
    }

    public String getSpaceMessage() {
//        return targetSpaceWrapper.getSpaceKey();
        return "setting page parents";
    }

    public void validateData() {
//        if (targetSpaceWrapper == null) {
//            log.error("csswOrigin was null when it should not have been");
//        }
        if (originServer == null) {
            log.error("csswOrigin was null when it should not have been");
        }
        if (originPages == null) {
            log.error("csswOrigin was null when it should not have been");
        }
    }

    public String getPageName() {
        return "all pages";
    }

    public String getWikiObjectType() {
        return "building hierarchy";
    }
}
