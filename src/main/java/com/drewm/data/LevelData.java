package com.drewm.data;

import java.util.List;

public record LevelData(
        List<EntityData> players,
        List<RoomData> rooms
) {
}
