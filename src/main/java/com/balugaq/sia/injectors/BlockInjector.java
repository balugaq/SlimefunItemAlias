package com.balugaq.sia.injectors;

import com.balugaq.sia.SlimefunItemAlias;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;

import java.util.ArrayList;
import java.util.List;

public class BlockInjector {
    public static void injectAll(String pluginName) {
        for (var group : SlimefunItemAlias.getInstance().getConfigManager().getInjects()) {
            for (var item : group) {
                Utils.tagInjectedBlock(item);
            }

            var common = Utils.getExactItem(group);
            if (common != null) {
                inject(common, group, pluginName);
            }
        }
    }

    public static void inject(SlimefunItem common, List<String> ids, String pluginName) {
        var c = new ArrayList<>(ids);
        var items = Slimefun.getRegistry().getAllSlimefunItems();
        for (var id : items.stream().map(SlimefunItem::getId).toList()) {
            c.remove(id);
        }
        for (var id : c) {
            Slimefun.getRegistry().getSlimefunItemIds().put(id, common);
        }

        Rollback.addRollback(pluginName, () -> {
            for (var id : c) {
                Slimefun.getRegistry().getSlimefunItemIds().remove(id);
            }
        });
    }
}
