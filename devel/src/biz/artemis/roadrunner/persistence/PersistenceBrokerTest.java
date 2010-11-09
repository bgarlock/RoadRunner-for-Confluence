package biz.artemis.roadrunner.persistence;

import junit.framework.TestCase;
import biz.artemis.roadrunner.model3.UserSettings;


public class PersistenceBrokerTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void testPersistUserSettings() {
        PersistanceBroker pb = PersistanceBroker.getInstance();
        UserSettings us = UserSettings.getInstance();
//        us.setLocal(new ConfluenceServerSettings());
//        us.getLocal().setLogin("test");
//        us.getLocal().setPassword("test");
//        String stringVersion1 = pb.serializeToXML(us);
//        us.save();
//
//        us.setLocal(null);
//        us.setRemote(null);
//
//        us.load();
//        String stringVersion2 = pb.serializeToXML(UserSettings.getInstance());
//        assertEquals(stringVersion1, stringVersion2);

    }
}
