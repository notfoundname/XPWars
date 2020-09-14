package nfn11.xpwars.utils;

import org.bukkit.inventory.ItemStack;

import java.util.List;

//that's gonna be API.. i think
public class XPWarsKit {

    private String name, priceType;
    private int price;
    private ItemStack icon;
    private List<ItemStack> items;

    public XPWarsKit(String name, ItemStack icon, List<ItemStack> items, int price, String priceType) {
        this.name = name;
        this.icon = icon;
        this.items = items;
        this.price = price;
        this.priceType = priceType;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public int getPrice() {
        return price;
    }

    public String getPriceType() {
        return priceType;
    }

}
