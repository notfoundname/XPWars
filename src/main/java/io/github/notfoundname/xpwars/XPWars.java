package io.github.notfoundname.xpwars;

import io.github.notfoundname.xpwars.commands.GamesCommand;
import io.github.notfoundname.xpwars.commands.JoinSortedCommand;
import io.github.notfoundname.xpwars.commands.XPWarsCommand;
import io.github.notfoundname.xpwars.inventories.GamesInventory;
import io.github.notfoundname.xpwars.inventories.KitSelectionInventory;
import io.github.notfoundname.xpwars.inventories.XPWarsInventory;
import io.github.notfoundname.xpwars.listener.ActionBarMessageListener;
import io.github.notfoundname.xpwars.listener.LevelSystemListener;
import io.github.notfoundname.xpwars.placeholderapi.PlaceholderAPIHook;
import io.github.notfoundname.xpwars.special.listener.RegisterSpecialListeners;
import io.github.notfoundname.xpwars.utils.XPWarsUtils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import io.github.notfoundname.xpwars.listener.EnemyHideNametagsListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.lib.sgui.listeners.InventoryListener;

public class XPWars extends JavaPlugin implements Listener {

    private static XPWars instance;
    private Configurator configurator;
    private GamesInventory gamesInventory;
    private XPWarsInventory xpWarsInventory;
    private KitSelectionInventory kitSelectionInventory;
    private Economy economy = null;

    @Override
    public void onEnable() {

        if (Main.getInstance() == null) {
            Bukkit.getServer().getLogger().warning("Addon won't start without ScreamingBedwars.");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;

        new XPWarsCommand();

        configurator = new Configurator(this);
        configurator.loadDefaults();

        InventoryListener.init(this);
        xpWarsInventory = new XPWarsInventory();
        Bukkit.getPluginManager().registerEvents(this, this);

        try {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
                    && XPWars.getConfigurator().config.getBoolean("features.placeholders")) {
                new PlaceholderAPIHook(this).register();
            } else XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&cYou don't have PlaceholderAPI installed.");
        } catch (Throwable ignored) {}

        if (getConfigurator().config.getBoolean("features.action-bar-messages"))
            new ActionBarMessageListener();
        
        if (getConfigurator().config.getBoolean("features.level-system"))
            new LevelSystemListener();

        if (getConfigurator().config.getBoolean("features.games-gui")) {
            gamesInventory = new GamesInventory(this);
            new GamesCommand();
            new JoinSortedCommand();
        }

        if (getConfigurator().config.getBoolean("features.specials"))
            new RegisterSpecialListeners();

        if (getConfigurator().config.getBoolean("features.hide-enemy-nametags"))
            new EnemyHideNametagsListener();

        if (getConfigurator().config.getBoolean("features.kits")) {
            kitSelectionInventory = new KitSelectionInventory(this);
            if (Main.isVault()) {
                try {
                    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
                    assert rsp != null;
                    economy = rsp.getProvider();
                } catch (Throwable ignored) { }
            }
        }
        XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(),
                "&aLoaded XPWars &2" + XPWars.getInstance().getDescription().getVersion() + "&a!");
        XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&aXPWars addon by &enotfoundname11");
        XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&9Visit https://github.com/notfoundname/XPWars/wiki");

        if (configurator.config.getBoolean("check-for-updates"))
            XPWarsUpdateChecker.checkForUpdate(Bukkit.getConsoleSender());
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

    public static GamesInventory getGamesInventory() {
        return instance.gamesInventory;
    }

    public static XPWarsInventory getXPWarsInventory() {
        return instance.xpWarsInventory;
    }
    
    public static KitSelectionInventory getKitSelectionInventory() {
        return instance.kitSelectionInventory;
    }

    public static float getVersion() {
        return Float.parseFloat(XPWars.getInstance().getDescription().getVersion().replace("-SNAPSHOT", ""));
    }

    public static boolean depositPlayer(Player player, double coins) {
        if (Main.isVault()) {
            try {
                EconomyResponse response = instance.economy.depositPlayer(player, coins);
                return response.transactionSuccess();
            } catch (Throwable ignored) { }
        } return false;
    }

    public static boolean withdrawPlayer(Player player, double coins) {
        if (Main.isVault()) {
            try {
                EconomyResponse response = instance.economy.withdrawPlayer(player, coins);
                return response.transactionSuccess();
            } catch (Throwable ignored) { }
        } return false;
    }

    public static double getBalance(Player player) {
        if (Main.isVault()) {
            try {
                return instance.economy.getBalance(player);
            } catch (Throwable ignored) { }
        } return 0.0;
    }

}
