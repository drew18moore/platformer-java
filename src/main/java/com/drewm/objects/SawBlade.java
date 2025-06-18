package com.drewm.objects;

import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SawBlade {
    private final Playing playing;
    private float worldX, worldY;
    private final float leftBound, rightBound;
    private boolean movingRight;
    private BufferedImage[] spriteFrames;
    private int spriteCounter = 0;
    private int spriteNum = 0;
    private float speed = 1f;
    private boolean upsideDown;
    private boolean showHitbox = false;

    public SawBlade(float startX, float endX, float y, boolean upsideDown, Playing playing) {
        this.playing = playing;
        if (endX > startX) {
            this.leftBound = startX;
            this.rightBound = endX;
            this.movingRight = true;
        } else {
            this.leftBound = endX;
            this.rightBound = startX;
            this.movingRight = false;
        }
        this.worldX = startX;
        this.worldY = y;
        this.upsideDown = upsideDown;

        try {
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/saw-blade-sm.png")));

            this.spriteFrames = new BufferedImage[4];
            for (int i = 0; i < spriteFrames.length; i++) {
                BufferedImage frame = spriteSheet.getSubimage(
                        i * Constants.TILE_WIDTH,
                        0,
                        Constants.TILE_WIDTH,
                        Constants.TILE_WIDTH / 2
                );

                if (upsideDown) {
                    // Flip the frame vertically
                    AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
                    tx.translate(0, -frame.getHeight());
                    AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    frame = op.filter(frame, null);
                }

                spriteFrames[i] = frame;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        worldX += movingRight ? speed : -speed;

        if (movingRight && worldX >= rightBound) {
            worldX = rightBound;
            movingRight = false;
        } else if (!movingRight && worldX <= leftBound) {
            worldX = leftBound;
            movingRight = true;
        }

        spriteCounter++;
        if (spriteCounter > 24) {
            if (spriteNum >= spriteFrames.length - 1) {
                spriteNum = 0;
            } else {
                spriteNum++;
            }
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2) {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();
        if (screenX + Constants.TILE_SIZE > 0 &&
                screenX < Constants.SCREEN_WIDTH &&
                screenY + Constants.TILE_SIZE > 0 &&
                screenY < Constants.SCREEN_HEIGHT) {
            g2.drawImage(spriteFrames[spriteNum], (int) screenX, (int) screenY, spriteFrames[0].getWidth() * Constants.SCALE, spriteFrames[0].getHeight() * Constants.SCALE, null);
            if (showHitbox) g2.draw(getScreenBounds());
        }
    }

    public Arc2D.Float getScreenBounds() {
        float screenX = this.worldX - playing.camera.getCameraX();
        float screenY = this.worldY - playing.camera.getCameraY();
        screenY -= upsideDown ? spriteFrames[0].getHeight() * Constants.SCALE : 0;
        float startingAngle = upsideDown ? 180f : 0f;
        float extent = 180f;
        return new Arc2D.Float(screenX, screenY, spriteFrames[0].getWidth() * Constants.SCALE, spriteFrames[0].getWidth() * Constants.SCALE, startingAngle, extent, Arc2D.PIE);
    }
}
