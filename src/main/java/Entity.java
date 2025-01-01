import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int screenX;
    public int screenY;

    public int spriteWidth;
    public int spriteHeight;

    public float velocityY = 0;

    public boolean isSolid = false;
    public boolean useGravity = false;

    BufferedImage[] movementSprites;
    public int spriteCounter = 0;
    public int spriteNum = 0;
    public boolean facingLeft = false;

    public Entity(int screenX, int screenY, BufferedImage[] movementSprites, int spriteWidth, int spriteHeight) {
        this.screenX = screenX;
        this.screenY = screenY;

        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        if (movementSprites != null) {
            this.movementSprites = movementSprites;
        } else {
            BufferedImage placeholder = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.BLUE);
            g2.fillRect(0, 0, 50, 50);
            g2.dispose();
            this.movementSprites = new BufferedImage[]{placeholder};
        }
    }

    public void update(double dt) {
        if (useGravity) {
            screenY += (int)velocityY;
            velocityY += Constants.GRAVITATIONAL_CONSTANT;
            if (velocityY > Constants.TERMINAL_VELOCITY) velocityY = Constants.TERMINAL_VELOCITY;
            else if (velocityY < -Constants.TERMINAL_VELOCITY) velocityY = -Constants.TERMINAL_VELOCITY;
        }
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX + this.spriteWidth * Constants.SCALE, this.screenY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.screenX, this.screenY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
