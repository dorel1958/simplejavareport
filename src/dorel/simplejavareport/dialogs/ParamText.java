package dorel.simplejavareport.dialogs;

import dorel.simplejavareport.designer.Eti;
import static dorel.simplejavareport.dialogs.JFontChooser.OK_OPTION;
import dorel.simplejavareport.report.ReportContent;
import dorel.simplejavareport.tools.SursaInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ParamText extends JDialog implements ActionListener {

    private boolean MY_FONT = false;  // daca selectez fontul cu controalele mele sau cu fereastra JFontChooser
    Font selectedFont;
    Color selectedColor;
    JLabel viewSelectedFont;
    JRadioButton rbText;
    JRadioButton rbCommon;
    JRadioButton rbData;
    JRadioButton rbReport;
    JTextField tfText;
    JComboBox cboxCommon;
    JComboBox cboxData;
    JComboBox cboxReport;
    JCheckBox cbNumeric;
    JComboBox cboxFontFamily;
    JCheckBox cbBold;
    JCheckBox cbItalic;
    JComboBox cboxFontSize;
    JRadioButton rbHleft;
    JRadioButton rbHcenter;
    JRadioButton rbHright;
    JRadioButton rbVtop;
    JRadioButton rbVcenter;
    JRadioButton rbVbottom;
    //
    public boolean result = false;
    private ReportContent reportContent;
    Eti eti;
    private SursaInfo.Sursa sursaText;
    private String text;
    private boolean numeric;
    private String fontFamily;
    private int iFontAtrib;
    private int fontSize;
    //
    private int hAlign;
    private int vAlign;

    // <editor-fold defaultstate="collapsed" desc="Get Set ...">      
    public void setHAlign(int hAlign) {
        this.hAlign = hAlign;
    }

    public void setVAlign(int vAlign) {
        this.vAlign = vAlign;
    }

    public int getHAlign() {
        return hAlign;
    }

    public int getVAlign() {
        return vAlign;
    }

    public Font getTextFont() {
        Font font = new Font(fontFamily, iFontAtrib, fontSize);
        return font;
    }

    public final void setTextFont(Font font) {
        fontFamily = font.getFamily();
        iFontAtrib = font.getStyle();
        fontSize = font.getSize();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isNumeric() {
        return numeric;
    }

    public void setNumeric(boolean numeric) {
        this.numeric = numeric;
    }

    public SursaInfo.Sursa getSursaText() {
        return sursaText;
    }

    public void setSursaText(SursaInfo.Sursa sursaText) {
        this.sursaText = sursaText;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
    //</editor-fold>

    public ParamText(JFrame frame, Eti eti, ReportContent reportContent) {
        super(frame, true);
        this.eti = eti;
        sursaText = eti.getSursaText();
        text = eti.getText();
        numeric = eti.isTextNumeric();
        setTextFont(eti.getFont());
        hAlign = eti.getHorizontalAlignment();
        vAlign = eti.getVerticalAlignment();
        this.reportContent = reportContent;
        selectedFont = this.eti.getFont();
        selectedColor = this.eti.getForeground();
        initComponents();
    }

    private void setValues() {
        setSursaTextValue();
        if (numeric) {
            cbNumeric.setSelected(true);
        } else {
            cbNumeric.setSelected(false);
        }
        if (MY_FONT) {
            setFontValue();
        }
        setAlignValue();
        viewSelectedFont.setFont(selectedFont);
        viewSelectedFont.setForeground(selectedColor);
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setTitle("Set text parameters");
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
        linia = new Component[3];
        rbReport = new JRadioButton("Report");
        rbReport.addActionListener(this);
        linia[1] = rbReport;
        cboxReport = new JComboBox();
        cboxReport.addItem("report_LineNumber");
        cboxReport.addItem("report_PageNumber");
        cboxReport.addItem("report_NPages");
        linia[2] = cboxReport;
        lTextSource.add(linia);
        //
        ButtonGroup bgAlign = new ButtonGroup();
        bgAlign.add(rbText);
        bgAlign.add(rbCommon);
        bgAlign.add(rbData);
        bgAlign.add(rbReport);
        //
        linia = new Component[1];
        JPanel panelTextSource = PanelFactory.createComponentArray(lTextSource);
        panelTextSource.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelTextSource;
        lComponents.add(linia);
        ((GroupLayout) panelTextSource.getLayout()).linkSize(SwingConstants.HORIZONTAL, tfText, cboxCommon, cboxReport, cboxData);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Numeric">      
        List<Component[]> lNumeric = new ArrayList<>();
        linia = new Component[1];
        cbNumeric = new JCheckBox("Numeric value");
        linia[0] = cbNumeric;
        lNumeric.add(linia);
        //
        JPanel panelNumeric = PanelFactory.createComponentArray(lNumeric);
        panelNumeric.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelNumeric;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Font">      
        List<Component[]> lFont = new ArrayList<>();
        if (MY_FONT) {
            linia = new Component[6];
            linia[0] = new JLabel("Font");
            cboxFontFamily = new JComboBox();
            cboxFontFamily.addItem("Dialog");
            cboxFontFamily.addItem("DialogInput");
            cboxFontFamily.addItem("Monospaced");
            cboxFontFamily.addItem("Serif");
            cboxFontFamily.addItem("SansSerif");
            linia[1] = cboxFontFamily;
            cbBold = new JCheckBox("Bold");
            linia[2] = cbBold;
            cbItalic = new JCheckBox("Italic");
            linia[3] = cbItalic;
            linia[4] = new JLabel("Size");
            cboxFontSize = new JComboBox();
            cboxFontSize.addItem("8");
            cboxFontSize.addItem("9");
            cboxFontSize.addItem("10");
            cboxFontSize.addItem("11");
            cboxFontSize.addItem("12");
            cboxFontSize.addItem("13");
            cboxFontSize.addItem("14");
            cboxFontSize.addItem("16");
            cboxFontSize.addItem("18");
            cboxFontSize.addItem("20");
            cboxFontSize.addItem("24");
            cboxFontSize.addItem("26");
            cboxFontSize.addItem("28");
            cboxFontSize.addItem("32");
            cboxFontSize.addItem("36");
            cboxFontSize.addItem("40");
            cboxFontSize.addItem("48");
            cboxFontSize.addItem("56");
            cboxFontSize.addItem("64");
            cboxFontSize.addItem("72");
            linia[5] = cboxFontSize;
            lFont.add(linia);
        } else {
            linia = new Component[1];
            JButton fontChooser = new JButton("Select font");
            fontChooser.addActionListener(this);
            linia[0] = fontChooser;
            lFont.add(linia);
        }
        //
        linia = new Component[1];
        JPanel panelFont = PanelFactory.createComponentArray(lFont);
        panelFont.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelFont;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Alignment">      
        List<Component[]> lHAlign = new ArrayList<>();
        linia = new Component[4];
        rbHleft = new JRadioButton("Left");
        rbHcenter = new JRadioButton("Center");
        rbHright = new JRadioButton("Right");
        linia[0] = new JLabel("Horizontal alignment");
        linia[1] = rbHleft;
        linia[2] = rbHcenter;
        linia[3] = rbHright;
        lHAlign.add(linia);

        ButtonGroup bgHAlign = new ButtonGroup();
        bgHAlign.add(rbHleft);
        bgHAlign.add(rbHcenter);
        bgHAlign.add(rbHright);
        //
        linia = new Component[1];
        JPanel panelHAlign = PanelFactory.createComponentArray(lHAlign);
        panelHAlign.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelHAlign;
        lComponents.add(linia);

        List<Component[]> lVAlign = new ArrayList<>();
        rbVtop = new JRadioButton("Top");
        rbVcenter = new JRadioButton("Center");
        rbVbottom = new JRadioButton("Bottom");

        linia = new Component[2];
        linia[1] = rbVtop;
        lVAlign.add(linia);

        linia = new Component[2];
        linia[0] = new JLabel("Vertical alignment");
        linia[1] = rbVcenter;
        lVAlign.add(linia);

        linia = new Component[2];
        linia[1] = rbVbottom;
        lVAlign.add(linia);

        ButtonGroup bgVAlign = new ButtonGroup();
        bgVAlign.add(rbVtop);
        bgVAlign.add(rbVcenter);
        bgVAlign.add(rbVbottom);
        //
        linia = new Component[1];
        JPanel panelVAlign = PanelFactory.createComponentArray(lVAlign);
        panelVAlign.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelVAlign;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Color">      
        List<Component[]> lColor = new ArrayList<>();
        linia = new Component[1];
        JButton fontChooser = new JButton("Select Color");
        fontChooser.addActionListener(this);
        linia[0] = fontChooser;
        lColor.add(linia);
        //
        linia = new Component[1];
        JPanel panelColor = PanelFactory.createComponentArray(lColor);
        panelColor.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelColor;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="View Font and color">      
        List<Component[]> lFontColor = new ArrayList<>();
        linia = new Component[1];
        viewSelectedFont = new JLabel("Selected font example");
        viewSelectedFont.setBackground(Color.white);
        viewSelectedFont.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        viewSelectedFont.setOpaque(true);
        linia[0] = viewSelectedFont;
        lFontColor.add(linia);
        //
        linia = new Component[1];
        JPanel panelFC = PanelFactory.createComponentArray(lFontColor);
        panelFC.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelFC;
        lComponents.add(linia);
        //</editor-fold>

        JPanel panel = PanelFactory.createComponentArray(lComponents);
        // set the same width
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, panelTextSource, panelNumeric, panelFont, panelHAlign, panelVAlign, panelColor, panelFC);
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
                cboxReport.setEnabled(false);
                break;
            case "Common":
                tfText.setEnabled(false);
                cboxCommon.setEnabled(true);
                cboxData.setEnabled(false);
                cboxReport.setEnabled(false);
                break;
            case "Data":
                tfText.setEnabled(false);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(true);
                cboxReport.setEnabled(false);
                break;
            case "Report":
                tfText.setEnabled(false);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(false);
                cboxReport.setEnabled(true);
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
                if (rbReport.isSelected()) {
                    sursaText = SursaInfo.Sursa.REPORT;
                    text = (String) cboxReport.getSelectedItem();
                }
                /// de pus si restul...
                // numeric
                if (cbNumeric.isSelected()) {
                    numeric = true;
                } else {
                    numeric = false;
                }
                // Font
                if (MY_FONT) {
                    fontFamily = (String) cboxFontFamily.getSelectedItem();
                    iFontAtrib = 0;
                    if (cbBold.isSelected()) {
                        iFontAtrib += Font.BOLD;
                    }
                    if (cbItalic.isSelected()) {
                        iFontAtrib += Font.ITALIC;
                    }
                    fontSize = Integer.parseInt((String) cboxFontSize.getSelectedItem());
                } else {
                    fontFamily = selectedFont.getFamily();
                    iFontAtrib = selectedFont.getStyle();
                    fontSize = selectedFont.getSize();
                }
                // Alignment
                if (rbHleft.isSelected()) {
                    hAlign = JLabel.LEFT;
                }
                if (rbHcenter.isSelected()) {
                    hAlign = JLabel.CENTER;
                }
                if (rbHright.isSelected()) {
                    hAlign = JLabel.RIGHT;
                }
                //
                if (rbVtop.isSelected()) {
                    vAlign = JLabel.TOP;
                }
                if (rbVcenter.isSelected()) {
                    vAlign = JLabel.CENTER;
                }
                if (rbVbottom.isSelected()) {
                    vAlign = JLabel.BOTTOM;
                }
                this.result = true;
                this.setVisible(false);
                break;
            case "Select font":
                JFontChooser fd = new JFontChooser();
                fd.setSelectedFont(selectedFont);
                int raspuns = fd.showDialog(this);
                if (raspuns == OK_OPTION) {
                    selectedFont = fd.getSelectedFont();
                    viewSelectedFont.setFont(selectedFont);
                    pack();
                }
                break;
            case "Select Color":
                ColorChooser cc = new ColorChooser(this, selectedColor);
                if (cc.getResponse()) {
                    selectedColor = cc.getSelectedColor();
                    viewSelectedFont.setForeground(selectedColor);
                }
                break;
            case "Cancel":
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown command:" + ae.getActionCommand());
        }
    }

    private void setSursaTextValue() {
        switch (sursaText) {
            case TEXT:
                rbText.setSelected(true);
                tfText.setEnabled(true);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(false);
                cboxReport.setEnabled(false);
                tfText.setText(text);
                break;
            case COMMON:
                rbCommon.setSelected(true);
                tfText.setEnabled(false);
                cboxCommon.setEnabled(true);
                cboxData.setEnabled(false);
                cboxReport.setEnabled(false);
                cboxCommon.setSelectedItem(text);
                break;
            case DATA:
                rbData.setSelected(true);
                tfText.setEnabled(false);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(true);
                cboxReport.setEnabled(false);
                cboxData.setSelectedItem(text);
                break;
            case REPORT:
                rbReport.setSelected(true);
                tfText.setEnabled(false);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(false);
                cboxReport.setEnabled(true);
                cboxReport.setSelectedItem(text);
                break;
            default:
                rbText.setSelected(true);
                tfText.setEnabled(true);
                cboxCommon.setEnabled(false);
                cboxData.setEnabled(false);
                tfText.setText(text);
                break;
        }
    }

    private void setFontValue() {
        cboxFontFamily.setSelectedItem(fontFamily);
        switch (iFontAtrib) {
            case Font.PLAIN:  //0
                cbBold.setSelected(false);
                cbItalic.setSelected(false);
                break;
            case Font.BOLD:  //1
                cbBold.setSelected(true);
                cbItalic.setSelected(false);
                break;
            case Font.ITALIC:  //2
                cbBold.setSelected(false);
                cbItalic.setSelected(true);
                break;
            case Font.BOLD + Font.ITALIC:  //  3
                cbBold.setSelected(true);
                cbItalic.setSelected(true);
                break;
        }
        cboxFontSize.setSelectedItem(String.valueOf(fontSize));
    }

    private void setAlignValue() {
        if (hAlign == JLabel.LEFT) {  //2
            rbHleft.setSelected(true);
        }
        if (hAlign == JLabel.CENTER) {  //0
            rbHcenter.setSelected(true);
        }
        if (hAlign == JLabel.RIGHT) {  //4
            rbHright.setSelected(true);
        }
        //
        if (vAlign == JLabel.TOP) {  //1
            rbVtop.setSelected(true);
        }
        if (vAlign == JLabel.CENTER) {  //0
            rbVcenter.setSelected(true);
        }
        if (vAlign == JLabel.BOTTOM) {  //3
            rbVbottom.setSelected(true);
        }

    }
}
