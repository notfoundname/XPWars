package io.github.notfoundname.xpwars.special;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

import io.github.notfoundname.xpwars.XPWars;

public class RideableProjectile extends SpecialItem implements io.github.notfoundname.xpwars.api.special.RideableProjectile {
    private boolean isAllowedLeaving, isRemoveOnLeave, isSpectatorMode;
    private Entity entity;
    
    public RideableProjectile(Game game, Player player, Team team, boolean isAllowedLeaving, boolean isRemoveOnLeave, boolean isSpectatorMode, Entity entity) {
        super(game, player, team);
        this.isAllowedLeaving = isAllowedLeaving;
        this.isRemoveOnLeave = isRemoveOnLeave;
        this.isSpectatorMode = isSpectatorMode;
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
    public boolean isSpectatorMode() {
        return isSpectatorMode;
    }

    @Override
    public void run(Entity entity) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(XPWars.getInstance(), task -> {
            if (player.isInsideVehicle())
                player.getVehicle().remove();
            entity.addPassenger(player);
            entity.setMetadata("rideableprojectile",
                    new FixedMetadataValue(XPWars.getInstance(), null));
            if (isAllowedLeaving)
                entity.setMetadata("allow-leave",
                        new FixedMetadataValue(XPWars.getInstance(), null));
            if (isRemoveOnLeave)
                entity.setMetadata("remove-on-leave",
                        new FixedMetadataValue(XPWars.getInstance(), null));

        }, 20);
    }

    @Override
    public Entity getEntity() {
        return entity;
    }
}
