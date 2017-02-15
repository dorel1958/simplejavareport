package dorel.simplejavareport.tools;

public class SursaInfo {

    public static enum Sursa {

        TEXT,
        COMMON,
        DATA,
        REPORT
    }

    public static String getStringSursa(Sursa sursa) {
        if (sursa==null){
            return "TEXT";
        }
        switch (sursa) {
            case TEXT:
                return "TEXT";
            case COMMON:
                return "COMMON";
            case DATA:
                return "DATA";
            case REPORT:
                return "REPORT";
        }
        return "TEXT";
    }

    public static Sursa getSursaString(String sSursa) {
        if (sSursa==null){
            return Sursa.TEXT;
        }
        switch (sSursa) {
            case "TEXT":
                return Sursa.TEXT;
            case "COMMON":
                return Sursa.COMMON;
            case "DATA":
                return Sursa.DATA;
            case "REPORT":
                return Sursa.REPORT;
        }
        return Sursa.TEXT;
    }
}
