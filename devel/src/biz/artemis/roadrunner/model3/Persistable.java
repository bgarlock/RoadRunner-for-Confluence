package biz.artemis.roadrunner.model3;

import biz.artemis.roadrunner.persistence.PersistanceBroker;

/**
 * Superclass which is meant to make 
 */
public class Persistable {

    public void save(String fileName) {
        PersistanceBroker pb = PersistanceBroker.getInstance();
        pb.save(this, fileName);
    }
    public Object load(String fileName) {
        PersistanceBroker pb = PersistanceBroker.getInstance();
        return pb.load(fileName);
    }
}
