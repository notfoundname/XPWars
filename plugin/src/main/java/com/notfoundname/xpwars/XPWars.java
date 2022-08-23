package com.notfoundname.xpwars;

import com.notfoundname.xpwars.api.XPWarsAPI;
import net.elytrium.java.commons.updates.UpdatesChecker;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.screamingsandals.bedwars.lib.sgui.listeners.InventoryListener;

import java.io.File;

public final class XPWars extends JavaPlugin implements XPWarsAPI, Listener {
    private static XPWars instance;

    @Override
    public void onEnable() {
        instance = this;
        File configFile = new File(getDataFolder(), "config.yml");
        XPWarsConfig.IMP.reload(configFile);

        InventoryListener.init(this);

        getLogger().info(getDescription().getName() + " version " + getDescription().getVersion() + " by " + getDescription().getAuthors());

        getLogger().info("Checking for updates...");
        if (!UpdatesChecker.checkVersionByURL("https://api.spigotmc.org/legacy/update.php?resource=76895",
                this.getDescription().getVersion())) {
            getLogger().info("New version of XPWars is available!\nDownload at https://www.spigotmc.org/resources/76895/");
        } else {
            getLogger().info("No updates found.");
        }
    }

    public static XPWars getInstance() {
        return instance;
    }

    @EventHandler
    public void onBedWarsReload(PluginEnableEvent event) {
        if (event.getPlugin().getName().equals("BedWars")) {
            try {
                Bukkit.getPluginManager().callEvent(new PluginDisableEvent(XPWars.getInstance()));
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            try {
                Bukkit.getServicesManager().unregisterAll(instance);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            try {
                HandlerList.unregisterAll(instance);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            try {
                Bukkit.getMessenger().unregisterIncomingPluginChannel(instance);
                Bukkit.getMessenger().unregisterOutgoingPluginChannel(instance);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }

            Bukkit.getServer().getPluginManager().enablePlugin(instance);
        }
    }

}
