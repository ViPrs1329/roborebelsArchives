package org.stlpriory.robotics.scouter.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchResult;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;
import org.stlpriory.robotics.scouter.ui.table.MatchResultTable;
import org.stlpriory.robotics.scouter.ui.table.TeamResultsTable;
import org.stlpriory.robotics.scouter.ui.tree.MatchTreeModel;
import org.stlpriory.robotics.scouter.ui.tree.MatchTreeNode;
import org.stlpriory.robotics.scouter.ui.tree.TeamTreeModel;
import org.stlpriory.robotics.scouter.ui.tree.TeamTreeNode;

public class NavTreePanel extends JPanel implements TreeSelectionListener {
    private static final long serialVersionUID = 1L;
    
    public static final int NAV_HSIZE = RoboScouter.NAV_HSIZE;
    public static final int NAV_VSIZE = RoboScouter.NAV_VSIZE;
    public static final int NAV_SCROLL_PANE_HSIZE = RoboScouter.NAV_SCROLL_PANE_HSIZE;
    
    private JTabbedPane navPanel;
    private JScrollPane matchesScrollPane;
    private JScrollPane teamsScrollPane;
    private JTree matchTree;
    private JTree teamTree;
    
    private IDataStore store;
    private RoboScouter mainGui;
    
    public NavTreePanel(final IDataStore theBackingStore, final RoboScouter theMainGui) {
        setBorder(BorderFactory.createTitledBorder("Navigation"));
        setLayout(new FlowLayout());
        setPreferredSize(new Dimension(NAV_HSIZE, NAV_VSIZE));

        this.store = theBackingStore;
        this.mainGui = theMainGui;
        
        this.navPanel = new JTabbedPane();
        
        this.matchesScrollPane = new JScrollPane();
        this.teamsScrollPane = new JScrollPane();
        
        this.matchTree = new JTree(new MatchTreeModel(this.store));
        this.teamTree = new JTree(new TeamTreeModel(this.store));
        
        this.matchTree.addTreeSelectionListener(this);
        this.teamTree.addTreeSelectionListener(this);
        
        this.matchesScrollPane.setPreferredSize(new Dimension(200, NAV_VSIZE));
        this.matchesScrollPane.setViewportView(this.matchTree);
        
        this.teamsScrollPane.setPreferredSize(new Dimension(200, NAV_VSIZE));
        this.teamsScrollPane.setViewportView(this.teamTree);
        
        this.navPanel.addTab("Matches", this.matchesScrollPane);
        this.navPanel.addTab("Teams", this.teamsScrollPane);

        add(this.navPanel);
    }

    @Override
    public void valueChanged(final TreeSelectionEvent event) {
        // ----------------------------------------------------------------------------------
        // Selection made in the Teams navigation tree...
        // ----------------------------------------------------------------------------------
        if (event.getSource() == this.teamTree) {
            TreePath newLeadSelectionPath = event.getNewLeadSelectionPath();
            if (newLeadSelectionPath == null) return;
            
            if (newLeadSelectionPath.getLastPathComponent() instanceof TeamTreeNode) {
                TeamTreeNode node = (TeamTreeNode) newLeadSelectionPath.getLastPathComponent();
                
                TeamInfo team = this.store.fetchTeam(node.getTeamNumber());
                List<Category> categories = this.store.fetchAllScoringCategories();
                List<TeamResult> results = this.store.fetchTeamResults(team);
                
                if (! results.isEmpty()) {
                    JTable table = new TeamResultsTable(team,results,categories,true);
                    this.mainGui.getDetailsPanel().setContentView(table);
                    if (team != null) {
                        this.mainGui.getDetailsPanel().setContentLabel("Team "+node.getTeamNumber()+" ("+team.getTeamName()+") results");
                    } else {
                        this.mainGui.getDetailsPanel().setContentLabel("Team "+node.getTeamNumber()+" results");
                    }
                    
                } else {
                    JOptionPane.showMessageDialog(this.mainGui, "There are no match results to show");
                }
                
            }
            
        } else if (event.getSource() == this.matchTree) { 
            TreePath newLeadSelectionPath = event.getNewLeadSelectionPath();
            if (newLeadSelectionPath == null) return;
            
            if (newLeadSelectionPath.getLastPathComponent() instanceof MatchTreeNode) {
                MatchTreeNode node = (MatchTreeNode) newLeadSelectionPath.getLastPathComponent();
                
                MatchResult result = this.store.fetchMatchResult(node.getMatchNumber());
                List<Category> categories = this.store.fetchAllScoringCategories();
                
                //MatchResultRater table = new MatchResultRater(result,categories);
                JTable table = new MatchResultTable(result,categories,false);
                this.mainGui.getDetailsPanel().setContentView(table);
                this.mainGui.getDetailsPanel().setContentLabel("Match "+node.getMatchNumber()+" results");
            }
        }
        
    }
    
    public void refresh() {
        this.matchTree.setModel(new MatchTreeModel(this.store));
        this.teamTree.setModel(new TeamTreeModel(this.store));
    }

}
