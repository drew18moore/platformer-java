package ui;

import utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Modal {
    public BufferedImage backgroundImg;
    public int bgX, bgY, bgW, bgH;
    private final BufferedImage headerText;
    public Button[] buttons;

    public Modal(String headerText, Button[] buttons) {
        this.backgroundImg = createBackground();
        this.headerText = createHeaderText(headerText);
        this.buttons = buttons;
    }

    private BufferedImage createBackground() {
        bgW = (int) (Constants.SCREEN_WIDTH * 0.5f);
        bgH = (int) (Constants.SCREEN_HEIGHT * 0.7f);
        bgX = (Constants.SCREEN_WIDTH - bgW) / 2;
        bgY = (Constants.SCREEN_HEIGHT - bgH) / 2;
        BufferedImage bg = new BufferedImage(bgW, bgH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(0, 0, bgW, bgH);

        g2.dispose();
        return bg;
    }

    private BufferedImage createHeaderText(String text) {
        BufferedImage headerText = new BufferedImage(Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = headerText.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(new Font("Arial", Font.BOLD, Constants.BTN_HEIGHT_SCALED / 3));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.BLACK);
        g2.drawString(text, (Constants.BTN_WIDTH_SCALED - fm.stringWidth(("Game Paused"))) / 2, (Constants.BTN_HEIGHT_SCALED - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
        return headerText;
    }

    public void update() {
        for (Button b: buttons) {
            b.update();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);
        g.drawImage(headerText, bgX + (bgW - Constants.BTN_WIDTH_SCALED) / 2, bgY + 20, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, null);

        for (Button b: buttons) {
            b.draw(g);
        }
    }
}
