package gamestates;

import entities.Player;
import levels.TileManager;
import ui.Button;
import ui.Modal;
import utils.Constants;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Playing implements Statemethods {
    public TileManager tileManager = new TileManager();
    public Player player = new Player(
            Constants.TILE_SIZE * 2,
            Constants.TILE_SIZE * 2
    );
    public Modal pauseMenu = new Modal("Game Paused", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Resume", () -> {
                isPaused = false;
            }),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu", () -> {
                Gamestate.state = Gamestate.MENU;
                isPaused = false;
            })
    });
    public boolean isPaused = false;

    @Override
    public void update() {
        if (!isPaused) {
            player.update();
        } else {
            pauseMenu.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        player.draw(g2);

        if (isPaused) {
            pauseMenu.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (isPaused) {
            pauseMenu.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isPaused) {
            pauseMenu.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isPaused) {
            pauseMenu.mouseMoved(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.jumpPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.leftPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.rightPressed = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            this.isPaused = !this.isPaused;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.jumpPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            player.leftPressed = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            player.rightPressed = false;
        }
    }
}
