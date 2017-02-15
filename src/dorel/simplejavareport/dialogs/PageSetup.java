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
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PageSetup extends JDialog implements ActionListener {

    public static double MM_PUNCTE_PRINT = 2.834645669; //dot/mm dpmm =72dpi
    public boolean result = false;
    private int orientation;
    // all values afisate, incarcate, salvate in mm, dar intern lucrez NUMAI in ug (unitati grafice)
    private double paper_width;
    private double paper_height;
    private double paper_marginLeft;
    private double paper_marginRight;
    private double paper_marginTop;
    private double paper_marginBottom;
    //
    JComboBox cbSize;
    JCheckBox cbCustom;
    JTextField tfWidth;
    JTextField tfHeight;
    JTextField tfTop;
    JTextField tfLeft;
    JTextField tfRight;
    JTextField tfBottom;
    JRadioButton rbPortret;
    JRadioButton rbLandscape;  // putin cam inutil, dar simetric

    public PageFormat getPageFormat() {
        PageFormat pf = new PageFormat();
        Paper paper = new Paper();
        paper.setSize(Calc.round(paper_width * MM_PUNCTE_PRINT, 1), Calc.round(paper_height * MM_PUNCTE_PRINT, 0));
        double imW = Calc.round((paper_width - paper_marginLeft - paper_marginRight) * MM_PUNCTE_PRINT, 10);
        double imH = Calc.round((paper_height - paper_marginTop - paper_marginBottom) * MM_PUNCTE_PRINT, 0);
        paper.setImageableArea(Calc.round(paper_marginLeft * MM_PUNCTE_PRINT, 0), Calc.round(paper_marginTop * MM_PUNCTE_PRINT, 0), imW, imH);
        pf.setPaper(paper);
        pf.setOrientation(orientation);
        return pf;
    }

    public void setPageFormat(PageFormat pf) {
        orientation = pf.getOrientation();
        Paper pap = pf.getPaper();
        paper_width = Calc.round(pap.getWidth() / MM_PUNCTE_PRINT, 0);
        paper_height = Calc.round(pap.getHeight() / MM_PUNCTE_PRINT, 0);
        paper_marginLeft = Calc.round(pap.getImageableX() / MM_PUNCTE_PRINT, 0);
        paper_marginRight = Calc.round((pap.getWidth() - pap.getImageableWidth() - pap.getImageableX()) / MM_PUNCTE_PRINT, 0);
        paper_marginTop = Calc.round(pap.getImageableY() / MM_PUNCTE_PRINT, 1);
        paper_marginBottom = Calc.round((pap.getHeight() - pap.getImageableHeight() - pap.getImageableY()) / MM_PUNCTE_PRINT, 0);
    }

    public PageSetup(Frame owner) {
        super(owner, true);
        //initComponents();
    }

    public void setValues() {
        if (paper_width == 210 && paper_height == 297) {
            cbSize.setSelectedIndex(0);
            cbCustom.setSelected(false);
            tfWidth.setEnabled(false);
            tfHeight.setEnabled(false);
        } else {
            if (paper_width == 297 && paper_height == 420) {
                cbSize.setSelectedIndex(1);
                cbCustom.setSelected(false);
                tfWidth.setEnabled(false);
                tfHeight.setEnabled(false);
            } else {
                if (paper_width == 216 && paper_height == 279) {
                    cbSize.setSelectedIndex(2);
                    cbCustom.setSelected(false);
                    tfWidth.setEnabled(false);
                    tfHeight.setEnabled(false);
                } else {
                    cbCustom.setSelected(true);
                    tfWidth.setEnabled(true);
                    tfHeight.setEnabled(true);
                    tfWidth.setText(String.valueOf(paper_width));
                    tfHeight.setText(String.valueOf(paper_height));
                }
            }
        }
        tfTop.setText(String.valueOf(paper_marginTop));
        tfLeft.setText(String.valueOf(paper_marginLeft));
        tfRight.setText(String.valueOf(paper_marginRight));
        tfBottom.setText(String.valueOf(paper_marginBottom));

        if (orientation == PageFormat.PORTRAIT) {
            rbPortret.setSelected(true);
        } else {
            rbLandscape.setSelected(true);
        }

    }

    public void initComponents() {
        setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        setTitle("Page Setup");
        setLayout(new BorderLayout());
        setResizable(false);

        List<Component[]> lComponents = new ArrayList<>();
        Component[] linia;

        // <editor-fold defaultstate="collapsed" desc="Paper">      
        List<Component[]> lPaper = new ArrayList<>();
        //
        linia = new Component[7];
        linia[0] = new JLabel("Paper");
        lPaper.add(linia);
        //
        linia = new Component[7];
        linia[0] = new JLabel("Size:");
        cbSize = new JComboBox();
        cbSize.addItem("A4 210 x 297 mm");
        cbSize.addItem("A3 297 x 420 mm");
        cbSize.addItem("Letter 216 x 279 mm");
        cbSize.addActionListener(this);
        linia[1] = cbSize;
        cbCustom = new JCheckBox("Custom");
        cbCustom.addActionListener(this);
        linia[2] = cbCustom;
        linia[3] = new JLabel("Width:");
        tfWidth = new JTextField(3);
        linia[4] = tfWidth;
        linia[5] = new JLabel("Height:");
        tfHeight = new JTextField(3);
        linia[6] = tfHeight;
        lPaper.add(linia);
        //
        JPanel panelPaper = PanelFactory.createComponentArray(lPaper);
        panelPaper.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelPaper;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Margins">      
        List<Component[]> lMargins = new ArrayList<>();
        //
        linia = new Component[5];
        linia[0] = new JLabel("Margins (mm)");
        lMargins.add(linia);
        //
        linia = new Component[5];
        linia[2] = new JLabel("Top:");
        tfTop = new JTextField(3);
        linia[3] = tfTop;
        lMargins.add(linia);
        //
        linia = new Component[5];
        linia[1] = new JLabel("Left:");
        tfLeft = new JTextField(3);
        linia[2] = tfLeft;
        linia[3] = new JLabel("Right:");
        tfRight = new JTextField(3);
        linia[4] = tfRight;
        lMargins.add(linia);
        //
        linia = new Component[5];
        linia[2] = new JLabel("Bottom:");
        tfBottom = new JTextField(3);
        linia[3] = tfBottom;
        lMargins.add(linia);
        //
        JPanel panelMargins = PanelFactory.createComponentArray(lMargins);
        panelMargins.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelMargins;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Orientation">      
        List<Component[]> lOrientation = new ArrayList<>();
        //
        linia = new Component[2];
        linia[0] = new JLabel("Orientation:");
        rbPortret = new JRadioButton("Portret");
        linia[1] = rbPortret;
        lOrientation.add(linia);
        //
        linia = new Component[2];
        rbLandscape = new JRadioButton("Landscape");
        linia[1] = rbLandscape;
        lOrientation.add(linia);

        JPanel panelOrientation = PanelFactory.createComponentArray(lOrientation);
        panelOrientation.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelOrientation;
        lComponents.add(linia);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbPortret);
        bg.add(rbLandscape);
        //</editor-fold>

        JPanel panel = PanelFactory.createComponentArray(lComponents);
        // set the same width
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, panelPaper, panelMargins, panelOrientation);
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
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "comboBoxChanged":
                switch (cbSize.getSelectedIndex()) {
                    case 0:
                        tfWidth.setText("210");
                        tfHeight.setText("297");
                        break;
                    case 1:
                        tfWidth.setText("297");
                        tfHeight.setText("420");
                        break;
                    case 2:
                        tfWidth.setText("216");
                        tfHeight.setText("279");
                        break;
                    default:
                        tfWidth.setText("210");
                        tfHeight.setText("297");
                }
                break;
            case "Custom":
                if (cbCustom.isSelected()) {
                    //JOptionPane.showMessageDialog(this, "Selected");
                    cbSize.setEnabled(false);
                    tfWidth.setEnabled(true);
                    tfHeight.setEnabled(true);
                } else {
                    //JOptionPane.showMessageDialog(this, "Not");
                    cbSize.setEnabled(true);
                    tfWidth.setEnabled(false);
                    tfHeight.setEnabled(false);
                    switch (cbSize.getSelectedIndex()) {
                        case 0:
                            tfWidth.setText("210");
                            tfHeight.setText("297");
                            break;
                        case 1:
                            tfWidth.setText("297");
                            tfHeight.setText("420");
                            break;
                        case 2:
                            tfWidth.setText("216");  //215.9
                            tfHeight.setText("279");  //279.4
                            break;
                        default:
                            cbSize.setSelectedIndex(0);
                            tfWidth.setText("210");
                            tfHeight.setText("297");
                    }
                }
                break;
            case "Set":
                String em = "";
                if (rbPortret.isSelected()) {
                    orientation = PageFormat.PORTRAIT;
                } else {
                    orientation = PageFormat.LANDSCAPE;
                }
                boolean corect;
                int pass = 0;
                try {
                    paper_width = Double.parseDouble(tfWidth.getText());
                    pass = 1;
                    paper_height = Double.parseDouble(tfHeight.getText());
                    pass = 2;
                    paper_marginTop = Double.parseDouble(tfTop.getText());
                    pass = 3;
                    paper_marginLeft = Double.parseDouble(tfLeft.getText());
                    pass = 4;
                    paper_marginRight = Double.parseDouble(tfRight.getText());
                    pass = 5;
                    paper_marginBottom = Double.parseDouble(tfBottom.getText());
                    corect = true;
                } catch (Exception ex) {
                    corect = false;
                    em = ex.getLocalizedMessage();
                }
                if (corect) {
                    result = true;
                    this.setVisible(false);
                } else {
                    String err_mes;
                    switch (pass) {
                        case 0:
                            err_mes = "Error paper width " + em;
                            break;
                        case 1:
                            err_mes = "Error paper height " + em;
                            break;
                        case 2:
                            err_mes = "Error margin top " + em;
                            break;
                        case 3:
                            err_mes = "Error margin left " + em;
                            break;
                        case 4:
                            err_mes = "Error margin right " + em;
                            break;
                        case 5:
                            err_mes = "Error margin bottom " + em;
                            break;
                        default:
                            err_mes = "Error dimensions " + em;
                    }
                    JOptionPane.showMessageDialog(this, err_mes);
                }
                break;
            case "Cancel":
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necunoscuta:" + e.getActionCommand());
                break;
        }
    }

}
