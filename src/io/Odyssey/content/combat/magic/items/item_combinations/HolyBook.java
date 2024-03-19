package io.Odyssey.content.combat.magic.items.item_combinations;

import java.util.List;
import java.util.Optional;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ItemCombination;

public class HolyBook extends ItemCombination {

	public HolyBook(GameItem outcome, Optional<List<GameItem>> revertedItems, GameItem[] items) {
		super(outcome, revertedItems, items);
	}

	@Override
	public void combine(Player player) {
		super.items.forEach(item -> player.getItems().deleteItem2(item.getId(), item.getAmount()));
		player.getItems().addItem(super.outcome.getId(), super.outcome.getAmount());
		player.getDH().sendItemStatement("You combined the items and created a Holy book.", 3840);
		player.setCurrentCombination(Optional.empty());
		player.nextChat = -1;
	}

	@Override
	public void showDialogue(Player player) {
		player.getDH().sendStatement("The Holy book is untradeable.", "You cannot revert this item either.");
	}

}
