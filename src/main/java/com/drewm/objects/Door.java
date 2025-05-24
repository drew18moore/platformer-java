package com.drewm.objects;

import com.drewm.data.LockType;
import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Door {
    private final Playing playing;
    private final int worldX, worldY;
    private final int destinationRoomIdx;
    private final float destinationX;
    private final float destinationY;
    private final LockType lockType;
    private final boolean isGoal;
    private final boolean drawHitbox = false;

    public Door(int x, int y, int destinationRoomIdx, float destinationX, float destinationY, LockType lockType, Playing playing) {
        this.worldX = x;
        this.worldY = y;
        this.destinationRoomIdx = destinationRoomIdx;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.lockType = lockType;
        this.playing = playing;
        this.isGoal = false;
    }

    public Door(int x, int y, boolean isGoal, LockType lockType, Playing playing) {
        this.worldX = x;
        this.worldY = y;
        this.destinationRoomIdx = -1;
        this.destinationX = -1;
        this.destinationY = -1;
        this.lockType = lockType;
        this.playing = playing;
        this.isGoal = isGoal;
    }

    public void draw(Graphics2D g2) {
        Rectangle2D.Float rect = getScreenBounds();
        if (drawHitbox) g2.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.player.worldX + playing.player.screenX;
        float screenY = this.worldY - playing.player.worldY + playing.player.screenY;
        return new Rectangle2D.Float(screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE * 2);
    }

    public int getDestinationRoomIdx() {
        return this.destinationRoomIdx;
    }

    public float getDestinationX() {
        return this.destinationX;
    }

    public float getDestinationY() {
        return this.destinationY;
    }

    public LockType getLockType() {
        return this.lockType;
    }

    public boolean getIsGoal() { return this.isGoal; }
}
