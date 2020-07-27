package nfn11.xpwars.special;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class ThrowableTNT extends SpecialItem implements nfn11.xpwars.special.api.ThrowableTNT {

    private int fuse_ticks, y_velocity;
    private ItemStack item;

    public ThrowableTNT(Game game, Player player, Team team, int fuse_ticks, int y_velocity, ItemStack item) {
        super(game, player, team);
        this.fuse_ticks = fuse_ticks;
        this.y_velocity = y_velocity;
        this.item = item;
    }

    @Override
    public int getFuseTicks() {
        return fuse_ticks;
    }

    @Override
    public int getYVelocity() {
        return y_velocity;
    }

    @Override
    public void throwTnt() {
        TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(new Location(player.getWorld(),
                player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ()),
                TNTPrimed.class);
        tnt.setFuseTicks(fuse_ticks);
        Vector playerDirection = player.getLocation().getDirection();
        Vector smallerVector = playerDirection.multiply(y_velocity);

        tnt.setVelocity(smallerVector);
        
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
    }

    @Override
    public ItemStack getItem() {
        return item;
    }
    
}
