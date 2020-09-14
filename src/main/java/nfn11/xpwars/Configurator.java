package nfn11.xpwars;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import nfn11.xpwars.commands.GamesCommand;
import nfn11.xpwars.commands.JoinSortedCommand;
import nfn11.xpwars.inventories.KitSelectionInventory;
import nfn11.xpwars.listener.ActionBarMessageListener;
import nfn11.xpwars.listener.LevelSystemListener;
import nfn11.xpwars.special.listener.RegisterSpecialListeners;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.screamingsandals.bedwars.Main;

import nfn11.xpwars.utils.XPWarsUtils;

/*
 * not mine. thanks misat11 & ceph
 */
public class Configurator {

    public File file;
    public FileConfiguration config;

    public final File dataFolder;
    public final XPWars main;

    public Configurator(XPWars main) {
        this.dataFolder = main.getDataFolder();
        this.main = main;
    }

    @SuppressWarnings("serial")
    public void loadDefaults() {
        dataFolder.mkdirs();

        file = new File(dataFolder, "config.yml");

        config = new YamlConfiguration();

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            file.renameTo(new File(dataFolder, "config_backup.yml"));
            XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(),
                    "[XPWars] &aYour XPWars configuration file was broken and plugin backed it up.");
            loadDefaults();
            return;
        }

        AtomicBoolean modify = new AtomicBoolean(false);

        ConfigurationSection resources = Main.getConfigurator().config.getConfigurationSection("resources");

        checkOrSetConfig(modify, "version", 4);

        if (config.getInt("version") != 4) {
            file.renameTo(new File(dataFolder, "config_backup.yml"));
            Bukkit.getServer().getLogger()
                    .info("[XPWars] Your XPWars configuration file was backed up. Please transfer values.");
            loadDefaults();
            return;
        }
        
        checkOrSetConfig(modify, "check-for-updates", true);
        
        checkOrSetConfig(modify, "features.level-system", false);
        checkOrSetConfig(modify, "features.games-gui", false);
        checkOrSetConfig(modify, "features.action-bar-messages", false);
        checkOrSetConfig(modify, "features.permission-to-join-game", false);
        checkOrSetConfig(modify, "features.placeholders", false);
        checkOrSetConfig(modify, "features.specials", false);
        checkOrSetConfig(modify, "features.kits", false);

        if (config.getBoolean("features.permission-to-join-game")) {
            checkOrSetConfig(modify, "permission-to-join-game.message",
                    "You don't have permission %perm% to join arena %arena%!");
            checkOrSetConfig(modify, "permission-to-join-game.arenas", new HashMap<String, Object>() {{
                put("[xpwars.example]", new ArrayList<String>() {{
                    add("ArenaName");
                    add("CaseSensetive");
                }});
                put("[bw.vip.game]", new ArrayList<String>() {{
                    add("Пихайте_чё_хотите_сюда");
                    add("abvaoiuobaoinoyaAvylbygbkYI");
                }});
            }});
        }

        if (config.getBoolean("features.action-bar-messages")) {
            new ActionBarMessageListener();

            checkOrSetConfig(modify, "action-bar-messages.in-lobby", "Your team: %team% [%pl_t%/%mxpl_t%]");
            checkOrSetConfig(modify, "action-bar-messages.in-game-alive", "Your team: %bed% %team%");
            checkOrSetConfig(modify, "action-bar-messages.in-game-spectator", "You are spectator!");
        }

        if (config.getBoolean("features.level-system")) {
            new LevelSystemListener();

            checkOrSetConfig(modify, "level.messages.maxreached", "&cYou can't have more than %max% levels!");
            checkOrSetConfig(modify, "level.percentage.give-from-killed-player", 33);
            checkOrSetConfig(modify, "level.percentage.keep-from-death", 33);
            checkOrSetConfig(modify, "level.maximum-xp", 1000);
            checkOrSetConfig(modify, "level.sound.sound", "ENTITY_EXPERIENCE_ORB_PICKUP");
            checkOrSetConfig(modify, "level.sound.volume", 1);
            checkOrSetConfig(modify, "level.sound.pitch", 1);

            checkOrSetConfig(modify, "level.per-arena-settings", new HashMap<String, HashMap<String, Object>>() {{
                put("ArenaNameCaseSensetive", new HashMap<String, Object>() {{
                    put("enable", true);
                    put("percentage.give-from-killed-player", 100);
                    put("percentage.keep-from-death", 0);
                    put("maximum-xp", 0);
                    put("messages.maxreached", "&loof");
                    put("sound.sound", "none");
                    put("sound.volume", 1);
                    put("sound.pitch", 2);
                    for (String key : resources.getKeys(false)) {
                        put("spawners." + key, 10);
                    }
                }});
            }});

            checkOrSetConfig(modify, "level.spawners", new HashMap<String, Object>() {{
                for (String key : resources.getKeys(false)) put(key, 3);
            }});
        }

        if (config.getBoolean("features.games-gui")) {
            new GamesCommand();
            new JoinSortedCommand();

            checkOrSetConfig(modify, "games-gui.permission", "xpwars.gamesgui");

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
            checkOrSetConfig(modify, "games-gui.categories", new HashMap<String, HashMap<String, Object>>() {{
                put("example", new HashMap<String, Object>() {{
                    put("stack", (XPWarsUtils.isNewVersion() ? "BLACK_CONCRETE" : "CONCRETE") + ";1;&rDuos");
                    put("skip", 3);
                    put("arenas", new ArrayList<String>() {{
                        add("ArenaName");
                        add("привет");
                    }});
                }});
            }});

            checkOrSetConfig(modify, "games-gui.itemstack.WAITING", (XPWarsUtils.isNewVersion() ? "GREEN_WOOL" : "WOOL")
                    + ";1;&a%arena% &f[%players%/%maxplayers%];Waiting %time_left%");
            checkOrSetConfig(modify, "games-gui.itemstack.RUNNING", (XPWarsUtils.isNewVersion() ? "RED_WOOL" : "WOOL")
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
            new RegisterSpecialListeners();
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
        }

        if (config.getBoolean("features.placeholders")) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                try {
                    new nfn11.xpwars.placeholderapi.PlaceholderAPIHook().register();

                    checkOrSetConfig(modify, "placeholders.waiting", "&aWaiting...");
                    checkOrSetConfig(modify, "placeholders.starting", "&eArena is starting in %time%!");
                    checkOrSetConfig(modify, "placeholders.running", "&cRunning! Time left: %time%");
                    checkOrSetConfig(modify, "placeholders.end-celebration", "&9Game ended!");
                    checkOrSetConfig(modify, "placeholders.rebuilding", "&7Rebuilding...");
                } catch (Throwable ignored) { }
            }
        }

        if (config.getBoolean("features.kits")) {
            checkOrSetConfig(modify, "kits.settings.rows", 4);
            checkOrSetConfig(modify, "kits.settings.render-actual-rows", 6);
            checkOrSetConfig(modify, "kits.settings.render-offset", 9);
            checkOrSetConfig(modify, "kits.settings.render-header-start", 0);
            checkOrSetConfig(modify, "kits.settings.render-footer-start", 45);
            checkOrSetConfig(modify, "kits.settings.items-on-row", 9);
            checkOrSetConfig(modify, "kits.settings.show-page-numbers", true);
            checkOrSetConfig(modify, "kits.settings.inventory-type", "CHEST");

            checkOrSetConfig(modify,"kits.list", new ArrayList<Object>() {{
                add(new HashMap<String, Object>() {{
                    put("code-name", "example1");
                    put("display-icon", "IRON_SWORD;1;Example 1;It contains iron tools!");
                    put("items", new ArrayList<String>() {{
                        add("IRON_SWORD");
                        add("IRON_PICKAXE");
                        add("IRON_AXE");
                        add("IRON_SHOVEL");
                        add("IRON_HOE");
                    }});
                    put("price", "1000");
                    put("price-type", "score");
                }});
                add(new HashMap<String, Object>() {{
                    put("name", "example2");
                    put("display-icon", "APPLE;3;Example 2;Everyone likes apples!; ;...right?");
                    put("items", new ArrayList<String>() {{
                        add("APPLE;64;Apples!");
                        add("CARROT;1;Not an apple.");
                    }});
                    put("price", "300");
                    put("price-type", "vault");
                }});
            }});
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

    public String getString(String string, String defaultString) {
        return ChatColor.translateAlternateColorCodes('&',
                XPWars.getConfigurator().config.getString(string, defaultString));
    }

    public List<String> getStringList(String string) {
        List<String> list = new ArrayList<>();
        for (String s : XPWars.getConfigurator().config.getStringList(string)) {
            s = ChatColor.translateAlternateColorCodes('&', s);
            list.add(s);
        }
        return list;
    }

    public boolean getBoolean(String string, boolean defaultBoolean) {
        return XPWars.getConfigurator().config.getBoolean(string, defaultBoolean);
    }

    public int getInt(String string, int defaultInt) {
        return XPWars.getConfigurator().config.getInt(string, defaultInt);
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
