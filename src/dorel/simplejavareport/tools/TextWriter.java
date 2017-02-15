package dorel.simplejavareport.tools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TextWriter {

    private BufferedWriter bw;
    private String mesajEroare;

    public String getMesajEroare() {
        return mesajEroare;
    }

    public TextWriter(String fileName, boolean append) {
        mesajEroare = "";
        bw = null;
        if (fileName == null) {
            mesajEroare = "Numele fisierului nu poate fi null.";
            return;
        }
        if (fileName.length() > 0) {
        } else {
            mesajEroare = "Nu ati dat numele fisierului.";
            return;
        }
        try {
            //creaza obiectul BufferedWriter
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, append), "UTF-8"));
        } catch (FileNotFoundException ex) {
            mesajEroare = "FileNotFound exception: " + ex.getLocalizedMessage();
        } catch (IOException ex) {
            mesajEroare = "IO exception: " + ex.getLocalizedMessage();
        }
    }

    public boolean writeLine(String linia) {
        mesajEroare = "";
        if (bw == null) {
            mesajEroare = "Fisierul nu este deschis pentru scriere.";
            return false;
        }
        try {
            bw.write(linia);
            bw.newLine();
            return true;
        } catch (IOException ex) {
            mesajEroare = "IO exception: " + ex.getLocalizedMessage();
        }
        return false;
    }

    public boolean write(String linia) {
        mesajEroare = "";
        if (bw == null) {
            mesajEroare = "Fisierul nu este deschis pentru scriere.";
            return false;
        }
        try {
            bw.write(linia);
            return true;
        } catch (IOException ex) {
            mesajEroare = "IO exception: " + ex.getLocalizedMessage();
        }
        return false;
    }

    public boolean write(int intreg) {
        // scrie intregul (considerat codul caracterului UTF8) ca un caracter UTF8
        mesajEroare = "";
        if (bw == null) {
            mesajEroare = "Fisierul nu este deschis pentru scriere.";
            return false;
        }
        try {
            bw.write(intreg);
            return true;
        } catch (IOException ex) {
            mesajEroare = "IO exception: " + ex.getLocalizedMessage();
        }
        return false;
    }

    public boolean newLine() {
        // scrie intregul (considerat codul caracterului UTF8) ca un caracter UTF8
        mesajEroare = "";
        if (bw == null) {
            mesajEroare = "Fisierul nu este deschis pentru scriere.";
            return false;
        }
        try {
            bw.newLine();
            return true;
        } catch (IOException ex) {
            mesajEroare = "IO exception: " + ex.getLocalizedMessage();
        }
        return false;
    }

    public boolean close() {
        mesajEroare = "";
        try {
            if (bw != null) {
                bw.close();
            }
            return true;
        } catch (IOException ex) {
            mesajEroare = "IO exception: " + ex.getLocalizedMessage();
        }
        return false;
    }
}
