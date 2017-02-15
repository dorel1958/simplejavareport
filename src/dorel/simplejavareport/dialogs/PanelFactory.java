package dorel.simplejavareport.dialogs;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

public class PanelFactory {

    private static enum HorizAlignment {

        LEFT,
        RIGHT
    }

    public static JPanel createHorizontalButtonsRow(List<JButton> lButoane) {
        // se va pune in BorderLayout.SOUTH
        HorizAlignment align = HorizAlignment.RIGHT;
        int sep = 5;
        JPanel panel = new JPanel();
        //panel.setBorder(BorderFactory.createLineBorder(Color.red));
        panel.setBorder(BorderFactory.createEmptyBorder(sep, sep, sep, sep));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        if (align == HorizAlignment.RIGHT) {
            panel.add(Box.createHorizontalGlue());
        }
        boolean ePrima = true;
        for (JButton but : lButoane) {
            if (ePrima) {
                ePrima = false;
            } else {
                panel.add(Box.createRigidArea(new Dimension(sep, 0)));
            }
            panel.add(but);
        }
        return panel;
    }

    public static JPanel createComponentArray(List<Component[]> lComponents) {
        return createComponentArray(lComponents, 5, 5, 5, 5);
    }

    public static JPanel createComponentArray(List<Component[]> lComponents, int leftMargin, int topMargin, int rightMargin, int bottomMargin) {
        // se va pune in BorderLayout.CENTER
        // toate String[] vor avea aceeasi lungime!!!
        int nCols = lComponents.get(0).length;
        boolean cuRedimText = false;
        JPanel panel = new JPanel();
        //panel.setBorder(BorderFactory.createLineBorder(Color.red));
        GroupLayout grLayout = new GroupLayout(panel);
        panel.setLayout(grLayout);
        GroupLayout.ParallelGroup parallelGroupH = grLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        //
        // prima coloana
        GroupLayout.SequentialGroup seqGroupH = grLayout.createSequentialGroup();
        seqGroupH.addGap(leftMargin); // distH la marginea stanga
        for (int i = 0; i < nCols; i++) {
            GroupLayout.ParallelGroup parGroupH = grLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
            for (int j = 0; j < lComponents.size(); j++) {
                Component comp = lComponents.get(j)[i];
                if (comp != null) {
                    if (cuRedimText) {
                        parGroupH.addComponent(comp);
                    } else {
                        parGroupH.addComponent(comp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
                    }
                }
            }
            seqGroupH.addGroup(parGroupH);
            // spatiu intre coloane
            seqGroupH.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        }
        // restul
        seqGroupH.addContainerGap(rightMargin, Short.MAX_VALUE);
        //
        parallelGroupH.addGroup(seqGroupH);
        //
        grLayout.setHorizontalGroup(parallelGroupH);
        //
        // Vertical
        GroupLayout.ParallelGroup parallelGroupV = grLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        GroupLayout.SequentialGroup seqGroupV = grLayout.createSequentialGroup();
        // margine sus
        seqGroupV.addGap(topMargin);  // distV de sus
        for (int i = 0; i < lComponents.size(); i++) {
            // rand cu rand
            GroupLayout.ParallelGroup parGroupV = grLayout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            for (int j = 0; j < nCols; j++) {
                Component comp = lComponents.get(i)[j];
                if (comp != null) {
                    parGroupV.addComponent(lComponents.get(i)[j], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
                }
            }
            seqGroupV.addGroup(parGroupV);
            seqGroupV.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
        }
        //margine jos
        seqGroupV.addContainerGap(bottomMargin, Short.MAX_VALUE);  // distV pana la marginea de jos
        //
        parallelGroupV.addGroup(seqGroupV);
        grLayout.setVerticalGroup(parallelGroupV);
        //
        return panel;
    }
}
