package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class FallingBlock {
    private final Playing playing;
    private float worldX, worldY;
    private final float width, height;

    private final float midpoint;
    private final float triggerRadius = 0.2f;

    private boolean isTriggered = false;
    private boolean isFalling = false;
    private float fallVelocity = 0;
    private final float gravity = Constants.GRAVITY_DESCEND;
    private final float maxFallSpeed = 1;

    public FallingBlock(float worldX, float worldY, float width, float height, Playing playing) {
        this.playing = playing;
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = width;
        this.height = height;
        this.midpoint = worldX + width / 2;
    }

    public void update() {
        if (!isTriggered &&
                playing.player.worldX > midpoint - (midpoint - worldX) * triggerRadius &&
                playing.player.worldX < midpoint + (midpoint - worldX) * triggerRadius &&
                playing.player.worldY > worldY + height &&
                playing.player.worldY < worldY + height + Constants.TILE_SIZE * 3) {

            isTriggered = true;
            isFalling = true;
        }

        if (isFalling) {
            fallVelocity += gravity;
            if (fallVelocity > maxFallSpeed) {
                fallVelocity = maxFallSpeed;
            }

            float nextY = worldY + fallVelocity;

            if (!isColliding(worldX, nextY)) {
                worldY = nextY;
            } else {
                isFalling = false;
                fallVelocity = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();

        if (screenX + width > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + height > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {

            g2.setColor(Color.DARK_GRAY);
            g2.fill(getScreenBounds());

            g2.setColor(Color.LIGHT_GRAY);
            g2.setStroke(new BasicStroke(4));
            Rectangle2D.Float bounds = getScreenBounds();
            g2.draw(new Rectangle2D.Float(
                    bounds.x + 4f / 2f,
                    bounds.y + 4f / 2f,
                    bounds.width - 4f,
                    bounds.height - 4f
            ));
        }
    }

    private boolean isColliding(float testX, float testY) {
        int leftX = (int) testX;
        int rightX = (int) (testX + width - 1);
        int topY = (int) testY;
        int bottomY = (int) (testY + height - 1);

        return playing.levelManager.isSolidTile(leftX, topY) ||
                playing.levelManager.isSolidTile(rightX, topY) ||
                playing.levelManager.isSolidTile(leftX, bottomY) ||
                playing.levelManager.isSolidTile(rightX, bottomY);
    }

    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(worldX, worldY, width, height);
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = worldX - playing.camera.getCameraX();
        float screenY = worldY - playing.camera.getCameraY();
        return new Rectangle2D.Float(screenX, screenY, width, height);
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isTriggered() {
        return isTriggered;
    }

    public boolean isFalling() {
        return isFalling;
    }
}