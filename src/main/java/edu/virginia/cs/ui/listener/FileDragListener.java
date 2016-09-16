package edu.virginia.cs.ui.listener;

import edu.virginia.cs.ui.CloudLinkPanel;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/9/15.
 */
public class FileDragListener implements DropTargetListener {
    private CloudLinkPanel panel;
    public FileDragListener(CloudLinkPanel panel){
        this.panel = panel;
    }
    @Override
    public void drop(DropTargetDropEvent event) {

        // Accept copy drops
        event.acceptDrop(DnDConstants.ACTION_COPY);

        // Get the transfer which can provide the dropped item data
        Transferable transferable = event.getTransferable();

        // Get the data formats of the dropped item
        DataFlavor[] flavors = transferable.getTransferDataFlavors();
        for (DataFlavor flavor : flavors) {
            try {
                    List<File> files = (List<File>) transferable.getTransferData(flavor);
                    // Loop them through
                    if(!flavor.getMimeType().equals("text/directory")) {
                        for (File file : files) {
                            if (file.isDirectory()) {
                                JOptionPane.showMessageDialog(null, "this tool doesn't support drag and drop folders!", "Waring", JOptionPane.WARNING_MESSAGE);
                            } else {
                                panel.doUploadFile(file);
                            }
                        }
                    }
            } catch (Exception e) {
                // Print out the error stack
                e.printStackTrace();
            }
        }
        // Inform that the drop is complete
        event.dropComplete(true);

    }

    @Override
    public void dragEnter(DropTargetDragEvent event) {
    }

    @Override
    public void dragExit(DropTargetEvent event) {
    }

    @Override
    public void dragOver(DropTargetDragEvent event) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent event) {
    }
}
