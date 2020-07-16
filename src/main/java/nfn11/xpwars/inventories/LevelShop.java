package nfn11.xpwars.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.game.GameStore;
import org.screamingsandals.bedwars.api.game.ItemSpawnerType;
import org.screamingsandals.bedwars.api.upgrades.Upgrade;
import org.screamingsandals.bedwars.api.upgrades.UpgradeRegistry;
import org.screamingsandals.bedwars.api.upgrades.UpgradeStorage;
import org.screamingsandals.bedwars.game.CurrentTeam;
import org.screamingsandals.bedwars.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;
import org.screamingsandals.bedwars.utils.Debugger;
import org.screamingsandals.bedwars.utils.Sounds;
import org.screamingsandals.bedwars.lib.sgui.SimpleInventories;
import org.screamingsandals.bedwars.lib.sgui.events.GenerateItemEvent;
import org.screamingsandals.bedwars.lib.sgui.events.PreActionEvent;
import org.screamingsandals.bedwars.lib.sgui.events.ShopTransactionEvent;
import org.screamingsandals.bedwars.lib.sgui.inventory.Options;
import org.screamingsandals.bedwars.lib.sgui.item.ItemProperty;
import org.screamingsandals.bedwars.lib.sgui.item.PlayerItemInfo;
import org.screamingsandals.bedwars.lib.sgui.utils.MapReader;
import nfn11.xpwars.XPWars;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;
import static org.screamingsandals.bedwars.lib.lang.I18n.i18nonly;
@SuppressWarnings("deprecation")
public class LevelShop implements Listener {
	/*
	 * Well, just original shop with some changed things.
	 */
	private Map<String, SimpleInventories> shopMap = new HashMap<>();
	private Options options = new Options(Main.getInstance());

	public LevelShop() {
		Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());

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

		options.setRows(Main.getConfigurator().config.getInt("shop.rows", 4));
		options.setRender_actual_rows(Main.getConfigurator().config.getInt("shop.render-actual-rows", 6));
		options.setRender_offset(Main.getConfigurator().config.getInt("shop.render-offset", 9));
		options.setRender_header_start(Main.getConfigurator().config.getInt("shop.render-header-start", 0));
		options.setRender_footer_start(Main.getConfigurator().config.getInt("shop.render-footer-start", 45));
		options.setItems_on_row(Main.getConfigurator().config.getInt("shop.items-on-row", 9));
		options.setShowPageNumber(Main.getConfigurator().config.getBoolean("shop.show-page-numbers", true));
		options.setInventoryType(
				InventoryType.valueOf(Main.getConfigurator().config.getString("shop.inventory-type", "CHEST")));

		options.setGenericShopPriceTypeRequired(false);
		options.setPrefix(i18nonly("item_shop_name", "[BW] Shop"));
		options.setGenericShop(true);
		options.setGenericShopPriceTypeRequired(true);
		options.setAnimationsEnabled(true);

		options.registerPlaceholder("team", (key, player, arguments) -> {
			GamePlayer gPlayer = Main.getPlayerGameProfile(player);
			CurrentTeam team = gPlayer.getGame().getPlayerTeam(gPlayer);
			if (arguments.length > 0) {
				String fa = arguments[0];
				switch (fa) {
				case "color":
					return team.teamInfo.color.name();
				case "chatcolor":
					return team.teamInfo.color.chatColor.toString();
				case "maxplayers":
					return Integer.toString(team.teamInfo.maxPlayers);
				case "players":
					return Integer.toString(team.players.size());
				case "hasBed":
					return Boolean.toString(team.isBed);
				}
			}
			return team.getName();
		});
		options.registerPlaceholder("spawner", (key, player, arguments) -> {
			GamePlayer gPlayer = Main.getPlayerGameProfile(player);
			Game game = gPlayer.getGame();
			if (arguments.length > 2) {
				String upgradeBy = arguments[0];
				String upgrade = arguments[1];
				UpgradeStorage upgradeStorage = UpgradeRegistry.getUpgrade("spawner");
				if (upgradeStorage == null) {
					return null;
				}
				List<Upgrade> upgrades = null;
				switch (upgradeBy) {
				case "name":
					upgrades = upgradeStorage.findItemSpawnerUpgrades(game, upgrade);
					break;
				case "team":
					upgrades = upgradeStorage.findItemSpawnerUpgrades(game, game.getPlayerTeam(gPlayer));
					break;
				}

				if (upgrades != null && !upgrades.isEmpty()) {
					String what = "level";
					if (arguments.length > 3) {
						what = arguments[2];
					}
					double heighest = Double.MIN_VALUE;
					switch (what) {
					case "level":
						for (Upgrade upgrad : upgrades) {
							if (upgrad.getLevel() > heighest) {
								heighest = upgrad.getLevel();
							}
						}
						return String.valueOf(heighest);
					case "initial":
						for (Upgrade upgrad : upgrades) {
							if (upgrad.getInitialLevel() > heighest) {
								heighest = upgrad.getInitialLevel();
							}
						}
						return String.valueOf(heighest);
					}
				}
			}
			return "";
		});

		loadNewShop("default", null, true);
	}

	public void show(Player player, GameStore store) {
		try {
			boolean parent = true;
			String file = null;
			if (store != null) {
				parent = store.getUseParent();
				file = store.getShopFile();
			}
			if (file != null) {
				if (file.endsWith(".yml")) {
					file = file.substring(0, file.length() - 4);
				}
				String name = (parent ? "+" : "-") + file;
				if (!shopMap.containsKey(name)) {
					if (new File(Main.getInstance().getDataFolder(), file + ".groovy").exists()) {
						loadNewShop(name, file + ".groovy", parent);
					} else {
						loadNewShop(name, file + ".yml", parent);
					}
				}
				SimpleInventories shop = shopMap.get(name);
				shop.openForPlayer(player);
			} else {
				shopMap.get("default").openForPlayer(player);
			}
		} catch (Throwable ignored) {
			player.sendMessage("Your shop.yml is invalid! Check it out or contact us on Discord.");
		}
	}

	@EventHandler
	public void onGeneratingItem(GenerateItemEvent event) {
		if (!shopMap.containsValue(event.getFormat())) {
			return;
		}

		PlayerItemInfo item = event.getInfo();
		Player player = event.getPlayer();
		Game game = Main.getPlayerGameProfile(player).getGame();
		MapReader reader = item.getReader();
		if (reader.containsKey("price")) {
			int price = reader.getInt("price");
			ItemStack stack = event.getStack();
			
			if (!Main.isPlayerInGame(event.getPlayer())) {
				ItemMeta meta = stack.getItemMeta();
				List<String> newlore = new ArrayList<>();
				List<String> lore = meta.getLore();
				if (meta.hasLore()) {
					for (String s : lore) {
						newlore.add(s);
					}
				}
				newlore.add(" ");
				newlore.add(ChatColor.RESET + "Price: " + price + " " + (reader.containsKey("price-type") ? reader.getString("price-type") : "Levels"));
				newlore.add(" ");
				newlore.add(ChatColor.RESET + "Properties: " + (item.hasProperties() ? " " : "none"));
				if (item.hasProperties()) {
					for(ItemProperty property : item.getProperties()) {
						if (property.hasName()) {
							newlore.add(property.getPropertyName());
						}
					}
				}
				meta.setLore(newlore);
				stack.setItemMeta(meta);
			} else {
				boolean enabled = Main.getConfigurator().config.getBoolean("lore.generate-automatically", true);
				enabled = reader.getBoolean("generate-lore", enabled);

				List<String> loreText = reader.getStringList("generated-lore-text",
						Main.getConfigurator().config.getStringList("lore.text"));

				if (enabled) {
					
					ItemMeta stackMeta = stack.getItemMeta();
					List<String> lore = new ArrayList<>();
					if (stackMeta.hasLore()) {
						lore = stackMeta.getLore();
					}
					for (String s : loreText) {
						s = s.replaceAll("%price%", Integer.toString(price));
						s = s.replaceAll("%resource%", reader.containsKey("price-type") ? reader.getString("price-type") : "Level");
						s = s.replaceAll("%amount%", Integer.toString(stack.getAmount()));
						lore.add(s);
					}
					stackMeta.setLore(lore);
					stack.setItemMeta(stackMeta);
					event.setStack(stack);
				}
				if (item.hasProperties()) {
					for (ItemProperty property : item.getProperties()) {
						if (property.hasName()) {
							ItemStack newItem = event.getStack();
							BedwarsApplyPropertyToDisplayedItem applyEvent = new BedwarsApplyPropertyToDisplayedItem(game,
									player, newItem, property.getReader(player).convertToMap());
							Main.getInstance().getServer().getPluginManager().callEvent(applyEvent);

							event.setStack(newItem);
						}
					}
				}
			}
			
		}

	}

	@EventHandler
	public void onPreAction(PreActionEvent event) {
		if (!shopMap.containsValue(event.getFormat()) || event.isCancelled()) {
			return;
		}

		if (Main.getPlayerGameProfile(event.getPlayer()).isSpectator) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onShopTransaction(ShopTransactionEvent event) {
		if (!shopMap.containsValue(event.getFormat()) || event.isCancelled()) {
			return;
		}

		MapReader reader = event.getItem().getReader();
		if (Main.isPlayerInGame(event.getPlayer())) {
			if (reader.containsKey("upgrade")) {
				handleUpgrade(event);
			} else {
				handleBuy(event);
			}
		}
	}

	@EventHandler
	public void onApplyPropertyToBoughtItem(BedwarsApplyPropertyToItem event) {
		if (!Main.isPlayerInGame(event.getPlayer()))
			return;
		if (event.getPropertyName().equalsIgnoreCase("applycolorbyteam")
				|| event.getPropertyName().equalsIgnoreCase("transform::applycolorbyteam")) {
			Player player = event.getPlayer();
			CurrentTeam team = (CurrentTeam) event.getGame().getTeamOfPlayer(player);

			if (Main.getConfigurator().config.getBoolean("automatic-coloring-in-shop")) {
				event.setStack(Main.applyColor(team.teamInfo.color, event.getStack()));
			}
		}
	}

	private void loadNewShop(String name, String fileName, boolean useParent) {
		SimpleInventories format = new SimpleInventories(options);
		try {
			if (useParent) {
				String shopFileName = "shop.yml";
				if (Main.getConfigurator().config.getBoolean("turnOnExperimentalGroovyShop", false)) {
					shopFileName = "shop.groovy";
				}
				format.loadFromDataFolder(Main.getInstance().getDataFolder(), shopFileName);
			}
			if (fileName != null) {
				format.loadFromDataFolder(Main.getInstance().getDataFolder(), fileName);
			}
		} catch (Exception ignored) {
			Bukkit.getLogger().severe("Wrong shop.yml configuration!");
			Bukkit.getLogger().severe("Your villagers won't work, check validity of your YAML!");
		}

		format.generateData();
		shopMap.put(name, format);
	}

	private static String getNameOrCustomNameOfItem(ItemStack stack) {
		try {
			if (stack.hasItemMeta()) {
				ItemMeta meta = stack.getItemMeta();
				if (meta == null) {
					return "";
				}

				if (meta.hasDisplayName()) {
					return meta.getDisplayName();
				}
				if (meta.hasLocalizedName()) {
					return meta.getLocalizedName();
				}
			}
		} catch (Throwable ignored) {
		}

		String normalItemName = stack.getType().name().replace("_", " ").toLowerCase();
		String[] sArray = normalItemName.split(" ");
		StringBuilder stringBuilder = new StringBuilder();

		for (String s : sArray) {
			stringBuilder.append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).append(" ");
		}
		return stringBuilder.toString().trim();
	}

	private void handleBuy(ShopTransactionEvent event) {
		Player player = event.getPlayer();
		Game game = Main.getPlayerGameProfile(event.getPlayer()).getGame();
		MapReader mapReader = event.getItem().getReader();
		ItemStack newItem = event.getStack();
		ClickType clickType = event.getClickType();
		
		int level = player.getLevel();
		int amount = newItem.getAmount();
		int price = event.getPrice();
		int inInventory = 0;

		if (mapReader.containsKey("currency-changer")) {
			String changeItemToName = mapReader.getString("currency-changer");
			ItemSpawnerType changeItemType;
			if (changeItemToName == null) {
				return;
			}

			changeItemType = Main.getSpawnerType(changeItemToName);
			if (changeItemType == null) {
				return;
			}

			newItem = changeItemType.getStack();
		}
		
		if (clickType.isShiftClick() && newItem.getMaxStackSize() > 1) {
			double priceOfOne = (double) price / amount;
			double maxStackSize;
			int finalStackSize;

			for (ItemStack itemStack : event.getPlayer().getInventory().getStorageContents()) {
				if (itemStack != null) {
					inInventory = inInventory + itemStack.getAmount();
				}
			}
			if (Main.getConfigurator().config.getBoolean("sell-max-64-per-click-in-shop")) {
				maxStackSize = Math.min(inInventory / priceOfOne, newItem.getMaxStackSize());
			} else {
				maxStackSize = inInventory / priceOfOne;
			}

			finalStackSize = (int) maxStackSize;
			if (finalStackSize > amount) {
				price = (int) (priceOfOne * finalStackSize);
				newItem.setAmount(finalStackSize);
				amount = finalStackSize;
			}
		}
		
		if (level >= price) {
			if (event.hasProperties()) {
				for (ItemProperty property : event.getProperties()) {
					if (property.hasName()) {
						BedwarsApplyPropertyToBoughtItem applyEvent = new BedwarsApplyPropertyToBoughtItem(game, player,
								newItem, property.getReader(player).convertToMap());
						Main.getInstance().getServer().getPluginManager().callEvent(applyEvent);

						newItem = applyEvent.getStack();
					}
				}
			}

			player.setLevel(level - price);
			if (player.getInventory().firstEmpty() == -1) {
				player.getLocation().getWorld().dropItem(player.getLocation(), newItem);
			} else {
				event.buyStack(newItem);
			}

			if (!Main.getConfigurator().config.getBoolean("removePurchaseMessages", false)) {
				player.sendMessage(
						i18n("buy_succes").replace("%item%", amount + "x " + getNameOrCustomNameOfItem(newItem))
								.replace("%material%", price + " " + "Levels"));
			}
			Sounds.playSound(player, player.getLocation(),
					Main.getConfigurator().config.getString("sounds.on_item_buy"), Sounds.ENTITY_ITEM_PICKUP, 1, 1);
		} else {
			if (!Main.getConfigurator().config.getBoolean("removePurchaseMessages", false)) {
				player.sendMessage(
						i18n("buy_failed").replace("%item%", amount + "x " + getNameOrCustomNameOfItem(newItem))
								.replace("%material%", price + " " + "Levels"));
			}
		}
	}

	private void handleUpgrade(ShopTransactionEvent event) {
		Player player = event.getPlayer();
		Game game = Main.getPlayerGameProfile(event.getPlayer()).getGame();
		MapReader mapReader = event.getItem().getReader();

		MapReader upgradeMapReader = mapReader.getMap("upgrade");
		List<MapReader> entities = upgradeMapReader.getMapList("entities");
		String itemName = upgradeMapReader.getString("shop-name", "UPGRADE");

		int price = event.getPrice();
		int level = player.getLevel();
		boolean sendToAll = false;
		boolean isUpgrade = true;

		if (level >= price) {
			player.setLevel(level - price);
			for (MapReader mapEntity : entities) {
				String configuredType = mapEntity.getString("type");
				if (configuredType == null) {
					return;
				}

				UpgradeStorage upgradeStorage = UpgradeRegistry.getUpgrade(configuredType);
				if (upgradeStorage != null) {

					// TODO: Learn SimpleGuiFormat upgrades pre-parsing and automatic renaming old
					// variables
					Team team = game.getTeamOfPlayer(event.getPlayer());
					double addLevels = mapEntity.getDouble("add-levels",
							mapEntity.getDouble("levels", 0) /* Old configuration */);
					/* You shouldn't use it in entities */
					if (mapEntity.containsKey("shop-name")) {
						itemName = mapEntity.getString("shop-name");
					}
					sendToAll = mapEntity.getBoolean("notify-team", false);

					List<Upgrade> upgrades = new ArrayList<>();

					if (mapEntity.containsKey("spawner-name")) {
						String customName = mapEntity.getString("spawner-name");
						upgrades = upgradeStorage.findItemSpawnerUpgrades(game, customName);
					} else if (mapEntity.containsKey("spawner-type")) {
						String mapSpawnerType = mapEntity.getString("spawner-type");
						ItemSpawnerType spawnerType = Main.getSpawnerType(mapSpawnerType);

						upgrades = upgradeStorage.findItemSpawnerUpgrades(game, team, spawnerType);
					} else if (mapEntity.containsKey("team-upgrade")) {
						boolean upgradeAllSpawnersInTeam = mapEntity.getBoolean("team-upgrade");

						if (upgradeAllSpawnersInTeam) {
							upgrades = upgradeStorage.findItemSpawnerUpgrades(game, team);
						}

					} else if (mapEntity.containsKey("customName")) { // Old configuration
						String customName = mapEntity.getString("customName");
						upgrades = upgradeStorage.findItemSpawnerUpgrades(game, customName);
					} else {
						isUpgrade = false;
						Debugger.warn("[BedWars]> Upgrade configuration is invalid.");
					}

					if (isUpgrade) {
						BedwarsUpgradeBoughtEvent bedwarsUpgradeBoughtEvent = new BedwarsUpgradeBoughtEvent(game,
								upgradeStorage, upgrades, player, addLevels);
						Bukkit.getPluginManager().callEvent(bedwarsUpgradeBoughtEvent);

						if (bedwarsUpgradeBoughtEvent.isCancelled()) {
							continue;
						}

						if (upgrades.isEmpty()) {
							continue;
						}

						for (Upgrade upgrade : upgrades) {
							BedwarsUpgradeImprovedEvent improvedEvent = new BedwarsUpgradeImprovedEvent(game,
									upgradeStorage, upgrade, upgrade.getLevel(), upgrade.getLevel() + addLevels);
							Bukkit.getPluginManager().callEvent(improvedEvent);
						}
					}
				}

				if (sendToAll) {
					for (Player player1 : game.getTeamOfPlayer(event.getPlayer()).getConnectedPlayers()) {
						if (!Main.getConfigurator().config.getBoolean("removePurchaseMessages", false)) {
							player1.sendMessage(i18n("buy_succes").replace("%item%", itemName).replace("%material%",
									price + " " + "Levels"));
						}
						Sounds.playSound(player1, player1.getLocation(),
								Main.getConfigurator().config.getString("sounds.on_upgrade_buy"),
								Sounds.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
					}
				} else {
					if (!Main.getConfigurator().config.getBoolean("removePurchaseMessages", false)) {
						player.sendMessage(i18n("buy_succes").replace("%item%", itemName).replace("%material%",
								price + " " + "Levels"));
					}
					Sounds.playSound(player, player.getLocation(),
							Main.getConfigurator().config.getString("sounds.on_upgrade_buy"),
							Sounds.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
				}
			}
		} else {
			if (!Main.getConfigurator().config.getBoolean("removePurchaseMessages", false)) {
				player.sendMessage(
						i18n("buy_failed").replace("%item%", "UPGRADE").replace("%material%", Integer.toString(price)));
			}
		}
	}
}
