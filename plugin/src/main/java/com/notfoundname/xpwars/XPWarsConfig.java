package com.notfoundname.xpwars;

import net.elytrium.java.commons.config.YamlConfig;
import org.screamingsandals.bedwars.Main;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class XPWarsConfig extends YamlConfig {

    @Ignore
    public static final XPWarsConfig IMP = new XPWarsConfig();

    @Comment("Do not change.")
    @Final
    public String VERSION = XPWars.getInstance().getDescription().getVersion();

    @Create
    public Kits KITS;
    @Comment("Enables feature to have selectable kits")
    public static class Kits {
        public boolean ENABLED = false;
    }

    @Create
    public BetterSidebar BETTER_SIDEBAR;
    @Comment("Overrides default simple sidebars with more flexible ones")
    public static class BetterSidebar {
        public boolean ENABLED = false;
    }

    @Create
    public TeamSettings TEAM_SETTINGS;
    @Comment("Manages default team settings")
    public static class TeamSettings {
        public boolean HIDE_ENEMY_NAMETAGS = false;
    }

    @Create
    public MoreSpecialItems MORE_SPECIAL_ITEMS;
    @Comment("Manages new special items")
    public static class MoreSpecialItems {
        public boolean ENABLED = false;
    }

    @Create
    public GameSelector GAME_SELECTOR;
    @Comment("Manages GUI where you can select a game")
    public static class GameSelector {
        public boolean ENABLED = false;
    }

    @Create
    public LevelSystem LEVEL_SYSTEM;
    @Comment("Manages level system. Picked up resources will give levels (points), which will be used as currency.")
    public static class LevelSystem {
        public boolean ENABLED = false;
        public static class Default {
            @Comment("Set to 0 to disable.")
            public int MAX_LEVEL = 1000;
            @Comment("This message will display in action bar. Set to null to not show it.")
            public String MAX_LEVEL_MESSAGE = "You have reached the limit!";
            @Comment("From 0 to 100")
            public int GIVE_TO_KILLER_PERCENT = 66;
            public int KEEP_TO_VICTIM = 33;
            @Comment("Set this to null to disable.")
            public String SOUND = "ENTITY_EXPERIENCE_ORB_PICKUP";
            public float SOUND_VOLUME = 1.0f;
            public float SOUND_PITCH = 1.0f;
            @Comment("How much levels resource will give. Cannot be lower or equal 0.")
            public HashMap<String, Integer> SPAWNERS = new HashMap<String, Integer>() {{
                // By default, gets all resources and sets to 3
                Objects.requireNonNull(Main.getConfigurator().config.getConfigurationSection("resources"))
                        .getKeys(false).forEach(resource -> put(resource, 3));
            }};
        }
        @Comment("Means these arenas will not be handled and others will be")
        public List<String> BLACKLISTED_ARENAS = Arrays.asList("Arena1", "Arena2");
        @Comment("Reverses function above, so these games will be handles and others not")
        public boolean AS_WHITELIST = false;
    }

    @Create
    public Database DATABASE;
    @Comment("Enables database used for bought kits")
    public static class Database {
        @Comment("Possible values: h2 (file-based), mysql (normal database)")
        public String STORAGE_TYPE = "h2";
        @Comment("Settings for mysql")
        public String HOSTNAME = "192.168.0.1:3306";
        public String USER = "user";
        public String PASSWORD = "password";
        public boolean USE_SSL = true;
    }

}
