package entities;

import inputs.KeyboardInput;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    public final int screenX = (Constants.SCREEN_WIDTH/2)-(Constants.PLAYER_SPRITE_TILE_SIZE* Constants.SCALE/2);
    public final int screenY = (Constants.SCREEN_HEIGHT/2)-(Constants.PLAYER_SPRITE_TILE_SIZE* Constants.SCALE/2);
    public boolean leftPressed, rightPressed, jumpPressed;

    public Player(int worldX, int worldY) {
        super(worldX, worldY, null, Constants.PLAYER_SPRITE_TILE_SIZE, Constants.PLAYER_SPRITE_TILE_SIZE);
        this.useGravity = true;

        this.hitboxWidth = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 60;
        this.hitboxHeight = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 55;
        this.hitboxOffsetX = 30;
        this.hitboxOffsetY = 35;
        this.showHitbox = true;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/knight.png"));

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
        super.update(jumpPressed);

        int nextWorldX = worldX;
        if (leftPressed) {
            facingLeft = true;
            nextWorldX -= Constants.PLAYER_SPEED;
        }
        if (rightPressed) {
            facingLeft = false;
            nextWorldX += Constants.PLAYER_SPEED;
        }

        if (!isColliding(nextWorldX, worldY)) {
            worldX = nextWorldX;
        }

        if (leftPressed || rightPressed) {
            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum >= 7) {
                    spriteNum = 0;
                } else {
                    spriteNum++;
                }
                spriteCounter = 0;
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(screenX + hitboxOffsetX, screenY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX + this.spriteWidth * Constants.SCALE, this.screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX, this.screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        if (showHitbox) {
            Rectangle rect = getBounds();
            g2.setColor(Color.RED);
            g2.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
    }
}
