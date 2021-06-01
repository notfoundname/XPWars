package io.github.notfoundname.xpwars.commands;

import java.util.Arrays;
import java.util.List;

import io.github.notfoundname.xpwars.XPWarsUpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.commands.BaseCommand;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;

import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.utils.XPWarsUtils;

public class XPWarsCommand extends BaseCommand {

    public XPWarsCommand() {
        super("xpwars", ADMIN_PERMISSION, true, true);
    }

    @Override
    public void completeTab(List<String> completion, CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            completion.addAll(Arrays.asList("reload", "open", "updates", "help"));
            if (XPWars.getConfigurator().config.getBoolean("features.kits"))
                completion.add("kits");
        }
        if (args.get(0).equalsIgnoreCase("open")) {
            switch (args.size()) {
                case 2:
                    completion.addAll(XPWarsUtils.getShopFileNames());
                    break;
                case 3:
                    completion.add("debug");
                    if (XPWars.getConfigurator().config.getBoolean("features.level-system", false))
                        completion.add("level");
                    break;
                case 4:
                    if (args.get(2).equalsIgnoreCase("level")
                    || args.get(2).equalsIgnoreCase("debug"))
                        completion.addAll(XPWarsUtils.getOnlinePlayers());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public boolean execute(CommandSender sender, List<String> args) throws IndexOutOfBoundsException {
        switch (args.get(0).toLowerCase()) {
            case "help":
                XPWarsUtils.xpwarsLog(sender, "- Version " + XPWars.getInstance().getDescription().getVersion());
                XPWarsUtils.xpwarsLog(sender, "by &enotfoundname11");
                XPWarsUtils.xpwarsLog(sender, "Available commands:");
                XPWarsUtils.xpwarsLog(sender, "- /bw xpwars help - &eShow this");
                XPWarsUtils.xpwarsLog(sender, "- /bw xpwars reload (or /bw reload) - &eReload addon");
                XPWarsUtils.xpwarsLog(sender, "- /bw xpwars updates - &eCheck for updates");
                XPWarsUtils.xpwarsLog(sender, "- /bw xpwars kits - &eOpen Kits GUI if enabled");
                XPWarsUtils.xpwarsLog(sender, "- /bw xpwars open <shop file> <debug/level> [player name] - " +
                        "&eOpen shop file in debug mode or level (if enabled)");
                XPWarsUtils.xpwarsLog(sender, " ");
                break;
            case "reload":
                Bukkit.getServer().getPluginManager().disablePlugin(XPWars.getInstance());
                Bukkit.getServer().getPluginManager().enablePlugin(XPWars.getInstance());
                XPWarsUtils.xpwarsLog(sender, "&aReloaded!");
                break;
            case "updates":
                XPWarsUpdateChecker.checkForUpdate(sender);
                break;
            case "kits":
                if (!XPWars.getConfigurator().config.getBoolean("features.kits")) {
                    XPWarsUtils.xpwarsLog(sender, i18n("unknown_usage", false));
                    break;
                }
                if (sender instanceof Player && Main.isPlayerInGame((Player) sender) &&
                        Main.getInstance().getGameOfPlayer((Player) sender).getStatus() == GameStatus.WAITING)
                    XPWars.getKitSelectionInventory().openForPlayer((Player) sender);
                break;
            case "open":
                // /bw xpwars open(0)[1] shop.yml(1)[2] playername(2)[3]
                if (XPWarsUtils.getShopFileNames().contains(args.get(1))) {
                    Player player = null;
                    if (args.size() >= 2) {
                        if (args.size() == 3)
                            player = Bukkit.getPlayer(args.get(2));
                        if (args.size() == 2 && sender instanceof Player)
                            player = (Player) sender;
                        if (player == null) {
                            XPWarsUtils.xpwarsLog(sender, "&cInvalid player: &4" + args.get(3));
                            break;
                        }
                        XPWars.getXPWarsInventory().show(player, args.get(1));
                    }
                    break;
                } else XPWarsUtils.xpwarsLog(sender, "&cInvalid shop file: &4" + args.get(1));
                break;
            default:
                XPWarsUtils.xpwarsLog(sender, i18n("unknown_usage", false));
                break;
        }
        return true;
    }
}
