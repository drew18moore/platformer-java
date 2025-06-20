package com.drewm.levels;

import com.drewm.entities.BasicZombie;
import com.drewm.objects.*;

import java.util.List;

public class Room {
    private final int roomNumTileWidth;
    private final int roomNumTileHeight;
    private int[][] worldMap;
    private int[][] worldBackground;
    private List<BasicZombie> basicZombies;
    private List<Collectable> collectables;
    private List<Door> doors;
    private List<FloatingMine> floatingMines;
    private List<SawBlade> sawBlades;
    private List<Laser> lasers;
    private List<MovingPlatform> movingPlatforms;
    private List<FallingBlock> fallingBlocks;

    public Room(int[][] worldMap,
                int roomNumTileWidth,
                int roomNumTileHeight,
                int[][] worldBackground,
                List<BasicZombie> basicZombies,
                List<Collectable> collectables,
                List<Door> doors,
                List<FloatingMine> floatingMines,
                List<SawBlade> sawBlades,
                List<Laser> lasers,
                List<MovingPlatform> movingPlatforms,
                List<FallingBlock> fallingBlocks
    ) {
        this.worldMap = worldMap;
        this.worldBackground = worldBackground;
        this.basicZombies = basicZombies;
        this.collectables = collectables;
        this.doors = doors;
        this.floatingMines = floatingMines;
        this.sawBlades = sawBlades;
        this.lasers = lasers;
        this.movingPlatforms = movingPlatforms;

        this.roomNumTileWidth = roomNumTileWidth;
        this.roomNumTileHeight = roomNumTileHeight;
        this.fallingBlocks = fallingBlocks;
    }

    public int[][] getWorldMap() {
        return worldMap;
    }

    public void setWorldMap(int[][] map) {
        this.worldMap = map;
    }

    public int[][] getWorldBackground() {
        return worldBackground;
    }

    public void setWorldBackground(int[][] background) {
        this.worldBackground = background;
    }

    public List<BasicZombie> getBasicZombies() {
        return basicZombies;
    }

    public void setBasicZombies(List<BasicZombie> enemies) {
        this.basicZombies = enemies;
    }

    public List<Collectable> getCollectables() {
        return collectables;
    }

    public void setCollectables(List<Collectable> collectables) {
        this.collectables = collectables;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public List<FloatingMine> getFloatingMines() { return floatingMines; }

    public void setFloatingMines(List<FloatingMine> floatingMines) { this.floatingMines = floatingMines; }

    public List<SawBlade> getSawBlades() { return sawBlades; }

    public void setSawBlades(List<SawBlade> sawBlades) { this.sawBlades = sawBlades; }

    public List<Laser> getLasers() { return lasers; }

    public void setLasers(List<Laser> lasers) { this.lasers = lasers; }

    public int getRoomNumTileWidth() {
        return roomNumTileWidth;
    }

    public int getRoomNumTileHeight() {
        return roomNumTileHeight;
    }

    public List<MovingPlatform> getMovingPlatforms() { return movingPlatforms; }

    public List<FallingBlock> getFallingBlocks() { return fallingBlocks; }
}
