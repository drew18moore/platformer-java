package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Coin {
    private final Playing playing;
    public float worldX, worldY;
    private BufferedImage sprite;

    public Coin(float x, float y, Playing playing) {
        this.playing = playing;
        this.worldX = x;
        this.worldY = y;

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/coin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean update() {
        if (getScreenBounds().intersects(playing.player.getBounds())) {
            playing.player.earnCoins(1);
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g2) {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();
        if (screenX + Constants.TILE_SIZE > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + Constants.TILE_SIZE > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {
            g2.drawImage(sprite, (int) screenX, (int) screenY, sprite.getWidth() * 2, sprite.getHeight() * 2, null);
        }
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.player.worldX + playing.player.screenX;
        float screenY = this.worldY - playing.player.worldY + playing.player.screenY;
        return new Rectangle2D.Float(screenX, screenY, sprite.getWidth() * 2, sprite.getHeight() * 2);
    }
}
