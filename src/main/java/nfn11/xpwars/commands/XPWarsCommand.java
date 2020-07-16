package nfn11.xpwars.commands;

import static org.screamingsandals.bedwars.lib.lang.I.i18nonly;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.commands.BaseCommand;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.XPWarsUtils;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;

public class XPWarsCommand extends BaseCommand {
	
	public XPWarsCommand() {
		super("xpwars", ADMIN_PERMISSION, true);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			if (sender.hasPermission(ADMIN_PERMISSION) || sender.isOp()) {
				completion.addAll(Arrays.asList("reload", "open"));
			}
		}
		if (args.size() == 2) {
			if (!sender.hasPermission(ADMIN_PERMISSION) || !sender.isOp()) {
				return;
			}
			if (args.get(0).equalsIgnoreCase("open")) {
				completion.addAll(XPWarsUtils.getShopFileNames());
			}
		}
		if (args.size() == 3) {
			if (!sender.hasPermission(ADMIN_PERMISSION) || !sender.isOp()) {
				return;
			}
			if (args.get(0).equalsIgnoreCase("open")) {
				completion.addAll(XPWarsUtils.getOnlinePlayers());
			}
		}
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws IndexOutOfBoundsException {
		if (!sender.hasPermission(ADMIN_PERMISSION)) {
			sender.sendMessage(i18n("no_permissions"));
			return true;
		}
		if (args.size() == 1) {
			if (args.get(0).equalsIgnoreCase("reload")) {
				Bukkit.getServer().getPluginManager().disablePlugin(XPWars.getInstance());
				Bukkit.getServer().getPluginManager().enablePlugin(XPWars.getInstance());
				sender.sendMessage("Addon reloaded!");
				return true;
			}
		}
		if (args.size() == 2 || args.size() == 3) {
			if (args.get(0).equalsIgnoreCase("open")) {
				if (XPWarsUtils.getShopFileNames().contains(args.get(1))) {

					Player player = null;

					if (args.size() == 3) {
						player = Bukkit.getPlayer(args.get(2));
					} else if (args.size() == 2) {
						if (sender instanceof Player) {
							player = (Player) sender;
						}
					}
					if (player == null) {
						sender.sendMessage((args.size() == 3 ? args.get(2) : "Console") + "is not a valid player!");
						return true;
					}
					GameStore store = new GameStore(null, args.get(1), false, i18nonly("item_shop_name", "[BW] Shop"),
							false, false);
					if (XPWars.getConfigurator().config.getBoolean("features.level-system")
							|| !Main.isPlayerInGame(player)) {
						XPWars.getLevelShop().show(player, store);
					} else {
						if (!Main.getPlayerGameProfile(player).isSpectator
								&& Main.getPlayerGameProfile(player).getGame().getStatus() == GameStatus.RUNNING) {
							XPWars.getShopInventory().show(player, store);
						}
					}
					return true;
				} else
					sender.sendMessage("Invalid shop file: " + args.get(1));
				return true;
			}
		} else
			sender.sendMessage(i18n("unknown_usage"));
		return true;
	}
}
