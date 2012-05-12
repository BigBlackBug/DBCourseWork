/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcoursework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author BigBlackBug
 */
public class DBTableModel extends AbstractTableModel {

    protected ArrayList<String[]> data;
    protected boolean editable;
    protected HashMap<Integer, Boolean> editableColumns = new HashMap<Integer, Boolean>();
    protected String[] columnNames;
    protected int colCount;

    protected DBTableModel() {
    }
    

    public DBTableModel(List<Map<String, Object>> data, boolean editable) {
        this.data = new ArrayList<String[]>();
        this.editable = editable;
        init(data);
     }
    protected final void init(List<Map<String, Object>> data){
        extractColumnNames(data);
        extractData(data);
        if (editable) {
            editableColumns.put(0, false);
            for (int i = 1; i < colCount; i++) {
                editableColumns.put(i, editable);
            }
        }
    }

    public DBTableModel(List<Map<String, Object>> data, boolean editable, TableModelListener listener) {
        this(data, editable);
        addTableModelListener(listener);
    }

    private void extractData(List<Map<String, Object>> data) {
        int i = 0;
        int j = 0;
        for (Map<String, Object> line : data) {
            //
            this.data.add(new String[colCount]);

            for (String key : line.keySet()) {
                this.data.get(i)[j++] = line.get(key).toString();
            }
            i++;
            j = 0;
        }
    }

    private void extractColumnNames(List<Map<String, Object>> data) {
        int i = 0;
        if (data.isEmpty()) {
            colCount = 0;
            columnNames = new String[0];
        } else {
            Map<String, Object> get = data.get(0);
            colCount = get.size();
            columnNames = new String[colCount];

            for (String col : get.keySet()) {
                columnNames[i++] = col;
                // editableColumns.
            }
            // colCount = get.size();
        }
    }

    public ArrayList<String[]> getData() {
        return data;
    }

    public String[] getLine(int index) {
        return data.get(index);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getColumnCount() {
        return colCount;
    }

    public void addRow(String[] row) {
        data.add(row);
        fireTableDataChanged();
    }
    public void remove(int index){
        data.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void setData(ArrayList<String[]> data) {
        this.data = data;
        fireTableDataChanged();

    }

    public void setData(List<Map<String, Object>> data) {
        clear();
        extractColumnNames(data);
        extractData(data);
        fireTableStructureChanged();
        fireTableDataChanged();
    }

    public void clear() {
        data = null;
        data = new ArrayList<String[]>();
        columnNames = null;

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    @Override
    public Class getColumnClass(int c) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        // return editable;
        if (editable) {
            return editableColumns.get(col);
        } else {
            return editable;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        //data.set(row)[col] = value;
        if (row >= data.size()) {
            for (int i = 0; i < row - data.size() + 1; i++) {
                data.add(new String[colCount]);
            }
        }

        data.get(row)[col] = (String) value;
        fireTableDataChanged();

    }
}
