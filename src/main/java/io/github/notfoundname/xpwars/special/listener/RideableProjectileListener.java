package io.github.notfoundname.xpwars.special.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;
import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.special.RideableProjectile;
import io.github.notfoundname.xpwars.utils.SpecialItemUtils;

public class RideableProjectileListener implements Listener {
    private static final String RIDEABLE_PROJECTILE_PREFIX = "Module:RideableProjectile:";

    @EventHandler
    public void onProjectileRegister(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("RideableProjectile"))
            APIUtils.hashIntoInvisibleString(event.getStack(), applyProperty(event));
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            assert Main.isPlayerInGame(player);
            GamePlayer gamePlayer = Main.getPlayerGameProfile(player);
            Game game = gamePlayer.getGame();

            String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(player.getInventory().getItemInHand(),
                    RIDEABLE_PROJECTILE_PREFIX);
            if (unhidden != null) {
                boolean allowLeave = Boolean.getBoolean(unhidden.split(":")[2]);
                boolean removeOnLeave = Boolean.getBoolean(unhidden.split(":")[3]);

                RideableProjectile special = new RideableProjectile(game, player, game.getTeamOfPlayer(player),
                        allowLeave, removeOnLeave, event.getEntity());
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
            } else if (event.getVehicle().hasMetadata("remove-on-leave"))
                event.getVehicle().remove();
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
