package weapons;

import entities.Player;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Pistol {
    private final Player player;
    private int pivotScreenX, pivotScreenY;
    private int angle = 45;
    private BufferedImage sprite;
    public ArrayList<Bullet> bullets;

    public float muzzleWorldX, muzzleWorldY;

    public Pistol(Player player) {
        this.player = player;
        this.bullets = new ArrayList<>();

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/pistol.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        int pistolScreenX;
        if (player.facingLeft) {
            pistolScreenX = player.screenX + player.hitboxOffsetX;
        } else {
            pistolScreenX = player.screenX + player.hitboxOffsetX + player.hitboxWidth;
        }

        int pistolScreenY = player.screenY + player.hitboxOffsetY + player.hitboxHeight / 2;

        pivotScreenX = pistolScreenX;
        pivotScreenY = pistolScreenY;

        calculateMuzzleWorldPosition();

        bullets.forEach(Bullet::update);
    }

    private void calculateMuzzleWorldPosition() {
        float scale = Constants.WEAPON_SCALE;
        float radians = (float) Math.toRadians(angle);

        float pistolWorldX = player.worldX + player.hitboxOffsetX;
        if (!player.facingLeft) {
            pistolWorldX += player.hitboxWidth;
        }
        float pistolWorldY = player.worldY + player.hitboxOffsetY + player.hitboxHeight / 2f;
        float scaledMuzzleX = (sprite.getWidth() - 3) * scale;

        if (player.facingLeft) scaledMuzzleX = -scaledMuzzleX;

        float rotatedX = scaledMuzzleX * (float) Math.cos(radians) + 6 * (float) Math.sin(radians);
        float rotatedY = scaledMuzzleX * (float) Math.sin(radians) - 6 * (float) Math.cos(radians);

        muzzleWorldX = pistolWorldX + rotatedX;
        muzzleWorldY = pistolWorldY + rotatedY;
    }


    public void draw(Graphics2D g2) {
        AffineTransform old = g2.getTransform();
        AffineTransform transform = new AffineTransform();

        transform.translate(pivotScreenX, pivotScreenY);
        transform.rotate(Math.toRadians(angle));
        transform.translate(0, -sprite.getHeight());

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

        g2.setColor(Color.RED);
        g2.fillRect(muzzleScreenX, muzzleScreenY, rectSize, rectSize);
        bullets.forEach(bullet -> bullet.draw(g2));
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            this.bullets.add(new Bullet(muzzleWorldX, muzzleWorldY, angle, player.facingLeft, Constants.BULLET_SPEED, player));
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        int pistolX = player.screenX + player.hitboxOffsetX + player.hitboxWidth / 2;
        int pistolY = player.screenY + player.hitboxOffsetY + player.hitboxHeight / 2;

        angle = (int) Math.toDegrees(Math.atan2(mouseY - pistolY, mouseX - pistolX));

        player.facingLeft = mouseX < player.screenX + player.hitboxOffsetX + player.hitboxWidth / 2;
        if (player.facingLeft) angle += 180;
    }

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {

    }
}
