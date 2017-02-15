package dorel.simplejavareport.dialogs;

import dorel.simplejavareport.tools.Calc;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class TestData extends JDialog implements ActionListener {

    public boolean result = false;
    private String[] columnNames;
    private List<String[]> lData;
    private Map<String, String> mCommon;
    JTextField tfNrCol;
    JButton butColNames;
    JButton butDetData;

    // <editor-fold defaultstate="collapsed" desc="Get Set ...">
    public void setColumnNames(String[] columnNames) {
        this.columnNames = new String[columnNames.length];
        System.arraycopy(columnNames, 0, this.columnNames, 0, columnNames.length);
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setCommon(Map<String, String> mCommon) {
        this.mCommon = new HashMap<>();
        Set chei = mCommon.keySet();
        Iterator li = chei.iterator();
        while (li.hasNext()) {
            String key = (String) li.next();
            this.mCommon.put(key, mCommon.get(key));
        }
    }

    public Map<String, String> getCommon() {
        return mCommon;
    }

    public void setData(List<String[]> lData) {
        this.lData = new ArrayList<>();
        for (int i = 0; i < lData.size(); i++) {
            String[] line = lData.get(i);
            String[] lLine = new String[line.length];
            System.arraycopy(line, 0, lLine, 0, line.length);
            this.lData.add(lLine);
        }
    }

    public List<String[]> getData() {
        return lData;
    }

    public void setValues(Map<String, String> mCommon, String[] columnNames, List<String[]> lData) {
        this.setColumnNames(columnNames);
        this.lData = lData;
        this.mCommon = mCommon;
    }
    //</editor-fold>

    public TestData(Frame frame) {
        super(frame, true);
    }

    public void setValues() {
        tfNrCol.setText(String.valueOf(columnNames.length));
        butColNames.setEnabled(columnNames.length > 0);
        butDetData.setEnabled(columnNames.length > 0);
    }

    public void initComponents() {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setTitle("Test Data");
        setLayout(new BorderLayout());
        setResizable(false);

        List<Component[]> lComponents = new ArrayList<>();
        Component[] linia;

        // <editor-fold defaultstate="collapsed" desc="Common Data">      
        List<Component[]> lCommon = new ArrayList<>();
        linia = new Component[4];
        JButton butCommon = new JButton("Edit common data");
        butCommon.addActionListener(this);
        linia[2] = butCommon;
        lCommon.add(linia);
        //
        JPanel panelCommon = PanelFactory.createComponentArray(lCommon);
        panelCommon.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelCommon;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Columns">      
        List<Component[]> lColumns = new ArrayList<>();
        linia = new Component[4];
        linia[0] = new JLabel("Columns");
        tfNrCol = new JTextField(3);
        linia[1] = tfNrCol;
        JButton butSetCol = new JButton("Set columns number");
        butSetCol.addActionListener(this);
        linia[2] = butSetCol;
        butColNames = new JButton("Edit column names");
        butColNames.addActionListener(this);
        linia[3] = butColNames;
        lColumns.add(linia);
        //
        JPanel panelColumns = PanelFactory.createComponentArray(lColumns);
        panelColumns.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelColumns;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Detail Data">      
        List<Component[]> lDetail = new ArrayList<>();
        linia = new Component[4];
        butDetData = new JButton("Edit detail data");
        butDetData.addActionListener(this);
        linia[2] = butDetData;
        lDetail.add(linia);
        //
        JPanel panelDetail = PanelFactory.createComponentArray(lDetail);
        panelDetail.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelDetail;
        lComponents.add(linia);
        //</editor-fold>

        JPanel panel = PanelFactory.createComponentArray(lComponents);
        // set the same width
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, panelCommon, panelColumns, panelDetail);
        add(panel, BorderLayout.CENTER);

        setValues();

        // <editor-fold defaultstate="collapsed" desc="Buttons">      
        List<JButton> lButoane = new ArrayList<>();
        JButton butSet = new JButton("Set");
        butSet.addActionListener(this);
        lButoane.add(butSet);
        JButton butCancel = new JButton("Cancel");
        butCancel.addActionListener(this);
        lButoane.add(butCancel);
        add(PanelFactory.createHorizontalButtonsRow(lButoane), BorderLayout.SOUTH);
        //</editor-fold>

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
            case "Edit common data":
                //JOptionPane.showMessageDialog(this, "Common data");
                EditData edc = new EditData(null);
                edc.setCommon(mCommon);
                edc.initComponents(EditData.DataType.COMMON_DATA);
                if (edc.result) {
                    this.mCommon = edc.getCommon();
                    String a = "";
                }
                break;
            case "Edit column names":
                EditData edn = new EditData(null);
                //edn.setData(lData);
                edn.setColumnNames(columnNames);
//                List<String[]> xData=new ArrayList<>();
//                for( String str:columnNames){
//                    String[] lin=new String[1];
//                    lin[0]=str;
//                    xData.add(lin);
//                }
//                edn.setData(xData);
                //edn.setColumnNames(columnNames);
                edn.initComponents(EditData.DataType.COLUMN_NAMES);
                if (edn.result) {
                    this.columnNames = edn.getColumnNames();
                }
                break;
            case "Edit detail data":
                EditData edd = new EditData(null);
                edd.setData(lData);
                edd.setColumnNames(columnNames);
                edd.initComponents(EditData.DataType.DETAIL_BAND_DATA);
                if (edd.result) {
                    this.lData = edd.getData();
                }
                break;
            case "Set columns number":
                String nrCol = tfNrCol.getText();
                int nCol;
                if (Calc.isInteger(nrCol)) {
                    nCol = Integer.parseInt(nrCol);
                    if (nCol == this.columnNames.length) {
                        // not changed;
                    } else {
                        if (nCol > this.columnNames.length) {
                            // add columns
                            String[] xColumnNames = new String[nCol];
                            System.arraycopy(columnNames, 0, xColumnNames, 0, columnNames.length);
                            // add new
                            for (int j = columnNames.length; j < xColumnNames.length; j++) {
                                xColumnNames[j] = "newColumn_" + String.valueOf(j);
                            }
                            this.columnNames = xColumnNames;
                            //
                            List<String[]> xlData = new ArrayList<>();
                            for (int i = 0; i < lData.size(); i++) {
                                String[] oldLine = lData.get(i);
                                String[] newLine = new String[nCol];
                                System.arraycopy(oldLine, 0, newLine, 0, oldLine.length);
                                // add new
                                for (int j = oldLine.length; j < newLine.length; j++) {
                                    newLine[j] = "newValue_" + String.valueOf(i) + "_" + String.valueOf(j);
                                }
                                xlData.add(newLine);
                            }
                            lData = xlData;
                        } else {
                            // delete columns
                            String[] xColumnNames = new String[nCol];
                            System.arraycopy(columnNames, 0, xColumnNames, 0, nCol);
                            this.columnNames = xColumnNames;
                            //
                            List<String[]> xlData = new ArrayList<>();
                            for (int i = 0; i < lData.size(); i++) {
                                String[] oldLine = lData.get(i);
                                String[] newLine = new String[nCol];
                                System.arraycopy(oldLine, 0, newLine, 0, nCol);
                                xlData.add(newLine);
                            }
                            lData = xlData;
                        }
                    }
                    butColNames.setEnabled(columnNames.length > 0);
                    butDetData.setEnabled(columnNames.length > 0);
                } else {
                    JOptionPane.showMessageDialog(this, "Columns is not an integer.");
                }
                break;
            case "Set":
                result = true;
                this.setVisible(false);
                break;
            case "Cancel":
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown command:" + ae.getActionCommand());
        }
    }
}
