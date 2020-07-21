package nfn11.xpwars.special.api;

import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface ThrowableTNT extends SpecialItem {
    
    /**
     * @return Fuse ticks
     */
    int getFuseTicks();

    /**
     * @return How far will TNT throw
     */
    int getYVelocity();
    
}
