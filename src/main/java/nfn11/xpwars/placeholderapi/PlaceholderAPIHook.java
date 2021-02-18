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
        return Float.toString(XPWars.getVersion());
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
            Game game = Main.getGame(parsed.replace("_ingame", ""));
            return game != null ?
                    game.getStatus() == GameStatus.RUNNING || game.getStatus() == GameStatus.GAME_END_CELEBRATING ?
                            Integer.toString(Main.getGame(parsed).countConnectedPlayers()) : "0" : "";
        }

        if (parsed.endsWith("_inlobby")) {
            Game game = Main.getGame(parsed.replace("_inlobby", ""));
            return game != null ?
                    game.getStatus() == GameStatus.WAITING ?
                            Integer.toString(game.countConnectedPlayers()) : "" : "";
        }

        if (parsed.endsWith("_lobbyworld")) {
            parsed = parsed.replace("_lobbyworld", "");
            return Main.getGame(parsed) != null ?
                    Main.getGame(parsed).getLobbyWorld().getName() : "";
        }

        if (parsed.endsWith("_state")) {
            Game game = Main.getGame(parsed.replace("_state", ""));
            assert game != null;

            GameStatus status = game.getStatus();
            int gameTime = game.getGameTime();
            int countdown = game.getPauseCountdown();

            switch (status) {
                case WAITING:
                    if (game.getMinPlayers() >= game.countConnectedPlayers())
                        return ChatColor.translateAlternateColorCodes
                                ('&', XPWars.getConfigurator().config.getString("placeholders.WAITING", "waiting"));
                    if (game.getMinPlayers() <= game.countConnectedPlayers())
                        return ChatColor.translateAlternateColorCodes('&',
                                XPWars.getConfigurator().config.getString("placeholders.starting", "starting")
                                .replace("%left%", game.getFormattedTimeLeft()));
                case RUNNING:
                    return ChatColor.translateAlternateColorCodes('&',
                            XPWars.getConfigurator().config.getString("placeholders.RUNNING", "running")
                                    .replace("%time%",
                                            game.getFormattedTimeLeft(gameTime - countdown).replace("%left%", game.getFormattedTimeLeft())));
                case GAME_END_CELEBRATING:
                    return ChatColor.translateAlternateColorCodes
                            ('&', XPWars.getConfigurator().config.getString("placeholders.GAME_END_CELEBRATING", "ended"));
                case REBUILDING:
                    return XPWars.getConfigurator().config.getString("placeholders.REBUILDING", "rebuilding");
                default:
                    return "null";
            }
        }

        if (parsed.endsWith("_tl")) {
            parsed = parsed.replace("_tl", "");
            return Main.getGame(parsed) != null ? Main.getGame(parsed).getFormattedTimeLeft() : "";
        }

        if (parsed.endsWith("_tp")) {
            Game game = Main.getGame(parsed.replace("_tp", ""));
            return game != null ? game.getFormattedTimeLeft(game.getGameTime() - game.getPauseCountdown()) : "";
        }

        if (Main.isPlayerInGame(player)) {
            GamePlayer gPlayer = Main.getPlayerGameProfile(player);
            Game game = gPlayer.getGame();
            CurrentTeam team = game.getPlayerTeam(gPlayer);
            GameStatus status = game.getStatus();

            switch (parsed.toLowerCase()) {
                case "color":
                    return gPlayer.isSpectator ?
                            ChatColor.GRAY + "" : team != null ?
                            team.teamInfo.color.chatColor + "" : ChatColor.RESET + "";
                case "state":
                    if (status == GameStatus.WAITING && game.getMinPlayers() <= game.countConnectedPlayers())
                        return ChatColor.translateAlternateColorCodes('&',
                                XPWars.getConfigurator().config.getString("placeholders.starting", "starting")
                                        .replace("%left%", game.getFormattedTimeLeft()));
                    return ChatColor.translateAlternateColorCodes('&',
                            XPWars.getConfigurator().config.getString("placeholders." + status.name(), "null"));
                default:
                    return "";
            }
        }
        return null;
    }
    
}
