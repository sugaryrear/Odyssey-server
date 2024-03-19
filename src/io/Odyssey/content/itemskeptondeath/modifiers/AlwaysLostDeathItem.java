package io.Odyssey.content.itemskeptondeath.modifiers;

import io.Odyssey.content.bosses.wildypursuit.FragmentOfSeren;
import io.Odyssey.content.bosses.wildypursuit.TheUnbearable;
import io.Odyssey.content.itemskeptondeath.DeathItemModifier;
import io.Odyssey.content.minigames.bounty_hunter.BountyHunterEmblem;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.Odyssey.model.Items.BRACELET_OF_ETHEREUM;
import static io.Odyssey.model.Items.BRACELET_OF_ETHEREUM_UNCHARGED;

/**
 * Items contained in the {@link AlwaysLostDeathItem#getItemIds()} are always dropped on death even if they
 * would have otherwise been kept.
 */
public class AlwaysLostDeathItem implements DeathItemModifier {

    private static final Set<Integer> ALL;

    static {
        ALL = new HashSet<>();
        ALL.add(TheUnbearable.KEY);
     //   ALL.add(BRACELET_OF_ETHEREUM);
       // ALL.add(BRACELET_OF_ETHEREUM_UNCHARGED);
        ALL.add(FragmentOfSeren.KEY);//todo add the pack yak here and make them retrieve it from gertrude.
        Arrays.stream(BountyHunterEmblem.values()).forEach(it -> ALL.add(it.getItemId()));
    }

    public static Set<Integer> items() {
        return ALL;
    }

    @Override
    public Set<Integer> getItemIds() {
        return ALL;
    }

    @Override
    public void modify(Player player, GameItem gameItem, boolean kept, List<GameItem> keptItems, List<GameItem> lostItems) {
        if (!kept)
            return;
        keptItems.remove(gameItem);
        lostItems.add(gameItem);
    }
}
