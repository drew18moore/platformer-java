package com.drewm.objects;

import com.drewm.entities.Player;
import com.drewm.main.Window;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Coin {
    private final Player player;
    public float worldX, worldY;
    private BufferedImage sprite;

    public Coin(float x, float y, Player player) {
        this.player = player;
        this.worldX = x;
        this.worldY = y;

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/coin.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean update() {
        if (getScreenBounds().intersects(player.getBounds())) {
            player.earnCoins(1);
            return true;
        }
        return false;
    }

    public void draw(Graphics2D g2) {
        float screenX = this.worldX - player.worldX + player.screenX;
        float screenY = this.worldY - player.worldY + player.screenY;
        if (worldX + Constants.TILE_SIZE * 2 > player.worldX - player.screenX &&
                worldX - Constants.TILE_SIZE * 2 < player.worldX + player.screenX &&
                worldY + Constants.TILE_SIZE * 2 > player.worldY - player.screenY &&
                worldY - Constants.TILE_SIZE * 2 < player.worldY + Window.getWindow().playing.player.screenY) {
            g2.drawImage(sprite, (int) screenX, (int) screenY, null);
        }
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = this.worldX - player.worldX + player.screenX;
        float screenY = this.worldY - player.worldY + player.screenY;
        return new Rectangle2D.Float(screenX, screenY, sprite.getWidth(), sprite.getHeight());
    }
}
