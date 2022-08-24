package com.notfoundname.xpwars;

import com.google.common.collect.ImmutableList;
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
    @Comment("Manages feature to have selectable kits")
    public static class Kits {
        public boolean ENABLED = false;
        @Create
        public Database DATABASE;
        public static class Database {
            @Comment("Possible values: h2 (file-based), mysql (normal database)")
            public String STORAGE_TYPE = "h2";
            @Comment("Settings for mysql")
            public String HOSTNAME = "192.168.0.1:3306";
            public String USER = "user";
            public String PASSWORD = "password";
            public boolean USE_SSL = true;
        }
        @Comment("If false, they will be sent in chat, otherwise in action bar. Set to null to not show it.")
        public boolean SEND_MESSAGES_IN_ACTION_BAR = false;
        public String NOT_ENOUGH_SCORE = "<red>You do not have enough score to use this kit!";
        @Comment({
                "Kits itself. Price can be:",
                "- score (enough earned score will unlock the kit);",
                "- vault (withdrawing money will unlock the kit).",
                "You can use short stack or long stack for items and displayed item, like in BedWars shop.",
                "More information here: https://github.com/ScreamingSandals/SimpleInventories/wiki/Variable:-stack",
                "Example kit:",
                "- kit-name: \"example\"",
                "  display-stack: \"STONE;1;Example Kit\"",
                "  price: \"1 of score\"",
                "  items:",
                "  - STONE;1;Stone;Example",
                "  - stack: \"IRON_BLOCK;4;Iron blocks;Yeah\"",
                "  - stack:",
                "      type: SLIME_BALL",
                "      display-name: \"Slime bowl\""
        })
        public List<Object> KITS = Arrays.asList(
                new HashMap<String, Object>() {{
                    put("kit-name", "builder");
                    put("display-stack", "SANDSTONE;1;Builder Kit;Get some blocks;For quick start");
                    put("price", "123 of score");
                    put("items", Arrays.asList(
                            "SANDSTONE;32",
                            "END_STONE;8"));
                }},
                new HashMap<String, Object>() {{
                    put("kit-name", "food");
                    put("display-stack", "SANDSTONE;1;Food;Be fit");
                    put("price", "456 of vault");
                    put("items", Arrays.asList(
                            "APPLE;4",
                            "GOLDEN_CARROT;1"
                    ));
                }}
        );
    }

    @Create
    public BetterSidebar BETTER_SIDEBAR;
    @Comment("Overrides default simple sidebars with more flexible ones")
    public static class BetterSidebar {
        public boolean ENABLED = false;
        public String LOBBY_SIDEBAR_TITLE = "BedWars";
        public List<String> t = ImmutableList.of("");
        public List<String> LOBBY_SIDEBAR = Arrays.asList(
                " ",
                "Players:",
                "<players>/<maxplayers>",
                " ");
        public boolean ENABLE_IN_GAME_SIDEBAR = false;
        public List<String> GAME_SIDEBAR = Arrays.asList(
                " ",
                "<teams>",
                " ",
                "<yellow>notfoundname.com");
        @Comment("Placeholders: <letter>, <team>, <teamcolored>, <status>, <members> <kills>, <deaths>, <beds>")
        public String GAME_SIDEBAR_TEAMS_FORMAT = "<letter> <teamcolored> <status>";
        public String GAME_SIDEBAR_BED_STATUS_ALIVE = "<green>V";
        public String GAME_SIDEBAR_BED_STATUS_BROKEN = "<yellow><members>";
        public String GAME_SIDEBAR_BED_STATUS_DIED = "<red>X";
    }

    @Create
    public TeamSettings TEAM_SETTINGS;
    @Comment("Manages default team settings")
    public static class TeamSettings {
        public boolean ENABLED = false;
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
        @Comment("Means these arenas will not be handled and others will be")
        public List<String> BLACKLISTED_ARENAS = Arrays.asList("Arena1", "Arena2");
        @Comment("Reverses function above, so these games will be handles and others not")
        public boolean AS_WHITELIST = false;
        @Create
        public Default DEFAULT;
        public static class Default {
            @Comment("Set to 0 to disable.")
            public int MAX_LEVEL = 1000;
            @Comment("This message will display in action bar. Set to null to not show it.")
            public String MAX_LEVEL_MESSAGE = "<red>You have reached the limit!";
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
    }
}
