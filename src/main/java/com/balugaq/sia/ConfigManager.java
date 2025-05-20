package com.balugaq.sia;

import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

@Getter
public class ConfigManager {
    public static final @NotNull String CONFIG_PATH = "config.yml";
    public final @NotNull JavaPlugin plugin;
    public @NotNull List<List<String>> injects = new ArrayList<>();

    public ConfigManager(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        setupDefaultConfig();
    }

    public void setupDefaultConfig() {
        // config.yml
        final InputStream inputStream = plugin.getResource(CONFIG_PATH);
        final File existingFile = new File(plugin.getDataFolder(), CONFIG_PATH);

        if (inputStream == null) {
            return;
        }

        final Reader reader = new InputStreamReader(inputStream);
        final FileConfiguration resourceConfig = YamlConfiguration.loadConfiguration(reader);

        if (!existingFile.exists()) {
            try {
                plugin.saveResource(CONFIG_PATH, false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().log(Level.SEVERE, "The default config file {0} does not exist in jar file!", "config.yml");
                return;
            }
        } else {
            final FileConfiguration existingConfig = YamlConfiguration.loadConfiguration(existingFile);

            for (String key : resourceConfig.getKeys(false)) {
                checkKey(existingConfig, resourceConfig, key);
            }

            try {
                existingConfig.save(existingFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (var s : plugin.getConfig().getStringList("injects")) {
            injects.add(Arrays.stream(s.split(" ")).toList());
        }
    }

    @ParametersAreNonnullByDefault
    public void checkKey(FileConfiguration existingConfig, FileConfiguration resourceConfig, String key) {
        final Object currentValue = existingConfig.get(key);
        final Object newValue = resourceConfig.get(key);
        if (newValue instanceof ConfigurationSection section) {
            for (String sectionKey : section.getKeys(false)) {
                checkKey(existingConfig, resourceConfig, key + "." + sectionKey);
            }
        } else if (currentValue == null) {
            existingConfig.set(key, newValue);
        }
    }
}
