package com.drewm.entities;

import com.drewm.gamestates.Playing;
import com.drewm.levels.LevelManager;
import com.drewm.main.Window;
import com.drewm.objects.MovingPlatform;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Entity {
    protected final Playing playing;
    public float worldX, worldY;
    private float previousWorldY;
    public int screenX, screenY;
    public int spriteWidth, spriteHeight;
    public int hitboxWidth, hitboxHeight, hitboxOffsetX, hitboxOffsetY;

    public float velocityY = 0;

    public boolean useGravity = false;
    public boolean showHitbox = false;
    public boolean isOnGround = false;
    public boolean isMoving = false;

    public float jumpPower = Constants.STARTING_JUMP_FORCE;
    public int health = 100;

    BufferedImage[] movementSprites;
    public int spriteCounter = 0;
    public int spriteNum = 0;
    public boolean facingLeft = false;

    public Entity(float worldX, float worldY, BufferedImage[] movementSprites, int spriteWidth, int spriteHeight, Playing playing) {
        this.playing = playing;
        this.worldX = worldX;
        this.worldY = worldY;

        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        this.hitboxOffsetX = 0;
        this.hitboxOffsetY = 0;
        this.hitboxWidth = spriteWidth;
        this.hitboxHeight = spriteHeight;

        if (movementSprites != null) {
            this.movementSprites = movementSprites;
        } else {
            BufferedImage placeholder = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.BLUE);
            g2.fillRect(0, 0, 50, 50);
            g2.dispose();
            this.movementSprites = new BufferedImage[]{placeholder};
        }
    }

    public void update(boolean jumpPressed) {
        previousWorldY = worldY;

        applyGravity();

        if (jumpPressed && isOnGround) {
            jump();
        }

        moveVertically();
        updateGroundStatus();

        updateSpriteAnimation();
    }


    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], (int)(this.worldX + this.spriteWidth * Constants.SCALE), (int) this.worldY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], (int) this.worldX, (int) this.worldY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        if (showHitbox) {
            Rectangle2D.Float hitbox = getScreenBounds();
            g2.setColor(Color.RED);
            g2.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    private void updateGroundStatus() {
        // Check regular tiles
        int feetY = (int) (worldY + hitboxOffsetY + hitboxHeight - 1);
        int leftX = (int) (worldX + hitboxOffsetX + 1);
        int rightX = (int) (worldX + hitboxOffsetX + hitboxWidth - 1);

        boolean onSolidTile = playing.levelManager.isSolidTile(leftX, feetY + 2) ||
                playing.levelManager.isSolidTile(rightX, feetY + 2);

        // Check one-way platforms
        boolean onOneWayPlatform = false;
        Rectangle2D.Float entityBounds = getBounds();

        for (MovingPlatform platform : playing.levelManager.getCurrentRoom().getMovingPlatforms()) {
            float platformTop = platform.getWorldY();
            float entityBottom = entityBounds.y + entityBounds.height;

            if (Math.abs(entityBottom - platformTop) <= 2 &&
                    entityBounds.x < platform.getWorldX() + platform.getBounds().width &&
                    entityBounds.x + entityBounds.width > platform.getWorldX()) {
                onOneWayPlatform = true;
                break;
            }
        }

        this.isOnGround = onSolidTile || onOneWayPlatform;
    }

    private void applyGravity() {
        if (!useGravity || isOnGround) return;


        velocityY += (velocityY < 0) ? Constants.GRAVITY_ASCEND : Constants.GRAVITY_DESCEND;

        if (velocityY > Constants.TERMINAL_VELOCITY) {
            velocityY = Constants.TERMINAL_VELOCITY;
        }
    }

    private void jump() {
        velocityY = -jumpPower;
        isOnGround = false;
    }

    private MovingPlatform checkOneWayPlatformCollision(float nextWorldY) {
        Rectangle2D.Float nextBounds = new Rectangle2D.Float(
                worldX + hitboxOffsetX,
                nextWorldY + hitboxOffsetY,
                hitboxWidth,
                hitboxHeight
        );

        for (MovingPlatform platform : playing.levelManager.getCurrentRoom().getMovingPlatforms()) {
            if (platform.canLandOn(nextBounds, velocityY, previousWorldY + hitboxOffsetY)) {
                return platform;
            }
        }

        return null;
    }

    private void moveVertically() {
        float nextWorldY = worldY + velocityY;

        if (!isColliding((int) worldX, Math.round(nextWorldY))) {
            MovingPlatform platform = checkOneWayPlatformCollision(nextWorldY);

            if (platform != null) {
                worldY = platform.getLandingY() - (hitboxOffsetY + hitboxHeight);
                velocityY = 0;
                isOnGround = true;
            } else {
                worldY = nextWorldY;
            }
        } else {
            if (velocityY > 0) isOnGround = true;
            velocityY = 0;
        }
        screenY = Math.round(worldY - playing.camera.getCameraY());
    }

    private void updateSpriteAnimation() {
        if (!isMoving) return;

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum + 1) % movementSprites.length;
            spriteCounter = 0;
        }
    }

    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(worldX + hitboxOffsetX, worldY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public Rectangle2D.Float getScreenBounds() {
        return new Rectangle2D.Float(screenX + hitboxOffsetX, screenY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public boolean isColliding(int worldX, int worldY) {
        LevelManager levelManager = Window.getWindow().playing.levelManager;

        int leftX = worldX + hitboxOffsetX;
        int rightX = worldX + hitboxOffsetX + hitboxWidth - 1;
        int topY = worldY + hitboxOffsetY;
        int bottomY = worldY + hitboxOffsetY + hitboxHeight - 1;

        return levelManager.isSolidTile(leftX, topY) || levelManager.isSolidTile(rightX, topY) ||
                levelManager.isSolidTile(leftX, bottomY) || levelManager.isSolidTile(rightX, bottomY);
    }

    public boolean isStandingOnSpike(int worldX, int worldY) {
        LevelManager levelManager = Window.getWindow().playing.levelManager;

        int leftX = worldX + hitboxOffsetX;
        int rightX = worldX + hitboxOffsetX + hitboxWidth - 1;
        int bottomY = worldY + hitboxOffsetY + hitboxHeight - 1;

        return levelManager.isSpikeTile(leftX, bottomY) || levelManager.isSpikeTile(rightX, bottomY);
    }

    public void takeDamage(int amount) {
        this.health -= amount;
    }
}
