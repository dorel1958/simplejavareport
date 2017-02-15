package dorel.simplejavareport.report;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class Print {

    private String mesajEroare;
    private boolean cuPageDialog = true;
    private boolean cuPrintDialog = true;

// <editor-fold defaultstate="collapsed" desc="Get Set">
    public boolean isCuPrintDialog() {
        return cuPrintDialog;
    }

    public void setCuPrintDialog(boolean cuPrintDialog) {
        this.cuPrintDialog = cuPrintDialog;
    }

    public boolean isCuPageDialog() {
        return cuPageDialog;
    }

    public void setCuPageDialog(boolean cuPageDialog) {
        this.cuPageDialog = cuPageDialog;
    }

    public String getMesajEroare() {
        return mesajEroare;
    }
//</editor-fold>

    public boolean tipareste(Printable content, PageFormat pf) {
        boolean raspuns = false;
        PrinterJob job = PrinterJob.getPrinterJob();

        if (this.isCuPageDialog()) {
            // afisaza PageSetup dialog
            pf = job.pageDialog(pf);
        }

        job.setPrintable(content, pf);

        boolean doPrint = true;
        if (this.isCuPrintDialog()) {
            // afisaza Print dialog
            doPrint = job.printDialog();
        }
        if (doPrint) {
            try {
                job.print();
                raspuns = true;
            } catch (PrinterException ex) {
                mesajEroare = ex.getLocalizedMessage();
            }
        }
        return raspuns;
    }
}
