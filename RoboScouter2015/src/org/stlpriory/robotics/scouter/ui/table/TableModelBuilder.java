package org.stlpriory.robotics.scouter.ui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TableModelBuilder {

    private final List<String> columnNames;
    private final List<List<String>> columnValues;

    private TableModelBuilder() {
        this.columnNames  = new ArrayList<>();
        this.columnValues = new ArrayList<>();
    }

    public static TableModelBuilder builder() {
        return new TableModelBuilder();
    }

    public TableModelBuilder columnNames(final List<String> theColumnNames) {
        this.columnNames.addAll(theColumnNames);
        return this;
    }

    public TableModelBuilder row(final List<String> theColumnValues) {
        List<String> theValues = new ArrayList<>(theColumnValues);
        this.columnValues.add(theValues);
        return this;
    }

    public TableModel build() {
        int numRows    = this.columnValues.size();
        int numColumns = this.columnNames.size();

        String[] theColumnNames = this.columnNames.toArray(new String[numColumns]);
        String[][] theRowData = new String[numRows][numColumns];

        for (int i = 0; i < numRows; i++) {
            List<String> rowData = this.columnValues.get(i);
            for (int j = 0; j < numColumns; j++) {
                if (j < rowData.size()) {
                    theRowData[i][j] = rowData.get(j);
                }
            }
        }

        return new DefaultTableModel(theRowData, theColumnNames);
    }

}
