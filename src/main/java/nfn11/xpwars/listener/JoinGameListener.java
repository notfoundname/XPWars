package nfn11.xpwars.listener;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.XPWarsUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;

public class JoinGameListener implements Listener {

    public JoinGameListener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
    }

    @EventHandler
    public void onJoinGame(BedwarsPlayerJoinEvent event) {
        if (event.isCancelled())
            return;
        ConfigurationSection sec = XPWars.getConfigurator().config.getConfigurationSection("permission-to-join-game");
        if (sec == null)
            return;
        for (String permission : sec.getConfigurationSection("arenas").getValues(false).keySet()) {
            if (sec.getStringList("arenas." + permission).contains(event.getGame().getName()) && !event.getPlayer()
                    .hasPermission(permission.replace("[", "").replace("]", ""))) {
                event.setCancelled(true);
                String message = sec.getString("permission-to-join-game.message",
                        "You don't have permission %perm% to join arena %arena%!")
                        .replace("%perm%", permission)
                        .replace("%arena%", event.getGame().getName());
                XPWarsUtils.sendActionBar(event.getPlayer(), message);
                return;
            }
        }
    }
}
