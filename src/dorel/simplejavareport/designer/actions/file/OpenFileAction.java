package dorel.simplejavareport.designer.actions.file;

import dorel.simplejavareport.designer.Designer;
import dorel.simplejavareport.report.ReportContent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class OpenFileAction extends AbstractAction {

    Designer designer;

    public OpenFileAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.testModif()) {
            ReportContent new_reportContent = new ReportContent();
            if (new_reportContent.open()) {
                designer.cPanel.setReportContent(new_reportContent);
                designer.setTitle("Simple Java Report Designer: " + new_reportContent.getNumeFis());
                // redim fereastra
                //Dimension dim = designer.cPanel.reportContent.getMyPreferredSize();
                //designer.setSize((int) (dim.getWidth() + 50 + 180), (int) (dim.getHeight() * 3 / 4));
            }
        }
    }
}
