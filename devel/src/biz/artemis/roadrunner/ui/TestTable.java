package biz.artemis.roadrunner.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.*;


public class TestTable extends JFrame {

    TableEvent eventHandler = new TableEvent(this);
    final JTable table;
    JPanel panel;
    DefaultTableModel dataModel;

    public TestTable() {
        super("My Table Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuItem newRow = new JMenuItem("New Row");
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(newRow);
        JMenuBar jmb = new JMenuBar();
        jmb.add(editMenu);
        setJMenuBar(jmb);
        newRow.addActionListener(eventHandler);

        panel = new JPanel(new GridLayout(1,0));

        String[] columnNames = {
            "First Name",
            "Last Name",
            "Sport",
            "# of Years",
            "Vegetarian"
        };
        Object[][] data = {
            {"Mary", "Campione",
             "Snowboarding", new Integer(5), new Boolean(false)},
            {"Alison", "Huml",
             "Rowing", new Integer(3), new Boolean(true)},
            {"Kathy", "Walrath",
             "Knitting", new Integer(2), new Boolean(false)},
            {"Sharon", "Zakhour",
             "Speed reading", new Integer(20), new Boolean(true)},
            {"Philip", "Milne",
             "Pool", new Integer(10), new Boolean(false)}
        };

        dataModel = new DefaultTableModel();
        for (int col = 0; col < columnNames.length; col++) {
            dataModel.addColumn(columnNames[col]);
        }
        for (int row = 0; row < columnNames.length; row++) {
            dataModel.addRow(data[row]);
        }

        table = new JTable(dataModel);
        table.setPreferredScrollableViewportSize(new Dimension(600, 120));
//        table.setFillsViewportHeight(true);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        panel.add(scrollPane);
        panel.setOpaque(true); //content panes must be opaque
        setContentPane(panel);

        //Display the window.
        pack();
        setVisible(true);
    } // end of SarathgopalrsTable() constructor

    public static void main(String[] args) {
        TestTable myFrame = new TestTable();
        myFrame.setVisible(true);
    } // end of main()

} // end of class

//----------------------------------------------------------------------------------------------------


class TableEvent implements ActionListener {
    TestTable gui;

    public TableEvent(TestTable in) {
        gui = in;
    } // end of TableEvent() constructor

    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.compareTo("New Row") == 0) {
            insertNewRow();
        }
    } // end of actionPerformed()

    public void insertNewRow() {
        gui.dataModel.addRow(
            new Object[] {"", "", "", new Integer(0), new Boolean(false)}
        );
        gui.dataModel.fireTableRowsInserted(
            gui.dataModel.getRowCount(),
            gui.dataModel.getRowCount()
        );
    } // end of insertNewRow()

} // end of class
