package dorel.simplejavareport.designer.actions.report;

import dorel.simplejavareport.designer.Designer;
import dorel.simplejavareport.dialogs.PageSetup;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public class PageSetupAction extends AbstractAction {

    Designer designer;

    public PageSetupAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.reportContent != null) {
            // save content
            //designer.cPanel.saveReportContent();
            //
            PageSetup ps = new PageSetup(designer);
            ps.setPageFormat(designer.cPanel.reportContent.getPageFormat());
            ps.initComponents();
            if (ps.result) {
                designer.cPanel.reportContent.setPageFormat(ps.getPageFormat());
                // clear components
                designer.cPanel.refreshReportContent();
                designer.cPanel.revalidate();
                designer.cPanel.repaint();
                designer.cPanel.setModif(true);
            }
        }
    }
}