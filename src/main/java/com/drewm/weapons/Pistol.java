package com.drewm.weapons;

import com.drewm.entities.Player;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Pistol {
    private final Player player;
    private int pivotScreenX, pivotScreenY;
    private double angle;
    private BufferedImage sprite;
    public List<Bullet> bullets;
    private int bulletsRemaining = Constants.STARTING_AMMO;

    private BufferedImage ammoCount;

    public float muzzleWorldX, muzzleWorldY;

    public Pistol(Player player, List<Bullet> bullets) {
        this.player = player;
        this.ammoCount = player.updateHudText("Ammo: ", bulletsRemaining);
        this.bullets = bullets;
        calculatePivotScreenPosition();

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/pistol.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        calculatePivotScreenPosition();
        calculateMuzzleWorldPosition();
    }

    private void calculatePivotScreenPosition() {
        int pistolScreenX;
        if (player.facingLeft) {
            pistolScreenX = player.screenX + player.hitboxOffsetX + player.spriteWidth * Constants.SCALE / 2;
        } else {
            pistolScreenX = player.screenX + player.hitboxOffsetX + player.hitboxWidth - player.spriteWidth * Constants.SCALE / 2;
        }

        int pistolScreenY = player.screenY + player.hitboxOffsetY + player.hitboxHeight / 3;

        pivotScreenX = pistolScreenX;
        pivotScreenY = pistolScreenY;
    }

    private void calculateMuzzleWorldPosition() {
        float scale = Constants.WEAPON_SCALE;
        float radians = (float) Math.toRadians(angle);

        // Find the pivot point in world coordinates
        float pivotWorldX;
        if (player.facingLeft) {
            pivotWorldX = player.worldX + player.hitboxOffsetX + player.spriteWidth * Constants.SCALE / 2f;
        } else {
            pivotWorldX = player.worldX + player.hitboxOffsetX + player.hitboxWidth - player.spriteWidth * Constants.SCALE / 2f;
        }

        float pivotWorldY = player.worldY + player.hitboxOffsetY + player.hitboxHeight / 3f;

        // Muzzle offset in local weapon space (along the weapon length)
        float muzzleOffsetX = (sprite.getWidth() - 3) * scale;
        if (player.facingLeft) muzzleOffsetX = -muzzleOffsetX;

        // Rotate the offset
        float rotatedX = muzzleOffsetX * (float) Math.cos(radians);
        float rotatedY = muzzleOffsetX * (float) Math.sin(radians);

        // Add to the pivot world position
        muzzleWorldX = pivotWorldX + rotatedX;
        muzzleWorldY = pivotWorldY + rotatedY;
    }



    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        AffineTransform transform = new AffineTransform();

        transform.translate(pivotScreenX, pivotScreenY);
        transform.rotate(Math.toRadians(angle));
        transform.translate(0, -3 * Constants.WEAPON_SCALE);

        if (player.facingLeft) {
            transform.scale(-Constants.WEAPON_SCALE, Constants.WEAPON_SCALE);
        } else {
            transform.scale(Constants.WEAPON_SCALE, Constants.WEAPON_SCALE);
        }

        g2.drawImage(sprite, transform, null);
        g2.setTransform(old);

        // Draw muzzle debug rectangle
        int rectSize = 2;
        int muzzleScreenX = (int) (muzzleWorldX - player.worldX + player.screenX);
        int muzzleScreenY = (int) (muzzleWorldY - player.worldY + player.screenY);

        // Draw debug line showing bullet trajectory
        g2.setColor(Color.RED);
        double radians = Math.toRadians(player.facingLeft ? angle - 180 : angle);
        int lineLength = 500;
        int endX = muzzleScreenX + (int) (Math.cos(radians) * lineLength);
        int endY = muzzleScreenY + (int) (Math.sin(radians) * lineLength);

        g2.drawLine(muzzleScreenX, muzzleScreenY, endX, endY);
        g2.fillRect(muzzleScreenX, muzzleScreenY, rectSize, rectSize);

        g2.drawImage(ammoCount, 0, 50, null);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && bulletsRemaining > 0) {
            this.bullets.add(new Bullet(muzzleWorldX, muzzleWorldY, angle, player.facingLeft, Constants.BULLET_SPEED, player));
            setBulletsRemaining(getBulletsRemaining()-1);
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        angle = Math.toDegrees(Math.atan2(mouseY - pivotScreenY, mouseX - pivotScreenX));

        player.facingLeft = mouseX < player.screenX + player.hitboxOffsetX + player.hitboxWidth / 2;
        if (player.facingLeft) angle += 180;
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }

    public int getBulletsRemaining() {
        return this.bulletsRemaining;
    }

    public void setBulletsRemaining(int amount) {
        this.bulletsRemaining = amount;
        this.ammoCount = player.updateHudText("Ammo: ", bulletsRemaining);
    }
}
