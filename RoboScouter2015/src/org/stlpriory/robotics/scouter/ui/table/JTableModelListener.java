package org.stlpriory.robotics.scouter.ui.table;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class JTableModelListener implements TableModelListener {
    private final JTable table;

    private JTableModelListener(final JTable table) {
        this.table = table;
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        int firstRow = e.getFirstRow();
        int lastRow = e.getLastRow();
        int index = e.getColumn();

        switch (e.getType()) {
            case TableModelEvent.INSERT:
                for (int i = firstRow; i <= lastRow; i++) {
                    System.out.println("Insert at (" + i + "," + index + ")");
                }
                break;
            case TableModelEvent.UPDATE:
                if (firstRow == TableModelEvent.HEADER_ROW) {
                    if (index == TableModelEvent.ALL_COLUMNS) {
                        System.out.println("A column was added");
                    } else {
                        System.out.println(index + "in header changed");
                    }
                } else {
                    for (int i = firstRow; i <= lastRow; i++) {
                        if (index == TableModelEvent.ALL_COLUMNS) {
                            System.out.println("All columns have changed");
                        } else {
                            int row = i + 1;
                            int column = index + 1;
                            System.out.println("Updated value at (" + row + "," + column + ") to " + this.table.getValueAt(i, index));
                        }
                    }
                }
                break;
            case TableModelEvent.DELETE:
                for (int i = firstRow; i <= lastRow; i++) {
                    System.out.println("Delete at (" + i + "," + index + ")");
                }
                break;
        }
    }

}
