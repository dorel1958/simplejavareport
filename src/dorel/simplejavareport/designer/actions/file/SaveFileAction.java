package dorel.simplejavareport.designer.actions.file;

import dorel.simplejavareport.designer.Designer;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public class SaveFileAction extends AbstractAction {

    Designer designer;

    public SaveFileAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.reportContent == null) {
            // n-am ce salva
        } else {
            designer.cPanel.saveReportContent();
            designer.setTitle("Java Report Designer: " + designer.cPanel.reportContent.getNumeFis());
        }
    }
}
