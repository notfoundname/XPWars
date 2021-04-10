package io.github.notfoundname.xpwars;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import io.github.notfoundname.xpwars.utils.XPWarsUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.screamingsandals.bedwars.Main;

/*
 * not mine. thanks misat11 & ceph
 */
public class Configurator {

    public File file, kitOwnersFile;
    public FileConfiguration config;

    public final File dataFolder;
    public final XPWars main;

    public Configurator(XPWars main) {
        this.dataFolder = main.getDataFolder();
        this.main = main;
    }

    public void loadDefaults() {
        dataFolder.mkdirs();

        file = new File(dataFolder, "config.yml");
        kitOwnersFile = new File(dataFolder, "kitOwners.yml");

        config = new YamlConfiguration();

        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!kitOwnersFile.exists()) try {
            kitOwnersFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            backupConfig(null);
            XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&eYour configuration file is broken. It was backed up as config-backup.yml");
            e.printStackTrace();
            return;
        }

        if (config.contains("version")) {
            backupConfig("config_legacy.yml");
            XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&aYour old config.yml was backed up as config_legacy.yml");
            return;
        }

        AtomicBoolean modify = new AtomicBoolean(false);

        ConfigurationSection resources = Main.getConfigurator().config.getConfigurationSection("resources");

        checkOrSetConfig(modify, "check-for-updates", true);

        checkOrSetConfig(modify, "features.level-system", false);
        checkOrSetConfig(modify, "features.games-gui", false);
        checkOrSetConfig(modify, "features.action-bar-messages", false);
        checkOrSetConfig(modify, "features.permission-to-join-game", false);
        checkOrSetConfig(modify, "features.placeholders", false);
        checkOrSetConfig(modify, "features.specials", false);
        checkOrSetConfig(modify, "features.kits", false);
        checkOrSetConfig(modify, "features.hide-enemy-nametags", false);

        if (config.getBoolean("features.permission-to-join-game")) {
            checkOrSetConfig(modify, "permission-to-join-game.message",
                    "You don't have permission %perm% to join arena %arena%!");
            checkOrSetConfig(modify, "permission-to-join-game.arenas", Map.of(
                    "ArenaName1;ArenaName2;ArenaName3",
                    "xpwars.arenas", "ExampleName", "bedwars.games"));
        }

        if (config.getBoolean("features.action-bar-messages")) {
            checkOrSetConfig(modify, "action-bar-messages.in-lobby", "Your team: %team% [%pl_t%/%mxpl_t%]");
            checkOrSetConfig(modify, "action-bar-messages.in-game-alive", "Your team: %bed% %team%");
            checkOrSetConfig(modify, "action-bar-messages.in-game-spectator", "You are spectator!");
        }

        if (config.getBoolean("features.level-system")) {
            checkOrSetConfig(modify, "level.messages.maxreached", "&cYou can't have more than %max% levels!");
            checkOrSetConfig(modify, "level.percentage.give-from-killed-player", 33);
            checkOrSetConfig(modify, "level.percentage.keep-from-death", 33);
            checkOrSetConfig(modify, "level.maximum-xp", 1000);
            checkOrSetConfig(modify, "level.sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
            checkOrSetConfig(modify, "level.sound.volume", 1);
            checkOrSetConfig(modify, "level.sound.pitch", 1);

            checkOrSetConfig(modify, "level.per-arena-settings", Map.of(
                "ArenaNameCaseSensetive.enable", true,
                "ArenaNameCaseSensetive.percentage.give-from-killed-player", 100,
                "ArenaNameCaseSensetive.percentage.keep-from-death", 0,
                "ArenaNameCaseSensetive.maximum-xp", 0,
                "ArenaNameCaseSensetive.messages.maxreached", "&loof",
                "ArenaNameCaseSensetive.sound.sound", "none",
                "ArenaNameCaseSensetive.sound.volume", 1,
                "ArenaNameCaseSensetive.sound.pitch", 2,
                "ArenaNameCaseSensetive.spawners.iron", 6));

            resources.getKeys(false).forEach(key -> checkOrSetConfig(modify, "level.spawners." + key, 3));
        }

        if (config.getBoolean("features.games-gui")) {

            checkOrSetConfig(modify, "games-gui.title", "&rGames [&e%free%&7/&6%total%&r]");

            checkOrSetConfig(modify, "games-gui.inventory-settings.rows", 4);
            checkOrSetConfig(modify, "games-gui.inventory-settings.render-actual-rows", 6);
            checkOrSetConfig(modify, "games-gui.inventory-settings.render-offset", 9);
            checkOrSetConfig(modify, "games-gui.inventory-settings.render-header-start", 0);
            checkOrSetConfig(modify, "games-gui.inventory-settings.render-footer-start", 45);
            checkOrSetConfig(modify, "games-gui.inventory-settings.items-on-row", 9);
            checkOrSetConfig(modify, "games-gui.inventory-settings.show-page-numbers", true);
            checkOrSetConfig(modify, "games-gui.inventory-settings.inventory-type", "CHEST");

            checkOrSetConfig(modify, "games-gui.enable-categories", false);
            checkOrSetConfig(modify, "games-gui.categories", Map.of(
                "example.stack", (XPWarsUtils.isNewVersion() ? "BLACK_CONCRETE" : "CONCRETE") + ";1;&rDuos",
                "example.skip", 3,
                "example.arenas", Arrays.asList("Arena9", "привет")));

            checkOrSetConfig(modify, "games-gui.itemstack.WAITING",
                    (XPWarsUtils.isNewVersion() ? "GREEN_WOOL" : "WOOL")
                    + ";1;&a%arena% &f[%players%/%maxplayers%];Waiting %time_left%");
            checkOrSetConfig(modify, "games-gui.itemstack.RUNNING",
                    (XPWarsUtils.isNewVersion() ? "RED_WOOL" : "WOOL")
                    + ";1;&c%arena% &f[%players%/%maxplayers%];Running :) Time left: %time_left%");
            checkOrSetConfig(modify, "games-gui.itemstack.GAME_END_CELEBRATING",
                    (XPWarsUtils.isNewVersion() ? "BLUE_WOOL" : "WOOL")
                            + ";1;&9%arena% &f[%players%/%maxplayers%];Game ended");
            checkOrSetConfig(modify, "games-gui.itemstack.REBUILDING",
                    (XPWarsUtils.isNewVersion() ? "GRAY_WOOL" : "WOOL")
                            + ";1;&7%arena% &f[%players%/%maxplayers%];rebuilding");
            checkOrSetConfig(modify, "games-gui.itemstack.DISABLED",
                    (XPWarsUtils.isNewVersion() ? "BLACK_WOOL" : "WOOL")
                            + ";1;&0%arena% &f[%players%/%maxplayers%];disabled");
        }

        if (config.getBoolean("features.specials")) {
            checkOrSetConfig(modify, "specials.remote-tnt.fuse-ticks", 100);
            checkOrSetConfig(modify, "specials.remote-tnt.detonator-itemstack", "TRIPWIRE_HOOK;1;&eDetonator");

            checkOrSetConfig(modify, "specials.trampoline.y-velocity", 5.0);

            checkOrSetConfig(modify, "specials.vouncher.levels", 50);

            checkOrSetConfig(modify, "specials.throwable-tnt.velocity", 5.0);
            checkOrSetConfig(modify, "specials.throwable-tnt.fuse-ticks", 5);

            checkOrSetConfig(modify, "specials.rideable-projectile.allow-leave", true);
            checkOrSetConfig(modify, "specials.rideable-projectile.remove-on-leave", true);

            checkOrSetConfig(modify, "specials.portable-shop.shop-file", "shop.yml");
            checkOrSetConfig(modify, "specials.portable-shop.use-parent", true);
            checkOrSetConfig(modify, "specials.portable-shop.entity-type", "VILLAGER");
            checkOrSetConfig(modify, "specials.portable-shop.enable-custom-name", false);
            checkOrSetConfig(modify, "specials.portable-shop.custom-name", "Portable Villager");
            checkOrSetConfig(modify, "specials.portable-shop.duration", 15);
            checkOrSetConfig(modify, "specials.portable-shop.baby", false);
            checkOrSetConfig(modify, "specials.portable-shop.use-citizens", false);
        }

        if (config.getBoolean("features.placeholders")
                && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            checkOrSetConfig(modify, "placeholders.waiting", "&aWaiting...");
            checkOrSetConfig(modify, "placeholders.starting", "&eArena is starting in %time%!");
            checkOrSetConfig(modify, "placeholders.running", "&cRunning! Time left: %time%");
            checkOrSetConfig(modify, "placeholders.end-celebration", "&9Game ended!");
            checkOrSetConfig(modify, "placeholders.rebuilding", "&7Rebuilding...");
        }

        if (config.getBoolean("features.kits")) {
            checkOrSetConfig(modify, "kits.messages.not-enough-score", "&cNot enough score to use this kit!");
            checkOrSetConfig(modify, "kits.messages.not-enough-vault", "&cNot enough money to buy this kit!");
            checkOrSetConfig(modify, "kits.messages.selected", "&aSelected kit: &e%item-name%(%raw-name%)");

            checkOrSetConfig(modify, "kits.settings.title", "&4BW &rKits");
            checkOrSetConfig(modify, "kits.settings.rows", 4);
            checkOrSetConfig(modify, "kits.settings.render-actual-rows", 6);
            checkOrSetConfig(modify, "kits.settings.render-offset", 9);
            checkOrSetConfig(modify, "kits.settings.render-header-start", 0);
            checkOrSetConfig(modify, "kits.settings.render-footer-start", 45);
            checkOrSetConfig(modify, "kits.settings.items-on-row", 9);
            checkOrSetConfig(modify, "kits.settings.show-page-numbers", true);
            checkOrSetConfig(modify, "kits.settings.inventory-type", "CHEST");

            checkOrSetConfig(modify,"kits.list", Arrays.asList(
                Map.of(
                    "name", "example1",
                    "display-icon", "IRON_SWORD;1;Example 1;It contains iron tools!",
                    "price", "100:score",
                    "give-on-respawn", false,
                    "items", Arrays.asList("IRON_SWORD", "IRON_PICKAXE", "IRON_AXE")),
                Map.of(
                    "name", "example2",
                    "display-icon", "APPLE;3;Example 2;Everyone like apples!;...;             right?",
                    "price", "50:vault",
                    "give-on-respawn", true,
                    "items", Arrays.asList("APPLE;64;Apples!", "CARROT;1;Not an apple."))));
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

    private void backupConfig(String fileName) {
        fileName = fileName == null ? "config_backup.yml" : fileName;
        file.renameTo(new File(dataFolder, fileName));
        loadDefaults();
    }

    private void checkOrSetConfig(AtomicBoolean modify, String path, Object value) {
        checkOrSet(modify, this.config, path, value);
    }

    private static void checkOrSet(AtomicBoolean modify, FileConfiguration config, String path, Object value) {
        if (!config.isSet(path)) {
            if (value instanceof Map) {
                config.createSection(path, (Map<?, ?>) value);
            } else {
                config.set(path, value);
            }
            modify.set(true);
        }
    }

}
