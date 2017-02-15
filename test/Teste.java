
import dorel.simplejavareport.report.JavaReport;

public class Teste {
    
    public static void main(String[] args) {
        JavaReport rp= new JavaReport(null,"example.xml",null, null);
        rp.preview();
        //rp.print(false, false);
    }

}
