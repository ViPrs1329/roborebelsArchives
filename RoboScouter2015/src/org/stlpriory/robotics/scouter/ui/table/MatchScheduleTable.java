package org.stlpriory.robotics.scouter.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;

import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.TeamInfo;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

public class MatchScheduleTable extends javax.swing.JTable {
    private static final long serialVersionUID = 1L;
    
    private static final Color LIGHT_BLUE = new Color(133, 167, 255);
    private static final Color LIGHT_RED  = new Color(255, 121, 121);
    
    private final List<MatchInfo> matches;
    private final boolean readOnly;
    private final boolean importAction;
    
    public MatchScheduleTable(final List<MatchInfo> theMatches,
                              final boolean isReadOnly,
                              final boolean isImportAction) {
        super( new JTableModel(theMatches) );
        
        this.matches  = theMatches;
        this.readOnly = isReadOnly;
        this.importAction = isImportAction;
        
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
        
        JTableHeaderCellRenderer headerRenderer = new JTableHeaderCellRenderer();
        headerRenderer.setHorizontalAlignment( JLabel.CENTER );
        JTableCellRenderer renderer = new JTableCellRenderer();
        renderer.setHorizontalAlignment( JLabel.CENTER );
        setDefaultRenderer(String.class, renderer);
        setDefaultRenderer(Integer.class, renderer);
        setDefaultRenderer(Double.class, renderer);
        setDefaultRenderer(JButton.class, renderer);
        
        TableColumnModel tcm = getColumnModel();
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            tcm.getColumn(i).setHeaderRenderer(headerRenderer);
            if (i == 0) {
                tcm.getColumn(i).setPreferredWidth(120);
            } else {
                tcm.getColumn(i).setPreferredWidth(80);
            }
        }

    }
    
    public void saveTableUpdates(final IDataStore theStore) {
        Set<MatchInfo> updates = ((JTableModel)getModel()).getUpdates();
        if (this.importAction) {
            updates = new HashSet<>(this.matches);
        }
        if (updates.isEmpty()) {
            return;
        }
        
        Map<Integer, TeamInfo> teamsByNumber = new HashMap<>();
        theStore.fetchAllTeams().forEach(t -> teamsByNumber.put(t.getTeamNumber(),t));

        for (int rowIndex = 0; rowIndex < getModel().getRowCount(); rowIndex++) {
            MatchInfo theMatch = this.matches.get(rowIndex);

            // If the model data for this match was updated then make those
            // changes in the MatchInfo object before saving it
            if (updates.contains(theMatch)) {
                for (int columnIndex = 0; columnIndex < getModel().getColumnCount(); columnIndex++) {
                    @SuppressWarnings("unused")
                    String name  = getModel().getColumnName(columnIndex);
                    Object value = getModel().getValueAt(rowIndex, columnIndex);
                    
                    if (columnIndex == 0) {
                        // do nothing for the match number column
                    } else if (columnIndex == 1 || columnIndex == 2 || columnIndex == 3) {
                        Integer theTeamNumber = (Integer)value;
                        TeamInfo theTeam = teamsByNumber.get(theTeamNumber);
                        if (theTeam != null) {
                            theMatch.getBlueAlliance().getTeams().set(columnIndex-1,theTeam);
                        }
                    } else if (columnIndex == 4 || columnIndex == 5 || columnIndex == 6) {
                        Integer theTeamNumber = (Integer)value;
                        TeamInfo theTeam = teamsByNumber.get(theTeamNumber);
                        if (theTeam != null) {
                            theMatch.getRedAlliance().getTeams().set(columnIndex-4,theTeam);
                        }
                    }
                }
            }
        }
        
        theStore.saveMatches(updates);        
    }
    
    public boolean isCellEditable(int rowIndex, int colIndex) {
        if (colIndex == 0) {
            return false;
        }
        return !this.readOnly;
    }
    
    public List<MatchInfo> getMatches() {
        return this.matches;
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private final List<String> columnNames   = new ArrayList<>();
        private final List<Class<?>> columnTypes = new ArrayList<>();
        private final List<MatchInfo> matches;
        private final Set<MatchInfo> updates;
        private final JTableValueHolder tableValueHolder;
        
        public JTableModel(final List<MatchInfo> theMatches) {
            this.matches = theMatches;
            this.updates = new HashSet<>();
            
            this.columnNames.add("Match Number");
            this.columnNames.add("Blue Team 1");
            this.columnNames.add("Blue Team 2");
            this.columnNames.add("Blue Team 3");
            this.columnNames.add("Red Team 1");
            this.columnNames.add("Red Team 2");
            this.columnNames.add("Red Team 3");

            this.columnTypes.add(Integer.class);
            this.columnTypes.add(Integer.class);
            this.columnTypes.add(Integer.class);
            this.columnTypes.add(Integer.class);
            this.columnTypes.add(Integer.class);
            this.columnTypes.add(Integer.class);
            this.columnTypes.add(Integer.class);
            
            this.tableValueHolder = new JTableValueHolder(theMatches, this.columnNames);
        }

        @Override 
        public int getColumnCount() {
            return this.columnNames.size();
        }

        @Override 
        public int getRowCount() {
            return this.matches.size();
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
            MatchInfo theMatch = this.matches.get(rowIndex);
            
            switch (columnIndex) {
                case 0: return theMatch.getMatchNumber();
                case 1: return this.tableValueHolder.getValue(theMatch, this.columnNames.get(columnIndex));
                case 2: return this.tableValueHolder.getValue(theMatch, this.columnNames.get(columnIndex));
                case 3: return this.tableValueHolder.getValue(theMatch, this.columnNames.get(columnIndex));
                case 4: return this.tableValueHolder.getValue(theMatch, this.columnNames.get(columnIndex));
                case 5: return this.tableValueHolder.getValue(theMatch, this.columnNames.get(columnIndex));
                case 6: return this.tableValueHolder.getValue(theMatch, this.columnNames.get(columnIndex));
                default: throw new IllegalArgumentException();
            }
        }   
        
        @Override
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            if (columnIndex == 0) {
                // match number column do nothing                
            } else {
                MatchInfo theMatch = this.matches.get(rowIndex);
                Integer oldValue =  this.tableValueHolder.getValue(theMatch, this.columnNames.get(columnIndex));
                if (! aValue.equals(oldValue)) {
                    this.tableValueHolder.putValue(theMatch, this.columnNames.get(columnIndex), (Integer)aValue);
                    this.updates.add(theMatch);
                }
            }
        }
      
        public Set<MatchInfo> getUpdates() {
            return this.updates;
        }
        
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class JTableValueHolder {
        // Table is a class from the Google Guava libraries that simplifies
        // storing and retrieving values by row and column identifiers
        private final Table<Integer, String, Integer> valueHolder;
        
        public JTableValueHolder(final List<MatchInfo> theMatches, 
                                 final List<String> theColumnNames) {
            
            List<Integer> rowList = theMatches.stream()
                                              .map(m -> getRowKey(m))
                                              .collect(Collectors.toList());
            List<String> columnList = theColumnNames.stream()
                                                    .map(c -> getColumnKey(c))
                                                    .collect(Collectors.toList());
            this.valueHolder = ArrayTable.create(rowList, columnList);
            for (MatchInfo theMatch : theMatches) {
                this.valueHolder.put(getRowKey(theMatch), getColumnKey(theColumnNames.get(0)), theMatch.getMatchNumber());
                int columnIndex = 0;
                for (TeamInfo theTeam : theMatch.getTeams()) {
                    columnIndex++;
                    this.valueHolder.put(getRowKey(theMatch), getColumnKey(theColumnNames.get(columnIndex)), theTeam.getTeamNumber());
                }
            }
        }       
        
        public Integer getValue(final MatchInfo theMatch, final String theColumnName) {
            return this.valueHolder.get(getRowKey(theMatch), getColumnKey(theColumnName));
        }
        
        public void putValue(final MatchInfo theMatch, final String theColumnName, final Integer value) {
            this.valueHolder.put(getRowKey(theMatch), getColumnKey(theColumnName), value);
        }
        
        private Integer getRowKey(final MatchInfo theMatch) {
            return theMatch.getMatchNumber();
        }
        
        private String getColumnKey(final String theColumnName) {
            return theColumnName;
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private static final Color LIGHT_GRAY = new Color(240, 240, 240);

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
            return this;
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableHeaderCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setValue(value);
            if (column == 0) {
                setBackground(Color.LIGHT_GRAY);
            } else if (column > 0 && column < 4) {
                setBackground(LIGHT_BLUE);
            } else {
                setBackground(LIGHT_RED);
            }
            return this;
        }
    }

}
