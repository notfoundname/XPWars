package com.notfoundname.xpwars.api.features.specials;

import org.bukkit.entity.LivingEntity;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface RideableProjectile extends SpecialItem {

    LivingEntity getEntity();

    boolean isAllowLeaving();

    boolean isRemoveOnLeave();

    void run();

}
