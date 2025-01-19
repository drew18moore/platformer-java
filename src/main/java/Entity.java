import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int worldX, worldY;
    public int spriteWidth, spriteHeight;
    public int hitboxWidth, hitboxHeight, hitboxOffsetX, hitboxOffsetY;

    public float velocityY = 0;

    public boolean isSolid = false;
    public boolean useGravity = false;
    public boolean showHitbox = false;

    BufferedImage[] movementSprites;
    public int spriteCounter = 0;
    public int spriteNum = 0;
    public boolean facingLeft = false;

    public Entity(int worldX, int worldY, BufferedImage[] movementSprites, int spriteWidth, int spriteHeight) {
        this.worldX = worldX;
        this.worldY = worldY;

        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;

        this.hitboxOffsetX = 0;
        this.hitboxOffsetY = 0;
        this.hitboxWidth = spriteWidth;
        this.hitboxHeight = spriteHeight;

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
        // Apply gravity if enabled
        if (useGravity) {
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
    }

    public void draw(Graphics2D g2) {
        if (facingLeft) {
            g2.drawImage(this.movementSprites[spriteNum], this.worldX + this.spriteWidth * Constants.SCALE, this.worldY, this.spriteWidth * -Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        } else {
            g2.drawImage(this.movementSprites[spriteNum], this.worldX, this.worldY, this.spriteWidth * Constants.SCALE, this.spriteHeight * Constants.SCALE, null);
        }

        if (showHitbox) {
            Rectangle hitbox = getBounds();
            g2.setColor(Color.RED);
            g2.drawRect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(worldX + hitboxOffsetX, worldY + hitboxOffsetY, hitboxWidth, hitboxHeight);
    }

    public boolean isColliding(int worldX, int worldY) {
        TileManager tileManager = Window.getWindow().tileManager;

        int leftX = worldX + hitboxOffsetX;
        int rightX = worldX + hitboxOffsetX + hitboxWidth - 1;
        int topY = worldY + hitboxOffsetY;
        int bottomY = worldY + hitboxOffsetY + hitboxHeight - 1;

        return tileManager.isSolidTile(leftX, topY) || tileManager.isSolidTile(rightX, topY) ||
                tileManager.isSolidTile(leftX, bottomY) || tileManager.isSolidTile(rightX, bottomY);
    }
}
