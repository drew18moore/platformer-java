package com.drewm.data;

public record LaserData(
        float x1,
        float x2,
        float y1,
        float y2,
        long activationIntervalMS,
        long delayMS
) {
}
