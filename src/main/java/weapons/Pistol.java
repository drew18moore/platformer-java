package weapons;

import entities.Player;
import gamestates.Statemethods;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Pistol {
    private Player player;
    private int angle = 45;
    private BufferedImage sprite;

    public Pistol(Player player) {
        this.player = player;

        try {
            this.sprite = ImageIO.read(getClass().getResourceAsStream("/sprites/pistol.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

    }

    public void draw(Graphics2D g2) {
        int pistolX;
        if (player.facingLeft) {
            pistolX = player.screenX + player.hitboxOffsetX;
        } else {
            pistolX = player.screenX + player.hitboxOffsetX + player.hitboxWidth;
        }

        int pistolY = player.screenY + player.hitboxOffsetY + player.hitboxHeight / 2;

        int pivotX = pistolX;
        int pivotY = pistolY;

        AffineTransform old = g2.getTransform();
        AffineTransform transform = new AffineTransform();

        transform.translate(pivotX, pivotY);
        transform.rotate(Math.toRadians(angle));
        transform.translate(0, -sprite.getHeight());

        if (player.facingLeft) {
            transform.scale(-Constants.WEAPON_SCALE, Constants.WEAPON_SCALE);
        } else {
            transform.scale(Constants.WEAPON_SCALE, Constants.WEAPON_SCALE);
        }

        g2.drawImage(sprite, transform, null);
        g2.setTransform(old);
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

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
