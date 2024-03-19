package io.Odyssey.content.itemskeptondeath.modifiers;

import io.Odyssey.content.itemskeptondeath.DeathItemModifier;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import java.util.List;
import java.util.Set;

public class ZamorakianHastaDeathItem implements DeathItemModifier {
    @Override
    public Set<Integer> getItemIds() {
        return Set.of(Items.ZAMORAKIAN_HASTA);
    }

    @Override
    public void modify(Player player, GameItem gameItem, boolean kept, List<GameItem> keptItems, List<GameItem> lostItems) {
        if (kept)
            return;
        lostItems.remove(gameItem);
        lostItems.add(new GameItem(Items.ZAMORAKIAN_SPEAR));
    }
}
