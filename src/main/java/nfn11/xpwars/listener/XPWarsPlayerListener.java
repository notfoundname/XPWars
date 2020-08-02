package nfn11.xpwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent.Result;
import org.screamingsandals.bedwars.api.game.*;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.bedwars.game.*;
import org.screamingsandals.bedwars.lib.nms.entity.PlayerUtils;

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

            int def_keep_from_death_player = XPWars.getConfigurator().getInt("level.percentage.keep-from-death", 0);
            int def_to_killer = XPWars.getConfigurator().getInt("level.percentage.give-from-killed-player", 0);

            int keep_from_death_player = XPWars.getConfigurator()
                    .getInt("level.games." + gamename + ".percentage.keep-from-death", def_keep_from_death_player);
            int to_killer = XPWars.getConfigurator()
                    .getInt("level.games." + gamename + "percentage.give-from-killed-player", def_to_killer);

            if (def_to_killer > 100 || def_to_killer < 0) {
                def_to_killer = 100;
            }

            if (to_killer > 100 || to_killer < 0) {
                to_killer = 100;
            }

            if (def_keep_from_death_player > 100 || def_keep_from_death_player < 0) {
                def_keep_from_death_player = 0;
            }

            if (keep_from_death_player > 100 || keep_from_death_player < 0) {
                def_keep_from_death_player = 0;
            }

            int defmax = XPWars.getConfigurator().getInt("level.maximum-xp", 0);
            int max = XPWars.getConfigurator().getInt("level.games." + gamename + ".maximum-xp", defmax);

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
        player.setHealth(player.getMaxHealth());
        PlayerUtils.respawn(XPWars.getInstance(), player, 0);
    }

    @EventHandler
    public void onShopOpen(BedwarsOpenShopEvent event) {
        if (Main.getPlayerGameProfile(event.getPlayer()).isSpectator)
            return;
        if (XPWars.getConfigurator().config.getBoolean("features.level-system") || XPWars.getConfigurator()
                .getBoolean("level.per-arena-settings." + event.getGame().getName() + "enable", true)) {
            event.setResult(Result.DISALLOW_THIRD_PARTY_SHOP);
            XPWars.getLevelShop().show(event.getPlayer(), event.getStore());
        } else
            return;
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!XPWars.getConfigurator().config.getBoolean("features.level-system"))
            return;

        if (event.getEntity() instanceof Player) {

            Player player = (Player) event.getEntity();
            if (!Main.isPlayerInGame(player))
                return;
            String gamename = Main.getPlayerGameProfile(player).getGame().getName();
            ConfigurationSection sec = XPWars.getConfigurator().config
                    .getConfigurationSection("level.per-arena-settings." + gamename);

            if (!sec.getBoolean("enable", true)) {
                return;
            }

            ItemStack picked = event.getItem().getItemStack();
            int level = player.getLevel();

            float defvolume = XPWars.getConfigurator().config.getInt("level.sound.volume", 1);
            float volume = sec.getInt("sound.volume", (int) defvolume);

            String defsound = XPWars.getConfigurator().config.getString("level.sound.sound",
                    "ENTITY_EXPERIENCE_ORB_PICKUP");
            String sound = sec.getString("sound.sound", defsound);

            int defmax = XPWars.getConfigurator().config.getInt("level.maximum-xp", 0);
            int max = sec.getInt("maximum-xp", defmax);

            float defpitch = XPWars.getConfigurator().config.getInt("level.sound.pitch", 1);
            float pitch = sec.getInt("sound.pitch", (int) defpitch);

            Main.getInstance().getItemSpawnerTypes().forEach(type -> {
                int defres = XPWars.getConfigurator().config.getInt("level.spawners." + type.getConfigKey(), 0);
                int res = sec.getInt("spawners." + type.getConfigKey(), defres);

                if (picked.isSimilar(type.getStack()) && picked.getItemMeta().equals(type.getStack().getItemMeta())) {
                    event.setCancelled(true);
                    if (max != 0 && (level + (res * picked.getAmount())) > max) {
                        ActionBarAPI.sendActionBar(player,
                                XPWars.getConfigurator().config
                                        .getString("messages.level.maxreached",
                                                "&cYou can't have more than %max% levels!")
                                        .replace("%max%", Integer.toString(max)));
                        return;
                    }
                    event.getItem().remove();
                    player.setLevel(level + (res * picked.getAmount()));
                }
            });
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
    }

    @EventHandler
    public void onGameTick(BedwarsGameTickEvent event) {
        if (event.getGame() == null)
            return;
        if (XPWars.getConfigurator().config.getBoolean("features.action-bar-messages")) {
            event.getGame().getConnectedPlayers().forEach(player -> {
                GamePlayer gp = Main.getPlayerGameProfile(player);
                CurrentTeam team = gp.getGame().getPlayerTeam(gp);
                if (gp.isSpectator) {
                    ActionBarAPI.sendActionBar(player,
                            XPWars.getConfigurator().config.getString("action-bar-messages.in-game-spectator"));
                    return;
                }
                if (team == null)
                    return;
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
                                    .replace("%team%", team.teamInfo.color.chatColor + team.getName()).replace("%bed%",
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
        ConfigurationSection sec = XPWars.getConfigurator().config
                .getConfigurationSection("permission-to-join-game.arenas");
        if (sec == null)
            return;
        if (sec.getValues(false).containsKey(event.getGame().getName())) {
            if (!event.getPlayer()
                    .hasPermission(XPWars.getConfigurator().config
                            .getString("permission-to-join-game.arenas." + event.getGame().getName()))
                    || !event.getPlayer().isOp() || !event.getPlayer().hasPermission(BaseCommand.ADMIN_PERMISSION)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(XPWars.getConfigurator().getString("permission-to-join-game.message", "")
                        .replace("%arena%", event.getGame().getName()).replace("%perm%", XPWars.getConfigurator().config
                                .getString("permission-to-join-game.arenas." + event.getGame().getName())));
                return;
            }
        }
    }
    
}
