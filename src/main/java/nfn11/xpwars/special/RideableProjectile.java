package nfn11.xpwars.special;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class RideableProjectile extends SpecialItem implements nfn11.xpwars.special.api.RideableProjectile {
    private boolean isAllowedLeaving, isRemoveOnLeave;

    public RideableProjectile(Game game, Player player, Team team, boolean isAllowedLeaving, boolean isRemoveOnLeave) {
        super(game, player, team);
        this.isAllowedLeaving = isAllowedLeaving;
        this.isRemoveOnLeave = isRemoveOnLeave;
    }

    @Override
    public boolean isAllowedLeaving() {
        return isAllowedLeaving;
    }

    @Override
    public boolean isRemoveOnLeave() {
        return isRemoveOnLeave;
    }
}
