package com.drewm.gamestates;

import com.drewm.main.Window;
import com.drewm.ui.Button;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Options implements Statemethods {
    private final Window window;
    private Button[] btns = new Button[2];

    public Options(Window window) {
        this.window = window;
        loadBtns();
    }

    private void loadBtns() {
        btns[0] = new Button((Constants.SCREEN_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, 50, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, window.getIsFullscreen() ? "Disable Fullscreen" : "Enable Fullscreen", () -> {
            window.setIsFullscreen(!window.getIsFullscreen());
            loadBtns();
        });
        btns[1] = new Button((Constants.SCREEN_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, 60 + Constants.BTN_HEIGHT_SCALED,Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED,  "Back", () -> { Gamestate.state = Gamestate.MENU; });
    }
    @Override
    public void update() {
        for (Button mb : btns) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
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
