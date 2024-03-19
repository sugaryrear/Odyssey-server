package io.Odyssey.model.shops;

import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.NamedItem;
import io.Odyssey.util.ItemConstants;

public class NamedShopItem extends NamedItem {

    private int price;

    public ShopItem toShopItem(ItemConstants itemConstants) {
        return new ShopItem(getId(itemConstants), getAmount(), price);
    }

    public NamedShopItem() {
    }

    public int getPrice() {
        return price;
    }
}
