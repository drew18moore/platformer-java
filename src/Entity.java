import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public int screenX;
    public int screenY;

    BufferedImage sprite;

    public Entity(int screenX, int screenY, BufferedImage sprite) {
        this.screenX = screenX;
        this.screenY = screenY;

        if (sprite != null) {
            this.sprite = sprite;
        } else {
            BufferedImage placeholder = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.BLUE);
            g2.fillRect(0, 0, 50, 50);
            g2.dispose();
            this.sprite = placeholder;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(screenX, screenY, sprite.getWidth(), sprite.getHeight());
    }
}
