package dorel.simplejavareport.report;
// Affine transform: http://www.javalobby.org/java/forums/t19387.html
// width la benzi e pusa la tiparire

import dorel.simplejavareport.report.components.Band;
import dorel.simplejavareport.tools.Calc;
import dorel.simplejavareport.tools.Utils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

//public static double MM_PUNCTE_PRINT = 2.834645669;
//public static double MM_PUNCTE_SCREEN = 3.779527559; //2.834645669; // 3.779527559;
//private static double INCH_PUNCTE_PRINT = 72; dpi
// 1 inch=25.4 mm
// 1 mm=0.03937008 inch
//private static double INCH_PUNCTE_SCREEN = 96;  // la mine acasa:  ppi=sqrt(1680^2+1050^2)/22=90 ppi
// A4: 8.27 / 11.69 in   210 / 297 mm
public final class ReportContent { // extends JLabel implements Printable

    // datorita:INCH_PUNCTE_PRINT = 72; dpi si INCH_PUNCTE_SCREEN = 96; ppi la mine 90 ppi
    private String numeFis = "";
    //
    public String[] columnNames;
    public List<String[]> lData;
    public Map<String, String> mCommon;
    //
    public List<Band> lBands;
    //
    private int lastPageIndex = 0; // setat la calculul paginatiei in ContentViewer, necesar la variabila de report nr_pagini
    //
    private int paper_width;
    private int paper_height;
    private int paper_marginLeft;
    private int paper_marginRight;
    private int paper_marginTop;
    private int paper_marginBottom;
    public int pageOrientation;
    public Font defaultFont;
    public Color defaultColor;
    //

    // <editor-fold defaultstate="collapsed" desc="Get Set">
    public Font getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(Font font) {
        defaultFont = font;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color color) {
        defaultColor = color;
    }

    public String getNumeFis() {
        return numeFis;
    }

    // <editor-fold defaultstate="collapsed" desc="Page format">
    public void setPageFormat(PageFormat pageFormat) {
        this.pageOrientation = pageFormat.getOrientation();
        Paper paper = pageFormat.getPaper();
        this.setPaper_width((int) paper.getWidth());
        this.setPaper_height((int) paper.getHeight());
        this.paper_marginLeft = (int) paper.getImageableX();
        this.paper_marginRight = (int) (paper.getWidth() - paper.getImageableX() - paper.getImageableWidth());
        this.paper_marginTop = (int) paper.getImageableY();
        this.paper_marginBottom = (int) (paper.getHeight() - paper.getImageableY() - paper.getImageableHeight());
    }

    public PageFormat getPageFormat() {
        Paper paper = new Paper();
        paper.setSize(paper_width, paper_height);
        double xs = paper_marginLeft;
        double ys = paper_marginTop;
        double ws = paper_width - paper_marginLeft - paper_marginRight;
        double hs = paper_height - paper_marginTop - paper_marginBottom;
        paper.setImageableArea(xs, ys, ws, hs);
        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);
        pageFormat.setOrientation(pageOrientation);
        return pageFormat;
    }

    public int getPaper_marginLeft() {
        return paper_marginLeft;
    }

    public int getPaper_marginRight() {
        return paper_marginRight;
    }

    public int getPaper_marginTop() {
        return paper_marginTop;
    }

    public int getPaper_marginBottom() {
        return paper_marginBottom;
    }
    // </editor-fold>

    public void setLastPageIndex(int lastPageIndex) {
        this.lastPageIndex = lastPageIndex;
    }

    public int getLastPageIndex() {
        return lastPageIndex;
    }

    // <editor-fold defaultstate="collapsed" desc="Paper dimensions">
    public void setPaper_width(int width) {
        this.paper_width = width;
    }

    public int getPaper_width() {
        return paper_width;
    }

    public void setPaper_height(int height) {
        this.paper_height = height;
    }

    public int getPaper_height() {
        return paper_height;
    }

    public int getOrientedPaperWidth() {
        if (pageOrientation == PageFormat.PORTRAIT) {
            return paper_width;
        } else {
            return paper_height;
        }
    }

    public int getOrientedPaperHeight() {
        if (pageOrientation == PageFormat.PORTRAIT) {
            return paper_height;
        } else {
            return paper_width;
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Image dimensions">
    public int getImage_width() {
        return paper_width - paper_marginLeft - paper_marginRight;
    }

    public int getImage_height() {
        return paper_height - paper_marginTop - paper_marginBottom;
    }

    public int getOrientedImageWidth() {
        // Portret -> Landscape: LeftMargin -> TopMargin
        // Landscape -> Portret: TopMargin -> LeftMargin
        if (pageOrientation == PageFormat.PORTRAIT) {
            return getImage_width();
        } else {
            return getImage_height();
        }
    }

    public int getOrientedImageHeight() {
        // Portret -> Landscape: LeftMargin -> TopMargin
        // Landscape -> Portret: TopMargin -> LeftMargin
        if (pageOrientation == PageFormat.PORTRAIT) {
            return getImage_height();
        } else {
            return getImage_width();
        }
    }
    // Portret -> Landscape: LeftMargin -> TopMargin rotatie dimensiuni left, right... se zapaceste de tot -> am scos posibilitatea de a modifica orientarea paginii
    // Landscape -> Portret: TopMargin -> LeftMargin
    //</editor-fold>
//</editor-fold>

    public ReportContent() {
        loadNew();
    }

    public ReportContent(String numeFis, Map mCommon, List lData) {
        if (numeFis == null) {
            loadNew();
        } else {
            if (this.readXML(numeFis)) {
                if (mCommon != null) {
                    this.mCommon = mCommon;
                }
                if (lData != null) {
                    this.lData = lData;
                }
            } else {
                loadNew();
            }
        }
    }

    public void loadNew() {
        //
        paper_width = 595; //210mm  = 595; //(int) 8.27inch * 72dpi = 595 ug;
        paper_height = 841; //297mm = 841;  //(int) 11.69 * 72;
        paper_marginLeft = 60; // = 15;
        paper_marginRight = 20; //5 = 14;
        paper_marginTop = 40; //10 = 28;
        paper_marginBottom = 20; //5 = 14;
        pageOrientation = PageFormat.PORTRAIT;
        //
        mCommon = new HashMap<>();
        columnNames = new String[0];
        lData = new ArrayList<>();
        //
        // benzile vor fi ordonate: primele - Headere, ... ultimele Footere
        lBands = new ArrayList<>();
        lBands.add(new Band(Band.PAGE_HEADER, 40, this));
        lBands.add(new Band(Band.DETAIL_BAND, 40, this));
        lBands.add(new Band(Band.PAGE_FOOTER, 40, this));
        // 
        this.defaultFont = new Font("Serif", Font.PLAIN, 10);
        defaultColor = Color.black;
    }

    public void resetBandsLists() {
        for (Band band : lBands) {
            band.resetLists();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Bands management">
    public int getDetailBandHeight() {
        for (Band band : lBands) {
            if (band.isDetail()) {
                return band.getHeight();
            }
        }
        return 0;
    }

    public int getSummaryBandHeight() {
        for (Band band : lBands) {
            if (band.isSumary()) {
                return band.getHeight();
            }
        }
        return 0;
    }

    // pot fi oricate headere si footere
    public int getPageHeaderHeight() {
        int hy = 0;
        for (Band band : lBands) {
            if (band.isHeader()) {
                hy += band.getHeight();
            }
        }
        return hy;
    }

    public int getPageFooterHeight() {
        int hy = 0;
        for (Band band : lBands) {
            if (band.isFooter()) {
                hy += band.getHeight();
            }
        }
        return hy;
    }

    public int getFirstPageHeight() {
        int hy = 0;
        for (Band band : lBands) {
            if (!band.isHeader()) {
                if (!band.isFooter()) {
                    if (!band.isDetail()) {
                        if (!band.isSumary()) {
                            hy += band.getHeight();
                        }
                    }
                }
            }
        }
        return hy;
    }

    public int getRestPagesHeight() {
        int hy = 0;
        for (Band band : lBands) {
            if (!band.isHeader()) {
                if (!band.isFooter()) {
                    if (!band.isDetail()) {
                        if (!band.isSumary()) {
                            if (!band.isOnlyInFirstPage()) {
                                hy += band.getHeight();
                            }
                        }
                    }
                }
            }
        }
        return hy;
    }

    public boolean isDetailConstraint() {
        // poate avea o singura banda 'detail'!!! sau niciuna
        int nBenzi = 0;
        for (Band band : lBands) {
            if (band.isDetail()) {
                nBenzi += 1;
            }
        }
        return nBenzi < 2;
    }

    public boolean isSummaryConstraint() {
        // poate avea o singura banda 'summary'!!! sau niciuna
        int nBenzi = 0;
        for (Band band : lBands) {
            if (band.isSumary()) {
                nBenzi += 1;
            }
        }
        return nBenzi < 2;
    }

    public boolean hasBandNamed(String bandName) {
        for (Band band : lBands) {
            if (band.getBandName().equals(bandName)) {
                return true;
            }
        }
        return false;
    }

    public Band getBandaNamed(String bandName) {
        for (Band band : lBands) {
            if (band.getBandName().equals(bandName)) {
                return band;
            }
        }
        return null;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Open file">
    public boolean open() {
        JFileChooser openFile = new JFileChooser();
        openFile.setCurrentDirectory(new File("."));
        openFile.setFileFilter(new XmlFilter());
        if (openFile.showOpenDialog(openFile) == JFileChooser.APPROVE_OPTION) {
            File xyz = openFile.getSelectedFile();
            readXML(xyz.getAbsoluteFile().getAbsolutePath());
            this.numeFis = xyz.getAbsoluteFile().getAbsolutePath();
            return true;
        }
        return false;
    }

    private boolean readXML(String xmlFile) {
        Document doc;
        Element elem;
        File file = new File(xmlFile);
        if (file.exists()) {
            // Clean file ...
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                doc = factory.newDocumentBuilder().parse(xmlFile);
                // Handicapatul asta nu accepta caractere (CR, LF, space ...) intre taguri!!!!!!!
                Element root = doc.getDocumentElement();
                NodeList nl = root.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    Node node = nl.item(i);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        elem = (Element) node;
                        try {
                            switch (elem.getNodeName()) {
                                case "pageFormat":
                                    paper_width = Integer.parseInt(elem.getAttribute("paper_width"));
                                    paper_height = Integer.parseInt(elem.getAttribute("paper_height"));
                                    paper_marginLeft = Integer.parseInt(elem.getAttribute("paper_marginLeft"));
                                    paper_marginRight = Integer.parseInt(elem.getAttribute("paper_marginRight"));
                                    paper_marginTop = Integer.parseInt(elem.getAttribute("paper_marginTop"));
                                    paper_marginBottom = Integer.parseInt(elem.getAttribute("paper_marginBottom"));
                                    if (elem.getAttribute("pageOrientation").equals("Portrait")) {
                                        pageOrientation = PageFormat.PORTRAIT;
                                    } else {
                                        pageOrientation = PageFormat.PORTRAIT;
                                    }
                                    break;
                                case "defaultFont":
                                    String fontFamily = elem.getAttribute("fontFamily");
                                    int iatrib;
                                    switch (elem.getAttribute("fontStyle")) {
                                        case "BOLD":
                                            iatrib = Font.BOLD;
                                            break;
                                        case "ITALIC":
                                            iatrib = Font.ITALIC;
                                            break;
                                        case "PLAIN":
                                            iatrib = Font.PLAIN;
                                            break;
                                        default:
                                            iatrib = Font.PLAIN;
                                    }
                                    int fontSize = Integer.parseInt(elem.getAttribute("fontSize"));
                                    defaultFont = new Font(fontFamily, iatrib, fontSize);
                                    break;
                                case "defaultColor":
                                    String sColor = elem.getAttribute("color");
                                    String[] asColor = sColor.split(",");
                                    if (asColor.length == 4) {
                                        if (Calc.isInteger(asColor[0]) && Calc.isInteger(asColor[1]) && Calc.isInteger(asColor[2]) && Calc.isInteger(asColor[3])) {
                                            int r = Integer.parseInt(asColor[0]);
                                            int g = Integer.parseInt(asColor[1]);
                                            int b = Integer.parseInt(asColor[2]);
                                            int a = Integer.parseInt(asColor[3]);
                                            if (0 <= r && r <= 255 && 0 <= g && g <= 255 && 0 <= b && b <= 255 && 0 <= a && a <= 255) {
                                                defaultColor = new Color(r, g, b, a);
                                            } else {
                                                defaultColor = Color.black;
                                            }
                                        } else {
                                            defaultColor = Color.black;
                                        }
                                    } else {
                                        defaultColor = Color.black;
                                    }
                                case "bands":
                                    extractBands(elem);
                                case "testData":
                                    extractTestData(elem);
                                    break;
                                default:
                                    break;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "ReportContent.readXML - XML parse exception:" + ex.getLocalizedMessage());
                            return false;
                        }
                    }
                }
                if (!isDetailConstraint()) {
                    JOptionPane.showMessageDialog(null, "ReportContent.readXML - The report has more than one detailBand.");
                    return false;
                }
                if (!isSummaryConstraint()) {
                    JOptionPane.showMessageDialog(null, "ReportContent.readXML - The report has more than one summaryBand.");
                    return false;
                }
                return true;
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                JOptionPane.showMessageDialog(null, "ReportContent.readXML - Error readXML: ParserConfigurationException | SAXException | IOException: " + ex.getLocalizedMessage());
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "ReportContent.readXML - File not found:" + xmlFile);
            return false;
        }
    }

    private void extractBands(Element baseElem) {
        Element elem;
        lBands = new ArrayList<>();
        NodeList nl = baseElem.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    elem = (Element) node;
                    lBands.add(new Band(elem, this));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractTestData - XML parse exception:" + ex.getLocalizedMessage());
                }
            }
        }
    }

    private void extractTestData(Element baseElem) {
        Element elem;
        NodeList nl = baseElem.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    elem = (Element) node;
                    switch (node.getNodeName()) {
                        case "mCommon":
                            extractMCommon(elem);
                            break;
                        case "columnNames":
                            extractColumnNames(elem);
                            break;
                        case "lData":
                            extractLData(elem);
                            break;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractTestData - XML parse exception:" + ex.getLocalizedMessage());
                }
            }
        }
    }

    private void extractMCommon(Element baseElem) {
        Element elem;
        NodeList nl = baseElem.getChildNodes();
        mCommon = new HashMap<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    elem = (Element) node;
                    mCommon.put(elem.getAttribute("key"), elem.getAttribute("value"));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractMCommon - XML parse exception:" + ex.getLocalizedMessage());
                }
            }
        }
    }

    private void extractColumnNames(Element baseElem) {
        int index;
        int nCol = Integer.parseInt(baseElem.getAttribute("length"));
        columnNames = new String[nCol];
        NodeList nl = baseElem.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String sIndex = elem.getAttribute("index");
                if (Calc.isInteger(sIndex)) {
                    index = Integer.parseInt(sIndex); //Integer.parseInt(node.getNodeName().substring(3));
                } else {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractColumnNames - ColumnIndex error: '" + sIndex + "' is not int");
                    index = 0;
                }
                try {
                    columnNames[index] = node.getTextContent();
                } catch (DOMException ex) {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractColumnNames - XML parse exception:" + ex.getLocalizedMessage());
                }
            }
        }
    }

    private void extractLData(Element baseElem) {
        Element elem;
        NodeList nl = baseElem.getChildNodes();
        lData = new ArrayList<>();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                try {
                    elem = (Element) node;
                    String[] line = extractLineData(elem);
                    lData.add(line);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractLData - XML parse exception:" + ex.getLocalizedMessage());
                }
            }
        }
    }

    private String[] extractLineData(Element baseElem) {
        int index;
        int nCol = Integer.parseInt(baseElem.getAttribute("length"));
        String[] line = new String[nCol];
        NodeList nl = baseElem.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                //index = Integer.parseInt(node.getNodeName().substring(3));
                Element elem = (Element) node;
                String sIndex = elem.getAttribute("index");
                if (Calc.isInteger(sIndex)) {
                    index = Integer.parseInt(sIndex); //Integer.parseInt(node.getNodeName().substring(3));
                } else {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractLineData - ColumnIndex error: '" + sIndex + "' is not int");
                    index = 0;
                }
                try {
                    line[index] = node.getTextContent();
                } catch (DOMException ex) {
                    JOptionPane.showMessageDialog(null, "ReportContent.extractLineData - XML parse exception:" + ex.getLocalizedMessage());
                }
            }
        }
        return line;
    }
// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Save file">
    public boolean save(boolean rename) {
        boolean response = false;
        if (rename) {
            JFileChooser saveFile = new JFileChooser();
            saveFile.setCurrentDirectory(new File("."));
            saveFile.setFileFilter(new XmlFilter());
            if (saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File xyz = saveFile.getSelectedFile();
                // schimba numeFis
                numeFis = xyz.getAbsoluteFile().getAbsolutePath();
                // test extension
                String ext = Utils.getExtension(xyz);
                if (ext == null) {
                    numeFis = numeFis + "." + Utils.xml;
                }
                response = writeXML();
            }
        } else {
            if (numeFis.length() != 0) {
                response = writeXML();
            } else {
                JFileChooser saveFile = new JFileChooser();
                saveFile.setCurrentDirectory(new File("."));
                saveFile.setFileFilter(new XmlFilter());
                if (saveFile.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File xyz = saveFile.getSelectedFile();
                    // schimba numeFis
                    numeFis = xyz.getAbsoluteFile().getAbsolutePath();
                    // test extension
                    String ext = Utils.getExtension(xyz);
                    if (ext == null) {
                        numeFis = numeFis + "." + Utils.xml;
                    }
                    response = writeXML();
                }
            }
        }
        return response;
    }

    private boolean writeXML() {
        Document doc;
        Element elem;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder parser = factory.newDocumentBuilder();
            //Create blank DOM Document
            doc = parser.newDocument();
            doc.setXmlStandalone(true);
            //create the root element
            Element root = doc.createElement("reportContent");
            doc.appendChild(root);
            //
            elem = doc.createElement("pageFormat");
            elem.setAttribute("paper_width", String.valueOf(paper_width));
            elem.setAttribute("paper_height", String.valueOf(paper_height));
            elem.setAttribute("paper_marginLeft", String.valueOf(paper_marginLeft));
            elem.setAttribute("paper_marginRight", String.valueOf(paper_marginRight));
            elem.setAttribute("paper_marginTop", String.valueOf(paper_marginTop));
            elem.setAttribute("paper_marginBottom", String.valueOf(paper_marginBottom));
            if (pageOrientation == PageFormat.PORTRAIT) {
                elem.setAttribute("pageOrientation", "Portrait");
            } else {
                elem.setAttribute("pageOrientation", "Landscape");
            }
            //add it to the xml tree
            root.appendChild(elem);
            //
            elem = doc.createElement("defaultFont");
            elem.setAttribute("fontFamily", this.defaultFont.getFamily());
            String fontStyle;
            switch (defaultFont.getStyle()) {
                case Font.BOLD:
                    fontStyle = "BOLD";
                    break;
                case Font.ITALIC:
                    fontStyle = "ITALIC";
                    break;
                case Font.PLAIN:
                    fontStyle = "PLAIN";
                    break;
                default:
                    fontStyle = "PLAIN";
            }
            elem.setAttribute("fontStyle", fontStyle);
            elem.setAttribute("fontSize", String.valueOf(defaultFont.getSize()));
            //add it to the xml tree
            root.appendChild(elem);
            elem = doc.createElement("defaultColor");
            elem.setAttribute("color", defaultColor.getRed() + "," + defaultColor.getGreen() + "," + defaultColor.getBlue() + "," + defaultColor.getAlpha());
            root.appendChild(elem);
            //
            Element bandsElem = doc.createElement("bands");
            for (Band band : lBands) {
                elem = band.getElementXML(doc);
                bandsElem.appendChild(elem);
            }
            root.appendChild(bandsElem);
            //
            Element mCommonElem = doc.createElement("mCommon");
            Set chei = mCommon.keySet();
            Iterator li = chei.iterator();
            while (li.hasNext()) {
                String key = (String) li.next();
                elem = doc.createElement("var");
                elem.setAttribute("key", key);
                elem.setAttribute("value", mCommon.get(key));
                mCommonElem.appendChild(elem);
            }
            //
            Element columnNamesElem = doc.createElement("columnNames");
            columnNamesElem.setAttribute("length", String.valueOf(columnNames.length));
            for (int i = 0; i < columnNames.length; i++) {
                elem = doc.createElement("col");
                elem.setAttribute("index", String.valueOf(i));
                Text tn = doc.createTextNode(columnNames[i]);
                elem.appendChild(tn);
                //elem.setAttribute("name", columnNames[i]);
                columnNamesElem.appendChild(elem);
            }
            //
            Element lDataElem = doc.createElement("lData");
            //lDataElem.setAttribute("size", String.valueOf(lData.size()));
            for (int i = 0; i < lData.size(); i++) {
                String[] line = lData.get(i);
                //Element lineElem = doc.createElement("line" + String.valueOf(i));
                Element lineElem = doc.createElement("line");
                lineElem.setAttribute("length", String.valueOf(line.length));
                for (int j = 0; j < line.length; j++) {
                    elem = doc.createElement("col");
                    elem.setAttribute("index", String.valueOf(j));
                    Text tn = doc.createTextNode(line[j]);
                    elem.appendChild(tn);
                    lineElem.appendChild(elem);
                }
                lDataElem.appendChild(lineElem);
            }
            //
            elem = doc.createElement("testData");
            elem.appendChild(mCommonElem);
            elem.appendChild(columnNamesElem);
            elem.appendChild(lDataElem);
            root.appendChild(elem);
            //
            //Printing the DOM Tree
            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();
            //http://stackoverflow.com/questions/1384802/java-how-to-indent-xml-generated-by-transformer
            //t.setOutputProperty(OutputKeys.INDENT, "yes");
            //t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            //aTransformer.setParameter(OutputKeys.INDENT, "yes");
            //aTransformer.setParameter("indent-number", new Integer(2));
            Source src = new DOMSource(doc);
            Result dest = new StreamResult(new File(numeFis));
            aTransformer.transform(src, dest);
            return true;
        } catch (ParserConfigurationException ex) {
            JOptionPane.showMessageDialog(null, "ReportContent.writeXML ParserConfigurationException: " + ex.getLocalizedMessage());
        } catch (TransformerConfigurationException ex) {
            JOptionPane.showMessageDialog(null, "ReportContent.writeXML TransformerConfigurationException: " + ex.getLocalizedMessage());
        } catch (TransformerException ex) {
            JOptionPane.showMessageDialog(null, "ReportContent.writeXML TransformerException: " + ex.getLocalizedMessage());
        }
        return false;
    }
    // </editor-fold>

    public Dimension getMyPreferredSize() {
        // da dimensiunea imaginii paginii ce poate fi vizualizata la Preview
        // SI dimensiunea maxima posibila la designer!!!
        // FixMe am anumite indoieli ca ar fi corecte!!!! - latimea pare mai mica cu 50 de puncte pe ecran
        Dimension dim;
        if (pageOrientation == PageFormat.PORTRAIT) {
            dim = new Dimension(paper_width, paper_height);
        } else {
            dim = new Dimension(paper_height, paper_width);
        }
        return dim;
    }
}

//
//------------------------------------------------------------------------------
//
class XmlFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        String extension = Utils.getExtension(file);
        if (extension != null) {
            if (extension.equals(Utils.xml)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "xml files";
    }

}
