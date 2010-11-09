package test;

import javax.swing.*;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: brendan
 * Date: Mar 10, 2009
 * Time: 4:57:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCode {

    public static void main(String[] args) {
        Calendar expDateCal = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        expDateCal.set(2009, 7, 7);
        if (now.after(expDateCal)) {
            System.out.println("WTF: "+now+"    "+expDateCal);
        }
    }
}
