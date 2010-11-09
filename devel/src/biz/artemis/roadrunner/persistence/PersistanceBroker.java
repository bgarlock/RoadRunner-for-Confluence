package biz.artemis.roadrunner.persistence;

import biz.artemis.roadrunner.model3.Persistable;
import com.thoughtworks.xstream.XStream;

import java.util.Map;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileInputStream;

/**
 * Encapsulates methods related to persisting and re-hydrating data locally. Currently
 * the target persistence mechanism is XML, but that could change to a db via Hibernate
 * <p/>
 * 'save' means save to a file
 * 'load' means load from a file
 */
public class PersistanceBroker {

    /// start singleton code ///
    protected static PersistanceBroker instance = new PersistanceBroker();

    public static PersistanceBroker getInstance() {
        return instance;
    }

    private PersistanceBroker() {
    }
    /// end singleton code ///


    /**
     * persist all the pages to an XML respresentation
     *
     * @param allRemoteServerPage
     * @param url
     * @param remoteSpace
     */
    public void persistPages(Map allRemoteServerPage, String url, String remoteSpace) {
        String fileName = "confPageInfo-" + remoteSpace + "-" + url.hashCode();
    }

    /**
     * convenience method to get a file handle to a file in the data dir
     *
     * @param fileName
     * @return
     */
    public File getDataFile(String fileName) {
        File dataDir = new File(getDataDirectoryLocation());
        if (!dataDir.exists()) {
            // data dir does not exist so create it
            boolean dirCreated = dataDir.mkdir();
            if (!dirCreated) {
                return null;
            }
        }
        File userSettings = new File(getDataDirectoryLocation() + File.separator + fileName);
        return userSettings;
    }

    public static String getDataDirectoryLocation() {
        return "rrdata";
    }


    /**
     * serialize an object to XML
     *
     * @param targetObj
     * @param fileName
     */
    public void serializeToXML(Object targetObj, String fileName) {
        FileWriter fw = null;
        try {
            File targetFile = getDataFile(fileName);
            fw = new FileWriter(targetFile);
            XStream xstream = new XStream();
            xstream.toXML(targetObj, fw);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

        }
    }

    public String serializeToXML(Object targetObj) {
        XStream xstream = new XStream();
        String xml = xstream.toXML(targetObj);
        return xml;
    }

    /**
     * Get the content of a file and put it in a byte array
     */
    public static String file2String(String fileName) {
        String content = "";
        FileInputStream insr = null;

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                return null;
            }
            insr = new FileInputStream(file);

            byte[] fileBuffer = new byte[(int) file.length()];
            insr.read(fileBuffer);
            insr.close();
            content = new String(fileBuffer);
        } catch (Exception e) {
            System.err.println("Unhandled exception:");
            e.printStackTrace();
        } finally {
            if (insr != null)
                try {
                    insr.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
            return content;
        }

    public void save(Persistable persistable, String fileName) {
        serializeToXML(persistable, fileName);
    }

    public Object load(String fileName) {
        File file = getDataFile(fileName);
        if (!file.exists()) {
            return null;
        }
        String xmlUserSetings = file2String(file.getPath());
        XStream xstream = new XStream();
        Object deserializedObject = xstream.fromXML(xmlUserSetings);


        return deserializedObject;
    }
}
