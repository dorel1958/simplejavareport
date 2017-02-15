package dorel.simplejavareport.designer;

import dorel.simplejavareport.dialogs.ParamImage;
import dorel.simplejavareport.dialogs.ParamLine;
import dorel.simplejavareport.dialogs.ParamText;
import dorel.simplejavareport.report.ReportContent;
import dorel.simplejavareport.report.components.Band;
import dorel.simplejavareport.report.components.RapImage;
import dorel.simplejavareport.report.components.RapLine;
import dorel.simplejavareport.report.components.RapText;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

public final class ContentPanel extends JPanel implements Scrollable, KeyListener, MouseListener, MouseMotionListener, ClipboardOwner {

    public static final double coeficientScala = 1; //1.333  =96/72; de cate ori sa fie mai mare imaginea din preview fata de cea afisata in Form(care e mai mica)
    // datorita:INCH_PUNCTE_PRINT = 72; dpi si INCH_PUNCTE_SCREEN = 96; ppi la mine 90 ppi
    public List<Eti> lEti;
    public List<LabelBand> lBands;

    private final int maxUnitIncrement = 1;  // pasul la Scroll
    JLabel page;
    //
    private int mouseCursor;  // for managing Drag Action
    private boolean redimBanda = false;
    private final int keyStep = 1;  // pasul de deplasare la apasarea tastelor cu sageti
    //
    private boolean inCut = false;  // daca e in curs un cut
    public List<Eti> lCutEti;  // lista etichetelor ce au primit comanda cut, dar nu sunt sterse INCA

    private boolean ownClipboard = false;  // sesizeaza daca o alta aplicatie a modificat clipboardul
    Point leftUpSelectionPoint; // used for RightClickPaste - left up pint of Selection

    private boolean inLasou;
    private final JLabel etiLasou;
    private int etiLasouX0;
    private int etiLasouY0;
    private final Designer designer;

    private boolean modif;

    // <editor-fold defaultstate="collapsed" desc="Get Set">
    public boolean isModif() {
        return modif;
    }

    public void setModif(boolean modif) {
        this.modif = modif;
        if (modif) {
            designer.setFrameIcon(Designer.IconType.MODIFIED);
        } else {
            designer.setFrameIcon(Designer.IconType.FILE);
        }
    }
    //</editor-fold>

    public ContentPanel(Designer designer) {
        this.designer = designer;
        setAutoscrolls(true); //enable synthetic drag events
        lEti = new ArrayList<>();
        lBands = new ArrayList<>();
        //setModif(false);  // panel-ul e gol la inceput - imi modifica Icon-ul
        etiLasou = new JLabel("");
        etiLasou.setBorder(BorderFactory.createDashedBorder(Color.lightGray));
        inLasou = false;
    }

    // <editor-fold defaultstate="collapsed" desc="Manage Object reportContent">
    public ReportContent reportContent;

    public void newReportContent() {
        if (testModif()) {
            clear();
            reportContent = new ReportContent();
            refreshReportContent();
            setModif(false);
            revalidate();
            repaint();
        }
    }

    public void setReportContent(ReportContent rc) {
        // utilizata NUMAI la OpenFileActiuon -> face acolo testul pt modificare
        clear();
        reportContent = rc;
        refreshReportContent();
        setModif(false);
        revalidate();
        repaint();
    }

    public ReportContent getReportContent() {
        return reportContent;
    }

    public boolean clearReportContent() {
        if (testModif()) {
            clear();
            setModif(false);
            revalidate();
            repaint();
            return true;
        } else {
            return false;
        }
    }

    public void saveReportContent() {
        if (reportContent == null) {
            // n-am ce salva
        } else {
            // proprietatile Panel Components -> ReportContent objects
            if (updateReportContent()) {
                if (reportContent.save(false)) {
                    setModif(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Salvarea nu s-a putut efectua");
                }
            }
        }
    }

    public void saveReportContentAs() {
        if (reportContent == null) {
            // n-am ce salva
        } else {
            if (updateReportContent()) {
                if (reportContent.save(true)) {
                    setModif(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Salvarea nu s-a putut efectua");
                }
            }
        }
    }

    private void clear() {
        // cutata componentele din Panel si anuleaza reportContent
        reportContent = null;
        lEti.clear();
        lBands.clear();
        Component[] comp = getComponents();
        for (Component component : comp) {
            remove(component);
        }
    }
    // </editor-fold>

    public void refreshReportContent() {
        // render reportContent objects as Components into panel
        if (reportContent != null) {
            int delimH = 20;
            int paperWidth;
            int imageWidth;
            int leftMargin_pix;
            int curentY_pix;
            int bottomMargin;
            setLayout(null);
            imageWidth = reportContent.getOrientedImageWidth();
            paperWidth = reportContent.getOrientedPaperWidth();

            leftMargin_pix = reportContent.getPaper_marginLeft();
            curentY_pix = reportContent.getPaper_marginTop();
            bottomMargin = reportContent.getPaper_marginBottom();
            Dimension sepSize = new Dimension((int) (paperWidth), delimH);
            //
            // clear existing components
            Component[] comp = getComponents();
            for (Component component : comp) {
                remove(component);
            }
            //
            // Bands
            lBands = new ArrayList<>();
            lEti = new ArrayList<>();
            for (Band band : reportContent.lBands) {
                // banda
                LabelBand lb = new LabelBand(band);
                lb.setSize(imageWidth, band.getHeight());
                lb.setLocation(leftMargin_pix, curentY_pix);
                lBands.add(lb);
                add(lb);
                curentY_pix += band.getHeight();

                // delim de sub banda
                Eti delim = new Eti(band.getBandName() + " ^");
                delim.setLabelBand(lb);
                delim.setTypeBanda(sepSize);
                delim.setLocation(0, curentY_pix);
                lEti.add(delim);
                add(delim);
                curentY_pix += delimH;

                // add controalele din bamda
                for (RapText rt : band.lTexts) {
                    Eti eti = rt.getEti(lb.getY(), leftMargin_pix);
                    lEti.add(eti);
                    add(eti, 0);  // 0 pt a o pune deasupra celorlalte
                }
                for (RapLine rl : band.lLines) {
                    Eti eti = rl.getLineEti(lb.getY(), leftMargin_pix);
                    lEti.add(eti);
                    add(eti, 0);
                }
                for (RapImage ri : band.lImages) {
                    Eti eti = ri.getEti(lb.getY(), leftMargin_pix);
                    lEti.add(eti);
                    add(eti, 0);
                }
            }

            // Page
            curentY_pix += bottomMargin;
            page = new JLabel("");
            page.setBackground(Color.white);
            page.setOpaque(true);
            page.setBorder(BorderFactory.createLineBorder(Color.lightGray));
            page.setSize(paperWidth, curentY_pix);
            page.setLocation(0, 0);
            setPreferredSize(new Dimension(paperWidth, curentY_pix));
            add(page);
        }
    }

    public boolean updateReportContent() {
        // actualizeaza in reportContent ceea ce se vede pe ecran
        //
        // pune inaltimile benzilor
        boolean raspuns = true;
        for (LabelBand lb : lBands) {
            lb.contentBand.setHeight(lb.getHeight());
        }
        //
        // add lines, images and Texts
        reportContent.resetBandsLists();  // mai intai anuleaza listele veci
        int marginLeft = reportContent.getPaper_marginLeft();
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                boolean luata = false;
                for (LabelBand lb : lBands) {
                    if (lb.testInBand(eti.getRealTop())) {
                        lb.contentBand.addEti(eti, marginLeft, lb.getY());
                        luata = true;
                    }
                }
                if (!luata) {
                    raspuns = false;
                    JOptionPane.showMessageDialog(null, "Control out of bands:" + eti.getText());
                }
            }
        }
        return raspuns;
    }

    // deseneaza bine, dar interactiunea e varza la coeficientScala<>1
    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.scale(coeficientScala, coeficientScala);
        super.paint(g2);
    }

    // <editor-fold defaultstate="collapsed" desc="MouseMotionListener">
    @Override
    public void mouseMoved(MouseEvent me) {
        int nX = (int) (me.getX() / coeficientScala);
        int nY = (int) (me.getY() / coeficientScala);
        for (Eti eti : lEti) {
            if (eti.testInEti(nX, nY)) {
                if (eti.isTypeBanda()) {
                    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return;
                } else {
                    if (eti.isSelected()) {
                        // ea este cea selectata
                        this.setCursor(new Cursor(eti.overWhichHandler(nX, nY)));
                        return;
                    }
                }
            }
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        int nX = (int) (me.getX() / coeficientScala);
        int nY = (int) (me.getY() / coeficientScala);
        if (inLasou) {
            //int dX = nX - etiLasouX;
            //int dY = nY - etiLasouY;
            int x;
            int y;
            int w;
            int h;
            if (nX > etiLasouX0) {
                x = etiLasouX0;
                w = nX - etiLasouX0;
            } else {
                x = nX;
                w = etiLasouX0 - nX;
            }
            if (nY > etiLasouY0) {
                y = etiLasouY0;
                h = nY - etiLasouY0;
            } else {
                y = nY;
                h = etiLasouY0 - nY;
            }
            etiLasou.setLocation(x, y);
            etiLasou.setSize(w, h);
            return;
        }
        for (Eti eti : lEti) {
            if (eti.isSelected()) {
                // deplasarile fata de ultima pozitie
                int dX = nX - eti.getMdX();
                int dY = nY - eti.getMdY();
                if (eti.isTypeBanda()) {
                    // benzi
                    if (eti.testInEti(nX, nY)) {
                        redimBenzi(eti, dY);
                        setModif(true);
                    } else {
                        eti.setSelected(false);
                    }
                } else {
                    // controale
                    if (mouseCursor == Cursor.HAND_CURSOR) {
                        eti.changeLocation(dX, dY);
                        setModif(true);
                    } else {
                        eti.mouseReSize(mouseCursor, dX, dY);
                        setModif(true);
                    }
                }
                // pune NOUA poz de referinta fata de care se misca mouse-ul
                eti.setMdX(nX);
                eti.setMdY(nY);
            }
        }
        // cand mouse ajunge la margine face autoscroll pentru a mentine cursorul in vedere
        Rectangle r = new Rectangle(nX, nY, 1, 1);
        this.scrollRectToVisible(r);
    }
//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="MouseListener">
    @Override
    public void mouseClicked(MouseEvent me) {
        switch (me.getClickCount()) {
            case 1:
                break;
            case 2:
                if (reportContent != null) {
                    for (Eti eti : lEti) {
                        if (!eti.isTypeBanda()) {
                            if (eti.testInEti(me.getX(), me.getY())) {
                                if (eti.isTypeText()) {
                                    ParamText pt = new ParamText(null, eti, reportContent);
                                    if (pt.result) {
                                        setModif(true);
                                        eti.setSursaText(pt.getSursaText());
                                        eti.setText(pt.getText());
                                        eti.setTextNumeric(pt.isNumeric());
                                        eti.setFont(pt.getTextFont());
                                        eti.setHorizontalAlignment(pt.getHAlign());
                                        eti.setVerticalAlignment(pt.getVAlign());
                                        eti.setForeground(pt.getSelectedColor());
                                        revalidate();
                                        repaint();
                                    }
                                } else {
                                    if (eti.isTypeLine()) {
                                        LineEti lineEti = (LineEti) eti;
                                        int x = lineEti.getRealX();
                                        int y = lineEti.getRealY();
                                        int w = lineEti.getRealWidth();
                                        int h = lineEti.getRealHeight();
                                        ParamLine pl = new ParamLine(null, lineEti);
                                        if (pl.result) {
                                            setModif(true);
                                            lineEti.setLineType(pl.getLineType());
                                            lineEti.setLineWidth(pl.getLineWidth());
                                            lineEti.setLineEnds(pl.getLineEnds());
                                            lineEti.setLineJoints(pl.getLineJoints());
                                            lineEti.setFlip(pl.isFlip());
                                            // redim la modif tip
                                            if (pl.getLineType() == LineEti.LineTypes.HORIZ) {
                                                h = 0;
                                            }
                                            if (pl.getLineType() == LineEti.LineTypes.VERT) {
                                                w = 0;
                                            }
                                            lineEti.setForeground(pl.getSelectedColor());
                                            //
                                            lineEti.setRealPosition(x, y, w, h);
                                            revalidate();
                                            repaint();
                                        }
                                    } else {
                                        // image
                                        ParamImage pi = new ParamImage(null, eti, reportContent);
                                        if (pi.result) {
                                            setModif(true);
                                            eti.setSursaText(pi.getSursaText());
                                            eti.setText(pi.getText());
                                            eti.setImageDrawMode(pi.getDrawMode());
                                            revalidate();
                                            repaint();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        int nX = (int) (me.getX() / coeficientScala);
        int nY = (int) (me.getY() / coeficientScala);
        // benzi
        for (Eti eti : lEti) {
            if (eti.testInEti(nX, nY)) {
                if (eti.isTypeBanda()) {
                    redimBanda = true;
                    deSelectAll();
                    eti.setSelected(true);
                    mouseCursor = Cursor.HAND_CURSOR;
                    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    if (eti.isSelected()) {
                        eti.setMdX(nX);
                        eti.setMdY(nY);
                    }
                }
            }
        }
        if (redimBanda) {
            return;
        }
        // controale
        boolean isOutEti = true;
        for (Eti eti : lEti) {
            if (eti.testInEti(nX, nY)) {
                isOutEti = false;
                if (me.isShiftDown()) {
                    // cu shift down comuta select
                    eti.setSelected(!eti.isSelected());
                } else {
                    // fara shift
                    switch (tipSelect()) {
                        case 0:
                            // nu e nimic selectat -> selecteaza acum
                            eti.setSelected(true);
                            mouseCursor = eti.overWhichHandler(nX, nY);
                            this.setCursor(new Cursor(mouseCursor));
                            break;
                        case 1:
                            // este o selectie
                            if (eti.isSelected()) {
                                // ea este cea selectata
                                mouseCursor = eti.overWhichHandler(nX, nY);
                                this.setCursor(new Cursor(mouseCursor));
                            } else {
                                // o alta e selectata -> o deselectez si o selectez pe a mea
                                deSelectAllOut(nX, nY);
                                eti.setSelected(true);
                                mouseCursor = eti.overWhichHandler(nX, nY);
                                this.setCursor(new Cursor(mouseCursor));
                            }
                            break;
                        case 2:
                            // exista deja multiselect -> afisaza manuta pentru a le deplasa
                            if (eti.isSelected()) {
                                mouseCursor = Cursor.HAND_CURSOR;
                                this.setCursor(new Cursor(Cursor.HAND_CURSOR));
                            } else {
                                // schimba selectia
                                deSelectAllOut(nX, nY);
                                eti.setSelected(true);
                                mouseCursor = eti.overWhichHandler(nX, nY);
                                this.setCursor(new Cursor(mouseCursor));
                            }
                            break;
                    }
                }
            }
            if (eti.isSelected()) {
                eti.setMdX(nX);
                eti.setMdY(nY);
            }
        }
        if (isOutEti) {
            // click in afara etichetelor -> deselecteaza tot
            deSelectAll();
            mouseCursor = Cursor.DEFAULT_CURSOR;
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            // incepe lasouSelect
            inLasou = true;
            etiLasouX0 = nX;
            etiLasouY0 = nY;
            etiLasou.setSize(0, 0);
            etiLasou.setLocation(nX, nY);
            this.add(etiLasou, 0);
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        if (redimBanda) {
            redimBanda = false;
            deSelectAll();
        }
        if (inLasou) {
            // selecteaza eti ce intra in intregime in lasou
            for (Eti eti : lEti) {
                eti.setSelected(etiInLasou(eti));
            }
            inLasou = false;
            this.remove(etiLasou);
            this.repaint();
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    private boolean etiInLasou(Eti eti) {
        if (eti.getY() > etiLasou.getY()) {
            if (eti.getY() + eti.getHeight() < etiLasou.getY() + etiLasou.getHeight()) {
                if (eti.getX() > etiLasou.getX()) {
                    if (eti.getX() + eti.getWidth() < etiLasou.getX() + etiLasou.getWidth()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //
    }
//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="KeyListener">
    @Override
    public void keyTyped(KeyEvent ke) {
        //JOptionPane.showMessageDialog(null, "Typed ke:"+ke.toString());
        //
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        //JOptionPane.showMessageDialog(null, "Pressed ke:"+ke.toString());
        int key = ke.getKeyCode();
        int dx = 0;
        int dy = 0;
        if (key == KeyEvent.VK_DELETE) {
            deleteSelectedComponents(true);
            return;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            inCut = false;
            return;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = keyStep;
        }
        if (key == KeyEvent.VK_LEFT) {
            dx = -keyStep;
        }
        if (key == KeyEvent.VK_UP) {
            dy = -keyStep;
        }
        if (key == KeyEvent.VK_DOWN) {
            dy += keyStep;
        }

        if (ke.isControlDown()) {
            if (key == KeyEvent.VK_C) {
                copySelectedComponents();
                return;
            }
            if (key == KeyEvent.VK_V) {
                setModif(true);
                pasteComponents(5, 5);
                return;
            }
            if (key == KeyEvent.VK_X) {
                cutSelectedComponents();
                return;
            }

        } else {
            if (ke.isShiftDown()) {
                for (int i = 0; i < lEti.size(); i++) {
                    Eti eti = lEti.get(i);
                    if (eti.isSelected()) {
                        setModif(true);
                        eti.keyReSize(dx, dy);
                    }
                }
            } else {
                for (Eti eti : lEti) {
                    if (eti.isSelected()) {
                        if (eti.isTypeBanda()) {
                            setModif(true);
                            redimBenzi(eti, dy);
                        } else {
                            setModif(true);
                            eti.changeLocation(dx, dy);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        //
        //JOptionPane.showMessageDialog(null, "Released ke:"+ke.toString());
    }
//</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Scrollable">
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
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
//</editor-fold>

    @Override
    public Dimension getPreferredSize() {
        // da dimensiunea imaginii paginii ce poate fi vizualizata la Preview
        if (reportContent == null) {
            return new Dimension(650, 400);// dimensiunile ferestrei la prima deschidere
        } else {
            return reportContent.getMyPreferredSize();
        }
    }

    private boolean redimBenzi(Eti etiSepBanda, int dY) {
        //setModif(true);
        // dY este valoarea cu care trebuie crescuta (+) sau redusa (-) largimea benzii
        if (etiSepBanda.isTypeBanda()) {
            LabelBand labelBand = etiSepBanda.getLabelBand();
            // conditie sa nu reduca prea mult dimensiunea benzii (initial >=0)
            int hMax = getBandMinHeight(labelBand);
            if (labelBand.getSize().getHeight() + dY >= hMax || dY > 0) {
                etiSepBanda.setLocation((int) etiSepBanda.getLocation().getX(), (int) etiSepBanda.getLocation().getY() + dY);
                labelBand.setSize((int) labelBand.getSize().getWidth(), (int) labelBand.getSize().getHeight() + dY);
                //
                // muta benzile de mai jos
                for (LabelBand labelBa : lBands) {
                    if (labelBa.getY() > labelBand.getY()) {
                        labelBa.setLocation((int) labelBa.getLocation().getX(), (int) labelBa.getLocation().getY() + dY);
                    }
                }
                page.setSize((int) page.getSize().getWidth(), (int) page.getSize().getHeight() + dY);
                //
                // y nivel referinta (partea de jos a barei de sub banda)
                double yRef = etiSepBanda.getLocation().getY() - dY + etiSepBanda.getHeight();
                // repozitioneaza etichetele
                for (Eti etic : lEti) {
                    // muta etichetele de mai jos de linie
                    //if (etic.getLocation().getY() > yRef) {
                    if (etic.getRealY() >= yRef) {
                        etic.setLocation((int) etic.getLocation().getX(), (int) etic.getLocation().getY() + dY);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private int getBandMinHeight(LabelBand lBand) {
        int hMin = 0;
        int etiMaxY;
        int yBottomEti;
        for (Eti etic : lEti) {
            // ia etichetele din banda
            if (!etic.isTypeBanda()) {
                //if (lBand.testInBand(etic.getY())) {
                yBottomEti = etic.getRealBottom();
                if (lBand.isHigherThenBand(yBottomEti)) {
                    //etiMaxY = etic.getY() + etic.getHeight() - lBand.getY();
                    etiMaxY = yBottomEti - lBand.getY();
                    if (etiMaxY > hMin) {
                        hMin = etiMaxY;
                    }
                }
            }
        }
        return hMin;
    }

    private void deSelectAll() {
        for (Eti eti : lEti) {
            if (eti.isSelected()) {
                eti.setSelected(false);
            }
        }
    }

    private void deSelectAllOut(int nX, int nY) {
        for (Eti eti : lEti) {
            if (eti.isSelected()) {
                if (eti.testInEti(nX, nY)) {

                } else {
                    eti.setSelected(false);
                }
            }
        }
    }

    private int tipSelect() {
        // 0= niciunul
        // 1= unul singur
        // 2= selectie multipla
        int nSelected = 0;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    nSelected++;
                }
            }
        }
        if (nSelected > 2) {
            nSelected = 2;
        }
        return nSelected;
    }

    // <editor-fold defaultstate="collapsed" desc="Copy, Paste, Delete">
    public void deleteSelectedComponents(boolean withConfirm) {
        if (tipSelect() > 0) {
            boolean doDelete = true;
            if (withConfirm) {
                Object[] options = {"Yes, delete", "No"};
                doDelete = (JOptionPane.showOptionDialog(this, "Confirm delete.", "Confirm dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == JOptionPane.YES_OPTION);
            }
            if (doDelete) {
                boolean deleted;
                do {
                    deleted = false;
                    for (Eti eti : lEti) {
                        if (eti.isSelected()) {
                            lEti.remove(eti);
                            this.remove(eti);
                            deleted = true;
                            break;
                        }
                    }
                } while (deleted);
                this.revalidate();
                this.repaint();
            }
        }
    }

    public void deleteCuttedComponents() {
        for (Eti eti : lCutEti) {
            lEti.remove(eti);
            this.remove(eti);
        }
        lCutEti = null;
    }

    public void cutSelectedComponents() {
        inCut = true;
        copySelectedComponents();
    }

    public void copySelectedComponents() {
        List<Eti> letiSel = new ArrayList<>();
        lCutEti = new ArrayList<>();
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    letiSel.add(eti);
                    if (inCut) {
                        lCutEti.add(eti);
                    }
                }
            }
        }
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        EtiSelection etiSelection = new EtiSelection(letiSel);
        clipboard.setContents(etiSelection, this);
        ownClipboard = true;
        setLeftUpSelectionPoint();
    }

    public void pasteComponents(int dx, int dy) {
        //
        if (ownClipboard) {
            if (inCut) {
                // delete components
                deleteCuttedComponents();
                inCut = false;
            } else {
                deSelectAll(); // deselecteaza etichetele vechi
            }
            //
            List<Eti> result;
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);
            if (contents == null) {
                // clipboard-ul este gol
            } else {
                boolean hasTransferableEti = contents.isDataFlavorSupported(EtiSelection.etiFlavor);
                if (hasTransferableEti) {
                    try {
                        result = (List<Eti>) contents.getTransferData(EtiSelection.etiFlavor);
                        if (result == null) {

                        } else {
                            for (Eti eti : result) {
                                eti.setLocation(eti.getX() + dx, eti.getY() + dy);
                                eti.setSelected(true);  // selecteaza etichetele noi
                                lEti.add(eti);
                                add(eti, 0);
                            }
                            this.revalidate();
                            this.repaint();
                        }
                    } catch (UnsupportedFlavorException | IOException ex) {
                        // EROARE - nu pune nimic
                    }
                }
            }
        }
    }

    private void setLeftUpSelectionPoint() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    if (eti.getX() < minX) {
                        minX = eti.getX();
                    }
                    if (eti.getY() < minY) {
                        minY = eti.getY();
                    }
                }
            }
        }
        if (minX == Integer.MAX_VALUE || minY == Integer.MAX_VALUE) {
            minX = 5;
            minY = 5;
        }
        leftUpSelectionPoint = new Point(minX, minY);
    }

    public Point getLeftUpSelectionPoint() {
        return leftUpSelectionPoint;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Align">
    private int getMinLeft() {
        int minX = Integer.MAX_VALUE;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    if (eti.getX() < minX) {
                        minX = eti.getX();
                    }
                }
            }
        }
        if (minX == Integer.MAX_VALUE) {
            minX = 0;
        }
        return minX;
    }

    private int getMinTop() {
        int minY = Integer.MAX_VALUE;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    if (eti.getY() < minY) {
                        minY = eti.getY();
                    }
                }
            }
        }
        if (minY == Integer.MAX_VALUE) {
            minY = 0;
        }
        return minY;
    }

    public void alignLeftSides() {
        int minX = getMinLeft();
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setLocation(minX, eti.getY());
                    setModif(true);
                }
            }
        }
    }

    public void alignRightSides() {
        // det poz max Right
        int maxX = 0;
        int xC;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    xC = eti.getX() + eti.getWidth();
                    if (xC > maxX) {
                        maxX = xC;
                    }
                }
            }
        }
        if (maxX == 0) {
            maxX = 150;
        }
        // set Right positions
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setLocation(maxX - eti.getWidth(), eti.getY());
                    setModif(true);
                }
            }
        }
    }

    public void alignTopEdges() {
        int minY = getMinTop();
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setLocation(eti.getX(), minY);
                    setModif(true);
                }
            }
        }
    }

    public void alignBottomEdges() {
        // det poz max Right
        int maxY = 0;
        int yC;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    yC = eti.getY() + eti.getHeight();
                    if (yC > maxY) {
                        maxY = yC;
                    }
                }
            }
        }
        if (maxY == 0) {
            maxY = 150;
        }
        // set Right positions
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setLocation(eti.getX(), maxY - eti.getHeight());
                    setModif(true);
                }
            }
        }
    }

    public void alignVerticalCenters() {
        // det poz max middle
        int maxY = 0;
        int yC;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    yC = eti.getY() + (int) (eti.getHeight() / 2);
                    if (yC > maxY) {
                        maxY = yC;
                    }
                }
            }
        }
        if (maxY == 0) {
            maxY = 150;
        }
        // set positions
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setLocation(eti.getX(), maxY - (int) (eti.getHeight() / 2));
                    setModif(true);
                }
            }
        }

    }

    public void alignHorizontalCenters() {
        // det poz max med Right
        int maxX = 0;
        int xC;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    xC = eti.getX() + (int) (eti.getWidth() / 2);
                    if (xC > maxX) {
                        maxX = xC;
                    }
                }
            }
        }
        if (maxX == 0) {
            maxX = 150;
        }
        // set positions
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setLocation((int) (maxX - eti.getWidth() / 2), eti.getY());
                    setModif(true);
                }
            }
        }

    }

    public void centerHorizontally() {
        if (tipSelect() == 1) {
            // caut eticheta selectata
            Eti selEti = null;
            for (Eti eti : lEti) {
                if (!eti.isTypeBanda()) {
                    if (eti.isSelected()) {
                        selEti = eti;
                        break;
                    }
                }
            }
            if (selEti != null) {
                // caut banda etichetei selectate
                for (LabelBand lBand : lBands) {
                    if (lBand.testInBand(selEti.getY())) {
                        selEti.setLocation((int) (lBand.getX() + (lBand.getWidth() - selEti.getWidth()) / 2), selEti.getY());
                        setModif(true);
                    }
                }
            }
        } else {
            //JOptionPane.showMessageDialog(this, "Center Horizontally is only for one control.");
        }
    }

    public void centerVertically() {
        if (tipSelect() == 1) {
            // caut eticheta selectata
            Eti selEti = null;
            for (Eti eti : lEti) {
                if (!eti.isTypeBanda()) {
                    if (eti.isSelected()) {
                        selEti = eti;
                        break;
                    }
                }
            }
            if (selEti != null) {
                // caut banda etichetei selectate
                for (LabelBand lBand : lBands) {
                    if (lBand.testInBand(selEti.getY())) {
                        selEti.setLocation(selEti.getX(), (int) (lBand.getY() + (lBand.getHeight() - selEti.getHeight()) / 2));
                        setModif(true);
                    }
                }
            }

        } else {
            //JOptionPane.showMessageDialog(this, "Center Vertically is only for one control.");
        }
    }

    public void toTallest() {
        int maxH = 0;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    if (eti.getHeight() > maxH) {
                        maxH = eti.getHeight();
                    }
                }
            }
        }

        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setSize(eti.getWidth(), maxH);
                    setModif(true);
                }
            }
        }
    }

    public void toShortest() {
        int minH = Integer.MAX_VALUE;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    if (eti.getHeight() < minH) {
                        minH = eti.getHeight();
                    }
                }
            }
        }

        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setSize(eti.getWidth(), minH);
                    setModif(true);
                }
            }
        }
    }

    public void toWidest() {
        int maxW = 0;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    if (eti.getWidth() > maxW) {
                        maxW = eti.getWidth();
                    }
                }
            }
        }

        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setSize(maxW, eti.getHeight());
                    setModif(true);
                }
            }
        }
    }

    public void toNarrowest() {
        int minW = Integer.MAX_VALUE;
        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    if (eti.getWidth() < minW) {
                        minW = eti.getWidth();
                        setModif(true);
                    }
                }
            }
        }

        for (Eti eti : lEti) {
            if (!eti.isTypeBanda()) {
                if (eti.isSelected()) {
                    eti.setSize(minW, eti.getHeight());
                }
            }
        }
    }

    public void bringToFront() {
        if (tipSelect() == 1) {
            // caut eticheta selectata
            Eti selEti = null;
            for (Eti eti : lEti) {
                if (!eti.isTypeBanda()) {
                    if (eti.isSelected()) {
                        selEti = eti;
                        break;
                    }
                }
            }
            if (selEti != null) {
                this.setComponentZOrder(selEti, 0);
                setModif(true);
                //this.invalidate();
                this.repaint();
            }
        } else {
            //JOptionPane.showMessageDialog(this, "Center Horizontally is only for one control.");
        }
    }

    public void sendToBack() {
        // nu merge calumea
        if (tipSelect() == 1) {
            // caut eticheta selectata
            Eti selEti = null;
            int pozZ = 0;
            int etiZ;
            for (Eti eti : lEti) {
                etiZ = eti.getComponentZOrder(eti);
                if (etiZ > pozZ) {
                    pozZ = etiZ;
                }
                if (!eti.isTypeBanda()) {
                    if (eti.isSelected()) {
                        selEti = eti;
                        //break;
                    }
                }
            }
            if (selEti != null) {
                // caut banda etichetei selectate
                this.setComponentZOrder(selEti, pozZ + 1);
                setModif(true);
                this.repaint();
            }
        } else {
            //JOptionPane.showMessageDialog(this, "Center Horizontally is only for one control.");
        }
    }
    // </editor-fold>

    @Override
    public void lostOwnership(Clipboard clpbrd, Transferable t) {
        ownClipboard = false; // a pierdut continutul Clipboard - altcineva a copiat ceva acolo
    }

    public boolean testModif() {
        // se executa la inchiderea documentului, a ferestrei, la load new, sau Load document
        // Salvez?
        if (modif) {
            Object[] options = {"Yes", "No", "Cancel"};
            int response = (JOptionPane.showOptionDialog(this, "Do you want to save the changes you made?", "Confirm dialog", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]));
            if (response == JOptionPane.CANCEL_OPTION) {
                return false;
            } else {
                if (response == JOptionPane.YES_OPTION) {
                    saveReportContent();
                }
                return true;
            }
        } else {
            return true;
        }
    }
}
