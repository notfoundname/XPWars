package nfn11.xpwars.placeholderapi;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.game.CurrentTeam;
import org.screamingsandals.bedwars.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;

import nfn11.xpwars.XPWars;

public class PlaceholderAPIHook extends me.clip.placeholderapi.expansion.PlaceholderExpansion {

    private XPWars plugin;

    public PlaceholderAPIHook(XPWars plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getAuthor() {
        return "notfoundname11";
    }

    @Override
    public String getIdentifier() {
        return "xpwars";
    }

    @Override
    public String getVersion() {
        return XPWars.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String parsed) {

        if (player == null)
            return "";

        if (parsed.endsWith("_ingame")) {
            parsed = parsed.replace("_ingame", "");
            Game game = Main.getGame(parsed);
            if (game == null)
                return "";
            GameStatus status = game.getStatus();
            if (status == GameStatus.RUNNING || status == GameStatus.GAME_END_CELEBRATING)
                return Integer.toString(Main.getGame(parsed).countConnectedPlayers());
            return "0";
        }

        if (parsed.endsWith("_inlobby")) {
            parsed = parsed.replace("_inlobby", "");
            Game game = Main.getGame(parsed);
            if (game == null)
                return "";
            if (game.getStatus() == GameStatus.WAITING)
                return Integer.toString(game.countConnectedPlayers());
            return "0";
        }

        if (parsed.endsWith("_lobbyworld")) {
            parsed = parsed.replace("_lobbyworld", "");
            if (Main.getGame(parsed) == null)
                return "";
            return Main.getGame(parsed).getLobbyWorld().getName();
        }

        if (parsed.endsWith("_state")) {
            parsed = parsed.replace("_state", "");

            Game game = Main.getGame(parsed);
            if (game == null)
                return "";
            GameStatus status = game.getStatus();
            int gameTime = game.getGameTime();
            int countdown = game.getPauseCountdown();

            switch (status) {
                case WAITING:
                    if (game.getMinPlayers() >= game.countConnectedPlayers())
                        return XPWars.getConfigurator().getString("placeholders.WAITING", "waiting");
                    if (game.getMinPlayers() <= game.countConnectedPlayers())
                        return XPWars.getConfigurator().getString("placeholders.starting", "starting")
                                .replace("%left%", game.getFormattedTimeLeft());
                case RUNNING:
                    return XPWars.getConfigurator().getString("placeholders.RUNNING", "running").replace("%time%",
                            game.getFormattedTimeLeft(gameTime - countdown).replace("%left%", game.getFormattedTimeLeft()));
                case GAME_END_CELEBRATING:
                    return XPWars.getConfigurator().getString("placeholders.GAME_END_CELEBRATING", "ended");
                case REBUILDING:
                    return XPWars.getConfigurator().getString("placeholders.REBUILDING", "rebuilding");
                default:
                    return "null";
            }
        }

        if (parsed.endsWith("_tl")) {
            parsed = parsed.replace("_tl", "");
            if (Main.getGame(parsed) == null)
                return "";
            return Main.getGame(parsed).getFormattedTimeLeft();
        }

        if (parsed.endsWith("_tp")) {
            parsed = parsed.replace("_tp", "");
            Game game = Main.getGame(parsed);
            if (game == null)
                return "";
            return game.getFormattedTimeLeft(game.getGameTime() - game.getPauseCountdown());
        }

        if (Main.isPlayerInGame(player)) {
            GamePlayer gPlayer = Main.getPlayerGameProfile(player);
            Game game = gPlayer.getGame();
            CurrentTeam team = game.getPlayerTeam(gPlayer);
            GameStatus status = game.getStatus();
            int gameTime = game.getGameTime();
            int pauseCountdown = game.getPauseCountdown();
            int countdown = game.getPauseCountdown();

            switch (parsed.toLowerCase()) {
                case "color":
                    if (gPlayer.isSpectator)
                        return ChatColor.GRAY + "";
                    if (team != null)
                        return team.teamInfo.color.chatColor + "";
                    return ChatColor.RESET + "";
                case "state":
                    if (status == GameStatus.WAITING && game.getMinPlayers() <= game.countConnectedPlayers())
                        return XPWars.getConfigurator().getString("placeholders.starting", "starting")
                                .replace("%left%", game.getFormattedTimeLeft());
                    return XPWars.getConfigurator().getString("placeholders." + status.name(), "null");
                default:
                    return "null";
            }
        }
        return null;
    }
    
}
