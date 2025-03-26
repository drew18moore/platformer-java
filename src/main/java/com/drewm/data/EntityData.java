package com.drewm.data;

public record EntityData(
        String type,
        float x,
        float y,
        int health,
        boolean facingLeft
) {
}

