package dorel.simplejavareport.interactivetable;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class InteractiveTableModel extends AbstractTableModel {

    protected String[] columnNames;
    protected List<LineInterface> lData;
    private LineInterface emptyRow;

    public InteractiveTableModel(String[] columnNames, LineInterface emptyRow) {
        this.columnNames = new String[columnNames.length + 1];
        System.arraycopy(columnNames, 0, this.columnNames, 0, columnNames.length);
        this.columnNames[this.columnNames.length - 1] = "";
        lData = new ArrayList<>();
        this.emptyRow = emptyRow;
    }

    public void setData(List<LineInterface> lData) {
        this.lData = lData;
    }

    public List<LineInterface> getData() {
        return lData;
    }

    public int getHiddenColIndex() {
        return columnNames.length - 1;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != getHiddenColIndex();
    }

    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        LineInterface record = (LineInterface) lData.get(row);
        if (column < record.getColumnsNumber()) {
            return record.getValueAt(column);
        } else {
            return "";
        }
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        LineInterface record = (LineInterface) lData.get(row);
        if (column < record.getColumnsNumber()) {
            record.setValueAt(column, (String) value);
        } else {
            System.out.println("invalid index");
        }
        fireTableCellUpdated(row, column);
    }

    @Override
    public int getRowCount() {
        return lData.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    public boolean hasEmptyRow() {
        if (lData.isEmpty()) {
            return false;
        }
        LineInterface record = (LineInterface) lData.get(lData.size() - 1);
        return record.isEmpty();
    }
//

    public void addEmptyRow() {
        if (emptyRow != null) {
            lData.add(emptyRow.getClone());
            fireTableRowsInserted(
                    lData.size() - 1,
                    lData.size() - 1);
        }
    }

    public void addRow(LineInterface newRow) {
        lData.add(newRow);
        fireTableRowsInserted(
                lData.size() - 1,
                lData.size() - 1);
    }

    public void deleteRow(int index) {
        lData.remove(index);
        fireTableRowsDeleted(index, index);
    }

    public void clearData() {
        lData = new ArrayList<>();
    }

}
