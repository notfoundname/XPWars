package nfn11.xpwars.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;

import nfn11.thirdparty.connorlinfoot.actionbarapi.ActionBarAPI;
import nfn11.xpwars.XPWars;

public class ResourcePickup implements Listener {

	public ResourcePickup() {
		Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
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

				if (picked.isSimilar(type.getStack())) {
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
}