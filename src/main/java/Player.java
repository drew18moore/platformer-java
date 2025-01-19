import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    public final int screenX = (Constants.SCREEN_WIDTH/2)-(Constants.PLAYER_SPRITE_TILE_SIZE*Constants.SCALE/2);
    public final int screenY = (Constants.SCREEN_HEIGHT/2)-(Constants.PLAYER_SPRITE_TILE_SIZE*Constants.SCALE/2);
    private final Input keyListener;

    private final int hitboxWidth, hitboxHeight, hitboxOffsetX, hitboxOffsetY;

    public Player(int worldX, int worldY, final Input input) {
        super(worldX, worldY, null, Constants.PLAYER_SPRITE_TILE_SIZE, Constants.PLAYER_SPRITE_TILE_SIZE);
        this.useGravity = true;
        this.keyListener = input;

        this.hitboxWidth = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 60;
        this.hitboxHeight = Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE - 55;
        this.hitboxOffsetX = 30;
        this.hitboxOffsetY = 35;

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

    public void update(double dt) {
        super.update(dt);

        int nextWorldX = worldX;
        if (keyListener.leftPressed) {
            facingLeft = true;
            nextWorldX -= Constants.PLAYER_SPEED;
        }
        if (keyListener.rightPressed) {
            facingLeft = false;
            nextWorldX += Constants.PLAYER_SPEED;
        }

        if (!isColliding(nextWorldX, worldY)) {
            worldX = nextWorldX;
        }

        // Gravity effect
        if (this.useGravity) {
            velocityY += Constants.GRAVITATIONAL_CONSTANT;
            if (velocityY > Constants.TERMINAL_VELOCITY) {
                velocityY = Constants.TERMINAL_VELOCITY;
            }
        }

        int nextWorldY = worldY + (int) velocityY;
        if (!isColliding(worldX, nextWorldY)) {
            worldY = nextWorldY;
        } else {
            velocityY = 0;
        }


        if (keyListener.leftPressed || keyListener.rightPressed) {
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

    private boolean isColliding(int worldX, int worldY) {
        TileManager tileManager = Window.getWindow().tileManager;

        int leftX = worldX + hitboxOffsetX;
        int rightX = worldX + hitboxOffsetX + hitboxWidth - 1;
        int topY = worldY + hitboxOffsetY;
        int bottomY = worldY + hitboxOffsetY + hitboxHeight - 1;

        return tileManager.isSolidTile(leftX, topY) || tileManager.isSolidTile(rightX, topY) ||
                tileManager.isSolidTile(leftX, bottomY) || tileManager.isSolidTile(rightX, bottomY);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(screenX + hitboxOffsetX, screenY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public void jump() {
        this.velocityY -= 10;
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX + this.spriteWidth * Constants.SCALE, this.screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX, this.screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        Rectangle rect = getBounds();
        g2.setColor(Color.RED);
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
    }
}
