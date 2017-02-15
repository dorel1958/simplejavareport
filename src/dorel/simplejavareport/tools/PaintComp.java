package dorel.simplejavareport.tools;

import dorel.simplejavareport.designer.EtiHandler;
import dorel.simplejavareport.report.components.RapImage;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class PaintComp {

    public static void paintText(Graphics g, String text, Rectangle rect, Font font, Color color, int hAlign, int vAlign, boolean isNumeric, boolean eEti) {
        // paint in report
        boolean cuLiniiDeContur = false;

        Graphics2D g2 = (Graphics2D) g;
        Font iniFont = g2.getFont();
        Color iniColor = g2.getColor();

        g2.setFont(font);

        g2.setColor(color);
        //Rectangle ext text
        int x_ug = rect.x;
        int y_ug = rect.y;
        int w_ug = rect.width;
        int h_ug = rect.height;
        //
        FontRenderContext frc = g2.getFontRenderContext();
        Rectangle2D boundsText_ug = font.getStringBounds(text, frc);
        String textulScurtat = text;
        //
        // verifica incadrarea textului in borderRect
        // poz text in interiorul Rectangle rect_ug
        int poziX_ug = 0;
        int poziY_ug = 0;
        if (w_ug
                == boundsText_ug.getWidth()) {
            // e la fix
            poziX_ug = 0;
        } else {
            if (w_ug > boundsText_ug.getWidth()) {
                // distribuie spatiul gol
                if (hAlign == JLabel.LEFT) {
                    poziX_ug = 0;
                }
                if (hAlign == JLabel.CENTER) {
                    poziX_ug = (int) ((w_ug - boundsText_ug.getWidth()) / 2);
                }
                if (hAlign == JLabel.RIGHT) {
                    poziX_ug = (int) (w_ug - boundsText_ug.getWidth());
                }
            } else {
                if (isNumeric) {
                    textulScurtat = "***";
                } else {
                    // taie textul ca sa incapa
                    while (true) {
                        if (textulScurtat.length() == 0) {
                            textulScurtat += "...";
                            break;
                        }
                        textulScurtat = textulScurtat.substring(0, textulScurtat.length() - 1);
                        boundsText_ug = font.getStringBounds(textulScurtat + "...", frc);
                        if (w_ug >= boundsText_ug.getWidth()) {
                            textulScurtat += "...";
                            break;
                        }
                    }
                }
            }
        }
        if (h_ug
                == boundsText_ug.getHeight()) {
            // e la fix
            poziY_ug = 0;
        } else {
            if (h_ug > boundsText_ug.getHeight()) {
                // distribuie spatiul gol
                if (vAlign == JLabel.TOP) {
                    poziY_ug = 0;
                }
                if (vAlign == JLabel.CENTER) {
                    poziY_ug = (int) ((h_ug - boundsText_ug.getHeight()) / 2);
                }
                if (vAlign == JLabel.BOTTOM) {
                    poziY_ug = (int) (h_ug - boundsText_ug.getHeight());
                }
            } else {
                // acum NU taie textul ca sa incapa
                //textul = "*";
            }
        }
        int sizeFont = font.getSize();
        // poz text in pagina (imageable page)
        int gx = x_ug + poziX_ug;
        int gy = y_ug + poziY_ug;

        g2.drawString(textulScurtat, gx, gy + sizeFont);

        if (cuLiniiDeContur) {
            // pt teste pozitionare
            // rect text
            g2.setColor(Color.blue);

            g2.drawRect(gx, gy, (int) boundsText_ug.getWidth(), (int) boundsText_ug.getHeight()
            );
            // rect ext
            g2.setColor(Color.red);

            g2.drawRect(x_ug, y_ug, w_ug, h_ug);
            //
            // reface setarile initiale
            g2.setColor(iniColor);

            g2.setFont(iniFont);
        } else {
            // deseneaza dreptunghiul negru in Design mode
            if (eEti) {
                g2.setColor(Color.black);

                g2.drawRect(x_ug, y_ug, w_ug, h_ug);
                //
                // reface setarile initiale
                g2.setColor(iniColor);
            }
        }
    }

    public static void paintImage(Graphics2D g2, String text, RapImage.DrawMode drawMode, int w, int h, int x, int y, boolean eEti) {
        Image img;
        try {
            img = ImageIO.read(new File(text));
            switch (drawMode) {
                case CLIP_CONTENTS:
                    // limitez afisarea pozei la dreptunghiul de definitie in raport
                    Shape initClip = null;
                    if (eEti) {
                        initClip = g2.getClip();
                    }
                    g2.setClip(new Rectangle(x, y, w + 1, h + 1));
                    g2.drawImage(img, x, y, null);
                    if (eEti) {
                        g2.setClip(initClip);
                    }
                    break;
                case SCALE_FILL_THE_FRAME:
                    // scalez toata imaginea in dreptunghiul de afisare
                    g2.drawImage(img,
                            x, y, x + w + 1, y + h + 1,
                            0, 0, img.getWidth(null), img.getHeight(null),
                            null);
                    break;
                case SCALE_RETAIN_SHAPE:
                    // scalez imaginea sa incapa in dreptunghiul de afisare
                    double scaleX = (double) w / img.getWidth(null);
                    double scaleY = (double) h / img.getHeight(null);
                    double scale = Math.min(scaleX, scaleY);
                    double imgW = img.getWidth(null) * scale;
                    double imgH = img.getHeight(null) * scale;
                    int dX = (int) ((w - imgW) / 2);
                    int dY = (int) ((h - imgH) / 2);
                    g2.drawImage(img,
                            x + dX, y + dY, x + w + 1 - dX, y + h + 1 - dY,
                            0, 0, img.getWidth(null), img.getHeight(null),
                            null);
                    break;
            }

        } catch (IOException ex) {
            // text cu Image inexistenta
            Stroke s;
            s = g2.getStroke();
            Paint p;
            p = g2.getPaint();

            g2.setPaint(Color.black);
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, y, w, h);
            Shape initClip = null;
            g2.setClip(new Rectangle(x, y, w + 1, h + 1));
            if (eEti) {
                g2.drawString("Image", x + 5, y + 15);
                g2.drawString(text, x + 5, y + 35);
            } else {
                g2.drawString("File not found:", x + 5, y + 15);
                g2.drawString(text, x + 5, y + 35);
            }

            g2.setClip(initClip);
            g2.setStroke(s);
            g2.setPaint(p);
        }
//        if (eEti) {
//            // deseneaza dreptunghiul negru in Design mode
//            Color iniColor = g2.getColor();
//            g2.setColor(Color.black);
//
//            g2.drawRect(x, y, w, h);
//                //
//            // reface setarile initiale
//            g2.setColor(iniColor);
//        }
    }

    public static void paintLine(Graphics2D g2, int x1, int y1, int x2, int y2, float lineWidth, boolean isRect, Color color, int lineEnds, int lineJoints, boolean eEti) {
        if (x1 == 0 && x2 == 0 && y1 == 0 && y2 == 0) {
            return;
        }
        // save init
        Stroke s;
        s = g2.getStroke();
        Paint p;
        p = g2.getPaint();
        //
        //g2.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));// retreaza la capat
        //g2.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));  // adauga si grosimea liniei la capat
        //g2.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));  // adauga si grosimea liniei rorunjita
        //g2.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));  // adauga si grosimea liniei rorunjita
        //g2.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));  // adauga si grosimea liniei rorunjita
        g2.setStroke(new BasicStroke(lineWidth, lineEnds, lineJoints));
        g2.setPaint(color);
        if (isRect) {
            int x = Math.min(x1, x2);
            int y = Math.min(y1, y2);
            int w = Math.abs(x2 - x1);
            int h = Math.abs(y2 - y1);
            g2.drawRect(x, y, w, h);
        } else {
            g2.drawLine(x1, y1, x2, y2);
        }
        // reface val initiale
        g2.setStroke(s);
        g2.setPaint(p);
//        if (eEti) {
//            // deseneaza dreptunghiul negru in Design mode
//            Color iniColor = g2.getColor();
//            g2.setColor(Color.black);
//
//            g2.drawRect(x, y, w, h);
//                //
//            // reface setarile initiale
//            g2.setColor(iniColor);
//        }
    }

    public static void paintHandlers(Graphics2D g2, List<EtiHandler> lHandlers) {
        // save
        Paint initPaint = g2.getPaint();

        // paint handlers
        g2.setPaint(Color.red);
        for (EtiHandler h : lHandlers) {
            h.paint(g2);
        }
        // reface
        g2.setPaint(initPaint);
    }
}
