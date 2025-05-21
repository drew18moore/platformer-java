package com.drewm.utils;

import java.awt.*;

public class Constants {
    public static final int SCALE = 5;
    public static final int TILE_SIZE = 16 * SCALE;
    public static final int TILE_WIDTH = 16;
    public static final int TILE_HEIGHT = 12;

    public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    public static final int SCREEN_WIDTH = SCREEN_SIZE.width;
    public static final int SCREEN_HEIGHT = SCREEN_SIZE.height;
    public static final String SCREEN_TITLE = "Jimmy Overdrive";

    public static final int FPS = 120;
    public static final int UPS = 200;

    public static final float PLAYER_STARTING_SPEED = 0.8f;
    public static final int PLAYER_STARTING_MAX_HEALTH = 1;
    public static final int PLAYER_HEALTH_FONT_SIZE = 25;

    public static final float GRAVITY_ASCEND = 0.15f;
    public static final float GRAVITY_DESCEND = 0.25f;
    public static final float TERMINAL_VELOCITY = 5.0f;
    public static final float STARTING_JUMP_FORCE = 3.0f;

    public static final int PLAYER_SPRITE_TILE_SIZE = 32;
    public static final int PLAYER_SPRITE_WIDTH = 16;
    public static final int PLAYER_SPRITE_HEIGHT = 32;

    public static final int WORLD_TILE_SET_NUM_TILE_WIDTH = 8;
    public static final int WORLD_TILE_SET_NUM_TILE_HEIGHT = 3;
    public static final int WORLD_BACKGROUND_TILE_SET_NUM_TILE_WIDTH = 7;
    public static final int WORLD_BACKGROUND_TILE_SET_NUM_TILE_HEIGHT = 3;

    public static final int WORLD_TILE_SET_NUM_TILES = WORLD_TILE_SET_NUM_TILE_WIDTH * WORLD_TILE_SET_NUM_TILE_HEIGHT;

    public static final int BTN_WIDTH = 100;
    public static final int BTN_HEIGHT = 15;
    public static final int BTN_WIDTH_SCALED = BTN_WIDTH * SCALE;
    public static final int BTN_HEIGHT_SCALED = BTN_HEIGHT * SCALE;

    public static final int MODAL_BG_WIDTH = (int) (Constants.SCREEN_WIDTH * 0.5f);
    public static final int MODAL_BG_HEIGHT = (int) (Constants.SCREEN_HEIGHT * 0.7f);
    public static final int MODAL_BG_X = (Constants.SCREEN_WIDTH - MODAL_BG_WIDTH) / 2;
    public static final int MODAL_BG_Y = (Constants.SCREEN_HEIGHT - MODAL_BG_HEIGHT) / 2;

    public static final float BASIC_ZOMBIE_SPEED = 0.5f;

    public static final int WEAPON_SCALE = 3;

    public static final int STARTING_AMMO = 1;
    public static final float BULLET_SPEED = 5f;
}
