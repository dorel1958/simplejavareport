package dorel.simplejavareport.designer.actions;

import dorel.simplejavareport.designer.Designer;
import dorel.simplejavareport.dialogs.FerHelp;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class HelpAction  extends AbstractAction{
    
    Designer designer;

    public HelpAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        //JOptionPane.showMessageDialog(null, "Help");
        FerHelp fh = new FerHelp(designer, false);
        
    }

}
