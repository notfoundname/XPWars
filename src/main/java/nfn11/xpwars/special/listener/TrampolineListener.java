package nfn11.xpwars.special.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.APIUtils;
import org.screamingsandals.bedwars.api.events.BedwarsApplyPropertyToBoughtItem;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerBuildBlock;
import nfn11.xpwars.XPWars;
import nfn11.xpwars.special.Trampoline;
import nfn11.xpwars.utils.SpecialItemUtils;

public class TrampolineListener implements Listener {

    private static final String TRAMPOLINE_PREFIX = "Module:Trampoline:";

    @EventHandler
    public void onTrampolineBuy(BedwarsApplyPropertyToBoughtItem event) {
        if (event.getPropertyName().equalsIgnoreCase("trampoline")) {
            ItemStack stack = event.getStack();
            APIUtils.hashIntoInvisibleString(stack, applyProperty(event));
        }
    }

    @EventHandler
    public void onPlace(BedwarsPlayerBuildBlock event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Block block = event.getBlock();

        String unhidden = APIUtils.unhashFromInvisibleStringStartsWith(item, TRAMPOLINE_PREFIX);
        if (unhidden != null) {
            int y_velocity = Integer.parseInt(unhidden.split(":")[2]);

            new Trampoline(event.getGame(), player, event.getTeam(), y_velocity);

            block.setMetadata("y-velocity", new FixedMetadataValue(XPWars.getInstance(), y_velocity));
        }
    }

    @EventHandler
    public void onStep(PlayerMoveEvent event) {
        if (event.isCancelled())
            return;
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (!Main.isPlayerInGame(player))
            return;

        if (location.getBlock().hasMetadata("y-velocity")) {
            location.getBlock().breakNaturally();
            location.getBlock().setType(Material.AIR);
            player.setVelocity(new Vector(player.getVelocity().getX(),
                    location.getBlock().getMetadata("y-velocity").get(0).asInt(), player.getVelocity().getZ()));
        }
    }

    private String applyProperty(BedwarsApplyPropertyToBoughtItem event) {
        return TRAMPOLINE_PREFIX + SpecialItemUtils.getIntFromProperty("y-velocity", XPWars.getConfigurator().config,
                "specials.trampoline.y-velocity", event);
    }
}
