package nfn11.xpwars;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import nfn11.xpwars.utils.XPWarsUtils;

public class XPWarsUpdateChecker {
    private static final String XPWARS_SPIGOT_VER = "https://api.spigotmc.org/legacy/update.php?resource=76895";
    private static final String XPWARS_JENKINS_VER = "https://ci.screamingsandals.org/job/XPWars/lastSuccessfulBuild/buildNumber";
    private static final String XPWARS_UPD_SPIGOT_ERROR = "&cUnable to check for new version.";
    private static final String XPWARS_UPD_JENKINS_ERROR = "&cUnable to check for new snapshot versions.";
    private static final String XPWARS_UPD_FOUND_STABLE = "&a&lFOUND NEW STABLE VERSION: %ver%";
    private static final String XPWARS_UPD_FOUND_SNAP = "&a&lFOUND NEW SNAPSHOT BUILD: %ver%";
    private static final String XPWARS_UPD_NONE = "&eNo updates available.";

    public XPWarsUpdateChecker(CommandSender sender) {
        if (!XPWars.getConfigurator().config.getBoolean("check-for-updates"))
            return;
        XPWarsUtils.xpwarsLog(sender, "Checking for updates...");
        URL url = null;

        try {
            url = new URL(XPWars.isSnapshotBuild() ? XPWARS_JENKINS_VER : XPWARS_SPIGOT_VER);
        } catch (MalformedURLException ignored) {
        }

        if (url == null) {
            XPWarsUtils.xpwarsLog(sender, XPWars.isSnapshotBuild() ? XPWARS_UPD_JENKINS_ERROR : XPWARS_UPD_SPIGOT_ERROR);
            return;
        }
        checkForNewVersion(sender, url);
    }

    private void checkForNewVersion(CommandSender sender, URL url) {
        new BukkitRunnable() {
            
            @Override
            public void run() {
                float New = 0;
                try {
                    final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    New = Float.parseFloat(
                            new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine());
                    if (New == (XPWars.isSnapshotBuild() ? XPWars.getBuildNumber() : XPWars.getVersion()) ||
                            New <= (XPWars.isSnapshotBuild() ? XPWars.getBuildNumber() : XPWars.getVersion())) {
                        XPWarsUtils.xpwarsLog(sender, XPWARS_UPD_NONE);
                        cancel();
                        return;
                    }
                } catch (IOException e) {
                    XPWarsUtils.xpwarsLog(sender, XPWARS_UPD_NONE);
                    cancel();
                    return;
                }
                
                XPWarsUtils.xpwarsLog(sender, (XPWars.isSnapshotBuild() ? XPWARS_UPD_FOUND_SNAP : XPWARS_UPD_FOUND_STABLE)
                        .replace("%ver%", Float.toString(New)));
                cancel();
            }

        }.runTaskAsynchronously(XPWars.getInstance());
        return;
    }

}
