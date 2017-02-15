package dorel.simplejavareport.dialogs;

import dorel.simplejavareport.report.ReportContent;
import dorel.simplejavareport.report.components.Band;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public final class Bands extends JDialog implements ActionListener {

    public boolean result = false;
    JCheckBox cbPageHeader;
    JCheckBox cbTitleBand;
    JCheckBox cbTableHeader;
    JCheckBox cbTableHeaderOnlyInFirstPage;
    JCheckBox cbDetailBand;
    JCheckBox cbSummaryBand;
    JCheckBox cbPageFooter;
    ReportContent reportContent;

    public Bands(Frame owner, ReportContent reportContent) {
        super(owner, true);
        this.reportContent = reportContent;
        initComponents();
    }

    public void setValues() {
        this.cbPageHeader.setSelected(reportContent.hasBandNamed(Band.PAGE_HEADER));
        this.cbTitleBand.setSelected(reportContent.hasBandNamed(Band.TITLE_BAND));
        if (reportContent.hasBandNamed(Band.TABLE_HEADER)) {
            this.cbTableHeader.setSelected(true);
            cbTableHeaderOnlyInFirstPage.setSelected(reportContent.getBandaNamed(Band.TABLE_HEADER).isOnlyInFirstPage());
        } else {
            this.cbTableHeader.setSelected(false);
            cbTableHeaderOnlyInFirstPage.setSelected(false);
        }
        this.cbDetailBand.setSelected(reportContent.hasBandNamed(Band.DETAIL_BAND));
        this.cbSummaryBand.setSelected(reportContent.hasBandNamed(Band.SUMMARY_BAND));
        this.cbPageFooter.setSelected(reportContent.hasBandNamed(Band.PAGE_FOOTER));
    }

    private void initComponents() {
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setTitle("Bands");
        this.setLayout(new BorderLayout());
        setResizable(false);

        List<Component[]> lComponents = new ArrayList<>();
        Component[] linia;

        // <editor-fold defaultstate="collapsed" desc="Page Header">      
        List<Component[]> lPageHeader = new ArrayList<>();
        linia = new Component[2];
        cbPageHeader = new JCheckBox("Page Header");
        linia[0] = cbPageHeader;
        lPageHeader.add(linia);
        linia = new Component[1];
        JPanel panelPageHeader = PanelFactory.createComponentArray(lPageHeader);
        panelPageHeader.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelPageHeader;
        lComponents.add(linia);
        //</editor-fold>        

        // <editor-fold defaultstate="collapsed" desc="Title Band">      
        List<Component[]> lTitleBand = new ArrayList<>();
        linia = new Component[2];
        cbTitleBand = new JCheckBox("Title Band");
        linia[0] = cbTitleBand;
        lTitleBand.add(linia);
        linia = new Component[1];
        JPanel panelTitleBand = PanelFactory.createComponentArray(lTitleBand);
        panelTitleBand.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelTitleBand;
        lComponents.add(linia);
        //</editor-fold>        

        // <editor-fold defaultstate="collapsed" desc="Table Header">      
        List<Component[]> lTableHeader = new ArrayList<>();
        linia = new Component[2];
        cbTableHeader = new JCheckBox("Table Header");
        cbTableHeader.addActionListener(this);
        cbTableHeaderOnlyInFirstPage = new JCheckBox("Only in First Page");
        linia[0] = cbTableHeader;
        linia[1] = cbTableHeaderOnlyInFirstPage;
        lTableHeader.add(linia);
        linia = new Component[1];
        JPanel panelTableHeader = PanelFactory.createComponentArray(lTableHeader);
        panelTableHeader.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelTableHeader;
        lComponents.add(linia);
        //</editor-fold>        

        // <editor-fold defaultstate="collapsed" desc="Detail Band">      
        List<Component[]> lDetailBand = new ArrayList<>();
        linia = new Component[2];
        cbDetailBand = new JCheckBox("Detail Band");
        linia[0] = cbDetailBand;
        lDetailBand.add(linia);
        linia = new Component[1];
        JPanel panelDetailBand = PanelFactory.createComponentArray(lDetailBand);
        panelDetailBand.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelDetailBand;
        lComponents.add(linia);
        //</editor-fold>        

        // <editor-fold defaultstate="collapsed" desc="Summary Band">      
        List<Component[]> lSummaryBand = new ArrayList<>();
        linia = new Component[2];
        cbSummaryBand = new JCheckBox("Summary Band");
        linia[0] = cbSummaryBand;
        lSummaryBand.add(linia);
        linia = new Component[1];
        JPanel panelSummaryBand = PanelFactory.createComponentArray(lSummaryBand);
        panelSummaryBand.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelSummaryBand;
        lComponents.add(linia);
        //</editor-fold>        

        // <editor-fold defaultstate="collapsed" desc="Page Footer">      
        List<Component[]> lPageFooter = new ArrayList<>();
        linia = new Component[2];
        cbPageFooter = new JCheckBox("Page Footer");
        linia[0] = cbPageFooter;
        lPageFooter.add(linia);
        linia = new Component[1];
        JPanel panelPageFooter = PanelFactory.createComponentArray(lPageFooter);
        panelPageFooter.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        linia[0] = panelPageFooter;
        lComponents.add(linia);
        //</editor-fold>        

        JPanel panel = PanelFactory.createComponentArray(lComponents);
        // set the same width
        ((GroupLayout) panel.getLayout()).linkSize(SwingConstants.HORIZONTAL, panelPageHeader, panelTitleBand, panelTableHeader, panelDetailBand, panelSummaryBand, panelPageFooter);
        add(panel, BorderLayout.CENTER);

        setValues();

        // <editor-fold defaultstate="collapsed" desc="Buttons">      
        List<JButton> lButoane = new ArrayList<>();
        JButton butSet = new JButton("Set");
        butSet.addActionListener(this);
        lButoane.add(butSet);
        JButton butCancel = new JButton("Cancel");
        butCancel.addActionListener(this);
        lButoane.add(butCancel);
        add(PanelFactory.createHorizontalButtonsRow(lButoane), BorderLayout.SOUTH);
        //</editor-fold>        

        pack();

        // <editor-fold defaultstate="collapsed" desc="Center in Screen">      
        // Get the size of the screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Determine the new location of the window
        int w = this.getSize().width;
        int h = this.getSize().height;
        int x = 0;
        if (dim.width > w) {
            x = (dim.width - w) / 2;
        }
        int y = 0;
        if (dim.height > h) {
            y = (dim.height - h) / 2;
        }
        // Move the window
        this.setLocation(x, y);
        //</editor-fold>

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case "Set":
                // refac lista benzi si o pun la loc
                List<Band> newLbands = new ArrayList<>();
                //
                if (cbPageHeader.isSelected()) {
                    if (reportContent.hasBandNamed(Band.PAGE_HEADER)) {
                        newLbands.add(reportContent.getBandaNamed(Band.PAGE_HEADER));
                    } else {
                        newLbands.add(new Band(Band.PAGE_HEADER, 40, reportContent));
                    }
                }
                //
                if (cbTitleBand.isSelected()) {
                    if (reportContent.hasBandNamed(Band.TITLE_BAND)) {
                        newLbands.add(reportContent.getBandaNamed(Band.TITLE_BAND));
                    } else {
                        newLbands.add(new Band(Band.TITLE_BAND, 40, reportContent));
                    }
                }
                //
                if (cbTableHeader.isSelected()) {
                    Band band;
                    if (reportContent.hasBandNamed(Band.TABLE_HEADER)) {
                        band = reportContent.getBandaNamed(Band.TABLE_HEADER);
                    } else {
                        band = new Band(Band.TABLE_HEADER, 40, reportContent);
                    }
                    band.setOnlyInFirstPage(cbTableHeaderOnlyInFirstPage.isSelected());
                    newLbands.add(band);
                }
                //
                if (cbDetailBand.isSelected()) {
                    if (reportContent.hasBandNamed(Band.DETAIL_BAND)) {
                        newLbands.add(reportContent.getBandaNamed(Band.DETAIL_BAND));
                    } else {
                        newLbands.add(new Band(Band.DETAIL_BAND, 40, reportContent));
                    }
                }
                //
                if (cbSummaryBand.isSelected()) {
                    if (reportContent.hasBandNamed(Band.SUMMARY_BAND)) {
                        newLbands.add(reportContent.getBandaNamed(Band.SUMMARY_BAND));
                    } else {
                        newLbands.add(new Band(Band.SUMMARY_BAND, 40, reportContent));
                    }
                }
                //
                if (cbPageFooter.isSelected()) {
                    if (reportContent.hasBandNamed(Band.PAGE_FOOTER)) {
                        newLbands.add(reportContent.getBandaNamed(Band.PAGE_FOOTER));
                    } else {
                        newLbands.add(new Band(Band.PAGE_FOOTER, 40, reportContent));
                    }
                }
                //
                reportContent.lBands = newLbands;
                result = true;
                this.setVisible(false);
                break;
            case "Table Header":
                if (cbTableHeader.isSelected()) {
                    cbTableHeaderOnlyInFirstPage.setEnabled(true);
                } else {
                    cbTableHeaderOnlyInFirstPage.setEnabled(false);
                    //cbTableHeaderOnlyInFirstPage.setSelected(false);
                }
                break;
            case "Cancel":
                this.setVisible(false);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Comanda necunoscuta:" + ae.getActionCommand());
                break;
        }
    }
}
