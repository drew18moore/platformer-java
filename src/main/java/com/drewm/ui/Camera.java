package com.drewm.ui;

import com.drewm.entities.Player;
import com.drewm.utils.Constants;

public class Camera {
    private float cameraX;
    private float cameraY;

    private final int DEADZONE_WIDTH = Constants.SCREEN_WIDTH / 3;
    private final int DEADZONE_HEIGHT = Constants.SCREEN_HEIGHT / 3;

    private final int DEADZONE_LEFT = (Constants.SCREEN_WIDTH - DEADZONE_WIDTH) / 2;
    private final int DEADZONE_RIGHT = DEADZONE_LEFT + DEADZONE_WIDTH;
    private final int DEADZONE_TOP = (Constants.SCREEN_HEIGHT - DEADZONE_HEIGHT) / 2;
    private final int DEADZONE_BOTTOM = DEADZONE_TOP + DEADZONE_HEIGHT;

    public void update(Player player) {
        float playerScreenX = player.worldX - cameraX;
        float playerScreenY = player.worldY - cameraY;

        if (playerScreenX < DEADZONE_LEFT)
            cameraX = player.worldX - DEADZONE_LEFT;
        else if (playerScreenX > DEADZONE_RIGHT)
            cameraX = player.worldX - DEADZONE_RIGHT;

        if (playerScreenY < DEADZONE_TOP)
            cameraY = player.worldY - DEADZONE_TOP;
        else if (playerScreenY > DEADZONE_BOTTOM)
            cameraY = player.worldY - DEADZONE_BOTTOM;
    }

    public float getCameraX() {
        return cameraX;
    }

    public float getCameraY() {
        return cameraY;
    }
}
