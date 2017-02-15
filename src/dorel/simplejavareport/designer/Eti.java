package dorel.simplejavareport.designer;

import dorel.simplejavareport.report.components.RapImage;
import dorel.simplejavareport.report.components.RapText;
import dorel.simplejavareport.tools.PaintComp;
import dorel.simplejavareport.tools.SursaInfo;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Eti extends JLabel implements Serializable {

    public enum Types {

        TYPE_BANDA,
        TYPE_LINE,
        TYPE_TEXT,
        TYPE_IMAGE
    }

    protected List<EtiHandler> lHandlers;
    //
    private Types type;
    protected boolean selected = false;
    private LabelBand labelBand = null;
    //
    protected int conturExt;
    protected int margineTop = 2;
    private int mdX = 0;
    private int mdY = 0;
    //
    // Text
    private SursaInfo.Sursa text_sursaText;  // =0 text; =1 ia textul din lCommon; =2 ia textul din lDate
    private boolean text_eNumeric = false;
    //
    // Image
    private RapImage.DrawMode image_drawMode;

    // <editor-fold defaultstate="collapsed" desc="Get Set">
    public void setImageDrawMode(RapImage.DrawMode image_drawMode) {
        this.image_drawMode = image_drawMode;
    }

    public RapImage.DrawMode getImageDrawMode() {
        return image_drawMode;
    }

    public void setLabelBand(LabelBand labelBand) {
        this.labelBand = labelBand;
    }

    public LabelBand getLabelBand() {
        return labelBand;
    }

    public boolean isTypeBanda() {
        return type == Types.TYPE_BANDA;
    }

    public void setTypeBanda(Dimension sepSize) {
        this.type = Types.TYPE_BANDA;
        setSize(sepSize);
        setHorizontalAlignment(JLabel.CENTER);
        setBackground(Color.lightGray);
        setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        this.repaint();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setRealPosition(int x, int y, int w, int h) {
        setLocation(x - conturExt, y - conturExt);
        setSize(w + 2 * conturExt + 1, h + 2 * conturExt + 1);
    }

    public int getRealX() {
        return getX() + getLocalX();
    }

    public int getRealY() {
        return getY() + getLocalY();
    }

    public int getLocalX() {
        return conturExt;
    }

    public int getLocalY() {
        return conturExt;
    }

    public int getRealWidth() {
        return getWidth() - 2 * conturExt - 1;
    }

    public int getRealHeight() {
        return getHeight() - 2 * conturExt - 1;
    }

    public int getRealTop() {
        return getY() + margineTop;
    }

    public int getRealBottom() {
        return getRealY() + getRealHeight();
    }

    // Text
    public boolean isTypeText() {
        return type == Types.TYPE_TEXT;
    }

    public void setTypeText() {
        this.type = Types.TYPE_TEXT;
    }

    public void setSursaText(SursaInfo.Sursa text_sursaText) {
        this.text_sursaText = text_sursaText;
    }

    public SursaInfo.Sursa getSursaText() {
        return text_sursaText;
    }

    public void setTextNumeric(boolean text_eNumeric) {
        this.text_eNumeric = text_eNumeric;
    }

    public boolean isTextNumeric() {
        return text_eNumeric;
    }

    // Line
    public boolean isTypeLine() {
        return false;
    }

    // Picture
    public boolean isTypeImage() {
        return type == Types.TYPE_IMAGE;
    }

    public void setTypeImage() {
        this.type = Types.TYPE_IMAGE;
    }

    public void setMdX(int mdX) {
        this.mdX = mdX;
    }

    public void setMdY(int mdY) {
        this.mdY = mdY;
    }

    public int getMdX() {
        return mdX;
    }

    public int getMdY() {
        return mdY;
    }
    //</editor-fold>

    public Eti(String string) {
        super(string);
        conturExt = 2;
        lHandlers = new ArrayList<>();
        image_drawMode=RapImage.DrawMode.CLIP_CONTENTS;
    }

    public void keyReSize(int dx, int dy) {
        setSize(getWidth() + dx, getHeight() + dy);
    }

    public void changeLocation(int dX, int dY) {
        int x = getX();
        int y = getY();
        if (type == Types.TYPE_BANDA) {
            setLocation(0, y + dY);
        } else {
            setLocation(x + dX, y + dY);
        }
    }

    private void myResize(int newX, int newY, int newW, int newH) {
        // limitez dim minima a etichetelor la conturExt * 2
        int dimMin = conturExt * 2;
        if (newW > dimMin && newH > dimMin) {
            setSize(newW, newH);
            setLocation(newX, newY);
        }
    }

    public void mouseReSize(int mouseCursor, int dX, int dY) {
        int x = getX();
        int y = getY();
        int w = getWidth();
        int h = getHeight();
        switch (mouseCursor) {
            case Cursor.E_RESIZE_CURSOR:
                myResize(x, y, w + dX, h);
                break;
            case Cursor.W_RESIZE_CURSOR:
                myResize(x + dX, y, w - dX, h);
                break;
            case Cursor.S_RESIZE_CURSOR:
                myResize(x, y, w, h + dY);
                break;
            case Cursor.N_RESIZE_CURSOR:
                myResize(x, y + dY, w, h - dY);
                break;
            case Cursor.NE_RESIZE_CURSOR:
                myResize(x, y + dY, w + dX, h - dY);
                break;
            case Cursor.SE_RESIZE_CURSOR:
                myResize(x, y, w + dX, h + dY);
                break;
            case Cursor.NW_RESIZE_CURSOR:
                myResize(x + dX, y + dY, w - dX, h - dY);
                break;
            case Cursor.SW_RESIZE_CURSOR:
                myResize(x + dX, y, w - dX, h + dY);
                break;
        }
    }

    public boolean testInEti(int px, int py) {
        return (px >= getRealX() && px <= (getRealX() + getRealWidth())) && (py >= getRealY() && py <= (getRealY() + getRealHeight()));
    }

    @Override
    public void paint(Graphics g) {
        // paint etich in Design mode
        Graphics2D g2 = (Graphics2D) g;
        switch (type) {
            case TYPE_TEXT:
                Rectangle rect = new Rectangle(getLocalX(), getLocalY(), getRealWidth(), getRealHeight());
                PaintComp.paintText(g, getText(), rect, getFont(), this.getForeground(), getHorizontalAlignment(), getVerticalAlignment(), text_eNumeric, true);
                break;
            case TYPE_BANDA:
                super.paint(g);
                break;
            case TYPE_IMAGE:
                PaintComp.paintImage(g2, getText(), image_drawMode, getRealWidth(), getRealHeight(), getLocalX(), getLocalY(), true);
                break;
        }
        // add handlers
        if (selected) {
            lHandlers = new ArrayList<>();
            if (type != Types.TYPE_BANDA) {
                // colturi
                lHandlers.add(new EtiHandler(getLocalX() + getRealWidth(), getLocalY(), Cursor.NE_RESIZE_CURSOR));
                lHandlers.add(new EtiHandler(getLocalX(), getLocalY() + getRealHeight(), Cursor.SW_RESIZE_CURSOR));
                lHandlers.add(new EtiHandler(getLocalX(), getLocalY(), Cursor.NW_RESIZE_CURSOR));
                lHandlers.add(new EtiHandler(getLocalX() + getRealWidth(), getLocalY() + getRealHeight(), Cursor.SE_RESIZE_CURSOR));
                // mijloace
                lHandlers.add(new EtiHandler(getLocalX(), getLocalY() + getRealHeight() / 2, Cursor.W_RESIZE_CURSOR));
                lHandlers.add(new EtiHandler(getLocalX() + getRealWidth(), getLocalY() + getRealHeight() / 2, Cursor.E_RESIZE_CURSOR));
                lHandlers.add(new EtiHandler(getLocalX() + getRealWidth() / 2, getLocalY(), Cursor.N_RESIZE_CURSOR));
                lHandlers.add(new EtiHandler(getLocalX() + getRealWidth() / 2, getLocalY() + getRealHeight(), Cursor.S_RESIZE_CURSOR));
            }
            PaintComp.paintHandlers(g2, lHandlers);
        } else {
            lHandlers = new ArrayList<>();
        }

    }

    public int overWhichHandler(int xm, int ym) {
        int raspuns = Cursor.HAND_CURSOR;
        for (EtiHandler h : lHandlers) {
            int curs = h.isMouseOver(xm - getX(), ym - getY());
            if (curs > -1) {
                raspuns = curs;
            }
        }
        return raspuns;
    }

    public RapText getRapText(int offsetX, int offsetY) {
        RapText rt = new RapText();
        if (isTypeText()) {
            rt.setSursaText(getSursaText());
            rt.setText(getText());
            rt.setTextNumeric(isTextNumeric());

            rt.setFont(getFont());
            rt.setColor(getForeground());

            rt.setHalign(getHorizontalAlignment());
            rt.setValign(getVerticalAlignment());

            rt.setX(getRealX() - offsetX);
            rt.setY(getRealY() - offsetY);
            rt.setW(getRealWidth());
            rt.setH(getRealHeight());
        }
        return rt;
    }

    public RapImage getRapImage(int offsetX, int offsetY) {
        RapImage ri = new RapImage();
        if (isTypeImage()) {
            ri.setSursaText(text_sursaText);
            ri.setText(getText());
            ri.setX(getRealX() - offsetX);
            ri.setY(getRealY() - offsetY);
            ri.setW(getRealWidth());
            ri.setH(getRealHeight());
            ri.setDrawMode(image_drawMode);
        }
        return ri;
    }
}
