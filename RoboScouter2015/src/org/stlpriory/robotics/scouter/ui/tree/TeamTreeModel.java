package org.stlpriory.robotics.scouter.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.model.TeamInfo;

public class TeamTreeModel extends javax.swing.tree.DefaultTreeModel {
    
    private static final long serialVersionUID = 1L;
    private IDataStore store;

    public TeamTreeModel(final IDataStore theBackingStore) {
        super(new DefaultMutableTreeNode(theBackingStore), true);
        
        this.store = theBackingStore;
        buildChildren();
    }
    
    protected void buildChildren() {
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)getRoot();
        // Create a TreeNode for each team
        this.store.fetchAllTeams().forEach(theTeam -> rootNode.add(createTreeNode(theTeam)) );
    }
    
    private TeamTreeNode createTreeNode(final TeamInfo theTeam) {
        int teamNumber = theTeam.getTeamNumber();
        return new TeamTreeNode(teamNumber,"Team "+teamNumber);
    }
    
}
