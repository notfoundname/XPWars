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

import nfn11.xpwars.XPWars;
import nfn11.xpwars.special.RemoteTNT;
import nfn11.xpwars.utils.SpecialItemUtils;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

public class RemoteTNTListener implements Listener {

    private static final String REMOTE_TNT_PREFIX = "Module:RemoteTnt:";
    final List<Location> locations = new ArrayList<>();

    @EventHandler
    public void onRemoteTntBuy(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("remotetnt"))
            APIUtils.hashIntoInvisibleString(event.getStack(), applyProperty(event));
    }

    @EventHandler
    public void onTntPlace(BedwarsPlayerBuildBlock event) {
        Player player = event.getPlayer();
        assert !event.isCancelled();

        ItemStack tnt = event.getItemInHand();
        String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(tnt, REMOTE_TNT_PREFIX);
        if (unhidden != null) {
            int fuse_ticks = Integer.parseInt(unhidden.split(":")[2]);
            Block block = event.getBlock();
            RemoteTNT special = new RemoteTNT(event.getGame(), player, event.getTeam(), fuse_ticks, block, locations);
            special.addBlockToLocations();
            ItemStack detonator = detonator();
            if (!player.getInventory().contains(detonator))
                player.getInventory().addItem(detonator);
        }
    }

    @EventHandler
    public void onDetonatorUsage(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack detonator = detonator();
        assert Main.isPlayerInGame(player) && event.getItem() != null && event.getItem().equals(detonator);
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            for (Location location : locations) {
                int ticks = location.getBlock().getMetadata("ticks").get(0).asInt();
                location.getBlock().setType(Material.AIR);
                TNTPrimed tnt = location.getWorld().spawn(location.add(0.5, 0.0, 0.5), TNTPrimed.class);
                Main.getPlayerGameProfile(player).getGame().getConnectedPlayers().forEach(players -> {
                    if (ticks >= 30)
                        players.playSound(tnt.getLocation(), Sound.ENTITY_TNT_PRIMED, 1.0F, 1.0F);
                });
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
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Player player = event.getEntity() instanceof Player ? (Player) event.getEntity() : null;
        assert player != null && Main.isPlayerInGame(player);
        if (event.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getDamager();
            if (tnt.hasMetadata("owner")) {
                event.setCancelled(
                        tnt.getMetadata("owner").get(0).asString().equals(player.getUniqueId().toString()));
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
