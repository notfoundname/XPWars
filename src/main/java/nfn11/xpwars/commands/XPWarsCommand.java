package nfn11.xpwars.commands;

import static org.screamingsandals.bedwars.lib.lang.I.i18nonly;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.events.BedwarsOpenShopEvent;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.commands.BaseCommand;
import nfn11.xpwars.XPWars;

public class XPWarsCommand extends BaseCommand {
	public XPWarsCommand() {
		super("xpwars", ADMIN_PERMISSION, true);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			completion.addAll(Arrays.asList("reload", "open", "help"));
		}
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws IndexOutOfBoundsException {
		if (args.size() == 1) {
			if (args.get(0).equalsIgnoreCase("reload")) {
				if (!sender.hasPermission(ADMIN_PERMISSION)) {
					sender.sendMessage(XPWars.getConfigurator().getString("messages.commands.noperm",
							"[XPWars] &cYou don't have permission!"));
				} else {
					Bukkit.getServer().getPluginManager().disablePlugin(XPWars.getInstance());
					Bukkit.getServer().getPluginManager().enablePlugin(XPWars.getInstance());
					sender.sendMessage(
							XPWars.getConfigurator().getString("messages.commands.reloaded", "[XPWars] &aReloaded!"));
					return true;
				}
			}
			if (args.get(0).equalsIgnoreCase("help")) {
				if (!sender.hasPermission(ADMIN_PERMISSION)) {
					sender.sendMessage(XPWars.getConfigurator().getString("messages.commands.noperm",
							"[XPWars] &cYou don't have permission!"));
				} else {
					sender.sendMessage("[XPWars] - Version "
							+ Bukkit.getServer().getPluginManager().getPlugin("XPWars").getDescription().getVersion());
					sender.sendMessage("Available commands:");
					sender.sendMessage("/bw xpwars reload - Reload the addon");
					sender.sendMessage("/bw xpwars help - Show this");
					sender.sendMessage("/bw xpwars open <store name> - Open shop");
				}
			}
		}
		if (args.size() == 2) {
			if (args.get(0).equalsIgnoreCase("open")) {
				if (XPWars.getShopFileNames().contains(args.get(1))) {
					GameStore store = new GameStore(null, args.get(1), false, i18nonly("item_shop_name", "[BW] Shop"),
							false);
					BedwarsOpenShopEvent event = new BedwarsOpenShopEvent(null, (Player) sender, store, null);
					Bukkit.getServer().getPluginManager().callEvent(event);
				} else
					sender.sendMessage(XPWars.getConfigurator().getString("messages.commands.nostore",
							"[XPWars] &cShop file does not exist or contains errors!"));
			}
		} else
			sender.sendMessage(XPWars.getConfigurator().getString("messages.commands.unknown",
					"[XPWars] &cUnknown command or wrong usage!"));

		return true;
	}
}
