package nfn11.xpwars.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.commands.BaseCommand;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;

import nfn11.xpwars.utils.XPWarsUtils;

public class JoinSortedCommand extends BaseCommand {
    // /bw xpwars connect example [PlayerName]
    public JoinSortedCommand() {
        super("connect", null, true, true);
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        if (args.size() == 1) completion.addAll(XPWarsUtils.getAllCategories());
        if (args.size() == 2) completion.addAll(XPWarsUtils.getOnlinePlayers());
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        Player player;
        if (args.size() == 1 && sender instanceof Player) {
            player = (Player) sender;
            if (Main.isPlayerInGame(player)) {
                player.sendMessage(i18n("you_are_already_in_some_game"));
                return true;
            }
            if (XPWarsUtils.getAllCategories().size() == 0 || !XPWarsUtils.getAllCategories().contains(args.get(0)))
                return true;
            XPWarsUtils.getGameWithHighestPlayersInCategory(args.get(0)).joinToGame(player);
        } else if (args.size() == 2) {
            player = Bukkit.getPlayer(args.get(1));
            if (player == null) {
                XPWarsUtils.xpwarsLog(sender, "Invalid player: " + args.get(3));
                return true;
            }
            if (Main.isPlayerInGame(player)) {
                player.sendMessage(i18n("you_are_already_in_some_game"));
                return true;
            }
            if (XPWarsUtils.getAllCategories().size() == 0 || !XPWarsUtils.getAllCategories().contains(args.get(0)))
                return true;
            XPWarsUtils.getGameWithHighestPlayersInCategory(args.get(0)).joinToGame(player);
        }
        return true;
    }
    
}
