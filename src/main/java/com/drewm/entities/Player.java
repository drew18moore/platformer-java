package com.drewm.entities;

import com.drewm.gamestates.Playing;
import com.drewm.levels.LevelManager;
import com.drewm.main.Window;
import com.drewm.utils.Constants;
import com.drewm.weapons.Bullet;
import com.drewm.weapons.Pistol;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Player extends Entity {
    public boolean leftPressed, rightPressed, jumpPressed;

    private int coinMultiplier = 1;
    private int coins = 100000;
    private int maxHealth = Constants.PLAYER_STARTING_MAX_HEALTH;
    private int health = maxHealth;
    private float speed = Constants.PLAYER_STARTING_SPEED;

    private int healthUpgradeLvl = 1;
    private int speedUpgradeLvl = 1;
    private int jumpUpgradeLvl = 1;
    private int timeUpgradeLvl = 1;

    public boolean invincible = false;
    private long invincibilityStartTime = 0;
    private final int INVINCIBILITY_DURATION = 500;

    private int maxTimeLimitSeconds = 5;
    private int currentTimeLeft = 0;
    private long lastCheck = System.currentTimeMillis();

    public boolean ownsPistol = false;
    public Pistol pistol;

    public boolean hasKeycard = false;

    private BufferedImage healthBar = updateHudText("Health: ", health);
    private BufferedImage coinCount = updateHudText("Coins: ", coins);
    private BufferedImage timeLeft = updateHudText("", currentTimeLeft);
    private BufferedImage keycardIcon = null;

    public Player(float worldX, float worldY, Playing playing, List<Bullet> bullets) {
        super(worldX, worldY, null, Constants.PLAYER_SPRITE_WIDTH, Constants.PLAYER_SPRITE_HEIGHT, playing);
        this.useGravity = true;

        this.hitboxWidth = Constants.PLAYER_SPRITE_WIDTH * Constants.SCALE - 10;
        this.hitboxHeight = Constants.PLAYER_SPRITE_HEIGHT * Constants.SCALE - 10;
        this.hitboxOffsetX = 5;
        this.hitboxOffsetY = 5;
        this.showHitbox = false;

        this.pistol = new Pistol(this, bullets);
        this.currentTimeLeft = maxTimeLimitSeconds;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/player.png"));

            this.movementSprites = new BufferedImage[8];
            for (int i = 0; i < 8; i++) {
                movementSprites[i] = spriteSheet.getSubimage(
                        i * Constants.PLAYER_SPRITE_WIDTH,
                        0,
                        Constants.PLAYER_SPRITE_WIDTH,
                        Constants.PLAYER_SPRITE_HEIGHT
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        super.update(jumpPressed);
        if (ownsPistol) this.pistol.update();

        this.isMoving = false;

        if (invincible) {
            long elapsedTime = System.currentTimeMillis() - invincibilityStartTime;
            if (elapsedTime >= INVINCIBILITY_DURATION) {
                invincible = false;
            }
        }

        float nextWorldX = worldX;
        if (leftPressed) {
            nextWorldX -= speed;
            this.isMoving = true;
        }
        if (rightPressed) {
            nextWorldX += speed;
            this.isMoving = true;
        }

        if (isMoving) this.pistol.calculateAngle();

        if (isStandingOnSpike((int) worldX, (int) worldY + 1)) {
            takeDamage(1);
        }

        if (!isColliding((int) nextWorldX, (int) worldY)) {
            worldX = nextWorldX;
            screenX = Math.round(worldX - playing.camera.getCameraX());
        }

        if (isCollidingWithGoal((int) nextWorldX, (int) worldY)) {
            this.playing.showWinScreen = true;
        }

        if (health <= 0 || currentTimeLeft <= 0) {
            playing.respawn();
            playing.showBuyMenu = true;
        }

        if (System.currentTimeMillis() - lastCheck >= 1000) {
            lastCheck = System.currentTimeMillis();
            currentTimeLeft -= 1;
            timeLeft = updateHudText("", currentTimeLeft);
        }
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX + this.spriteWidth * Constants.SCALE, this.screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX, this.screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        if (showHitbox) {
            Rectangle2D.Float rect = getScreenBounds();
            g2.setColor(Color.RED);
            g2.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
        }

        g2.drawImage(healthBar, 0, 0, null);
        g2.drawImage(coinCount, Constants.SCREEN_WIDTH - coinCount.getWidth(), 0, null);
        g2.drawImage(timeLeft, (Constants.SCREEN_WIDTH - timeLeft.getWidth()) / 2, 0, null);
        if (hasKeycard && keycardIcon != null) {
            g2.drawImage(keycardIcon, 0, Window.getWindow().getSize().height - Window.getWindow().getInsets().top - (keycardIcon.getHeight() * Constants.SCALE), keycardIcon.getWidth() * Constants.SCALE, keycardIcon.getHeight() * Constants.SCALE, null);
        }
        if (ownsPistol) this.pistol.draw(g2);
    }

    private boolean isCollidingWithGoal(int worldX, int worldY) {
        LevelManager levelManager = Window.getWindow().playing.levelManager;

        int leftX = worldX + hitboxOffsetX;
        int rightX = worldX + hitboxOffsetX + hitboxWidth - 1;
        int topY = worldY + hitboxOffsetY;
        int bottomY = worldY + hitboxOffsetY + hitboxHeight - 1;

        return levelManager.isGoalTile(leftX, topY) || levelManager.isGoalTile(rightX, topY) ||
                levelManager.isGoalTile(leftX, bottomY) || levelManager.isGoalTile(rightX, bottomY);
    }

    public BufferedImage updateHudText(String text, int value) {
        String textStr = text + value;
        Font font = new Font("Arial", Font.BOLD, Constants.PLAYER_HEALTH_FONT_SIZE);

        BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tempImg.createGraphics();
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(textStr);
        int textHeight = fm.getHeight();
        g2.dispose();

        int padding = 5;
        int imageWidth = textWidth + padding * 2;
        int imageHeight = textHeight + padding * 2;
        BufferedImage healthImg = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        g2 = healthImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        g2.setFont(font);
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, imageWidth, imageHeight);
        g2.setColor(Color.WHITE);

        g2.drawString(textStr, padding, padding + fm.getAscent());
        g2.dispose();

        return healthImg;
    }

    public void takeDamage(int amount) {
        if (!invincible) {
            this.health -= amount;
            this.healthBar = updateHudText("Health: ", health);
            invincible = true;
            invincibilityStartTime = System.currentTimeMillis();
        }
    }

    public int getHealthUpgradeCost() {
        return 50*this.healthUpgradeLvl;
    }

    public int getSpeedUpgradeCost() {
        return (int) Math.pow(2, this.speedUpgradeLvl);
    }

    public int getJumpUpgradeCost() {
        return (int) Math.pow(3, this.jumpUpgradeLvl);
    }

    public int getTimeUpgradeCost() {
        return 10*this.timeUpgradeLvl;
    }

    public int getCoinMultiplierUpgradeCost() {
        return 20*this.coinMultiplier;
    }

    public int getAmmoUpgradeCost() {
        return 30*this.pistol.getMaxBullets();
    }

    public void upgradeHealth() {
        if (spendCoins(getHealthUpgradeCost())) {
            maxHealth += 1;
            health = maxHealth;
            this.healthBar = updateHudText("Health: ", health);
            this.healthUpgradeLvl++;
        }

    }

    /**
     * speed: 1, 2, 5, 8, 10, 20, 60, 400, 800, 1000
     * jump: 3, 10, 15, 20, 25, 50, 75, 100, 150, 200
     * double jump: 600exp
     * time: 10, 15(8sec), 20(14sec), 25(20sec), 30(26sec), 35(32sec), 40, 45(44sec), 50(50sec), 60(56sec), 70(62sec), 80(68sec), 90(74sec), 100(80sec), 120(86sec), 140(92sec), 160(98sec), 180(104sec)
     * health: 50, 150, 200, 300
     * multi: 20, 30, 40, 60, 80, 100, 150, 200, 250, 300
     * ammo: 20, 40, 70, 100, 150, 200, 250, 300, 400
     */

    public void upgradeSpeed() {
        if (spendCoins(getSpeedUpgradeCost())) {
            speed += 0.5f;
            this.speedUpgradeLvl++;
        }
    }

    public void upgradeJumpPower() {
        if (spendCoins(getJumpUpgradeCost())) {
            jumpPower += 2.0f;
            this.jumpUpgradeLvl++;
        }
    }

    public void buyPistol() {
        if (!ownsPistol && this.spendCoins(5)) {
            this.ownsPistol = true;
        }
    }

    public void buyAmmo() {
        if (ownsPistol && this.spendCoins(getAmmoUpgradeCost())) {
            this.pistol.setMaxBullets(pistol.getMaxBullets()+1);
            this.pistol.setBulletsRemaining(pistol.getBulletsRemaining()+1);
        }
    }

    public void buyTimeUpgrade() {
        if (spendCoins(getTimeUpgradeCost())) {
            this.maxTimeLimitSeconds += 10;
            this.currentTimeLeft = this.maxTimeLimitSeconds;
            this.timeUpgradeLvl++;
        }
    }

    public boolean spendCoins(int amount) {
        if (coins >= amount) {
            coins -= amount;
            this.coinCount = updateHudText("Coins: ", coins);
            return true;
        }
        return false;
    }

    public void earnCoins(int amount) {
        coins += amount;
        this.coinCount = updateHudText("Coins: ", coins);
    }

    public void buyCoinMultiplier() {
        if (spendCoins(getCoinMultiplierUpgradeCost())) this.coinMultiplier++;
    }

    public int getCoinMultiplier() {
        return this.coinMultiplier;
    }

    public int getCoins() { return coins; }

    public void respawn(float x, float y) {
        this.worldX = x;
        this.worldY = y;
        this.health = maxHealth;
        this.healthBar = updateHudText("Health: ", health);
        this.pistol.setBulletsRemaining(pistol.getMaxBullets());
        this.currentTimeLeft = maxTimeLimitSeconds;
    }

    public void setKeycardIcon(BufferedImage img) {
        this.keycardIcon = img;
    }

    public void setCurrentTimeLeft(int ammount) {
        this.currentTimeLeft = ammount;
    }
}
