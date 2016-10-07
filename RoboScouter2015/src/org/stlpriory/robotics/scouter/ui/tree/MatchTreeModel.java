package org.stlpriory.robotics.scouter.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.model.MatchInfo;

public class MatchTreeModel extends javax.swing.tree.DefaultTreeModel {

    private static final long serialVersionUID = 1L;

    protected IDataStore store;

    public MatchTreeModel(final IDataStore theBackingStore) {
        super(new DefaultMutableTreeNode(theBackingStore), true);

        this.store = theBackingStore;
        buildChildren();
    }

    protected void buildChildren() {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) getRoot();
        // Create a TreeNode for each match
        this.store.fetchAllMatches().forEach(theMatch -> rootNode.add(createTreeNode(theMatch)));
    }
    
    private MatchTreeNode createTreeNode(final MatchInfo theMatch) {
        int matchNumber = theMatch.getMatchNumber();
        return new MatchTreeNode(matchNumber,"Match "+matchNumber);
    }

}
