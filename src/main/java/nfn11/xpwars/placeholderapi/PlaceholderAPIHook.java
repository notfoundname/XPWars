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
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class PlaceholderAPIHook extends PlaceholderExpansion {

	nfn11.xpwars.XPWars plugin;
    public PlaceholderAPIHook(nfn11.xpwars.XPWars plugin) {
    	this.plugin = plugin;
    }
    
	@Override
	public String getAuthor() {
		return "notfoundname11";
	}

	@Override
	public String getIdentifier() {
		return "sbwa";
	}

	@Override
	public String getVersion() {
		return "";
	}

	@Override
	public String onPlaceholderRequest(Player player, String parsed) {

		if(player == null) return "";
		
		if(parsed.endsWith("_ingame")) {
			parsed = parsed.replace("_ingame", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			
			GameStatus status = BedwarsAPI.getInstance().getGameByName(parsed).getStatus();
			if(status==GameStatus.RUNNING || status==GameStatus.GAME_END_CELEBRATING) 
        		return Integer.toString(BedwarsAPI.getInstance().getGameByName(parsed).countConnectedPlayers());
        	return "0";
		}
		
		if(parsed.endsWith("_inlobby")) {
			parsed = parsed.replace("_inlobby", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			
			if(BedwarsAPI.getInstance().getGameByName(parsed).getStatus()==GameStatus.WAITING) 
        		return Integer.toString(BedwarsAPI.getInstance().getGameByName(parsed).countConnectedPlayers());
        	return "0";
		}
		
		if(parsed.endsWith("_inall")) {
			parsed = parsed.replace("_inall", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			
			org.screamingsandals.bedwars.api.game.Game game = BedwarsAPI.getInstance().getGameByName(parsed);
        	return Integer.toString(game.countConnectedPlayers());
			
		}
		
		if(parsed.endsWith("_world")) {
			parsed = parsed.replace("_world", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			
			org.screamingsandals.bedwars.api.game.Game game = BedwarsAPI.getInstance().getGameByName(parsed);
        	return Integer.toString(game.countConnectedPlayers());
			
		}
		
		if(parsed.endsWith("_state")) {
			parsed = parsed.replace("_state", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			GameStatus status = Main.getGame(parsed).getStatus();
			
			int gameTime = Main.getGame(parsed).getGameTime();
			int countdown = Main.getGame(parsed).getPauseCountdown();

			if(status==GameStatus.WAITING && BedwarsAPI.getInstance().getGameByName(parsed).getMinPlayers()
					>= BedwarsAPI.getInstance().getGameByName(parsed).countConnectedPlayers()) {
				return plugin.getConfig().getString("messages.placeholders.waiting").replace("&", "§");
			}
			if(status==GameStatus.RUNNING) {
				return plugin.getConfig().getString("messages.placeholders.running")
						.replace("&", "§")
						.replace("%time%", Main.getGame(parsed).getFormattedTimeLeft(gameTime - countdown)
						.replace("%left%", Main.getGame(parsed).getFormattedTimeLeft()));
			}
			if(status==GameStatus.WAITING 
					&& BedwarsAPI.getInstance().getGameByName(parsed).getMinPlayers()
					<= BedwarsAPI.getInstance().getGameByName(parsed).countConnectedPlayers()) {
				return plugin.getConfig().getString("messages.placeholders.starting")
						.replace("&", "§")
						.replace("%left%", Main.getGame(parsed).getFormattedTimeLeft());
			}
			if(status==GameStatus.GAME_END_CELEBRATING) {
				return plugin.getConfig().getString("messages.placeholders.ended").replace("&", "§");
			}
			if(status==GameStatus.REBUILDING) {
				return plugin.getConfig().getString("messages.placeholders.rebuilding").replace("&", "§");
			}
    		return "none"; //MAYBE
			
		}
		
		if(parsed.endsWith("_mxpl")) {
			parsed = parsed.replace("_mxpl", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			
			return Integer.toString(BedwarsAPI.getInstance().getGameByName(parsed).getMaxPlayers());
		}
		
		if(parsed.endsWith("_mnpl")) {
			parsed = parsed.replace("_mnpl", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			
			return Integer.toString(BedwarsAPI.getInstance().getGameByName(parsed).getMinPlayers());
		}
		
		if(parsed.endsWith("_tl")) {
			parsed = parsed.replace("_tl", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
			
			return Main.getGame(parsed).getFormattedTimeLeft();
		}
		
		if(parsed.endsWith("_tp")) {
			parsed = parsed.replace("_tp", "");
			if(!BedwarsAPI.getInstance().isGameWithNameExists(parsed)) return "";
				
			int gameTime = Main.getGame(parsed).getGameTime();
			int countdown = Main.getGame(parsed).getPauseCountdown();
			
			return Main.getGame(parsed).getFormattedTimeLeft(gameTime - countdown);
		}
		
		if(parsed.equals("color")) {
        	if (!BedwarsAPI.getInstance().isPlayerPlayingAnyGame(player)) return ChatColor.GRAY + "";
        	
        	GamePlayer gPlayer = Main.getPlayerGameProfile(player);
        	Game game = gPlayer.getGame();
        	if (gPlayer.isSpectator) return ChatColor.GRAY + "";

            CurrentTeam team = game.getPlayerTeam(gPlayer);
            if (team != null) {
                return team.teamInfo.color.chatColor + "";
            }
            return ChatColor.RESET + "";
            
		}
		
		if(parsed.equals("tl")) {
			if(!Main.isPlayerInGame(player)) return "00:00";
			
			return Main.getPlayerGameProfile(player).getGame().getFormattedTimeLeft();
		}
		
		if(parsed.equals("tp")) {
			if(!Main.isPlayerInGame(player)) return "00:00";
			
			int gameTime = Main.getGame(parsed).getGameTime();
			int countdown = Main.getGame(parsed).getPauseCountdown();
			
			return Main.getPlayerGameProfile(player).getGame().getFormattedTimeLeft(gameTime - countdown);
		}
		

		
		if(parsed.endsWith("_stats_kills")) {
			parsed = parsed.replace("_stats_kills", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "";
			
			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentKills() + stats.getKills());
		}
		if(parsed.endsWith("_stats_deaths")) {
			parsed = parsed.replace("_stats_deaths", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "";
			
			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentDeaths() + stats.getDeaths());
		}
		
		if(parsed.endsWith("_stats_destroyed_beds")) {
			parsed = parsed.replace("_stats_destroyed_beds", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "";
				
			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentDestroyedBeds() + stats.getDestroyedBeds());
		}
		
		if(parsed.endsWith("_stats_loses")) {
			parsed = parsed.replace("_stats_loses", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "";
			
			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentLoses() + stats.getLoses());
			
		}
		
		if(parsed.endsWith("_stats_score")) {
			parsed = parsed.replace("_stats_score", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "";
			
			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentScore() + stats.getScore());
		}
		
		if(parsed.endsWith("_stats_wins")) {
			parsed = parsed.replace("_stats_wins", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentWins() + stats.getWins());
		}
		
		if(parsed.endsWith("_stats_games")) {
			parsed = parsed.replace("_stats_games", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Integer.toString(stats.getCurrentGames() + stats.getGames());
		}
		
		if(parsed.endsWith("_stats_kd")) {
			parsed = parsed.replace("_stats_loses", "");
			if(!Main.isPlayerGameProfileRegistered(Bukkit.getPlayer(parsed))) return "Player does not exist";

			PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(Bukkit.getPlayer(parsed));
			return Double.toString(stats.getCurrentKD());
		}
		
		return null;
	}
}
