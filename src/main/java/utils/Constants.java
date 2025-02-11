package utils;

public class Constants {
    public static final int SCALE = 3;
    public static final int TILE_SIZE = 16 * SCALE;
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 12;
    public static final int SCREEN_WIDTH = TILE_WIDTH * TILE_SIZE;
    public static final int SCREEN_HEIGHT = TILE_HEIGHT * TILE_SIZE;
    public static final String SCREEN_TITLE = "Platformer";

    public static final int FPS = 120;
    public static final int UPS = 200;

    public static final float PLAYER_SPEED = 1.2f;

    public static final float GRAVITY_ASCEND = 0.15f;
    public static final float GRAVITY_DESCEND = 0.25f;
    public static final float TERMINAL_VELOCITY = 5.0f;
    public static final float JUMP_FORCE = 7.0f;

    public static final int PLAYER_SPRITE_TILE_SIZE = 32;

    public static final int WORLD_MAP_NUM_TILE_WIDTH = 64;
    public static final int WORLD_MAP_NUM_TILE_HEIGHT = 64;

    public static final int WORLD_TILE_SET_NUM_TILE_WIDTH = 7;
    public static final int WORLD_TILE_SET_NUM_TILE_HEIGHT = 3;

    public static final int WORLD_TILE_SET_NUM_TILES = WORLD_TILE_SET_NUM_TILE_WIDTH * WORLD_TILE_SET_NUM_TILE_HEIGHT;

    public static final int BTN_WIDTH = 100;
    public static final int BTN_HEIGHT = 25;
    public static final int BTN_WIDTH_SCALED = BTN_WIDTH * SCALE;
    public static final int BTN_HEIGHT_SCALED = BTN_HEIGHT * SCALE;
}
