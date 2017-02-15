package dorel.simplejavareport.dialogs;

import dorel.simplejavareport.designer.LineEti;
import dorel.simplejavareport.designer.LineEti.LineTypes;
import dorel.simplejavareport.tools.Calc;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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

public final class ParamLine extends JDialog implements ActionListener {

    public boolean result = false;
    LineEti lineEti;
    JRadioButton rbHorizontal;
    JRadioButton rbVertical;
    JRadioButton rbDiagonal;
    JRadioButton rbRectangle;
    JComboBox cboxLineEnds;
    JComboBox cboxLineJoints;
    //
    private LineTypes lineType;
    JTextField tfWidth;
    JCheckBox cbFlip;
    private float lineWidth;
    private boolean flip;
    Color selectedColor;
    private int lineEnds;
    private int lineJoints;

    // <editor-fold defaultstate="collapsed" desc="Get Set ...">
    public void setLineJoints(int lineJoints) {
        if (lineJoints == BasicStroke.JOIN_BEVEL || lineJoints == BasicStroke.JOIN_MITER || lineJoints == BasicStroke.JOIN_ROUND) {
            this.lineJoints = lineJoints;
        } else {
            this.lineJoints = BasicStroke.JOIN_MITER;
        }
    }

    public int getLineJoints() {
        return lineJoints;
    }

    public void setLineEnds(int lineEnds) {
        if (lineEnds == BasicStroke.CAP_SQUARE || lineEnds == BasicStroke.CAP_BUTT || lineEnds == BasicStroke.CAP_ROUND) {
            this.lineEnds = lineEnds;
        } else {
            this.lineEnds = BasicStroke.CAP_SQUARE;
        }
    }

    public int getLineEnds() {
        return lineEnds;
    }

    public void setLineType(LineTypes lineType) {
        this.lineType = lineType;
    }

    public LineTypes getLineType() {
        return lineType;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
    // </editor-fold>      

    public ParamLine(JFrame frame, LineEti lineEti) {
        super(frame, true);
        this.lineEti = lineEti;
        this.setLineType(lineEti.getLineType());
        this.setLineWidth(lineEti.getLineWidth());
        this.setFlip(lineEti.isFlip());
        this.selectedColor = lineEti.getForeground();
        lineEnds = lineEti.getLineEnds();
        lineJoints = lineEti.getLineJoints();

        initComponents();
    }

    private void initComponents() {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setTitle("Set line parameters");
        this.setLayout(new BorderLayout());
        setResizable(false);

        List<Component[]> lComponents = new ArrayList<>();
        Component[] linia;

        // <editor-fold defaultstate="collapsed" desc="Line Type">      
        List<Component[]> lType = new ArrayList<>();

        linia = new Component[4];
        rbHorizontal = new JRadioButton("Horizontal");
        rbHorizontal.addActionListener(this);
        linia[1] = rbHorizontal;
        lType.add(linia);

        linia = new Component[4];
        linia[0] = new JLabel("Line type");
        rbVertical = new JRadioButton("Vertical");
        rbVertical.addActionListener(this);
        linia[1] = rbVertical;
        cboxLineEnds = new JComboBox();
        cboxLineEnds.addItem("CAP_SQUARE");
        cboxLineEnds.addItem("CAP_ROUND");
        cboxLineEnds.addItem("CAP_BUTT");
        linia[3] = cboxLineEnds;
        lType.add(linia);

        linia = new Component[4];
        rbDiagonal = new JRadioButton("Diagonal");
        rbDiagonal.addActionListener(this);
        linia[1] = rbDiagonal;
        cbFlip = new JCheckBox("Flip");
        linia[2] = cbFlip;
        lType.add(linia);

        linia = new Component[4];
        rbRectangle = new JRadioButton("Rectangle");
        rbRectangle.addActionListener(this);
        linia[1] = rbRectangle;
        cboxLineJoints = new JComboBox();
        cboxLineJoints.addItem("JOIN_MITER");
        cboxLineJoints.addItem("JOIN_ROUND");
        cboxLineJoints.addItem("JOIN_BEVEL");
        linia[3] = cboxLineJoints;
        lType.add(linia);
        //
        JPanel panelType = PanelFactory.createComponentArray(lType);
        panelType.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelType;
        lComponents.add(linia);

        ButtonGroup bgAlign = new ButtonGroup();
        bgAlign.add(rbHorizontal);
        bgAlign.add(rbVertical);
        bgAlign.add(rbDiagonal);
        bgAlign.add(rbRectangle);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Width">      
        List<Component[]> lWidth = new ArrayList<>();

        linia = new Component[2];
        linia[0] = new JLabel("Line width:");
        tfWidth = new JTextField(4);
        linia[1] = tfWidth;
        lWidth.add(linia);

        JPanel panelWidth = PanelFactory.createComponentArray(lWidth);
        panelWidth.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelWidth;
        lComponents.add(linia);
        //</editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Color">      
        List<Component[]> lColor = new ArrayList<>();

        linia = new Component[1];
        JButton fontChooser = new JButton("Select Color");
        fontChooser.addActionListener(this);
        linia[0] = fontChooser;
        lColor.add(linia);

        JPanel panelColor = PanelFactory.createComponentArray(lColor);
        panelColor.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia = new Component[1];
        linia[0] = panelColor;
        lComponents.add(linia);
        //</editor-fold>
        //

        JPanel panel = PanelFactory.createComponentArray(lComponents);
        // set the same width
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, panelType, panelWidth, panelColor);
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
            case "Horizontal":
                cbFlip.setEnabled(false);
                cboxLineEnds.setEnabled(true);
                cboxLineJoints.setEnabled(false);
                break;
            case "Vertical":
                cbFlip.setEnabled(false);
                cboxLineEnds.setEnabled(true);
                cboxLineJoints.setEnabled(false);
                break;
            case "Diagonal":
                cbFlip.setEnabled(true);
                cboxLineEnds.setEnabled(true);
                cboxLineJoints.setEnabled(false);
                break;
            case "Rectangle":
                cbFlip.setEnabled(false);
                cboxLineEnds.setEnabled(false);
                cboxLineJoints.setEnabled(true);
                break;
            case "Set":
                result = true;
                flip = false;
                if (rbHorizontal.isSelected()) {
                    lineType = LineTypes.HORIZ;
                } else {
                    if (rbVertical.isSelected()) {
                        lineType = LineTypes.VERT;
                    } else {
                        if (rbDiagonal.isSelected()) {
                            lineType = LineTypes.DIAG;
                            this.setFlip(cbFlip.isSelected());
                        } else {
                            if (rbRectangle.isSelected()) {
                                lineType = LineTypes.RECT;
                            }
                        }
                    }
                }
                if (Calc.isNumeric(tfWidth.getText())) {
                    this.setLineWidth(Float.parseFloat(tfWidth.getText()));
                    result = true;
                } else {
                    JOptionPane.showMessageDialog(this, "Line width is not a number.");
                    result = false;
                }
                if (result) {
                    this.setVisible(false);
                }
                lineEnds = getIntDinString((String) cboxLineEnds.getSelectedItem());
                lineJoints = getIntDinString((String) cboxLineJoints.getSelectedItem());
                break;
            case "Select Color":
                ColorChooser cc = new ColorChooser(this, selectedColor);
                if (cc.getResponse()) {
                    selectedColor = cc.getSelectedColor();
                }
                break;
            case "Cancel":
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Unknown command:" + ae.getActionCommand());
        }
    }

    private void setValues() {
        switch (lineType) {
            case HORIZ:
                rbHorizontal.setSelected(true);
                cbFlip.setEnabled(false);
                cboxLineEnds.setEnabled(true);
                cboxLineJoints.setEnabled(false);
                break;
            case VERT:
                rbVertical.setSelected(true);
                cbFlip.setEnabled(false);
                cboxLineEnds.setEnabled(true);
                cboxLineJoints.setEnabled(false);
                break;
            case DIAG:
                rbDiagonal.setSelected(true);
                cbFlip.setEnabled(true);
                cbFlip.setSelected(flip);
                cboxLineEnds.setEnabled(true);
                cboxLineJoints.setEnabled(false);
                break;
            case RECT:
                rbRectangle.setSelected(true);
                cbFlip.setEnabled(false);
                cboxLineEnds.setEnabled(false);
                cboxLineJoints.setEnabled(true);
                break;
        }
        tfWidth.setText(String.valueOf(lineWidth));
        cboxLineEnds.setSelectedItem(getStringLineEnds());
        cboxLineJoints.setSelectedItem(getStringLineJoints());
    }

    private String getStringLineEnds() {
        switch (lineEnds) {
            case BasicStroke.CAP_BUTT:
                return "CAP_BUTT";
            case BasicStroke.CAP_SQUARE:
                return "CAP_SQUARE";
            case BasicStroke.CAP_ROUND:
                return "CAP_ROUND";
        }
        return "CAP_SQUARE";
    }

    private String getStringLineJoints() {
        switch (lineJoints) {
            case BasicStroke.JOIN_BEVEL:
                return "JOIN_BEVEL";
            case BasicStroke.JOIN_MITER:
                return "JOIN_MITER";
            case BasicStroke.JOIN_ROUND:
                return "JOIN_ROUND";
        }
        return "JOIN_MITER";
    }

    private int getIntDinString(String str) {
        switch (str) {
            case "CAP_BUTT":
                return BasicStroke.CAP_BUTT;
            case "CAP_SQUARE":
                return BasicStroke.CAP_SQUARE;
            case "CAP_ROUND":
                return BasicStroke.CAP_ROUND;
            case "JOIN_BEVEL":
                return BasicStroke.JOIN_BEVEL;
            case "JOIN_MITER":
                return BasicStroke.JOIN_MITER;
            case "JOIN_ROUND":
                return BasicStroke.JOIN_ROUND;
        }
        return 0;
    }
}
