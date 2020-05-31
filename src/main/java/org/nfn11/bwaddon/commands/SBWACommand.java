package org.nfn11.bwaddon.commands;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.nfn11.bwaddon.BwAddon;
import org.nfn11.bwaddon.utils.SBWAUtils;
import org.screamingsandals.bedwars.commands.BaseCommand;

public class SBWACommand extends BaseCommand {
	public SBWACommand() {
		super("sbwa", ADMIN_PERMISSION, true);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			completion.addAll(Arrays.asList("reload", "shop"));
		}
		if (args.size() == 2 && args.get(1).equals("shop")) {
			completion.addAll(Arrays.asList("open"));
		}
		if (args.size() == 3 && args.get(2).equals("open")) {
			completion.addAll(SBWAUtils.getStores());
		}
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) throws IndexOutOfBoundsException {
		if (args.size() == 1) {
			if (args.get(0).equalsIgnoreCase("reload")) {
				if (!sender.hasPermission(ADMIN_PERMISSION)) {
					sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.noperm", "[SBWA] &cYou don't have permission!"));
				} else {
					Bukkit.getServer().getPluginManager().disablePlugin(BwAddon.getInstance());
					Bukkit.getServer().getPluginManager().enablePlugin(BwAddon.getInstance());
					sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.reloaded", "[SBWA] &aReloaded!"));
					return true;
				}
			}
			if (args.get(0).equalsIgnoreCase("shop")) {
				sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.unknown", "[SBWA] &cUnknown command or wrong usage!"));
			}
		} 
		if (args.size() == 2 && args.get(0).equalsIgnoreCase("shop")) {
			if (args.get(1).equalsIgnoreCase("open")) {
					
			}
			
		}
		else sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.unknown", "[SBWA] &cUnknown command or wrong usage!"));
		
		return true;
	}
}
