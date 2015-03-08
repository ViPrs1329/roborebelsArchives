/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.stlpriory.robotics.scouter.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import org.stlpriory.robotics.scouter.io.CsvImporter;
import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.io.IImporter;
import org.stlpriory.robotics.scouter.io.XmlDataStore;
import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;
import org.stlpriory.robotics.scouter.ui.table.MatchScheduleTable;
import org.stlpriory.robotics.scouter.ui.table.TeamRankingTable;
import org.stlpriory.robotics.scouter.ui.table.TeamsTable;

/**
 *
 */
public class RoboScouter extends JFrame {
    private static final long serialVersionUID = 1L;
    
    public static final int OVERALL_HSIZE = 1400;
    public static final int OVERALL_VSIZE = 800;
    
    public static final int SPLIT_PANE_HSIZE = OVERALL_HSIZE - 100;
    public static final int SPLIT_PANE_VSIZE = OVERALL_VSIZE - 100;
    
    public static final int NAV_SCROLL_PANE_HSIZE = 200;
    public static final int NAV_HSIZE = NAV_SCROLL_PANE_HSIZE + 20;
    public static final int NAV_VSIZE = RoboScouter.SPLIT_PANE_VSIZE - 100;
    
    public static final int CONTENT_HSIZE = RoboScouter.SPLIT_PANE_HSIZE - NavTreePanel.NAV_HSIZE;
    public static final int CONTENT_VSIZE = RoboScouter.SPLIT_PANE_VSIZE -100 ;
    
    private IDataStore store;
    
    private JMenuItem exitApp;
    private JMenuItem importTeamsFile;
    private JMenuItem importMatchScheduleFile;
    private JMenuItem openFile;
    private JFileChooser importFileChooser;
    private JMenu fileMenu;
    private JMenu viewMenu;
    private JMenuBar jMenuBar1;
    private JMenuItem teamRankingItem;
    private JMenuItem viewTeamsItem;
    private JMenuItem viewMatches;
    private JSplitPane jSplitPane1;
    private JFileChooser openFileChooser;
    
    private DetailsPanel detailsPanel;
    private NavTreePanel navTreePanel;
    
    // ==================================================================================
    //                        C O N S T R U C T O R S
    // ==================================================================================

    /**
     * Creates new form RoboScouter
     */
    public RoboScouter(final IDataStore theStore) {
        this.store = theStore;
        initMenus();
        initComponents();
    }
    
    private void setDataStore(final IDataStore theStore) {
        this.store = theStore;
        initMenus();
        initComponents();
    }
    
    public DetailsPanel getDetailsPanel() {
        return this.detailsPanel;
    }
    
    public NavTreePanel getNavTreePanel() {
        return this.navTreePanel;
    }

    private void initMenus() {
        this.openFileChooser = new JFileChooser();
        this.importFileChooser = new JFileChooser();

        this.jMenuBar1 = new JMenuBar();
        this.fileMenu = new JMenu();
        this.openFile = new JMenuItem();
        this.importTeamsFile = new JMenuItem();
        this.importMatchScheduleFile = new JMenuItem();
        this.exitApp = new JMenuItem();
        this.viewMenu = new JMenu();
        this.teamRankingItem = new JMenuItem();
        this.viewTeamsItem = new JMenuItem();
        this.viewMatches = new JMenuItem();

        this.openFileChooser.setDialogTitle("Open Directory");
        this.openFileChooser.setFileFilter(new OpenDirectoryFilter());
        this.openFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        this.importFileChooser.setDialogTitle("Import Match Result");
        this.importFileChooser.setFileFilter(new ImportFileFilter());

        // File menu
        this.fileMenu.setText("File");
        this.openFile.setText("Open Regional Folder");
        this.openFile.addActionListener(e -> OpenRegionalActionPerformed(e));
        this.fileMenu.add(this.openFile);

        this.importTeamsFile.setText("Import Teams");
        this.importTeamsFile.addActionListener(e -> ImportTeamsActionPerformed(e));
        this.fileMenu.add(this.importTeamsFile);

        this.importMatchScheduleFile.setText("Import Match Schedule");
        this.importMatchScheduleFile.addActionListener(e -> ImportMatchScheduleActionPerformed(e));
        this.fileMenu.add(this.importMatchScheduleFile);

        this.exitApp.setText("Exit");
        this.exitApp.addActionListener(e -> ExitActionPerformed(e));
        this.fileMenu.add(this.exitApp);

        this.jMenuBar1.add(this.fileMenu);

        // View menu
        this.viewMenu.setText("View");
        this.viewMatches.setText("Matches");
        this.viewMatches.addActionListener(e -> viewMatchesItemActionPerformed(e));
        this.viewMenu.add(this.viewMatches);

        this.viewTeamsItem.setText("Teams");
        this.viewTeamsItem.addActionListener(e -> viewTeamsItemActionPerformed(e));
        this.viewMenu.add(this.viewTeamsItem);

        this.teamRankingItem.setText("Team Rankings");
        this.teamRankingItem.addActionListener(e -> teamRankingItemActionPerformed(e));
        this.viewMenu.add(this.teamRankingItem);

        this.jMenuBar1.add(this.viewMenu);
    }

    private void initComponents() {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("RoboRebels Scouting App");
        setPreferredSize(new Dimension(OVERALL_HSIZE, OVERALL_VSIZE));

        this.detailsPanel = new DetailsPanel(this.store, this);
        this.navTreePanel = new NavTreePanel(this.store, this); 
        
        this.jSplitPane1 = new JSplitPane();
        this.jSplitPane1.setDividerLocation(NAV_HSIZE);
        this.jSplitPane1.setPreferredSize(new Dimension(SPLIT_PANE_HSIZE, SPLIT_PANE_VSIZE));
        this.jSplitPane1.setLeftComponent(this.navTreePanel);
        this.jSplitPane1.setRightComponent(this.detailsPanel);

        setJMenuBar(this.jMenuBar1);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(this.jSplitPane1, GroupLayout.PREFERRED_SIZE, SPLIT_PANE_HSIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(this.jSplitPane1, GroupLayout.PREFERRED_SIZE, SPLIT_PANE_VSIZE, GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }

    private void teamRankingItemActionPerformed(final ActionEvent evt) {
        List<TeamInfo> theTeams = this.store.fetchAllTeams();
        List<TeamResult> theResults = this.store.fetchAllTeamResults();
        List<Category> theScoringCategories = this.store.fetchAllScoringCategories();
        
        JTable theTable = new TeamRankingTable(theTeams,theResults,theScoringCategories);
        this.detailsPanel.setContentView(theTable);
        this.detailsPanel.setContentLabel("Team Rankings");
    }

    private void viewTeamsItemActionPerformed(final ActionEvent evt) {
        List<TeamInfo> theTeams = this.store.fetchAllTeams();
        
        JTable theTable = new TeamsTable(theTeams,false,false);
        this.detailsPanel.setContentView(theTable);
        this.detailsPanel.setContentLabel("Participants ("+theTeams.size()+" teams)");
    }

    private void viewMatchesItemActionPerformed(final ActionEvent evt) {
        List<MatchInfo> theMatches = this.store.fetchAllMatches();
        
        JTable theTable = new MatchScheduleTable(theMatches,false,false);
        this.detailsPanel.setContentView(theTable);
        this.detailsPanel.setContentLabel("Participating teams");
    }

    private void ImportTeamsActionPerformed(final ActionEvent evt) {
        int returnVal = this.importFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.importFileChooser.getSelectedFile();
            System.out.println("Picked file " + file.getAbsolutePath());
            
            IImporter importer = new CsvImporter(file);
            List<TeamInfo> theTeams = importer.importTeams();
            
            JTable theTable = new TeamsTable(theTeams,false,true);
            this.detailsPanel.setContentView(theTable);
            this.detailsPanel.setContentLabel("Team import results ("+theTeams.size()+" teams)");
            
        } else {
            System.out.println("File access cancelled by user.");
        }
    }

    private void ImportMatchScheduleActionPerformed(final ActionEvent evt) {
        int returnVal = this.importFileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = this.importFileChooser.getSelectedFile();
            System.out.println("Picked file " + file.getAbsolutePath());
            
            IImporter importer = new CsvImporter(file);
            List<MatchInfo> theMatches = importer.importMatchSchedule(this.store.fetchAllTeams());
            
            JTable theTable = new MatchScheduleTable(theMatches,false,true);
            this.detailsPanel.setContentView(theTable);
            this.detailsPanel.setContentLabel("Match schedule import results");
            
        } else {
            System.out.println("File access cancelled by user.");
        }
    }

    private void ExitActionPerformed(final ActionEvent evt) {
        System.exit(0);
    }

    private void OpenRegionalActionPerformed(final ActionEvent evt) {
        int returnVal = this.openFileChooser.showOpenDialog(RoboScouter.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File theRootDir = this.openFileChooser.getSelectedFile();
            IDataStore theStore = new XmlDataStore(theRootDir);
            setDataStore(theStore);
        }
    }

    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private class OpenDirectoryFilter extends FileFilter {
        @Override
        public boolean accept(final File file) {
            return file.isDirectory();
        }

        @Override
        public String getDescription() {
            // This description will be displayed in the dialog,
            return "Directories containing match results";
        }
    }

    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private class ImportFileFilter extends FileFilter {
        @Override
        public boolean accept(final File file) {
            // Allow only directories, or files with ".txt" extension
            return file.isDirectory() || file.getAbsolutePath().endsWith(".csv");
        }

        @Override
        public String getDescription() {
            // This description will be displayed in the dialog
            return "Exported match spreadsheet files (*.csv)";
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                // "Metal", "Nimbus", "Motif", "Windows", "WindowsClassic"
                if ("Metal".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RoboScouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(RoboScouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(RoboScouter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(RoboScouter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (args.length == 0) {
            throw new IllegalStateException("Need to specify the directory path to the Regional folder as an argument");
        }
        File rootDir = new File(args[0]);
        if (!rootDir.exists()) {
            throw new IllegalStateException("The specified Regional directory, "+rootDir+" does not exist");
        }
        System.out.println("Launching RoboScouter with the '"+rootDir+"' directory");

        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                File rootDir = new File(args[0]);
                IDataStore store = new XmlDataStore(rootDir);
                new RoboScouter(store).setVisible(true);
            }
        });
    }

}
