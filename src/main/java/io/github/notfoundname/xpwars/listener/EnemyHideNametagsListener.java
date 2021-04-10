package io.github.notfoundname.xpwars.listener;

import io.github.notfoundname.xpwars.XPWars;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;

public class EnemyHideNametagsListener implements Listener {

    public EnemyHideNametagsListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
    }

    // I hope it works
    @EventHandler
    public void onGameStart(BedwarsGameStartedEvent event) {
        event.getGame().getRunningTeams().forEach(runningTeam ->
                runningTeam.getScoreboardTeam().setOption(
                    Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OWN_TEAM));
    }

}
