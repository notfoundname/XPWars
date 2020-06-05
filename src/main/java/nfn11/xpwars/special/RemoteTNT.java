package nfn11.xpwars.special;

import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class RemoteTNT extends SpecialItem{
	private Game game;
	private Player player;
	private Team team;
	
	public RemoteTNT(Game game, Player player, Team team) {
		super(game, player, team);
		// TODO Auto-generated constructor stub
	}
	
}