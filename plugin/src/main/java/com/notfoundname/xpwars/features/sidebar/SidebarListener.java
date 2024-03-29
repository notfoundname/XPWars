package com.notfoundname.xpwars.features.sidebar;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.lib.bukkit.player.BukkitPlayerMapper;
import org.screamingsandals.lib.sidebar.Sidebar;

public class SidebarListener implements Listener {

    private final Sidebar sidebar = Sidebar.of()
            .title(Component.text("Sidebar Test"))
            .bottomLine(Component.text("test"))
            .bottomLine(Component.text("linea"))
            .bottomLine(Component.text("я руский"))
            .show();

    public SidebarListener(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGameJoined(BedwarsPlayerJoinedEvent event) {
        if (event.getGame().getStatus() == GameStatus.WAITING) {
            sidebar.addViewer(BukkitPlayerMapper.getPlayer(event.getPlayer().getUniqueId()).get());
        }
    }

    @EventHandler
    public void onGameLeft(BedwarsPlayerLeaveEvent event) {
        if (event.getGame().getStatus() == GameStatus.WAITING) {
            sidebar.removeViewer(BukkitPlayerMapper.getPlayer(event.getPlayer().getUniqueId()).get());
        }
    }
}
