package io.github.notfoundname.xpwars.kit;

import io.github.notfoundname.xpwars.XPWars;
import io.github.notfoundname.xpwars.utils.KitUtils;
import io.github.notfoundname.xpwars.utils.XPWarsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerJoinedEvent;
import org.screamingsandals.bedwars.api.game.GameStatus;
import org.screamingsandals.bedwars.lib.sgui.SimpleInventories;
import org.screamingsandals.bedwars.lib.sgui.builder.FormatBuilder;
import org.screamingsandals.bedwars.lib.sgui.events.PostActionEvent;
import org.screamingsandals.bedwars.lib.sgui.inventory.GuiHolder;
import org.screamingsandals.bedwars.lib.sgui.inventory.Options;
import org.screamingsandals.bedwars.lib.sgui.utils.MapReader;
import org.screamingsandals.bedwars.lib.sgui.utils.StackParser;
import java.util.*;
import static org.screamingsandals.bedwars.lib.lang.I.i18n;

public class KitSelectionInventory implements Listener {

    private SimpleInventories menu;
    private Options options;
    private List<Player> openedForPlayers = new ArrayList<>();
    private HashMap<UUID, String> selectedKit = new HashMap<>();
    XPWars plugin;

    public KitSelectionInventory(XPWars plugin) {

        this.plugin = plugin;

        options = new Options(XPWars.getInstance());
        options.setPrefix(ChatColor.translateAlternateColorCodes('&',
                XPWars.getConfigurator().config.getString("kits.settings.title")));

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

        options.setRows(XPWars.getConfigurator().config.getInt("kits.settings.rows"));
        options.setRender_actual_rows(
                XPWars.getConfigurator().config.getInt("kits.settings.render-actual-rows"));
        options.setRender_offset(
                XPWars.getConfigurator().config.getInt("kits.settings.render-offset"));
        options.setRender_header_start(
                XPWars.getConfigurator().config.getInt("kits.settings.render-header-start"));
        options.setRender_footer_start(
                XPWars.getConfigurator().config.getInt("kits.settings.render-footer-start"));
        options.setItems_on_row(XPWars.getConfigurator().config.getInt("kits.settings.items-on-row"));
        options.setShowPageNumber(
                XPWars.getConfigurator().config.getBoolean("kits.settings.show-page-numbers"));
        options.setInventoryType(InventoryType.valueOf(
                XPWars.getConfigurator().config.getString("kits.settings.inventory-type")));
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

    @SuppressWarnings("unchecked")
    private void createData() {
        SimpleInventories menu = new SimpleInventories(options);
        FormatBuilder builder = new FormatBuilder();

        KitUtils.updateKits();

        KitUtils.getKits().values().forEach(kit ->
                openedForPlayers.forEach(player ->
                        builder.add(kit.getDisplayIcon())
                                .set("kit-items", kit.getItems())
                                .set("kit-price", kit.getPrice())
                                .set("kit-price-type", kit.getPriceType())
                                .set("kit-name", kit.getName())
                                .set("kit-give-on-respawn", kit.isGivenOnRespawn())
                                .set("kit-respawn-cooldown", kit.getRespawnCooldown())));

        menu.load(builder);
        menu.generateData();

        this.menu = menu;
    }

    @EventHandler
    public void onPostAction(PostActionEvent event) {
        if (event.getFormat() != menu)
            return;

        Player player = event.getPlayer();
        MapReader reader = event.getItem().getReader();

        if (reader.containsKey("kit-name")) {
            player.closeInventory();
            boolean pass = false;
            switch (reader.getString("kit-price-type")) {
                case "score":
                    if (reader.getInt("kit-price") > Main.getPlayerStatisticsManager().getStatistic(player).getScore())
                        XPWarsUtils.xpwarsLog(player,
                                XPWars.getConfigurator().config.getString("kits.messages.not-enough-score"));
                    else pass = true;
                    break;
                case "vault":
                    if (reader.getInt("kit-price") > XPWars.getBalance(player))
                        XPWarsUtils.xpwarsLog(player,
                                XPWars.getConfigurator().config.getString("kits.messages.not-enough-vault"));
                    else pass = true;
                    break;
                default:
                    player.sendMessage("Kit named " + reader.getString("kit-name")
                            + " has invalid price type. Contact server staff.");
                    XPWarsUtils.xpwarsLog(Bukkit.getConsoleSender(), "&cPlayer tried to use kit "
                            + reader.getString("kit-name") + ", but it has invalid price type!" +
                            " Only score and vault are allowed.");
                    break;
            }
            if (pass) {
                if (selectedKit.containsKey(player.getUniqueId()))
                    selectedKit.remove(player.getUniqueId());
                selectedKit.put(player.getUniqueId(), reader.getString("kit-name"));
                XPWarsUtils.xpwarsLog(player, XPWars.getConfigurator().config.getString("kits.messages.selected"));
            }
            repaint();
            openedForPlayers.remove(player);
        }
    }

    @EventHandler
    public void onGameStart(BedwarsGameStartedEvent event) {
        event.getGame().getConnectedPlayers().forEach(player -> {
            if (selectedKit.containsKey(player)) {
                Kit kit = KitUtils.getKit(selectedKit.get(player));
                if (kit.getPriceType().equalsIgnoreCase("vault")
                        && XPWars.withdrawPlayer(player, kit.getPrice()))
                    KitUtils.giveKit(player, kit);
                if (!kit.isGivenOnRespawn())
                    selectedKit.remove(player);
            }
        });
    }

    @EventHandler
    public void onDeath(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (!Main.isPlayerInGame(player))
            return;

        if (selectedKit.containsKey(player))
            KitUtils.giveKit(player, KitUtils.getKit(selectedKit.get(player)));
    }

    @EventHandler
    public void onJoinGame(BedwarsPlayerJoinEvent event) {
        if (event.getGame().getStatus() == GameStatus.WAITING) {
            int kitItemPosition = XPWars.getConfigurator().config.getInt("kits.item-hotbar-position", 4);
            if (kitItemPosition >= 0 && kitItemPosition <= 8) {
                ItemStack kitItem = StackParser.parseShortStack(ChatColor.translateAlternateColorCodes('&',
                        XPWars.getConfigurator().config.getString("kits.item-hotbar", "CHEST;1;&eKitSelector")));
                ItemMeta kitItemMeta = kitItem.getItemMeta();
                kitItemMeta.setDisplayName(i18n("leave_from_game_item", false));
                kitItem.setItemMeta(kitItemMeta);
                event.getPlayer().getInventory().setItem(kitItemPosition, kitItem);
            }
        }
    }

    @EventHandler
    public void onKitItemUsage(PlayerInteractEvent event) {
        assert Main.isPlayerInGame(event.getPlayer()) && !Main.getPlayerGameProfile(event.getPlayer()).isSpectator;
        if (event.getItem().equals(StackParser.parseShortStack(ChatColor.translateAlternateColorCodes('&',
                XPWars.getConfigurator().config.getString("kits.item-hotbar", "CHEST;1;&eKitSelector")))))
            if (Main.getPlayerGameProfile(event.getPlayer()).getGame().getStatus() == GameStatus.WAITING)
                XPWars.getKitSelectionInventory().openForPlayer(event.getPlayer());
    }

}
