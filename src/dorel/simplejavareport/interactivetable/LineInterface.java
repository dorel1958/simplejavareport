package dorel.simplejavareport.interactivetable;

public interface LineInterface {
    public int getColumnsNumber();
    public String getValueAt(int index);
    public void setValueAt(int index, String value);
    public boolean isEmpty();
    public String[] getStringArray();
    public LineInterface getClone();
}
