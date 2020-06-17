package nfn11.xpwars.commands;

import static org.screamingsandals.bedwars.lib.lang.I.i18nonly;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.commands.BaseCommand;
import org.screamingsandals.bedwars.inventories.ShopInventory;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.inventories.LevelShop;

public class XPWarsCommand extends BaseCommand {
	public XPWarsCommand() {
		super("xpwars", ADMIN_PERMISSION, true);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			completion.addAll(Arrays.asList("reload", "open", "help"));
		}
		if (args.size() == 2) {
			if (args.get(0).equalsIgnoreCase("open")) {
				completion.addAll(XPWars.getShopFileNames());
			}
		}
		if (args.size() == 3) {
			if (args.get(0).equalsIgnoreCase("open")) {
				completion.addAll(XPWars.getOnlinePlayers());
			}
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
					sender.sendMessage("/bw xpwars open <store name> [player] - Open shop");
				}
				return true;
			}
		}
		if (args.size() >= 2 && args.size() >= 3) {
			if (args.get(0).equalsIgnoreCase("open")) {
				if (XPWars.getShopFileNames().contains(args.get(1))) {

					GameStore store = new GameStore(null, args.get(1), false, i18nonly("item_shop_name", "[BW] Shop"),
							false);

					Player player = null;
					if (!args.get(2).isEmpty()) {
						player = Bukkit.getPlayer(args.get(2));
					} else {
						if (sender instanceof Player) {
							player = (Player) sender;
						}
					}
					if (player == null) {
						sender.sendMessage("player does not exist");
						return true;
					}
					if (XPWars.getConfigurator().getBoolean("level.enable", true) || !Main.isPlayerInGame(player)) {
						LevelShop shop = new LevelShop();
						shop.show(player, store);
					} else {
						if (!Main.getPlayerGameProfile(player).isSpectator) {
							ShopInventory shop = new ShopInventory();
							shop.show(player, store);
						}
					}
				} else
					sender.sendMessage(XPWars.getConfigurator()
							.getString("messages.commands.nostore", "[XPWars] &cInvalid shop file: %file%!")
							.replace("%file%", args.get(1)));
				return true;
			}
		} else
			sender.sendMessage(XPWars.getConfigurator().getString("messages.commands.unknown",
					"[XPWars] &cUnknown command or wrong usage!"));
		return true;
	}
}
