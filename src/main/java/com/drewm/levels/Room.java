package com.drewm.levels;

import com.drewm.entities.BasicZombie;
import com.drewm.objects.Collectable;
import com.drewm.objects.Door;

import java.util.List;

public class Room {
    private int roomNumTileWidth;
    private int roomNumTileHeight;
    private int[][] worldMap;
    private int[][] worldBackground;
    private List<BasicZombie> basicZombies;
    private List<Collectable> collectables;
    private List<Door> doors;

    public Room(int[][] worldMap, int roomNumTileWidth, int roomNumTileHeight, int[][] worldBackground, List<BasicZombie> basicZombies, List<Collectable> collectables, List<Door> doors) {
        this.worldMap = worldMap;
        this.worldBackground = worldBackground;
        this.basicZombies = basicZombies;
        this.collectables = collectables;
        this.doors = doors;

        this.roomNumTileWidth = roomNumTileWidth;
        this.roomNumTileHeight = roomNumTileHeight;
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

    public int getRoomNumTileWidth() {
        return roomNumTileWidth;
    }

    public int getRoomNumTileHeight() {
        return roomNumTileHeight;
    }
}
