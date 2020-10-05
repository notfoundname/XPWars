package nfn11.xpwars.listener;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.XPWarsUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.BedwarsGameTickEvent;
import org.screamingsandals.bedwars.game.CurrentTeam;
import org.screamingsandals.bedwars.game.GamePlayer;

public class ActionBarMessageListener implements Listener {

    public ActionBarMessageListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
    }

    @EventHandler
    public void onGameTick(BedwarsGameTickEvent event) {
        event.getGame().getConnectedPlayers().forEach(player -> {
            GamePlayer gp = Main.getPlayerGameProfile(player);
            CurrentTeam team = gp.getGame().getPlayerTeam(gp);
            switch (gp.getGame().getStatus()) {
                case WAITING:
                    XPWarsUtils.sendActionBar(player,
                            XPWars.getConfigurator().config.getString("action-bar-messages.in-lobby")
                                    .replace("%pl_t%", Integer.toString(team.countConnectedPlayers()))
                                    .replace("%team%", team.teamInfo.color.chatColor + team.getName())
                                    .replace("%mxpl_t%", Integer.toString(team.getMaxPlayers())));
                    break;
                case RUNNING:
                    if (team == null || gp.isSpectator)
                        XPWarsUtils.sendActionBar(player,
                                XPWars.getConfigurator().config.getString("action-bar-messages.in-game-spectator"));
                    else XPWarsUtils.sendActionBar(player,
                            XPWars.getConfigurator().config.getString("action-bar-messages.in-game-alive")
                                    .replace("%team%", team.teamInfo.color.chatColor + team.getName())
                                    .replace("%bed%",
                                            team.isTargetBlockExists()
                                                    ? Main.getConfigurator().config.getString("scoreboard.bedExists")
                                                    : Main.getConfigurator().config.getString("scoreboard.bedLost")));
                    break;
                default:
                    break;
            }
        });
    }

}
