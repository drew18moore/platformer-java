package com.drewm.entities;

import com.drewm.gamestates.Playing;
import com.drewm.levels.LevelManager;
import com.drewm.main.Window;
import com.drewm.objects.MovingPlatform;
import com.drewm.objects.FallingBlock;
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

    private MovingPlatform ridingPlatform = null;

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

        checkFallingBlockUndersideCollision();

        if (ridingPlatform != null && isOnGround) {
            worldX += ridingPlatform.getDeltaX();
            worldY += ridingPlatform.getDeltaY();
            worldY = ridingPlatform.getLandingY() - (hitboxOffsetY + hitboxHeight);
        }

        screenY = Math.round(worldY - playing.camera.getCameraY());

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
        int feetY = (int) (worldY + hitboxOffsetY + hitboxHeight - 1);
        int leftX = (int) (worldX + hitboxOffsetX + 1);
        int rightX = (int) (worldX + hitboxOffsetX + hitboxWidth - 1);

        boolean onSolidTile = playing.levelManager.isSolidTile(leftX, feetY + 2) ||
                playing.levelManager.isSolidTile(rightX, feetY + 2);

        MovingPlatform platformBelow = findPlatformBelow();
        FallingBlock fallingBlockBelow = findFallingBlockBelow();

        if (platformBelow != null) {
            ridingPlatform = platformBelow;
            isOnGround = true;
        } else if (fallingBlockBelow != null) {
            ridingPlatform = null;
            isOnGround = true;
        } else if (onSolidTile) {
            ridingPlatform = null;
            isOnGround = true;
        } else {
            ridingPlatform = null;
            isOnGround = false;
        }
    }

    private MovingPlatform findPlatformBelow() {
        Rectangle2D.Float entityBounds = getBounds();
        float entityBottom = entityBounds.y + entityBounds.height;

        if (ridingPlatform != null) {
            float platformTop = ridingPlatform.getWorldY();
            if (Math.abs(entityBottom - platformTop) <= 3 &&
                    entityBounds.x < ridingPlatform.getWorldX() + ridingPlatform.getBounds().width &&
                    entityBounds.x + entityBounds.width > ridingPlatform.getWorldX()) {
                return ridingPlatform;
            }
        }

        for (MovingPlatform platform : playing.levelManager.getCurrentRoom().getMovingPlatforms()) {
            float platformTop = platform.getWorldY();

            if (Math.abs(entityBottom - platformTop) <= 3 &&
                    entityBounds.x < platform.getWorldX() + platform.getBounds().width &&
                    entityBounds.x + entityBounds.width > platform.getWorldX()) {
                return platform;
            }
        }

        return null;
    }

    private FallingBlock findFallingBlockBelow() {
        Rectangle2D.Float entityBounds = getBounds();
        float entityBottom = entityBounds.y + entityBounds.height;

        for (FallingBlock fallingBlock : playing.levelManager.getCurrentRoom().getFallingBlocks()) {
            Rectangle2D.Float blockBounds = fallingBlock.getBounds();
            float blockTop = blockBounds.y;

            if (Math.abs(entityBottom - blockTop) <= 3 &&
                    entityBounds.x < blockBounds.x + blockBounds.width &&
                    entityBounds.x + entityBounds.width > blockBounds.x) {
                return fallingBlock;
            }
        }

        return null;
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
        ridingPlatform = null;
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

    private FallingBlock checkFallingBlockCollision(float nextWorldY) {
        Rectangle2D.Float nextBounds = new Rectangle2D.Float(
                worldX + hitboxOffsetX,
                nextWorldY + hitboxOffsetY,
                hitboxWidth,
                hitboxHeight
        );

        if (velocityY > 0) {
            for (FallingBlock fallingBlock : playing.levelManager.getCurrentRoom().getFallingBlocks()) {
                Rectangle2D.Float blockBounds = fallingBlock.getBounds();

                if (nextBounds.intersects(blockBounds) ||
                        (nextBounds.y + nextBounds.height >= blockBounds.y &&
                                nextBounds.y + nextBounds.height <= blockBounds.y + 5 &&
                                nextBounds.x < blockBounds.x + blockBounds.width &&
                                nextBounds.x + nextBounds.width > blockBounds.x)) {
                    return fallingBlock;
                }
            }
        }

        return null;
    }

    private void moveVertically() {
        if (ridingPlatform != null && isOnGround) {
            return;
        }

        float nextWorldY = worldY + velocityY;

        if (!isColliding((int) worldX, Math.round(nextWorldY))) {
            MovingPlatform platform = checkOneWayPlatformCollision(nextWorldY);
            FallingBlock fallingBlock = checkFallingBlockCollision(nextWorldY);

            if (platform != null) {
                worldY = platform.getLandingY() - (hitboxOffsetY + hitboxHeight);
                velocityY = 0;
                isOnGround = true;
                ridingPlatform = platform;
            } else if (fallingBlock != null) {
                worldY = fallingBlock.getBounds().y - (hitboxOffsetY + hitboxHeight);
                velocityY = 0;
                isOnGround = true;
                ridingPlatform = null;
            } else {
                worldY = nextWorldY;
            }
        } else {
            if (velocityY > 0) isOnGround = true;
            velocityY = 0;
        }
    }

    public boolean isCollidingWithFallingBlocks(float testX, float testY) {
        Rectangle2D.Float testBounds = new Rectangle2D.Float(
                testX + hitboxOffsetX,
                testY + hitboxOffsetY,
                hitboxWidth,
                hitboxHeight
        );

        for (FallingBlock fallingBlock : playing.levelManager.getCurrentRoom().getFallingBlocks()) {
            if (testBounds.intersects(fallingBlock.getBounds())) {
                return true;
            }
        }

        return false;
    }

    private void updateSpriteAnimation() {
        if (!isMoving) return;

        spriteCounter++;
        if (spriteCounter > 12) {
            spriteNum = (spriteNum + 1) % movementSprites.length;
            spriteCounter = 0;
        }
    }

    private void checkFallingBlockUndersideCollision() {
        Rectangle2D.Float entityBounds = getBounds();

        for (FallingBlock fallingBlock : playing.levelManager.getCurrentRoom().getFallingBlocks()) {
            if (fallingBlock.isFalling()) {
                Rectangle2D.Float blockBounds = fallingBlock.getBounds();

                if (entityBounds.intersects(blockBounds)) {
                    float entityTop = entityBounds.y;
                    float blockBottom = blockBounds.y + blockBounds.height;

                    if (entityTop < blockBottom && entityTop + entityBounds.height > blockBounds.y) {
                        if (entityTop > blockBounds.y) {
                            takeDamage(this.health);
                        }
                    }
                }
            }
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

        boolean tileCollision = levelManager.isSolidTile(leftX, topY) || levelManager.isSolidTile(rightX, topY) ||
                levelManager.isSolidTile(leftX, bottomY) || levelManager.isSolidTile(rightX, bottomY);

        boolean fallingBlockCollision = isCollidingWithFallingBlocks(worldX, worldY);

        return tileCollision || fallingBlockCollision;
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