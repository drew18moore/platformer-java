package com.drewm.data;

import java.util.List;

public record RoomData(
        List<int[]> tiles,
        List<int[]> background,
        List<EntityData> enemies,
        List<CollectableData> collectables,
        List<DoorData> doors,
        List<FloatingMineData> floatingMines,
        List<SawBladeData> sawBlades
) {
}
