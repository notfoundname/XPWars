package nfn11.xpwars.special;

import nfn11.xpwars.XPWars;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.game.GameStore;
import org.screamingsandals.bedwars.special.SpecialItem;

public class PortableShop extends SpecialItem implements nfn11.xpwars.special.api.PortableShop {
    private int duration;
    private ItemStack stack;
    private LivingEntity entity;
    private GameStore store;

    public PortableShop(Game game, Player player, Team team, int duration, ItemStack stack, GameStore store) {
        super(game, player, team);
        this.duration = duration;
        this.stack = stack;
        this.store = store;
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
        return stack;
    }

    @Override
    public void run() {
        entity = store.spawn();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isDead()) {
                    entity = store.kill();
                    entity = null;
                } else this.cancel();
            }
        }.runTaskLaterAsynchronously(XPWars.getInstance(), duration * 20);
    }

}
