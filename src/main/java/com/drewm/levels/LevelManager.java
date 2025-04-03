package com.drewm.levels;

import com.drewm.data.*;
import com.drewm.entities.BasicZombie;
import com.drewm.entities.Player;
import com.drewm.gamestates.Playing;
import com.drewm.main.Window;
import com.drewm.objects.Coin;
import com.drewm.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private final Playing playing;
    private int[][] worldMap;
    private List<BasicZombie> basicZombies;
    private List<Coin> coins;
    Tile[] tiles;

    public LevelManager(Playing playing) {
        this.playing = playing;
    }

    public void loadLevel(String filePath, boolean persistState) {
        try {
            // Load tileset
            this.tiles = getTileSet("/tilesets/tileset0.png");

            ObjectMapper objectMapper = new ObjectMapper();
            LevelData levelData = objectMapper.readValue(getClass().getResourceAsStream(filePath), LevelData.class);

            this.worldMap = new int[Constants.WORLD_MAP_NUM_TILE_WIDTH][Constants.WORLD_MAP_NUM_TILE_HEIGHT];
            List<int[]> tileList = levelData.tiles();
            for (int i = 0; i < tileList.size(); i++) {
                int[] tileRow = tileList.get(i);
                System.arraycopy(tileRow, 0, worldMap[i], 0, tileRow.length);
            }

            // Load player
            EntityData playerData = levelData.players().get(0);
            if (persistState) {
                playing.player.respawn(playerData.x(), playerData.y());
            } else {
                playing.player = new Player(playerData.x(), playerData.y(), this.playing, this.playing.bullets);
            }

            // Load enemies
            List<EntityData> enemies = levelData.enemies();
            this.basicZombies = new ArrayList<>();
            for (EntityData enemy : enemies) {
                this.basicZombies.add(new BasicZombie(enemy.x(), enemy.y(), this.playing.player));
            }

            // Load coins
            List<CoinData> coins = levelData.coins();
            this.coins = new ArrayList<>();
            for (CoinData coin : coins) {
                this.coins.add(new Coin(coin.x(), coin.y(), this.playing.player));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tile[] getTileSet(String path) throws IOException {
        Tile[] tiles;
        BufferedImage tileSet = ImageIO.read(getClass().getResourceAsStream(path));
        tiles = new Tile[Constants.WORLD_TILE_SET_NUM_TILES];
        int t = 0;
        for (int i = 0; i < Constants.WORLD_TILE_SET_NUM_TILE_HEIGHT; i++) {
            for (int j = 0; j < Constants.WORLD_TILE_SET_NUM_TILE_WIDTH; j++) {
                tiles[t] = new Tile(tileSet.getSubimage(j * Constants.TILE_WIDTH, i * Constants.TILE_WIDTH, Constants.TILE_WIDTH, Constants.TILE_WIDTH), t != 11 && t != 6, t == 6, t == 13);
                t++;
            }
        }
        return tiles;
    }

    public boolean isSolidTile(int worldX, int worldY) {
        int col = worldX / Constants.TILE_SIZE;
        int row = worldY / Constants.TILE_SIZE;

        if (col < 0 || col >= Constants.WORLD_MAP_NUM_TILE_WIDTH || row < 0 || row >= Constants.WORLD_MAP_NUM_TILE_HEIGHT) {
            return true;
        }

        int tileIndex = worldMap[row][col];
        return tileIndex >= 0 && tiles[tileIndex].collision;
    }

    public boolean isGoalTile(int worldX, int worldY) {
        int col = worldX / Constants.TILE_SIZE;
        int row = worldY / Constants.TILE_SIZE;

        if (col < 0 || col >= Constants.WORLD_MAP_NUM_TILE_WIDTH || row < 0 || row >= Constants.WORLD_MAP_NUM_TILE_HEIGHT) {
            return false;
        }

        int tileIndex = worldMap[row][col];
        return tileIndex >= 0 && tiles[tileIndex].isGoal;
    }

    public boolean isSpikeTile(int worldX, int worldY) {
        int col = worldX / Constants.TILE_SIZE;
        int row = worldY / Constants.TILE_SIZE;

        if (col < 0 || col >= Constants.WORLD_MAP_NUM_TILE_WIDTH || row < 0 || row >= Constants.WORLD_MAP_NUM_TILE_HEIGHT) {
            return false;
        }

        int tileIndex = worldMap[row][col];
        return tileIndex >= 0 && tiles[tileIndex].isSpike;
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < Constants.WORLD_MAP_NUM_TILE_WIDTH && worldRow < Constants.WORLD_MAP_NUM_TILE_HEIGHT) {
            int tile = this.worldMap[worldRow][worldCol];

            int worldX = worldCol * Constants.TILE_SIZE;
            int worldY = worldRow * Constants.TILE_SIZE;
            float screenX = worldX - Window.getWindow().playing.player.worldX + Window.getWindow().playing.player.screenX;
            float screenY = worldY - Window.getWindow().playing.player.worldY + Window.getWindow().playing.player.screenY;

            if (tile != -1 &&
                    worldX + Constants.TILE_SIZE * 2 > Window.getWindow().playing.player.worldX - Window.getWindow().playing.player.screenX &&
                    worldX - Constants.TILE_SIZE * 2 < Window.getWindow().playing.player.worldX + Window.getWindow().playing.player.screenX &&
                    worldY + Constants.TILE_SIZE * 2 > Window.getWindow().playing.player.worldY - Window.getWindow().playing.player.screenY &&
                    worldY - Constants.TILE_SIZE * 2 < Window.getWindow().playing.player.worldY + Window.getWindow().playing.player.screenY) {
                g2.drawImage(tiles[tile].image, (int) screenX, (int) screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
            }
            worldCol++;

            if (worldCol == Constants.WORLD_MAP_NUM_TILE_WIDTH) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public List<BasicZombie> getBasicZombies() {
        return basicZombies;
    }

    public int[][] getWorldMap() {
        return worldMap;
    }
}
