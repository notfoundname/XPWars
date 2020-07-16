package nfn11.xpwars.special;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class Trampoline extends SpecialItem implements nfn11.xpwars.special.api.Trampoline {
	private int y_velocity;

	public Trampoline(Game game, Player player, Team team, int y_velocity) {
		super(game, player, team);
		this.y_velocity = y_velocity;
	}

	@Override
	public int getYVelocity() {
		return y_velocity;
	}
}
