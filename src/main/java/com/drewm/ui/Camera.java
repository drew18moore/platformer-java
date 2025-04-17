package com.drewm.ui;

import com.drewm.entities.Player;
import com.drewm.gamestates.Playing;
import com.drewm.utils.Constants;

public class Camera {
    private final Playing playing;
    private float cameraX;
    private float cameraY;

    private final int DEADZONE_WIDTH = Constants.SCREEN_WIDTH / 3;
    private final int DEADZONE_HEIGHT = Constants.SCREEN_HEIGHT / 3;

    private final int DEADZONE_LEFT = (Constants.SCREEN_WIDTH - DEADZONE_WIDTH) / 2;
    private final int DEADZONE_RIGHT = DEADZONE_LEFT + DEADZONE_WIDTH;
    private final int DEADZONE_TOP = (Constants.SCREEN_HEIGHT - DEADZONE_HEIGHT) / 2;
    private final int DEADZONE_BOTTOM = DEADZONE_TOP + DEADZONE_HEIGHT;

    public Camera(Playing playing) {
        this.playing = playing;
    }

    public void update(Player player) {
        float hitboxWorldX = player.worldX + player.hitboxOffsetX;
        float hitboxWorldY = player.worldY + player.hitboxOffsetY;

        float hitboxBottomY = hitboxWorldY + player.hitboxHeight;
        float hitboxRightX = hitboxWorldX + player.hitboxWidth;

        float playerLeftScreenX = hitboxWorldX - cameraX;
        float playerTopScreenY = hitboxWorldY - cameraY;
        float playerBottomScreenY = hitboxBottomY - cameraY;
        float playerRightScreenX = hitboxRightX - cameraX;

        if (playerLeftScreenX < DEADZONE_LEFT) {
            cameraX = hitboxWorldX - DEADZONE_LEFT;
        } else if (playerRightScreenX > DEADZONE_RIGHT) {
            cameraX = hitboxRightX - DEADZONE_RIGHT;
        }

        if (playerTopScreenY < DEADZONE_TOP) {
            cameraY = hitboxWorldY - DEADZONE_TOP;
        } else if (playerBottomScreenY > DEADZONE_BOTTOM) {
            cameraY = hitboxBottomY - DEADZONE_BOTTOM;
        }

        int maxCameraX = playing.roomNumTileWidth * Constants.TILE_SIZE - Constants.SCREEN_WIDTH;
        int maxCameraY = playing.roomNumTileHeight * Constants.TILE_SIZE - Constants.SCREEN_HEIGHT;

        cameraX = Math.max(0, Math.min(cameraX, maxCameraX));
        cameraY = Math.max(0, Math.min(cameraY, maxCameraY));
    }

    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }
}
