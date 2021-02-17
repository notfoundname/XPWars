package nfn11.xpwars.listener;

import nfn11.xpwars.XPWars;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;

public class EnemyHideNametagsListener implements Listener {

    public EnemyHideNametagsListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
    }

    @EventHandler
    public void onGameStart(BedwarsGameStartedEvent event) {
        event.getGame().getRunningTeams().forEach(runningTeam ->
                runningTeam.getScoreboardTeam().setOption(
                    Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM));
    }

}
