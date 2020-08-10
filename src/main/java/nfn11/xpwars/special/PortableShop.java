package nfn11.xpwars.special;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.special.SpecialItem;
import org.screamingsandals.lib.nms.entity.EntityUtils;

import nfn11.xpwars.XPWars;

public class PortableShop extends SpecialItem implements nfn11.xpwars.special.api.PortableShop {
    private int duration;
    private ItemStack item;
    private GameStore store;

    public PortableShop(Game game, Player player, Team team, GameStore store, int duration, ItemStack item) {
        super(game, player, team);
        this.store = store;
        this.duration = duration;
        this.item = item;
    }
    
    @Override
    public GameStore getGameStore() {
        return store;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void run() {
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            try {
                if (player.getInventory().getItemInOffHand().equals(item)) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                } else {
                    player.getInventory().remove(item);
                }
            } catch (Throwable e) {
                player.getInventory().remove(item);
            }
        }
        player.updateInventory();

        LivingEntity entity = store.spawn();
        EntityUtils.disableEntityAI(entity);
        entity.setMetadata(player.getUniqueId().toString(), new FixedMetadataValue(XPWars.getInstance(), null));
        entity.setMetadata("portable-shop", new FixedMetadataValue(XPWars.getInstance(), null));
        
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isDead() || entity == null)
                    return;
                entity.remove();
            }
        }.runTaskLater(XPWars.getInstance(), duration * 20);
    }
    
}
