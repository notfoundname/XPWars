package nfn11.xpwars.inventories;

import static org.screamingsandals.bedwars.lib.lang.I.i18n;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.lib.sgui.SimpleInventories;
import org.screamingsandals.bedwars.lib.sgui.inventory.Options;

import nfn11.xpwars.XPWars;

public class AvailableGamesInv implements Listener {
	private SimpleInventories menu;
	private Options options;

	public AvailableGamesInv() {
		Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
		options = new Options(XPWars.getInstance());
		options.setPrefix(
				XPWars.getConfigurator().getString("messages.gamesinv.header", "&rGames [&e%free%&7/&6%total%&r]"));
		options.setShowPageNumber(Main.getConfigurator().config.getBoolean("shop.show-page-numbers", true));
		
		ItemStack backItem = Main.getConfigurator().readDefinedItem("shopback", "BARRIER");
		ItemMeta backItemMeta = backItem.getItemMeta();
		backItemMeta.setDisplayName(i18n("shop_back", false));
		backItem.setItemMeta(backItemMeta);
		options.setBackItem(backItem);

		ItemStack pageBackItem = Main.getConfigurator().readDefinedItem("pageback", "ARROW");
		ItemMeta pageBackItemMeta = backItem.getItemMeta();
		pageBackItemMeta.setDisplayName(i18n("page_back", false));
		pageBackItem.setItemMeta(pageBackItemMeta);
		options.setPageBackItem(pageBackItem);

		ItemStack pageForwardItem = Main.getConfigurator().readDefinedItem("pageforward", "ARROW");
		ItemMeta pageForwardItemMeta = backItem.getItemMeta();
		pageForwardItemMeta.setDisplayName(i18n("page_forward", false));
		pageForwardItem.setItemMeta(pageForwardItemMeta);
		options.setPageForwardItem(pageForwardItem);

		ItemStack cosmeticItem = Main.getConfigurator().readDefinedItem("shopcosmetic", "AIR");
		options.setCosmeticItem(cosmeticItem);
		
		options.setRender_header_start(0);
		options.setRender_offset(9);
		int gameCount = BedwarsAPI.getInstance().getGames().size();
		if (gameCount <= 9) {
			options.setRender_actual_rows(1);
		} else if (gameCount <= 18) {
			options.setRender_actual_rows(2);
		}
		createData();
	}

	private void createData() {

	}
}
