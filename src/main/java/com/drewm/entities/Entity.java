package com.drewm.entities;

import com.drewm.gamestates.Playing;
import com.drewm.levels.LevelManager;
import com.drewm.main.Window;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Entity {
    protected final Playing playing;
    public float worldX, worldY;
    public int screenX, screenY;
    public int spriteWidth, spriteHeight;
    public int hitboxWidth, hitboxHeight, hitboxOffsetX, hitboxOffsetY;

    public float velocityY = 0;

    public boolean isSolid = false;
    public boolean useGravity = false;
    public boolean showHitbox = false;
    public boolean isOnGround = false;
    public boolean isMoving = false;

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
        int feetY = (int) (worldY + hitboxOffsetY + hitboxHeight);
        int leftX = (int) (worldX + hitboxOffsetX + 1); // small padding
        int rightX = (int) (worldX + hitboxOffsetX + hitboxWidth - 1);

        this.isOnGround = playing.levelManager.isSolidTile(leftX, feetY + 1) ||
                playing.levelManager.isSolidTile(rightX, feetY + 1);

        if (useGravity) {
            if (velocityY < 0) {
                velocityY += Constants.GRAVITY_ASCEND;
            } else {
                velocityY += Constants.GRAVITY_DESCEND;
            }

            if (velocityY > Constants.TERMINAL_VELOCITY) {
                velocityY = Constants.TERMINAL_VELOCITY;
            }
        }

        if (jumpPressed && isOnGround) {
            System.out.println("JUMP!");
            velocityY = -Constants.JUMP_FORCE;
        }

        float nextWorldY = worldY + velocityY;
        if (!isColliding((int) worldX, (int) nextWorldY)) {
            worldY = nextWorldY;
            screenY = Math.round(worldY - playing.camera.getCameraY());
        } else {
            if (velocityY > 0) isOnGround = true;
            velocityY = 0;
        }

        if (isMoving) {
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum >= 7) {
                    spriteNum = 0;
                } else {
                    spriteNum++;
                }
                spriteCounter = 0;
            }
        }
    }


    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], (int)(this.worldX + this.spriteWidth * Constants.SCALE), (int) this.worldY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], (int) this.worldX, (int) this.worldY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        if (showHitbox) {
            Rectangle2D.Float hitbox = getBounds();
            g2.setColor(Color.RED);
            g2.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(worldX + hitboxOffsetX, worldY + hitboxOffsetY, hitboxWidth, hitboxHeight);
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
