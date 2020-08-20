package nfn11.xpwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent.Result;
import org.screamingsandals.bedwars.api.game.*;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;
import org.screamingsandals.bedwars.game.*;

import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.XPWars;

public class XPWarsPlayerListener implements Listener {

    public XPWarsPlayerListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(BedwarsPlayerKilledEvent event) {
        Player player = event.getPlayer();

        if (XPWars.getConfigurator().config.getBoolean("features.level-system")) {
            String gamename = event.getGame().getName();

            int player_level = player.getLevel();

            ConfigurationSection arenaSettings = XPWars.getConfigurator().config.getConfigurationSection(
                    "level.per-arena-settings." + gamename);
            ConfigurationSection globalSettings = XPWars.getConfigurator().config.getConfigurationSection("level");

            if (!arenaSettings.getBoolean("enable", true))
                return;

            int keep_from_death_player = arenaSettings.getInt("percentage.keep-from-death",
                    globalSettings.getInt("percentage.keep-from-death",0));
            int to_killer = arenaSettings.getInt("percentage.give-from-killed-player", 0);
            int max = arenaSettings.getInt("maximum-xp", globalSettings.getInt("maximum-xp", 0));

            if (event.getKiller() != null) {
                Player killer = event.getKiller();
                int killer_level = killer.getLevel();
                if (max != 0 && (killer_level + (player_level / 100) * to_killer) > max) {
                    killer.setLevel(max);
                } else
                    killer.setLevel(killer_level + (player_level / 100) * to_killer);
            }

            if (max != 0 && ((player_level / 100) * keep_from_death_player) > max) {
                player.setLevel(max);
            } else
                player.setLevel((player_level / 100) * keep_from_death_player);
        }
        PlayerRespawnEvent playerRespawnEvent = new PlayerRespawnEvent(player, Main.getPlayerGameProfile(player).isSpectator
                ? event.getGame().getSpectatorSpawn() : event.getGame().getTeamOfPlayer(player).getTeamSpawn(), false, false);
        Bukkit.getPluginManager().callEvent(playerRespawnEvent);
    }

    @EventHandler
    public void onShopOpen(BedwarsOpenShopEvent event) {
        if (Main.getPlayerGameProfile(event.getPlayer()).isSpectator)
            return;
        if (XPWars.getConfigurator().config.getBoolean("features.level-system") || XPWars.getConfigurator()
                .getBoolean("level.per-arena-settings." + event.getGame().getName() + "enable", true)) {
            event.setResult(Result.DISALLOW_THIRD_PARTY_SHOP);
            XPWars.getLevelShop().show(event.getPlayer(), event.getStore());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR) 
    public void onPickup(EntityPickupItemEvent event) {
        if (!XPWars.getConfigurator().config.getBoolean("features.level-system"))
            return;
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!Main.isPlayerInGame(player))
                return;

            ConfigurationSection arenaSettings = XPWars.getConfigurator().config.getConfigurationSection(
                    "level.per-arena-settings." + Main.getPlayerGameProfile(player).getGame().getName());
            ConfigurationSection globalSettings = XPWars.getConfigurator().config.getConfigurationSection("level");

            if (!arenaSettings.getBoolean("enable", true))
                return;

            ItemStack picked = event.getItem().getItemStack();
            int level = player.getLevel();

            String sound = arenaSettings.getString("sound.sound", globalSettings.getString
                    ("sound.sound","ENTITY_EXPERIENCE_ORB_PICKUP"));
            double volume = arenaSettings.getDouble("sound.volume", globalSettings.getDouble("sound.volume", 1));
            double pitch = arenaSettings.getDouble("sound.pitch", globalSettings.getDouble("sound.pitch", 1));
            
            int max = arenaSettings.getInt("maximum-xp", globalSettings.getInt("maximum-xp", 0));
            
            for (ItemSpawnerType type : Main.getInstance().getItemSpawnerTypes()) {
                int res = arenaSettings.getInt("spawners." + type.getConfigKey(), globalSettings.getInt
                        ("spawners." + type.getConfigKey()));
                if (picked.isSimilar(type.getStack()) && picked.getItemMeta().equals(type.getStack().getItemMeta())) {
                    event.setCancelled(true);
                    if (max != 0 && (level + (res * picked.getAmount())) > max) {
                        arenaSettings.getString("messages.maxreached", globalSettings.getString(
                                "messages.maxreached","&cYou can't have more than %max% levels!")
                                        .replace("%max%", Integer.toString(max)));
                        return;
                    }
                    event.getItem().remove();
                    player.setLevel(level + (res * picked.getAmount()));
                }
            }
            if (!sound.equalsIgnoreCase("none"))
                player.playSound(player.getLocation(), Sound.valueOf(sound), (float) volume, (float) pitch);
        }
    }

    @EventHandler
    public void onGameTick(BedwarsGameTickEvent event) {
        if (XPWars.getConfigurator().config.getBoolean("features.action-bar-messages")) {
            event.getGame().getConnectedPlayers().forEach(player -> {
                GamePlayer gp = Main.getPlayerGameProfile(player);
                CurrentTeam team = gp.getGame().getPlayerTeam(gp);
                if (team == null || gp.isSpectator) {
                    ActionBarAPI.sendActionBar(player,
                            XPWars.getConfigurator().config.getString("action-bar-messages.in-game-spectator"));
                    return;
                }
                if (gp.getGame().getStatus() == GameStatus.WAITING) {
                    ActionBarAPI.sendActionBar(player,
                            XPWars.getConfigurator().config.getString("action-bar-messages.in-lobby")
                                    .replace("%pl_t%", Integer.toString(team.countConnectedPlayers()))
                                    .replace("%team%", team.teamInfo.color.chatColor + team.getName())
                                    .replace("%mxpl_t%", Integer.toString(team.getMaxPlayers())));
                    return;
                }
                if (gp.getGame().getStatus() == GameStatus.RUNNING) {
                    ActionBarAPI.sendActionBar(player,
                            XPWars.getConfigurator().config.getString("action-bar-messages.in-game-alive")
                                    .replace("%team%", team.teamInfo.color.chatColor + team.getName())
                                    .replace("%bed%",
                                            team.isTargetBlockExists()
                                                    ? Main.getConfigurator().config.getString("scoreboard.bedExists")
                                                    : Main.getConfigurator().config.getString("scoreboard.bedLost")));
                }
            });
        }
    }

    @EventHandler
    public void onJoinGame(BedwarsPlayerJoinEvent event) {
        if (event.isCancelled())
            return;
        ConfigurationSection sec = XPWars.getConfigurator().config.getConfigurationSection("permission-to-join-game");
        if (sec == null)
            return;
        for (String permission : sec.getConfigurationSection("arenas").getValues(false).keySet()) {
            if (sec.getStringList("arenas." + permission).contains(event.getGame().getName()) && !event.getPlayer()
                    .hasPermission(permission.replace("[", "").replace("]", ""))) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(sec.getString("permission-to-join-game.message",
                        "You don't have permission %perm% to join arena %arena%!")
                        .replace("%perm%", permission)
                        .replace("%arena%", event.getGame().getName()));
                return;
            }
        }
    }

}
