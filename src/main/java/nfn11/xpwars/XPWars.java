package nfn11.xpwars;

import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;
import nfn11.xpwars.inventories.DebugInventory;
import nfn11.xpwars.inventories.KitSelectionInventory;
import nfn11.xpwars.listener.ActionBarMessageListener;
import nfn11.xpwars.placeholderapi.PlaceholderAPIHook;
import nfn11.xpwars.special.listener.RegisterSpecialListeners;
import nfn11.xpwars.utils.KitManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.bedwars.inventories.ShopInventory;
import nfn11.xpwars.commands.GamesCommand;
import nfn11.xpwars.commands.JoinSortedCommand;
import nfn11.xpwars.commands.XPWarsCommand;
import nfn11.xpwars.inventories.GamesInventory;
import nfn11.xpwars.inventories.LevelShopInventory;
import nfn11.xpwars.listener.LevelSystemListener;
import nfn11.xpwars.utils.XPWarsUtils;
import org.screamingsandals.bedwars.lib.sgui.listeners.InventoryListener;

public class XPWars extends JavaPlugin implements Listener {

    private static XPWars instance;
    private Configurator configurator;
    private HashMap<String, BaseCommand> commands = new HashMap<>();
    private GamesInventory gamesInventory;
    private LevelShopInventory levelShopInventory;
    private ShopInventory shopInventory;
    private KitSelectionInventory kitSelectionInventory;
    private DebugInventory debugInventory;
    private static Economy econ = null;

    @Override
    public void onEnable() {
        instance = this;
        new XPWarsUtils();
        new XPWarsCommand();
        debugInventory = new DebugInventory();

        if (Main.getInstance() == null)
            Bukkit.getServer().getPluginManager().disablePlugin(this);

        configurator = new Configurator(this);
        configurator.loadDefaults();

        InventoryListener.init(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
                    && XPWars.getConfigurator().config.getBoolean("features.placeholders"))
                new PlaceholderAPIHook(this).register();
        } catch (Exception e) {
            XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(),
                    "&cUnable to register PlaceholderAPI Expansion! Make sure you have PAPI correctly installed!");
        }

        if (getConfigurator().config.getBoolean("features.action-bar-messages"))
            new ActionBarMessageListener();

        if (getConfigurator().config.getBoolean("features.level-system")) {
            new LevelSystemListener();
            levelShopInventory = new LevelShopInventory();
        }

        if (getConfigurator().config.getBoolean("features.games-gui")) {
            gamesInventory = new GamesInventory(this);
            new GamesCommand();
            new JoinSortedCommand();
        }

        if (getConfigurator().config.getBoolean("features.kits")) {
            kitSelectionInventory = new KitSelectionInventory(this);
            new KitManager();
            try {
                if (getServer().getPluginManager().getPlugin("Vault") == null) {
                    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
                    if (rsp != null)
                        econ = rsp.getProvider();
                }
            } catch (Exception e) {
                XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(),
                        "&cUnable to register Vault economy, you won't be able to use it as price for kits!");
            }
        }

        if (getConfigurator().config.getBoolean("features.specials"))
            new RegisterSpecialListeners();

        XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(),
                "&aLoaded XPWars &2" + XPWars.getInstance().getDescription().getVersion()
                        + (isSnapshotBuild() ? " " + getBuildNumber() + "&a!" : "&a!"));
        XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&aXPWars addon by &enotfoundname11");
        XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&9https://github.com/notfoundname/XPWars/wiki");

        if (configurator.config.getBoolean("check-for-updates"))
            new XPWarsUpdateChecker(Bukkit.getConsoleSender());
    }

    @EventHandler
    public void onBwReload(PluginEnableEvent event) {
        if (event.getPlugin().equals(Main.getInstance())) {
            Bukkit.getServer().getPluginManager().disablePlugin(XPWars.getInstance());
            Bukkit.getServer().getPluginManager().enablePlugin(XPWars.getInstance());
        }
    }

    public static Configurator getConfigurator() {
        return instance.configurator;
    }

    public static XPWars getInstance() {
        return instance;
    }

    public static HashMap<String, BaseCommand> getCommands() {
        return instance.commands;
    }

    public static GamesInventory getGamesInventory() {
        return instance.gamesInventory;
    }

    public static LevelShopInventory getLevelShopInventory() {
        return instance.levelShopInventory;
    }

    public static ShopInventory getShopInventory() {
        return instance.shopInventory;
    }

    public static DebugInventory getDebugInventory() {
        return instance.debugInventory;
    }
    
    public static KitSelectionInventory getKitSelectionInventory() {
        return instance.kitSelectionInventory;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static boolean isSnapshotBuild() {
        return XPWars.getInstance().getDescription().getVersion().contains("-SNAPSHOT") && getBuildNumber() != 0;
    }

    public static float getVersion() {
        if (isSnapshotBuild()) {
            return Float.parseFloat(XPWars.getInstance().getDescription().getVersion().replace("-SNAPSHOT", ""));
        }
        return Float.parseFloat(XPWars.getInstance().getDescription().getVersion());
    }

    public static int getBuildNumber() {
        try {
            return Integer.parseInt(XPWars.getInstance().getDescription().getDescription());
        } catch (Throwable ignored) {
            return 0;
        }
    }

}
