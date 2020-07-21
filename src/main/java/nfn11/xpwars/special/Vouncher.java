package nfn11.xpwars.special;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class Vouncher extends SpecialItem implements nfn11.xpwars.special.api.Vouncher {
    private int levels;

    public Vouncher(Game game, Player player, Team team, int levels) {
        super(game, player, team);
        this.levels = levels;
    }

    @Override
    public int getLevels() {
        return levels;
    }

}
