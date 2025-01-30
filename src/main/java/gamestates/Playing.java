package gamestates;

import entities.Player;
import levels.TileManager;
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

    @Override
    public void update() {
        player.update();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        player.draw(g2);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

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
