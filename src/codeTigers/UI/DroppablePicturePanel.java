package codeTigers.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DroppablePicturePanel extends JPanel implements DropTargetListener {
    private String DROP_TEXT = "Drop Image Here";
    private DropTarget target;
    private boolean dragging = false;
    private BufferedImage image = null;
    private String imagePath = "";
    private Runnable callback = null;

    public DroppablePicturePanel() {
        super();
        target = new DropTarget(this, this);
    }

    // this is how we make a runnable callback
    public void setCallback(Runnable c) {
        callback = c;
    }

    public void setImage(BufferedImage value) {
        image = value;
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public void paint(Graphics g) {
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();

        g.clearRect(0, 0, panelWidth - 1, panelHeight - 1);

        if (image != null) {
            // Draw image
            g.drawImage(image, 0, 0, panelWidth, panelHeight, null);
        } else {
            // Draw drop text
            int textWidth = g.getFontMetrics().stringWidth(DROP_TEXT);
            int textHeight = g.getFontMetrics().getHeight();
            g.drawString(DROP_TEXT, (panelWidth - textWidth) / 2, (panelHeight + textHeight / 2)/2);
        }

        // Highlight the panel to show the user is dragging over it.
        if (dragging) {
            g.setColor(new Color(0, 0, 255, 64));
            g.fillRect(0, 0, panelWidth - 1, panelHeight - 1);
            g.setColor(Color.BLACK);
        }

        // Draw frame around panel
        g.drawRect(0, 0, panelWidth - 1, panelHeight - 1);
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        // System.out.println("Drag Enter");
        dragging = true;
        repaint();
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // System.out.println("Drag Over");
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // System.out.println("Drop Action Changed");
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        // System.out.println("Drag Exit");
        dragging = false;
        repaint();
    }

    public String getCurrentImagePath(){
        return imagePath;
    }

    private void loadFile(File file) {
        try {
            image = ImageIO.read(file);
            imagePath = file.toString();
            System.out.println(file);

            // here is where we tell the thing to run the callback
            if (callback != null)
                callback.run();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        dragging = false;
        System.out.println("Drop");
        Transferable tr = dtde.getTransferable();
        DataFlavor[] flavors = tr.getTransferDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            if (flavors[i].isFlavorJavaFileListType()) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

                // And add the list of file names to our text area
                java.util.List list = null;
                try {
                    list = (java.util.List)tr.getTransferData(flavors[i]);
                    if (list.size() > 0)
                        loadFile((File)list.get(0));
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // If we made it this far, everything worked.
                dtde.dropComplete(true);
                repaint();
                return;
            }
        }
        repaint();
    }
}
