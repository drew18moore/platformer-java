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
    private BufferedImage[] spriteFrames;
    public int spriteCounter = 0;
    public int spriteNum = 0;

    public Coin(float x, float y, Playing playing) {
        this.playing = playing;
        this.worldX = x;
        this.worldY = y;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/coin.png"));

            this.spriteFrames = new BufferedImage[4];
            for (int i = 0; i < spriteFrames.length; i++) {
                spriteFrames[i] = spriteSheet.getSubimage(
                        i * Constants.TILE_WIDTH,
                        0,
                        Constants.TILE_WIDTH,
                        Constants.TILE_WIDTH
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean update() {
        if (getScreenBounds().intersects(playing.player.getBounds())) {
            playing.player.earnCoins(1);
            return true;
        }

        System.out.println(spriteCounter);
        spriteCounter++;
        if (spriteCounter > 24) {
            if (spriteNum >= 3) {
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
        if (screenX + Constants.TILE_SIZE > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + Constants.TILE_SIZE > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {
            g2.drawImage(spriteFrames[spriteNum], (int) screenX, (int) screenY, spriteFrames[0].getWidth() * Constants.SCALE, spriteFrames[0].getHeight() * Constants.SCALE, null);
        }
    }

    public Rectangle2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.player.worldX + playing.player.screenX;
        float screenY = this.worldY - playing.player.worldY + playing.player.screenY;
        return new Rectangle2D.Float(screenX, screenY, spriteFrames[0].getWidth() * Constants.SCALE, spriteFrames[0].getHeight() * Constants.SCALE);
    }
}
