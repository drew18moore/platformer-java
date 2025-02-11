package levels;

import main.Window;
import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TileManager {
    Tile[] tiles;
    int worldMap[][];

    public TileManager() {
        try {
            this.tiles = getTileSet("/tilesets/tileset0.png");
            this.worldMap = getMapFromCSV("/maps/map0.csv");
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
                tiles[t] = new Tile(tileSet.getSubimage(j * Constants.TILE_WIDTH, i * Constants.TILE_WIDTH, Constants.TILE_WIDTH, Constants.TILE_WIDTH), t != 11 && t != 6);
                t++;
            }
        }
        return tiles;
    }

    private int[][] getMapFromCSV(String path) throws IOException {
        List<int[]> rows = new ArrayList<>();

        InputStream is = getClass().getResourceAsStream(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;

        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            int[] intValues = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                intValues[i] = Integer.parseInt(values[i]);
            }
            rows.add(intValues);
        }

        int[][] array = new int[Constants.WORLD_MAP_NUM_TILE_WIDTH][Constants.WORLD_MAP_NUM_TILE_HEIGHT];
        for (int i = 0; i < rows.size(); i++) {
            array[i] = rows.get(i);
        }

        return array;
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

    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < Constants.WORLD_MAP_NUM_TILE_WIDTH && worldRow < Constants.WORLD_MAP_NUM_TILE_HEIGHT) {
            int tile = this.worldMap[worldRow][worldCol];

            int worldX = worldCol * Constants.TILE_SIZE;
            int worldY = worldRow * Constants.TILE_SIZE;
            float screenX = worldX - main.Window.getWindow().playing.player.worldX + main.Window.getWindow().playing.player.screenX;
            float screenY = worldY - main.Window.getWindow().playing.player.worldY + main.Window.getWindow().playing.player.screenY;

            if (tile != -1 &&
                    worldX + Constants.TILE_SIZE * 2 > main.Window.getWindow().playing.player.worldX - main.Window.getWindow().playing.player.screenX &&
                    worldX - Constants.TILE_SIZE * 2 < main.Window.getWindow().playing.player.worldX + main.Window.getWindow().playing.player.screenX &&
                    worldY + Constants.TILE_SIZE * 2 > main.Window.getWindow().playing.player.worldY - main.Window.getWindow().playing.player.screenY &&
                    worldY - Constants.TILE_SIZE * 2 < main.Window.getWindow().playing.player.worldY + Window.getWindow().playing.player.screenY) {
                g2.drawImage(tiles[tile].image, (int) screenX, (int) screenY, Constants.TILE_SIZE, Constants.TILE_SIZE, null);
            }
            worldCol++;

            if (worldCol == Constants.WORLD_MAP_NUM_TILE_WIDTH) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
