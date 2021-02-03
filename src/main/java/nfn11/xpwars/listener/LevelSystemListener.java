package nfn11.xpwars.listener;

import nfn11.xpwars.utils.XPWarsUtils;
import nfn11.xpwars.XPWars;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler
    public void onDeath(BedwarsPlayerKilledEvent event) {
        final Player player = event.getPlayer();
        final int playerLevel = player.getLevel();
        String gameName = event.getGame().getName();

        ConfigurationSection arenaSettings = XPWars.getConfigurator().config.getConfigurationSection(
                "level.per-arena-settings." + gameName);
        ConfigurationSection globalSettings = XPWars.getConfigurator().config.getConfigurationSection("level");

        assert globalSettings != null;
        if (arenaSettings == null) arenaSettings = globalSettings;

        if (!arenaSettings.getBoolean("enable", true))
            return;

        int keepFromDeathPercentage = arenaSettings.getInt("percentage.keep-from-death",
                globalSettings.getInt("percentage.keep-from-death")) / 100;
        int giveToKillerPercentage = arenaSettings.getInt("percentage.give-from-killed-player",
                globalSettings.getInt("percentage.give-from-killed-player")) / 100;
        int maximumLevel = arenaSettings.getInt("maximum-xp");

        player.setLevel(playerLevel * keepFromDeathPercentage);

        if (event.getKiller() != null) {
            final Player killer = event.getKiller();
            int killerLevel = killer.getLevel();
            if (maximumLevel != 0 && killerLevel + (playerLevel * giveToKillerPercentage) > maximumLevel)
                killer.setLevel(maximumLevel);
            else killer.setLevel(killerLevel + (playerLevel * giveToKillerPercentage));
        }
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player player = (Player) event.getEntity();

            if (!Main.isPlayerInGame(player))
                return;

            String gameName = Main.getPlayerGameProfile(player).getGame().getName();
            int playerLevel = player.getLevel();
            ItemStack itemStack = event.getItem().getItemStack();

            ConfigurationSection globalSettings = XPWars.getConfigurator().config.getConfigurationSection("level");
            ConfigurationSection arenaSettings = XPWars.getConfigurator().config.getConfigurationSection(
                    "level.per-arena-settings." + gameName);

            assert globalSettings != null;
            if (arenaSettings == null) arenaSettings = globalSettings;

            String sound = arenaSettings.getString("sound.sound","ENTITY_EXPERIENCE_ORB_PICKUP");
            float volume = (float) arenaSettings.getDouble("sound.volume", 0.5);
            float pitch = (float) arenaSettings.getDouble("sound.pitch", 1.0);
            int maximumLevel = arenaSettings.getInt("maximum-xp", 0);
            boolean pickupItemLogic = arenaSettings.getBoolean("dev-different-pickup-item-logic", false);

            for (ItemSpawnerType type : Main.getInstance().getItemSpawnerTypes()) {
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
                player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

}
