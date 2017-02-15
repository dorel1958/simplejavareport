package dorel.simplejavareport.designer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.List;

class EtiSelection implements Transferable {
    // Utilizate la Copy si Paste in Clipboard

    public static final DataFlavor etiFlavor = new DataFlavor(Eti.class, "Eti data flavor");
    private final List<Eti> lEtiSel;

    public EtiSelection(List<Eti> lEtiSel) {
        this.lEtiSel = lEtiSel;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] ret = {etiFlavor};
        return ret;

    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor df) {
        return etiFlavor.equals(df);
    }

    @Override
    public Object getTransferData(DataFlavor df) {
        if (isDataFlavorSupported(df)) {
            return this.lEtiSel;
        } else {
            return null;
        }
    }

}
