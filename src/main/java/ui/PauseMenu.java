package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.Constants;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class PauseMenu {
    private Playing playing;
    public BufferedImage backgroundImg;
    public int bgX, bgY, bgW, bgH;
    private BufferedImage headerText;
    public PauseButton resumeB, menuB;

    public PauseMenu(Playing playing) {
        this.playing = playing;
        // Create background
        backgroundImg = createBackground();
        // Create Header
        headerText = createHeaderText();
        // Create buttons
        resumeB = new PauseButton(bgX + (bgW - Constants.BTN_WIDTH_SCALED) / 2, bgY + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Resume");
        menuB = new PauseButton(bgX + (bgW - Constants.BTN_WIDTH_SCALED) / 2, bgY + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu");
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

    private BufferedImage createHeaderText() {
        BufferedImage headerText = new BufferedImage(Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = headerText.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(new Font("Arial", Font.BOLD, Constants.BTN_HEIGHT_SCALED / 3));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.BLACK);
        g2.drawString("Game Paused", (Constants.BTN_WIDTH_SCALED - fm.stringWidth(("Game Paused"))) / 2, (Constants.BTN_HEIGHT_SCALED - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
        return headerText;
    }


    public void update() {
        resumeB.update();
        menuB.update();
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, bgX, bgY, bgW, bgH, null);

        g.drawImage(headerText, bgX + (bgW - Constants.BTN_WIDTH_SCALED) / 2, bgY + 20, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, null);
        resumeB.draw(g);
        menuB.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        if (isIn(e, resumeB)) {
            resumeB.mousePressed = true;
        } else if (isIn(e, menuB)) {
            menuB.mousePressed = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (isIn(e, resumeB) && resumeB.mousePressed) {
            playing.isPaused = false;
        } else if (isIn(e, menuB) && menuB.mousePressed) {
            Gamestate.state = Gamestate.MENU;
            playing.isPaused = false;
        }

        resumeB.resetBools();
        menuB.resetBools();
    }

    public void mouseMoved(MouseEvent e) {

    }

    private boolean isIn(MouseEvent e, PauseButton pb) {
        return pb.bounds.contains(e.getX(), e.getY());
    }
}
