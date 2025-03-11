package entities;

import gamestates.Playing;
import levels.TileManager;
import main.Window;
import utils.Constants;
import weapons.Bullet;
import weapons.Pistol;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Player extends Entity {
    private final Playing playing;
    public final int screenX = (Constants.SCREEN_WIDTH/2)-(Constants.PLAYER_SPRITE_TILE_SIZE* Constants.SCALE/2);
    public final int screenY = (Constants.SCREEN_HEIGHT/2)-(Constants.PLAYER_SPRITE_TILE_SIZE* Constants.SCALE/2);
    public boolean leftPressed, rightPressed, jumpPressed;
    public float SPEED = Constants.PLAYER_SPEED;
    private int health = Constants.PLAYER_STARTING_MAX_HEALTH;
    private BufferedImage healthBar = updateHealthBar();

    public boolean invincible = false;
    private long invincibilityStartTime = 0;
    private final int INVINCIBILITY_DURATION = 500;

    public Pistol pistol;

    public Player(int worldX, int worldY, Playing playing, List<Bullet> bullets) {
        super(worldX, worldY, null, Constants.PLAYER_SPRITE_TILE_SIZE, Constants.PLAYER_SPRITE_TILE_SIZE);
        this.playing = playing;
        this.useGravity = true;

        this.hitboxWidth = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 60;
        this.hitboxHeight = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 55;
        this.hitboxOffsetX = 30;
        this.hitboxOffsetY = 35;
        this.showHitbox = true;

        this.pistol = new Pistol(this, bullets);

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/knight.png"));

            this.movementSprites = new BufferedImage[8];
            for (int i = 0; i < 8; i++) {
                movementSprites[i] = spriteSheet.getSubimage(
                        i * Constants.PLAYER_SPRITE_TILE_SIZE,
                        2 * Constants.PLAYER_SPRITE_TILE_SIZE,
                        Constants.PLAYER_SPRITE_TILE_SIZE,
                        Constants.PLAYER_SPRITE_TILE_SIZE
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        super.update(jumpPressed);
        this.pistol.update();

        this.isMoving = false;

        if (invincible) {
            long elapsedTime = System.currentTimeMillis() - invincibilityStartTime;
            if (elapsedTime >= INVINCIBILITY_DURATION) {
                invincible = false; // End invincibility after duration
            }
        }

        float nextWorldX = worldX;
        if (leftPressed) {
            nextWorldX -= SPEED;
            this.isMoving = true;
        }
        if (rightPressed) {
            nextWorldX += SPEED;
            this.isMoving = true;
        }

        if (!isColliding((int) nextWorldX, (int) worldY)) {
            worldX = nextWorldX;
        }

        if (isCollidingWithGoal((int) nextWorldX, (int) worldY)) {
            this.playing.showWinScreen = true;
        }

        if (health <= 0) {
            playing.showDeathScreen = true;
        }
    }

    @Override
    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(screenX + hitboxOffsetX, screenY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX + this.spriteWidth * Constants.SCALE, this.screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX, this.screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        if (showHitbox) {
            Rectangle2D.Float rect = getBounds();
            g2.setColor(Color.RED);
            g2.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
        }

        g2.drawImage(healthBar, 0, 0, null);
        this.pistol.draw(g2);
    }

    private boolean isCollidingWithGoal(int worldX, int worldY) {
        TileManager tileManager = Window.getWindow().playing.tileManager;

        int leftX = worldX + hitboxOffsetX;
        int rightX = worldX + hitboxOffsetX + hitboxWidth - 1;
        int topY = worldY + hitboxOffsetY;
        int bottomY = worldY + hitboxOffsetY + hitboxHeight - 1;

        return tileManager.isGoalTile(leftX, topY) || tileManager.isGoalTile(rightX, topY) ||
                tileManager.isGoalTile(leftX, bottomY) || tileManager.isGoalTile(rightX, bottomY);
    }

    private BufferedImage updateHealthBar() {
        String healthStr = "Health: " + health;
        Font font = new Font("Arial", Font.BOLD, Constants.PLAYER_HEALTH_FONT_SIZE);

        BufferedImage tempImg = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = tempImg.createGraphics();
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int textWidth = fm.stringWidth(healthStr);
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

        g2.drawString(healthStr, padding, padding + fm.getAscent());
        g2.dispose();

        return healthImg;
    }

    public void takeDamage(int amount) {
        if (!invincible) {
            this.health -= amount;
            this.healthBar = updateHealthBar();
            invincible = true;
            invincibilityStartTime = System.currentTimeMillis();
        }
    }
}
