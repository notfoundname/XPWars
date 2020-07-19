package nfn11.xpwars.special;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.screamingsandals.bedwars.api.Team;
import org.screamingsandals.bedwars.api.game.Game;
import org.screamingsandals.bedwars.special.SpecialItem;

public class PortableShop extends SpecialItem implements nfn11.xpwars.special.api.PortableShop {
	private final Location loc;
	private final String shopFile;
	private final String shopName;
	private final boolean enableCustomName;
	private final boolean useParent;
	private int duration;
	private LivingEntity entity;
	private boolean isBaby;

	public PortableShop(Game game, Player player, Team team, Location loc, String shopFile, String shopName,
			boolean enableCustomName, boolean useParent, LivingEntity entity, boolean isBaby, int duration) {
		super(game, player, team);
		this.loc = loc;
		this.shopFile = shopFile;
		this.shopName = shopName;
		this.enableCustomName = enableCustomName;
		this.useParent = useParent;
		this.duration = duration;
		this.entity = entity;
	}

	@Override
	public Location getLocation() {
		return loc;
	}

	@Override
	public String getShopFile() {
		return shopFile;
	}

	@Override
	public boolean isUsesParent() {
		return useParent;
	}

	@Override
	public LivingEntity getEntity() {
		return entity;
	}

	@Override
	public String getShopName() {
		return shopName;
	}

	@Override
	public boolean isEnabledCustomName() {
		return enableCustomName;
	}

	@Override
	public boolean isBaby() {
		return isBaby;
	}

	@Override
	public int getDuration() {
		return duration;
	}

}
