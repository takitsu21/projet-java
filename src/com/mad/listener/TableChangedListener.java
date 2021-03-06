package com.mad.listener;

import com.mad.AbstractApplication;
import com.mad.util.Data;
import com.mad.util.Table;
import com.mad.util.XmlMethodType;
import com.mad.util.XmlWriter;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class TableChangedListener extends AbstractApplication implements TableModelListener {
    public TableChangedListener() {
    }

    public static void updateCell(String newVal, String numEtu, String courseId) {
        if (newVal.isEmpty()) {
            XmlWriter.deleteCourse(numEtu, courseId);
        } else {
            try {
                if (Double.parseDouble(newVal) <= 20.0 && Double.parseDouble(newVal) >= 0.0 && !XmlWriter.modifyCourse(numEtu, courseId, newVal)) {
                    XmlWriter.addCourse(numEtu, courseId, newVal);
                }
            } catch (NumberFormatException exc) {
                if ((newVal.equalsIgnoreCase("abi") || newVal.equalsIgnoreCase("abj")) && !XmlWriter.modifyCourse(numEtu, courseId, newVal.toUpperCase())) {
                    XmlWriter.addCourse(numEtu, courseId, newVal.toUpperCase());
                }
            }
        }

    }

    public void tableChanged(TableModelEvent e) {
        if (e.getType() == TableModelEvent.UPDATE) {
            int col = Table.table.getSelectedColumn();
            String oldValue = Data.dataArray[e.getFirstRow() + 1][e.getColumn()];
            String courseId = String.valueOf(Table.table.getModel().getColumnName(col)).split(" ")[0];
            String newVal = (String) Table.table.getModel().getValueAt(e.getFirstRow(), e.getColumn());
            String numEtu = (String) Table.table.getModel().getValueAt(e.getFirstRow(), 0);
            if (oldValue.equalsIgnoreCase(newVal)) {
                return;
            }
            this.insertAction(() -> updateCell(newVal, numEtu, courseId), "course", XmlMethodType.MODIFY, false, oldValue, numEtu, courseId);
            Table.table.getModel().removeTableModelListener(this);
            Table.table.getModel().addTableModelListener(this);
        }

    }
}
