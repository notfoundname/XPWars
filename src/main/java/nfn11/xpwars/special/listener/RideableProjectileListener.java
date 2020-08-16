package nfn11.xpwars.special.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;
import nfn11.xpwars.XPWars;
import nfn11.xpwars.special.RideableProjectile;
import nfn11.xpwars.utils.SpecialItemUtils;

public class RideableProjectileListener implements Listener {
    private static final String RIDEABLE_PROJECTILE_PREFIX = "Module:RideableProjectile:";

    @EventHandler
    public void onProjectileRegister(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("RideableProjectile")) {
            ItemStack stack = event.getStack();
            APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if (!Main.isPlayerInGame(player))
                return;
            GamePlayer gamePlayer = Main.getPlayerGameProfile(player);
            Game game = gamePlayer.getGame();
            ItemStack stack = player.getInventory().getItemInHand();

            String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(stack, RIDEABLE_PROJECTILE_PREFIX);
            if (unhidden != null) {
                boolean allow_leave = Boolean.getBoolean(unhidden.split(":")[2]);
                boolean remove_on_leave = Boolean.getBoolean(unhidden.split(":")[3]);

                RideableProjectile special = new RideableProjectile(game, player, game.getTeamOfPlayer(player),
                        allow_leave, remove_on_leave, event.getEntity());
                special.run(event.getEntity());
            }
        }
    }

    @EventHandler
    public void onLeave(VehicleExitEvent event) {
        if (event.getExited() instanceof Player) {
            if (!event.getVehicle().hasMetadata("allow-leave")) {
                event.setCancelled(true);
                event.getVehicle().removePassenger(event.getExited());
                event.getVehicle().addPassenger(event.getExited());
            } else {
                if (event.getVehicle().hasMetadata("remove-on-leave")) {
                    event.getVehicle().remove();
                }
            }
        }
    }

    private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
        return RIDEABLE_PROJECTILE_PREFIX
                + SpecialItemUtils.getBooleanFromProperty("allow-leave", XPWars.getConfigurator().config,
                        "specials.rideable-projectile.allow-leave", event)
                + ":" + SpecialItemUtils.getBooleanFromProperty("remove-on-leave", XPWars.getConfigurator().config,
                        "specials.rideable-projectile.remove-on-leave", event);
    }
}
