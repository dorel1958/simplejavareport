package dorel.simplejavareport.dialogs;

import dorel.simplejavareport.interactivetable.InteractiveForm;
import dorel.simplejavareport.interactivetable.LineClass;
import dorel.simplejavareport.interactivetable.LineInterface;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class EditData extends JDialog implements ActionListener {

    // Atentie stringurile se repeta aici in mai multe in switch-uri.
    public enum DataType {

        COMMON_DATA,
        COLUMN_NAMES,
        DETAIL_BAND_DATA
    }
    //
    public boolean result = false;
    private List<LineInterface> columnNames;
    private List<LineInterface> lData;
    private List<LineInterface> lCommon;
    private InteractiveForm iform;

    private DataType editedData;

    // <editor-fold defaultstate="collapsed" desc="Get Set ...">
    public void setCommon(Map<String, String> mCommon) {
        lCommon = new ArrayList<>();
        LineInterface lineI;
        String[] cv;
        Set chei = mCommon.keySet();
        Iterator li = chei.iterator();
        while (li.hasNext()) {
            String key = (String) li.next();
            cv = new String[2];
            cv[0] = key;
            cv[1] = mCommon.get(key);
            lineI = new LineClass(cv);
            lCommon.add(lineI);
        }
    }

    public Map<String, String> getCommon() {
        Map mCommon = new HashMap<>();
        for (int i = 0; i < lCommon.size(); i++) {
            mCommon.put(lCommon.get(i).getValueAt(0), lCommon.get(i).getValueAt(1));
        }
        return mCommon;
    }

    public void setData(List<String[]> lData) {
        this.lData = new ArrayList<>();
        LineInterface lineI;
        for (int i = 0; i < lData.size(); i++) {
            String[] line = lData.get(i);
            String[] lLine = new String[line.length];
            System.arraycopy(line, 0, lLine, 0, line.length);
            lineI = new LineClass(lLine);
            this.lData.add(lineI);
        }
    }

    public List<String[]> getData() {
        List<String[]> xData = new ArrayList<>();
        for (LineInterface lineI : lData) {
            xData.add(lineI.getStringArray());
        }
        return xData;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = new ArrayList<>();
        LineInterface lineI;
        for (String columnName : columnNames) {
            String[] line = new String[1];
            line[0] = columnName;
            lineI = new LineClass(line);
            this.columnNames.add(lineI);
        }
    }

    public String[] getColumnNames() {
        String[] nColumnNames = new String[columnNames.size()];
        int contor = 0;
        for (LineInterface lineI : columnNames) {
            nColumnNames[contor] = lineI.getValueAt(0);
            contor+=1;
        }
        return nColumnNames;
    }
//</editor-fold>

    public EditData(Frame frame) {
        super(frame, true);
    }

    public void initComponents(DataType editedData) {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.editedData = editedData;

        switch (this.editedData) {
            case COMMON_DATA:
                this.setTitle("Edit mCommon");
                String[] colNames = {"Key", "Value"};
                iform = new InteractiveForm(colNames, new LineClass(2));
                this.add(iform, BorderLayout.CENTER);
                iform.tableModel.clearData();
                iform.tableModel.setData(lCommon);
                break;
            case COLUMN_NAMES:
                this.setTitle("Edit columnNames");
                String[] colNamescn = {"Column name"};
                iform = new InteractiveForm(colNamescn, null);
                this.add(iform, BorderLayout.CENTER);
                iform.tableModel.clearData();
                iform.tableModel.setData(columnNames);
                break;
            case DETAIL_BAND_DATA:
                String[] cNames=getColumnNames();
                iform = new InteractiveForm(cNames, new LineClass(columnNames.size()));
                this.add(iform, BorderLayout.CENTER);
                iform.tableModel.clearData();
                iform.tableModel.setData(lData);
                break;
        }

        List<JButton> lButoane = new ArrayList<>();
        if (this.editedData != DataType.COLUMN_NAMES) {
            JButton butAdd = new JButton("Add Line");
            butAdd.addActionListener(this);
            lButoane.add(butAdd);
            JButton butDel = new JButton("Delete Line");
            butDel.addActionListener(this);
            lButoane.add(butDel);
        }
        JButton butSet = new JButton("Accept");
        butSet.addActionListener(this);
        lButoane.add(butSet);
        JButton butCancel = new JButton("Cancel");
        butCancel.addActionListener(this);
        lButoane.add(butCancel);
        add(PanelFactory.createHorizontalButtonsRow(lButoane), BorderLayout.SOUTH);

        this.pack();

        // <editor-fold defaultstate="collapsed" desc="Center in Screen">      
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = 0;
        if (dim.width > w) {
            x = (dim.width - w) / 2;
        }
        int y = 0;
        if (dim.height > h) {
            y = (dim.height - h) / 2;
        }
        // Move the window
        this.setLocation(x, y);
        //</editor-fold>

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Add Line":
                switch (editedData) {
                    case COMMON_DATA:
                        iform.tableModel.addEmptyRow();
                        break;
                    case DETAIL_BAND_DATA:
                        iform.tableModel.addEmptyRow();
                        break;
                }
                break;
            case "Delete Line":
                int seleRow = iform.table.getSelectedRow();
                if (seleRow >= 0) {
                    iform.tableModel.deleteRow(seleRow);
                }
                break;
            case "Accept":
                iform.updateLastEdited();
                boolean unique = true;
                switch (editedData) {
                    case COMMON_DATA:
                        //test unique key
                        for (int i = 0; i < lCommon.size() - 1; i++) {
                            for (int j = i + 1; j < lCommon.size(); j++) {
                                if (lCommon.get(i).getValueAt(0).equals(lCommon.get(j).getValueAt(0))) {
                                    unique = false;
                                    break;
                                }
                            }
                        }
                        if (!unique) {
                            JOptionPane.showMessageDialog(this, "mCommon keys must be unique.");
                            return;
                        }
                        break;
                    case COLUMN_NAMES:
                        //test unique
                        for (int i = 0; i < columnNames.size() - 1; i++) {
                            for (int j = i + 1; j < columnNames.size(); j++) {
                                if (columnNames.get(i).getValueAt(0).equals(columnNames.get(j).getValueAt(0))) {
                                    unique = false;
                                    break;
                                }
                            }
                        }
                        if (!unique) {
                            JOptionPane.showMessageDialog(this, "columnNames must be unique.");
                            return;
                        }
                        break;
                    case DETAIL_BAND_DATA:
                        // 
                        break;
                }
                result = true;
                this.setVisible(false);
                break;
            case "Cancel":
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necunoscuta:" + ae.getActionCommand());
                break;
        }
    }
}
