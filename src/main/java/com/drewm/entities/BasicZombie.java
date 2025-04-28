package com.drewm.entities;

import com.drewm.gamestates.Playing;
import com.drewm.objects.Collectable;
import com.drewm.objects.ItemType;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import static com.drewm.utils.RandomGenerator.RANDOM;

public class BasicZombie extends Entity {
    public BasicZombie(float worldX, float worldY, Playing playing) {
        super(worldX, worldY, null, Constants.PLAYER_SPRITE_TILE_SIZE, Constants.PLAYER_SPRITE_TILE_SIZE, playing);

        this.useGravity = true;

        this.hitboxWidth = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 80;
        this.hitboxHeight = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 60;
        this.hitboxOffsetX = 40;
        this.hitboxOffsetY = 45;
        this.showHitbox = false;
        this.health = 30;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/zombie-basic.png"));

            this.movementSprites = new BufferedImage[8];
            for (int i = 0; i < 8; i++) {
                movementSprites[i] = spriteSheet.getSubimage(
                        i * Constants.PLAYER_SPRITE_TILE_SIZE,
                        2 * Constants.PLAYER_SPRITE_TILE_SIZE,
                        Constants.PLAYER_SPRITE_TILE_SIZE,
                        Constants.PLAYER_SPRITE_TILE_SIZE
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        super.update(false);

        float nextWorldX = worldX;
        if (facingLeft) {
            nextWorldX -= Constants.BASIC_ZOMBIE_SPEED;
            this.isMoving = true;
        } else {
            nextWorldX += Constants.BASIC_ZOMBIE_SPEED;
            this.isMoving = true;
        }

        if (!isColliding((int) nextWorldX, (int) worldY)) {
            worldX = nextWorldX;
            screenX = Math.round(worldX - playing.camera.getCameraX());
        } else {
            this.facingLeft = !this.facingLeft;
        }

        if (getBounds().intersects(playing.player.getBounds())) {
            playing.player.takeDamage(5);
        }
    }

    public void draw(Graphics2D g2) {
        float screenX = worldX - playing.camera.getCameraX();
        float screenY = worldY - playing.camera.getCameraY();
        if (screenX + Constants.TILE_SIZE > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + Constants.TILE_SIZE > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {
            if (facingLeft) {
                g2.drawImage(this.movementSprites[spriteNum], (int) screenX + this.spriteWidth * Constants.SCALE, (int) screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
            } else {
                g2.drawImage(this.movementSprites[spriteNum], (int) screenX, (int) screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
            }
        }

        if (showHitbox) {
            Rectangle2D.Float hitbox = getBounds();
            g2.setColor(Color.RED);
            g2.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
        }
    }

    public Rectangle2D.Float getBounds() {
        float screenX = worldX - playing.player.worldX + playing.player.screenX;
        float screenY = worldY - playing.player.worldY + playing.player.screenY;
        return new Rectangle2D.Float(screenX + hitboxOffsetX, screenY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public Rectangle2D.Float getWorldBounds() {
        return new Rectangle2D.Float(worldX + hitboxOffsetX, worldY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public void handleDrop() {
        float dropChance = RANDOM.nextFloat();

        if (dropChance < 0.3f) {
            playing.levelManager.getCurrentRoom().getCollectables().add(
                    new Collectable(worldX + hitboxOffsetX, worldY + hitboxOffsetY, ItemType.COIN, playing)
            );
        }
    }
}
