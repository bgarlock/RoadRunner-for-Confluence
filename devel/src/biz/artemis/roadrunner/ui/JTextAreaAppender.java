package biz.artemis.roadrunner.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;


public class JTextAreaAppender extends AppenderSkeleton {

//    static Logger logger = Logger.getLogger(JTextAreaAppender.class);

    protected JTextArea jTextArea = null;
//    protected Layout layout = null;
    public static final String newline = System.getProperty("line.separator");
//    public static final String DEFAULT_CONVERSION_PATTERN = "%d [%t] %-5p %c - %m%n";


    public JTextAreaAppender(Layout layout) {
        this();
        setLayout(layout);
    }

    public JTextAreaAppender(Layout layout, JTextArea jTextArea) {
        this();
//        setLayout(layout);
        setJTextArea(jTextArea);
    }

    public JTextAreaAppender(JTextArea jTextArea) {
        this();
        setJTextArea(jTextArea);
    }

    public JTextAreaAppender() {
        super();
        name = "JTextAreaAppender";
//        this.layout = new PatternLayout(DEFAULT_CONVERSION_PATTERN);
        setJTextArea(null);
    }

    public void close() {
    }

    public boolean requiresLayout() {
        return true;
    }

    public void append(LoggingEvent event) {
        String text = getLayout().format(event);
        String trace[];

        if (jTextArea == null) {
            return;
            //createJFrame();
        }
        jTextArea.append(text);
        if ((trace = event.getThrowableStrRep()) != null) {
            for (int i = 0; i < trace.length; i++) {
                jTextArea.append(trace[i]); jTextArea.append(newline);
            }
        }
        // Set the caret to the end of the text to cause the window to automatically
        // scroll to the bottom.
        jTextArea.setCaretPosition(jTextArea.getText().length());

        // Keep the text area down to a certain character size
        int idealSize = 1000;
        int maxExcess = 300;
        int excess = jTextArea.getDocument().getLength() - idealSize;
        if (excess >= maxExcess) {
            jTextArea.replaceRange("", 0, excess);
        }

    }

//    public void createJFrame() {
//        JFrame jFrame = new JFrame("Default JTextAreaAppender Frame");
//        jTextArea = new JTextArea();
//        jTextArea.setEditable(false);
//        JScrollPane jScrollPane = new JScrollPane(jTextArea);
//        jScrollPane.setPreferredSize(new Dimension(400, 300));
//        Container contentPane = jFrame.getContentPane();
//        contentPane.add(jScrollPane, BorderLayout.CENTER);
//
//        jFrame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
//
//        jFrame.pack();
//        jFrame.setVisible(true);
//    }


//    public Layout getLayout() {
//        return layout;
//    }
//    public void setLayout(Layout layout) {
//        this.layout = layout;
//    }

    public JTextArea getJTextArea() {
        return jTextArea;
    }
    public void setJTextArea(JTextArea jTextArea) {
        this.jTextArea = jTextArea;
    }

//    static public void main(String args[]) {
//        // The preferred method is to create your frame like this,
//        // then create your appender and add it to your logger below.
//        // (If you don't create a frame, but use the JTextAreaAppender,
//        // a frame will be created for you.)
//        JFrame jFrame = new JFrame("My JTextAreaAppender Frame");
//        JTextArea jta = new JTextArea();
//        jta.setEditable(false);
//        JScrollPane jScrollPane = new JScrollPane(jta);
//        jScrollPane.setPreferredSize(new Dimension(500, 300));
//        Container contentPane = jFrame.getContentPane();
//        contentPane.add(jScrollPane, BorderLayout.CENTER);
//
//        jFrame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                System.exit(0);
//            }
//        });
//
//        jFrame.pack();
//        jFrame.setVisible(true);
//
//
//        JTextAreaAppender jtaa = new JTextAreaAppender(jta);
//        logger.addAppender(jtaa); // <-- THIS IS KEY.  WE ARE ADDING OUR APPENDER.
//
//        logger.debug("Here is my log message!");
//        logger.info("Last one.");
//        logger.warn("Bye.");
//    }
}

