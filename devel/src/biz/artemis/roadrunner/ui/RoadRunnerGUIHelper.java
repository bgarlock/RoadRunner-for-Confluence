package biz.artemis.roadrunner.ui;

import biz.artemis.roadrunner.model3.ConfluenceServer;
import biz.artemis.roadrunner.model3.UserSettings;
import biz.artemis.confluence.xmlrpcwrapper.PageForXmlRpc;
import biz.artemis.confluence.xmlrpcwrapper.RemoteWikiBroker;

import javax.swing.*;
import javax.swing.tree.TreePath;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.File;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import org.apache.xmlrpc.XmlRpcException;

/**
 * this class is meant to break out reusable methods which
 * help the GUI gather and tally data. Especially methods that interface with the RemoteWikiBroker
 * <p/>
 * Since we're using JFormDesigner to
 * <p/>
 * Try to refactore as many reusable methods
 */
public class RoadRunnerGUIHelper {


    /**
     * retrieves a the ConfluenceServer objected mapped to the alias currently selected
     * in the GUI component
     *
     * @param serverJList
     * @return
     */
    public ConfluenceServer getCurrentlySelectedConfluenceServer(JList serverJList) {
        String selectedServerAlias = (String) serverJList.getSelectedValue();
        UserSettings userSettings = UserSettings.getInstance();
        ConfluenceServer confluenceServer = userSettings.getConfluenceServerFromAlias(selectedServerAlias);
        return confluenceServer;
    }

    public ConfluenceServer getCurrentlySelectedConfluenceServer(JComboBox serverComboBox) {
        String selectedServerAlias = (String) serverComboBox.getSelectedItem();
        UserSettings userSettings = UserSettings.getInstance();
        ConfluenceServer confluenceServer = userSettings.getConfluenceServerFromAlias(selectedServerAlias);
        return confluenceServer;
    }


    /**
     * exports
     * @param checkboxTree
     * @param currentSelection
     * @param confServer
     */
    public void exportHTML(CheckboxTree checkboxTree, TreePath currentSelection, ConfluenceServer confServer) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
        if (!(node.getUserObject() instanceof PageForXmlRpc)) {
            JOptionPane.showMessageDialog(null, "For the moment you can only export HTML by clicking on 'page' node. \n Space nodes and server nodes will be added soon. \n", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // @todo - prompt user where to save it with File Browser dialog

        // @todo - if the node is a space node or a server node then call this method recursively untill it's down to the pages


//        PageForXmlRpc page = (PageForXmlRpc) node.getUserObject();
        RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();

        // get list of all cheched child nodes as PageForXML objects
        List<PageForXmlRpc> checkedPages = getCheckedChildNodesAsPages(checkboxTree, currentSelection);
        for (int i = 0; i < checkedPages.size(); i++) {
            PageForXmlRpc page = checkedPages.get(i);
            // gather page id, space, server info
            String pageId = page.getId();
            String spaceKey = page.getSpace();
            String content = "";


            // get HTML from server
            String pageHTML = null;
            try {
                pageHTML = rwb.renderContent(confServer.getConfluenceServerSettingsForXmlRpc(), spaceKey, pageId, content);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (XmlRpcException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            // create directories
            File htmlSpaceDir = new File("html-export"+File.separator+spaceKey);
            if (!htmlSpaceDir.exists()) {
                htmlSpaceDir.mkdirs();
            }
            try {
                BufferedWriter out = new BufferedWriter(new FileWriter("html-export"+File.separator+spaceKey+File.separator+page.getTitle() + ".html"));
                out.write(pageHTML);
                out.close();
            }
            catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }

        }

    }

    /**
     * @param checkboxTree
     * @param currentSelection
     * @return PageForXml ojbects which are represented by the checked child nodes of the selected node (including the selected node)
     */
    private List<PageForXmlRpc> getCheckedChildNodesAsPages(CheckboxTree checkboxTree, TreePath currentSelection) {
        List listOfChildren = new ArrayList();
        List<PageForXmlRpc> checkedChildrenObjects = getAllTreeNodeChildrenAsList(checkboxTree, currentSelection, listOfChildren, true);
        return checkedChildrenObjects;
    }


    /**
     * hand this method a node ins a tree and it will traverse the tree and hand back all the nodes of that tree
     *
     * @param checkboxTree
     * @param currentSelection
     * @param listOfChildren
     * @return
     */
    List getAllTreeNodeChildrenAsList(CheckboxTree checkboxTree, TreePath currentSelection, List listOfChildren, boolean addOnlyIfChecked) {
        // Traverse children
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) currentSelection.getLastPathComponent();
        DefaultMutableTreeNode n = null;
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                n = (DefaultMutableTreeNode) e.nextElement();
                TreePath path = currentSelection.pathByAddingChild(n);
                getAllTreeNodeChildrenAsList(checkboxTree, path, listOfChildren, addOnlyIfChecked);
            }
        }

        // if we're only creating a list of checked nodes then evaluate whether the node is checked
        if (addOnlyIfChecked) {
            if (checkboxTree.getCheckingModel().isPathChecked(currentSelection)) {
                listOfChildren.add(node.getUserObject());
            }
        } else {

            listOfChildren.add(node.getUserObject());
        }
        return listOfChildren;

    }

    /**
     * expands all the tree nodes from the selected node on down
     *
     * @param tree
     * @param parent
     * @param expand
     */
    public void jtreeExpandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                jtreeExpandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    /**
     * checks off all the tree nodes on down
     *
     * @param tree
     * @param parent
     * @param checkTheBox
     */
    public void jtreeCheckAll(CheckboxTree tree, TreePath parent, boolean checkTheBox) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                jtreeCheckAll(tree, path, checkTheBox);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (checkTheBox) {
            tree.addCheckingPath(parent);
        } else {
            tree.removeCheckingPath(parent);
        }
    }
}
