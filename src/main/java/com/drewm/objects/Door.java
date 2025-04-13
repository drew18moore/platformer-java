package com.drewm.objects;

import com.drewm.entities.Player;
import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Door {
    private final int worldX, worldY;
    private Playing playing;

    public Door(int x, int y, Playing playing) {
        this.worldX = x;
        this.worldY = y;
        this.playing = playing;
    }

    public boolean tryOpen(Player player) {
        System.out.println("DOOR");

        return false;
    }


    public void draw(Graphics2D g2) {
        Rectangle2D.Float rect = getScreenBounds();
        g2.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.player.worldX + playing.player.screenX;
        float screenY = this.worldY - playing.player.worldY + playing.player.screenY;
        return new Rectangle2D.Float(screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE * 2);
    }
}
