package nfn11.xpwars.special;

import nfn11.xpwars.utils.XPWarsUtils;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

import nfn11.xpwars.XPWars;

public class Vouncher extends SpecialItem implements nfn11.xpwars.special.api.Vouncher {
    private int levels;
    private ItemStack item;
    
    public Vouncher(Game game, Player player, Team team, int levels, ItemStack item) {
        super(game, player, team);
        this.levels = levels;
        this.item = item;
    }

    @Override
    public int getLevels() {
        return levels;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public void setLevel() {
        int defmax = XPWars.getConfigurator().getInt("level.maximum-xp", 0);
        int max = XPWars.getConfigurator().getInt("level.games." + game.getName() + ".maximum-xp", defmax);

        if ((player.getLevel() + levels) > max) {
            XPWarsUtils.sendActionBar(player,
                    XPWars.getConfigurator().config
                            .getString("level.per-arena-settings." + game.getName() + ".messages.maxreached",
                                    XPWars.getConfigurator().config.getString("level.messages.maxreached"))
                            .replace("%max%", Integer.toString(max)));
        } else {
            player.setLevel(player.getLevel() + levels);
        }
    }

}
