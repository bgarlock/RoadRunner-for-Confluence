package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 * DefaultTreeModelDemoPanel This class is intended to demonstrate the right and
 * wrong ways to manipulate a DefaultTreeModel.
 *
 * @author Collin Fagan
 */
public class TreeWithRightClick extends JPanel {

    private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Root");

    private DefaultMutableTreeNode child1Node = new DefaultMutableTreeNode(
            "Child 1");

    private DefaultMutableTreeNode child2Node = new DefaultMutableTreeNode(
            "Child 2");

    private DefaultMutableTreeNode child3Node = new DefaultMutableTreeNode(
            "Child 3");

    private JTree tree = new JTree(rootNode);

    private JPopupMenu menu = new JPopupMenu("Operations");

    /**
     *
     */
    public TreeWithRightClick() {
        setLayout(new BorderLayout());
        add(new JScrollPane(tree));
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.addMouseListener(treeMenuClicked);

        rootNode.add(child1Node);
        rootNode.add(child2Node);
        rootNode.add(child3Node);

        JMenuItem removeNode = new JMenuItem(
                "Remove from node - [Will NOT update correctly]");
        removeNode.setToolTipText("Will NOT update correctly");
        removeNode.addActionListener(removeNodeFromNode);

        JMenuItem removeModel = new JMenuItem("Remove from model");
        removeModel.setToolTipText("Will update correctly");
        removeModel.addActionListener(removeNodeFromModel);

        JMenuItem addNumbers = new JMenuItem("Add 50 children");
        addNumbers.setToolTipText("Example of a bulk update");
        addNumbers.addActionListener(addEvenOddNumbers);

        JMenuItem longTextNoUpdate = new JMenuItem(
                "Set long text - without update - [Will NOT update correctly]");
        longTextNoUpdate.setToolTipText("Will NOT update correctly");
        longTextNoUpdate.addActionListener(modifyUserObjectWithoutUpdate);

        JMenuItem longTextWithManUpdate = new JMenuItem(
                "Set long text - with manual update");
        longTextWithManUpdate.setToolTipText("Will update correctly");
        longTextWithManUpdate.addActionListener(modifyUserObjectWithManualUpdate);

        JMenuItem longTextWithAutoUpdate = new JMenuItem(
                "Set long text - with automatic update");
        longTextWithAutoUpdate.setToolTipText("Will update correctly");
        longTextWithAutoUpdate.addActionListener(modifyUserObjectWithAutomaticUpdate);

        menu.add(removeNode);
        menu.add(removeModel);
        menu.addSeparator();
        menu.add(addNumbers);
        menu.addSeparator();
        menu.add(longTextNoUpdate);
        menu.add(longTextWithManUpdate);
        menu.add(longTextWithAutoUpdate);
    }

    /**
     * MouseAdapter to popup the menu the tree menu
     */
    private MouseAdapter treeMenuClicked = new MouseAdapter() {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                tree.setSelectionPath(tree.getPathForRow(row));
                menu.show(tree, e.getX(), e.getY());
            }
        }
    };

    /**
     * Example of the WRONG way to remove a single node from a tree. This will
     * not update the tree correctly.
     */
    private ActionListener removeNodeFromNode = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
                        .getLastPathComponent();

                node.removeFromParent();
            }
        }
    };

    /**
     * Example of the CORRECT way to remove a single node from a tree.
     */
    private ActionListener removeNodeFromModel = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
                        .getLastPathComponent();
                DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
                model.removeNodeFromParent(node);
            }
        }
    };

    /**
     * Example of adding children in bulk, then updating the tree.
     */
    private ActionListener addEvenOddNumbers = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
                        .getLastPathComponent();
                DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());

                for (int i = 0; i < 50; i++) {
                    node.add(new DefaultMutableTreeNode(i));
                }
                // The above changes may not seem to take effect until
                // nodeStructureChanged is called
                model.nodeStructureChanged(node);
            }
        }
    };

    /**
     * Example of the setting the user object and NOT updating the model. This
     * will NOT update the tree correctly.
     */
    private ActionListener modifyUserObjectWithoutUpdate = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
                        .getLastPathComponent();

                node.setUserObject("THIS IS A VERY LOOOOOOOOOOOOOOOOOOOOONG STRING");
            }
        }
    };

    /**
     * Example of setting the user object and updating the model. This WILL
     * update the tree correctly.
     */
    private ActionListener modifyUserObjectWithManualUpdate = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection
                        .getLastPathComponent();

                node.setUserObject("THIS IS A VERY LOOOOOOOOOOOOOOOOOOOOONG STRING");

                DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
                model.nodeChanged(node);
            }
        }
    };

    /**
     * Example of setting the user object and updating the model. This WILL
     * update the tree correctly.
     */
    private ActionListener modifyUserObjectWithAutomaticUpdate = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                DefaultTreeModel model = ((DefaultTreeModel) tree.getModel());
                model.valueForPathChanged(currentSelection, "THIS IS A VERY LOOOOOOOOOOOOOOOOOOOOONG STRING");
            }
        }
    };

    /**
     * Main program entry point, starts the demo.
     *
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JFrame demoFrame = new JFrame("DefaultTreeModel Demo");
                demoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                demoFrame.setContentPane(new TreeWithRightClick());
                demoFrame.setSize(300, 600);
                // centers the frame on the screen
                demoFrame.setLocationRelativeTo(null);

                demoFrame.setVisible(true);
            }
        });
    }
}
