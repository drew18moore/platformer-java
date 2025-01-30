package utils;

public class Constants {
    public static final int SCALE = 3;
    public static final int TILE_SIZE = 16 * SCALE;
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 12;
    public static final int SCREEN_WIDTH = TILE_WIDTH * TILE_SIZE;
    public static final int SCREEN_HEIGHT = TILE_HEIGHT * TILE_SIZE;
    public static final String SCREEN_TITLE = "Platformer";

    public static final int PLAYER_SPEED = 3;

    public static final float GRAVITY_ASCEND = 0.3f;
    public static final float GRAVITY_DESCEND = 0.6f;
    public static final float TERMINAL_VELOCITY = 12.0f;
    public static final float JUMP_FORCE = 10.0f;

    public static final int PLAYER_SPRITE_TILE_SIZE = 32;

    public static final int WORLD_MAP_NUM_TILE_WIDTH = 30;
    public static final int WORLD_MAP_NUM_TILE_HEIGHT = 20;

    public static final int WORLD_TILE_SET_NUM_TILE_WIDTH = 16;
    public static final int WORLD_TILE_SET_NUM_TILE_HEIGHT = 16;

    public static final int WORLD_TILE_SET_NUM_TILES = WORLD_TILE_SET_NUM_TILE_WIDTH * WORLD_TILE_SET_NUM_TILE_HEIGHT;

    public static final int BTN_WIDTH = 100;
    public static final int BTN_HEIGHT = 25;
    public static final int BTN_WIDTH_SCALED = BTN_WIDTH * SCALE;
    public static final int BTN_HEIGHT_SCALED = BTN_HEIGHT * SCALE;
}
