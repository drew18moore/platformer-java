import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int screenX;
    public int screenY;

    public float velocityY = 0;

    public boolean isSolid = false;
    public boolean useGravity = false;

    BufferedImage sprite;

    public Entity(int screenX, int screenY, BufferedImage sprite) {
        this.screenX = screenX;
        this.screenY = screenY;

        if (sprite != null) {
            this.sprite = sprite;
        } else {
            BufferedImage placeholder = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.BLUE);
            g2.fillRect(0, 0, 50, 50);
            g2.dispose();
            this.sprite = placeholder;
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
        g2.drawImage(this.sprite, this.screenX, this.screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(screenX, screenY, Constants.TILE_SIZE, Constants.TILE_SIZE);
    }
}
