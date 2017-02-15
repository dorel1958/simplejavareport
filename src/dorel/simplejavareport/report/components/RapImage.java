package dorel.simplejavareport.report.components;

import dorel.simplejavareport.designer.Eti;
import dorel.simplejavareport.tools.PaintComp;
import dorel.simplejavareport.tools.SursaInfo;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RapImage {

    SursaInfo.Sursa sursaText;
    private String text;
    private String printedText;
    private int x;
    private int y;
    private int w;
    private int h;
    private DrawMode drawMode;

    //<editor-fold defaultstate="collapsed" desc="Draw mode">
    public enum DrawMode {

        CLIP_CONTENTS,
        SCALE_FILL_THE_FRAME,
        SCALE_RETAIN_SHAPE
    }

    public static String drawModeToString(DrawMode drawMode) {
        switch (drawMode) {
            case CLIP_CONTENTS:
                return "CLIP_CONTENTS";
            case SCALE_FILL_THE_FRAME:
                return "SCALE_FILL_THE_FRAME";
            case SCALE_RETAIN_SHAPE:
                return "SCALE_RETAIN_SHAPE";
        }
        return "CLIP_CONTENTS";
    }

    public static DrawMode stringToDrawMode(String drawMode) {
        switch (drawMode) {
            case "CLIP_CONTENTS":
                return DrawMode.CLIP_CONTENTS;
            case "SCALE_FILL_THE_FRAME":
                return DrawMode.SCALE_FILL_THE_FRAME;
            case "SCALE_RETAIN_SHAPE":
                return DrawMode.SCALE_RETAIN_SHAPE;
        }
        return DrawMode.CLIP_CONTENTS;
    }

    public static List<String> getDrawModes() {
        List<String> drawModes = new ArrayList<>();
        drawModes.add("CLIP_CONTENTS");
        drawModes.add("SCALE_FILL_THE_FRAME");
        drawModes.add("SCALE_RETAIN_SHAPE");
        return drawModes;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Get Set">
    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public void setSursaText(SursaInfo.Sursa sursaText) {
        this.sursaText = sursaText;
    }

    public SursaInfo.Sursa getSursaText() {
        if (sursaText == null) {
            return SursaInfo.Sursa.TEXT;
        }
        return sursaText;
    }

    public void setPrintedText(String printedNumeFis) {
        this.printedText = printedNumeFis;
    }

    public String getPrintedText() {
        return printedText;
    }

    public void setText(String numeFis) {
        this.text = numeFis;
    }

    public String getText() {
        return text;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }
    //</editor-fold>

    public RapImage() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        text = "";
        drawMode = DrawMode.CLIP_CONTENTS;
    }

    public void draw(Graphics g, int width, int height, int translateX, int translateY) {
        Graphics2D g2 = (Graphics2D) g;
        PaintComp.paintImage(g2, printedText, drawMode, w, h, x + translateX, y + translateY, false);
    }

    public Element getElementXML(Document doc) {
        Element elem = doc.createElement("image");
        elem.setAttribute("sursaText", SursaInfo.getStringSursa(sursaText));
        elem.setAttribute("numeFis", String.valueOf(text));
        elem.setAttribute("drawMode", String.valueOf(drawModeToString(drawMode)));
        elem.setAttribute("x", String.valueOf(x));
        elem.setAttribute("y", String.valueOf(y));
        elem.setAttribute("w", String.valueOf(w));
        elem.setAttribute("h", String.valueOf(h));
        return elem;
    }

    public void setElementXML(Element elem) {
        sursaText = SursaInfo.getSursaString(elem.getAttribute("sursaText"));
        text = elem.getAttribute("numeFis");
        drawMode = stringToDrawMode(elem.getAttribute("drawMode"));
        x = Integer.parseInt(elem.getAttribute("x"));
        y = Integer.parseInt(elem.getAttribute("y"));
        w = Integer.parseInt(elem.getAttribute("w"));
        h = Integer.parseInt(elem.getAttribute("h"));
    }

    public Eti getEti(int topY_pix, int leftMargin_pix) {
        Eti eti = new Eti(text);
        eti.setSursaText(sursaText);
        eti.setRealPosition(x + leftMargin_pix, y + topY_pix, w, h);
        eti.setTypeImage();
        eti.setImageDrawMode(drawMode);
        return eti;
    }

}
