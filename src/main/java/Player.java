import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    private final Input keyListener;
    public BufferedImage[] movementSprites;
    public int spriteCounter = 0;
    public int spriteNum = 0;
    public boolean facingLeft = false;

    public Player(int screenX, int screenY, final Input input) {
        super(screenX, screenY, null);
        this.useGravity = true;
        this.keyListener = input;

        try {
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/sprites/knight.png"));

            movementSprites = new BufferedImage[8];
            for (int i = 0; i < 8; i++) {
                movementSprites[i] = spriteSheet.getSubimage(i * Constants.PLAYER_SPRITE_TILE_SIZE, 2 * Constants.PLAYER_SPRITE_TILE_SIZE, Constants.PLAYER_SPRITE_TILE_SIZE, Constants.PLAYER_SPRITE_TILE_SIZE);
            }

            this.sprite = movementSprites[spriteNum];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(double dt) {
        super.update(dt);

        if (keyListener.leftPressed) {
            facingLeft = true;
            screenX -= Constants.PLAYER_SPEED;
        }
        if (keyListener.rightPressed) {
            facingLeft = false;
            screenX += Constants.PLAYER_SPEED;
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

    public void jump() {
        this.velocityY -= 10;
    }

    public void draw(Graphics2D g2) {
        this.sprite = movementSprites[spriteNum];

        if (facingLeft) {
            g2.drawImage(this.sprite, this.screenX + Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE, this.screenY, Constants.PLAYER_SPRITE_TILE_SIZE * -Constants.SCALE, Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE, null);
        } else {
            g2.drawImage(this.sprite, this.screenX, this.screenY, Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE, Constants.PLAYER_SPRITE_TILE_SIZE * Constants.SCALE, null);
        }
    }
}
