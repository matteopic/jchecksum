/*
 * FileAndTextTransferHandler.java is used by the 1.4
 * DragFileDemo.java example.
 */

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public abstract class FileTransferHandler extends TransferHandler {

    private DataFlavor fileFlavor;

    public FileTransferHandler() {
    	fileFlavor = DataFlavor.javaFileListFlavor;
    }

    public boolean importData(JComponent c, Transferable t) {
        if (!canImport(c, t.getTransferDataFlavors())) {
            return false;
        }

        try {
            if (hasFileFlavor(t.getTransferDataFlavors())) {
                List files =
                     (List)t.getTransferData(fileFlavor);
                for (int i = 0; i < files.size(); i++) {
                    File file = (File)files.get(i);
                    fileDropped(file);
                }
                return true;
            }
        } catch (Exception ufe) {}

        return false;
    }


    public boolean canImport(JComponent c, DataFlavor[] flavors) {
        if (hasFileFlavor(flavors))   { return true; }
        return false;
    }

    private boolean hasFileFlavor(DataFlavor[] flavors) {
        for (int i = 0; i < flavors.length; i++) {
            if (fileFlavor.equals(flavors[i])) {
                return true;
            }
        }
        return false;
    }

    public abstract void fileDropped(File f);

}