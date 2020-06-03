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

import nfn11.xpwars.XPWars;

public class ResourcePickup implements Listener {

	public ResourcePickup() {
		Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
	}

	@EventHandler
	public void onPickup(EntityPickupItemEvent event) {
		if (event.getEntity() instanceof Player) {

			Player player = (Player) event.getEntity();
			if (!Main.isPlayerInGame(player))
				return;
			String gamename = Main.getPlayerGameProfile(player).getGame().getName();
			ItemStack picked = event.getItem().getItemStack();

			for (ItemSpawnerType type : Main.getInstance().getItemSpawnerTypes()) {
				String defsound = XPWars.getConfigurator().getString("level.sound.sound",
						"ENTITY_EXPERIENCE_ORB_PICKUP");
				String sound = XPWars.getConfigurator().getString("level.games." + gamename + ".sound.sound", defsound);

				int level = player.getLevel();
				int defmax = XPWars.getConfigurator().getInt("level.maximum-xp", 0);
				int max = XPWars.getConfigurator().getInt("level.games." + gamename + ".maximum-xp", defmax);

				float defvolume = XPWars.getConfigurator().getInt("level.sound.volume", 1);
				float volume = XPWars.getConfigurator().getInt("level.games." + gamename + ".sound.volume",
						(int) defvolume);

				float defpitch = XPWars.getConfigurator().getInt("level.sound.pitch", 1);
				float pitch = XPWars.getConfigurator().getInt("level.games." + gamename + ".sound.pitch",
						(int) defpitch);

				if (picked.equals(type.getStack())) {

					if (max != 0 && (level + (XPWars.getConfigurator().config.getInt("resources." + type.getConfigKey())
							* picked.getAmount())) > max) {

						event.setCancelled(true);
						player.sendMessage(XPWars.getConfigurator()
								.getString("messages.level.maxreached", "&cYou can't have more than %max% levels!")
								.replace("%max%", Integer.toString(max)));
						return;
					}

					event.getItem().remove();
					player.setLevel(level + (XPWars.getConfigurator().config.getInt("resources." + type.getConfigKey())
							* picked.getAmount()));

					player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
				}
			}
		}
	}
}