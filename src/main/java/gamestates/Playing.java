package gamestates;

import entities.BasicZombie;
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
            Constants.TILE_SIZE * 59,
            Constants.TILE_SIZE * 2,
            this
    );
    public BasicZombie basicZombie = new BasicZombie(Constants.TILE_SIZE * 59, Constants.TILE_SIZE * 2, player);

    private final Modal pauseMenu = new Modal("Game Paused", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Resume", () -> {
                isPaused = false;
            }),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu", () -> {
                Gamestate.state = Gamestate.MENU;
                resetLevel();
            })
    });
    private final Modal winScreen = new Modal("You Win!", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Restart", this::resetLevel),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu", () -> {
                Gamestate.state = Gamestate.MENU;
                resetLevel();
            })
    });
    private final Modal deathScreen = new Modal("You Died!", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Restart", this::resetLevel),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu", () -> {
                Gamestate.state = Gamestate.MENU;
                resetLevel();
            })
    });
    public boolean isPaused = false;
    public boolean showWinScreen = false;
    public boolean showDeathScreen = false;

    @Override
    public void update() {
        if (!isPaused && !showWinScreen && !showDeathScreen) {
            player.update();
            basicZombie.update();
        } else {
            if (isPaused) pauseMenu.update();
            else if (showDeathScreen) deathScreen.update();
            else if (showWinScreen) winScreen.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        player.draw(g2);
        basicZombie.draw(g2);

        if (isPaused) {
            pauseMenu.draw(g);
        } else if (showDeathScreen) {
            deathScreen.draw(g);
        } else if (showWinScreen) {
            winScreen.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (isPaused) {
            pauseMenu.mousePressed(e);
        } else if (showDeathScreen) {
            deathScreen.mousePressed(e);
        } else if (showWinScreen) {
            winScreen.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isPaused) {
            pauseMenu.mouseReleased(e);
        } else if (showDeathScreen) {
            deathScreen.mouseReleased(e);
        } else if (showWinScreen) {
            winScreen.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (isPaused) {
            pauseMenu.mouseMoved(e);
        } else if (showDeathScreen) {
            deathScreen.mouseMoved(e);
        } else if (showWinScreen) {
            winScreen.mouseMoved(e);
        } else {
            player.pistol.mouseMoved(e);
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
        if (!showWinScreen && !showDeathScreen && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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

    public void resetLevel() {
        this.tileManager = new TileManager();
        this.player = new Player(
                Constants.TILE_SIZE * 59,
                Constants.TILE_SIZE * 2,
                this
        );
        this.basicZombie = new BasicZombie(Constants.TILE_SIZE * 59, Constants.TILE_SIZE * 2, player);
        this.isPaused = false;
        this.showWinScreen = false;
        this.showDeathScreen = false;
    }
}
