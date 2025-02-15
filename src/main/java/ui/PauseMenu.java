package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import utils.Constants;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PauseMenu extends Modal {
    public PauseMenu(Playing playing) {
        super("Game Paused", new Button[]{
                new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Resume", () -> { playing.isPaused = false; }),
                new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu", () -> { Gamestate.state = Gamestate.MENU; playing.isPaused = false; }) });

    }

    public void update() {
        super.update();
    }

    public void draw(Graphics g) {
        super.draw(g);
    }

    public void mousePressed(MouseEvent e) {
        for (Button b : buttons) {
            if (b.bounds.contains(e.getX(), e.getY())) {
                b.mousePressed = true;
                break;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        for (Button b : buttons) {
            if (b.bounds.contains(e.getX(), e.getY())) {
                if (b.mousePressed) b.onClick.run();
                break;
            }
        }
        resetBtns();
    }

    public void mouseMoved(MouseEvent e) {
        for (Button b : buttons) {
            if (b.bounds.contains(e.getX(), e.getY())) {
                b.mouseOver = true;
            } else b.mouseOver = false;
        }
    }

    private void resetBtns() {
        for (Button mb : buttons) {
            mb.resetBools();
        }
    }
}
