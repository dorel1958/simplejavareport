package dorel.simplejavareport.report;

import dorel.simplejavareport.report.components.Band;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class ContentPainter implements Printable {

    ReportContent reportContent;
    private List<Integer> pageBreaks;  // array of page break line positions.
    private boolean summaryBandPage;  // if summary band is on next page
    //
    int imageHeight;
    int hHeader;
    int hFooter;

    public ContentPainter(ReportContent reportContent) {
        this.reportContent = reportContent;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        PageFormat initialPageFormat = reportContent.getPageFormat();
        reportContent.setPageFormat(pageFormat);
        if (initialPageFormat.getOrientation() != pageFormat.getOrientation()) {
            // aici ar trebui schimbate marginile paginii in caz de modificare a orientarii
            //
        }

        int imageWidth = reportContent.getOrientedImageWidth();
        imageHeight = reportContent.getOrientedImageHeight();
        hHeader = reportContent.getPageHeaderHeight();
        hFooter = reportContent.getPageFooterHeight();

        // deoarece se poate schimba paginatia, se vor lasa aici:
        for (Band band : reportContent.lBands) {
            band.setWidth(imageWidth);
        }

        // calculez paginatia NUMAI la primul apel
        if (pageBreaks == null) {
            calcPaginatia();
        }
        //
        // daca pagina EXISTA sau nu
        if (pageIndex > getLastPageIndex()) {
            return Printable.NO_SUCH_PAGE;
        }
        //
        // User (0,0) is typically outside the imageable area, so we must translate by the X and Y values in the PageFormat to avoid clipping.
        //graphics.translate((int) (pageFormat.getImageableX() / factorTransf), (int) pageFormat.getImageableY());
        graphics.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());
        //
        // Now we perform our rendering - pagina cu indexul pageIndex
        int pozYonPage = 0;
        for (Band band : reportContent.lBands) {
            if (band.isDetail()) {
                if (pageIndex <= pageBreaks.size()) { // ca sa nu se busasca la pagina ce are numai SummaryBand, fara date Detail
                    int start = (pageIndex == 0) ? 0 : pageBreaks.get(pageIndex - 1);
                    int end = (pageIndex == pageBreaks.size()) ? reportContent.lData.size() : pageBreaks.get(pageIndex);
                    for (int lineIndex = start; lineIndex < end; lineIndex++) {
                        band.setTranslateY(pozYonPage);
                        pozYonPage = band.drawComponents(graphics, pageIndex, lineIndex);
                    }
                }
            } else {
                if (band.isSumary()) {
                    // summary e ultima pe pagina (nu mai adauga la pozYonPage) (footerul se asaza singur la baza paginii)
                    if (summaryBandPage) {
                        if (pageIndex == pageBreaks.size() + 1) {
                            band.setTranslateY(pozYonPage);
                            band.drawComponents(graphics, pageIndex);
                        }
                    } else {
                        if (pageIndex == pageBreaks.size()) {
                            band.setTranslateY(pozYonPage);
                            band.drawComponents(graphics, pageIndex);
                        }
                    }
                } else {
                    if (band.isFooter()) {
                        // muta cursorul la partea de jos a paginii
                        int pozYonFooter = reportContent.getOrientedImageHeight() - reportContent.getPageFooterHeight();
                        band.setTranslateY(pozYonFooter);
                        band.drawComponents(graphics, pageIndex);
                    } else {
                        band.setTranslateY(pozYonPage);
                        pozYonPage = band.drawComponents(graphics, pageIndex);
                    }
                }
            }
        }
        // tell the caller that this page is part of the printed document
        return Printable.PAGE_EXISTS;
    }

    public int getLastPageIndex() {
        if (pageBreaks == null) {
            return 0;
        } else {
            if (summaryBandPage) {
                return pageBreaks.size() + 1;
            } else {
                return pageBreaks.size();
            }
        }
    }

    public void resetPageBreaks() {
        pageBreaks = null;
    }

    private void calcPaginatia() {
        double hDetail;
        int restLinii;
        int linesDetailPerPage;
        int pageIndex = 0;
        int currentBreak = 0;
        summaryBandPage = false;
        pageBreaks = new ArrayList<>();

        hDetail = calcSpLiber(pageIndex); // necesar pt situatia in care nu exista inregistrari in lContent

        restLinii = reportContent.lData.size();
        int detailBandHeight = reportContent.getDetailBandHeight();
        while (restLinii > 0) {
            // calculez spatiul ramas liber pt liniile de detail
            hDetail = calcSpLiber(pageIndex);
            if (hDetail <= 0) {
                // pt a nu se bloca
                break;
            }
            // calculez pozitia break-ului la pagina cu pageIndex
            linesDetailPerPage = (int) Math.floor(hDetail / detailBandHeight);
            if (restLinii > linesDetailPerPage) {
                currentBreak += linesDetailPerPage;
                hDetail -= linesDetailPerPage * detailBandHeight;
                pageBreaks.add(currentBreak);
                restLinii -= linesDetailPerPage;
                pageIndex++;
            } else {
                hDetail -= restLinii * detailBandHeight;
                restLinii = 0;
                // ultimul nu se pune
            }
        }
        //
        int summaryBandHeight = reportContent.getSummaryBandHeight();
        if (summaryBandHeight > 0) {
            // compar spatiul ramas dupa ultima linie a ultimei pagini cu hSummaryBand
            if (hDetail < summaryBandHeight) {
                // mai adauga o pagina daca nu e spatiu suficient
                summaryBandPage = true;
            } else {
                // e bine, ramane asa summaryBand incape pe ultima pagina
            }
        }
        // setat pentru variabilade report  nrPagini
        reportContent.setLastPageIndex(getLastPageIndex());
    }

    private int calcSpLiber(int pageIndex) {
        // calculez spatiul ramas liber pt liniile de detail
        int hDetail = imageHeight;
        if (hHeader + hFooter < imageHeight) {
            hDetail = hDetail - hHeader - hFooter;
            if (pageIndex == 0) {
                int hFirstPageHeight = reportContent.getFirstPageHeight();
                if (hDetail > hFirstPageHeight) {
                    hDetail -= hFirstPageHeight;
                } else {
                    JOptionPane.showMessageDialog(null, "MyContentPainter.calcSpLiber: hDetail < hOnlyInFirstPage");
                    hDetail = 0;
                }
            } else {
                int hRestPagesHeight = reportContent.getRestPagesHeight();
                if (hDetail > hRestPagesHeight) {
                    hDetail -= hRestPagesHeight;
                } else {
                    JOptionPane.showMessageDialog(null, "MyContentPainter.calcSpLiber: hDetail < hInRestPagini");
                    hDetail = 0;
                }
            }
        } else {
            // header+footer>imageHeight
            JOptionPane.showMessageDialog(null, "MyContentPainter.calcSpLiber: hHeader+hFooter > imageHeight");
            hDetail = 0;
        }
        return hDetail;
    }

}
