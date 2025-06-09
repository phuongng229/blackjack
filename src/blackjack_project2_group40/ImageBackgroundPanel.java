/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package blackjack_project2_group40;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author Jonathan
 */
public class ImageBackgroundPanel extends JPanel {
    private final Image background;

    public ImageBackgroundPanel(Image background, LayoutManager layout) {
        super(layout);
        this.background = background;
        setOpaque(false); // Important!
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (background != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();

            int imgWidth = background.getWidth(this);
            int imgHeight = background.getHeight(this);

            if (imgWidth > 0 && imgHeight > 0) {
                float panelRatio = (float) panelWidth / panelHeight;
                float imageRatio = (float) imgWidth / imgHeight;

                int drawWidth, drawHeight;
                if (imageRatio > panelRatio) {
                    drawHeight = panelHeight;
                    drawWidth = (int) (panelHeight * imageRatio);
                } else {
                    drawWidth = panelWidth;
                    drawHeight = (int) (panelWidth / imageRatio);
                }

                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;

                g.drawImage(background, x, y, drawWidth, drawHeight, this);
            }
        }
    }
}
