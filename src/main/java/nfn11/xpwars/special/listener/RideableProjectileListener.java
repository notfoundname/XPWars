package nfn11.xpwars.special.listener;

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
import nfn11.xpwars.special.RideableProjectile;

public class RideableProjectileListener implements Listener {
	private static final String RIDEABLE_PROJECTILE_PREFIX = "Module:RideableProjectile:";

	@EventHandler
	public void onTrackerRegistered(BedwarsApplyPropertyToBoughtItem event) {
		if (event.getPropertyName().equalsIgnoreCase("RideableProjectile")) {
			ItemStack stack = event.getStack();
			APIUtils.hashIntoInvisibleString(stack, RIDEABLE_PROJECTILE_PREFIX);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onThrowPearl(ProjectileLaunchEvent event) {
		if (event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			if (!Main.isPlayerInGame(player))
				return;
			GamePlayer gamePlayer = Main.getPlayerGameProfile(player);
			Game game = gamePlayer.getGame();
			ItemStack stack = player.getInventory().getItemInHand();
			if (stack == null)
				return;
			if (game.getStatus() == GameStatus.RUNNING && !gamePlayer.isSpectator) {
				String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(stack, RIDEABLE_PROJECTILE_PREFIX);
				if (unhidden != null) {
					new RideableProjectile(game, player, game.getTeamOfPlayer(player));
					if (player.isInsideVehicle()) {
						player.getVehicle().remove();
					}
					event.getEntity().addPassenger(player);
				}
			}
		}
	}
}
