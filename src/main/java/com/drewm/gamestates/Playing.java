package com.drewm.gamestates;

import com.drewm.data.LockType;
import com.drewm.entities.BasicZombie;
import com.drewm.entities.Player;
import com.drewm.levels.LevelManager;
import com.drewm.objects.Collectable;
import com.drewm.objects.Door;
import com.drewm.ui.Button;
import com.drewm.ui.Camera;
import com.drewm.ui.Modal;
import com.drewm.utils.Constants;
import com.drewm.weapons.Bullet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Playing implements Statemethods {
    private String currentLevelFilePath = "/maps/map1-lg.json";
    public List<Bullet> bullets = new ArrayList<>();
    public LevelManager levelManager = new LevelManager(this);
    public Player player;
    public Camera camera;
    public Door currentDoor = null;

    private final Modal pauseMenu = new Modal("Game Paused", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Resume", () -> {
                isPaused = false;
            }),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu", () -> {
                Gamestate.state = Gamestate.MENU;
                resetLevel();
            })
    });
    private final Modal buyMenu = new Modal("Buy Menu", new Button[]{
            new Button((Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, (int) (Constants.SCREEN_WIDTH * 0.5f), Constants.BTN_HEIGHT_SCALED, "Health Upgrade [5 coins]", () -> {
                player.upgradeHealth();
            }, () -> player.getCoins() >= 5),
            new Button((Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, (int) (Constants.SCREEN_WIDTH * 0.5f), Constants.BTN_HEIGHT_SCALED, "Speed Upgrade [5 coins]", () -> {
                player.upgradeSpeed();
            }, () -> player.getCoins() >= 5),
            new Button((Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2, Constants.MODAL_BG_Y + 50 + Constants.BTN_HEIGHT_SCALED * 3, (int) (Constants.SCREEN_WIDTH * 0.5f), Constants.BTN_HEIGHT_SCALED, "Jump Power Upgrade [5 coins]", () -> {
                player.upgradeJumpPower();
            }, () -> player.getCoins() >= 5),
            new Button((Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2, Constants.MODAL_BG_Y + 60 + Constants.BTN_HEIGHT_SCALED * 4, (int) (Constants.SCREEN_WIDTH * 0.5f), Constants.BTN_HEIGHT_SCALED, "Buy Pistol [5 coins]", () -> {
                player.buyPistol();
            }, () -> player.getCoins() >= 5),
            new Button((Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2, Constants.MODAL_BG_Y + 70 + Constants.BTN_HEIGHT_SCALED * 5, (int) (Constants.SCREEN_WIDTH * 0.5f), Constants.BTN_HEIGHT_SCALED, "Buy Ammo [1 coin]", () -> {
                player.buyAmmo();
            }, () -> player.ownsPistol && player.getCoins() >= 1),
            new Button((Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2, Constants.MODAL_BG_Y + 80 + Constants.BTN_HEIGHT_SCALED * 6, (int) (Constants.SCREEN_WIDTH * 0.5f), Constants.BTN_HEIGHT_SCALED, "Time upgrade [10 coins]", () -> {
                player.buyTimeUpgrade();
            }, () -> player.getCoins() >= 10),
            new Button((Constants.SCREEN_WIDTH - (int) (Constants.SCREEN_WIDTH * 0.5f)) / 2, Constants.MODAL_BG_Y + 90 + Constants.BTN_HEIGHT_SCALED * 7, (int) (Constants.SCREEN_WIDTH * 0.5f), Constants.BTN_HEIGHT_SCALED, "Coin multiplier upgrade [20 coins]", () -> {
                player.buyCoinMultiplier();
            }, () -> player.getCoins() >= 20)
    });
    private final Modal winScreen = new Modal("You Win!", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Restart", this::resetLevel),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, "Main Menu", () -> {
                Gamestate.state = Gamestate.MENU;
                resetLevel();
            })
    });
    public boolean isPaused = false;
    public boolean showBuyMenu = false;
    public boolean showWinScreen = false;
    public boolean showDeathScreen = false;

    public Playing() {
        this.camera = new Camera(this.levelManager);
    }

    @Override
    public void update() {
        if (!isPaused && !showWinScreen && !showDeathScreen && !showBuyMenu) {
            player.update();
            camera.update(player);

            Iterator<Bullet> bulletIterator = bullets.iterator();
            while(bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (bullet.update(this.levelManager.getCurrentRoom().getBasicZombies())) {
                    bulletIterator.remove();
                }
            }

            Iterator<BasicZombie> zombieIterator = this.levelManager.getCurrentRoom().getBasicZombies().iterator();
            while (zombieIterator.hasNext()) {
                BasicZombie zombie = zombieIterator.next();
                zombie.update();
                if (zombie.health <= 0) {
                    zombie.handleDrop();
                    zombieIterator.remove();
                }
            }

            Iterator<Collectable> collectableIterator = this.levelManager.getCurrentRoom().getCollectables().iterator();
            while(collectableIterator.hasNext()) {
                Collectable collectable = collectableIterator.next();
                if (collectable.update()) {
                    collectableIterator.remove();
                }
            }

            currentDoor = null;
            for (Door door : this.levelManager.getCurrentRoom().getDoors()) {
                if (door.getScreenBounds().intersects(player.getBounds())) {
                    currentDoor = door;
                    break;
                }
            }

        } else {
            Modal activeModal = getActiveModal();
            if (activeModal != null) activeModal.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        levelManager.draw(g2);
        player.draw(g2);
        for (BasicZombie zombie : new ArrayList<>(levelManager.getCurrentRoom().getBasicZombies())) {
            zombie.draw(g2);
        }

        for (Collectable c : new ArrayList<>(levelManager.getCurrentRoom().getCollectables())) {
            c.draw(g2);
        }

        for (Bullet b : new ArrayList<>(bullets)) {
            b.draw(g2);
        }
        this.levelManager.getCurrentRoom().getDoors().forEach(door -> door.draw(g2));

        Modal activeModal = getActiveModal();
        if (activeModal != null) activeModal.draw(g);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Modal activeModal = getActiveModal();
        if (activeModal != null) activeModal.mousePressed(e);
        else player.pistol.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Modal activeModal = getActiveModal();
        if (activeModal != null) activeModal.mouseReleased(e);
        else player.pistol.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Modal activeModal = getActiveModal();
        if (activeModal != null) activeModal.mouseMoved(e);
        else player.pistol.mouseMoved(e);
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
        if (e.getKeyCode() == KeyEvent.VK_W) {
            if (currentDoor != null) {
                if (currentDoor.getLockType() == LockType.NONE || (currentDoor.getLockType() == LockType.KEYCARD && player.hasKeycard)) {
                    float destinationX = this.currentDoor.getDestinationX();
                    float destinationY = this.currentDoor.getDestinationY();
                    this.levelManager.setCurrentRoomIdx(this.currentDoor.getDestinationRoomIdx());
                    this.player.worldX = destinationX;
                    this.player.worldY = destinationY;
                }
            }
        }
        if (!showWinScreen && !showDeathScreen && e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (showBuyMenu) showBuyMenu = false;
            else this.isPaused = !this.isPaused;
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

    private Modal getActiveModal() {
        if (isPaused) return pauseMenu;
        if (showBuyMenu) return buyMenu;
        if (showWinScreen) return winScreen;
        return null;
    }

    private void resetBools() {
        this.isPaused = false;
        this.showBuyMenu = false;
        this.showWinScreen = false;
        this.showDeathScreen = false;
    }

    public void resetLevel() {
        this.levelManager = new LevelManager(this);
        this.bullets.clear();

        resetBools();
    }

    public void respawn() {
        this.bullets.clear();
        levelManager.loadRooms(getCurrentLevelFilePath(), true);
        player.respawn(levelManager.getPlayerSpawnX(), levelManager.getPlayerSpawnY());

        resetBools();
    }

    public String getCurrentLevelFilePath() {
        return this.currentLevelFilePath;
    }
}
