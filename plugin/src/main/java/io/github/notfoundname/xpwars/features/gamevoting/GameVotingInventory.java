package io.github.notfoundname.xpwars.features.gamevoting;

import io.github.notfoundname.xpwars.XPWars;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.screamingsandals.bedwars.Main;
import org.screamingsandals.bedwars.api.events.BedwarsGameStartedEvent;
import org.screamingsandals.bedwars.api.events.BedwarsPlayerLeaveEvent;
import org.screamingsandals.bedwars.lib.sgui.SimpleInventories;
import org.screamingsandals.bedwars.lib.sgui.builder.FormatBuilder;
import org.screamingsandals.bedwars.lib.sgui.events.PostActionEvent;
import org.screamingsandals.bedwars.lib.sgui.inventory.GuiHolder;
import org.screamingsandals.bedwars.lib.sgui.inventory.Options;
import org.screamingsandals.bedwars.lib.sgui.utils.MapReader;

import java.util.*;;
import java.util.stream.Collectors;

import static org.screamingsandals.bedwars.lib.lang.I18n.i18nonly;

public class GameVotingInventory implements Listener {
    private SimpleInventories simpleGuiFormat;
    private Options options;
    private List<Player> openedForPlayers = new ArrayList<>();
    private HashMap<Player, org.screamingsandals.bedwars.api.game.Game> playerVotes = new HashMap<>();

    public GameVotingInventory(XPWars plugin) {
        Bukkit.getServer().getPluginManager().registerEvents(this, Main.getInstance());
        options = new Options(Main.getInstance());
        options.setPrefix(i18nonly("arena_voting_name", "Vote for next arena"));
        options.setShowPageNumber(false);
        options.setRender_header_start(54); // Disable header
        options.setRender_offset(0);
        options.setRender_actual_rows(3);

        createData();

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void destroy() {
        openedForPlayers.clear();
        HandlerList.unregisterAll(this);
    }

    public void openForPlayer(Player player) {
        createData();
        simpleGuiFormat.openForPlayer(player);
        openedForPlayers.add(player);
    }

    private void createData() {
        SimpleInventories simpleGuiFormat = new SimpleInventories(options);
        FormatBuilder builder = new FormatBuilder();

        for (org.screamingsandals.bedwars.api.game.Game game : Main.getInstance().getGames().stream()
                .filter(game -> !game.getName().equals("arena0"))
                .collect(Collectors.toList())) {

            ItemStack stack = new ItemStack(Material.PAPER);
            ItemMeta stackMeta = stack.getItemMeta();

            stackMeta.setDisplayName(org.bukkit.ChatColor.RESET + game.getName());
            try {
                stackMeta.setLore(getGameVotes(game).stream()
                        .map(Player::getName)
                        .collect(Collectors.toList()
                        ));
                stack.setItemMeta(stackMeta);

                builder.add(stack).set("game", game).set("players-voted", getGameVotes(game));
            } catch (Throwable e) {
                stackMeta.setLore(Collections.singletonList("No one has nominated this game yet."));
                stack.setItemMeta(stackMeta);

                builder.add(stack).set("game", game);
            }
        }

        simpleGuiFormat.load(builder);
        simpleGuiFormat.generateData();

        this.simpleGuiFormat = simpleGuiFormat;
    }

    private void repaint() {
        for (Player player : openedForPlayers) {
            GuiHolder guiHolder = simpleGuiFormat.getCurrentGuiHolder(player);
            if (guiHolder == null) {
                return;
            }

            createData();
            guiHolder.setFormat(simpleGuiFormat);
            guiHolder.repaint();
        }
    }

    @EventHandler
    public void onPostAction(PostActionEvent event) {
        if (event.getFormat() != simpleGuiFormat) {
            return;
        }

        Player player = event.getPlayer();
        MapReader reader = event.getItem().getReader();
        if (reader.containsKey("game")) {
            if (!openedForPlayers.contains(player)) {
                openedForPlayers.add(player);
            }
            setGameVotes((org.screamingsandals.bedwars.api.game.Game) reader.get("game"), player);
            repaint();
        }
    }

    @EventHandler
    public void onPlayerLeave(BedwarsPlayerLeaveEvent event) {
        openedForPlayers.remove(event.getPlayer());
        removePlayerVote(event.getPlayer());
        repaint();
    }

    @EventHandler
    public void onGameStart(BedwarsGameStartedEvent event) {
        event.getGame().getConnectedPlayers().forEach(this::removePlayerVote);
        repaint();
    }

    public List<Player> getGameVotes(org.screamingsandals.bedwars.api.game.Game game) {
        if (playerVotes.containsValue(game)) {
            return playerVotes.keySet().stream()
                    .filter(player -> playerVotes.get(player) == game)
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<Player> getAllPlayersVoted() {
        return new ArrayList<>(playerVotes.keySet());
    }

    public void setGameVotes(org.screamingsandals.bedwars.api.game.Game game, Player... players) {
        for (Player player : players) {
            playerVotes.put(player, game);
        }
    }

    public void removePlayerVote(Player player) {
        playerVotes.remove(player);
    }

    public org.screamingsandals.bedwars.api.game.Game getGameWithHighestVotes() {
        org.screamingsandals.bedwars.api.game.Game highestGame = null;

        for (org.screamingsandals.bedwars.api.game.Game game : playerVotes.values().stream().distinct().collect(Collectors.toList())) {
            try {
                if (getGameVotes(game).size() > getGameVotes(highestGame).size()) {
                    highestGame = game;
                }
            } catch (Throwable e) {
                highestGame = Main.getInstance().getGameByName("country");
            }

        }

        return highestGame == null ? Main.getInstance().getGameByName("country") : highestGame;
    }
}
