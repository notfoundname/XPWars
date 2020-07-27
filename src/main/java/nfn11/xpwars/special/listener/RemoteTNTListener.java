package nfn11.xpwars.special.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerBuildBlock;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.special.RemoteTNT;
import nfn11.xpwars.utils.SpecialItemUtils;

public class RemoteTNTListener implements Listener {

    private static final String REMOTE_TNT_PREFIX = "Module:RemoteTnt:";
    final List<Location> locations = new ArrayList<Location>();

    @EventHandler
    public void onRemoteTntBuy(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("remotetnt")) {
            ItemStack stack = event.getStack();
            APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
        }
    }

    @EventHandler
    public void onTntPlace(BedwarsPlayerBuildBlock event) {
        Player player = event.getPlayer();

        if (event.isCancelled())
            return;

        ItemStack tnt = event.getItemInHand();
        String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(tnt, REMOTE_TNT_PREFIX);
        if (unhidden != null) {
            int fuse_ticks = Integer.parseInt(unhidden.split(":")[2]);
            Block block = event.getBlock();
            RemoteTNT special = new RemoteTNT(event.getGame(), player, event.getTeam(), fuse_ticks, block, locations);
            special.addBlockToLocations();
            ItemStack detonator = detonator();
            if (!player.getInventory().contains(detonator)) {
                player.getInventory().addItem(detonator);
            }
        } else
            return;
    }

    @EventHandler
    public void onDetonatorUsage(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!Main.isPlayerInGame(player))
            return;
        ItemStack detonator = detonator();
        if (!(event.getItem() == null)) {
            if (event.getItem().equals(detonator)
                    && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
                for (Location location : locations) {
                    int ticks = location.getBlock().getMetadata("ticks").get(0).asInt();
                    location.getBlock().setType(Material.AIR);
                    TNTPrimed tnt = (TNTPrimed) location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
                    for (Player players : Main.getPlayerGameProfile(player).getGame().getConnectedPlayers()) {
                        if (ticks >= 30) {
                            players.playSound(tnt.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0F, 1.0F);
                        }
                    }
                    Main.registerGameEntity(tnt, Main.getPlayerGameProfile(player).getGame());
                    tnt.setFuseTicks(ticks);
                    tnt.setMetadata("owner",
                            new FixedMetadataValue(XPWars.getInstance(), player.getUniqueId().toString()));
                    location.getBlock().setType(Material.AIR);
                    locations.remove(location);
                }
                event.setCancelled(true);
                player.getInventory().remove(detonator);
            }
        } else
            return;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        if (!Main.isPlayerInGame(player))
            return;
        if (event.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getDamager();
            if (tnt.hasMetadata("owner")) {
                event.setCancelled(
                        tnt.getMetadata("owner").get(0).asString().equals(player.getUniqueId().toString()) ? true
                                : false);
            }
        }
    }

    private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
        return REMOTE_TNT_PREFIX + SpecialItemUtils.getIntFromProperty("fuse-ticks", XPWars.getConfigurator().config,
                "specials.remote-tnt.fuse-ticks", event);
    }

    private ItemStack detonator() {
        return StackParser
                .parse(XPWars.getConfigurator().config.get("specials.remote-tnt.detonator-itemstack", "TRIPWIRE_HOOK"));
    }
}
