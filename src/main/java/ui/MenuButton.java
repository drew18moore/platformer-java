package ui;

import gamestates.Gamestate;
import utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuButton {
    private int xPos, yPos, index;
    private int width = Constants.BTN_WIDTH_SCALED, height = Constants.BTN_HEIGHT_SCALED;
    private int xOffsetCenter = Constants.BTN_WIDTH_SCALED / 2;

    public boolean mouseOver, mousePressed;
    public Rectangle bounds;
    private Gamestate gamestate;
    private BufferedImage[] imgs;

    public MenuButton(int xPos, int yPos, String label, Gamestate gamestate) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.gamestate = gamestate;
        loadImgs(label);
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, width, height);
    }

    private void loadImgs(String label) {
        imgs = new BufferedImage[3];

        imgs[0] = createBtnImg(label, width, height, Color.DARK_GRAY, Color.WHITE);
        imgs[1] = createBtnImg(label, width, height, Color.GRAY, Color.YELLOW);
        imgs[2] = createBtnImg(label, width, height, Color.BLACK, Color.RED);
    }

    private BufferedImage createBtnImg(String label, int width, int height, Color bgColor, Color textColor) {
        BufferedImage btnImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = btnImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(bgColor);
        g2.fillRect(0, 0, width, height);

        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, width - 1, height - 1);

        g2.setFont(new Font("Arial", Font.BOLD, height / 3));
        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(label);

        g2.setColor(textColor);
        g2.drawString(label, (width - textWidth) / 2, (height - fm.getHeight()) / 2 + fm.getAscent());

        g2.dispose();
        return btnImg;
    }

    public void draw(Graphics g) {
        g.drawImage(imgs[index], xPos - xOffsetCenter, yPos, width, height, null);
    }

    public void update() {
        index = 0;
        if (mouseOver) index = 1;
        if (mousePressed) index = 2;
    }

    public void applyGamestate() {
        Gamestate.state = gamestate;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
