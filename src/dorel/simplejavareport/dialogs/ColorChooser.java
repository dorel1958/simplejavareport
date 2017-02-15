package dorel.simplejavareport.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ColorChooser extends JDialog implements ActionListener, ChangeListener {

    protected JColorChooser tcc;
    private Color selectedColor;
    private boolean response;
    
    public boolean getResponse(){
        return response;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public ColorChooser(JDialog dialog, Color selectedColor) {
        super(dialog, true);
        this.selectedColor = selectedColor;
        initComponents();
    }

    private void initComponents() {
        response=false;
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        // color chooser
        tcc = new JColorChooser(selectedColor);
        tcc.getSelectionModel().addChangeListener(this);
        tcc.setBorder(BorderFactory.createTitledBorder("Choose color"));
        add(tcc, BorderLayout.CENTER);

        List<JButton> lButoane = new ArrayList<>();
        JButton butSet = new JButton("Set");
        butSet.addActionListener(this);
        lButoane.add(butSet);
        JButton butCancel = new JButton("Cancel");
        butCancel.addActionListener(this);
        lButoane.add(butCancel);
        add(PanelFactory.createHorizontalButtonsRow(lButoane), BorderLayout.SOUTH);

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

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Set":
                response=true;
                setVisible(false);
                break;
            case "Cancel":
                setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necunoscuta:" + ae.getActionCommand());
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        selectedColor = tcc.getColor();
    }

}
