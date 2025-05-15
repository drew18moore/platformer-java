package com.drewm.ui;

import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public class Button {
    public int x, y, width, height, index;
    private Supplier<Boolean> enabledSupplier;
    public Runnable onClick;

    public boolean mouseOver, mousePressed;
    public Rectangle bounds;
    private BufferedImage[] imgs;

    public Button(int x, int y, int width, int height, String label, Runnable onClick) {
        this(x, y, width, height, label, onClick, () -> true);
    }

    public Button(int idx, String label, Runnable onClick, Supplier<Boolean> enabledSupplier) {
        this(
                (Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2,
                Constants.MODAL_BG_Y + Constants.BTN_HEIGHT_SCALED * idx,
                (int) (Constants.SCREEN_WIDTH * 0.5f),
                Constants.BTN_HEIGHT_SCALED,
                label,
                onClick,
                enabledSupplier
        );
    }

    public Button(int x, int y, int width, int height, String label, Runnable onClick, Supplier<Boolean> enabledSupplier) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.onClick = onClick;
        loadImgs(label);
        this.bounds = new Rectangle(x, y, width, height);
        this.enabledSupplier = enabledSupplier;
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

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isEnabled() {
        return enabledSupplier.get();
    }

    public void update() {
        index = 0;
        if (mouseOver) index = 1;
        if (mousePressed) index = 2;
    }

    public void draw(Graphics g) {
        int drawIndex = isEnabled() ? index : 0;
        g.drawImage(imgs[drawIndex], x, y, width, height, null);

        if (!isEnabled()) {
            g.setColor(new Color(0, 0, 0, 100)); // overlay grey-out
            g.fillRect(x, y, width, height);
        }
    }
}
