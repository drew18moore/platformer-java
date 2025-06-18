package com.drewm.levels;

import com.drewm.data.*;
import com.drewm.entities.BasicZombie;
import com.drewm.entities.Player;
import com.drewm.gamestates.Playing;
import com.drewm.objects.*;
import com.drewm.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelManager {
    private final Playing playing;
    Tile[] tiles;
    Tile[] backgroundTiles;
    int currentRoomIdx;
    float playerSpawnWorldX, playerSpawnWorldY;
    Map<Integer, Room> rooms;

    public LevelManager(Playing playing) {
        this.playing = playing;
        loadRooms(playing.getCurrentLevelFilePath(), false);
    }

    public void loadRooms(String filePath, boolean persistPlayerState) {
        try {
            this.currentRoomIdx = 0;
            this.rooms = new HashMap<>();
            // Load tilesets
            this.tiles = getTileSet("/tilesets/tileset0.png", Constants.WORLD_TILE_SET_NUM_TILE_WIDTH, Constants.WORLD_TILE_SET_NUM_TILE_HEIGHT);
            this.backgroundTiles = getTileSet("/tilesets/background-tileset0.png", Constants.WORLD_BACKGROUND_TILE_SET_NUM_TILE_WIDTH, Constants.WORLD_BACKGROUND_TILE_SET_NUM_TILE_HEIGHT);

            ObjectMapper objectMapper = new ObjectMapper();
            LevelData levelData = objectMapper.readValue(getClass().getResourceAsStream(filePath), LevelData.class);

            for (int i = 0; i < levelData.rooms().size(); i++) {
                RoomData room = levelData.rooms().get(i);
                List<int[]> tileList = room.tiles();
                int roomNumTileWidth = tileList.get(0).length;
                int roomNumTileHeight = tileList.size();
                int[][] worldMap = new int[roomNumTileHeight][roomNumTileWidth];
                for (int j = 0; j < tileList.size(); j++) {
                    int[] tileRow = tileList.get(j);
                    System.arraycopy(tileRow, 0, worldMap[j], 0, tileRow.length);
                }

                int[][] worldBackground = new int[roomNumTileHeight][roomNumTileWidth];
                tileList = room.background();
                for (int j = 0; j < tileList.size(); j++) {
                    int[] tileRow = tileList.get(j);
                    System.arraycopy(tileRow, 0, worldBackground[j], 0, tileRow.length);
                }

                // Load player
                if (!persistPlayerState) {
                    EntityData playerData = levelData.players().get(0);
                    this.playerSpawnWorldX = playerData.x();
                    this.playerSpawnWorldY = playerData.y();
                    playing.player = new Player(playerData.x(), playerData.y(), this.playing, this.playing.bullets);
                }

                // Load enemies
                List<EntityData> enemies = room.enemies();
                List<BasicZombie> basicZombies = new ArrayList<>();
                for (EntityData enemy : enemies) {
                    basicZombies.add(new BasicZombie(enemy.x(), enemy.y(), this.playing));
                }

                // Load coins
                List<CollectableData> collectablesData = room.collectables();
                List<Collectable> collectables = new ArrayList<>();
                for (CollectableData collectable : collectablesData) {
                    collectables.add(new Collectable(collectable.x(), collectable.y(), collectable.itemType(), this.playing));
                }

                // Load doors
                List<DoorData> doorsData = room.doors();
                List<Door> doors = new ArrayList<>();
                for (DoorData door : doorsData) {
                    if (door.isGoal()) {
                        doors.add(new Door(door.x(), door.y(), door.isGoal(), door.lockType(), this.playing));
                    } else {
                        doors.add(new Door(door.x(), door.y(), door.destinationRoom(), door.destinationX(), door.destinationY(), door.lockType(), this.playing));
                    }
                }

                // Load floating mines
                List<FloatingMineData> floatingMineData = room.floatingMines();
                List<FloatingMine> floatingMines = new ArrayList<>();
                for (FloatingMineData floatingMine : floatingMineData) {
                    floatingMines.add(new FloatingMine(floatingMine.startX(), floatingMine.startY(), floatingMine.endX(), floatingMine.endY(), playing));
                }

                // Load saw blades
                List<SawBladeData> sawBladeData = room.sawBlades();
                List<SawBlade> sawBlades = new ArrayList<>();
                for (SawBladeData sawBlade : sawBladeData) {
                    sawBlades.add(new SawBlade(sawBlade.startX(), sawBlade.endX(), sawBlade.y(), sawBlade.upsideDown(), playing));
                }

                // Load lasers
                List<LaserData> laserData = room.lasers();
                List<Laser> lasers = new ArrayList<>();
                for (LaserData laser : laserData) {
                    lasers.add(new Laser(laser.x1(), laser.y1(), laser.x2(), laser.y2(), laser.activationIntervalMS(), laser.delayMS(), playing));
                }

                // Load moving platforms
                List<MovingPlatformData> movingPlatformData = room.movingPlatforms();
                List<MovingPlatform> movingPlatforms = new ArrayList<>();
                for (MovingPlatformData movingPlatform : movingPlatformData) {
                    movingPlatforms.add(new MovingPlatform(movingPlatform.startX(), movingPlatform.startY(), movingPlatform.endX(), movingPlatform.endY(), movingPlatform.width(), movingPlatform.height(), movingPlatform.speed(), playing));
                }

                // Load falling blocks
                List<FallingBlockData> fallingBlockData = room.fallingBlocks();
                List<FallingBlock> fallingBlocks = new ArrayList<>();
                for (FallingBlockData fallingBlock : fallingBlockData) {
                    fallingBlocks.add(new FallingBlock(fallingBlock.x(), fallingBlock.y(), fallingBlock.width(), fallingBlock.height(), playing));
                }

                rooms.put(i, new Room(worldMap, roomNumTileWidth, roomNumTileHeight, worldBackground, basicZombies, collectables, doors, floatingMines, sawBlades, lasers, movingPlatforms, fallingBlocks));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tile[] getTileSet(String path, int width, int height) throws IOException {
        Tile[] tiles;
        BufferedImage tileSet = ImageIO.read(getClass().getResourceAsStream(path));
        tiles = new Tile[Constants.WORLD_TILE_SET_NUM_TILES];
        int t = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                tiles[t] = new Tile(tileSet.getSubimage(j * Constants.TILE_WIDTH, i * Constants.TILE_WIDTH, Constants.TILE_WIDTH, Constants.TILE_WIDTH), t != 12 && t != 6, t == 6, t == 14);
                t++;
            }
        }
        return tiles;
    }

    public boolean isSolidTile(int worldX, int worldY) {
        int col = worldX / Constants.TILE_SIZE;
        int row = worldY / Constants.TILE_SIZE;

        if (col < 0 || col >= getCurrentRoom().getRoomNumTileWidth() || row < 0 || row >= getCurrentRoom().getRoomNumTileHeight()) {
            return true;
        }

        int tileIndex = getCurrentRoom().getWorldMap()[row][col];
        return tileIndex >= 0 && tiles[tileIndex].collision;
    }

    public boolean isGoalTile(int worldX, int worldY) {
        int col = worldX / Constants.TILE_SIZE;
        int row = worldY / Constants.TILE_SIZE;

        if (col < 0 || col >= getCurrentRoom().getRoomNumTileWidth() || row < 0 || row >= getCurrentRoom().getRoomNumTileHeight()) {
            return false;
        }

        int tileIndex = getCurrentRoom().getWorldMap()[row][col];
        return tileIndex >= 0 && tiles[tileIndex].isGoal;
    }

    public boolean isSpikeTile(int worldX, int worldY) {
        int col = worldX / Constants.TILE_SIZE;
        int row = worldY / Constants.TILE_SIZE;

        if (col < 0 || col >= getCurrentRoom().getRoomNumTileWidth() || row < 0 || row >= getCurrentRoom().getRoomNumTileHeight()) {
            return false;
        }

        int tileIndex = getCurrentRoom().getWorldMap()[row][col];
        return tileIndex >= 0 && tiles[tileIndex].isSpike;
    }

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;
        float cameraX = playing.camera.getCameraX();
        float cameraY = playing.camera.getCameraY();

        while (worldCol < getCurrentRoom().getRoomNumTileWidth() && worldRow < getCurrentRoom().getRoomNumTileHeight()) {
            int tile = getCurrentRoom().getWorldMap()[worldRow][worldCol];
            int background = getCurrentRoom().getWorldBackground()[worldRow][worldCol];

            int worldX = worldCol * Constants.TILE_SIZE;
            int worldY = worldRow * Constants.TILE_SIZE;
            float screenX = worldX - cameraX;
            float screenY = worldY - cameraY;

            if (tile != -1 &&
                    screenX + Constants.TILE_SIZE > 0 &&
                    screenX < Constants.SCREEN_WIDTH &&
                    screenY + Constants.TILE_SIZE > 0 &&
                    screenY < Constants.SCREEN_HEIGHT) {
                int drawX = Math.round(screenX);
                int drawY = Math.round(screenY);
                g2.drawImage(backgroundTiles[background].image, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
                g2.drawImage(tiles[tile].image, drawX, drawY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
            }
            worldCol++;

            if (worldCol == getCurrentRoom().getRoomNumTileWidth()) {
                worldCol = 0;
                worldRow++;
            }
        }
    }

    public Room getCurrentRoom() {
        return rooms.get(currentRoomIdx);
    }

    public void setCurrentRoomIdx(int idx) {
        this.currentRoomIdx = idx;
    }

    public float getPlayerSpawnX() {
        return playerSpawnWorldX;
    }

    public float getPlayerSpawnY() {
        return playerSpawnWorldY;
    }

    public boolean isKeycardAlreadyDropped() {
        for (Collectable c : getCurrentRoom().getCollectables()) {
            if (c.getItemType() == ItemType.KEYCARD) {
                return true;
            }
        }
        return false;
    }

}
