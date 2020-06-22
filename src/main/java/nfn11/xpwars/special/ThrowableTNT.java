package nfn11.xpwars.special;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class ThrowableTNT extends SpecialItem implements nfn11.xpwars.special.api.ThrowableTNT {
	
	private int fuse_ticks, y_velocity;
	
	public ThrowableTNT(Game game, Player player, Team team) {
		super(game, player, team);
	}

	@Override
	public int getFuseTicks() {
		return fuse_ticks;
	}

	@Override
	public int getYVelocity() {
		return y_velocity;
	}
}
