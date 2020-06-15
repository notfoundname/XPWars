package nfn11.xpwars.special.listener;

import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.game.GamePlayer;

import nfn11.xpwars.special.RideableEnderPearl;

public class RideableEnderPearlListener implements Listener {
	private static final String RIDEABLE_ENDER_PEARL_PREFIX = "Module:RideableEnderPearl:";

	@EventHandler
	public void onTrackerRegistered(BedwarsApplyPropertyToBoughtItem event) {
		if (event.getPropertyName().equalsIgnoreCase("RideableEnderPearl")) {
			ItemStack stack = event.getStack();
			APIUtils.hashIntoInvisibleString(stack, RIDEABLE_ENDER_PEARL_PREFIX);
		}
	}

	@EventHandler
	public void onThrowPearl(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			if (!Main.isPlayerInGame(player))
				return;
			GamePlayer gamePlayer = Main.getPlayerGameProfile(player);
			Game game = gamePlayer.getGame();
			ItemStack stack = player.getItemInHand();
			if (stack == null)
				return;
			if (game.getStatus() == GameStatus.RUNNING && !gamePlayer.isSpectator) {
				String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(stack, RIDEABLE_ENDER_PEARL_PREFIX);
				if (unhidden != null) {
					if (event.getEntity() instanceof EnderPearl) {
						new RideableEnderPearl(game, player, game.getTeamOfPlayer(player));
						event.getEntity().addPassenger(player);
					}
				}
			}
		}
	}
}
