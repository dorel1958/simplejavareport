package dorel.simplejavareport.interactivetable;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class InteractiveForm extends JPanel {  // implements FocusListener {

    // ultima coloana este "" - HiddenColumn - pt a lua linie noua la sageata dreapta in ultima coloana
    private final String[] columnNames;

    public JTable table;
    private JScrollPane scroller;
    public InteractiveTableModel tableModel;

    public InteractiveForm(String[] columnNames, LineInterface newLine) {
        // if newLine==null nu adauga newLine la tabel
        this.columnNames = columnNames;
        initComponent(newLine);
    }

    private void initComponent(LineInterface newLine) {
        table = new JTable();
        tableModel = new InteractiveTableModel(columnNames, newLine);
        tableModel.addTableModelListener(new InteractiveTableModelListener(table));
        table.setModel(tableModel);
        //Also, this line should be added after initializing our JTable:
        table.setSurrendersFocusOnKeystroke(true);
        //This line will initialize an empty row in our JTable for first time use:
        if (!tableModel.hasEmptyRow()) {
            tableModel.addEmptyRow();
        }

        scroller = new javax.swing.JScrollPane(table);
        table.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
        //We need to make HIDDEN_INDEX table column's width as small as possible and at the same time our table cell renderer could also be called when it receives the cell focus:
        TableColumn hidden = table.getColumnModel().getColumn(tableModel.getHiddenColIndex());
        hidden.setMinWidth(2);
        hidden.setPreferredWidth(2);
        hidden.setMaxWidth(2);
        hidden.setCellRenderer(new InteractiveRenderer(tableModel.getHiddenColIndex(), this));
        //HIDDEN_INDEX column will act as a dummy cell to intercept any focus it receives and in turn it will automatically add a new row below it, if it doesn't have one. 
        //NOTE: Setting column width to 1 doesn't call our plugged-in table cell renderer.
        setLayout(new BorderLayout());
        add(scroller, BorderLayout.CENTER);
    }

    public void highlightLastRow(int row) {
        //If we haven't reached the last row, JTable is forced to select the next row;
        //otherwise JTable is forced to select the newly-added empty row:
        int lastrow = tableModel.getRowCount();
        if (row == lastrow - 1) {
            table.setRowSelectionInterval(lastrow - 1, lastrow - 1);
        } else {
            table.setRowSelectionInterval(row + 1, row + 1);
        }
        table.setColumnSelectionInterval(0, 0);
    }

    public void updateLastEdited() {
        // apelata pentru a scrie ultima celula modificata din care nu am iesit in Model
        int editingRow = table.getEditingRow();
        int editingCol = table.getEditingColumn();
        table.editCellAt(0, 0);
        table.editCellAt(editingRow, editingCol);
    }
}
