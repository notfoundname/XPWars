package io.github.notfoundname.xpwars;

import io.github.notfoundname.xpwars.config.XPWarsConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
    Отче наш, Иже еси на небесех!
    Да святится имя Твое, да приидет Царствие Твое,
    да будет воля Твоя, яко на небеси и на земли́.
    Хлеб наш насущный даждь нам днесь;
    и остави нам долги наша, якоже и мы оставляем должником нашим;
    и не введи нас во искушение, но избави нас от лукаваго.
    Яко Твое есть Царство и сила, и слава, Отца, и Сына, и Святаго Духа, ныне и присно, и во веки веков. Аминь.
*/

public final class XPWars extends JavaPlugin implements Listener {

    private static XPWars instance;
    private XPWarsConfig config;

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getPluginManager().registerEvents(this, this);

        config = new XPWarsConfig(this);
        config.generate();
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
                HandlerList.unregisterAll((Listener) instance);
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
