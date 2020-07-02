package nfn11.xpwars.inventories;

import static org.screamingsandals.bedwars.lib.lang.I.i18n;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.game.Game;
import org.screamingsandals.bedwars.lib.sgui.SimpleInventories;
import org.screamingsandals.bedwars.api.events.BedwarsGameTickEvent;
import org.screamingsandals.bedwars.lib.sgui.builder.FormatBuilder;
import org.screamingsandals.bedwars.lib.sgui.events.PostActionEvent;
import org.screamingsandals.bedwars.lib.sgui.events.CloseInventoryEvent;
import org.screamingsandals.bedwars.lib.sgui.events.OpenInventoryEvent;
import org.screamingsandals.bedwars.lib.sgui.inventory.GuiHolder;
import org.screamingsandals.bedwars.lib.sgui.inventory.Options;
import org.screamingsandals.bedwars.lib.sgui.utils.MapReader;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;

import nfn11.xpwars.XPWars;

public class GamesInventory implements Listener {
	private SimpleInventories menu;
	private Options options;
	private List<Player> openedForPlayers = new ArrayList<>();

	public GamesInventory() {
		Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
		options = new Options(XPWars.getInstance());
		options.setPrefix(
				XPWars.getConfigurator().getString("messages.gamesinv.header", "&rGames [&e%free%&7/&6%total%&r]")
						.replace("%free%", Integer.toString(free()))
						.replace("%total%", Integer.toString(Main.getGameNames().size())));
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
		options.setRender_actual_rows(6);

		createData();
	}

	public void destroy() {
		openedForPlayers.clear();
		HandlerList.unregisterAll(this);
	}

	public void openForPlayer(Player player) {
		createData();
		menu.openForPlayer(player);
		openedForPlayers.add(player);
	}

	private void createData() {
		SimpleInventories menu = new SimpleInventories(options);
		FormatBuilder builder = new FormatBuilder();
		ItemStack stack = new ItemStack(Material.AIR);

		for (org.screamingsandals.bedwars.api.game.Game game : BedwarsAPI.getInstance().getGames()) {
			stack = formatItem(stack, game);
			builder.add(stack).set("game", game);
		}

		menu.load(builder);
		menu.generateData();

		this.menu = menu;
	}

	private ItemStack formatItem(ItemStack stack, org.screamingsandals.bedwars.api.game.Game game) {
		switch (game.getStatus()) {
		case DISABLED:
			stack = StackParser
					.parse(XPWars.getConfigurator().config.get("games-gui.item.stack.disabled"));
			break;
		case GAME_END_CELEBRATING:
			stack = StackParser
					.parse(XPWars.getConfigurator().config.get("games-gui.item.stack.ended"));
			break;
		case REBUILDING:
			stack = StackParser
					.parse(XPWars.getConfigurator().config.get("games-gui.item.stack.rebuilding"));
			break;
		case RUNNING:
			stack = StackParser
					.parse(XPWars.getConfigurator().config.get("games-gui.item.stack.running"));
			break;
		case WAITING:
			if (game.countConnectedPlayers() >= game.getMinPlayers()) {
				stack = StackParser.parse(
						XPWars.getConfigurator().config.get("games-gui.item.stack.starting"));
			} else
				stack = StackParser
						.parse(XPWars.getConfigurator().config.get("games-gui.item.stack.waiting"));
			break;
		default:
			break;
		}
		
		ItemMeta meta = stack.getItemMeta();
		List<String> lore = meta.getLore();
		String name = meta.getDisplayName();
		meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
				name.replace("%arena%", game.getName()).replace("%mxpl%", Integer.toString(game.getMaxPlayers())
						.replace("%pl%", Integer.toString(game.countConnectedPlayers())))));
		List<String> newLore = new ArrayList<>();
		
		for (String string : lore) {
			string = string.replaceAll("%pl%", Integer.toString(game.countConnectedPlayers()));
			string = string.replaceAll("%mxpl%", Integer.toString(game.getMaxPlayers()));
			string = string.replaceAll("%tl%", Main.getGame(game.getName()).getFormattedTimeLeft());
			string = string.replaceAll("%tp%", Main.getGame(game.getName())
					.getFormattedTimeLeft(game.getGameTime() - Main.getGame(game.getName()).getPauseCountdown()));
			newLore.add(string);
		}
		meta.setLore(newLore);
		stack.setItemMeta(meta);
		return stack;
	}

	private int free() {
		int i = 0;
		for (org.screamingsandals.bedwars.api.game.Game game : Main.getInstance().getGames()) {
			if (game.getStatus() == GameStatus.WAITING)
				i++;
		}
		return i;
	}

	public void repaint() {
		for (Player player : openedForPlayers) {
			GuiHolder guiHolder = menu.getCurrentGuiHolder(player);
			if (guiHolder == null) {
				return;
			}

			createData();
			guiHolder.setFormat(menu);
			guiHolder.repaint();
		}
	}

	@EventHandler
	public void onPostAction(PostActionEvent event) {
		if (event.getFormat() != menu) {
			return;
		}

		Player player = event.getPlayer();
		MapReader reader = event.getItem().getReader();
		if (reader.containsKey("game")) {
			Game game = (Game) reader.get("game");
			Main.getGame(game.getName()).joinToGame(player);
			player.closeInventory();

			repaint();
			openedForPlayers.remove(player);
		}
	}
}
