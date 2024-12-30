import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
    private final Input keyListener;

    public Player(int screenX, int screenY, BufferedImage sprite, final Input input) {
        super(screenX, screenY, sprite);
        this.useGravity = true;
        this.keyListener = input;
    }

    public void update(double dt) {
        super.update(dt);

        if (keyListener.leftPressed) {
            screenX -= Constants.PLAYER_SPEED;
        }
        if (keyListener.rightPressed) {
            screenX += Constants.PLAYER_SPEED;
        }
    }

    public void jump() {
        this.velocityY -= 10;
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(this.sprite, this.screenX, this.screenY, null);
    }
}
