package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MovingPlatform {
    private final Playing playing;
    private float worldX, worldY;
    private float startX, startY, endX, endY;
    private float directionX, directionY;
    private boolean movingToEnd = true;
    private final float width, height;
    private float speed;

    public MovingPlatform(float startX, float startY, float endX, float endY, float width, float height, float speed, Playing playing) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.worldX = startX;
        this.worldY = startY;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.playing = playing;
    }

    public void update() {

    }

    public void draw(Graphics2D g2) {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();
        if (screenX + Constants.TILE_SIZE * 2 > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + Constants.TILE_SIZE * 2 > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {
            g2.draw(getScreenBounds());
        }
    }

    public boolean canLandOn(Rectangle2D.Float entityBounds, float entityVelocityY, float entityPreviousY) {
        if (entityVelocityY <= 0) {
            return false;
        }

        if (entityBounds.x + entityBounds.width <= worldX || entityBounds.x >= worldX + width) {
            return false;
        }

        float entityBottom = entityBounds.y + entityBounds.height;
        float entityPreviousBottom = entityPreviousY + entityBounds.height;
        float platformTop = worldY;

        return entityPreviousBottom <= platformTop &&
                entityBottom >= platformTop &&
                entityBottom <= platformTop + height;
    }

    public float getLandingY() {
        return worldY;
    }

    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(worldX, worldY, width, height);
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();
        return new Rectangle2D.Float(screenX, screenY, width, height * Constants.SCALE);
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }
}
