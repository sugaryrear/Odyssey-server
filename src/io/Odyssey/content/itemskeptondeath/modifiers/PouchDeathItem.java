package io.Odyssey.content.itemskeptondeath.modifiers;

import io.Odyssey.content.combat.magic.items.pouch.GemBag;
import io.Odyssey.content.combat.magic.items.pouch.HerbSack;
import io.Odyssey.content.combat.magic.items.pouch.Pouch;
import io.Odyssey.content.combat.magic.items.pouch.RunePouch;
import io.Odyssey.content.itemskeptondeath.DeathItemModifier;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Covers any containers that extends the {@link Pouch} class if they
 * are added to the {@link PouchDeathItem#getItemIds()}.
 */
public class PouchDeathItem implements DeathItemModifier {

    private static final Logger logger = LoggerFactory.getLogger(PouchDeathItem.class);

    @Override
    public Set<Integer> getItemIds() {
        return Set.of(HerbSack.HERB_SACK_ID, GemBag.GEM_BAG_ID, RunePouch.RUNE_POUCH_ID);
    }

    @Override
    public void modify(Player player, GameItem gameItem, boolean kept, List<GameItem> keptItems, List<GameItem> lostItems) {
        List<GameItem> pouchItems;

        if (gameItem.getId() == HerbSack.HERB_SACK_ID) {
            pouchItems = player.getHerbSack().getItems();
        } else if (gameItem.getId() == GemBag.GEM_BAG_ID) {
            pouchItems = player.getGemBag().getItems();
        } else if (gameItem.getId() == RunePouch.RUNE_POUCH_ID) {
            pouchItems = player.getRunePouch().getItems();
        } else {
            logger.error("No pouch instances for {}", gameItem);
            return;
        }

        Iterator<GameItem> iterator = pouchItems.iterator();
        while (iterator.hasNext()) {
            GameItem item = iterator.next();
            iterator.remove();
            if (item == null || item.getId() <= 0 || item.getAmount() <= 0)
                continue;
            lostItems.add(item);
        }
    }
}
