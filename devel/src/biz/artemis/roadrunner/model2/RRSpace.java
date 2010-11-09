package biz.artemis.roadrunner.model2;

import org.apache.commons.collections15.map.HashedMap;
import biz.artemis.confluence.xmlrpcwrapper.SpaceForXmlRpc;

import java.util.Hashtable;

/**
 * @todo - fill this in
 * User: brendan
 * Date: Jun 22, 2007
 * Time: 4:50:27 PM
 */
public class RRSpace {
    SpaceForXmlRpc spaceForXmlRpc;
    HashedMap<String, RRPage> pageMap;

    public HashedMap<String, RRPage> getPageMap() {
        return pageMap;
    }

    public void setPageMap(HashedMap<String, RRPage> pageMap) {
        this.pageMap = pageMap;
    }

    //////////////////
    // START - SpaceForXml wrapper code
    //////////////////
    public Hashtable<String, String> getSpaceParams() {
        return spaceForXmlRpc.getSpaceParams();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setSpaceParams(Hashtable spaceParams) {
        spaceForXmlRpc.setSpaceParams(spaceParams);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getId() {
        return spaceForXmlRpc.getId();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setId(String idVal) {
        spaceForXmlRpc.setId(idVal);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getSpaceName() {
        return spaceForXmlRpc.getSpaceName();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setSpaceName(String spaceNameVal) {
        spaceForXmlRpc.setSpaceName(spaceNameVal);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getSpaceKey() {
        return spaceForXmlRpc.getSpaceKey();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setSpaceKey(String spaceKey) {
        spaceForXmlRpc.setSpaceKey(spaceKey);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getURL() {
        return spaceForXmlRpc.getURL();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setURL(String urlVal) {
        spaceForXmlRpc.setURL(urlVal);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getHomepage() {
        return spaceForXmlRpc.getHomepage();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setHomepage(String homepageVal) {
        spaceForXmlRpc.setHomepage(homepageVal);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public String getDescription() {
        return spaceForXmlRpc.getDescription();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void setDescription(String descriptionVal) {
        spaceForXmlRpc.setDescription(descriptionVal);    //To change body of overridden methods use File | Settings | File Templates.
    }

    //////////////////
    // END - SpaceForXml wrapper code
    //////////////////
}
