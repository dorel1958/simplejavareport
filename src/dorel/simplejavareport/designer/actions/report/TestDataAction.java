package dorel.simplejavareport.designer.actions.report;

import dorel.simplejavareport.designer.Designer;
import dorel.simplejavareport.dialogs.TestData;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.ImageIcon;

public class TestDataAction extends AbstractAction {

    Designer designer;

    public TestDataAction(String text, ImageIcon icon, String desc, Integer mnemonic, Designer designer) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.designer = designer;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (designer.cPanel.reportContent != null) {
            TestData td = new TestData(designer);
            td.setValues(designer.cPanel.reportContent.mCommon, designer.cPanel.reportContent.columnNames, designer.cPanel.reportContent.lData);
            //td.initComponents("mCommon");
            td.initComponents();
            if (td.result) {
                designer.cPanel.reportContent.mCommon = td.getCommon();
                designer.cPanel.reportContent.columnNames = td.getColumnNames();
                designer.cPanel.reportContent.lData = td.getData();
                designer.cPanel.setModif(true);
            }
        }
    }
}
