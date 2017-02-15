package dorel.simplejavareport.designer.actions.report;

import dorel.simplejavareport.designer.Designer;
import dorel.simplejavareport.dialogs.Bands;
//import dorel.simplejavareport.dialogs.BandsV;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public class BandsAction extends AbstractAction {

    Designer designer;

    public BandsAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.reportContent != null) {
            // save content
            designer.cPanel.saveReportContent();
            // modif bands
            Bands ob = new Bands(designer, designer.cPanel.reportContent);
            if (ob.result) {
                designer.cPanel.refreshReportContent();
                designer.cPanel.revalidate();
                designer.cPanel.repaint();
                designer.cPanel.setModif(true);
            }
        }
    }
}
