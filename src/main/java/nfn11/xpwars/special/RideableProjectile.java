package nfn11.xpwars.special;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class RideableProjectile extends SpecialItem implements nfn11.xpwars.special.api.RideableProjectile {
    
	public RideableProjectile(Game game, Player player, Team team) {
		super(game, player, team);
	}

}
