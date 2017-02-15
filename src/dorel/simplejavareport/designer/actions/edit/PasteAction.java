package dorel.simplejavareport.designer.actions.edit;

import dorel.simplejavareport.designer.Designer;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public class PasteAction extends AbstractAction {

    Designer designer;

    public PasteAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.reportContent != null) {
            int x = 0;
            int y = 0;
            Point p = designer.cPanel.getLeftUpSelectionPoint();
            String la = designer.popup.getLabel();
            if (la.startsWith("Activ")) {
                String[] aLa = la.split(",");
                x = Integer.parseInt(aLa[1]);
                y = Integer.parseInt(aLa[2]);
            }
            designer.popup.setLabel("Inactiv");

            //designer.cPanel.pasteComponents(x-(int)p.getX(), y-(int)p.getY());
            designer.cPanel.pasteComponents(x - (int) p.getX(), y - (int) p.getY());
        }
    }
}
