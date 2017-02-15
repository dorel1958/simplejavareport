package dorel.simplejavareport.interactivetable;

import javax.swing.JOptionPane;

public class LineClass implements LineInterface {

    // clasa 'model' - aplicatia poate utiliza ce clasa vrea ea, dar sa implementeze LineInterface
    private String[] lineData;

    // <editor-fold defaultstate="collapsed" desc="Constructor">         
    public LineClass(String[] lineData) {
        if (lineData == null) {
            this.lineData = new String[1];
            this.lineData[0] = "lineData=null";
        } else {
            this.lineData = lineData;
            for (int i = 0; i < lineData.length; i++) {
                if (this.lineData[i] == null) {
                    this.lineData[i] = "";
                }
            }
        }
    }

    public LineClass(int nrCol) {
        lineData = new String[nrCol];
        for (int i = 0; i < lineData.length; i++) {
            lineData[i] = "";
        }
    }
    //</editor-fold>

    @Override
    public int getColumnsNumber() {
        return lineData.length;
    }

    @Override
    public String getValueAt(int index) {
        if (index < 0) {
            return "LineClass.getValueAt - Not a column with index=" + index;
        }
        if (index < lineData.length) {
            return lineData[index];
        } else {
            return "LineClass.getValueAt - Not a column with index=" + index;
        }
    }

    @Override
    public void setValueAt(int index, String value) {
        if (index < 0) {
            JOptionPane.showMessageDialog(null, "LineClass.setValueAt - Not a column with index=" + index);
            return;
        }
        if (index < lineData.length) {
            if (value == null) {
                lineData[index] = "";
            } else {
                lineData[index] = value;
            }
        } else {
            JOptionPane.showMessageDialog(null, "LineClass.setValueAt - Not a column with index=" + index);
        }
    }

    @Override
    public boolean isEmpty() {
        for (String str : lineData) {
            if (!str.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String[] getStringArray() {
        return lineData;
    }

    @Override
    public LineInterface getClone() {
        String[] nLineData = new String[lineData.length];
        System.arraycopy(lineData, 0, nLineData, 0, lineData.length);
        LineInterface newLine = new LineClass(nLineData);
        return newLine;
    }
}
