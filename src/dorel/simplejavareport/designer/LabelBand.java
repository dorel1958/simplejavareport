package dorel.simplejavareport.designer;

import dorel.simplejavareport.report.components.Band;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

// label utilizat in designer pt reprezentarea benzilor
public class LabelBand extends JLabel {

    Band contentBand;
    
    public LabelBand(Band contentBand) {
        super("");
        this.contentBand=contentBand;
        setBorder(BorderFactory.createLineBorder(Color.lightGray));
    }

    public boolean testInBand(int yEti) {
        return (getY() <= yEti && yEti <= (getY() + getHeight()));
    }

    public boolean isHigherThenBand(int etiRealBottom) {
        return (etiRealBottom <= (getY() + getHeight()));
    }
}
