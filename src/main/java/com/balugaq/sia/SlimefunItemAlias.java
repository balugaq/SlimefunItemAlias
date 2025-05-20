package com.balugaq.sia;

import com.balugaq.sia.injectors.PluginInjector;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class SlimefunItemAlias extends JavaPlugin implements SlimefunAddon {
    @Getter
    public static SlimefunItemAlias instance;
    @Getter
    public ConfigManager configManager;

    @Override
    public void onLoad() {
        instance = this;
        getLogger().info("Enabling SlimefunItemAlias...");

        getLogger().info("Loading configuration...");
        configManager = new ConfigManager(this);
        Bukkit.getPluginManager().registerEvents(new PluginInjector(), this);
        // Inject all addons when server is completely loaded
        Bukkit.getScheduler().runTaskLater(this, PluginInjector::injectAll, 1L);
    }

    @NotNull
    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Nullable
    @Override
    public String getBugTrackerURL() {
        return "https://github.com/balugaq/SlimefunItemAlias/issues";
    }
}
