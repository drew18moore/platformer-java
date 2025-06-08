package com.drewm.gamestates;

import com.drewm.data.LockType;
import com.drewm.entities.BasicZombie;
import com.drewm.entities.Player;
import com.drewm.levels.LevelManager;
import com.drewm.objects.*;
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
    private String currentLevelFilePath = "/maps/map1.json";
    public List<Bullet> bullets = new ArrayList<>();
    public LevelManager levelManager = new LevelManager(this);
    private final List<Explosion> explosions = new ArrayList<>();

    public Player player;
    public Camera camera;
    public Door currentDoor = null;

    private final Modal pauseMenu = new Modal("Game Paused", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, () -> "Resume", () -> {
                isPaused = false;
            }),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, () -> "Main Menu", () -> {
                Gamestate.state = Gamestate.MENU;
                resetLevel();
            })
    });
    private final Modal buyMenu = new Modal("Buy Menu", new Button[]{
            new Button(1, () -> "Health Upgrade [" + player.getHealthUpgradeCost() + "coins]", () -> {
                player.upgradeHealth();
            }, () -> player.getCoins() >= player.getHealthUpgradeCost()),
            new Button(2, () -> "Speed Upgrade [" + player.getSpeedUpgradeCost() + "coins]", () -> {
                player.upgradeSpeed();
            }, () -> player.getCoins() >= player.getSpeedUpgradeCost()),
            new Button(3, () -> "Jump Power Upgrade [" + player.getJumpUpgradeCost() + "coins]", () -> {
                player.upgradeJumpPower();
            }, () -> player.getCoins() >= player.getJumpUpgradeCost()),
            new Button(4, () -> "Buy Pistol [" + 5 + "coins]", () -> {
                player.buyPistol();
            }, () -> player.getCoins() >= 5),
            new Button(5, () -> "Buy Ammo [" + player.getAmmoUpgradeCost() + "coin]", () -> {
                player.buyAmmo();
            }, () -> player.ownsPistol && player.getCoins() >= player.getAmmoUpgradeCost()),
            new Button(6, () -> "Time upgrade [" + player.getTimeUpgradeCost() + " coins]", () -> {
                player.buyTimeUpgrade();
            }, () -> player.getCoins() >= player.getTimeUpgradeCost()),
            new Button(7, () -> "Coin multiplier upgrade [" + player.getCoinMultiplierUpgradeCost() + " coins]", () -> {
                player.buyCoinMultiplier();
            }, () -> player.getCoins() >= player.getCoinMultiplierUpgradeCost())
    });
    private final Modal winScreen = new Modal("You Win!", new Button[]{
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 30 + Constants.BTN_HEIGHT_SCALED, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, () -> "Restart", this::resetLevel),
            new Button(Constants.MODAL_BG_X + (Constants.MODAL_BG_WIDTH - Constants.BTN_WIDTH_SCALED) / 2, Constants.MODAL_BG_Y + 40 + Constants.BTN_HEIGHT_SCALED * 2, Constants.BTN_WIDTH_SCALED, Constants.BTN_HEIGHT_SCALED, () -> "Main Menu", () -> {
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
                if (door.getScreenBounds().intersects(player.getScreenBounds())) {
                    currentDoor = door;
                    break;
                }
            }

            Iterator<FloatingMine> floatingMineIterator = this.levelManager.getCurrentRoom().getFloatingMines().iterator();
            while(floatingMineIterator.hasNext()) {
                FloatingMine floatingMine = floatingMineIterator.next();
                if (floatingMine.update()) {
                    explosions.add(new Explosion(floatingMine.getWorldX() + 16 * Constants.SCALE, floatingMine.getWorldY() + 16 * Constants.SCALE));
                    floatingMineIterator.remove();
                    player.takeDamage(1);
                }
            }

            for (SawBlade sawBlade : this.levelManager.getCurrentRoom().getSawBlades()) {
                sawBlade.update();
                if (sawBlade.getScreenBounds().intersects(player.getScreenBounds())) {
                    player.takeDamage(1);
                }
            }

            for (Laser laser : this.levelManager.getCurrentRoom().getLasers()) {
                laser.update();
                if (laser.isActive() && laser.getScreenBounds().intersects(player.getScreenBounds())) {
                    player.takeDamage(1);
                }
            }

            Iterator<Explosion> explosionIterator = explosions.iterator();
            while (explosionIterator.hasNext()) {
                Explosion explosion = explosionIterator.next();
                explosion.update();
                if (explosion.isFinished()) {
                    explosionIterator.remove();
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

        for (FloatingMine fm : new ArrayList<>(levelManager.getCurrentRoom().getFloatingMines())) {
            fm.draw(g2);
        }

        for (SawBlade sb : new ArrayList<>(this.levelManager.getCurrentRoom().getSawBlades())) {
            sb.draw(g2);
        }

        for (Laser l : new ArrayList<>(this.levelManager.getCurrentRoom().getLasers())) {
            l.draw(g2);
        }

        for (Explosion explosion : explosions) {
            explosion.draw(g2, camera.getCameraX(), camera.getCameraY());
        }

        for (MovingPlatform movingPlatform : this.levelManager.getCurrentRoom().getMovingPlatforms()) {
            movingPlatform.draw(g2);
        }

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
                if (currentDoor.getIsGoal()) {
                    this.showWinScreen = true;
                } else {
                    if (currentDoor.getLockType() == LockType.NONE || (currentDoor.getLockType() == LockType.KEYCARD && player.hasKeycard)) {
                        float destinationX = this.currentDoor.getDestinationX();
                        float destinationY = this.currentDoor.getDestinationY();
                        this.levelManager.setCurrentRoomIdx(this.currentDoor.getDestinationRoomIdx());
                        this.player.worldX = destinationX;
                        this.player.worldY = destinationY;
                    }
                }
            }
        }
        if (!showWinScreen && !showDeathScreen && !showBuyMenu && e.getKeyCode() == KeyEvent.VK_R) {
            player.setCurrentTimeLeft(0);
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

    public List<Explosion> getExplosions() {
        return explosions;
    }
}
