package io.github.notfoundname.xpwars;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import io.github.notfoundname.xpwars.utils.XPWarsUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class XPWarsUpdateChecker {
    private static final String XPWARS_UPD_FOUND_STABLE = "&a&lFound new stable version: ";
    private static final String XPWARS_UPD_NONE = "&eNo updates available.";
    private static boolean hasUpdate = false;

    public static boolean checkForUpdate(CommandSender sender) {
        XPWarsUtils.xpwarsLog(sender, "&aChecking for updates...");
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=76895");
                    final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    double newVersion = Float.parseFloat(
                            new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine());
                    if (newVersion <= XPWars.getVersion()) {
                        XPWarsUtils.xpwarsLog(sender, XPWARS_UPD_NONE);
                        cancel();
                    }
                    else XPWarsUtils.xpwarsLog(sender, XPWARS_UPD_FOUND_STABLE + newVersion);
                    hasUpdate = true;
                } catch (Exception e) {
                    XPWarsUtils.xpwarsLog(sender, "&cUnable to check for new version.");
                }
            }
        }.runTaskAsynchronously(XPWars.getInstance());
        return hasUpdate;
    }

}
