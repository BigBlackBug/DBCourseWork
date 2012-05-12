/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcoursework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import javax.swing.event.TableModelListener;

/**
 *
 * @author BigBlackBug
 */
public class JLstTableModel extends DBTableModel {

    public JLstTableModel(List<Map<String, Object>> data, boolean editable) {
        for (Map<String, Object> m : data) {
            m.put("_", " ");
        }
        this.data = new ArrayList<>();
        this.editable = editable;
        init(data);
    }

    public JLstTableModel(List<Map<String, Object>> data, boolean editable, TableModelListener listener) {
        this(data, editable);
        addTableModelListener(listener);
    }

    @Override
    public Class getColumnClass(int c) {
        if (c == colCount - 1) {
            return JScrollPane.class;
        }
        return super.getColumnClass(c);
    }
}
