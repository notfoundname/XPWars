package nfn11.xpwars.special.listener;

import org.bukkit.Bukkit;
import nfn11.xpwars.XPWars;

public class RegisterSpecialListeners {
	public RegisterSpecialListeners() {
		Bukkit.getServer().getPluginManager().registerEvents(new RemoteTNTListener(), XPWars.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new ThrowableTNTListener(), XPWars.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new TrampolineListener(), XPWars.getInstance());
		Bukkit.getServer().getPluginManager().registerEvents(new VouncherListener(), XPWars.getInstance());
	}
}
