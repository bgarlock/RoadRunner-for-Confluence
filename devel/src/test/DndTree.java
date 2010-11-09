package test;

/**
 * Created by IntelliJ IDEA.
 * User: brendan
 * Date: Dec 30, 2008
 * Time: 3:38:06 PM
 * To change this template use File | Settings | File Templates.
 */
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;

public class DndTree {
  public static void main(String args[]) {
    Runnable runner = new Runnable() {
      public void run() {

/////////////////////////////////////////
/////////////////////////////////////////
////   THIS EXAMPLE ONLY COMPILES AND WORKS WITH 1.6          
/////////////////////////////////////////
/////////////////////////////////////////


//        JFrame f = new JFrame("D-n-D JTree");
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel top = new JPanel(new BorderLayout());
//        JLabel dragLabel = new JLabel("Drag me:");
//        JTextField text = new JTextField();
//        text.setDragEnabled(true);
//        top.add(dragLabel, BorderLayout.WEST);
//        top.add(text, BorderLayout.CENTER);
//        f.add(top, BorderLayout.NORTH);
//
//        final JTree tree = new JTree();
//        final DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
//        tree.setTransferHandler(new TransferHandler() {
//          public boolean canImport(TransferHandler.TransferSupport support) {
//            if (!support.isDataFlavorSupported(DataFlavor.stringFlavor) ||
//                !support.isDrop()) {
//              return false;
//            }
//
//            JTree.DropLocation dropLocation =
//              (JTree.DropLocation)support.getDropLocation();
//
//            return dropLocation.getPath() != null;
//          }
//
//          public boolean importData(TransferHandler.TransferSupport support) {
//            if (!canImport(support)) {
//              return false;
//            }
//
//            JTree.DropLocation dropLocation =
//              (JTree.DropLocation)support.getDropLocation();
//
//            TreePath path = dropLocation.getPath();
//
//            Transferable transferable = support.getTransferable();
//
//            String transferData;
//            try {
//              transferData = (String)transferable.getTransferData(
//                DataFlavor.stringFlavor);
//            } catch (IOException e) {
//              return false;
//            } catch (UnsupportedFlavorException e) {
//              return false;
//            }
//
//            int childIndex = dropLocation.getChildIndex();
//            if (childIndex == -1) {
//              childIndex = model.getChildCount(path.getLastPathComponent());
//            }
//
//            DefaultMutableTreeNode newNode =
//              new DefaultMutableTreeNode(transferData);
//            DefaultMutableTreeNode parentNode =
//              (DefaultMutableTreeNode)path.getLastPathComponent();
//            model.insertNodeInto(newNode, parentNode, childIndex);
//
//            TreePath newPath = path.pathByAddingChild(newNode);
//            tree.makeVisible(newPath);
//            tree.scrollRectToVisible(tree.getPathBounds(newPath));
//
//            return true;
//          }
//        });
//
//        JScrollPane pane = new JScrollPane(tree);
//        f.add(pane, BorderLayout.CENTER);
//
//        JPanel bottom = new JPanel();
//        JLabel comboLabel = new JLabel("DropMode");
//        String options[] = {"USE_SELECTION",
//                "ON", "INSERT", "ON_OR_INSERT"
//        };
//        final DropMode mode[] = {DropMode.USE_SELECTION,
//                DropMode.ON, DropMode.INSERT, DropMode.ON_OR_INSERT};
//        final JComboBox combo = new JComboBox(options);
//        combo.addActionListener(new ActionListener() {
//          public void actionPerformed(ActionEvent e) {
//            int selectedIndex = combo.getSelectedIndex();
//            tree.setDropMode(mode[selectedIndex]);
//          }
//        });
//        bottom.add(comboLabel);
//        bottom.add(combo);
//        f.add(bottom, BorderLayout.SOUTH);
//        f.setSize(300, 400);
//        f.setVisible(true);
      }
    };
    EventQueue.invokeLater(runner);
  }
}
