package nfn11.xpwars.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.*;
import org.screamingsandals.bedwars.api.upgrades.Upgrade;
import org.screamingsandals.bedwars.api.upgrades.UpgradeRegistry;
import org.screamingsandals.bedwars.api.upgrades.UpgradeStorage;
import org.screamingsandals.bedwars.game.CurrentTeam;
import org.screamingsandals.bedwars.game.Game;
import org.screamingsandals.bedwars.game.GamePlayer;
import org.screamingsandals.bedwars.lib.sgui.SimpleInventories;
import org.screamingsandals.bedwars.lib.sgui.builder.FormatBuilder;
import org.screamingsandals.bedwars.lib.sgui.events.GenerateItemEvent;
import org.screamingsandals.bedwars.lib.sgui.inventory.Options;
import org.screamingsandals.bedwars.lib.sgui.item.ItemProperty;
import org.screamingsandals.bedwars.lib.sgui.item.PlayerItemInfo;
import org.screamingsandals.bedwars.lib.sgui.utils.MapReader;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18n;
import static org.screamingsandals.bedwars.lib.lang.I18n.i18nonly;

import nfn11.xpwars.XPWars;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class DebugInventory implements Listener {
    /*
     * Well, just original shop with some changed things.
     */

    private SimpleInventories format;
    private Options options = new Options(Main.getInstance());
    public DebugInventory() {
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
        format = new SimpleInventories(options);
        try {
            format.loadFromDataFolder(Main.getInstance().getDataFolder(), "shop.yml");
        } catch (Exception e) {
            FormatBuilder builder = new FormatBuilder();
            builder.add("BARRIER;1;OOPS;Your shop file is broken!");
        }
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
        format.generateData();
    }

    public void show(Player player, String fileName) {
        try {
            format.loadFromDataFolder(Main.getInstance().getDataFolder(), fileName);
        } catch (Throwable ignored) {
            player.sendMessage("Your shop file is invalid!");
            FormatBuilder builder = new FormatBuilder();
            builder.add("BARRIER;1;OOPS;Your shop file is broken!");
        }
        format.generateData();
        format.openForPlayer(player);
    }

    @EventHandler
    public void onGeneratingItem(GenerateItemEvent event) {
        if (event.getFormat() != format) {
            return;
        }

        PlayerItemInfo item = event.getInfo();
        Player player = event.getPlayer();
        Game game = Main.getPlayerGameProfile(player).getGame();
        MapReader reader = item.getReader();

        if (reader.containsKey("price")) {
            int price = reader.getInt("price");
            ItemStack stack = event.getStack();

            List<String> loreText = reader.getStringList("generated-lore-text",
                    Main.getConfigurator().config.getStringList("lore.text"));

            ItemMeta stackMeta = stack.getItemMeta();
            List<String> lore = new ArrayList<>();
            if (stackMeta.hasLore())
                lore = stackMeta.getLore();
            for (String s : loreText) {
                s = s.replaceAll("%price%", Integer.toString(price));
                s = s.replaceAll("%resource%",
                        reader.containsKey("price-type") ? reader.getString("price-type") : "Level");
                s = s.replaceAll("%amount%", Integer.toString(stack.getAmount()));
                lore.add(s);
            }

            if (item.hasProperties()) {
                lore.add("Properties:");
                for (ItemProperty itemProperty : item.getProperties()) {
                    lore.add("---");
                    lore.add(itemProperty.getPropertyName());
                }
            }

            stackMeta.setLore(lore);
            stack.setItemMeta(stackMeta);
            event.setStack(stack);
        }

        if (item.hasProperties()) {
            for (ItemProperty property : item.getProperties()) {
                if (property.hasName()) {
                    ItemStack newItem = event.getStack();
                    BedwarsApplyPropertyToDisplayedItem applyEvent = new BedwarsApplyPropertyToDisplayedItem(
                            game, player, newItem, property.getReader(player).convertToMap());
                    Main.getInstance().getServer().getPluginManager().callEvent(applyEvent);
                    event.setStack(newItem);
                }
            }
        }
    }

}
