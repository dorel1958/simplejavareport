package dorel.simplejavareport.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class FerHelp extends JDialog implements ActionListener {

    public FerHelp(Frame frame, boolean bln) {
        super(frame, bln);
        initComponents();
    }

    private void initComponents() {
        this.setTitle("Help");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        StringBuilder sb = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/dorel/simplejavareport/designer/actions/resources/help/help.html"), "UTF-8"));
            for (int c = br.read(); c != -1; c = br.read()) {
                sb.append((char) c);
            }
        } catch (UnsupportedEncodingException ex) {
            sb.append("UnsupportedEncodingException: ").append(ex.getLocalizedMessage());
        } catch (IOException ex) {
            sb.append("IOException: ").append(ex.getLocalizedMessage());
        }

        JLabel label = new JLabel();
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        label.setBackground(Color.white);
        label.setOpaque(true);
        label.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(label);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.lightGray));

        add(scrollPane, BorderLayout.CENTER);

        // <editor-fold defaultstate="collapsed" desc="Buttons">      
        List<JButton> lButoane = new ArrayList<>();
        JButton butCancel = new JButton("Close");
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
            case "Close":
                this.dispose();
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necunoscuta:" + ae.getActionCommand());
        }
    }
}
