package com.balugaq.sia.injectors;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
    public static Set<String> injectedItem = new HashSet<>();
    public static Set<String> injectedBlock = new HashSet<>();

    public static void tagInjectedItem(String id) {
        injectedItem.add(id);
    }

    public static boolean taggedInjectedItem(String id) {
        return injectedItem.contains(id);
    }

    public static void tagInjectedBlock(String id) {
        injectedBlock.add(id);
    }

    public static boolean taggedInjectedBlock(String id) {
        return injectedBlock.contains(id);
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
