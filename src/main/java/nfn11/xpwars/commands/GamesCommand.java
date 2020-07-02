package nfn11.xpwars.commands;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.commands.BaseCommand;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.inventories.GamesInventory;

public class GamesCommand extends BaseCommand {
	public GamesCommand() {
		super("games", null, false);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (!sender.isOp() || !sender.hasPermission(ADMIN_PERMISSION) || !sender
				.hasPermission(XPWars.getConfigurator().getString("games-gui.permission", "xpwars.gamesgui"))) {
			return;
		}
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) {
		if (!sender.isOp() || !sender.hasPermission(ADMIN_PERMISSION) || !sender
				.hasPermission(XPWars.getConfigurator().getString("games-gui.permission", "xpwars.gamesgui"))) {
			return true;
		}

		if (sender instanceof Player) {
			GamesInventory inv = new GamesInventory();
			inv.openForPlayer((Player) sender);
		}
		return true;
	}
}
