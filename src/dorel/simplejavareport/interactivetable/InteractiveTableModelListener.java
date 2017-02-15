package dorel.simplejavareport.interactivetable;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

//In order to for JTable to move from left to right column,
//we added a InteractiveTableModelListener to listen for any updates done inside the cells: 
public class InteractiveTableModelListener implements TableModelListener {

    JTable table;

    public InteractiveTableModelListener(JTable table) {
        this.table = table;
    }

    @Override
    public void tableChanged(TableModelEvent evt) {
        if (evt.getType() == TableModelEvent.UPDATE) {
            int column = evt.getColumn();
            int row = evt.getFirstRow();
            //System.out.println("row: " + row + " column: " + column+" new value:"+table.getModel().getValueAt(row, column));
            table.setColumnSelectionInterval(column + 1, column + 1);
            table.setRowSelectionInterval(row, row);
        }
    }
}
