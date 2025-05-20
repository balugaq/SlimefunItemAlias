package com.balugaq.sia.injectors;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class PluginInjector implements Listener {
    public static void injectAll() {
        for (var plugin : Bukkit.getPluginManager().getPlugins()) {
            if (plugin instanceof SlimefunAddon sa) {
                ItemInjector.injectAll();
                BlockInjector.injectAll(sa.getName());
            }
        }
    }

    @EventHandler
    public void onDisablePlugin(PluginDisableEvent event) {
        if (event.getPlugin() instanceof SlimefunAddon sa) {
            Rollback.rollback(sa.getName());
        }
    }
}
