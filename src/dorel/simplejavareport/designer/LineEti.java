package dorel.simplejavareport.designer;

import dorel.simplejavareport.report.components.RapLine;
import dorel.simplejavareport.tools.PaintComp;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
//import javax.swing.JOptionPane;

public class LineEti extends Eti {

    public enum LineTypes {

        HORIZ,
        VERT,
        DIAG,
        RECT
    }

    public LineEti() {
        super(null);
    }

    private float lineWidth;
    private boolean flip;
    protected LineTypes lineType;
    private int lineEnds;
    private int lineJoints;

    // <editor-fold defaultstate="collapsed" desc="Get Set">
    public void setLineJoints(int lineJoints){
        if (lineJoints==BasicStroke.JOIN_BEVEL || lineJoints==BasicStroke.JOIN_MITER || lineJoints==BasicStroke.JOIN_ROUND){
            this.lineJoints=lineJoints;
        } else {
            this.lineJoints=BasicStroke.JOIN_MITER;
        }
    }

    public int getLineJoints(){
        return lineJoints;
    }

    public void setLineEnds(int lineEnds){
        if (lineEnds==BasicStroke.CAP_SQUARE || lineEnds==BasicStroke.CAP_BUTT || lineEnds==BasicStroke.CAP_ROUND){
            this.lineEnds=lineEnds;
        } else {
            this.lineEnds=BasicStroke.CAP_SQUARE;
        }
    }

    public int getLineEnds(){
        return lineEnds;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setLineType(LineTypes lineType) {
        this.lineType = lineType;
    }

    public LineTypes getLineType() {
        return lineType;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        this.calculezConturExt();  // seteaza nr de pixeli cu care eti va fi mai mare (in toate directiile) decat linia
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setTypeFlip(boolean isRect, int h, int w, int y2, int y1) {
        LineEti.LineTypes etLineType;
        if (isRect) {
            etLineType = LineEti.LineTypes.RECT;
        } else {
            if (w == 0) {
                etLineType = LineEti.LineTypes.VERT;
            } else {
                if (h == 0) {
                    etLineType = LineEti.LineTypes.HORIZ;
                } else {
                    etLineType = LineEti.LineTypes.DIAG;
                    if (y2 > y1) {
                        setFlip(false);
                    } else {
                        setFlip(true);
                    }
                }
            }
        }
        setLineType(etLineType);
    }
    // </editor-fold>

    public LineEti(String string) {
        super(string);
        lineWidth = 1F;
        flip = false;
        lineType = LineTypes.DIAG;
        lineEnds = BasicStroke.CAP_SQUARE;
        lineJoints = BasicStroke.JOIN_MITER;
    }

    // <editor-fold defaultstate="collapsed" desc="Override">
    @Override
    public boolean isTypeLine() {
        return true;
    }

    @Override
    public void paint(Graphics g) {
        // paint etich in Design mode
        Graphics2D g2 = (Graphics2D) g;
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        boolean isRect = false;
        switch (lineType) {
            case VERT:
                x1 = getLocalX();
                x2 = x1;
                y1 = getLocalY();
                y2 = getLocalY() + getRealHeight();
                break;
            case HORIZ:
                x1 = getLocalX();
                x2 = getLocalX() + getRealWidth();
                y1 = getLocalY();
                y2 = y1;
                break;
            case DIAG:
                x1 = getLocalX();
                x2 = getLocalX() + getRealWidth();
                if (flip) {
                    y1 = getLocalY() + getRealHeight();
                    y2 = getLocalY();
                } else {
                    y1 = getLocalY();
                    y2 = getLocalY() + getRealHeight();
                }
                break;
            case RECT:
                isRect = true;
                x1 = getLocalX();
                x2 = getLocalX() + getRealWidth();
                y1 = getLocalY();
                y2 = getLocalY() + getRealHeight();
                break;
        }
        PaintComp.paintLine(g2, x1, y1, x2, y2, lineWidth, isRect, getForeground(), lineEnds, lineJoints, true);
        //
        // add Handlers
        if (selected) {
            lHandlers = new ArrayList<>();
            switch (lineType) {
                case HORIZ:
                    lHandlers.add(new EtiHandler(getLocalX(), getLocalY(), Cursor.W_RESIZE_CURSOR));
                    lHandlers.add(new EtiHandler(getLocalX() + getRealWidth(), getLocalY(), Cursor.E_RESIZE_CURSOR));
                    break;
                case VERT:
                    lHandlers.add(new EtiHandler(getLocalX(), getLocalY(), Cursor.N_RESIZE_CURSOR));
                    lHandlers.add(new EtiHandler(getLocalX(), getLocalY() + getRealHeight(), Cursor.S_RESIZE_CURSOR));
                    break;
                case DIAG:
                    if (flip) {
                        lHandlers.add(new EtiHandler(getLocalX() + getRealWidth(), getLocalY(), Cursor.NE_RESIZE_CURSOR));
                        lHandlers.add(new EtiHandler(getLocalX(), getLocalY() + getRealHeight(), Cursor.SW_RESIZE_CURSOR));
                    } else {
                        lHandlers.add(new EtiHandler(getLocalX(), getLocalY(), Cursor.NW_RESIZE_CURSOR));
                        lHandlers.add(new EtiHandler(getLocalX() + getRealWidth(), getLocalY() + getRealHeight(), Cursor.SE_RESIZE_CURSOR));
                    }
                    break;
                case RECT:
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
                    break;
            }
            PaintComp.paintHandlers(g2, lHandlers);
        } else {
            lHandlers = new ArrayList<>();
        }
    }

    @Override
    public boolean testInEti(int px, int py) {
        int dXY = conturExt + 1;
        switch (lineType) {
            case HORIZ:
                return (getX() <= px && px <= getX() + getWidth()) && (getY() <= py && py <= getY() + getHeight());
            case VERT:
                return (getX() <= px && px <= getX() + getWidth()) && (getY() <= py && py <= getY() + getHeight());
            case DIAG:
                if ((getX() <= px && px <= getX() + getWidth()) && (getY() <= py && py <= getY() + getHeight())) {
                    double yLinie;
                    double rh = (double) getRealHeight();
                    double rw = (double) getRealWidth();
                    if (rh < dXY || rw < dXY) {
                        return true;
                    } else {
                        double panta = rh / rw;
                        if (flip) {
                            yLinie = (getRealY() + getRealHeight()) - panta * (px - getRealX());
                        } else {
                            yLinie = getRealY() + panta * (px - getRealX());
                        }
                        if (Math.abs(yLinie - py) < dXY) {
                            return true;
                        }
                    }
                }
                break;
            case RECT:
                if (getX() <= px && px <= getX() + getWidth()) {
                    if ((getY() <= py && py <= getY() + 2 * dXY) || (getY() + getHeight() - 2 * dXY <= py && py <= getY() + getHeight())) {
                        return true;
                    }
                }
                if (getY() <= py && py <= getY() + getHeight()) {
                    if ((getX() <= px && px <= getX() + 2 * dXY) || (getX() + getWidth() - 2 * dXY <= px && px <= getX() + getWidth())) {
                        return true;
                    }
                }
        }
        return false;
    }
    // </editor-fold>

    private void calculezConturExt() {
        conturExt = 0;
        if (lineWidth > 0) {
            conturExt = (int) Math.floor(lineWidth / 2);
        } else {
            conturExt = 0;
        }
        if (conturExt < 2) {
            margineTop = 2 - conturExt;
            conturExt = 2;
        } else {
            margineTop = 0;  // la liniile groase nu am margine sus
        }
    }

    public RapLine getRapLine(int offsetX, int offsetY) {
        RapLine rl = new RapLine();
        rl.setLineWidth(getLineWidth());
        rl.setColor(getForeground());
        rl.setLineEnds(lineEnds);
        rl.setLineJoints(lineJoints);
        int newX = getRealX() - offsetX;
        int newY = getRealY() - offsetY;
        int newW = getRealWidth();
        int newH = getRealHeight();
        switch (lineType) {
            case VERT:
                rl.setXY(newX, newY, newX, newY + newH);
                rl.setRect(false);
                break;
            case HORIZ:
                rl.setXY(newX, newY, newX + newW, newY);
                rl.setRect(false);
                break;
            case DIAG:
                if (!flip) {
                    rl.setXY(newX, newY, newX + newW, newY + newH);
                } else {
                    rl.setXY(newX, newY + newH, newX + newW, newY);
                }
                rl.setRect(false);
                break;
            case RECT:
                rl.setXY(newX, newY, newX + newW, newY + newH);
                rl.setRect(true);
                break;
        }
        return rl;
    }
}
