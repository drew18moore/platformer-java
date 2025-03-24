package com.drewm.weapons;

import com.drewm.entities.BasicZombie;
import com.drewm.entities.Player;
import com.drewm.levels.TileManager;
import com.drewm.main.Window;
import com.drewm.utils.Constants;

import java.awt.geom.Rectangle2D;
import java.util.List;
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
    private final int damage = 10;

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

        this.velocityX = velX;
        this.velocityY = velY;

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean update(List<BasicZombie> zombies) {
        float nextWorldX = this.worldX + velocityX;
        float nextWorldY = this.worldY + velocityY;
        if(!isColliding((int) nextWorldX, (int) nextWorldY)) {
            this.worldX += velocityX;
            this.worldY += velocityY;
        } else {
            return true;
        }

        for (BasicZombie zombie : zombies) {
            if (getWorldBounds().intersects(zombie.getWorldBounds())) {
                zombie.takeDamage(damage);
                return true;
            }
        }
        return false;
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

    public Rectangle2D.Float getWorldBounds() {
        return new Rectangle2D.Float(worldX, worldY, sprite.getWidth(), sprite.getHeight());
    }

    public boolean isColliding(int worldX, int worldY) {
        TileManager tileManager = Window.getWindow().playing.tileManager;

        int leftX = worldX;
        int rightX = worldX + sprite.getWidth();
        int topY = worldY;
        int bottomY = worldY + sprite.getHeight();

        return tileManager.isSolidTile(leftX, topY) || tileManager.isSolidTile(rightX, topY) ||
                tileManager.isSolidTile(leftX, bottomY) || tileManager.isSolidTile(rightX, bottomY);
    }
}
