package com.notfoundname.xpwars.features.sidebar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.GameStatus;

public class SidebarListener implements Listener {

    public SidebarListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGameJoined(BedwarsPlayerJoinedEvent event) {
        if (event.getGame().getStatus() == GameStatus.WAITING) {
            //sidebar.addViewer(BukkitPlayerMapper.getPlayer(event.getPlayer().getUniqueId()).get());
        }
    }

    @EventHandler
    public void onGameLeft(BedwarsPlayerLeaveEvent event) {
        //sidebar.removeViewer(BukkitPlayerMapper.getPlayer(event.getPlayer().getUniqueId()).get());
    }
}
