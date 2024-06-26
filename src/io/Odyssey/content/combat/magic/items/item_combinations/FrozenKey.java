package io.Odyssey.content.combat.magic.items.item_combinations;

import java.util.List;
import java.util.Optional;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ItemCombination;

public class FrozenKey extends ItemCombination {

    public FrozenKey(GameItem outcome, Optional<List<GameItem>> revertedItems, GameItem[] items) {
        super(outcome, revertedItems, items);
    }

    @Override
    public void combine(Player player) {
        items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
        player.getInventory().addOrDrop(new ImmutableItem(outcome.getId(), outcome.getAmount()));
        player.startAnimation(6929);
        player.FrozenKey = true;
        player.getDH().sendItemStatement("You combinethe frozen pieces.", outcome.getId());
        player.setCurrentCombination(Optional.empty());
        player.nextChat = -1;
    }

    @Override
    public void showDialogue(Player player) {
        player.getDH().sendStatement("Combining these are final.", "You cannot revert this.");
    }

}
