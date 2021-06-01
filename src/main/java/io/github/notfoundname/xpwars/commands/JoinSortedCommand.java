package io.github.notfoundname.xpwars.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.commands.BaseCommand;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;

import io.github.notfoundname.xpwars.utils.XPWarsUtils;

public class JoinSortedCommand extends BaseCommand {
    // /bw xpwars connect <category> [PlayerName]
    public JoinSortedCommand() {
        super("connect", null, true, true);
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        switch(args.size()) {
            case 1:
                completion.addAll(XPWarsUtils.getAllCategories());
                break;
            case 2:
                completion.addAll(XPWarsUtils.getOnlinePlayers());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        switch (args.size()) {
            case 1:
                if (sender instanceof Player) {
                    if (Main.isPlayerInGame((Player) sender)) {
                        sender.sendMessage(i18n("you_are_already_in_some_game"));
                        break;
                    }
                    if (XPWarsUtils.getAllCategories().size() == 0 || !XPWarsUtils.getAllCategories().contains(args.get(0)))
                        break;
                    XPWarsUtils.getGameWithHighestPlayersInCategory(args.get(0)).joinToGame((Player) sender);
                } else break;
                break;
            case 2:
                if (Bukkit.getPlayer(args.get(1)) == null || Main.isPlayerInGame(Bukkit.getPlayer(args.get(1)))) {
                    XPWarsUtils.xpwarsLog(sender, "Cannot connect this player: " + args.get(3));
                    break;
                }
                if (XPWarsUtils.getAllCategories().size() == 0 || !XPWarsUtils.getAllCategories().contains(args.get(0)))
                    break;
                XPWarsUtils.getGameWithHighestPlayersInCategory(args.get(0)).joinToGame((Bukkit.getPlayer(args.get(1))));
        }
        return true;
    }
    
}
