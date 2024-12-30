import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    public Player(int screenX, int screenY, BufferedImage sprite) {
        super(screenX, screenY, sprite);
        this.useGravity = true;
    }

    public void update(double dt) {
        super.update(dt);
    }

    public void jump() {
        this.velocityY -= 10;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(this.sprite, this.screenX, this.screenY, null);
    }
}
