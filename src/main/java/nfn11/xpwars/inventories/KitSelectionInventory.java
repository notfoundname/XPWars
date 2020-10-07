package nfn11.xpwars.inventories;

import nfn11.xpwars.XPWars;
import nfn11.xpwars.utils.KitManager;
import nfn11.xpwars.utils.XPWarsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.lib.sgui.SimpleInventories;
import org.screamingsandals.bedwars.lib.sgui.builder.FormatBuilder;
import org.screamingsandals.bedwars.lib.sgui.events.PostActionEvent;
import org.screamingsandals.bedwars.lib.sgui.inventory.GuiHolder;
import org.screamingsandals.bedwars.lib.sgui.inventory.Options;
import org.screamingsandals.bedwars.lib.sgui.utils.MapReader;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;
import org.screamingsandals.bedwars.statistics.PlayerStatistic;
import org.screamingsandals.bedwars.statistics.PlayerStatisticManager;

import java.util.*;

import static org.screamingsandals.bedwars.lib.lang.I.i18n;

public class KitSelectionInventory implements Listener {

    private SimpleInventories menu;
    private Options options;
    private List<Player> openedForPlayers = new ArrayList<>();
    private HashMap<Player, String> selectedKit = new HashMap<>();
    XPWars plugin;

    public KitSelectionInventory(XPWars plugin) {

        this.plugin = plugin;

        options = new Options(XPWars.getInstance());
        options.setPrefix(ChatColor.translateAlternateColorCodes('&',
                XPWars.getConfigurator().getString("kits.settings.title", "kits")));
        options.setShowPageNumber(true);

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

        options.setRows(XPWars.getConfigurator().config.getInt("kits.settings.rows", 4));
        options.setRender_actual_rows(
                XPWars.getConfigurator().config.getInt("kits.settings.render-actual-rows", 6));
        options.setRender_offset(
                XPWars.getConfigurator().config.getInt("kits.settings.render-offset", 9));
        options.setRender_header_start(
                XPWars.getConfigurator().config.getInt("kits.settings.render-header-start", 0));
        options.setRender_footer_start(
                XPWars.getConfigurator().config.getInt("kits.settings.render-footer-start", 45));
        options.setItems_on_row(XPWars.getConfigurator().config.getInt("kits.settings.items-on-row", 9));
        options.setShowPageNumber(
                XPWars.getConfigurator().config.getBoolean("kits.settings.show-page-numbers", true));
        options.setInventoryType(InventoryType.valueOf(
                XPWars.getConfigurator().config.getString("kits.settings.inventory-type", "CHEST")));
        Bukkit.getServer().getPluginManager().registerEvents(this, XPWars.getInstance());
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

    public void repaint() {
        openedForPlayers.forEach(player -> {
            GuiHolder guiHolder = menu.getCurrentGuiHolder(player);
            if (guiHolder == null) {
                return;
            }

            createData();
            guiHolder.setFormat(menu);
            guiHolder.repaint();
        });
    }

    private void createData() {
        SimpleInventories menu = new SimpleInventories(options);
        FormatBuilder builder = new FormatBuilder();

        List<HashMap<String, Object>> kits = (List<HashMap<String, Object>>) XPWars.getConfigurator().config.getList("kits.list");

        for (HashMap<String, Object> kit : kits) {
            String name = (String) kit.get("name");
            ItemStack icon = StackParser.parse(kit.get("display-icon"));
            List<ItemStack> items = StackParser.parseAll((Collection<Object>) kit.get("items"));
            int price = Integer.parseInt(kit.get("price").toString());
            String priceType = kit.get("price-type").toString();

            builder.add(icon)
                .set("kit-items", items)
                .set("kit-price", price)
                .set("kit-price-type", priceType)
                .set("kit-name", name);
        }

        menu.load(builder);
        menu.generateData();

        this.menu = menu;
    }

    @EventHandler
    public void onPostAction(PostActionEvent event) {
        if (event.getFormat() != menu) {
            return;
        }

        Player player = event.getPlayer();
        MapReader reader = event.getItem().getReader();
        if (reader.containsKey("kit-name")) {
            player.closeInventory();
            boolean pass = false;
            switch (reader.getString("kit-price-type")) {
                case "score":
                    if (reader.getInt("kit-price") > Main.getPlayerStatisticsManager().getStatistic(player)
                            .getCurrentScore())
                        player.sendMessage("Not enough score to use this kit!");
                    else pass = true;
                    break;
                case "vault":
                    if (reader.getInt("kit-price") > XPWars.getEconomy().getBalance(player))
                        player.sendMessage("Not enough money to buy this kit!");
                    else pass = true;
                    break;
                default:
                    player.sendMessage("This kit is temporary unavailable.");
                    XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&cPlayer tried to use kit "
                            + reader.getString("kit-name") + ", but it has invalid price type!" +
                            " Only score and vault are allowed.");
                    break;
            }
            if (pass) {
                if (selectedKit.containsKey(player))
                    selectedKit.remove(player);
                selectedKit.put(player, reader.getString("kit-name"));
                player.sendMessage("Selected kit: " + reader.getString("kit-name"));
            }
            repaint();
            openedForPlayers.remove(player);
        }
    }

    @EventHandler
    public void onGameStart(BedwarsGameStartedEvent event) {
        event.getGame().getConnectedPlayers().forEach(player -> {
            if (selectedKit.containsKey(player)) {
                KitManager.Kit kit = KitManager.getKit(selectedKit.get(player));
                switch (kit.getPriceType().toLowerCase()) {
                    case "score":
                        PlayerStatistic stats = Main.getPlayerStatisticsManager().getStatistic(player);
                        stats.setCurrentScore(stats.getCurrentScore() - kit.getPrice());
                        break;
                    case "vault":
                        XPWars.getEconomy().withdrawPlayer(player, kit.getPrice());
                        break;
                }
                KitManager.giveKit(player, kit);
                selectedKit.remove(player);
            }
        });
    }

}