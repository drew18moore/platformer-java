package entities;

import levels.TileManager;
import main.Window;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    public final int screenX = (Constants.SCREEN_WIDTH/2)-(Constants.PLAYER_SPRITE_TILE_SIZE* Constants.SCALE/2);
    public final int screenY = (Constants.SCREEN_HEIGHT/2)-(Constants.PLAYER_SPRITE_TILE_SIZE* Constants.SCALE/2);
    public boolean leftPressed, rightPressed, jumpPressed;
    public float SPEED = Constants.PLAYER_SPEED;

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

        float nextWorldX = worldX;
        if (leftPressed) {
            facingLeft = true;
            nextWorldX -= SPEED;
        }
        if (rightPressed) {
            facingLeft = false;
            nextWorldX += SPEED;
        }

        if (!isColliding((int) nextWorldX, (int) worldY)) {
            worldX = nextWorldX;
        }

        if (isCollidingWithGoal((int) nextWorldX, (int) worldY)) {
            System.out.println("GOAL");
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
    public Rectangle2D.Float getBounds() {
        return new Rectangle2D.Float(screenX + hitboxOffsetX, screenY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX + this.spriteWidth * Constants.SCALE, this.screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX, this.screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        if (showHitbox) {
            Rectangle2D.Float rect = getBounds();
            g2.setColor(Color.RED);
            g2.drawRect((int) rect.x, (int) rect.y, (int) rect.width, (int) rect.height);
        }
    }

    private boolean isCollidingWithGoal(int worldX, int worldY) {
        TileManager tileManager = Window.getWindow().playing.tileManager;

        int leftX = worldX + hitboxOffsetX;
        int rightX = worldX + hitboxOffsetX + hitboxWidth - 1;
        int topY = worldY + hitboxOffsetY;
        int bottomY = worldY + hitboxOffsetY + hitboxHeight - 1;

        return tileManager.isGoalTile(leftX, topY) || tileManager.isGoalTile(rightX, topY) ||
                tileManager.isGoalTile(leftX, bottomY) || tileManager.isGoalTile(rightX, bottomY);
    }
}
