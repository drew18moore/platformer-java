package com.drewm.levels;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public boolean collision = false;
    public boolean isGoal = false;
    public boolean isSpike = false;

    public Tile(BufferedImage image, boolean collision, boolean isGoal, boolean isSpike) {
        this.image = image;
        this.collision = collision;
        this.isGoal = isGoal;
        this.isSpike = isSpike;
    }
}
