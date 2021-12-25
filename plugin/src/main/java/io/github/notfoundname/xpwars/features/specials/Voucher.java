package io.github.notfoundname.xpwars.features.specials;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class Voucher extends SpecialItem implements io.github.notfoundname.xpwars.api.features.specials.Voucher {

    private final int level;

    public Voucher(Game game, Player player, Team team, int level) {
        super(game, player, team);
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }
}
