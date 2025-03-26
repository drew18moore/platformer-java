package com.drewm.data;

import java.util.List;

public record LevelData(
        List<int[]> tiles,
        List<EntityData> players,
        List<EntityData> enemies,
        List<CoinData> coins
) {
}
