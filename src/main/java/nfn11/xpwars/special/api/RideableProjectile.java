package nfn11.xpwars.special.api;

import org.bukkit.entity.Entity;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface RideableProjectile extends SpecialItem {

    /**
     * @return true if player is allowed to dismount
     */
    boolean isAllowedLeaving();

    /**
     * @return true if projectile will be removed on dismount
     */
    boolean isRemoveOnLeave();

    void run(Entity entity);

    /**
     * @return projectile
     */
    Entity getEntity();
    
}
