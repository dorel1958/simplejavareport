package dorel.simplejavareport.designer.actions;

import dorel.simplejavareport.designer.Designer;
import dorel.simplejavareport.designer.Eti;
import dorel.simplejavareport.tools.SursaInfo;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class AddTextAction extends AbstractAction {

    Designer designer;

    public AddTextAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.reportContent != null) {
            int x = 30;
            int y = 10;
            // nu reusesc sa obtin poz Mouse, si daca e activ altfel!!
            String la = designer.popup.getLabel();
            if (la.startsWith("Activ")) {
                String[] aLa = la.split(",");
                x = Integer.parseInt(aLa[1]);
                y = Integer.parseInt(aLa[2]);
            }
            designer.popup.setLabel("Inactiv");
            Eti eti = new Eti("Text");
            eti.setLocation(x, y);
            eti.setSize(new Dimension(100, 20));
            //eti.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            eti.setTypeText();
            eti.setSursaText(SursaInfo.Sursa.TEXT);
            eti.setTextNumeric(false);
            eti.setFont(designer.cPanel.reportContent.defaultFont);
            eti.setHorizontalAlignment(JLabel.LEFT);
            eti.setVerticalAlignment(JLabel.CENTER);
            eti.setForeground(designer.cPanel.reportContent.getDefaultColor());
            eti.setFont(designer.cPanel.reportContent.getDefaultFont());

            designer.cPanel.lEti.add(eti);
            designer.cPanel.add(eti, 0);
            designer.cPanel.revalidate();
            designer.cPanel.repaint();
            designer.cPanel.setModif(true);
        }
    }
}
