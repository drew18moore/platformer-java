package com.drewm.data;

import java.util.List;

public record Room(
        List<int[]> tiles,
        List<int[]> background,
        List<EntityData> players,
        List<EntityData> enemies,
        List<CoinData> coins,
        List<DoorData> doors
) {
}
