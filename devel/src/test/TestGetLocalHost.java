package test;
import java.net.InetAddress;
import java.net.UnknownHostException;
public class TestGetLocalHost {
    public static void main(String[] args) {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println("localhost = "+ localhost.toString());
        } catch (UnknownHostException e) {
            System.out.println("Unable to set localhost. This prevents creation of a GUID. Cause was: " + e.getMessage());
        }
    }
}
