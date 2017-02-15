package dorel.simplejavareport.designer.actions.format;

import dorel.simplejavareport.designer.ContentPanel;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class ToShortestAction extends AbstractAction {

    ContentPanel cPanel;

    public ToShortestAction(String text, ImageIcon icon, String desc, Integer mnemonic, ContentPanel cPanel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.cPanel = cPanel;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        cPanel.toShortest();
    }

}
