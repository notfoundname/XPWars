package io.github.notfoundname.xpwars.listener;

import io.github.notfoundname.xpwars.utils.XPWarsUtils;
import io.github.notfoundname.xpwars.XPWars;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;

public class LevelSystemListener implements Listener {

    public LevelSystemListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
    }

    private static ConfigurationSection globalSettings = XPWars.getConfigurator().config.getConfigurationSection("level");
    private static ConfigurationSection arenaSettings = XPWars.getConfigurator().config.getConfigurationSection("level.per-arena-settings");

    @EventHandler
    public void onDeath(BedwarsPlayerKilledEvent event) {
        String gameName = event.getGame().getName();
        // later
        /*player.setLevel(playerLevel * keepFromDeathPercentage);

        if (event.getKiller() != null) {
            final Player killer = event.getKiller();
            int killerLevel = killer.getLevel();
            if (maximumLevel != 0 && killerLevel + (playerLevel * giveToKillerPercentage) > maximumLevel)
                killer.setLevel(maximumLevel);
            else killer.setLevel(killerLevel + (playerLevel * giveToKillerPercentage));
        }*/
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        assert event.getEntity().getType() != EntityType.PLAYER;
        final Player player = (Player) event.getEntity();
        // later
            /*for (ItemSpawnerType type : Main.getInstance().getItemSpawnerTypes()) {
                int spawnerLevel = arenaSettings.getInt("spawners." + type.getConfigKey());
                if (itemStack.isSimilar(type.getStack()) && itemStack.getItemMeta() == type.getStack().getItemMeta()) {
                    event.setCancelled(true);
                    if (maximumLevel != 0 && (playerLevel + (spawnerLevel * itemStack.getAmount()) > maximumLevel)) {
                        XPWarsUtils.sendActionBar(player, arenaSettings.getString("messages.maxreached")
                                .replace("%max%", Integer.toString(maximumLevel)));
                        return;
                    }
                    player.setLevel(playerLevel + (spawnerLevel * itemStack.getAmount()));
                    if (pickupItemLogic)
                        event.getItem().getItemStack().setType(Material.AIR);
                    else event.getItem().remove();
                }
            }
            if (sound != null && !sound.equals("none"))
                player.playSound(player.getLocation(), sound, volume, pitch);*/

    }

}
