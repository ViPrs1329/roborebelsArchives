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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.stlpriory.robotics.scouter.model.Category;
import org.stlpriory.robotics.scouter.model.MatchInfo;
import org.stlpriory.robotics.scouter.model.MatchResult;
import org.stlpriory.robotics.scouter.model.TeamInfo;
import org.stlpriory.robotics.scouter.model.TeamResult;
import org.stlpriory.robotics.scouter.ui.TeamScoringPanel;
import org.stlpriory.robotics.scouter.util.UiUtils;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.Table;

public class MatchResultTable extends JTable {
    private static final long serialVersionUID = 1L;
    
    private static final Color LIGHT_BLUE = new Color(133, 167, 255);
    private static final Color LIGHT_RED  = new Color(255, 121, 121);
    
    /** Display size of the image within this panel */
    private static final int IMAGE_WIDTH  = 50;
    private static final int IMAGE_HEIGHT = 50;
    
    private final MatchResult matchResult;
    private final List<Category> categories;
    private final boolean readOnly;
    
    public MatchResultTable(final MatchResult theResult, 
                            final List<Category> theScoringCategories, 
                            final boolean isReadOnly) {
        super( new JTableModel(theResult,theScoringCategories) );
        
        this.matchResult = theResult;
        this.categories  = theScoringCategories;
        this.readOnly    = isReadOnly;
        
        addMouseListener(new JTableButtonMouseListener(this));
        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
        setCellSelectionEnabled(true);
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        
        setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));  
        setRowHeight(IMAGE_HEIGHT+10);
        //setRowHeight(25);
        
        JTableCellRenderer renderer = new JTableCellRenderer(theResult,theScoringCategories);
        renderer.setHorizontalAlignment( JLabel.CENTER );
        setDefaultRenderer(String.class, renderer);
        setDefaultRenderer(Integer.class, renderer);
        setDefaultRenderer(JLabel.class, renderer);
        setDefaultRenderer(JButton.class, renderer);
        setDefaultRenderer(JPanel.class, renderer);

        TableColumnModel tcm = getColumnModel();
        for (int i = 0; i < tcm.getColumnCount(); i++) {
            int imageColumnIndex  = 0;
            int nameColumnIndex   = 1;
            int buttonColumnIndex = this.categories.size() + 2;
            
            if (i == imageColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(IMAGE_WIDTH);
                
            } else if (i == nameColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(120);
                
            } else if (i == buttonColumnIndex) {
                tcm.getColumn(i).setPreferredWidth(100);
                tcm.getColumn(i).setCellRenderer(renderer);
                tcm.getColumn(i).setCellEditor(new ButtonsEditor(this,this.matchResult));
                
            } else {
                tcm.getColumn(i).setPreferredWidth(60);
            }
        }

    }
    
    public void saveTableUpdates(final IDataStore theStore) {
        Set<TeamResult> updates = ((JTableModel)getModel()).getUpdates();
        if (updates.isEmpty()) {
            return;
        }
        
        for (int rowIndex = 0; rowIndex < getModel().getRowCount(); rowIndex++) {
            int imageColumnIndex  = 0;
            int nameColumnIndex   = 1;
            int buttonColumnIndex = this.categories.size() + 2;

            TeamResult theTeamResult = this.matchResult.getTeamResults().get(rowIndex);

            // If the model data for this team result was updated then make those
            // changes in the TeamResult object before saving it
            if (updates.contains(theTeamResult)) {
                for (int columnIndex = 0; columnIndex < getModel().getColumnCount(); columnIndex++) {
                    int categoryIndex = columnIndex - 2;

                    @SuppressWarnings("unused")
                    String name  = getModel().getColumnName(columnIndex);
                    Object value = getModel().getValueAt(rowIndex, columnIndex);
                    
                    if (columnIndex == imageColumnIndex) {
                        // do nothing for the image column
                    } else if (columnIndex == nameColumnIndex) {
                        // do nothing for the team name column
                    } else if (columnIndex == buttonColumnIndex) {
                        // do nothing for the button column
                    } else {
                        Category c = this.categories.get(categoryIndex);
                        theTeamResult.setScore(c, (Integer)value);
                    }
                }
            }
            
        }
        
        theStore.saveTeamResults(updates);        
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        int imageColumnIndex  = 0;
        int nameColumnIndex   = 1;
        int buttonColumnIndex = this.categories.size() + 2;
        
        if (columnIndex == imageColumnIndex) {
            return false;
        } else if (columnIndex == nameColumnIndex) {
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
        private final List<TeamResult> results;
        private final List<Category> categories;
        private final Set<TeamResult> updates;
        private final JTableValueHolder tableValueHolder;
        
        public JTableModel(final MatchResult theMatchResult, 
                           final List<Category> theScoringCategories) {
            this.results    = theMatchResult.getTeamResults();
            this.categories = theScoringCategories;
            this.updates    = new HashSet<>();
            this.tableValueHolder = new JTableValueHolder(theMatchResult,theScoringCategories);
            
            // Header for image column
            this.columnNames.add("Images");
            this.columnTypes.add(JLabel.class);
            // Header for team name column
            this.columnNames.add("Teams");
            this.columnTypes.add(String.class);
            // Headers for each scoring category column
            for (Category c : theScoringCategories) {
                this.columnNames.add(c.getDisplayName());
                this.columnTypes.add(Integer.class);
            }
            // Header for JButton column
            this.columnNames.add("Match Scoring");
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
            int imageColumnIndex  = 0;
            int nameColumnIndex   = 1;
            int buttonColumnIndex = this.categories.size() + 2;
            int categoryIndex     = columnIndex - 2;
            
            TeamResult tr = this.results.get(rowIndex);
            if (columnIndex == imageColumnIndex) {
                TeamInfo theTeam = tr.getTeam();
                File imageFile = theTeam.getImageFile();
                BufferedImage robotImage = null;
                try {
                    robotImage = UiUtils.rescale(ImageIO.read(imageFile),IMAGE_WIDTH, IMAGE_HEIGHT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new JLabel(new ImageIcon(robotImage));
                
            } else if (columnIndex == nameColumnIndex) {
                return "Team "+Integer.toString(tr.getTeam().getTeamNumber());
                
            } else if (columnIndex == buttonColumnIndex) {
                return new ButtonsPanel();
                
            } else {
                Category c = this.categories.get(categoryIndex);
                return this.tableValueHolder.getValue(tr.getTeam(), c);
            }
        }   
        
        @Override
        public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
            int imageColumnIndex  = 0;
            int nameColumnIndex   = 1;
            int buttonColumnIndex = this.categories.size() + 2;
            int categoryIndex     = columnIndex - 2;
            
            if (columnIndex == imageColumnIndex) {
                // image column do nothing                
            } else if (columnIndex == nameColumnIndex) {
                // team name column do nothing                
            } else if (columnIndex == buttonColumnIndex) {
                // button column do nothing                
            } else {
                TeamResult tr = this.results.get(rowIndex);
                Category c    = this.categories.get(categoryIndex);
                Integer oldValue = this.tableValueHolder.getValue(tr.getTeam(), c);
                if (! aValue.equals(oldValue)) {
                    this.tableValueHolder.putValue(tr.getTeam(), c, (Integer)aValue);
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
        
        public JTableValueHolder(final MatchResult theMatchResult, 
                                 final List<Category> theScoringCateogries) {
            
            List<String> rowList = theMatchResult.getTeamResults()
                                                 .stream()
                                                 .map(tr -> getRowKey(tr.getTeam()))
                                                 .collect(Collectors.toList());
            List<String> columnList = theScoringCateogries.stream()
                                                          .map(c -> getColumnKey(c))
                                                          .collect(Collectors.toList());
            this.valueHolder = ArrayTable.create(rowList, columnList);
            for (TeamResult tr : theMatchResult.getTeamResults()) {
                for (Category category : theScoringCateogries) {
                    this.valueHolder.put(getRowKey(tr.getTeam()), getColumnKey(category), tr.getScore(category));
                }
            }
        }       
        
        public Integer getValue(final TeamInfo theTeam, final Category theCategory) {
            return this.valueHolder.get(getRowKey(theTeam), getColumnKey(theCategory));
        }
        
        public void putValue(final TeamInfo theTeam, final Category theCategory, final Integer value) {
            this.valueHolder.put(getRowKey(theTeam), getColumnKey(theCategory), value);
        }
        
        private String getRowKey(final TeamInfo theTeam) {
            return Integer.toString(theTeam.getTeamNumber());
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
        private final MatchResult matchResult;
        private final List<Category> categories;

        public JTableCellRenderer(final MatchResult theMatchResult, 
                                  final List<Category> theScoringCateogries) {
            this.matchResult = theMatchResult;
            this.categories  = theScoringCateogries;
        }

        public Component getTableCellRendererComponent(JTable table, 
                                                       Object value, 
                                                       boolean isSelected, 
                                                       boolean hasFocus, 
                                                       int rowIndex,
                                                       int columnIndex) {
            setValue(value);
            
            int imageColumnIndex  = 0;
            int nameColumnIndex   = 1;
            int buttonColumnIndex = this.categories.size() + 2;
            
            if (columnIndex == imageColumnIndex) {
                TeamInfo theTeam = this.matchResult.getMatch().getTeams().get(rowIndex);
                File imageFile = theTeam.getImageFile();
                BufferedImage robotImage = null;
                try {
                    robotImage = UiUtils.rescale(ImageIO.read(imageFile),IMAGE_WIDTH, IMAGE_HEIGHT);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new JLabel(new ImageIcon(robotImage));
                
            } else if (columnIndex == nameColumnIndex) {
                if (rowIndex >= 0 && rowIndex <= 2) {
                    setBackground(LIGHT_BLUE);
                } else if (rowIndex >= 3 && rowIndex <= 5) {
                    setBackground(LIGHT_RED);
                }
                setFont(new Font("Arial", Font.PLAIN, 16));
                
            } else if (columnIndex == buttonColumnIndex) {
                setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                ButtonsPanel panel = (ButtonsPanel) value;
                return panel;
                
            } else {
                setBackground(Color.WHITE);
                setFont(new Font("Arial", Font.PLAIN, 16));
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
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row    = e.getY()/table.getRowHeight();

            // Checking the row or column is valid or not
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    ((JButton)value).doClick();
                }
            }
        }
    }    
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class ButtonsPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        
        protected JButton notesButton;
        protected JButton scoringButton;
        
        public ButtonsPanel() {
            super();
            setOpaque(true);
            
            this.notesButton = new JButton("Notes");
            this.notesButton.setFocusable(false);
            this.notesButton.setRolloverEnabled(false);
            add(this.notesButton);
            
            this.scoringButton = new JButton("Scoring");
            this.scoringButton.setFocusable(false);
            this.scoringButton.setRolloverEnabled(false);
            add(this.scoringButton);
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================
    
    private static class ButtonsEditor extends ButtonsPanel implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        
        protected transient ChangeEvent changeEvent;
        private final JTable table;
        private final MatchResult matchResult;

        public ButtonsEditor(final JTable theTable, final MatchResult theResult) {
            super();
            this.table = theTable;
            this.matchResult = theResult;
            this.notesButton.setAction(new NotesAction(this.table,this.matchResult));
            this.scoringButton.setAction(new ScoringAction(this.table,this.matchResult));

            EditingStopHandler handler = new EditingStopHandler();
            this.notesButton.addMouseListener(handler);
            this.scoringButton.addMouseListener(handler);
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
            fireEditingCanceled();
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }

//        public CellEditorListener[] getCellEditorListeners() {
//            return listenerList.getListeners(CellEditorListener.class);
//        }

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

        protected void fireEditingCanceled() {
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
        private final MatchResult result;

        public NotesAction(final JTable theTable, final MatchResult theMatchResult) {
            super("Notes");
            this.table  = theTable;
            this.result = theMatchResult;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            TeamResult theResult = this.result.getTeamResults().get(row);
            TeamInfo theTeam = theResult.getTeam();
            
            String oldNotes = theResult.getNotes();
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
                    theResult.setNotes(newNotes);
                    JTableModel model = (JTableModel)table.getModel();
                    model.getUpdates().add(theResult);
                }
            }        
        }
    }
    
    // ==================================================================================
    //                        I N N E R   C L A S S
    // ==================================================================================

    private static class ScoringAction extends AbstractAction {
        private static final long serialVersionUID = 1L;

        /** Display size of this panel */
        private static final int PREFERRED_WIDTH  = 500;
        private static final int PREFERRED_HEIGHT = 600;
        
        private final JTable table;
        private final MatchResult result;
        private final Map<Integer,TeamScoringPanel> scoreSheets;

        public ScoringAction(final JTable theTable, final MatchResult theMatchResult) {
            super("Scoring");
            this.table  = theTable;
            this.result = theMatchResult;
            this.scoreSheets = new HashMap<>();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.convertRowIndexToModel(table.getEditingRow());
            TeamResult theResult  = this.result.getTeamResults().get(row);
            MatchInfo theMatch    = theResult.getMatch();
            TeamInfo theTeam      = theResult.getTeam();
            Integer theTeamNumber= theTeam.getTeamNumber();
            
            if (! this.scoreSheets.containsKey(theTeamNumber)) {
                this.scoreSheets.put(theTeamNumber, new TeamScoringPanel(theMatch,theTeam) );
            }
            TeamScoringPanel scoreSheet = this.scoreSheets.get(theTeamNumber);
            JScrollPane scrollPane = new JScrollPane(scoreSheet);

            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
            UIManager.put("OptionPane.minimumSize", new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

            JOptionPane.showMessageDialog(null, scrollPane, 
                                          "Match scoring for " + theTeam.toString(),
                                          JOptionPane.PLAIN_MESSAGE);
            
            JTableModel model = (JTableModel)table.getModel();
            model.setValueAt(scoreSheet.getAutoScore(),  row, 2);
            model.setValueAt(scoreSheet.getTotesScore(), row, 3);
            model.setValueAt(scoreSheet.getBinsScore(),  row, 4);
            model.setValueAt(scoreSheet.getFlawsScore(), row, 5);
            model.setValueAt(scoreSheet.getMatchScore(), row, 6);
            theResult.setNotes(theResult.getNotes()+ " " + scoreSheet.getMatchNotes());
            model.fireTableDataChanged();
        }
    }
    
    
}
