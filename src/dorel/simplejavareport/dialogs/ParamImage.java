package dorel.simplejavareport.dialogs;

import dorel.simplejavareport.designer.Eti;
import dorel.simplejavareport.report.ReportContent;
import dorel.simplejavareport.report.components.RapImage;
import dorel.simplejavareport.tools.SursaInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ParamImage extends JDialog implements ActionListener {

    Eti eti;
    ReportContent reportContent;

    JRadioButton rbText;
    JRadioButton rbCommon;
    JRadioButton rbData;
    JTextField tfText;
    JComboBox cboxCommon;
    JComboBox cboxData;
    JComboBox cboxDrawMode;

    public boolean result = false;
    SursaInfo.Sursa sursaText;
    String text;
    RapImage.DrawMode drawMode;

    // <editor-fold defaultstate="collapsed" desc="Get Set ...">
    public SursaInfo.Sursa getSursaText() {
        return sursaText;
    }

    public String getText() {
        return text;
    }

    public RapImage.DrawMode getDrawMode() {
        return drawMode;
    }
    // </editor-fold>      

    public ParamImage(JFrame frame, Eti eti, ReportContent reportContent) {
        super(frame, true);
        this.eti = eti;
        this.reportContent = reportContent;
        this.drawMode = eti.getImageDrawMode();
        text = eti.getText();
        this.sursaText=eti.getSursaText();
        
        initComponents();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setTitle("Set image parameters");
        setLayout(new BorderLayout());
        setResizable(false);

        List<Component[]> lComponents = new ArrayList<>();
        Component[] linia;

        // <editor-fold defaultstate="collapsed" desc="Text source">      
        List<Component[]> lTextSource = new ArrayList<>();
        linia = new Component[3];
        rbText = new JRadioButton("Text");
        rbText.addActionListener(this);
        linia[1] = rbText;
        tfText = new JTextField(20);
        linia[2] = tfText;
        lTextSource.add(linia);
        //
        linia = new Component[3];
        linia[0] = new JLabel("Text source:");
        rbCommon = new JRadioButton("Common");
        rbCommon.addActionListener(this);
        linia[1] = rbCommon;
        cboxCommon = new JComboBox();
        Set chei = reportContent.mCommon.keySet();
        Iterator li = chei.iterator();
        while (li.hasNext()) {
            String key = (String) li.next();
            cboxCommon.addItem(key);
        }
        linia[2] = cboxCommon;
        lTextSource.add(linia);
        //
        linia = new Component[3];
        rbData = new JRadioButton("Data");
        rbData.addActionListener(this);
        linia[1] = rbData;
        cboxData = new JComboBox();
        for (String columnName : reportContent.columnNames) {
            cboxData.addItem(columnName);
        }
        linia[2] = cboxData;
        lTextSource.add(linia);
        //
        //
        ButtonGroup bgAlign = new ButtonGroup();
        bgAlign.add(rbText);
        bgAlign.add(rbCommon);
        bgAlign.add(rbData);
        //
        linia = new Component[1];
        JPanel panelTextSource = PanelFactory.createComponentArray(lTextSource);
        panelTextSource.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelTextSource;
        lComponents.add(linia);
        ((GroupLayout) panelTextSource.getLayout()).linkSize(SwingConstants.HORIZONTAL, tfText, cboxCommon, cboxData);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Draw mode">      
        List<Component[]> lDraw = new ArrayList<>();

        linia = new Component[2];
        linia[0] = new JLabel("If source and frame are different sizes:");
        cboxDrawMode = new JComboBox();
        for (String item : RapImage.getDrawModes()) {
            cboxDrawMode.addItem(item);
        }
        linia[1] = cboxDrawMode;
        lDraw.add(linia);
        //
        linia = new Component[1];
        JPanel panelDraw = PanelFactory.createComponentArray(lDraw);
        panelDraw.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelDraw;
        lComponents.add(linia);
        //</editor-fold>

        JPanel panel = PanelFactory.createComponentArray(lComponents);
        // set the same width
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, panelTextSource, panelDraw);
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

        pack();

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
            case "Text":
                tfText.setEnabled(true);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(false);
                break;
            case "Common":
                tfText.setEnabled(false);
                cboxCommon.setEnabled(true);
                cboxData.setEnabled(false);
                break;
            case "Data":
                tfText.setEnabled(false);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(true);
                break;
            case "Set":
                // sursaText
                if (rbText.isSelected()) {
                    sursaText = SursaInfo.Sursa.TEXT;
                    text = tfText.getText();
                }
                if (rbCommon.isSelected()) {
                    sursaText = SursaInfo.Sursa.COMMON;
                    text = (String) cboxCommon.getSelectedItem();
                }
                if (rbData.isSelected()) {
                    sursaText = SursaInfo.Sursa.DATA;
                    text = (String) cboxData.getSelectedItem();
                }

                drawMode = RapImage.stringToDrawMode((String) cboxDrawMode.getSelectedItem());

                this.result = true;
                this.setVisible(false);
                break;
            case "Cancel":
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown command:" + ae.getActionCommand());
        }
    }

    private void setValues() {
        switch (sursaText) {
            case TEXT:
                rbText.setSelected(true);
                tfText.setEnabled(true);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(false);
                tfText.setText(text);
                break;
            case COMMON:
                rbCommon.setSelected(true);
                tfText.setEnabled(false);
                cboxCommon.setEnabled(true);
                cboxData.setEnabled(false);
                cboxCommon.setSelectedItem(text);
                break;
            case DATA:
                rbData.setSelected(true);
                tfText.setEnabled(false);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(true);
                cboxData.setSelectedItem(text);
                break;
        }
        cboxDrawMode.setSelectedItem(RapImage.drawModeToString(drawMode));
    }
}
