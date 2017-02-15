package dorel.simplejavareport.designer.actions.file;

import dorel.simplejavareport.designer.Designer;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class CloseFileAction extends AbstractAction {

    Designer designer;

    public CloseFileAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.clearReportContent()) {
            designer.setFrameIcon(Designer.IconType.EMPTY);
        }
    }
}
