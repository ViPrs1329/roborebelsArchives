package org.stlpriory.robotics.scouter.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

public class TeamResultsTable extends JTable {
    private static final long serialVersionUID = 1L;
    
    private final List<TeamResult> teamResults;
    private final List<Category> categories;
    private boolean readOnly;
    
    public TeamResultsTable(final TeamInfo theTeam, 
                            final List<TeamResult> theResults,
                            final List<Category> theScoringCategories, 
                            final boolean isReadOnly) {
        super( new JTableModel(theResults,theScoringCategories) );

        this.teamResults = theResults;
        this.categories  = theScoringCategories;
        this.readOnly    = isReadOnly;
        
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
        setRowHeight(30);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        setDefaultRenderer(String.class, centerRenderer);
        setDefaultRenderer(Integer.class, centerRenderer);
        
        JTableCellRenderer renderer = new JTableCellRenderer(theScoringCategories);
        renderer.setHorizontalAlignment( JLabel.CENTER );
        TableColumnModel tcm = getColumnModel();
        tcm.getColumn(0).setCellRenderer(renderer);
        tcm.getColumn(theScoringCategories.size()+1).setCellRenderer(renderer);

        for (int i = 0; i < tcm.getColumnCount(); i++) {
            if (i == 0) {
                tcm.getColumn(i).setPreferredWidth(120);
            } else {
                tcm.getColumn(i).setPreferredWidth(80);
            }
        }
    }
    
    public void saveTableUpdates(final IDataStore theStore) {
        Set<TeamResult> updates = ((JTableModel)getModel()).getUpdates();
        if (updates.isEmpty()) {
            return;
        }

        for (int rowIndex = 0; rowIndex < getModel().getRowCount(); rowIndex++) {
            TeamResult theTeamResult = this.teamResults.get(rowIndex);

            // If the model data for this team result was updated then make those
            // changes in the TeamResult object before saving it
            if (updates.contains(theTeamResult)) {
                for (int columnIndex = 0; columnIndex < getModel().getColumnCount(); columnIndex++) {
                    @SuppressWarnings("unused")
                    String name  = getModel().getColumnName(columnIndex);
                    Object value = getModel().getValueAt(rowIndex, columnIndex);
                    
                    if (columnIndex == 0) {
                        // do nothing for the header column
                    } else if (columnIndex > this.categories.size()) {
                        // do nothing for the button column
                    } else {
                        Category c = this.categories.get(columnIndex - 1);
                        theTeamResult.setScore(c, (Integer)value);
                    }
                }
            }
            
        }
        
        theStore.saveTeamResults(updates);
    }
    
    public boolean isCellEditable(int rowIndex, int colIndex) {
        if (colIndex == 0) {
            return false;
        }
        return !this.readOnly;
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private final List<String> columnNames   = new ArrayList<>();
        private final List<Class<?>> columnTypes = new ArrayList<>();
        private final List<TeamResult> results;
        private final List<Category> categories;
        private final Set<TeamResult> updates;
        private final JTableValueHolder tableValueHolder;
        
        public JTableModel(final List<TeamResult> theResults, 
                           final List<Category> theScoringCategories) {
            this.results    = theResults;
            this.categories = theScoringCategories;
            this.updates    = new HashSet<>();
            this.tableValueHolder = new JTableValueHolder(theResults,theScoringCategories);
            
            // Header for first column
            this.columnNames.add("Matches");
            this.columnTypes.add(String.class);
            
            // Headers for scoring category columns
            for (Category c : theScoringCategories) {
                this.columnNames.add(c.getDisplayName());
                this.columnTypes.add(Integer.class);
            }
            
            // Header for JButton column
            this.columnNames.add("Match Notes");
            this.columnTypes.add(JButton.class);
        }

        @Override 
        public int getColumnCount() {
            return this.columnNames.size();
        }

        @Override 
        public int getRowCount() {
            return this.results.size();
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
            TeamResult tr = this.results.get(rowIndex);
            if (columnIndex == 0) {
                return "Match "+Integer.toString(tr.getMatch().getMatchNumber());
                
            } else if (columnIndex > this.categories.size()) {
                final JButton button = new JButton("Notes");
                button.addActionListener(e -> {
                    MatchInfo theMatch = tr.getMatch();
                    String oldNotes = tr.getNotes();
                    JTextArea textArea = new JTextArea(oldNotes);
                    textArea.setColumns(1);
                    textArea.setRows(30);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setCaretPosition(textArea.getDocument().getLength());
                    JScrollPane scrollPane = new JScrollPane(textArea);

                    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                    scrollPane.setPreferredSize(new Dimension(500, 100));
                    UIManager.put("OptionPane.minimumSize", new Dimension(510, 200));

                    int result = JOptionPane.showConfirmDialog(null, scrollPane, 
                                                               "Enter notes for " + theMatch.toString(),
                                                               JOptionPane.OK_CANCEL_OPTION, 
                                                               JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        String newNotes = textArea.getText();
                        if (! newNotes.equals(oldNotes)) {
                            tr.setNotes(newNotes);
                            this.updates.add(tr);
                        }
                    }
                });
                return button;
                
            } else {
                Category c = this.categories.get(columnIndex - 1);
                return this.tableValueHolder.getValue(tr.getMatch(), c);
            }
        }   
        
        @Override
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            if (columnIndex == 0) {
                // header column do nothing                
            } else if (columnIndex > this.categories.size()) {
                // button column do nothing                
            } else {
                TeamResult tr = this.results.get(rowIndex);
                Category c = this.categories.get(columnIndex - 1);
                Integer oldValue = this.tableValueHolder.getValue(tr.getMatch(), c);
                if (! aValue.equals(oldValue)) {
                    this.tableValueHolder.putValue(tr.getMatch(), c, (Integer)aValue);
                    this.updates.add(tr);
                }
            }
        }
        
        public Set<TeamResult> getUpdates() {
            return this.updates;
        }
        
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class JTableValueHolder {
        // Table is a class from the Google Guava libraries that simplifies
        // storing and retrieving values by row and column identifiers
        private final Table<String, String, Integer> valueHolder;
        
        public JTableValueHolder(final List<TeamResult> theResults, 
                                 final List<Category> theScoringCateogries) {
            
            List<String> rowList = theResults.stream()
                                             .map(tr -> getRowKey(tr.getMatch()))
                                             .collect(Collectors.toList());
            List<String> columnList = theScoringCateogries.stream()
                                                          .map(c -> getColumnKey(c))
                                                          .collect(Collectors.toList());
            this.valueHolder = ArrayTable.create(rowList, columnList);
            for (TeamResult tr : theResults) {
                for (Category category : theScoringCateogries) {
                    this.valueHolder.put(getRowKey(tr.getMatch()), getColumnKey(category), tr.getScore(category));
                }
            }
        }       
        
        public Integer getValue(final MatchInfo theMatch, final Category theCategory) {
            return this.valueHolder.get(getRowKey(theMatch), getColumnKey(theCategory));
        }
        
        public void putValue(final MatchInfo theMatch, final Category theCategory, final Integer value) {
            this.valueHolder.put(getRowKey(theMatch), getColumnKey(theCategory), value);
        }
        
        private String getRowKey(final MatchInfo theMatch) {
            return Integer.toString(theMatch.getMatchNumber());
        }
        
        private String getColumnKey(final Category theCategory) {
            return theCategory.getName();
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
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
