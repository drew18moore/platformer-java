package com.drewm.data;

public record DoorData(
        int x,
        int y,
        int currentRoom,
        int destinationRoom,
        float destinationX,
        float destinationY,
        LockType lockType
) {}

