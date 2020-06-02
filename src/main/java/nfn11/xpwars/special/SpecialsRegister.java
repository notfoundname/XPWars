package nfn11.xpwars.special;

import org.bukkit.plugin.Plugin;

import nfn11.xpwars.special.listener.*;

public class SpecialsRegister {

    public static void onEnable(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new RemoteTNTListener(), plugin);
    }
}
