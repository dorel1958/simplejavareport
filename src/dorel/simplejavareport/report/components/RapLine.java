package dorel.simplejavareport.report.components;

import dorel.simplejavareport.designer.LineEti;
import dorel.simplejavareport.tools.Calc;
import dorel.simplejavareport.tools.PaintComp;
import dorel.simplejavareport.tools.Utils;
import java.awt.Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RapLine {

    //static double MM_PUNCTE_PRINT = 2.834645669;
    //double factorTransf=MM_PUNCTE_PRINT;
    //double factorTransf = 1;
    //
    //public static int minDimension = 10;
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private boolean isRect;
    private Color color;  // pentru cand voi lucra colorat
    private float lineWidth;
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
    
    public void setRect(boolean isRect) {
        this.isRect = isRect;
    }
    
    public boolean getRect() {
        return isRect;
    }
    
    public void setXY(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }
    
    public float getlineWidth() {
        return lineWidth;
    }
    
    public void setX1(int x1) {
        this.x1 = x1;
    }
    
    public int getX1() {
        return x1;
    }
    
    public int getX() {
        return Math.min(x1, x2);
    }
    
    public int getY() {
        return Math.min(y1, y2);
    }
    
    public int getWidth() {
        return Math.abs(x1 - x2);
    }
    
    public int getHeight() {
        return Math.abs(y1 - y2);
    }
    
    public void setY1(int y1) {
        this.y1 = y1;
    }
    
    public double getY1() {
        return y1;
    }
    
    public void setX2(int x2) {
        this.x2 = x2;
    }
    
    public int getX2() {
        return x2;
    }
    
    public void setY2(int y2) {
        this.y2 = y2;
    }
    
    public int getY2() {
        return y2;
    }
    //</editor-fold>

    public RapLine() {
        lineWidth = 1;
        color = Color.black;
        isRect = false;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 0;
        this.y2 = 0;
        lineEnds = BasicStroke.CAP_SQUARE;
        lineJoints = BasicStroke.JOIN_MITER;
    }
    
    public void draw(Graphics g, int width, int height, int translateX, int translateY) {
        PaintComp.paintLine((Graphics2D)g, x1+translateX, y1+translateY, x2+translateX, y2+translateY, lineWidth, isRect, color, lineEnds, lineJoints, isRect);
    }
    
    public Element getElementXML(Document doc) {
        Element elem = doc.createElement("line");
        elem.setAttribute("x1", String.valueOf(x1));
        elem.setAttribute("y1", String.valueOf(y1));
        elem.setAttribute("x2", String.valueOf(x2));
        elem.setAttribute("y2", String.valueOf(y2));
        elem.setAttribute("isRect", Utils.booleanToString(isRect));
        //
        elem.setAttribute("lineWidth", String.valueOf(lineWidth));
        elem.setAttribute("color", color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha());
        elem.setAttribute("lineEnds", String.valueOf(lineEnds));
        elem.setAttribute("lineJoints", String.valueOf(lineJoints));
        return elem;
    }
    
    public void setElementXML(Element elem) {
        //lineType = elem.getAttribute("lineType");
        x1 = Integer.parseInt(elem.getAttribute("x1"));
        y1 = Integer.parseInt(elem.getAttribute("y1"));
        x2 = Integer.parseInt(elem.getAttribute("x2"));
        y2 = Integer.parseInt(elem.getAttribute("y2"));
        isRect = elem.getAttribute("isRect").equals("true");
        //
        lineWidth = Float.parseFloat(elem.getAttribute("lineWidth"));
        //
        String sColor = elem.getAttribute("color");
        String[] asColor = sColor.split(",");
        if (asColor.length == 4) {
            if (Calc.isInteger(asColor[0]) && Calc.isInteger(asColor[1]) && Calc.isInteger(asColor[2]) && Calc.isInteger(asColor[3])) {
                int r = Integer.parseInt(asColor[0]);
                int g = Integer.parseInt(asColor[1]);
                int b = Integer.parseInt(asColor[2]);
                int a = Integer.parseInt(asColor[3]);
                if (0 <= r && r <= 255 && 0 <= g && g <= 255 && 0 <= b && b <= 255 && 0 <= a && a <= 255) {
                    color = new Color(r, g, b, a);
                } else {
                    color = Color.black;
                }
            } else {
                color = Color.black;
            }
        } else {
            color = Color.black;
        }
        lineEnds = Integer.parseInt(elem.getAttribute("lineEnds"));
        lineJoints = Integer.parseInt(elem.getAttribute("lineJoints"));
    }
    
    public LineEti getLineEti(int topY_pix, int leftMargin_pix) {
        LineEti lineEti = new LineEti("");
        lineEti.setForeground(color);
        lineEti.setLineWidth(lineWidth);
        lineEti.setTypeFlip(isRect, getHeight(), getWidth(), y2, y1);
        lineEti.setLineEnds(lineEnds);
        lineEti.setLineJoints(lineJoints);
        // inainte de ea TREBUIE setate: setLineWidth!!!
        lineEti.setRealPosition(getX() + leftMargin_pix, getY() + topY_pix, getWidth(), getHeight());
        return lineEti;
    }
    
}
