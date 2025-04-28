package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Collectable {
    private final Playing playing;
    public float worldX, worldY;
    private BufferedImage[] spriteFrames;
    private ItemType itemType;
    public int spriteCounter = 0;
    public int spriteNum = 0;

    public Collectable(float x, float y, ItemType type, Playing playing) {
        this.playing = playing;
        this.worldX = x;
        this.worldY = y;
        this.itemType = type;

        try {
            String spritePath;
            switch(itemType) {
                case COIN -> spritePath = "/sprites/coin.png";
                case KEYCARD -> spritePath = "/sprites/keycard.png";
                default -> throw new IllegalStateException("Unexpected value: " + itemType);
            }
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(spritePath)));

            this.spriteFrames = new BufferedImage[spriteSheet.getWidth()/Constants.TILE_WIDTH];
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
            switch(itemType) {
                case COIN -> playing.player.earnCoins(1);
                case KEYCARD -> {
                    playing.player.setKeycardIcon(this.spriteFrames[0]);
                    playing.player.hasKeycard = true;
                }
                default -> throw new IllegalStateException("Unexpected value: " + itemType);
            }
            return true;
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

    public ItemType getItemType() {
        return this.itemType;
    }
}
