package org.nfn11.bwaddon.commands;

import java.util.Arrays;
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
            completion.addAll(Arrays.asList("reload"));
        }
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args) {
		if (args.size() == 0) {
			if (args.get(0).equalsIgnoreCase("reload")) {
				Bukkit.getServer().getPluginManager().disablePlugin(BwAddon.getInstance());
		        Bukkit.getServer().getPluginManager().enablePlugin(BwAddon.getInstance());
		        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
		            new org.nfn11.bwaddon.placeholderapi.PlaceholderAPIHook(BwAddon.getInstance()).register();
		            Bukkit.getLogger().info("Succesfully registered PlaceholderAPI!");
		        }
		        sender.sendMessage(BwAddon.getConfigurator().config.getString("messages.commands.reloaded".replace("&", "§")));
			}
		} else {
            sender.sendMessage(BwAddon.getConfigurator().config.getString("usage_bw_admin"));
        }
		
		return true;
	}

}
