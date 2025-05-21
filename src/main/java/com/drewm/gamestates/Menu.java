package com.drewm.gamestates;

import com.drewm.ui.Button;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Menu implements Statemethods {
    private final BufferedImage titleText;
    private Button[] btns = new Button[3];

    public Menu() {
        this.titleText = createTitleText(Constants.SCREEN_TITLE);
        loadBtns();
    }

    private BufferedImage createTitleText(String text) {
        BufferedImage headerText = new BufferedImage(Constants.BTN_WIDTH_SCALED*2, Constants.BTN_HEIGHT_SCALED*2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = headerText.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(new Font("Arial", Font.BOLD, Constants.BTN_HEIGHT_SCALED*2 / 3));
        FontMetrics fm = g2.getFontMetrics();
        g2.setColor(Color.BLACK);
        g2.drawString(text, (Constants.BTN_WIDTH_SCALED*2 - fm.stringWidth((text))) / 2, (Constants.BTN_HEIGHT_SCALED*2 - fm.getHeight()) / 2 + fm.getAscent());
        g2.dispose();
        return headerText;
    }

    private void loadBtns() {
        btns[0] = new Button((Constants.SCREEN_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, this.titleText.getHeight(), Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Play", () -> { Gamestate.state = Gamestate.PLAYING; });
        btns[1] = new Button((Constants.SCREEN_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, this.titleText.getHeight() + Constants.BTN_HEIGHT_SCALED + 10, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED,  "Options", () -> { Gamestate.state = Gamestate.OPTIONS; });
        btns[2] = new Button((Constants.SCREEN_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, this.titleText.getHeight() + (Constants.BTN_HEIGHT_SCALED + 10) * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED,  "Quit", () -> { Gamestate.state = Gamestate.QUIT; });
    }
    @Override
    public void update() {
        for (Button mb : btns) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(titleText, (Constants.SCREEN_WIDTH - titleText.getWidth()) / 2, 0, null);
        for (Button mb : btns) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Button mb : btns) {
            if (mb.bounds.contains(e.getX(), e.getY())) {
                mb.mousePressed = true;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Button mb : btns) {
            if (mb.bounds.contains(e.getX(), e.getY())) {
                if (mb.mousePressed) mb.onClick.run();
                break;
            }
        }
        resetBtns();
    }

    private void resetBtns() {
        for (Button mb : btns) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Button mb : btns) {
            if (mb.bounds.contains(e.getX(), e.getY())) {
                mb.mouseOver = true;
            } else mb.mouseOver = false;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Gamestate.state = Gamestate.PLAYING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
