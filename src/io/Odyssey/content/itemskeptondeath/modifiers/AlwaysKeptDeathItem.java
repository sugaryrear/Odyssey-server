package io.Odyssey.content.itemskeptondeath.modifiers;

import io.Odyssey.content.itemskeptondeath.DeathItemModifier;
import io.Odyssey.content.trails.TreasureTrails;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AlwaysKeptDeathItem implements DeathItemModifier {

    private static final Set<Integer> ALL;

    static {
        ALL = new HashSet<>();

        // All pet items
        ALL.addAll(Arrays.stream(PetHandler.Pets.values()).map(PetHandler.Pets::getItemId).collect(Collectors.toList()));
ALL.add(27);
        ALL.addAll(Set.of(TreasureTrails.EASY_CLUE_SCROLL,
                TreasureTrails.MEDIUM_CLUE_SCROLL,
                TreasureTrails.HARD_CLUE_SCROLL,
                TreasureTrails.MASTER_CLUE_SCROLL));

        ALL.addAll(Set.of(TreasureTrails.EASY_CASKET,
                TreasureTrails.MEDIUM_CASKET,
                TreasureTrails.HARD_CASKET,
                TreasureTrails.MASTER_CASKET));
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
        if (!kept) {
            lostItems.remove(gameItem);
            keptItems.add(gameItem);
        }
    }
}
