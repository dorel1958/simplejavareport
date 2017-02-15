package dorel.simplejavareport.report.components;
//http://docs.oracle.com/javase/tutorial/2d/

import dorel.simplejavareport.designer.Eti;
import dorel.simplejavareport.tools.Calc;
import dorel.simplejavareport.tools.PaintComp;
import dorel.simplejavareport.tools.SursaInfo;
import dorel.simplejavareport.tools.Utils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JLabel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class RapText {

    //private boolean deseneazaConturText = true;
    //static double MM_PUNCTE_PRINT = 2.834645669;
    //double factorTransf=MM_PUNCTE_PRINT;
    //double factorTransf = 1;
    //
//    public static int SURSA_TEXT_TEXT = 0;
//    public static int SURSA_TEXT_COMMON = 1;
//    public static int SURSA_TEXT_DATA = 2;
//    public static int SURSA_TEXT_REPORT = 3;
    //
    int x;
    int y;
    int w;
    int h;
    int hAlign;
    int vAlign;
    Font font;
    Color color;
    //
    SursaInfo.Sursa sursaText;
    String text;  // textul, key in common sau numele coloanei
    boolean numeric;  // daca e numeric nu poate sa taie din el daca nu incape - afisaza eronat
    private String printedText;

    // <editor-fold defaultstate="collapsed" desc="Get Set">
    public void setPrintedText(String printedText) {
        this.printedText = printedText;
    }

    // Text
    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }

    public Color getColor() {
        return color;
    }

    public SursaInfo.Sursa getSursaText() {
        return sursaText;
    }

    public void setSursaText(SursaInfo.Sursa sursaText) {
        this.sursaText = sursaText;
    }

    public void setText(String textul) {
        this.text = textul;
    }

    public String getText() {
        return text;
    }

    public void setTextNumeric(boolean eNumeric) {
        this.numeric = eNumeric;
    }

    public boolean isTextNumeric() {
        return numeric;
    }

    public void setHalign(int hAlign) {
        this.hAlign = hAlign;
    }

    public void setValign(int vAlign) {
        this.vAlign = vAlign;
    }

    public int getX() {
        return x;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }
//    public int getX_pix() {
//        return (int) (x * ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public int getY_pix() {
//        return (int) (y * ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public int getW_pix() {
//        return (int) (w * ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public int getH_pix() {
//        return (int) (h * ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public void setX_pix(int nPix) {
//        x = (int) (nPix / ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public void setY_pix(int nPix) {
//        y = (int) (nPix / ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public void setW_pix(int nPix) {
//        w = (int) (nPix / ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public void setH_pix(int nPix) {
//        h = (int) (nPix / ReportContent.MM_PUNCTE_SCREEN);
//    }
    //</editor-fold>

    public RapText() {
        x = 0;
        y = 0;
        w = 0;
        h = 0;
        hAlign = JLabel.LEFT;
        vAlign = JLabel.CENTER;
        sursaText = SursaInfo.Sursa.TEXT;
        text = "";
        numeric = false;
        font = new Font("SERIF", Font.PLAIN, 10);
    }

//    public void draw(Graphics g, int width, int height, int translateX, int translateY) {
//        if (printedText == null) {
//            return;
//        }
//        if (printedText.length() == 0) {
//            return;
//        }
//        // double width_mm, double height_mm = limitele benzii in care deseneaza
//        // daca nu se incadreaza in banda, IL TRANSLATEAZA sa incapa
//        //int width_ug = (int) (width * factorTransf);
//        //int height_ug = (int) (height * factorTransf);
//
//        // 
//        int x_ug = (int) (x * factorTransf);
//        int y_ug = (int) (y * factorTransf);
//        int w_ug = (int) (w * factorTransf);
//        int h_ug = (int) (h * factorTransf);
//
//        int pozX_ug = x_ug;
//        int pozY_ug = y_ug;
////        if (x_ug < 0) {
////            pozX_ug = 0;
////        }
////        if (x_ug + w_ug > width_ug) {
////            pozX_ug = width_ug - w_ug;
////        }
////        if (y_ug < 0) {
////            pozY_ug = 0;
////        }
////        if (y_ug + h_ug > height_ug) {
////            pozY_ug = height_ug - h_ug;
////        }
//        //
//        Rectangle rect_ug = new Rectangle(pozX_ug + (int) (translateX * factorTransf), pozY_ug + (int) (translateY * factorTransf), w_ug, h_ug);
//        PaintComp.paintText(g, printedText, rect_ug, getFont(), color, hAlign, vAlign, numeric, false);
//    }
    public void draw(Graphics g, int width, int height, int translateX, int translateY) {
        if (printedText == null) {
            return;
        }
        if (printedText.length() == 0) {
            return;
        }
        int pozX = x;
        int pozY = y;
        if (x < 0) {
            pozX = 0;
        }
        if (x + w > width) {
            pozX = width - w;
        }
        if (y < 0) {
            pozY = 0;
        }
        if (y + h > height) {
            pozY = height - h;
        }
        //
        Rectangle rect_ug = new Rectangle(pozX + translateX, pozY + translateY, w, h);
        PaintComp.paintText(g, printedText, rect_ug, getFont(), color, hAlign, vAlign, numeric, false);
    }

    public Element getElementXML(Document doc) {
        Element elem = doc.createElement("text");
        elem.setAttribute("sursaText", String.valueOf(sursaText));
        elem.setAttribute("text", text);
        elem.setAttribute("numeric", Utils.booleanToString(numeric));
        elem.setAttribute("x", String.valueOf(x)); //  String.valueOf((double) Math.round(100 * x) / 100));
        elem.setAttribute("y", String.valueOf(y)); //String.valueOf((double) Math.round(100 * y) / 100));
        elem.setAttribute("w", String.valueOf(w)); //String.valueOf((double) Math.round(100 * w) / 100));
        elem.setAttribute("h", String.valueOf(h)); //String.valueOf((double) Math.round(100 * h) / 100));
        elem.setAttribute("horizAlign", hAlignToText(hAlign));
        elem.setAttribute("vertAlign", vAlignToText(vAlign));

        elem.setAttribute("fontFamily", font.getFamily());
        String sFontStyle;
        switch (font.getStyle()) {
            case Font.PLAIN:
                sFontStyle = "PLAIN";
                break;
            case Font.BOLD:
                sFontStyle = "BOLD";
                break;
            case Font.ITALIC:
                sFontStyle = "ITALIC";
                break;
            case Font.BOLD + Font.ITALIC:
                sFontStyle = "BOLDITALIC";
                break;
            default:
                sFontStyle = "PLAIN";
        }
        elem.setAttribute("fontStyle", sFontStyle);
        elem.setAttribute("fontSize", String.valueOf(font.getSize()));
        elem.setAttribute("color", color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha());
        return elem;
    }

    public void setElementXML(Element elem) {
        sursaText = SursaInfo.getSursaString(elem.getAttribute("sursaText"));
        text = elem.getAttribute("text");
        numeric = elem.getAttribute("numeric").equals("true");
        //
        x = Integer.parseInt(elem.getAttribute("x"));
        y = Integer.parseInt(elem.getAttribute("y"));
        w = Integer.parseInt(elem.getAttribute("w"));
        h = Integer.parseInt(elem.getAttribute("h"));
        //
        String fontFamily = elem.getAttribute("fontFamily");
        int iAtrib; // 0=PLAIN, 1=BOLD, 2=ITALIC
        switch (elem.getAttribute("fontStyle")) {
            case "PLAIN":
                iAtrib = Font.PLAIN;
                break;
            case "BOLD":
                iAtrib = Font.BOLD;
                break;
            case "ITALIC":
                iAtrib = Font.ITALIC;
                break;
            case "BOLDITALIC":
                iAtrib = Font.BOLD + Font.ITALIC;
                break;
            default:
                iAtrib = Font.PLAIN;
        }
        int fontSize = Integer.parseInt(elem.getAttribute("fontSize"));
        font = new Font(fontFamily, iAtrib, fontSize);
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
        //
        hAlign = TextToHAlign(elem.getAttribute("horizAlign"));
        vAlign = TextToVAlign(elem.getAttribute("vertAlign"));
    }

    public Eti getEti(int topY_pix, int leftMargin_pix) {
        Eti eti = new Eti("");
        eti.setRealPosition(x + leftMargin_pix, y + topY_pix, w, h);
        eti.setTypeText();
        eti.setSursaText(sursaText);
        eti.setText(getText());
        eti.setTextNumeric(isTextNumeric());
        eti.setFont(getFont());
        eti.setForeground(getColor());
        eti.setHorizontalAlignment(hAlign);
        eti.setVerticalAlignment(vAlign);
        return eti;
    }

    public static String hAlignToText(int hAlign) {
        if (hAlign == JLabel.LEFT) {
            return "LEFT";
        }
        if (hAlign == JLabel.CENTER) {
            return "CENTER";
        }
        if (hAlign == JLabel.RIGHT) {
            return "RIGHT";
        }
        return "LEFT";
    }

    public static String vAlignToText(int hAlign) {
        if (hAlign == JLabel.TOP) {
            return "TOP";
        }
        if (hAlign == JLabel.CENTER) {
            return "CENTER";
        }
        if (hAlign == JLabel.BOTTOM) {
            return "BOTTOM";
        }
        return "CENTER";
    }

    public static int TextToHAlign(String shAlign) {
        switch (shAlign) {
            case "LEFT":
                return JLabel.LEFT;  //2
            case "CENTER":
                return JLabel.CENTER;  //0
            case "RIGHT":
                return JLabel.RIGHT;  // 4
        }
        return JLabel.LEFT;
    }

    public static int TextToVAlign(String shAlign) {
        switch (shAlign) {
            case "TOP":
                return JLabel.TOP;  //1
            case "CENTER":
                return JLabel.CENTER;  //0
            case "BOTTOM":
                return JLabel.BOTTOM;  //3
        }
        return JLabel.CENTER;
    }
}
