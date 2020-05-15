package org.nfn11.bwaddon.commands;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.nfn11.bwaddon.BwAddon;
import org.screamingsandals.bedwars.commands.BaseCommand;

public class SBWACommand extends BaseCommand {

	public SBWACommand() {
		super("sbwa", ADMIN_PERMISSION, true);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			completion.add("reload");
		}
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) {
		if (args.size() >= 1) {
			if (args.get(1).equals("reload")) {
				if (!sender.hasPermission(ADMIN_PERMISSION)) {
					sender.sendMessage(BwAddon.getConfigurator().config.getString("messages.commands.reloaded".replace("&", "§")));
					return true;
				}
				Bukkit.getServer().getPluginManager().disablePlugin(BwAddon.getInstance());
				Bukkit.getServer().getPluginManager().enablePlugin(BwAddon.getInstance());
				if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
					new org.nfn11.bwaddon.placeholderapi.PlaceholderAPIHook(BwAddon.getInstance()).register();
					Bukkit.getLogger().info("Succesfully registered PlaceholderAPI!");
				}
				sender.sendMessage(BwAddon.getConfigurator().config.getString("messages.commands.reloaded".replace("&", "§")));
			} else {
				sender.sendMessage(BwAddon.getConfigurator().config.getString("messages.commands.unknown".replace("&", "§")));
			}
		}
		return true;
	}

}
