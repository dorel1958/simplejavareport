package dorel.simplejavareport.dialogs;

import dorel.simplejavareport.designer.Designer;
import static dorel.simplejavareport.dialogs.JFontChooser.OK_OPTION;
import dorel.simplejavareport.tools.SursaInfo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class FontAndColorSetup extends JDialog implements ActionListener {

    private boolean MY_FONT = false;  // daca selectez fontul cu controalele mele sau cu fereastra JFontChooser
    public boolean result = false;
    private Color selectedColor;
    private Font selectedFont;
    private Designer designer;

    private String fontFamily;
    private int iFontAtrib;
    private int fontSize;

    JComboBox cboxFontFamily;
    JCheckBox cbBold;
    JCheckBox cbItalic;
    JComboBox cboxFontSize;
    JLabel viewSelectedFont;

    public Color getSelectedColor() {
        return selectedColor;
    }

    public Font getSelectedFont() {
        if(MY_FONT){
            return new Font(fontFamily, iFontAtrib, fontSize);
        } else {
        return selectedFont;
        }
    }

    public final void setTextFont(Font font) {
        fontFamily = font.getFamily();
        iFontAtrib = font.getStyle();
        fontSize = font.getSize();
    }

    public FontAndColorSetup(Designer designer) {
        super(designer, true);
        this.designer = designer;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setTitle("Default font and color");
        setLayout(new BorderLayout());
        setResizable(false);

        List<Component[]> lComponents = new ArrayList<>();
        Component[] linia;

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
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, panelFont, panelColor, panelFC);
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

    public void setValues() {
        Color col = designer.cPanel.reportContent.defaultColor;
        this.selectedColor = new Color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha());
        Font font = designer.cPanel.reportContent.defaultFont;
        this.selectedFont = new Font(font.getName(), font.getStyle(), font.getSize());
        //
        viewSelectedFont.setFont(selectedFont);
        viewSelectedFont.setForeground(selectedColor);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Set":
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
}
