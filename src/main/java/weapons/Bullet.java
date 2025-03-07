package weapons;

import entities.Player;
import main.Window;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Bullet {
    private final Player player;
    public float worldX, worldY;
    public BufferedImage sprite;
    public float velocityX, velocityY;
    public double angle;
    public boolean facingLeft;

    public Bullet(float x, float y, double angle, boolean facingLeft, float speed, Player player) {
        this.player = player;
        this.worldX = x;
        this.worldY = y;
        this.angle = angle;
        this.facingLeft = facingLeft;

        float velX = speed * (float) Math.cos(Math.toRadians(Math.abs(angle)));
        float velY = speed * (float) Math.sin(Math.toRadians(Math.abs(angle)));

        if (facingLeft) {
            velX *= -1;
            velY *= angle <= 0 ? 1 : -1;
        } else {
            velY *= angle <= 0 ? -1 : 1;
        }

        System.out.println(velX + " | " + velY);
        this.velocityX = velX;
        this.velocityY = velY;

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        this.worldX += velocityX;
        this.worldY += velocityY;
    }

    public void draw(Graphics2D g2) {
        float screenX = this.worldX - player.worldX + player.screenX;
        float screenY = this.worldY - player.worldY + player.screenY;
        if (worldX + Constants.TILE_SIZE * 2 > player.worldX - player.screenX &&
                worldX - Constants.TILE_SIZE * 2 < player.worldX + player.screenX &&
                worldY + Constants.TILE_SIZE * 2 > player.worldY - player.screenY &&
                worldY - Constants.TILE_SIZE * 2 < player.worldY + Window.getWindow().playing.player.screenY) {
            AffineTransform old = g2.getTransform();
            AffineTransform transform = new AffineTransform();

            transform.translate(screenX, screenY - sprite.getHeight());
            transform.rotate(Math.toRadians(angle));

            if (player.facingLeft) transform.scale(-1, 1);
            g2.drawImage(sprite, transform, null);
            g2.setTransform(old);
        }
    }
}
