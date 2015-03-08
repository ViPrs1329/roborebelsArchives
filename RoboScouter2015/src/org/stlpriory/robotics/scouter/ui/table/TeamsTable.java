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
import org.stlpriory.robotics.scouter.model.TeamInfo;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

public class TeamsTable extends JTable {
    private static final long serialVersionUID = 1L;

    private final List<TeamInfo> teams;
    private final boolean readOnly;
    private final boolean importAction;

    public TeamsTable(final List<TeamInfo> theTeams, 
                      final boolean isReadOnly,
                      final boolean isImportAction) {
        super( new JTableModel(theTeams) );
        this.teams    = theTeams;
        this.readOnly = isReadOnly;
        this.importAction = isImportAction;

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
        
        JTableCellRenderer renderer = new JTableCellRenderer();
        renderer.setHorizontalAlignment( JLabel.CENTER );
        setDefaultRenderer(String.class, renderer);
        setDefaultRenderer(Integer.class, renderer);
        setDefaultRenderer(JButton.class, renderer);
        
        TableColumnModel tcm = getColumnModel();
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            if (i == 0) {
                tcm.getColumn(i).setPreferredWidth(20);
            } else if ((i == 1) || (i == 4) || (i == 5)) {
                tcm.getColumn(i).setPreferredWidth(40);
            } else {
                tcm.getColumn(i).setPreferredWidth(100);
            }
        }
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int colIndex) {
        if (colIndex == 0) {
            return false;
        }
        return !this.readOnly;
    }

    public void saveTableUpdates(final IDataStore theStore) {
        Set<TeamInfo> updates = ((JTableModel)getModel()).getUpdates();
        if (this.importAction) {
            updates = new HashSet<>(this.teams);
        }
        if (updates.isEmpty()) {
            return;
        }
        
        for (int rowIndex = 0; rowIndex < getModel().getRowCount(); rowIndex++) {
            TeamInfo theTeam = this.teams.get(rowIndex);
            // If the model data for this team was updated then make those
            // changes in the TeamInfo object before saving it
            if (updates.contains(theTeam)) {
                for (int columnIndex = 0; columnIndex < getModel().getColumnCount(); columnIndex++) {
                    @SuppressWarnings("unused")
                    String name  = getModel().getColumnName(columnIndex);
                    Object value = getModel().getValueAt(rowIndex, columnIndex);
                    if (columnIndex == 1) {
                        theTeam.setTeamName((String)value);
                    } else if (columnIndex == 2) {
                        theTeam.setCity((String)value);
                    } else if (columnIndex == 3) {
                        theTeam.setState((String)value);
                    } else if (columnIndex == 4) {
                        theTeam.setCountry((String)value);
                    }
                }
            }
        }

        theStore.saveTeams(updates);
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class JTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;
        private final List<String> columnNames   = new ArrayList<>();
        private final List<Class<?>> columnTypes = new ArrayList<>();
        private final List<TeamInfo> teams;
        private final Set<TeamInfo> updates;
        private final JTableValueHolder tableValueHolder;
        
        public JTableModel(final List<TeamInfo> theTeams) {
            this.teams   = theTeams;
            this.updates = new HashSet<>();
            
            this.columnNames.add("Team Number");
            this.columnNames.add("Team Name");
            this.columnNames.add("City");
            this.columnNames.add("State");
            this.columnNames.add("Country");
            this.columnNames.add("Team Notes");

            this.columnTypes.add(Integer.class);
            this.columnTypes.add(String.class);
            this.columnTypes.add(String.class);
            this.columnTypes.add(String.class);
            this.columnTypes.add(String.class);
            this.columnTypes.add(JButton.class);
            
            this.tableValueHolder = new JTableValueHolder(theTeams, this.columnNames);
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
            
            switch (columnIndex) {
                case 0: return theTeam.getTeamNumber();
                case 1: return this.tableValueHolder.getValue(theTeam, this.columnNames.get(columnIndex));
                case 2: return this.tableValueHolder.getValue(theTeam, this.columnNames.get(columnIndex));
                case 3: return this.tableValueHolder.getValue(theTeam, this.columnNames.get(columnIndex));
                case 4: return this.tableValueHolder.getValue(theTeam, this.columnNames.get(columnIndex));
                case 5: {
                    final JButton button = new JButton("Notes");
                    button.addActionListener(e -> {
                        String oldNotes = theTeam.getNotes();
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
                                                                   "Enter notes for " + theTeam.toString(),
                                                                   JOptionPane.OK_CANCEL_OPTION, 
                                                                   JOptionPane.PLAIN_MESSAGE);
                        if (result == JOptionPane.OK_OPTION) {
                            String newNotes = textArea.getText();
                            if (! newNotes.equals(oldNotes)) {
                                theTeam.setNotes(newNotes);
                                this.updates.add(theTeam);
                            }
                        }
                    });
                    return button;
                }
                default: throw new IllegalArgumentException();
            }
        }   
        
        @Override
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            if (columnIndex == 0) {
                // team number column do nothing                
            } else if (columnIndex == 5) {
                // button column do nothing                
            } else {
                TeamInfo theTeam = this.teams.get(rowIndex);
                String oldValue =  this.tableValueHolder.getValue(theTeam, this.columnNames.get(columnIndex));
                if (! aValue.equals(oldValue)) {
                    this.tableValueHolder.putValue(theTeam, this.columnNames.get(columnIndex), (String)aValue);
                    this.updates.add(theTeam);
                }
            }
        }
        
        public Set<TeamInfo> getUpdates() {
            return this.updates;
        }
        
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class JTableValueHolder {
        // Table is a class from the Google Guava libraries that simplifies
        // storing and retrieving values by row and column identifiers
        private final Table<Integer, String, String> valueHolder;
        
        public JTableValueHolder(final List<TeamInfo> theTeams, 
                                 final List<String> theColumnNames) {
            
            List<Integer> rowList = theTeams.stream()
                                            .map(t -> getRowKey(t))
                                            .collect(Collectors.toList());
            List<String> columnList = theColumnNames.stream()
                                                    .map(c -> getColumnKey(c))
                                                    .collect(Collectors.toList());
            this.valueHolder = ArrayTable.create(rowList, columnList);
            for (TeamInfo theTeam : theTeams) {
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(0)), Integer.toString(theTeam.getTeamNumber()));
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(1)), theTeam.getTeamName());
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(2)), theTeam.getCity());
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(3)), theTeam.getState());
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(4)), theTeam.getCountry());
            }
        }       
        
        public String getValue(final TeamInfo theTeam, final String theColumnName) {
            return this.valueHolder.get(getRowKey(theTeam), getColumnKey(theColumnName));
        }
        
        public void putValue(final TeamInfo theTeam, final String theColumnName, final String value) {
            this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnName), value);
        }
        
        private Integer getRowKey(final TeamInfo theTeam) {
            return theTeam.getTeamNumber();
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
            if (column == 5) {
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
