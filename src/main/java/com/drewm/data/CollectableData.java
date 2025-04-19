package com.drewm.data;

import com.drewm.objects.ItemType;

public record CollectableData(
        float x,
        float y,
        ItemType itemType
) {
}
