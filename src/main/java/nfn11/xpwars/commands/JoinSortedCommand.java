package nfn11.xpwars.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.commands.BaseCommand;
import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.XPWarsUtils;

public class JoinSortedCommand extends BaseCommand {

    public JoinSortedCommand() {
        super("connect", null, false);
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            completion.addAll(XPWarsUtils.getAllCategories());
        }
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) {
        Player player = (Player) sender;
        if (!player.isOp() || !player.hasPermission(ADMIN_PERMISSION)
                || !player.hasPermission(XPWars.getConfigurator().getString("games-gui.permission", "xpwars.gamesgui")))
            return true;
        if (Main.isPlayerInGame(player)) {
            player.sendMessage(i18n("you_are_already_in_some_game"));
            return true;
        }
        if (args.size() >= 1) {
            if (XPWarsUtils.getAllCategories().size() == 0 || !XPWarsUtils.getAllCategories().contains(args.get(0)))
                return true;
            XPWarsUtils.getGameWithHighestPlayersInCategory(args.get(0)).joinToGame(player);
        }
        return true;
    }
    
}
