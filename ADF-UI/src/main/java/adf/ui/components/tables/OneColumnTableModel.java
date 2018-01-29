package adf.ui.components.tables;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

public abstract class OneColumnTableModel<T> extends AbstractTableModel {

    private String[] columnNames = {""};
    private boolean editingAllowed = true;

    private final List<T> data = new ArrayList();

    public OneColumnTableModel(List data) {
        this(new String[]{""}, data);
    }

    public OneColumnTableModel(String[] columnNames, List data) {
        this.columnNames = columnNames;
        if (data == null) {
            return;
        }
        this.data.addAll(data);
    }

    public String getColumnName(int ind) {
        return columnNames[ind];
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }


    public void setData(List<T> data) {
        this.data.clear();
        if (data == null) {
            return;
        }
        this.data.addAll(data);
        fireTableDataChanged(); // this updates table column width

    }

    public List<T> getData(){
        return new ArrayList<T>(data);
    }

    public int getRowCount() {
        return data.size();
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public void appendRow(T obj) {
        System.out.println(" appendRow " + obj.getClass().getName());
        data.add(obj);
    }

    public void insertRow(T obj, int rowIndex) {

        if (getRowCount() == 0) {
            appendRow(obj);
            return;
        }

        if (rowIndex > -1 && rowIndex < getRowCount()) {
            data.add(rowIndex, obj);
        }

    }

    public void removeRow(int rowIndex) {
        data.remove(rowIndex);
    }

    public void appendRowAndFireEvent(T obj) {
        appendRow(obj);
        int rowIndex = this.getRowCount() - 1;
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void insertRowAndFireEvent(T obj, int rowIndex) {
        insertRow(obj, rowIndex);
        fireTableRowsInserted(rowIndex, rowIndex);
    }

    public void removeRowAndFireEvent(int rowIndex) {
        removeRow(rowIndex);
        this.fireTableRowsDeleted(rowIndex, rowIndex);
    }

    abstract public Object getValueAt(int rowIndex, int columnIndex);

    abstract public void setValueAt(Object value, int rowIndex, int columnIndex);


//    public Object getValueAt( int rowIndex, int columnIndex ){
//        if ( rowIndex > -1 && rowIndex < getRowCount() && columnIndex > -1 ) {
//           Object[] row = (Object[])mcln.data.get(rowIndex);
//           if ( columnIndex < row.length ){
//               return row[columnIndex];
//           }
//        }
//        return null;
//    }
//     
//    public void setValueAt( Object value, int rowIndex, int columnIndex ){
//        if ( rowIndex > -1 && rowIndex < getRowCount() && columnIndex > -1 ) {
//            Object[] row = (Object[])mcln.data.get(rowIndex);
//            if ( columnIndex < row.length ){
//                row[columnIndex] = value;
//            }
//         }
//    }


    public T getRow(int rowIndex) {
        if (rowIndex > -1 && rowIndex < getRowCount()) {
            return data.get(rowIndex);
        }
        return null;
    }

    public void setRow(T row, int rowIndex) {
        if (rowIndex > -1 && rowIndex < getRowCount()) {
            data.set(rowIndex, row);
        }
    }

    public boolean isEditingAllowed() {
        return editingAllowed;
    }

    public void setEditingAllowed(boolean editingAllowed) {
        this.editingAllowed = editingAllowed;

    }


}
