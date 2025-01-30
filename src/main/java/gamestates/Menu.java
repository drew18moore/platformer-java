package gamestates;

import ui.MenuButton;
import utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Menu implements Statemethods {
    private MenuButton[] btns = new MenuButton[3];

    public Menu() {
        loadBtns();
    }

    private void loadBtns() {
        btns[0] = new MenuButton(Constants.SCREEN_WIDTH / 2, 50, "Play", Gamestate.PLAYING);
        btns[1] = new MenuButton(Constants.SCREEN_WIDTH / 2, 60 + Constants.BTN_HEIGHT_SCALED, "Options", Gamestate.OPTIONS);
        btns[2] = new MenuButton(Constants.SCREEN_WIDTH / 2, 70 + Constants.BTN_HEIGHT_SCALED * 2, "Quit", Gamestate.QUIT);
    }
    @Override
    public void update() {
        for (MenuButton mb : btns) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        for (MenuButton mb : btns) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : btns) {
            if (mb.bounds.contains(e.getX(), e.getY())) {
                mb.mousePressed = true;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : btns) {
            if (mb.bounds.contains(e.getX(), e.getY())) {
                if (mb.mousePressed) mb.applyGamestate();
                break;
            }
        }
        resetBtns();
    }

    private void resetBtns() {
        for (MenuButton mb : btns) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : btns) {
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
