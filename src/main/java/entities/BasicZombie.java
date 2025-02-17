package entities;

import gamestates.Playing;
import main.Window;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BasicZombie extends Entity {
    private Player player;
    public BasicZombie(int worldX, int worldY, Player player) {
        super(worldX, worldY, null, Constants.PLAYER_SPRITE_TILE_SIZE, Constants.PLAYER_SPRITE_TILE_SIZE);

        this.player = player;
        this.useGravity = true;

        this.hitboxWidth = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 60;
        this.hitboxHeight = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 55;
        this.hitboxOffsetX = 30;
        this.hitboxOffsetY = 35;
        this.showHitbox = true;

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
        } else {
            this.facingLeft = !this.facingLeft;
        }
    }

    public void draw(Graphics2D g2) {
        float screenX = worldX - player.worldX + player.screenX;
        float screenY = worldY - player.worldY + player.screenY;
        if (worldX + Constants.TILE_SIZE * 2 > player.worldX - player.screenX &&
                worldX - Constants.TILE_SIZE * 2 < player.worldX + player.screenX &&
                worldY + Constants.TILE_SIZE * 2 > player.worldY - player.screenY &&
                worldY - Constants.TILE_SIZE * 2 < player.worldY + Window.getWindow().playing.player.screenY) {
            if (facingLeft) {
                g2.drawImage(this.movementSprites[spriteNum], (int) screenX + this.spriteWidth * Constants.SCALE, (int) screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
            } else {
                g2.drawImage(this.movementSprites[spriteNum], (int) screenX, (int) screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
            }
        }

//        // TODO: Reimplement to render in screen space instead of world space
//        if (showHitbox) {
//            Rectangle2D.Float hitbox = getBounds();
//            g2.setColor(Color.RED);
//            g2.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
//        }
    }
}
