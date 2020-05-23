package org.nfn11.bwaddon.special;

import org.bukkit.plugin.Plugin;
import org.nfn11.bwaddon.special.listener.*;

public class SpecialsRegister {

    public static void onEnable(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new RemoteTNTListener(), plugin);
    }
}
