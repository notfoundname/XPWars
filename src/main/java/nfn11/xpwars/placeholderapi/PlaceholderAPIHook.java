package nfn11.xpwars.placeholderapi;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.api.statistics.PlayerStatistic;
import org.screamingsandals.bedwars.game.CurrentTeam;
import org.screamingsandals.bedwars.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.XPWarsUtils;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	public PlaceholderAPIHook() {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			PlaceholderAPI.registerPlaceholderHook(this.getIdentifier(), this);
			XPWarsUtils.xpwarsLog("&aSuccesfully registered PlaceholderAPI!");
		}
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
		return "";
	}

	@Override
	public String onPlaceholderRequest(Player player, String parsed) {

		if (player == null)
			return "";

		if (parsed.endsWith("_ingame")) {
			parsed = parsed.replace("_ingame", "");
			if (!BedwarsAPI.getInstance().isGameWithNameExists(parsed))
				return "";

			GameStatus status = BedwarsAPI.getInstance().getGameByName(parsed).getStatus();
			if (status == GameStatus.RUNNING || status == GameStatus.GAME_END_CELEBRATING)
				return Integer.toString(BedwarsAPI.getInstance().getGameByName(parsed).countConnectedPlayers());
			return "0";
		}

		if (parsed.endsWith("_inlobby")) {
			parsed = parsed.replace("_inlobby", "");
			if (!BedwarsAPI.getInstance().isGameWithNameExists(parsed))
				return "";

			if (BedwarsAPI.getInstance().getGameByName(parsed).getStatus() == GameStatus.WAITING)
				return Integer.toString(BedwarsAPI.getInstance().getGameByName(parsed).countConnectedPlayers());
			return "0";
		}

		if (parsed.endsWith("_inall")) {
			parsed = parsed.replace("_inall", "");
			if (!BedwarsAPI.getInstance().isGameWithNameExists(parsed))
				return "";

			org.screamingsandals.bedwars.api.game.Game game = BedwarsAPI.getInstance().getGameByName(parsed);
			return Integer.toString(game.countConnectedPlayers());

		}

		if (parsed.endsWith("_gameworld")) {
			parsed = parsed.replace("_gameworld", "");
			if (!BedwarsAPI.getInstance().isGameWithNameExists(parsed))
				return "";

			org.screamingsandals.bedwars.api.game.Game game = BedwarsAPI.getInstance().getGameByName(parsed);
			return game.getGameWorld().getName();
		}

		if (parsed.endsWith("_lobbyworld")) {
			parsed = parsed.replace("_lobbyworld", "");
			if (!BedwarsAPI.getInstance().isGameWithNameExists(parsed))
				return "";

			org.screamingsandals.bedwars.api.game.Game game = BedwarsAPI.getInstance().getGameByName(parsed);
			return game.getLobbyWorld().getName();
		}

		if (parsed.endsWith("_state")) {
			parsed = parsed.replace("_state", "");
			if (!BedwarsAPI.getInstance().isGameWithNameExists(parsed))
				return "";
			GameStatus status = Main.getGame(parsed).getStatus();

			int gameTime = Main.getGame(parsed).getGameTime();
			int countdown = Main.getGame(parsed).getPauseCountdown();

			if (status == GameStatus.WAITING && Main.getGame(parsed)
					.getMinPlayers() >= Main.getGame(parsed).countConnectedPlayers()) {
				return XPWars.getConfigurator().getString("messages.placeholders.waiting", "waiting");
			}
			if (status == GameStatus.RUNNING) {
				return XPWars.getConfigurator().getString("messages.placeholders.running", "running")
						.replace("%time%", Main.getGame(parsed).getFormattedTimeLeft(gameTime - countdown)
						.replace("%left%", Main.getGame(parsed).getFormattedTimeLeft()));
			}
			if (status == GameStatus.WAITING && Main.getGame(parsed).getMinPlayers() <= Main.getGame(parsed).countConnectedPlayers()) {
				return XPWars.getConfigurator().getString("messages.placeholders.starting", "starting")
						.replace("%left%", Main.getGame(parsed).getFormattedTimeLeft());
			}
			if (status == GameStatus.GAME_END_CELEBRATING) {
				return XPWars.getConfigurator().getString("messages.placeholders.ended", "ended");
			}
			if (status == GameStatus.REBUILDING) {
				return XPWars.getConfigurator().getString("messages.placeholders.rebuilding", "rebuilding");
			}
		}

		if (parsed.endsWith("_mxpl")) {
			parsed = parsed.replace("_mxpl", "");
			if (!Main.isGameExists(parsed))
				return "";

			return Integer.toString(Main.getGame(parsed).getMaxPlayers());
		}

		if (parsed.endsWith("_mnpl")) {
			parsed = parsed.replace("_mnpl", "");
			if (!Main.isGameExists(parsed))
				return "";

			return Integer.toString(Main.getGame(parsed).getMinPlayers());
		}

		if (parsed.endsWith("_tl")) {
			parsed = parsed.replace("_tl", "");
			if (!Main.isGameExists(parsed))
				return "";

			return Main.getGame(parsed).getFormattedTimeLeft();
		}

		if (parsed.endsWith("_tp")) {
			parsed = parsed.replace("_tp", "");
			if (!Main.isGameExists(parsed))
				return "";

			int gameTime = Main.getGame(parsed).getGameTime();
			int countdown = Main.getGame(parsed).getPauseCountdown();

			return Main.getGame(parsed).getFormattedTimeLeft(gameTime - countdown);
		}

		if (parsed.equals("color")) {
			if (!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player))
				return ChatColor.GRAY + "";

			GamePlayer gPlayer = Main.getPlayerGameProfile(player);
			Game game = gPlayer.getGame();
			if (gPlayer.isSpectator)
				return ChatColor.GRAY + "";

			CurrentTeam team = game.getPlayerTeam(gPlayer);
			if (team != null) {
				return team.teamInfo.color.chatColor + "";
			}
			return ChatColor.RESET + "";

		}

		if (parsed.equals("tl")) {
			if (!Main.isPlayerInGame(player))
				return "00:00";

			return Main.getPlayerGameProfile(player).getGame().getFormattedTimeLeft();
		}

		if (parsed.equals("tp")) {
			if (!Main.isPlayerInGame(player))
				return "00:00";

			int gameTime = Main.getGame(parsed).getGameTime();
			int countdown = Main.getGame(parsed).getPauseCountdown();

			return Main.getPlayerGameProfile(player).getGame().getFormattedTimeLeft(gameTime - countdown);
		}
		
		if (parsed.equals("state")) {
			if (!Main.isPlayerInGame(player))
				return "";
			
			Game game = Main.getPlayerGameProfile(player).getGame();
			GameStatus status = game.getStatus();

			int gameTime = game.getGameTime();
			int countdown = game.getPauseCountdown();

			if (status == GameStatus.WAITING && game.getMinPlayers() >= game.countConnectedPlayers()) {
				return XPWars.getConfigurator().getString("messages.placeholders.waiting", "waiting");
			}
			if (status == GameStatus.RUNNING) {
				return XPWars.getConfigurator().getString("messages.placeholders.running", "running")
						.replace("%time%", game.getFormattedTimeLeft(gameTime - countdown)
						.replace("%left%", game.getFormattedTimeLeft()));
			}
			if (status == GameStatus.WAITING && BedwarsAPI.getInstance().getGameByName(parsed)
					.getMinPlayers() <= BedwarsAPI.getInstance().getGameByName(parsed).countConnectedPlayers()) {
				return XPWars.getConfigurator().getString("messages.placeholders.starting", "starting")
						.replace("%left%", Main.getGame(parsed).getFormattedTimeLeft());
			}
			if (status == GameStatus.GAME_END_CELEBRATING) {
				return XPWars.getConfigurator().getString("messages.placeholders.ended", "ended");
			}
			if (status == GameStatus.REBUILDING) {
				return XPWars.getConfigurator().getString("messages.placeholders.rebuilding", "rebuilding");
			}
		}
		
		if (parsed.endsWith("_stats_kills")) {
			parsed = parsed.replace("_stats_kills", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentKills() + stats.getKills());
		}
		if (parsed.endsWith("_stats_deaths")) {
			parsed = parsed.replace("_stats_deaths", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentDeaths() + stats.getDeaths());
		}

		if (parsed.endsWith("_stats_destroyed_beds")) {
			parsed = parsed.replace("_stats_destroyed_beds", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentDestroyedBeds() + stats.getDestroyedBeds());
		}

		if (parsed.endsWith("_stats_loses")) {
			parsed = parsed.replace("_stats_loses", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentLoses() + stats.getLoses());

		}

		if (parsed.endsWith("_stats_score")) {
			parsed = parsed.replace("_stats_score", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentScore() + stats.getScore());
		}

		if (parsed.endsWith("_stats_wins")) {
			parsed = parsed.replace("_stats_wins", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentWins() + stats.getWins());
		}

		if (parsed.endsWith("_stats_games")) {
			parsed = parsed.replace("_stats_games", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentGames() + stats.getGames());
		}

		if (parsed.endsWith("_stats_kd")) {
			parsed = parsed.replace("_stats_loses", "");
			if (!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed)))
				return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Double.toString(stats.getCurrentKD());
		}

		return null;
	}
}
