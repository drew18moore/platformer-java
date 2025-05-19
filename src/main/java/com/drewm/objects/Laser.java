package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Laser {
    private final Playing playing;
    private float x1, x2, y1, y2;
    private boolean active;
    private long activationInterval;
    private long lastToggleTime;
    private long delayMS;
    private boolean delayPassed;
    private BufferedImage sprite;

    public Laser(float x1, float y1, float x2, float y2, long activationIntervalMS, long delayMS, Playing playing) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.activationInterval = activationIntervalMS;
        this.lastToggleTime = System.currentTimeMillis();
        this.delayMS = delayMS;
        this.delayPassed = false;
        this.active = true;
        this.playing = playing;

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/laser.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        long currentTimeMS = System.currentTimeMillis();

        if (!delayPassed) {
            if (currentTimeMS - lastToggleTime >= delayMS) {
                delayPassed = true;
                lastToggleTime = currentTimeMS;
                active = true;
            }
            return;
        }

        if (currentTimeMS - lastToggleTime >= activationInterval) {
            active = !active;
            lastToggleTime = currentTimeMS;
        }
    }

    public void draw(Graphics2D g2) {
        if (!active) return;

        float x = Math.min(x1, x2);
        float y = Math.min(y1, y2);
        float width = Math.abs(x2 - x1);
        float height = Math.abs(y2 - y1);

        // Ensure minimum 16px thickness if line is flat
        if (width == 0) width = Constants.TILE_SIZE;
        if (height == 0) height = Constants.TILE_SIZE;

        float screenX = x - playing.camera.getCameraX();
        float screenY = y - playing.camera.getCameraY();
        if (screenX + width > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + height > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {
            AffineTransform oldTransform = g2.getTransform();

            if (width > height) {
                g2.translate(screenX, screenY);
                g2.rotate(Math.toRadians(90));
                g2.drawImage(sprite, 0, - (int) width, (int) height, (int) width, null);
            } else {
                g2.drawImage(sprite,
                        (int) screenX, (int) screenY,
                        (int) (screenX + width), (int) (screenY + height),
                        0, 0, sprite.getWidth(), sprite.getHeight(),
                        null);
            }

            // Reset transform
            g2.setTransform(oldTransform);
        }
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = Math.min(x1, x2) - playing.player.worldX + playing.player.screenX;
        float screenY = Math.min(y1, y2) - playing.player.worldY + playing.player.screenY;
        float width = Math.abs(x2 - x1);
        float height = Math.abs(y2 - y1);
        if (width == 0) width = Constants.TILE_SIZE;
        if (height == 0) height = Constants.TILE_SIZE;
        return new Rectangle2D.Float(screenX, screenY, width, height);
    }
}
