package io.github.notfoundname.xpwars.api.special;

import org.bukkit.entity.Entity;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface RideableProjectile extends SpecialItem {

    boolean isAllowedLeaving();

    boolean isRemoveOnLeave();

    boolean isSpectatorMode();

    void run(Entity entity);

    Entity getEntity();
    
}
