package dorel.simplejavareport.tools;

public class Calc {
//
//    public static int mmToPix(double val_mm) {
//        return (int) (val_mm * ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public static double pixToMm(int val_pix) {
//        return (double) Math.round((double) val_pix / ReportContent.MM_PUNCTE_SCREEN);
//    }
//
//    public static int mmToDot(double val_mm) {
//        return (int) (val_mm * ReportContent.MM_PUNCTE_PRINT);
//    }
//
//    public static double dotToMm(int val_dot) {
//        return (double) Math.round((double) val_dot / ReportContent.MM_PUNCTE_PRINT);
//    }

    public static double round(double val, int nrDec) {
        double zecP = Math.pow(10, nrDec);
        return Math.round(zecP * val) / zecP;
    }

    public static boolean isInteger(String stringul) {
        for (char c : stringul.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNumeric(String stringul) {
        try {
            double nDbl = Double.parseDouble(stringul);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static int ugToPix(int ug) {
        return ug / 72 * 96;
    }

    public static int pixToUg(int pix) {
        return pix / 96 * 72;
    }
}
