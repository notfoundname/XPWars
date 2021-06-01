package io.github.notfoundname.xpwars.special;

import io.github.notfoundname.xpwars.XPWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.RunningTeam;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class PortableShop extends SpecialItem implements io.github.notfoundname.xpwars.api.special.PortableShop {
    private int duration;
    private ItemStack itemStack;
    private LivingEntity entity;
    private Location shopLocation;
    private String shopFile, shopName;
    private boolean isEnabledCustomName, isBaby, isAllowedKilling;
    private EntityType entityType;


    public PortableShop(Game game, Player player, Team team, ItemStack itemStack, int duration,
                        EntityType entityType, String shopFile, boolean isEnabledCustomName, String shopName, boolean isAllowedKilling, boolean isBaby,
                        Location shopLocation) {
        super(game, player, team);
        this.duration = duration;
        this.entityType = entityType;
        this.itemStack = itemStack;
        this.shopFile = shopFile;
        this.shopName = shopName;
        this.isEnabledCustomName = isEnabledCustomName;
        this.isAllowedKilling = isAllowedKilling;
        this.isBaby = isBaby;
        this.shopLocation = shopLocation;
    }

    @Override
    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public Location getShopLocation() {
        return shopLocation;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String getShopFile() {
        return shopFile;
    }

    @Override
    public String getShopName() {
        return shopName;
    }

    @Override
    public boolean isEnabledCustomName() {
        return isEnabledCustomName;
    }

    @Override
    public boolean isAllowedKilling() {
        return isAllowedKilling;
    }

    @Override
    public boolean isBaby() {
        return isBaby;
    }

    @Override
    public void run() {
        try {
            entity = (LivingEntity) shopLocation.getWorld().spawnEntity(shopLocation, entityType, CreatureSpawnEvent.SpawnReason.CUSTOM);
        } catch (Throwable throwable) {
            entity = (LivingEntity) shopLocation.getWorld().spawnEntity(shopLocation, entityType);
        }
        entity.setInvulnerable(isAllowedKilling);
        entity.setCustomNameVisible(isEnabledCustomName);
        entity.setCustomName(shopName);
        entity.setSilent(true);
        entity.setRemoveWhenFarAway(false);
        entity.setAI(false);
        if (entity instanceof Ageable && isBaby)
            ((Ageable) entity).setBaby();

        Bukkit.getScheduler().runTaskLaterAsynchronously(XPWars.getInstance(), task -> {
            if (!entity.isDead())
                entity.remove();
            else task.cancel();
        }, duration * 20);
    }

}
