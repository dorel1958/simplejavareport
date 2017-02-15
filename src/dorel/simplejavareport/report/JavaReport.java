package dorel.simplejavareport.report;

import dorel.simplejavareport.designer.Designer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public final class JavaReport extends JDialog implements ActionListener {

    private boolean designTime = false;
    private ReportContent reportContent;
    PreviewLabel previewLabel;
    private boolean cuPageDialog;
    private boolean cuPrintDialog;
    String numeFis;
    Map<String, String> mCommon;
    String[] columnNames;
    List<String[]> lDate;
    JScrollPane scroll;

    public JavaReport(JFrame frame, String numeFis, Map<String, String> mCommon, List<String[]> lData) {
        super(frame, true);
        this.numeFis = numeFis;
        this.lDate = lData;
        this.mCommon = mCommon;
    }

    public JavaReport(Designer designer) {
        super(designer, true);
        this.reportContent = designer.cPanel.getReportContent();
        designTime = true;
    }

    public void preview() {
        preview(false, false);
    }

    public void preview(boolean cuPageDialog, boolean cuPrintDialog) {
        if (!designTime) {
            reportContent = new ReportContent(numeFis, mCommon, lDate);
        }
        this.cuPageDialog = cuPageDialog;
        this.cuPrintDialog = cuPrintDialog;
        initComponents();
    }

    public void print() {
        print(false, false);
    }

    public void print(boolean cuPageDialog, boolean cuPrintDialog) {
        if (reportContent == null) {
            // daca nu a avut preview inainte - incarca si tipareste direct
            reportContent = new ReportContent(numeFis, mCommon, lDate);
        }
        Print print = new Print();
        PageFormat pageFormat;
        pageFormat = reportContent.getPageFormat();
        print.setCuPageDialog(cuPageDialog);
        print.setCuPrintDialog(cuPrintDialog);
        ContentPainter contentPainter = new ContentPainter(reportContent);
        if (print.tipareste(contentPainter, pageFormat)) {
            // e bine
        } else {
            JOptionPane.showMessageDialog(this, "Print error: " + print.getMesajEroare());
        }
    }

    private void initComponents() {
        this.setTitle("Java Report Preview: " + reportContent.getNumeFis());
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        //JButton butonPrintr = new JButton("SetPage ...");
        //butonPrintr.addActionListener(this);
        JButton butonPrima = new JButton("|<");
        butonPrima.addActionListener(this);
        JButton butonPrec = new JButton("<");
        butonPrec.addActionListener(this);
        JButton butonGoTo = new JButton("...");
        butonGoTo.addActionListener(this);
        JButton butonUrm = new JButton(">");
        butonUrm.addActionListener(this);
        JButton butonUltima = new JButton(">|");
        butonUltima.addActionListener(this);

        JButton butonPrint = new JButton("Print");
        butonPrint.addActionListener(this);
        JButton butonPrintd = new JButton("Print ...");
        butonPrintd.addActionListener(this);

        JButton butonIesire = new JButton("Iesire");
        butonIesire.addActionListener(this);

        //panel.add(butonPrintr);
        panel.add(butonPrima);
        panel.add(butonPrec);
        //panel.add(butonGoTo);
        panel.add(butonUrm);
        panel.add(butonUltima);
        panel.add(butonPrint);
        panel.add(butonPrintd);
        panel.add(butonIesire);

        previewLabel = new PreviewLabel(reportContent);
        //scroll = new JScrollPane(reportContent);
        scroll = new JScrollPane(previewLabel);
        int pw;
        int ph;
        if (reportContent.pageOrientation == PageFormat.PORTRAIT) {
            pw = (int) (reportContent.getPaper_width()) + 20;  // +20 latimea scroll bar-ului
            ph = (int) (reportContent.getPaper_height());
        } else {
            ph = (int) (reportContent.getPaper_width());
            pw = (int) (reportContent.getPaper_height()) + 20;  // +20 latimea scroll bar-ului
        }
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setPreferredSize(new Dimension((int) (pw * PreviewLabel.coeficientScala), (int) (ph / 2)));

        add(scroll, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        this.pack();

        // <editor-fold defaultstate="collapsed" desc="Centrare in Screen">      
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

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        int curentIndex = previewLabel.getPageIndex();

        switch (ae.getActionCommand()) {
            case "SetPage ...":
                // Get a PrinterJob
                PrinterJob job = PrinterJob.getPrinterJob();
                // Ask user for page format (e.g., portrait/landscape)
                PageFormat pf = job.pageDialog(reportContent.getPageFormat());

                reportContent.setPageFormat(pf);
                ContentPainter contentPainter = new ContentPainter(reportContent);
                contentPainter.resetPageBreaks();
                int pw;
                int ph;
                if (reportContent.pageOrientation == PageFormat.PORTRAIT) {
                    pw = (int) (reportContent.getPaper_width()) + 20;
                    ph = (int) (reportContent.getPaper_height());
                } else {
                    ph = (int) (reportContent.getPaper_width());
                    pw = (int) (reportContent.getPaper_height()) + 20;
                }
                scroll.setPreferredSize(new Dimension(pw, (int) (3 * ph / 4)));
                this.pack();
                break;
            case "|<":
                previewLabel.setPageIndex(0);
                previewLabel.repaint();
                break;
            case "<":
                if (curentIndex > 0) {
                    previewLabel.setPageIndex(curentIndex - 1);
                    previewLabel.repaint();
                } else {
                    //JOptionPane.showMessageDialog(this, "Sunteti la prima pagina.");
                }
                break;
            case "...":
                // fereastra de preluare nrPagina
                break;
            case ">":
                if (curentIndex < reportContent.getLastPageIndex()) {
                    previewLabel.setPageIndex(curentIndex + 1);
                    previewLabel.repaint();
                } else {
                    //JOptionPane.showMessageDialog(this, "Sunteti la ultima pagina.");
                }
                break;
            case ">|":
                previewLabel.setPageIndex(reportContent.getLastPageIndex());
                previewLabel.repaint();
                break;
            case "Print":
                print(cuPageDialog, cuPrintDialog);
                break;
            case "Print ...":
                print(true, true);
                break;
            case "Iesire":
                this.dispose();
                break;
        }
    }
}
