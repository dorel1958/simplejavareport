package dorel.simplejavareport.report;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

// Label utilizat numai la vizualizarea documentului la Preview in Report
public class PreviewLabel extends JLabel implements Scrollable {

    public ReportContent reportContent;
    private ContentPainter contentPainter;
    private final int maxUnitIncrement = 1;
    public static final double coeficientScala =1.333;// 1.333; //=96/72; de cate ori sa fie mai mare imaginea din preview fata de cea afisata in Form(care e mai mica)
    private static final boolean deseneazaConturImageable = true; // folosit numai pentru teste
    private int pageIndex = 0;
    
    // <editor-fold defaultstate="collapsed" desc="Get Set">
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }
//</editor-fold>

    public PreviewLabel(ReportContent reportContent) {
        this.reportContent = reportContent;
        contentPainter = new ContentPainter(reportContent);
        setAutoscrolls(true);
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(coeficientScala, coeficientScala);
        //g2.scale(2, 2);  // chiar merge - la preview face de 2 ori mai mare poza

        // deseneaza pagina alba
        int dim1 = reportContent.getPaper_width();
        int dim2 = reportContent.getPaper_height();

        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, dim1, dim2);
        //g2.drawRect(0, 0, dim1, dim2);

        if (deseneazaConturImageable) {
            // contur imageable - numai la preview NU deseneaza la printare
            g2.setColor(Color.lightGray);
            //g2.drawRect((int) reportContent.getPageFormat().getPaper().getImageableX() + 1, (int) reportContent.getPageFormat().getPaper().getImageableY() + 1, (int) reportContent.getPageFormat().getPaper().getImageableWidth(), (int) reportContent.getPageFormat().getPaper().getImageableHeight());
            g2.drawRect((int) reportContent.getPageFormat().getPaper().getImageableX(), (int) reportContent.getPageFormat().getPaper().getImageableY(), (int) reportContent.getPageFormat().getPaper().getImageableWidth(), (int) reportContent.getPageFormat().getPaper().getImageableHeight());
            //g2.drawRect((int) getPageFormat().getPaper().getImageableX(), (int) getPageFormat().getPaper().getImageableY(), (int) getPageFormat().getPaper().getImageableWidth(), (int) getPageFormat().getPaper().getImageableHeight());
        }

        //factorTransf = MM_PUNCTE_PRINT;
        g2.setColor(Color.black);
        //ContentPainter contentPainter = new ContentPainter(reportContent);
        contentPainter.print(g2, reportContent.getPageFormat(), pageIndex);
        g2.scale(1, 1);
    }

    // <editor-fold defaultstate="collapsed" desc="Scrollable">
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension dimensionUG = reportContent.getMyPreferredSize();
        return new Dimension((int) (dimensionUG.width * coeficientScala), (int) (dimensionUG.height * coeficientScala));
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        //Get the current position.
        int currentPosition;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        //Return the number of pixels between currentPosition and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition - (currentPosition / maxUnitIncrement) * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement - currentPosition;
        }
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension dimensionUG = reportContent.getMyPreferredSize();
        return new Dimension((int) (dimensionUG.width * coeficientScala), (int) (dimensionUG.height * coeficientScala));
    }
//</editor-fold>
}
