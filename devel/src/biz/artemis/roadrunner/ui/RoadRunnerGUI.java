package biz.artemis.roadrunner.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import java.util.List;
import java.io.IOException;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.border.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;

import com.jgoodies.forms.layout.*;
import biz.artemis.roadrunner.model3.*;
import biz.artemis.roadrunner.engine.ConfluenceSynchronizer;
import biz.artemis.confluence.xmlrpcwrapper.*;
import biz.artemis.util.Util;
import org.apache.xmlrpc.XmlRpcException;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel;
/*
 * Created by JFormDesigner on Tue Nov 11 17:47:48 PST 2008
 *
 * Right now this class contains all the of the GUI screens and action events generated with the JFormDesigner tool
 */


/**
 * @author Brendan Patterson
 */
public class RoadRunnerGUI {
    private RoadRunnerGUIHelper guiHelper = new RoadRunnerGUIHelper();
    private CheckboxTree checkboxTree = new CheckboxTree();

    public RoadRunnerGUI() {
        initComponents();
        initComponents2();
    }


    /**
     * this listens for mouse clicks an launches the popup window
     * on the pageCopy tree
     */
    private MouseAdapter copyPageTreeMenuClickedAdapter = new MouseAdapter() {

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int row = checkboxTree.getClosestRowForLocation(e.getX(), e.getY());
                checkboxTree.setSelectionPath(checkboxTree.getPathForRow(row));
                menu.show(checkboxTree, e.getX(), e.getY());
            }
        }
    };
    JPopupMenu menu = new JPopupMenu("Operations");


    /**
     * do anything else needed to initialize the components, but
     * this method contains no generated code. initComponents is generated
     */
    private void initComponents2() {

        refreshServerList();

        getMainFrame().addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(1);
            }
        });

        serverTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (serverTable.getSelectionModel().getValueIsAdjusting() == true) {
                    serverTable.getSelectionModel().setValueIsAdjusting(false);
                    return;
                }
                UserSettings userSettings = UserSettings.getInstance();
                TreeMap<String, ConfluenceServer> serversMap = userSettings.getConfluenceServersAliasMap();
                DefaultTableModel dataModel = (DefaultTableModel) serverTable.getModel();
                int selectedRow = serverTable.getSelectedRow();
                if (selectedRow < 0) return;
                String selectedAlias = (String) dataModel.getValueAt(selectedRow, 0);
                ConfluenceServer confServer = serversMap.get(selectedAlias);
                serverAliasField.setText(confServer.getServerAlias());
                urlField.setText(confServer.getUrl());
                loginField.setText(confServer.getLogin());
                passwordField.setText(confServer.getPassword());

            }
        });
        refreshSelectedSyncListFromModel();
        setInitialCheckboxTree();
        scrollPane8.setViewportView(checkboxTree);

        /////////////////////////////////
        // add right click pop up menus to the page copy tree
        JMenuItem expandAll = new JMenuItem("Expand all");
        expandAll.setToolTipText("Expands all children");
        JMenuItem selectAllChildren = new JMenuItem("Select All Children");
        selectAllChildren.setToolTipText("check all children nodes");
        JMenuItem unselectChildren = new JMenuItem("Unselect children");
        unselectChildren.setToolTipText("uncheck all child nodes");
        JMenuItem exportHTML = new JMenuItem("Export HTML (of checked nodes)");
        unselectChildren.setToolTipText("Exports HTML for checked nodes");
        exportHTML.addActionListener(exportHTMLActionListener);
        expandAll.addActionListener(expandAllChildrenNodesActionListener);
        selectAllChildren.addActionListener(selectAllChildrenNodesActionListener);
        unselectChildren.addActionListener(unselectAllChildrenNodesActionListener);
        menu.add(expandAll);
        menu.add(selectAllChildren);
        menu.add(unselectChildren);
        menu.addSeparator();
        menu.add(exportHTML);
        checkboxTree.addMouseListener(copyPageTreeMenuClickedAdapter);

        // end - add right click pop up
        /////////////////////////


//        checkboxTree.setTransferHandler(new TransferHandler() {
//            public boolean canImport(TransferHandler.TransferSupport support) {
//                if (!support.isDataFlavorSupported(DataFlavor.stringFlavor) ||
//                        !support.isDrop()) {
//                    return false;
//                }
//
//                JTree.DropLocation dropLocation =
//                        (JTree.DropLocation) support.getDropLocation();
//
//                return dropLocation.getPath() != null;
//            }
//
//            public boolean importData(TransferHandler.TransferSupport support) {
//                if (!canImport(support)) {
//                    return false;
//                }
//
//                JTree.DropLocation dropLocation =
//                        (JTree.DropLocation) support.getDropLocation();
//
//                TreePath path = dropLocation.getPath();
//
//                Transferable transferable = support.getTransferable();
//
//                String transferData;
//                try {
//                    transferData = (String) transferable.getTransferData(
//                            DataFlavor.stringFlavor);
//                } catch (IOException e) {
//                    return false;
//                } catch (UnsupportedFlavorException e) {
//                    return false;
//                }
//
//                int childIndex = dropLocation.getChildIndex();
//                DefaultTreeModel model = (DefaultTreeModel) checkboxTree.getModel();
//                if (childIndex == -1) {
//                    childIndex = model.getChildCount(path.getLastPathComponent());
//                }
//
//                DefaultMutableTreeNode newNode =
//                        new DefaultMutableTreeNode(transferData);
//                DefaultMutableTreeNode parentNode =
//                        (DefaultMutableTreeNode) path.getLastPathComponent();
//                model.insertNodeInto(newNode, parentNode, childIndex);
//
//                TreePath newPath = path.pathByAddingChild(newNode);
//                pageCopyJTree.makeVisible(newPath);
//                pageCopyJTree.scrollRectToVisible(pageCopyJTree.getPathBounds(newPath));
//
//                return true;
//            }
//        });

    }

    /**
     * the default checkbox tree has some dummy data we want to remove and replace
     */
    private void setInitialCheckboxTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("(Click the Refresh Tree button to get started)");
        DefaultTreeModel model = (DefaultTreeModel) checkboxTree.getModel();
        model.setRoot(root);
    }

    public static void main(String[] args) {
        RoadRunnerGUI gui = new RoadRunnerGUI();
    }



    /**
     * Expands all the child nodes in the tree from a right click
     */
    private ActionListener selectAllChildrenNodesActionListener = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = checkboxTree.getSelectionPath();
            if (currentSelection != null) {
                DefaultTreeModel model = ((DefaultTreeModel) checkboxTree.getModel());
                final ConfluenceServer confServer = getGUIHelper().getCurrentlySelectedConfluenceServer(pageCopyLocalServerComboBox);
                getGUIHelper().jtreeCheckAll(checkboxTree, currentSelection, true);
            }
        }
    };

    /**
     * Expands all the child nodes in the tree from a right click
     */
    private ActionListener unselectAllChildrenNodesActionListener = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = checkboxTree.getSelectionPath();
            if (currentSelection != null) {
                DefaultTreeModel model = ((DefaultTreeModel) checkboxTree.getModel());
                final ConfluenceServer confServer = getGUIHelper().getCurrentlySelectedConfluenceServer(pageCopyLocalServerComboBox);
                getGUIHelper().jtreeCheckAll(checkboxTree, currentSelection, false);
            }
        }
    };

    /**
     * Expands all the child nodes in the tree from a right click
     */
    private ActionListener expandAllChildrenNodesActionListener = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = checkboxTree.getSelectionPath();
            if (currentSelection != null) {
                DefaultTreeModel model = ((DefaultTreeModel) checkboxTree.getModel());
                final ConfluenceServer confServer = getGUIHelper().getCurrentlySelectedConfluenceServer(pageCopyLocalServerComboBox);
                getGUIHelper().jtreeExpandAll(checkboxTree, currentSelection, true);
            }
        }
    };

    /**
     * Example of setting the user object and updating the model. This WILL
     * update the tree correctly.
     */
    private ActionListener exportHTMLActionListener = new ActionListener() {

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            TreePath currentSelection = checkboxTree.getSelectionPath();
            if (currentSelection != null) {
                DefaultTreeModel model = ((DefaultTreeModel) checkboxTree.getModel());
//                model.valueForPathChanged(currentSelection, "THIS IS A VERY LOOOOOOOOOOOOOOOOOOOOONG STRING");
                final ConfluenceServer confServer = getGUIHelper().getCurrentlySelectedConfluenceServer(pageCopyLocalServerComboBox);
                getGUIHelper().exportHTML(checkboxTree, currentSelection, confServer);

            }
        }
    };


    private void removeServerButtonActionPerformed(ActionEvent e) {
        String emptyStr = "";
        serverAliasField.setText(emptyStr);
        urlField.setText(emptyStr);
        loginField.setText(emptyStr);
        passwordField.setText(emptyStr);

        serverTable.getSelectionModel().setValueIsAdjusting(true);
        UserSettings userSettings = UserSettings.getInstance();
//        TreeMap<String, ConfluenceServer> serversMap = userSettings.getConfluenceServersAliasMap();
        DefaultTableModel dataModel = (DefaultTableModel) serverTable.getModel();
        int selectedRow = serverTable.getSelectedRow();
        String selectedAlias = (String) dataModel.getValueAt(selectedRow, 0);
        userSettings.removeSeverByAlias(selectedAlias);
        refreshServerList();
        serverTable.getSelectionModel().clearSelection();
        userSettings.save();
    }


    /**
     * this convenience method just makes sure we don't get a null result
     *
     * @return
     */
    public String getFieldText(JTextField field) {
        String fieldText = field.getText();
        if (fieldText == null) fieldText = "";
        return fieldText.trim();

    }

    /**
     * user clicked on button to add or update server info
     *
     * @param e
     */
    private void addServerButtonActionPerformed(ActionEvent e) {
        UserSettings userSettings = UserSettings.getInstance();
        ConfluenceServer confServer = userSettings.getConfluenceServer(serverAliasField);
        if (confServer == null) {
            confServer = new ConfluenceServer();
        }
        confServer.setServerAlias(getFieldText(serverAliasField));
        confServer.setUrl(getFieldText(urlField));
        confServer.setLogin(getFieldText(loginField));
        confServer.setPassword(getFieldText(passwordField));

        userSettings.updateServersMap(confServer);
        refreshServerList();
        userSettings.save();

    }

    public JFrame getMainFrame() {
        return MainFrame;
    }

    /**
     * updates the servers list when user adds or updates a server
     * <p/>
     * there are several components in the UI that contain a server list and this currently
     * refreshes all of them
     */
    private void refreshServerList() {
        DefaultTableModel dataModel = (DefaultTableModel) serverTable.getModel();
        Map<String, ConfluenceServer> serversMap = UserSettings.getInstance().getConfluenceServersAliasMap();
        Iterator<String> it = serversMap.keySet().iterator();
        dataModel.setRowCount(serversMap.size());
        localServerComboBox.removeAllItems();
        pageCopyLocalServerComboBox.removeAllItems();
        pageCopyRemoteServerComboBox.removeAllItems();
        int i = 0;

        Vector serverListVector = new Vector(serversMap.size());
        while (it.hasNext()) {
            String alias = it.next();
            ConfluenceServer server = serversMap.get(alias);
            dataModel.setValueAt(alias, i, 0);
            dataModel.setValueAt(server.getUrl(), i, 1);
            serverListVector.add(alias);
            localServerComboBox.addItem(alias);
            pageCopyLocalServerComboBox.addItem(alias);
            pageCopyRemoteServerComboBox.addItem(alias);
            i++;
        }

        remoteServerList.setListData(serverListVector);

        serverTable.repaint();
        serverTable.validate();
        //To change body of created methods use File | Settings | File Templates.
    }


    /**
     * this probably isn't needed
     *
     * @param e
     */
    private void serverTablePropertyChange(PropertyChangeEvent e) {
        // TODO add your code here
    }


    RoadRunnerGUIHelper getGUIHelper() {
        return guiHelper;
    }

    /**
     * user clicks on 'refresh' to get spaces from the remote server
     *
     * @param e
     */
    private void spaceRefreshButtonActionPerformed(ActionEvent e) {
        // get the selected server
        ConfluenceServer confluenceServer = getGUIHelper().getCurrentlySelectedConfluenceServer(remoteServerList);

        // retrieve the relevant spaces
        // @todo - can replace this code
        RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
        List spaceSummaries = null;
        try {
            spaceSummaries = rwb.getSpacesSummaryList(confluenceServer.getConfluenceServerSettingsForXmlRpc());
        } catch (XmlRpcException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // get Vector of String from List of SpaceSummaries
        Vector spaceNames = new Vector(spaceSummaries.size());

        for (int i = 0; i < spaceSummaries.size(); i++) {
            SpaceSummaryForXmlRpc spaceSummaryForXmlRpc = (SpaceSummaryForXmlRpc) spaceSummaries.get(i);
            String spaceName = spaceSummaryForXmlRpc.getSpaceName();
            String spaceKey = spaceSummaryForXmlRpc.getSpaceKey();
            if (spaceName.contains("::")) spaceName.replace("::", ";;");
            spaceNames.add(spaceName + " :: " + spaceKey);
        }

        // fill in the combo box with spaces
        spacesToSyncList.setListData(spaceNames);

        // fill in the model for the ConfluenceServer
        confluenceServer.setAllKnownSpaces(spaceSummaries);

        // persist
        UserSettings.getInstance().save();
    }

    /**
     * persist the selections to the model and update the
     * selectedSyncsList UI widget
     *
     * @param e
     */
    private void saveSyncButtonActionPerformed(ActionEvent e) {
        // get the selected ConfluenceServer
        ConfluenceServer confluenceServer = getGUIHelper().getCurrentlySelectedConfluenceServer(remoteServerList);
        // get the selected spaces' keys
        Object selectedValues[] = spacesToSyncList.getSelectedValues();
        if (selectedValues == null || selectedValues.length == 0) return;
        List spacesToSync = new ArrayList(selectedValues.length);
        for (int i = 0; i < selectedValues.length; i++) {
            String selectedValue = (String) selectedValues[i];
            SpaceSyncDefinition syncDef = new SpaceSyncDefinition();
            syncDef.setSourceServer(confluenceServer);
            syncDef.setSpaceNameAndKey(selectedValue);
            syncDef.setTargetServer(getGUIHelper().getCurrentlySelectedConfluenceServer(localServerComboBox));
            spacesToSync.add(syncDef);
        }


        // add those keys to the UserSettings - Confluence Servers in the model
        confluenceServer.setSpacesToSync(spacesToSync);

        // created the selected Sync list Strings from the model and update the seletedSyncsList UI widge
        refreshSelectedSyncListFromModel();


        // persist the model
        UserSettings.getInstance().save();


    }

    /**
     * refreshes the sync list
     */
    private void refreshSelectedSyncListFromModel() {
        Vector selectedSyncListCache = new Vector();
        Collection c = UserSettings.getInstance().getConfluenceServersAliasMap().values();
        for (Iterator iterator = c.iterator(); iterator.hasNext();) {
            ConfluenceServer server = (ConfluenceServer) iterator.next();
            List<SpaceSyncDefinition> spacesToSyncForServer = server.getSpacesToSync();
            for (int i = 0; i < spacesToSyncForServer.size(); i++) {
                SpaceSyncDefinition spaceSyncDef = spacesToSyncForServer.get(i);
                selectedSyncListCache.add(spaceSyncDef);
            }
        }
        selectedSyncsList.setListData(selectedSyncListCache);

    }

    /**
     * updates the space list depending on which server is highlighted
     *
     * @param e
     */
    private void remoteServerListValueChanged(ListSelectionEvent e) {
        // get ConfluenceServer from alias
        String serverAlias = (String) remoteServerList.getSelectedValue();
        ConfluenceServer server = UserSettings.getInstance().getConfluenceServersAliasMap().get(serverAlias);
        if (server == null) return;

        // populate the spaces list
        List<SpaceSummaryForXmlRpc> allSpaces = server.getAllKnownSpaces();
        if (allSpaces == null) {
            // clears gui list to no components
            spacesToSyncList.setListData(new Object[0]);
            return;
        }

        List<SpaceSyncDefinition> spacesToSync = server.getSpacesToSync();

        Vector allSpacesVector = new Vector(allSpaces.size());
        for (int i = 0; i < allSpaces.size(); i++) {
            SpaceSummaryForXmlRpc spaceSummaryForXmlRpc = allSpaces.get(i);
            allSpacesVector.add(spaceSummaryForXmlRpc.getSpaceName() + " :: " + spaceSummaryForXmlRpc.getSpaceKey());
        }
        spacesToSyncList.setListData(allSpacesVector);

        // highlight the correct spaces
        List<Integer> selectedIndices = new ArrayList(allSpaces.size());
        int intArray[] = new int[allSpaces.size()];
        for (int i = 0; i < allSpaces.size(); i++) {
            intArray[i] = -1;
        }
        int k = 0;
        for (int i = 0; i < allSpaces.size(); i++) {
            String spaceKey = (String) spacesToSyncList.getModel().getElementAt(i);
            spaceKey = spaceKey.split(":: ")[1];
            List<SpaceSyncDefinition> spaceSyncs = server.getSpacesToSync();
            for (int j = 0; j < spaceSyncs.size(); j++) {
                SpaceSyncDefinition spaceSyncDefinition = spaceSyncs.get(j);
                if (spaceKey.equals(spaceSyncDefinition.getSourceSpaceKey())) {
                    intArray[k++] = i;
                }

            }
        }
        spacesToSyncList.setSelectedIndices(intArray);

    }

    /**
     * listen for delete key pressed to remove selected syncs
     *
     * @param e
     */
    private void selectedSyncsListKeyPressed(KeyEvent e) {

        // check that the delete key is being pressed
        if (e.getKeyCode() != 8) {
            return;
        }
        // iterate through values
        Object values[] = selectedSyncsList.getSelectedValues();

        for (int i = 0; i < values.length; i++) {
            SpaceSyncDefinition value = (SpaceSyncDefinition) values[i];
            List spacesToSync = value.getSourceServer().getSpacesToSync();
            spacesToSync.remove(value);
        }
        UserSettings.getInstance().save();

        // refresh this list directly from the model
        refreshSelectedSyncListFromModel();
    }

    private void startSyncButtonActionPerformed(ActionEvent e) {
        // check license
        if (!licenseIsValid()) {
            return;
        }

        // kicking this off in it's own thread allows the GUI to continue rendering
        final biz.artemis.roadrunner.ui.SwingWorker worker = new biz.artemis.roadrunner.ui.SwingWorker() {
            public Object construct() {
                try {
                    ConfluenceSynchronizer.getInstance().synchConfluence();
                } catch (IOException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (XmlRpcException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                return null;

            }
        };
        worker.start();  //required for SwingWorker 3


    }

    /**
     * setting this to permanent true since we are now opensourcing
     * @return
     */
    private boolean licenseIsValid() {
        return true;
    }

    /**
     * this is just like a standard sync, but first clears out all
     * the persistent data and in memory data
     *
     * @param e
     */
    private void refreshSyncButtonActionPerformed(ActionEvent e) {
        ContentVersionListContainer.clearForRefresh();
        ContentIdMappingContainer.clearForRefresh();
        startSyncButtonActionPerformed(e);
    }

    /**
     * send the check marked pages to the server
     *
     * @param e
     */
    private void pageCopySendPagesButtonActionPerformed(ActionEvent e) {
//        // make sure the from and to servers are not the same
//
//        // make a list of the checked pages
//
//         TreePath checked[] = checkboxTree.getCheckingPaths();
//        for (int i = 0; i < checked.length; i++) {                          2
//            TreePath treePath = checked[i];
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
//            System.out.println("checked = "+node);
//        }
//
//        // create a ContentVersionListContainer
//
//
    }


    /**
     * refresh the JTree of pages
     *
     * @param e
     */
    private void pageCopyRefreshTreeButtonActionPerformed(ActionEvent e) {
        // get the local server info
        ConfluenceServer localServer = getGUIHelper().getCurrentlySelectedConfluenceServer(pageCopyLocalServerComboBox);


        // get the list of spaces
        RemoteWikiBroker rwb = RemoteWikiBroker.getInstance();
        List spaceSummaries = null;
        ConfluenceServerSettings localServerSettings = localServer.getConfluenceServerSettingsForXmlRpc();
        try {
            spaceSummaries = rwb.getSpacesSummaryList(localServerSettings);
        } catch (XmlRpcException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // get Vector of String from List of SpaceSummaries
//        Vector spaceNames = new Vector(spaceSummaries.size());
//        DefaultMutableTreeNode root = (DefaultMutableTreeNode) pageCopyJTree.getModel().getRoot();

        // create the tree root
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(localServer.getServerAlias());
        DefaultTreeModel model = (DefaultTreeModel) pageCopyJTree.getModel();
        model.setRoot(root);

        root.setUserObject(localServer.getServerAlias());
        Map<String, DefaultMutableTreeNode> nodeMap = new HashMap<String, DefaultMutableTreeNode>();


        for (int i = 0; i < spaceSummaries.size(); i++) {
            SpaceSummaryForXmlRpc spaceSummaryForXmlRpc = (SpaceSummaryForXmlRpc) spaceSummaries.get(i);
            DefaultMutableTreeNode spaceNode = new DefaultMutableTreeNode(spaceSummaryForXmlRpc.getSpaceName());
            root.add(spaceNode);

            try {
                Vector<PageForXmlRpc> pageSummaries = rwb.getAllServerPageSummaries(localServerSettings, spaceSummaryForXmlRpc.getSpaceKey());
                // put all of the pages into tree nodes
                for (int j = 0; j < pageSummaries.size(); j++) {
                    PageForXmlRpc page = (PageForXmlRpc) pageSummaries.elementAt(j);
                    DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode(page);
                    nodeMap.put(page.getId(), pageNode);
                }
                // create hierarchy by setting the correct parent for each node
                for (int j = 0; j < pageSummaries.size(); j++) {
                    PageForXmlRpc page = (PageForXmlRpc) pageSummaries.elementAt(j);
                    DefaultMutableTreeNode node = nodeMap.get(page.getId());
                    String parentId = page.getParentId();
                    if (Integer.parseInt(parentId) < 1) {
                        // this node has no parents so make the space node it's parent
                        spaceNode.add(node);
                        continue;
                    }
                    DefaultMutableTreeNode parent = nodeMap.get(parentId);
//                    node.setParent(parent);
                    parent.add(node);
                    System.out.println("nodeparent = " + parent + " :: node= " + node);
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (XmlRpcException e1) {
                e1.printStackTrace();
            }

            // clear the map and reuse in next iteration
            nodeMap.clear();
        }

//        this.checkboxTree = new CheckboxTree(model);
        this.checkboxTree.setModel(model);
        checkboxTree.getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.SIMPLE);

        scrollPane8.setViewportView(checkboxTree);


        pageCopyJTree.updateUI();
        // get all the pages for each space


        // build the tree simply from a map
        // clean up

    }

    /**
     * this method was for a button which is now replaced by the right click menu and removed
     * @param e
     */
    private void pageCopyClearAllButtonActionPerformed(ActionEvent e) {
        checkboxTree.clearChecking();
    }

    /**
     * this method was for a button which is now replaced by the right click menu and removed
     * @param e
     */
    private void pageCopyExpandAllActionPerformed(ActionEvent e) {
        checkboxTree.expandAll();
    }

    private void pageCopySendPagesButton2ActionPerformed(ActionEvent e) {
        pageCopySendPages(false);
    }

    private void pageCopySendPages(final boolean sendAttachments) {
        // make sure the from and to servers are not the same
        final ConfluenceServer localServer = getGUIHelper().getCurrentlySelectedConfluenceServer(pageCopyLocalServerComboBox);
        final ConfluenceServer remoteServer = getGUIHelper().getCurrentlySelectedConfluenceServer(pageCopyRemoteServerComboBox);

        // check that the selected servers are different
        if (localServer.getServerAlias().equals(remoteServer.getServerAlias())) {
            String errorMessage = "the local and remote servers must be different.";
            JOptionPane.showMessageDialog(null, errorMessage, "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // make sure both servers are reachable
        if (!Util.verifyServerConnection(localServer) || !Util.verifyServerConnection(remoteServer)) {
            return;
        }

        final TreePath checkedPaths[] = checkboxTree.getCheckingPaths();

        // kick off the copy in it's own thread
        // check license
        if (!licenseIsValid()) {
            return;
        }

        // kicking this off in it's own thread allows the GUI to continue rendering
        final biz.artemis.roadrunner.ui.SwingWorker worker = new biz.artemis.roadrunner.ui.SwingWorker() {
            public Object construct() {
                ConfluenceSynchronizer.getInstance().pageCopySync(localServer, remoteServer, checkboxTree.getModel(), checkedPaths, sendAttachments);
                return null;

            }
        };
        worker.start();  //required for SwingWorker 3
    }

    private void pageCopySendPagesAndAttachmentsButtonActionPerformed(ActionEvent e) {
        pageCopySendPages(true);
    }

    /**
     * test the selected server's connection
     *
     * @param e
     */
    private void serverTestConnectionButtonActionPerformed(ActionEvent e) {
        UserSettings userSettings = UserSettings.getInstance();
        ConfluenceServer confServer = userSettings.getConfluenceServer(serverAliasField);
        if (confServer == null) {
            confServer = new ConfluenceServer();
        }
        confServer.setServerAlias(getFieldText(serverAliasField));
        confServer.setUrl(getFieldText(urlField));
        confServer.setLogin(getFieldText(loginField));
        confServer.setPassword(getFieldText(passwordField));

        if (Util.verifyServerConnection(confServer)) {
            // connection test was successful
            String successMessage = "Success! The Confluence Server: " + confServer.getServerAlias() + "is accessible.";
            JOptionPane.showMessageDialog(null, successMessage, "alert", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        MainFrame = new JFrame();
        mainPanel = new JPanel();
        mainTabbedPane = new JTabbedPane();
        startSyncPanel = new JPanel();
        rrIconLabel = new JLabel();
        startSyncButton = new JButton();
        refreshSyncButton = new JButton();
        confluenceServersPanel = new JPanel();
        serverListPanel = new JPanel();
        scrollPane1 = new JScrollPane();
        serverTable = new JTable();
        serverInfoFormPanel = new JPanel();
        serverAliasLabel = new JLabel();
        serverAliasField = new JTextField();
        label6 = new JLabel();
        urlLabel = new JLabel();
        urlField = new JTextField();
        label2 = new JLabel();
        loginLabel = new JLabel();
        loginField = new JTextField();
        passwordLabel = new JLabel();
        passwordField = new JPasswordField();
        label9 = new JLabel();
        panel8 = new JPanel();
        removeServerButton = new JButton();
        serverTestConnectionButton = new JButton();
        addServerButton = new JButton();
        spacesPanel = new JPanel();
        label1 = new JLabel();
        remoteServerListPanel = new JLabel();
        spacesToSyncListLabel = new JLabel();
        localServerSelectLabel = new JLabel();
        scrollPane2 = new JScrollPane();
        remoteServerList = new JList();
        arrowLabel = new JLabel();
        scrollPane3 = new JScrollPane();
        spacesToSyncList = new JList();
        arrowLabel2 = new JLabel();
        localServerComboBox = new JComboBox();
        spaceRefreshButton = new JButton();
        saveSyncButton = new JButton();
        selectedSyncLabel = new JLabel();
        scrollPane4 = new JScrollPane();
        selectedSyncsList = new JList();
        panel1 = new JPanel();
        panel2 = new JPanel();
        pageCopyLabel1 = new JLabel();
        pageCopyLabel2 = new JLabel();
        pageCopyLocalServerComboBox = new JComboBox();
        pageCopyLabel3 = new JLabel();
        pageCopyRemoteServerComboBox = new JComboBox();
        panel4 = new JPanel();
        scrollPane8 = new JScrollPane();
        pageCopyJTree = new JTree();
        panel3 = new JPanel();
        pageCopyRefreshTreeButton = new JButton();
        panel5 = new JPanel();
        pageCopySendPagesButton2 = new JButton();
        pageCopySendPagesAndAttachmentsButton = new JButton();
        scrollPane9 = new JScrollPane();
        pageCopySelectedListTextArea = new JTextArea();
        historyPanel = new JPanel();
        syncHistoryLabel = new JLabel();
        scrollPane5 = new JScrollPane();
        syncHistoryList = new JList();
        syncHistoryDetailsLabel = new JLabel();
        scrollPane6 = new JScrollPane();
        textArea1 = new JTextArea();
        CellConstraints cc = new CellConstraints();

        //======== MainFrame ========
        {
            MainFrame.setTitle("RoadRunner For Confluence v0.5.0");
            MainFrame.setIconImage(new ImageIcon("/Users/brendan/Desktop/projects/roadrunner/devel/FreeMindWindowIcon.png").getImage());
            Container MainFrameContentPane = MainFrame.getContentPane();
            MainFrameContentPane.setLayout(new BorderLayout());

            //======== mainPanel ========
            {
                mainPanel.setPreferredSize(new Dimension(660, 442));
                mainPanel.setLayout(new BorderLayout());

                //======== mainTabbedPane ========
                {
                    mainTabbedPane.setBackground(new Color(238, 238, 238));

                    //======== startSyncPanel ========
                    {
                        startSyncPanel.setBackground(Color.white);
                        startSyncPanel.setLayout(new GridBagLayout());
                        ((GridBagLayout)startSyncPanel.getLayout()).columnWidths = new int[] {6, 78, 6, 0};
                        ((GridBagLayout)startSyncPanel.getLayout()).rowHeights = new int[] {0, 5, 32, 0, 0, 0};
                        ((GridBagLayout)startSyncPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
                        ((GridBagLayout)startSyncPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                        //---- rrIconLabel ----
                        rrIconLabel.setIcon(new ImageIcon(getClass().getResource("/rr-logo-v2.png")));
                        startSyncPanel.add(rrIconLabel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- startSyncButton ----
                        startSyncButton.setText("     Start Sync     ");
                        startSyncButton.setBackground(new Color(0, 204, 0));
                        startSyncButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                startSyncButtonActionPerformed(e);
                            }
                        });
                        startSyncPanel.add(startSyncButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.NONE,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- refreshSyncButton ----
                        refreshSyncButton.setText("Refresh Sync");
                        refreshSyncButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                refreshSyncButtonActionPerformed(e);
                            }
                        });
                        startSyncPanel.add(refreshSyncButton, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    mainTabbedPane.addTab("Start Sync", startSyncPanel);


                    //======== confluenceServersPanel ========
                    {
                        confluenceServersPanel.setLayout(new FormLayout(
                            "default:grow",
                            "default, $lgap, 176dlu"));

                        //======== serverListPanel ========
                        {
                            serverListPanel.setLayout(new FormLayout(
                                "default:grow",
                                "fill:80dlu:grow"));

                            //======== scrollPane1 ========
                            {

                                //---- serverTable ----
                                serverTable.setModel(new DefaultTableModel(
                                    new Object[][] {
                                        {null, null},
                                    },
                                    new String[] {
                                        "Server Alias", "Server URL"
                                    }
                                ) {
                                    Class[] columnTypes = new Class[] {
                                        String.class, String.class
                                    };
                                    boolean[] columnEditable = new boolean[] {
                                        false, false
                                    };
                                    @Override
                                    public Class<?> getColumnClass(int columnIndex) {
                                        return columnTypes[columnIndex];
                                    }
                                    @Override
                                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                                        return columnEditable[columnIndex];
                                    }
                                });
                                serverTable.setShowHorizontalLines(false);
                                serverTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                                serverTable.addPropertyChangeListener("selectedRow", new PropertyChangeListener() {
                                    public void propertyChange(PropertyChangeEvent e) {
                                        serverTablePropertyChange(e);
                                    }
                                });
                                scrollPane1.setViewportView(serverTable);
                            }
                            serverListPanel.add(scrollPane1, cc.xy(1, 1));
                        }
                        confluenceServersPanel.add(serverListPanel, cc.xy(1, 1));

                        //======== serverInfoFormPanel ========
                        {
                            serverInfoFormPanel.setBorder(new TitledBorder(null, "Confluence Server Settings", TitledBorder.LEADING, TitledBorder.TOP));
                            serverInfoFormPanel.setLayout(new FormLayout(
                                "2*(default, $lcgap), [80dlu,default]:grow, 2*($lcgap, default)",
                                "4*(default, $lgap), default"));

                            //---- serverAliasLabel ----
                            serverAliasLabel.setText("server alias");
                            serverInfoFormPanel.add(serverAliasLabel, cc.xy(3, 1));

                            //---- serverAliasField ----
                            serverAliasField.setColumns(4);
                            serverAliasField.setPreferredSize(new Dimension(83, 28));
                            serverInfoFormPanel.add(serverAliasField, cc.xywh(5, 1, 2, 1));

                            //---- label6 ----
                            label6.setText("a name that identifies the server: my server");
                            serverInfoFormPanel.add(label6, cc.xy(9, 1));

                            //---- urlLabel ----
                            urlLabel.setText("URL");
                            serverInfoFormPanel.add(urlLabel, cc.xy(3, 3));
                            serverInfoFormPanel.add(urlField, cc.xywh(5, 3, 4, 1));

                            //---- label2 ----
                            label2.setText("base url of server i.e. http://localhost:8080");
                            serverInfoFormPanel.add(label2, cc.xy(9, 3));

                            //---- loginLabel ----
                            loginLabel.setText("login");
                            serverInfoFormPanel.add(loginLabel, cc.xy(3, 5));
                            serverInfoFormPanel.add(loginField, cc.xywh(5, 5, 2, 1));

                            //---- passwordLabel ----
                            passwordLabel.setText("password");
                            serverInfoFormPanel.add(passwordLabel, cc.xy(3, 7));
                            serverInfoFormPanel.add(passwordField, cc.xy(5, 7));

                            //---- label9 ----
                            label9.setText("you can leave this blank and will be prompted");
                            serverInfoFormPanel.add(label9, cc.xy(9, 7));

                            //======== panel8 ========
                            {
                                panel8.setLayout(new FlowLayout(FlowLayout.CENTER, 7, 7));

                                //---- removeServerButton ----
                                removeServerButton.setText("Remove");
                                removeServerButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        removeServerButtonActionPerformed(e);
                                    }
                                });
                                panel8.add(removeServerButton);

                                //---- serverTestConnectionButton ----
                                serverTestConnectionButton.setText("Test Connection");
                                serverTestConnectionButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        serverTestConnectionButtonActionPerformed(e);
                                    }
                                });
                                panel8.add(serverTestConnectionButton);

                                //---- addServerButton ----
                                addServerButton.setText("Add / Update");
                                addServerButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        button3ActionPerformed(e);
                                        addServerButtonActionPerformed(e);
                                    }
                                });
                                panel8.add(addServerButton);
                            }
                            serverInfoFormPanel.add(panel8, cc.xywh(5, 9, 5, 1));
                        }
                        confluenceServersPanel.add(serverInfoFormPanel, cc.xy(1, 3));
                    }
                    mainTabbedPane.addTab("Confluence Servers", confluenceServersPanel);


                    //======== spacesPanel ========
                    {
                        spacesPanel.setBorder(new TitledBorder(null, "Synchronization Select", TitledBorder.LEADING, TitledBorder.TOP));
                        spacesPanel.setLayout(new FormLayout(
                            "default:grow, $lcgap, 20dlu, $lcgap, default:grow, $lcgap, 21dlu, $lcgap, default:grow",
                            "5*(default, $lgap), fill:113dlu:grow"));

                        //---- label1 ----
                        label1.setText("Please select the remote server and spaces to synchronize with your local server.");
                        label1.setForeground(new Color(0, 0, 153));
                        spacesPanel.add(label1, cc.xywh(1, 1, 9, 1));

                        //---- remoteServerListPanel ----
                        remoteServerListPanel.setText("remote server(s)");
                        spacesPanel.add(remoteServerListPanel, cc.xy(1, 3));

                        //---- spacesToSyncListLabel ----
                        spacesToSyncListLabel.setText("spaces to sync");
                        spacesPanel.add(spacesToSyncListLabel, cc.xy(5, 3));

                        //---- localServerSelectLabel ----
                        localServerSelectLabel.setText("local server");
                        spacesPanel.add(localServerSelectLabel, cc.xy(9, 3));

                        //======== scrollPane2 ========
                        {

                            //---- remoteServerList ----
                            remoteServerList.setModel(new AbstractListModel() {
                                String[] values = {
                                    "my local server, ",
                                    "department server",
                                    "corporate server,"
                                };
                                public int getSize() { return values.length; }
                                public Object getElementAt(int i) { return values[i]; }
                            });
                            remoteServerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                            remoteServerList.addListSelectionListener(new ListSelectionListener() {
                                public void valueChanged(ListSelectionEvent e) {
                                    remoteServerListValueChanged(e);
                                    remoteServerListValueChanged(e);
                                }
                            });
                            scrollPane2.setViewportView(remoteServerList);
                        }
                        spacesPanel.add(scrollPane2, cc.xy(1, 5));

                        //---- arrowLabel ----
                        arrowLabel.setText(">>");
                        arrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        spacesPanel.add(arrowLabel, cc.xy(3, 5));

                        //======== scrollPane3 ========
                        {

                            //---- spacesToSyncList ----
                            spacesToSyncList.setModel(new AbstractListModel() {
                                String[] values = {
                                    " "
                                };
                                public int getSize() { return values.length; }
                                public Object getElementAt(int i) { return values[i]; }
                            });
                            scrollPane3.setViewportView(spacesToSyncList);
                        }
                        spacesPanel.add(scrollPane3, cc.xy(5, 5));

                        //---- arrowLabel2 ----
                        arrowLabel2.setText(">>");
                        arrowLabel2.setHorizontalAlignment(SwingConstants.CENTER);
                        spacesPanel.add(arrowLabel2, cc.xy(7, 5));

                        //---- localServerComboBox ----
                        localServerComboBox.setModel(new DefaultComboBoxModel(new String[] {
                            "localhost"
                        }));
                        spacesPanel.add(localServerComboBox, cc.xy(9, 5));

                        //---- spaceRefreshButton ----
                        spaceRefreshButton.setText("Refresh");
                        spaceRefreshButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                spaceRefreshButtonActionPerformed(e);
                            }
                        });
                        spacesPanel.add(spaceRefreshButton, cc.xy(5, 7));

                        //---- saveSyncButton ----
                        saveSyncButton.setText("Save Sync");
                        saveSyncButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                saveSyncButtonActionPerformed(e);
                                saveSyncButtonActionPerformed(e);
                            }
                        });
                        spacesPanel.add(saveSyncButton, cc.xy(9, 7));

                        //---- selectedSyncLabel ----
                        selectedSyncLabel.setText("synchronizations selected (these are the spaces RoadRunner will synchronize)");
                        spacesPanel.add(selectedSyncLabel, cc.xywh(1, 9, 9, 1));

                        //======== scrollPane4 ========
                        {

                            //---- selectedSyncsList ----
                            selectedSyncsList.setModel(new AbstractListModel() {
                                String[] values = {
                                    "department server >> ALL Spaces >> my local server  (includes new spaces)",
                                    "corporate server >> Technology >> my local server"
                                };
                                public int getSize() { return values.length; }
                                public Object getElementAt(int i) { return values[i]; }
                            });
                            selectedSyncsList.addKeyListener(new KeyAdapter() {
                                @Override
                                public void keyPressed(KeyEvent e) {
                                    selectedSyncsListKeyPressed(e);
                                }
                            });
                            scrollPane4.setViewportView(selectedSyncsList);
                        }
                        spacesPanel.add(scrollPane4, cc.xywh(1, 11, 9, 1));
                    }
                    mainTabbedPane.addTab("Spaces", spacesPanel);


                    //======== panel1 ========
                    {
                        panel1.setBackground(new Color(238, 238, 238));
                        panel1.setLayout(new FormLayout(
                            "2*(default, $lcgap), 50dlu:grow, 2*($lcgap, default)",
                            "default, $lgap, default:grow, 2*($lgap, default), $lgap, 47dlu:grow"));

                        //======== panel2 ========
                        {
                            panel2.setLayout(new FormLayout(
                                "default:grow, $lcgap, default, $lcgap, default:grow",
                                "default, $lgap, default"));

                            //---- pageCopyLabel1 ----
                            pageCopyLabel1.setText("from server");
                            panel2.add(pageCopyLabel1, cc.xy(1, 1));

                            //---- pageCopyLabel2 ----
                            pageCopyLabel2.setText("to server");
                            panel2.add(pageCopyLabel2, cc.xy(5, 1));
                            panel2.add(pageCopyLocalServerComboBox, cc.xy(1, 3));

                            //---- pageCopyLabel3 ----
                            pageCopyLabel3.setText(">>");
                            panel2.add(pageCopyLabel3, cc.xy(3, 3));
                            panel2.add(pageCopyRemoteServerComboBox, cc.xy(5, 3));
                        }
                        panel1.add(panel2, cc.xy(5, 1));

                        //======== panel4 ========
                        {
                            panel4.setBorder(new TitledBorder(null, "local page tree", TitledBorder.LEADING, TitledBorder.TOP));
                            panel4.setLayout(new FormLayout(
                                "2*(default, $lcgap), 50dlu:grow, 2*($lcgap, default)",
                                "default:grow"));

                            //======== scrollPane8 ========
                            {
                                scrollPane8.setViewportView(pageCopyJTree);
                            }
                            panel4.add(scrollPane8, cc.xywh(1, 1, 9, 1));
                        }
                        panel1.add(panel4, cc.xywh(1, 3, 9, 1));

                        //======== panel3 ========
                        {
                            panel3.setLayout(new FormLayout(
                                "default:grow, $lcgap, default, $lcgap, default:grow",
                                "default"));

                            //---- pageCopyRefreshTreeButton ----
                            pageCopyRefreshTreeButton.setText("Refresh  Tree");
                            pageCopyRefreshTreeButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    pageCopyRefreshTreeButtonActionPerformed(e);
                                }
                            });
                            panel3.add(pageCopyRefreshTreeButton, cc.xy(3, 1));
                        }
                        panel1.add(panel3, cc.xywh(5, 5, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                        //======== panel5 ========
                        {
                            panel5.setLayout(new FormLayout(
                                "2*(default, $lcgap), default",
                                "default"));

                            //---- pageCopySendPagesButton2 ----
                            pageCopySendPagesButton2.setText("     Send Pages     ");
                            pageCopySendPagesButton2.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    pageCopySendPagesButton2ActionPerformed(e);
                                }
                            });
                            panel5.add(pageCopySendPagesButton2, cc.xywh(3, 1, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                            //---- pageCopySendPagesAndAttachmentsButton ----
                            pageCopySendPagesAndAttachmentsButton.setText("Send Pages And Attachments");
                            pageCopySendPagesAndAttachmentsButton.addActionListener(new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    pageCopySendPagesAndAttachmentsButtonActionPerformed(e);
                                }
                            });
                            panel5.add(pageCopySendPagesAndAttachmentsButton, cc.xy(5, 1));
                        }
                        panel1.add(panel5, cc.xywh(5, 7, 1, 1, CellConstraints.CENTER, CellConstraints.DEFAULT));

                        //======== scrollPane9 ========
                        {

                            //---- pageCopySelectedListTextArea ----
                            pageCopySelectedListTextArea.setPreferredSize(new Dimension(0, 48));
                            scrollPane9.setViewportView(pageCopySelectedListTextArea);
                        }
                        panel1.add(scrollPane9, cc.xywh(1, 9, 9, 1));
                    }
                    mainTabbedPane.addTab("Pages", panel1);


                    //======== historyPanel ========
                    {
                        historyPanel.setLayout(new FormLayout(
                            "default:grow",
                            "4*(default, $lgap), fill:40dlu:grow"));

                        //---- syncHistoryLabel ----
                        syncHistoryLabel.setText("Synchronization history");
                        historyPanel.add(syncHistoryLabel, cc.xy(1, 1));

                        //======== scrollPane5 ========
                        {

                            //---- syncHistoryList ----
                            syncHistoryList.setModel(new AbstractListModel() {
                                String[] values = {
                                    "Jan 5, 2008 - 4:53 p.m.,",
                                    "Jan 15, 2008 - 4:53 p.m.,",
                                    "Jan 17, 2008 - 4:53 p.m.,",
                                    "Jan 25, 2008 - 4:53 p.m.,",
                                    "Feb 4, 2008 - 4:53 p.m."
                                };
                                public int getSize() { return values.length; }
                                public Object getElementAt(int i) { return values[i]; }
                            });
                            scrollPane5.setViewportView(syncHistoryList);
                        }
                        historyPanel.add(scrollPane5, cc.xy(1, 3));

                        //---- syncHistoryDetailsLabel ----
                        syncHistoryDetailsLabel.setText("Sync Details");
                        historyPanel.add(syncHistoryDetailsLabel, cc.xy(1, 7));

                        //======== scrollPane6 ========
                        {

                            //---- textArea1 ----
                            textArea1.setText("Starting Synch Jan 17th, 2008 - 4:53p.m.\nServer 'Corporate Server'\nChecking space 'Technology' for updates\nFound 6 page updates\nChecking space 'Other' for updates\n\tNo updates found.");
                            scrollPane6.setViewportView(textArea1);
                        }
                        historyPanel.add(scrollPane6, cc.xy(1, 9));
                    }
                    mainTabbedPane.addTab("History", historyPanel);

                }
                mainPanel.add(mainTabbedPane, BorderLayout.CENTER);
            }
            MainFrameContentPane.add(mainPanel, BorderLayout.CENTER);
            MainFrame.pack();
            MainFrame.setLocationRelativeTo(MainFrame.getOwner());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    private void button3ActionPerformed(ActionEvent e) {
        //To change body of created methods use File | Settings | File Templates.
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JFrame MainFrame;
    private JPanel mainPanel;
    private JTabbedPane mainTabbedPane;
    private JPanel startSyncPanel;
    private JLabel rrIconLabel;
    private JButton startSyncButton;
    private JButton refreshSyncButton;
    private JPanel confluenceServersPanel;
    private JPanel serverListPanel;
    private JScrollPane scrollPane1;
    private JTable serverTable;
    private JPanel serverInfoFormPanel;
    private JLabel serverAliasLabel;
    private JTextField serverAliasField;
    private JLabel label6;
    private JLabel urlLabel;
    private JTextField urlField;
    private JLabel label2;
    private JLabel loginLabel;
    private JTextField loginField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private JLabel label9;
    private JPanel panel8;
    private JButton removeServerButton;
    private JButton serverTestConnectionButton;
    private JButton addServerButton;
    private JPanel spacesPanel;
    private JLabel label1;
    private JLabel remoteServerListPanel;
    private JLabel spacesToSyncListLabel;
    private JLabel localServerSelectLabel;
    private JScrollPane scrollPane2;
    private JList remoteServerList;
    private JLabel arrowLabel;
    private JScrollPane scrollPane3;
    private JList spacesToSyncList;
    private JLabel arrowLabel2;
    private JComboBox localServerComboBox;
    private JButton spaceRefreshButton;
    private JButton saveSyncButton;
    private JLabel selectedSyncLabel;
    private JScrollPane scrollPane4;
    private JList selectedSyncsList;
    private JPanel panel1;
    private JPanel panel2;
    private JLabel pageCopyLabel1;
    private JLabel pageCopyLabel2;
    private JComboBox pageCopyLocalServerComboBox;
    private JLabel pageCopyLabel3;
    private JComboBox pageCopyRemoteServerComboBox;
    private JPanel panel4;
    private JScrollPane scrollPane8;
    private JTree pageCopyJTree;
    private JPanel panel3;
    private JButton pageCopyRefreshTreeButton;
    private JPanel panel5;
    private JButton pageCopySendPagesButton2;
    private JButton pageCopySendPagesAndAttachmentsButton;
    private JScrollPane scrollPane9;
    private JTextArea pageCopySelectedListTextArea;
    private JPanel historyPanel;
    private JLabel syncHistoryLabel;
    private JScrollPane scrollPane5;
    private JList syncHistoryList;
    private JLabel syncHistoryDetailsLabel;
    private JScrollPane scrollPane6;
    private JTextArea textArea1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
