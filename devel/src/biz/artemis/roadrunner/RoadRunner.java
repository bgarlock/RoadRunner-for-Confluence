package biz.artemis.roadrunner;

import biz.artemis.confluence.xmlrpcwrapper.RemoteWikiBroker;
import biz.artemis.roadrunner.ui.RoadRunnerGUI;
import org.apache.log4j.PropertyConfigurator;


import javax.swing.*;
import java.net.URL;
import java.util.Properties;

/**
 * This class is the top level management class which kicks off the
 * UI
 */
public class RoadRunner {
    protected RemoteWikiBroker rwb = null;
    private static RoadRunner instance = new RoadRunner();
//    public SpaceManagerForm spaceMangerForm = null;
    //    public RRMainForm rrMainForm;
    static RoadRunnerGUI roadRunnerGUI = null;

    public static RoadRunner getInstance() {
        return instance;
    }

    private RoadRunner() {
    }

    public static void main(String[] args) {
        RoadRunner rr = RoadRunner.getInstance();
        rr.init();
        RoadRunnerGUI gui = new RoadRunnerGUI();
        roadRunnerGUI = gui;
        JFrame mainFrame = gui.getMainFrame();
        mainFrame.setVisible(true);
//        RRProgressDashboard progressDashboard = RRProgressDashboard.getInstance();
//        progressDashboard.setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(new ShutDownController(rr)));
    }

    private void initializeLogging() {
//        URL url = this.getClass().getResource( "log4j.properties" );
//        PropertyConfigurator.configure(url);
        Properties prop = new Properties();

        prop.setProperty("log4j.rootCategory","DEBUG, A1, A2");
        prop.setProperty("log4j.logger.RRProgressLogger","INFO, ProgressDashboardAppender");
        prop.setProperty("log4j.appender.A1","org.apache.log4j.ConsoleAppender");
        prop.setProperty("log4j.appender.A1.layout","org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.A1.layout.ConversionPattern","%d %-5p [%t] %c{2} - %m%n");
        prop.setProperty("log4j.appender.A2","org.apache.log4j.FileAppender");
        prop.setProperty("log4j.appender.A2.layout","org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.A2.layout.ConversionPattern","%d %-5p [%t] %c{2} - %m%n");
        prop.setProperty("log4j.appender.A2.File","rr.log");
        prop.setProperty("log4j.appender.ProgressDashboardAppender","biz.artemis.roadrunner.ui.JTextAreaAppender");
        prop.setProperty("log4j.appender.ProgressDashboardAppender.layout","org.apache.log4j.PatternLayout");
        prop.setProperty("log4j.appender.ProgressDashboardAppender.layout.ConversionPattern","%m%n");

        PropertyConfigurator.configure(prop);
//        PropertyConfigurator.configure("log4j.properties");
    }

    protected void init() {
        initializeLogging();
        rwb = RemoteWikiBroker.getInstance();
    }


    public static RoadRunnerGUI getRoadRunnerGUI() {
        return roadRunnerGUI;
    }
}
