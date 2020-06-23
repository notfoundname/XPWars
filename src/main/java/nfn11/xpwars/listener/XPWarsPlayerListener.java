package nfn11.xpwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.BedwarsGameTickEvent;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerKilledEvent;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent.Result;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;
import org.screamingsandals.bedwars.game.CurrentTeam;
import org.screamingsandals.bedwars.game.GamePlayer;
import org.screamingsandals.bedwars.lib.nms.entity.PlayerUtils;

import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.XPWars;
import nfn11.xpwars.inventories.LevelShop;

public class XPWarsPlayerListener implements Listener {
	LevelShop xp;

	public XPWarsPlayerListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onDeath(BedwarsPlayerKilledEvent event) {
		Player player = event.getPlayer();

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
		player.setHealth(player.getMaxHealth());
		PlayerUtils.respawn(XPWars.getInstance(), player, 0);
	}

	@EventHandler
	public void onShopOpen(BedwarsOpenShopEvent event) {
		if (Main.getPlayerGameProfile(event.getPlayer()).isSpectator)
			return;
		if (XPWars.getConfigurator().config.getBoolean("level.enable", true)) {
			event.setResult(Result.DISALLOW_THIRD_PARTY_SHOP);
			xp.show(event.getPlayer(), event.getStore());
		} else
			return;
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if (!XPWars.getConfigurator().config.getBoolean("level.enable"))
			return;

		if (event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();
			if (!Main.isPlayerInGame(player))
				return;
			String gamename = Main.getPlayerGameProfile(player).getGame().getName();
			ItemStack picked = event.getItem().getItemStack();

			for (ItemSpawnerType type : Main.getInstance().getItemSpawnerTypes()) {

				String defsound = XPWars.getConfigurator().config.getString("level.sound.sound",
						"ENTITY_EXPERIENCE_ORB_PICKUP");
				String sound = XPWars.getConfigurator().config
						.getString("level.per-arena-settings." + gamename + ".sound.sound", defsound);

				int level = player.getLevel();
				int defmax = XPWars.getConfigurator().config.getInt("level.maximum-xp", 0);
				int max = XPWars.getConfigurator().config.getInt("level.per-arena-settings." + gamename + ".maximum-xp",
						defmax);

				int defres = XPWars.getConfigurator().config.getInt("level.spawners." + type.getConfigKey(), 0);
				int res = XPWars.getConfigurator()
						.getInt("level.per-arena-settings." + gamename + ".spawners." + type.getConfigKey(), defres);

				float defvolume = XPWars.getConfigurator().config.getInt("level.sound.volume", 1);
				float volume = XPWars.getConfigurator().config
						.getInt("level.per-arena-settings." + gamename + ".sound.volume", (int) defvolume);

				float defpitch = XPWars.getConfigurator().config.getInt("level.sound.pitch", 1);
				float pitch = XPWars.getConfigurator().config
						.getInt("level.per-arena-settings." + gamename + ".sound.pitch", (int) defpitch);

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

					player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
				}
			}
		}
	}

	@EventHandler
	public void onGameTick(BedwarsGameTickEvent event) {
		if (event.getGame() == null)
			return;
		for (Player player : event.getGame().getConnectedPlayers()) {
			GamePlayer gp = Main.getPlayerGameProfile(player);
			CurrentTeam team = gp.getGame().getPlayerTeam(gp);
			if (team == null || gp.getGame().getStatus() == GameStatus.WAITING)
				return;
			ActionBarAPI.sendActionBar(player,
					"Team: %team% [%players%/%maxplayers%]"
							.replace("%players%", Integer.toString(team.countConnectedPlayers()))
							.replace("%team%", team.teamInfo.color.chatColor + team.getName())
							.replace("%maxplayers%", Integer.toString(team.getMaxPlayers())));
		}
	}
}
