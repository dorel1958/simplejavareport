package dorel.simplejavareport.designer.actions.report;

import dorel.simplejavareport.designer.Designer;
import dorel.simplejavareport.dialogs.FontAndColorSetup;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public class FontAndColorAction extends AbstractAction {

    Designer designer;

    public FontAndColorAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.reportContent != null) {
            FontAndColorSetup fcs = new FontAndColorSetup(designer);
            if (fcs.result) {
                designer.cPanel.reportContent.setDefaultColor(fcs.getSelectedColor());
                designer.cPanel.reportContent.setDefaultFont(fcs.getSelectedFont());
                System.out.println("a setat in reportContent");
            } else {
                System.out.println("Nu a setat in reportContent");
            }
        }
    }

}
