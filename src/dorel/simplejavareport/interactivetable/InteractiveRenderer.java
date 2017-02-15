package dorel.simplejavareport.interactivetable;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

//The main trick to move from the upper row to the next row is found in InteractiveRenderer class:
class InteractiveRenderer extends DefaultTableCellRenderer {

    protected int interactiveColumn;
    protected InteractiveForm interactiveForm;

    public InteractiveRenderer(int interactiveColumn, InteractiveForm interactiveForm) {
        this.interactiveColumn = interactiveColumn;
        this.interactiveForm = interactiveForm;
    }

    //Take a closer look at getTableCellRendererComponent method.
    //If our cell focus reaches our desired table column index, which is HIDDEN_INDEX,
    //and the AudioRecord's title attribute is not "", we'll force the table model to add a new row: 
    @Override
    public Component getTableCellRendererComponent(
            JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (column == interactiveColumn && hasFocus) {
            if ((table.getModel().getRowCount() - 1) == row && !((InteractiveTableModel) table.getModel()).hasEmptyRow()) {
                ((InteractiveTableModel) table.getModel()).addEmptyRow();
            }
            interactiveForm.highlightLastRow(row);
        }
        return c;
    }
}
