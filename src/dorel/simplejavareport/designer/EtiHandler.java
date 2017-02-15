package dorel.simplejavareport.designer;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.io.Serializable;

public class EtiHandler implements Serializable{

    public int x;  // poz in coord din eticheta
    public int y;
    public int w = 4;
    public int h = 4;
    public int cursor;

    public EtiHandler(int x, int y, int cursor) {
        this.x = x;
        this.y = y;
        this.cursor = cursor;
    }

    public void paint(Graphics2D g2) {
        int xl=x;
        int yl=y;
        switch(cursor){
            case Cursor.NE_RESIZE_CURSOR:
                xl=x-1;
                yl=y-2;
                break;
            case Cursor.NW_RESIZE_CURSOR:
                xl=x-2;
                yl=y-2;
                break;
            case Cursor.SE_RESIZE_CURSOR:
                xl=x-1;
                yl=y-1;
                break;
            case Cursor.SW_RESIZE_CURSOR:
                xl=x-2;
                yl=y-1;
                break;
            case Cursor.E_RESIZE_CURSOR:
                xl=x-1;
                yl=y-2;
                break;
            case Cursor.W_RESIZE_CURSOR:
                xl=x-2;
                yl=y-2;
                break;
            case Cursor.N_RESIZE_CURSOR:
                xl=x-2;
                yl=y-2;
                break;
            case Cursor.S_RESIZE_CURSOR:
                xl=x-2;
                yl=y-1;
                break;
        }
        g2.fillRect(xl, yl, w, h);
    }

    public int isMouseOver(int xm, int ym) {
        if (x - 2 <= xm && xm <= x - 2 + w && y - 2 <= ym && ym <= y - 2 + h) {
            return cursor;
        } else {
            return -1;
        }
    }
}
