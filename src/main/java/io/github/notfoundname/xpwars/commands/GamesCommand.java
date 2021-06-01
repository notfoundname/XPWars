package io.github.notfoundname.xpwars.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.commands.BaseCommand;

import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.utils.XPWarsUtils;

public class GamesCommand extends BaseCommand {
    public GamesCommand() {
        super("games", Collections.singletonList("xpwars.gamesgui"), true, true);
    }

    // /bw xpwars games [playername]
    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        if (!sender.isOp() || BaseCommand.hasPermission(sender, ADMIN_PERMISSION, false))
            return;
        if (args.size() == 1)
            completion.addAll(XPWarsUtils.getOnlinePlayers());
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            if (Bukkit.getServer().getPlayer(args.get(0)) != null)
                XPWars.getGamesInventory().openForPlayer(Bukkit.getServer().getPlayer(args.get(0)));
            return true;
        }
        if (sender instanceof Player)
            XPWars.getGamesInventory().openForPlayer((Player) sender);
        return true;
    }
    
}
