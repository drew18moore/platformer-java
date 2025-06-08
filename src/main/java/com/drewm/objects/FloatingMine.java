package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class FloatingMine {
    private final Playing playing;
    private float worldX, worldY;
    private float startX, startY, endX, endY;
    private float directionX, directionY;
    private boolean movingToEnd = true;
    private BufferedImage[] spriteFrames;
    private int spriteCounter = 0;
    private int spriteNum = 0;
    private float speed = 1f;
    private final boolean showHitbox = true;

    public FloatingMine(float startX, float startY, float endX, float endY, Playing playing) {
        this.playing = playing;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.worldX = startX;
        this.worldY = startY;

        try {
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/floating-mine.png")));

            this.spriteFrames = new BufferedImage[3];
            for (int i = 0; i < spriteFrames.length; i++) {
                spriteFrames[i] = spriteSheet.getSubimage(
                        i * Constants.TILE_WIDTH * 2,
                        0,
                        Constants.TILE_WIDTH * 2,
                        Constants.TILE_WIDTH * 2
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        calculateDirection();
    }

    public boolean update() {
        if (getScreenBounds().intersects(playing.player.getScreenBounds())) {
            return true;
        }
        worldX += directionX * speed;
        worldY += directionY * speed;

        float targetX = movingToEnd ? endX : startX;
        float targetY = movingToEnd ? endY : startY;

        if (distanceTo(targetX, targetY) < speed) {
            // Snap to target to avoid overshoot
            worldX = targetX;
            worldY = targetY;

            // Reverse direction
            movingToEnd = !movingToEnd;
            calculateDirection();
        }

        spriteCounter++;
        if (spriteCounter > 24) {
            if (spriteNum >= spriteFrames.length - 1) {
                spriteNum = 0;
            } else {
                spriteNum++;
            }
            spriteCounter = 0;
        }
        return false;
    }

    public void draw(Graphics2D g2) {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();
        if (screenX + Constants.TILE_SIZE * 2 > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + Constants.TILE_SIZE * 2 > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {
            g2.drawImage(spriteFrames[spriteNum], (int) screenX, (int) screenY, spriteFrames[0].getWidth() * Constants.SCALE, spriteFrames[0].getHeight() * Constants.SCALE, null);
            if (showHitbox) g2.draw(getScreenBounds());
        }
    }

    public Ellipse2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.player.worldX + playing.player.screenX;
        float screenY = this.worldY - playing.player.worldY + playing.player.screenY;
        return new Ellipse2D.Float(screenX + 3 * Constants.SCALE, screenY, (spriteFrames[0].getWidth() - 6) * Constants.SCALE, spriteFrames[0].getWidth() * Constants.SCALE);
    }

    private float distanceTo(float targetX, float targetY) {
        float dx = targetX - worldX;
        float dy = targetY - worldY;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    private void calculateDirection() {
        float dx = (movingToEnd ? endX - worldX : startX - worldX);
        float dy = (movingToEnd ? endY - worldY : startY - worldY);
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance != 0) {
            directionX = dx / distance;
            directionY = dy / distance;
        }
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }
}
