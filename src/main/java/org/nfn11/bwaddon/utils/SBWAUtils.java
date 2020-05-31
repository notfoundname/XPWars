package org.nfn11.bwaddon.utils;

import java.io.File;
import java.util.List;
import org.screamingsandals.bedwars.Main;

public class SBWAUtils {
	private static List<String> list;
	
	public static List<String> getStores() {
		File[] files = Main.getInstance().getDataFolder().listFiles();
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				list.add(files[i].getName());
			} else if (files[i].isDirectory()) {
				continue;
			}
		}
		
		return list;
	}
}
