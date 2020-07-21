package nfn11.xpwars.special.api;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.screamingsandals.bedwars.api.special.SpecialItem;

public interface PortableShop extends SpecialItem {
    
    Location getLocation();

    String getShopFile();

    boolean isUsesParent();

    LivingEntity getEntity();

    String getShopName();

    boolean isEnabledCustomName();

    boolean isBaby();

    int getDuration();

}
