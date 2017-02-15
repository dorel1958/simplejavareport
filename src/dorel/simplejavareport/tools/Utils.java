package dorel.simplejavareport.tools;

import java.io.File;

public class Utils {
    public static String booleanToString(boolean bool){
        if (bool){
            return "true";
        } else {
            return "false";
        }
    }

    public final static String xml = "xml";

    public static String getExtension(File file) {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
