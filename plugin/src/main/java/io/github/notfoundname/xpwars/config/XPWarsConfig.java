package io.github.notfoundname.xpwars.config;

import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.api.XPWarsOption;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class XPWarsConfig {

    public File file;
    public FileConfiguration config;

    public final File dataFolder;
    public final XPWars main;

    public XPWarsConfig(XPWars main) {
        this.dataFolder = main.getDataFolder();
        this.main = main;
    }

    public void generate() {
        dataFolder.mkdirs();

        file = new File(dataFolder, "config.yml");

        config = new YamlConfiguration();

        if (!file.exists()) try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        AtomicBoolean modify = new AtomicBoolean(false);

        // ConfigurationSection resources = Main.getConfigurator().config.getConfigurationSection("resources");

        for (XPWarsOption option : XPWarsOption.values()) {
            checkOrSet(modify, config, option);
        }

        saveConfig();
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkOrSet(AtomicBoolean modify, FileConfiguration config, XPWarsOption option) {
        if (!config.isSet(option.path)) {
            if (option.defaultValue instanceof Map) {
                config.createSection(option.path, (Map<?, ?>) option.defaultValue);
            } else {
                config.set(option.path, option.defaultValue);
            }
            modify.set(true);
        } else {

        }
    }
}
