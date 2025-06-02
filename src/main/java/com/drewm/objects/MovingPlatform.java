package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MovingPlatform {
    private final Playing playing;
    private float worldX, worldY;
    private final float width, height;
    private float speed;
    private float min, max;
    private boolean horizontal;
    private boolean forward = true;

    public MovingPlatform(float worldX, float worldY, float width, float height, float speed, float min, float max, boolean horizontal, Playing playing) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.min = min;
        this.max = max;
        this.horizontal = horizontal;
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

    public Rectangle2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();
        return new Rectangle2D.Float(screenX, screenY, width * Constants.SCALE, height * Constants.SCALE);
    }

    public float getWorldX() {
        return worldX;
    }

    public float getWorldY() {
        return worldY;
    }

    public float getSpeedX() {
        return horizontal ? (forward ? speed : -speed) : 0;
    }

    public float getSpeedY() {
        return horizontal ? 0 : (forward ? speed : -speed);
    }
}
