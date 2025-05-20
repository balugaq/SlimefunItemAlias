package com.balugaq.sia.injectors;

import com.balugaq.sia.SlimefunItemAlias;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import java.util.ArrayList;
import java.util.List;

public class BlockInjector {
    public static void injectAll(String pluginName) {
        for (var group : SlimefunItemAlias.getInstance().getConfigManager().getInjects()) {
            inject(group, pluginName);
        }
    }

    public static void inject(List<String> ids, String pluginName) {
        var c = new ArrayList<>(ids);
        for (var id : Slimefun.getRegistry().getSlimefunItemIds().keySet()) {
            c.remove(id);
        }

        for (var id : c) {
            Slimefun.getRegistry().getSlimefunItemIds().put(id, Utils.getFixedItem(id));
        }

        Rollback.addRollback(pluginName, () -> {
            for (var id : c) {
                Slimefun.getRegistry().getSlimefunItemIds().remove(id);
            }
        });
    }
}
