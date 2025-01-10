import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX;
    public int worldY;

    public int spriteWidth;
    public int spriteHeight;

    public float velocityY = 0;

    public boolean isSolid = false;
    public boolean useGravity = false;

    BufferedImage[] movementSprites;
    public int spriteCounter = 0;
    public int spriteNum = 0;
    public boolean facingLeft = false;

    public Entity(int worldX, int worldY, BufferedImage[] movementSprites, int spriteWidth, int spriteHeight) {
        this.worldX = worldX;
        this.worldY = worldY;

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
            worldY += (int)velocityY;
            velocityY += Constants.GRAVITATIONAL_CONSTANT;
            if (velocityY > Constants.TERMINAL_VELOCITY) velocityY = Constants.TERMINAL_VELOCITY;
            else if (velocityY < -Constants.TERMINAL_VELOCITY) velocityY = -Constants.TERMINAL_VELOCITY;
        }
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.worldX + this.spriteWidth * Constants.SCALE, this.worldY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.worldX, this.worldY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(worldX, worldY, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
