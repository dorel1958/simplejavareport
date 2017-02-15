package dorel.simplejavareport.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;

public final class Rule extends JComponent {

    //public static final int INCH = (int) (Toolkit.getDefaultToolkit().getScreenResolution() * 1.15);  // Auto adjust this 1.15 ?
    //double MM_PUNCTE_SCREEN = 2.834 * 96 / 72;
    double MM_PIX_SCREEN; // =3.779527559; // 3.779527559;  //2.834645669;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SIZE = 15;
    public int orientation;
    public boolean isMetric;
    private final Color rule_color = Color.lightGray;

    public Rule(int o, boolean m, double coefScala) {
        orientation = o;
        isMetric = m;
        if (coefScala == 1) {
            MM_PIX_SCREEN = 2.834645669;
        } else {
            MM_PIX_SCREEN = 3.779527559;
        }
        //setIncrementAndUnits();
        if (o == HORIZONTAL) {
            this.setPreferredHeight(SIZE);
        } else {
            this.setPreferredWidth(SIZE);
        }
    }

    public void setPreferredHeight(int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    public void setPreferredWidth(int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }

    @Override
    public void paintComponent(Graphics g) {
        Rectangle drawHere = g.getClipBounds();

        // Fill clipping area with rule_color.
        g.setColor(rule_color);
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        // Do the ruler labels in a small font that's black.
        g.setFont(new Font("SansSerif", Font.PLAIN, 8));
        g.setColor(Color.black);

        // Some vars we need.
        int end;
        int start;
        start = 0;
        if (orientation == HORIZONTAL) {
            end = (int) ((drawHere.x + drawHere.width) * MM_PIX_SCREEN);
        } else {
            end = (int) ((drawHere.y + drawHere.height) * MM_PIX_SCREEN);
        }
        //
        int i = 0;
        int x = start;
        int h;
        String text;
        if (orientation == VERTICAL) {
            while (x < end) {
                x = start + (int) (i * MM_PIX_SCREEN);
                if (i % 10 == 0) {
                    h = 0;
                    text = String.valueOf((int) (i / 10));
                    if (i == 0) {
                        text = text + " cm";
                    }
                    g.drawString(text, 2, x + 8);
                } else {
                    if (i % 5 == 0) {
                        h = SIZE / 2;
                    } else {
                        h = (int) (3 * SIZE / 4);
                    }
                }
                g.drawLine(h, x, SIZE, x);
                i++;
            }
        } else {
            while (x < end) {
                x = start + (int) (i * MM_PIX_SCREEN);
                if (i % 10 == 0) {
                    h = 0;
                    text = String.valueOf((int) (i / 10));
                    if (i == 0) {
                        text = text + " cm";
                    }
                    g.drawString(text, x + 2, 8);
                } else {
                    if (i % 5 == 0) {
                        h = SIZE / 2;
                    } else {
                        h = (int) (3 * SIZE / 4);
                    }
                }
                g.drawLine(x, h, x, SIZE);
                i++;
            }
        }
    }
}
