package biz.artemis.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.InetAddress;
import java.security.SecureRandom;
import java.util.Date;
import java.util.ArrayList;

public class UniqueID {
    //    static Category log = Logger.getLogger(UniqueID.class.getName());
    Logger log = Logger.getLogger(UniqueID.class.getName());
    static UniqueID instance = null;

    public static UniqueID getInstance() {
        if (instance == null) {
            instance = new UniqueID();
        }
        return instance;
    }

    private UniqueID() {
    }


//    public String getUID() {
//        String strRetVal = "";
//        String strTemp = "";
//        try {
//            // Get IPAddress Segment
//            InetAddress addr = InetAddress.getLocalHost();
//
//            byte[] ipaddr = addr.getAddress();
//            for (int i = 0; i < ipaddr.length; i++) {
//                Byte b = new Byte(ipaddr[i]);
//
//                strTemp = Integer.toHexString(b.intValue() & 0x000000ff);
//                while (strTemp.length() < 2) {
//                    strTemp = '0' + strTemp;
//                }
//                strRetVal += strTemp;
//            }
//
//            strRetVal += ':';
//
//            //Get CurrentTimeMillis() segment
//            strTemp = Long.toHexString(System.currentTimeMillis());
//            while (strTemp.length() < 12) {
//                strTemp = '0' + strTemp;
//            }
//            strRetVal += strTemp + ':';
//
//            //Get Random Segment
//            SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
//
//            strTemp = Integer.toHexString(prng.nextInt());
//            while (strTemp.length() < 8) {
//                strTemp = '0' + strTemp;
//            }
//
//            strRetVal += strTemp.substring(4) + ':';
//
//            //Get IdentityHash() segment
//            strTemp = Long.toHexString(System.identityHashCode((Object) this));
//            while (strTemp.length() < 8) {
//                strTemp = '0' + strTemp;
//            }
//            strRetVal += strTemp;
//
//        }
//        catch (java.net.UnknownHostException ex) {
//            log.error("Unknown Host Exception Caught: " + ex.getMessage());
//        }
//        catch (java.security.NoSuchAlgorithmException ex) {
//            log.error("No Such Algorithm Exception Caught: " + ex.getMessage());
//        }
//
//        return strRetVal.toUpperCase();
//    }
//
    private static void initializeLogging() {
        PropertyConfigurator.configure("log4j.properties");
    }

    public static void main(String[] args) throws Exception {
        initializeLogging();
        for (int i = 0; i < 10; i++) {
            long lngStart = System.currentTimeMillis();
            UniqueID objUniqueID = UniqueID.getInstance();

            objUniqueID.log.info(objUniqueID.createUniqueId());
            objUniqueID.log.debug("Elapsed time: " + (System.currentTimeMillis() - lngStart));
        }
    }

    ArrayList<String> uniqueIdList = new ArrayList<String>();

    /**
     * creates a unique id
     *
     * @return
     */
    public String createUniqueId() {
        long timestamp = (new Date()).getTime();
        String hexstr = Long.toString(timestamp, 36);
        while (uniqueIdList.contains(hexstr)) {
            // if the unique id has alreay been used then keep incrementing
            timestamp++;
            hexstr = Long.toString(timestamp, 36);
        }
        uniqueIdList.add(hexstr);
        StringBuilder uniqueId = new StringBuilder(hexstr);
        // now we'll massage the string a bit more
        uniqueId = uniqueId.delete(0, 1);
        uniqueId = uniqueId.reverse();
        int firstChar = uniqueId.charAt(0);
        if (('0' <= firstChar) && (firstChar <= '9')) {
            // if the first char is a number move it into a letter
            // by boosting its ascii value
            firstChar = firstChar + 50;
            uniqueId = uniqueId.deleteCharAt(0);
            uniqueId = uniqueId.insert(0, firstChar);
        }
        return uniqueId.toString();
    }

}