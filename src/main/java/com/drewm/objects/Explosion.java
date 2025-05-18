package com.drewm.objects;

import com.drewm.utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Explosion {
    private float worldX, worldY;
    private BufferedImage[] frames;
    private int frameWidth = 0;
    private int currentFrame = 0;
    private int frameCounter = 0;
    private boolean finished = false;

    public Explosion(float x, float y) {
        this.worldX = x;
        this.worldY = y;

        try {
            BufferedImage spriteSheet = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sprites/explosion.png")));
            int frameCount = 33;
            this.frameWidth = spriteSheet.getWidth() / frameCount;
            frames = new BufferedImage[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = spriteSheet.getSubimage(
                        i * frameWidth,
                        0,
                        frameWidth,
                        frameWidth
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        frameCounter++;
        if (frameCounter > 5) {
            currentFrame++;
            frameCounter = 0;
            if (currentFrame >= frames.length) {
                finished = true;
            }
        }
    }

    public void draw(Graphics2D g2, float cameraX, float cameraY) {
        if (!finished) {
            float screenX = (worldX - (float) frameWidth * Constants.SCALE / 2) - cameraX;
            float screenY = (worldY - (float) frameWidth * Constants.SCALE / 2) - cameraY;
            g2.drawImage(frames[currentFrame], (int) screenX, (int) screenY,
                    this.frameWidth * Constants.SCALE, this.frameWidth * Constants.SCALE, null);
        }
    }

    public boolean isFinished() {
        return finished;
    }
}

