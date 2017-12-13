package codeTigers.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Picture Class to display images on the test
 *
 * @author Marc Goodman, Wally Haven
 * @version 11/30/2017.
 * <p>
 * Modifications:  Wally Haven Removed mouse event and callback code.
 *                 Added logic to supply default text if an image is null.
 */
public class PicturePanel extends JPanel {
    private BufferedImage image = null;

    public PicturePanel() {
    }

    public void setImage(BufferedImage value) {
        image = value;
        repaint();
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
            // Draw no image text
            String DROP_TEXT = "No Image Available";
            g.setFont(new Font("TimesRoman", Font.PLAIN, 22));
            int textWidth = g.getFontMetrics().stringWidth(DROP_TEXT);
            int textHeight = g.getFontMetrics().getHeight();
            g.drawString(DROP_TEXT, (panelWidth - textWidth) / 2, (panelHeight + textHeight / 2) / 2);
        }
        // Draw frame around panel
        g.drawRect(0, 0, panelWidth - 1, panelHeight - 1);
    }

}
