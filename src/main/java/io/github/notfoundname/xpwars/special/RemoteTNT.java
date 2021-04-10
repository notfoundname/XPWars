package io.github.notfoundname.xpwars.special;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

import io.github.notfoundname.xpwars.XPWars;

public class RemoteTNT extends SpecialItem implements io.github.notfoundname.xpwars.api.special.RemoteTNT {
    private int fuse_ticks;
    private Block block;
    private List<Location> locations;

    public RemoteTNT(Game game, Player player, Team team, int fuse_ticks, Block block, List<Location> locations) {
        super(game, player, team);
        this.fuse_ticks = fuse_ticks;
        this.block = block;
    }

    @Override
    public int getFuseTicks() {
        return fuse_ticks;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    @Override
    public List<Location> getLocations() {
        return locations;
    }

    @Override
    public void addBlockToLocations() {
        block.setMetadata("owner", new FixedMetadataValue(XPWars.getInstance(), player.getUniqueId().toString()));
        block.setMetadata("ticks", new FixedMetadataValue(XPWars.getInstance(), fuse_ticks));
        locations.add(block.getLocation());
    }
}