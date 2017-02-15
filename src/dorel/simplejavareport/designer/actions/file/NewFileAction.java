package dorel.simplejavareport.designer.actions.file;

import dorel.simplejavareport.designer.Designer;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class NewFileAction extends AbstractAction {

    Designer designer;

    public NewFileAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        designer.cPanel.newReportContent();
        // redim fereastra
        //Dimension dim = designer.cPanel.reportContent.getMyPreferredSize();
        //designer.setSize((int) (dim.getWidth() + 50 + 180), (int) (dim.getHeight() * 3 / 4));
    }

}
