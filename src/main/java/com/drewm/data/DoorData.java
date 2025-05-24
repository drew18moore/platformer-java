package com.drewm.data;

public record DoorData(
        int x,
        int y,
        int destinationRoom,
        float destinationX,
        float destinationY,
        LockType lockType,
        boolean isGoal
) {}

