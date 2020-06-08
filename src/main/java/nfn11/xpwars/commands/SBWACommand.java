package nfn11.xpwars.commands;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.screamingsandals.bedwars.commands.BaseCommand;
import nfn11.xpwars.XPWars;

public class SBWACommand extends BaseCommand {
	public SBWACommand() {
		super("xpwars", ADMIN_PERMISSION, true);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			completion.addAll(Arrays.asList("reload"));
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
					sender.sendMessage("[XPWars] - Version " + Bukkit.getServer().getPluginManager().getPlugin("XPWars").getDescription().getVersion());
					sender.sendMessage("Available commands:");
					sender.sendMessage("/bw xpwars reload - Reload the addon");
					sender.sendMessage("/bw xpwars help - Show this");
					sender.sendMessage("/bw xpwars level disable <arena> - Disable level system in arena");
				}
			}
		} else
			sender.sendMessage(XPWars.getConfigurator().getString("messages.commands.unknown",
					"[XPWars] &cUnknown command or wrong usage!"));

		return true;
	}
}
