package biz.artemis.roadrunner.ui;

import java.awt.*;
import javax.swing.border.*;
import com.intellij.uiDesigner.core.*;
import com.jgoodies.forms.layout.*;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.event.*;
import java.util.Enumeration;

public class RRProgressDashboard extends JDialog {
    private static RRProgressDashboard instance;

    protected Logger progressLogger = null;
    private int wikiObjectProgressBarValue = 0;
    private int spaceProgressBarValue = 0;

    public RRProgressDashboard() {
        initComponents();
        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        RRProgressDashboard dialog = new RRProgressDashboard();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


    public static RRProgressDashboard getInstance() {
        if (instance == null) {
            instance = new RRProgressDashboard();
        }
        instance.setSize(406, 469);
        return instance;
    }

    /**
     * start the progress bars as indeterminent progress
     */
    public void startIndeterminateProgress() {
        wikiObjectProgressBar.setIndeterminate(true);
        spaceProgressBar.setIndeterminate(true);
    }

    public void stopIndeterminateProgress() {
        wikiObjectProgressBar.setIndeterminate(false);
        spaceProgressBar.setIndeterminate(false);
    }

    /**
     * get a handle to the log4j Logger we're using to write to the text area
     * in the RRProgressDashboard. Also tie the logger's appender to the text area.
     *
     * @return
     */
    public Logger getLogger() {
        if (progressLogger == null) {
            progressLogger = Logger.getLogger("RRProgressLogger");
            // we don't want to inherit appenders, instead just set them explicitly
            progressLogger.setAdditivity(false);

            // get a handle to the appender configured in the log4j.properties file
            Enumeration appendersEnum = progressLogger.getAllAppenders();
            JTextAreaAppender jtaa = (JTextAreaAppender) progressLogger.getAppender("ProgressDashboardAppender");
            
            // point the appender at the JTextArea
            jtaa.setJTextArea(syncLogArea);
        }

//        //JTextAreaAppender jtaa = new JTextAreaAppender(syncLogArea);
//        jtaa.setJTextArea(syncLogArea);
//        Enumeration appendersEnum = progressLogger.getAllAppenders();
//        progressLogger.addAppender(jtaa); // <-- THIS IS KEY.  WE ARE ADDING OUR APPENDER.
        return progressLogger;
    }

    public void startProgress() {
        startIndeterminateProgress();
        setSpaceTextField("searching");
        setWikiObjectTextField("searching");

    }

    /**
     * set progress bars to complete
     */
    public void progressComplete() {
        stopIndeterminateProgress();
        int max = wikiObjectProgressBar.getMaximum();
        wikiObjectProgressBar.setValue(max);
        int spaceProgressMax = spaceProgressBar.getMaximum();
        spaceProgressBar.setValue(spaceProgressMax);
        getLogger().info("synchronization complete");
        setSpaceTextField("complete");
        setWikiObjectTextField("complete");
    }

    public void setTotalSpacesToSync(int totalSpacesCount) {
        spaceProgressBar.setMaximum(totalSpacesCount + 1);
    }

    public void setSpaceTextField(String text) {
        spaceNameTextField.setText(text);
    }

    public void setWikiObjectTextField(String text) {
        wikiObjectTextField.setText(text);
    }

    public JProgressBar getWikiObjectProgressBar() {
        return wikiObjectProgressBar;
    }

    public void incrementWikiObjectProgressBar() {
        wikiObjectProgressBarValue++;
        wikiObjectProgressBar.setValue(wikiObjectProgressBarValue);
    }

    /**
     * set the new max for the progress bar and reset the value to zero
     * @param totalSubActions
     */
    public void resetWikiObjectProgressBar(int totalSubActions) {
        spaceProgressBar.setIndeterminate(false);
        wikiObjectProgressBar.setIndeterminate(false);
        getWikiObjectProgressBar().setValue(0);
        wikiObjectProgressBarValue = 0;
        getWikiObjectProgressBar().setMaximum(totalSubActions);
    }

    /**
     * set the new max for the progress bar and reset the value to zero
     * @param totalSubActions
     */
    public void resetSpaceProgressBar(int totalSubActions) {
        spaceProgressBar.setIndeterminate(false);
        getSpaceProgressBar().setValue(0);
        spaceProgressBarValue = 0;
        getSpaceProgressBar().setMaximum(totalSubActions+1);
    }

    public JProgressBar getSpaceProgressBar() {
        return spaceProgressBar;
    }

    public void incrementSpaceProgressBar() {
        spaceProgressBarValue++;
        spaceProgressBar.setValue(spaceProgressBarValue);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel8 = new JPanel();
        contentPane = new JPanel();
        panel9 = new JPanel();
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        buttonOK = new JButton();
        buttonCancel = new JButton();
        JPanel panel3 = new JPanel();
        RoadRunnerLabel = new JLabel();
        spaceProgressBar = new JProgressBar();
        wikiObjectProgressBar = new JProgressBar();
        JPanel panel4 = new JPanel();
        JLabel label1 = new JLabel();
        wikiObjectTextField = new JTextField();
        JPanel panel5 = new JPanel();
        JScrollPane scrollPane1 = new JScrollPane();
        syncLogArea = new JTextArea();
        JPanel panel6 = new JPanel();
        JLabel label2 = new JLabel();
        spaceNameTextField = new JTextField();
        JPanel panel7 = new JPanel();
        allRadioButton = new JRadioButton();
        conflictsRadioButton = new JRadioButton();
        totalsRadioButton = new JRadioButton();
        CellConstraints cc = new CellConstraints();

        //======== panel8 ========
        {
            panel8.setLayout(new BorderLayout());

            //======== contentPane ========
            {
                contentPane.setBackground(Color.white);
                contentPane.setLayout(new GridLayoutManager(1, 1, new Insets(10, 10, 10, 10), -1, -1));

                //======== panel9 ========
                {
                    panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));

                    //======== panel1 ========
                    {
                        panel1.setBackground(Color.white);
                        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

                        //======== panel2 ========
                        {
                            panel2.setBackground(Color.white);
                            panel2.setLayout(new FlowLayout());

                            //---- buttonOK ----
                            buttonOK.setText("OK");
                            panel2.add(buttonOK);

                            //---- buttonCancel ----
                            buttonCancel.setText("Cancel");
                            panel2.add(buttonCancel);
                        }
                        panel1.add(panel2, new GridConstraints(0, 0, 1, 1,
                            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                            null, null, null));
                    }
                    panel9.add(panel1, new GridConstraints(1, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK,
                        null, null, null));

                    //======== panel3 ========
                    {
                        panel3.setBackground(Color.white);
                        panel3.setLayout(new FormLayout(
                            "default, left:4dlu, default:grow, left:4dlu, [4px,default]",
                            "default, 4*(top:4dlu, [4px,default]), top:4dlu, [4px,default]:grow, top:4dlu, [4px,default]"));

                        //---- RoadRunnerLabel ----
                        RoadRunnerLabel.setText("Road Runner Synchronizer");
                        RoadRunnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        panel3.add(RoadRunnerLabel, cc.xywh(1, 1, 5, 1));
                        panel3.add(spaceProgressBar, cc.xywh(1, 3, 5, 1, CellConstraints.FILL, CellConstraints.DEFAULT));
                        panel3.add(wikiObjectProgressBar, cc.xywh(1, 7, 3, 1, CellConstraints.FILL, CellConstraints.DEFAULT));

                        //======== panel4 ========
                        {
                            panel4.setBackground(Color.white);
                            panel4.setLayout(new FlowLayout());

                            //---- label1 ----
                            label1.setText("Sychronizing Data:");
                            panel4.add(label1);

                            //---- wikiObjectTextField ----
                            wikiObjectTextField.setPreferredSize(new Dimension(165, 20));
                            wikiObjectTextField.setText("Sample Text for a Space");
                            wikiObjectTextField.setOpaque(false);
                            wikiObjectTextField.setDragEnabled(false);
                            wikiObjectTextField.setEditable(false);
                            panel4.add(wikiObjectTextField);
                        }
                        panel3.add(panel4, cc.xywh(1, 9, 5, 1));

                        //======== panel5 ========
                        {
                            panel5.setBackground(Color.white);
                            panel5.setPreferredSize(new Dimension(100, 300));
                            panel5.setLayout(new BorderLayout());

                            //======== scrollPane1 ========
                            {
                                scrollPane1.setBackground(new Color(204, 255, 204));
                                scrollPane1.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), ""));

                                //---- syncLogArea ----
                                syncLogArea.setText(" ");
                                syncLogArea.setRows(100);
                                syncLogArea.setPreferredSize(new Dimension(300, 1400));
                                syncLogArea.setOpaque(false);
                                syncLogArea.setWrapStyleWord(true);
                                scrollPane1.setViewportView(syncLogArea);
                            }
                            panel5.add(scrollPane1, BorderLayout.CENTER);
                        }
                        panel3.add(panel5, cc.xywh(1, 11, 5, 1));

                        //======== panel6 ========
                        {
                            panel6.setBackground(Color.white);
                            panel6.setLayout(new FlowLayout());

                            //---- label2 ----
                            label2.setText("Synchronizing Space:");
                            panel6.add(label2);

                            //---- spaceNameTextField ----
                            spaceNameTextField.setPreferredSize(new Dimension(165, 20));
                            spaceNameTextField.setText("Space Name");
                            spaceNameTextField.setOpaque(false);
                            spaceNameTextField.setEditable(false);
                            panel6.add(spaceNameTextField);
                        }
                        panel3.add(panel6, cc.xywh(1, 5, 5, 1));

                        //======== panel7 ========
                        {
                            panel7.setBackground(Color.white);
                            panel7.setLayout(new FlowLayout());

                            //---- allRadioButton ----
                            allRadioButton.setText("All");
                            allRadioButton.setSelected(true);
                            panel7.add(allRadioButton);

                            //---- conflictsRadioButton ----
                            conflictsRadioButton.setText("Conflicts");
                            panel7.add(conflictsRadioButton);

                            //---- totalsRadioButton ----
                            totalsRadioButton.setText("Totals");
                            panel7.add(totalsRadioButton);
                        }
                        panel3.add(panel7, cc.xywh(1, 13, 5, 1));
                    }
                    panel9.add(panel3, new GridConstraints(0, 0, 1, 1,
                        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                        null, null, null));
                }
                contentPane.add(panel9, new GridConstraints(0, 0, 1, 1,
                    GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
                    null, null, null));
            }
            panel8.add(contentPane, BorderLayout.CENTER);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel8;
    private JPanel contentPane;
    private JPanel panel9;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel RoadRunnerLabel;
    private JProgressBar spaceProgressBar;
    private JProgressBar wikiObjectProgressBar;
    private JTextField wikiObjectTextField;
    private JTextArea syncLogArea;
    private JTextField spaceNameTextField;
    private JRadioButton allRadioButton;
    private JRadioButton conflictsRadioButton;
    private JRadioButton totalsRadioButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}

