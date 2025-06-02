package com.drewm.data;

public record MovingPlatformData(
        float x,
        float y,
        float width,
        float height,
        float speed,
        float min,
        float max,
        boolean movingHorizontal
) {
}
