package io.github.notfoundname.xpwars.special;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

import io.github.notfoundname.xpwars.XPWars;

public class RideableProjectile extends SpecialItem implements io.github.notfoundname.xpwars.api.special.RideableProjectile {
    private boolean isAllowedLeaving, isRemoveOnLeave;
    private Entity entity;
    
    public RideableProjectile(Game game, Player player, Team team, boolean isAllowedLeaving, boolean isRemoveOnLeave, Entity entity) {
        super(game, player, team);
        this.isAllowedLeaving = isAllowedLeaving;
        this.isRemoveOnLeave = isRemoveOnLeave;
        this.entity = entity;
    }

    @Override
    public boolean isAllowedLeaving() {
        return isAllowedLeaving;
    }

    @Override
    public boolean isRemoveOnLeave() {
        return isRemoveOnLeave;
    }
    
    @Override
    public void run(Entity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isInsideVehicle()) {
                    player.getVehicle().remove();
                }
                entity.addPassenger(player);
                entity.setMetadata("rideableprojectile",
                        new FixedMetadataValue(XPWars.getInstance(), null));
                if (isAllowedLeaving)
                    entity.setMetadata("allow-leave",
                            new FixedMetadataValue(XPWars.getInstance(), null));
                if (isRemoveOnLeave)
                    entity.setMetadata("remove-on-leave",
                            new FixedMetadataValue(XPWars.getInstance(), null));
            }
        }.runTaskLater(XPWars.getInstance(), 20);
    }

    @Override
    public Entity getEntity() {
        return entity;
    }
}
