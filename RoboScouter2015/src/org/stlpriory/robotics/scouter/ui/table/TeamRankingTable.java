package org.stlpriory.robotics.scouter.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;
import org.stlpriory.robotics.scouter.ui.TeamSummaryPanel;
import org.stlpriory.robotics.scouter.util.ModelUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class TeamRankingTable extends JTable {
    private static final long serialVersionUID = 1L;
    
    public TeamRankingTable(final List<TeamInfo> theTeams,
                            final List<TeamResult> theResults,
                            final List<Category> theScoringCategories) {
        super( new JTableModel(theTeams,theResults,theScoringCategories) );
        
        addMouseListener(new JTableButtonMouseListener(this));
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(true);
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        try {
            setAutoCreateRowSorter(true);
        } catch (Exception continuewithNoSort) {
            // do nothing
        }
        
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));    
        setRowHeight(25);
        
        JTableCellRenderer renderer = new JTableCellRenderer(theScoringCategories);
        renderer.setHorizontalAlignment( JLabel.CENTER );
        setDefaultRenderer(String.class, renderer);
        setDefaultRenderer(Integer.class, renderer);
        setDefaultRenderer(Double.class, renderer);
        setDefaultRenderer(JButton.class, renderer);
        
        TableColumnModel tcm = getColumnModel();
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            if (i == 0) {
                tcm.getColumn(i).setPreferredWidth(120);
            } else {
                tcm.getColumn(i).setPreferredWidth(80);
            }
        }
    }
    
    public boolean isCellEditable(int rowIndex, int colIndex) {
        return false;
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private final List<String> columnNames   = new ArrayList<>();
        private final List<Class<?>> columnTypes = new ArrayList<>();
        private final List<TeamInfo> teams;
        private final List<TeamResult> results;
        private final List<Category> categories;
        private final Multimap<TeamInfo, TeamResult> resultsMap;
        
        public JTableModel(final List<TeamInfo> theTeams,
                           final List<TeamResult> theResults,
                           final List<Category> theScoringCategories) {
            this.teams      = theTeams;
            this.results    = theResults;
            this.categories = theScoringCategories;
            this.resultsMap = ArrayListMultimap.create();
            
            // Header for first column
            this.columnNames.add("Teams");
            this.columnTypes.add(String.class);
            // Headers for scoring category columns
            for (Category c : theScoringCategories) {
                this.columnNames.add(c.getDisplayName());
                //this.columnTypes.add(String.class);
                this.columnTypes.add(Double.class);
            }
            // Header for JButton column
            this.columnNames.add("Summaries");
            this.columnTypes.add(JButton.class);
            
            for (TeamResult teamResult : this.results) {
                TeamInfo team = teamResult.getTeam();
                this.resultsMap.put(team, teamResult);
            }
        }

        @Override 
        public int getColumnCount() {
            return this.columnNames.size();
        }

        @Override 
        public int getRowCount() {
            return this.teams.size();
        }

        @Override 
        public String getColumnName(int columnIndex) {
            return this.columnNames.get(columnIndex);
        }

        @Override 
        public Class<?> getColumnClass(int columnIndex) {
            return this.columnTypes.get(columnIndex);
        }

        @Override 
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            TeamInfo theTeam = this.teams.get(rowIndex);
            if (columnIndex == 0) {
                return "Team "+Integer.toString(theTeam.getTeamNumber());
                
            } else if (columnIndex > this.categories.size()) {
                final JButton button = new JButton("Summary");
                button.addActionListener(e -> {
                    Collection<TeamResult> theTeamResults = this.resultsMap.get(theTeam);
                    TeamSummaryPanel summary = new TeamSummaryPanel(theTeam, this.categories, theTeamResults);
                    JScrollPane scrollPane = new JScrollPane(summary);

                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane.setPreferredSize(new Dimension(600, 500));
                    UIManager.put("OptionPane.minimumSize", new Dimension(600, 500));

                    JOptionPane.showMessageDialog(null, scrollPane, 
                                                  "Summary for " + theTeam.toString(),
                                                  JOptionPane.PLAIN_MESSAGE);
                });
                return button;
                
            } else {
                Category c = this.categories.get(columnIndex - 1);
                Collection<TeamResult> theTeamResults = this.resultsMap.get(theTeam);
                Double avgScore = ModelUtils.averageScore(theTeamResults, c);
                avgScore = Math.floor(avgScore * 100) / 100;
                return avgScore;
                //return String.format("%.2f", avgScore);
            }
        }   
        
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private static final Color LIGHT_GRAY = new Color(240, 240, 240);

        private final List<Category> cateogries;

        public JTableCellRenderer(final List<Category> theScoringCateogries) {
            this.cateogries = theScoringCateogries;
        }

        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value, 
                                                       boolean isSelected, 
                                                       boolean hasFocus, 
                                                       int row,
                                                       int column) {
            setValue(value);
            if (row % 2 == 0) {
                setBackground(LIGHT_GRAY);
            } else {
                setBackground(Color.WHITE);
            }
            if (column > this.cateogries.size()) {
                JButton button = (JButton) value;
                return button;
            }
            return this;
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;
        
        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
            int row    = e.getY()/table.getRowHeight(); //get the row of the button

            // Checking the row or column is valid or not
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    ((JButton)value).doClick();
                }
            }
        }
    }

}
