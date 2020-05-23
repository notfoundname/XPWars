package org.nfn11.bwaddon.commands;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.nfn11.bwaddon.BwAddon;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.commands.BaseCommand;

public class SBWACommand extends BaseCommand {
	private List<String> files;
	public SBWACommand() {
		super("sbwa", ADMIN_PERMISSION, true);
	}

	@Override
	public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
		if (args.size() == 1) {
			completion.addAll(Arrays.asList("reload", "openstore"));
			if (args.size() == 2 && args.get(0).equalsIgnoreCase("openstore")) {		
				try {
					files = Files.readAllLines(Main.getConfigurator().dataFolder.toPath(), Charset.defaultCharset());
					
					files.remove("config");
					files.remove("record");
					files.remove("sign");
					files.remove("stats");
					
					completion.addAll(files);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

	@Override
	public boolean execute(CommandSender sender, List<String> args)  throws IndexOutOfBoundsException  {
		if (args.size() == 1) {
			if (args.get(0).equals("reload")) {
				if (!sender.hasPermission(ADMIN_PERMISSION)) {
					sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.noperm"));
				} else {
					Bukkit.getServer().getPluginManager().disablePlugin(BwAddon.getInstance());
					Bukkit.getServer().getPluginManager().enablePlugin(BwAddon.getInstance());
					sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.reloaded"));
				}
			} else sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.unknown"));
		} else sender.sendMessage(BwAddon.getConfigurator().getString("messages.commands.unknown"));
		
		return true;
	}
}
