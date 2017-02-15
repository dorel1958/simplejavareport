package dorel.simplejavareport.report.components;

import dorel.simplejavareport.designer.Eti;
import dorel.simplejavareport.designer.LineEti;
import dorel.simplejavareport.report.ReportContent;
import dorel.simplejavareport.tools.Calc;
import dorel.simplejavareport.tools.Utils;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Band { // extends JPanel

    public static String PAGE_HEADER = "pageHeader";
    public static String TITLE_BAND = "titleBand";
    public static String TABLE_HEADER = "tableHeader";
    public static String DETAIL_BAND = "detailBand";
    public static String SUMMARY_BAND = "summaryBand";
    public static String PAGE_FOOTER = "pageFooter";
    //public static double MM_PUNCTE_PRINT = 2.834645669;
    //public static double MM_PUNCTE_SCREEN = 3.779527559; //2.834645669; // 3.779527559;
    //
    private final ReportContent rc;
    //
    public List<RapLine> lLines;
    public List<RapText> lTexts;
    public List<RapImage> lImages;
    //
    protected int height;
    protected int width;
    protected int translateX;
    protected int translateY;
    private String bandName;
    private boolean header = false;
    private boolean onlyInFirstPage = false;
    private boolean detail = false;
    private boolean sumary = false;
    private boolean footer = false;

    // <editor-fold defaultstate="collapsed" desc="Get Set">
    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public boolean isFooter() {
        return footer;
    }

    public void setFooter(boolean footer) {
        this.footer = footer;
    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public boolean isSumary() {
        return sumary;
    }

    public void setSumary(boolean sumary) {
        this.sumary = sumary;
    }

    public boolean isOnlyInFirstPage() {
        return onlyInFirstPage;
    }

    public void setOnlyInFirstPage(boolean onlyInFirstPage) {
        this.onlyInFirstPage = onlyInFirstPage;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTranslateX() {
        return translateX;
    }

    public void setTranslateX(int translateX) {
        this.translateX = translateX;
    }

    public void setTranslateY(int translateY) {
        this.translateY = translateY;
    }
    //</editor-fold>

    public Band(Element elem, ReportContent rc) {
        loadBand(elem);
        width = rc.getOrientedImageWidth();  //210; // cam cat A4 portret
        translateX = 0;
        translateY = 0;
        this.rc = rc;
        //
    }

    public Band(String bandName, int height, ReportContent rc) {
        // used for new ReportContent
        this.bandName = bandName;
        setTypeImplicit();
        this.height = height;
        width = rc.getOrientedImageWidth(); //210; // like A4 Portret - wil be set corect in MyContentViewer.print
        translateX = 0;
        translateY = 0;
        //
        this.rc = rc;
        resetLists();
    }

    private void setTypeImplicit() {
        if (bandName.equals(TITLE_BAND)) {
            onlyInFirstPage = true;
        }
        if (bandName.equals(DETAIL_BAND)) {
            detail = true;
        }
        if (bandName.equals(SUMMARY_BAND)) {
            sumary = true;
        }
        if (bandName.equals(PAGE_HEADER)) {
            header = true;
        }
        if (bandName.equals(PAGE_FOOTER)) {
            footer = true;
        }
    }

    public final void resetLists() {
        lLines = new ArrayList<>();
        lTexts = new ArrayList<>();
        lImages = new ArrayList<>();
    }

    public int drawComponents(Graphics graphics, int pageIndex) {
        return drawComponents(graphics, pageIndex, 0);
    }

    public int drawComponents(Graphics graphics, int pageIndex, int lineIndex) {
        if (height == 0) {
            return translateY;
        }
        if (onlyInFirstPage) {
            if (pageIndex > 0) {
                return translateY;
            }
        }
        // linie contur
        //Graphics2D graphics2 = (Graphics2D) graphics;
        //graphics2.setPaint(Color.BLUE);
        //graphics2.drawRect(translateX, translateY, width, height);
        //System.out.println("Print banda: " + bandName + " translateY=" + translateY);

        //
        // images
        for (RapImage ri : lImages) {
            switch (ri.getSursaText()) {
                case TEXT:
                    ri.setPrintedText(ri.getText());
                    break;
                case COMMON:
                    ri.setPrintedText(rc.mCommon.get(ri.getText()));
                    break;
                case DATA:
                    String[] line = rc.lData.get(lineIndex);
                    int colIndex = colNameToIndex(ri.getText());
                    ri.setPrintedText(line[colIndex]);
                    break;
            }
            ri.draw(graphics, width, height, translateX, translateY);
        }
        //
        // lines
        for (RapLine rl : lLines) {
            rl.draw(graphics, width, height, translateX, translateY);
        }
        //
        // texts
        for (RapText rt : lTexts) {
            switch (rt.getSursaText()) {
                case TEXT:
                    rt.setPrintedText(rt.getText());
                    //rt.draw(graphics, width, height, translateX, translateY);
                    break;
                case COMMON:
                    rt.setPrintedText(rc.mCommon.get(rt.getText()));
                    //rt.draw(graphics, width, height, translateX, translateY);
                    break;
                case DATA:
                    int colIndex = colNameToIndex(rt.getText());

                    if (sumary) {
                        double suma = 0;
                        boolean nan = false;
                        String strSuma;
                        //if (rt.isTextNumeric()) {
                        boolean isPoint = false;
                        for (int j = 0; j < rc.lData.size(); j++) {
                            String[] linx = rc.lData.get(j);
                            String sValue;
                            if (colIndex < linx.length) {
                                sValue = linx[colIndex];
                            } else {
                                sValue = "";
                            }
                            CharSequence cs = ".";
                            if (sValue.contains(cs)) {
                                isPoint = true;
                            }
                            if (Calc.isNumeric(sValue)) {
                                suma += Double.parseDouble(sValue);
                            } else {
                                nan = true;
                                break;
                            }
                        }
                        if (nan) {
                            strSuma = "Not Numeric";
                        } else {
                            if (!isPoint) {
                                strSuma = String.valueOf((int) suma);
                            } else {
                                strSuma = String.valueOf(suma);
                            }
                        }
                        //}
                        rt.setPrintedText(strSuma);
                    } else {
                        String[] line = rc.lData.get(lineIndex);
                        if (colIndex < line.length) {
                            rt.setPrintedText(line[colIndex]);
                        } else {
                            rt.setPrintedText("");
                        }
                        break;
                    }
                    break;
                case REPORT:
                    switch (rt.getText()) {
                        case "report_LineNumber":
                            rt.setPrintedText(String.valueOf(lineIndex + 1));
                            rt.setTextNumeric(true);
                            break;
                        case "report_PageNumber":
                            rt.setPrintedText(String.valueOf(pageIndex + 1));
                            rt.setTextNumeric(true);
                            break;
                        case "report_NPages":
                            rt.setPrintedText(String.valueOf(rc.getLastPageIndex() + 1));
                            rt.setTextNumeric(true);
                            break;
                        default:
                            rt.setPrintedText("");
                            break;
                    }
                    break;
            }
            rt.draw(graphics, width, height, translateX, translateY);
        }
        return translateY + height;
    }

    private int colNameToIndex(String colName) {
        for (int i = 0; i < rc.columnNames.length; i++) {
            if (colName.equals(rc.columnNames[i])) {
                return i;
            }
        }
        return 0;
    }

    public void addEti(Eti eti, int offsetX, int offsetY) {
        if (eti.isTypeLine()) {
            lLines.add(((LineEti) eti).getRapLine(offsetX, offsetY));
        }
        if (eti.isTypeText()) {
            lTexts.add(eti.getRapText(offsetX, offsetY));
        }
        if (eti.isTypeImage()) {
            lImages.add(eti.getRapImage(offsetX, offsetY));
        }
    }

    // <editor-fold defaultstate="collapsed" desc="XML Load/Save">
    private void loadBand(Element elem) {
        if (elem.getNodeName().equals("band")) {
            bandName = elem.getAttribute("name");
            setTypeImplicit();
            resetLists();
            onlyInFirstPage = elem.getAttribute("onlyInFirstPage").equals("true");
            detail = elem.getAttribute("detail").equals("true");
            sumary = elem.getAttribute("sumary").equals("true");
            header = elem.getAttribute("header").equals("true");
            footer = elem.getAttribute("footer").equals("true");
            height = Integer.parseInt(elem.getAttribute("height"));
            //
            NodeList nl = elem.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                switch (node.getNodeName()) {
                    case "texts":
                        loadTexts((Element) node);
                        break;
                    case "lines":
                        loadLines((Element) node);
                        break;
                    case "images":
                        loadImages((Element) node);
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Band.loadBand - Error: NodeName#'band'");
            resetLists();
        }
    }

    private void loadTexts(Element elem) {
        RapText rt;
        NodeList nl = elem.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                rt = new RapText();
                rt.setElementXML((Element) node);
                lTexts.add(rt);
            }
        }
    }

    private void loadLines(Element elem) {
        RapLine rl;
        NodeList nl = elem.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                rl = new RapLine();
                rl.setElementXML((Element) node);
                lLines.add(rl);
            }
        }
    }

    private void loadImages(Element elem) {
        RapImage ri;
        NodeList nl = elem.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ri = new RapImage();
                ri.setElementXML((Element) node);
                lImages.add(ri);
            }
        }
    }

    public Element getElementXML(Document doc) {
        Element elem = doc.createElement("band");
        elem.setAttribute("name", bandName);
        elem.setAttribute("height", String.valueOf(height));
        elem.setAttribute("onlyInFirstPage", Utils.booleanToString(onlyInFirstPage));
        elem.setAttribute("detail", Utils.booleanToString(detail));
        elem.setAttribute("sumary", Utils.booleanToString(sumary));
        elem.setAttribute("header", Utils.booleanToString(header));
        elem.setAttribute("footer", Utils.booleanToString(footer));
        addTextsLinesAndImagesXML(elem, doc);
        return elem;
    }

    protected void addTextsLinesAndImagesXML(Element elem, Document doc) {
        Element texts = doc.createElement("texts");
        for (RapText rt : this.lTexts) {
            texts.appendChild(rt.getElementXML(doc));
        }
        elem.appendChild(texts);
        //
        Element lines = doc.createElement("lines");
        for (RapLine rl : this.lLines) {
            lines.appendChild(rl.getElementXML(doc));
        }
        elem.appendChild(lines);
        //
        Element images = doc.createElement("images");
        for (RapImage ri : lImages) {
            images.appendChild(ri.getElementXML(doc));
        }
        elem.appendChild(images);
    }
//</editor-fold>
}
