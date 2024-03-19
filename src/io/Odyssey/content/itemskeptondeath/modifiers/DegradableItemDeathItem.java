package io.Odyssey.content.itemskeptondeath.modifiers;

import io.Odyssey.content.combat.magic.items.Degrade;
import io.Odyssey.content.itemskeptondeath.DeathItemModifier;
import io.Odyssey.model.Items;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import java.util.*;

public class DegradableItemDeathItem implements DeathItemModifier {

    private static final Set<Integer> ALL;

    static {
        ALL = new HashSet<>();
        Arrays.stream(Degrade.DegradableItem.values()).forEach(it -> ALL.add(it.getItemId()));
    }

    @Override
    public Set<Integer> getItemIds() {
        return ALL;
    }

    @Override
    public void modify(Player player, GameItem gameItem, boolean kept, List<GameItem> keptItems, List<GameItem> lostItems) {
        if (kept)
            return;

        Degrade.DegradableItem degrade = Degrade.DegradableItem.forId(gameItem.getId()).orElse(null);
        if (degrade == null)
            return;

        lostItems.remove(gameItem);
        Degrade.reset(player, degrade);

        if (degrade.getBrokenId() > 0 && ItemDef.forId(degrade.getBrokenId()).isTradable()) {
            lostItems.add(new GameItem(degrade.getBrokenId()));
        } else if (degrade.getCost() > 0) {
            lostItems.add(new GameItem(Items.COINS, degrade.getCost()));
        }
    }
}
