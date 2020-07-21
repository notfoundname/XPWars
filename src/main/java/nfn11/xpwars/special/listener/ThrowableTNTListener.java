package nfn11.xpwars.special.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.game.GamePlayer;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.special.ThrowableTNT;
import nfn11.xpwars.utils.SpecialItemUtils;

public class ThrowableTNTListener implements Listener {

    private static final String THROWABLE_TNT_PREFIX = "Module:ThrowableTnt:";

    @EventHandler
    public void onThrowableTntBuy(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("throwabletnt")) {
            ItemStack stack = event.getStack();
            APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
        }
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onUseItem(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (!Main.isPlayerInGame(player))
            return;

        if (event.isCancelled())
            return;

        if (item == null)
            return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(item, THROWABLE_TNT_PREFIX);
            if (unhidden != null) {
                GamePlayer gp = Main.getPlayerGameProfile(player);
                int fuse_ticks = Integer.parseInt(unhidden.split(":")[2]);
                int velocity = Integer.parseInt(unhidden.split(":")[3]);

                new ThrowableTNT(gp.getGame(), player, gp.getGame().getPlayerTeam(gp), fuse_ticks, velocity);

                TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(new Location(player.getWorld(),
                        player.getLocation().getX(), player.getLocation().getY() + 1, player.getLocation().getZ()),
                        TNTPrimed.class);
                tnt.setFuseTicks(fuse_ticks);
                Vector playerDirection = player.getLocation().getDirection();
                Vector smallerVector = playerDirection.multiply(velocity);

                tnt.setVelocity(smallerVector);

                if (item.getAmount() == 1) {
                    player.getInventory().remove(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
                event.setCancelled(true);
            }
        }
    }

    private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
        return THROWABLE_TNT_PREFIX
                + SpecialItemUtils.getIntFromProperty("fuse-ticks", XPWars.getConfigurator().config,
                        "specials.throwable-tnt.fuse-ticks", event)
                + ":" + SpecialItemUtils.getIntFromProperty("velocity", XPWars.getConfigurator().config,
                        "specials.throwable-tnt.velocity", event);
    }
}
