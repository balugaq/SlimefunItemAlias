package com.balugaq.sia.injectors;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Utils {
    public static final Map<String, SlimefunItem> fixedItems = new HashMap<>();
    public static void addFixedItem(String id, SlimefunItem item) {
        fixedItems.put(id, item);
    }

    public static SlimefunItem getFixedItem(String id) {
        return fixedItems.get(id);
    }

    @Nullable
    public static SlimefunItem getExactItem(List<String> ids) {
        for (var id : ids) {
            var item = SlimefunItem.getById(id);
            if (item != null) {
                return item;
            }
        }

        return null;
    }
}
