package nfn11.xpwars.special.api;

import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface RideableProjectile extends SpecialItem {

    boolean isAllowedLeaving();

    boolean isRemoveOnLeave();
    
}
