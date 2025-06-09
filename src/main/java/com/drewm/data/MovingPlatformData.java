package com.drewm.data;

public record MovingPlatformData(
        float startX,
        float startY,
        float endX,
        float endY,
        float width,
        float height,
        float speed
) {
}
