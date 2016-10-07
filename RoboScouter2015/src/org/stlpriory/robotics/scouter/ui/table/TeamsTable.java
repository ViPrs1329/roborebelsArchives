package org.stlpriory.robotics.scouter.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import org.stlpriory.robotics.scouter.io.IDataStore;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.util.UiUtils;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

public class TeamsTable extends JTable {
    private static final long serialVersionUID = 1L;
    
    /** Display size of the image within this panel */
    private static final int IMAGE_WIDTH  = 50;
    private static final int IMAGE_HEIGHT = 50;

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

//        addMouseListener(new JTableButtonMouseListener(this));
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
        setRowHeight(IMAGE_HEIGHT+10);
//        setRowHeight(25);
        
        JTableCellRenderer renderer = new JTableCellRenderer(this.teams);
        renderer.setHorizontalAlignment( JLabel.CENTER );
        setDefaultRenderer(String.class, renderer);
        setDefaultRenderer(Integer.class, renderer);
        setDefaultRenderer(JLabel.class, renderer);
        setDefaultRenderer(JButton.class, renderer);
        setDefaultRenderer(JPanel.class, renderer);
        
        TableColumnModel tcm = getColumnModel();
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            int imageColumnIndex  = 0;
            int numberColumnIndex = 1;
            int nameColumnIndex   = 2;
            int cityColumnIndex   = 3;
            int buttonColumnIndex = 6;
            
            if (i == imageColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(IMAGE_WIDTH);
                
            } else if (i == numberColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(20);
                
            } else if (i == nameColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(100);
                
            } else if (i == buttonColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(60);
                tcm.getColumn(i).setCellRenderer(renderer);
                tcm.getColumn(i).setCellEditor(new ButtonsEditor(this,this.teams));
                
            } else if (i == cityColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(60);
                
            } else {
                tcm.getColumn(i).setPreferredWidth(40);
            }
        }
    }

    public void saveTableUpdates(final IDataStore theStore) {
        Set<TeamInfo> updates = ((JTableModel)getModel()).getUpdates();
        if (this.importAction) {
            updates = new HashSet<>(this.teams);
        }
        if (updates.isEmpty()) {
            return;
        }
        
        int imageColumnIndex   = 0;
        int numberColumnIndex  = 1;
        int nameColumnIndex    = 2;
        int cityColumnIndex    = 3;
        int stateColumnIndex   = 4;
        int countryColumnIndex = 5;
        int buttonColumnIndex  = 6;
        
        for (int rowIndex = 0; rowIndex < getModel().getRowCount(); rowIndex++) {
            TeamInfo theTeam = this.teams.get(rowIndex);
            // If the model data for this team was updated then make those
            // changes in the TeamInfo object before saving it
            if (updates.contains(theTeam)) {
                for (int columnIndex = 0; columnIndex < getModel().getColumnCount(); columnIndex++) {
                    @SuppressWarnings("unused")
                    String name  = getModel().getColumnName(columnIndex);
                    Object value = getModel().getValueAt(rowIndex, columnIndex);
                    
                    if (columnIndex == imageColumnIndex) {
                        // do nothing for the image column
                    } else if (columnIndex == numberColumnIndex) {
                        // do nothing for the team number column
                    } else if (columnIndex == buttonColumnIndex) {
                        // do nothing for the button column
                    } else if (columnIndex == nameColumnIndex) {
                        theTeam.setTeamName((String)value);
                    } else if (columnIndex == cityColumnIndex) {
                        theTeam.setCity((String)value);
                    } else if (columnIndex == stateColumnIndex) {
                        theTeam.setState((String)value);
                    } else if (columnIndex == countryColumnIndex) {
                        theTeam.setCountry((String)value);
                    }
                }
            }
        }

        theStore.saveTeams(updates);
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        int imageColumnIndex  = 0;
        int numberColumnIndex = 1;
        int buttonColumnIndex = 6;
        
        if (columnIndex == imageColumnIndex) {
            return false;
        } else if (columnIndex == numberColumnIndex) {
            return false;
        } else if (columnIndex == buttonColumnIndex) {
            return true;
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
        private final List<TeamInfo> teams;
        private final Set<TeamInfo> updates;
        private final JTableValueHolder tableValueHolder;
        
        public JTableModel(final List<TeamInfo> theTeams) {
            this.teams   = theTeams;
            this.updates = new HashSet<>();
            
            this.columnNames.add("Images");
            this.columnTypes.add(JLabel.class);
            
            this.columnNames.add("Team Number");
            this.columnTypes.add(Integer.class);
            
            this.columnNames.add("Team Name");
            this.columnTypes.add(String.class);
            
            this.columnNames.add("City");
            this.columnTypes.add(String.class);
            
            this.columnNames.add("State");
            this.columnTypes.add(String.class);
            
            this.columnNames.add("Country");
            this.columnTypes.add(String.class);
            
            this.columnNames.add("Team Notes");
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
            int imageColumnIndex   = 0;
            int numberColumnIndex  = 1;
            int buttonColumnIndex  = 6;
            
            if (columnIndex == imageColumnIndex) {
                File imageFile = theTeam.getImageFile();
                BufferedImage robotImage = null;
                try {
                    robotImage = UiUtils.rescale(ImageIO.read(imageFile),IMAGE_WIDTH, IMAGE_HEIGHT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new JLabel(new ImageIcon(robotImage));
                
            } else if (columnIndex == numberColumnIndex) {
                return theTeam.getTeamNumber();
                
            } else if (columnIndex == buttonColumnIndex) {
                return new ButtonsPanel();
                
            } else {
                return this.tableValueHolder.getValue(theTeam, this.columnNames.get(columnIndex));
            }
        }   
        
        @Override
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            int imageColumnIndex   = 0;
            int numberColumnIndex  = 1;
            int buttonColumnIndex  = 6;
            
            if (columnIndex == imageColumnIndex) {
                // image column do nothing                
            } else if (columnIndex == numberColumnIndex) {
                // team number column do nothing                
            } else if (columnIndex == buttonColumnIndex) {
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
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(1)), Integer.toString(theTeam.getTeamNumber()));
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(2)), theTeam.getTeamName());
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(3)), theTeam.getCity());
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(4)), theTeam.getState());
                this.valueHolder.put(getRowKey(theTeam), getColumnKey(theColumnNames.get(5)), theTeam.getCountry());
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
        private final List<TeamInfo> teams;

        public JTableCellRenderer(final List<TeamInfo> theTeams) {
            this.teams = theTeams;
        }

        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value, 
                                                       boolean isSelected, 
                                                       boolean hasFocus, 
                                                       int rowIndex,
                                                       int columnIndex) {
            setValue(value);
            
            TeamInfo theTeam = this.teams.get(rowIndex);
            int imageColumnIndex  = 0;
            int buttonColumnIndex = 6;
            
            if (columnIndex == imageColumnIndex) {
                File imageFile = theTeam.getImageFile();
                BufferedImage robotImage = null;
                try {
                    robotImage = UiUtils.rescale(ImageIO.read(imageFile),IMAGE_WIDTH, IMAGE_HEIGHT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new JLabel(new ImageIcon(robotImage));
                
            } else if (columnIndex == buttonColumnIndex) {
                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                ButtonsPanel panel = (ButtonsPanel) value;
                return panel;
                
            } else {
                setFont(new Font("Arial", Font.PLAIN, 12));
                if (rowIndex % 2 == 0) {
                    setBackground(LIGHT_GRAY);
                } else {
                    setBackground(Color.WHITE);
                }
            }
            return this;
        }
    }
    
//    // ==================================================================================
//    //                        I N N E R   C L A S S
//    // ==================================================================================
//    
//    private static class JTableButtonMouseListener extends MouseAdapter {
//        private final JTable table;
//
//        public JTableButtonMouseListener(JTable table) {
//            this.table = table;
//        }
//
//        public void mouseClicked(MouseEvent e) {
//            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the column of the button
//            int row    = e.getY()/table.getRowHeight(); //get the row of the button
//
//            // Checking the row or column is valid or not
//            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
//                Object value = table.getValueAt(row, column);
//                if (value instanceof JButton) {
//                    ((JButton)value).doClick();
//                }
//            }
//        }
//    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class ButtonsPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        protected JButton notesButton;
        
        public ButtonsPanel() {
            super();
            setOpaque(true);
            
            this.notesButton = new JButton("Notes");
            this.notesButton.setFocusable(false);
            this.notesButton.setRolloverEnabled(false);
            add(this.notesButton);
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class ButtonsEditor extends ButtonsPanel implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        
        protected transient ChangeEvent changeEvent;
        private final JTable table;
        private final List<TeamInfo> teams;

        public ButtonsEditor(final JTable theTable, final List<TeamInfo> theTeams) {
            super();
            this.table = theTable;
            this.teams = theTeams;
            this.notesButton.setAction(new NotesAction(this.table,this.teams));

            EditingStopHandler handler = new EditingStopHandler();
            this.notesButton.addMouseListener(handler);
            addMouseListener(handler);
        }
       
        private class EditingStopHandler extends MouseAdapter implements ActionListener {
            @Override
            public void mousePressed(MouseEvent e) {
                Object o = e.getSource();
                if (o instanceof TableCellEditor) {
                    actionPerformed(null);
                    
                } else if (o instanceof JButton) {
                    ButtonModel m = ((JButton) e.getComponent()).getModel();
                    if (m.isPressed() && table.isRowSelected(table.getEditingRow()) && e.isControlDown()) {
                        setBackground(table.getBackground());
                    }
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        fireEditingStopped();
                    }
                });
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.setBackground(table.getSelectionBackground());
            return this;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        @Override
        public void cancelCellEditing() {
            fireEditingCancelled();
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }

        protected void fireEditingStopped() {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    // Lazily create the event:
                    if (changeEvent == null) {
                        changeEvent = new ChangeEvent(this);
                    }
                    ((CellEditorListener) listeners[i + 1]).editingStopped(changeEvent);
                }
            }
        }

        protected void fireEditingCancelled() {
            // Guaranteed to return a non-null array
            Object[] listeners = listenerList.getListenerList();
            // Process the listeners last to first, notifying
            // those that are interested in this event
            for (int i = listeners.length - 2; i >= 0; i -= 2) {
                if (listeners[i] == CellEditorListener.class) {
                    // Lazily create the event:
                    if (changeEvent == null) {
                        changeEvent = new ChangeEvent(this);
                    }
                    ((CellEditorListener) listeners[i + 1]).editingCanceled(changeEvent);
                }
            }
        }
    }    
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class NotesAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        /** Display size of this panel */
        private static final int PREFERRED_WIDTH  = 500;
        private static final int PREFERRED_HEIGHT = 100;
        
        private final JTable table;
        private final List<TeamInfo> teams;

        public NotesAction(final JTable theTable, final List<TeamInfo> theTeams) {
            super("Notes");
            this.table = theTable;
            this.teams = theTeams;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            TeamInfo theTeam = this.teams.get(row);
            
            String oldNotes = theTeam.getNotes();
            JTextArea textArea = new JTextArea(oldNotes);
            textArea.setColumns(1);
            textArea.setRows(30);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setCaretPosition(textArea.getDocument().getLength());
            JScrollPane scrollPane = new JScrollPane(textArea);

            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
            UIManager.put("OptionPane.minimumSize", new Dimension(PREFERRED_WIDTH+10, PREFERRED_HEIGHT+100));

            int result = JOptionPane.showConfirmDialog(null, scrollPane, "Enter notes for " + theTeam.toString(),
                                                       JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String newNotes = textArea.getText();
                if (!newNotes.equals(oldNotes)) {
                    theTeam.setNotes(newNotes);
                    JTableModel model = (JTableModel)table.getModel();
                    model.getUpdates().add(theTeam);
                }
            }        
        }
    }

    
}
