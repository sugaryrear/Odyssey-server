package io.Odyssey.model.multiplayersession.flowerpoker;

import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.lock.CompleteLock;

public class FlowerPokerLock extends CompleteLock {
    @Override
    public boolean cannotClickItem(Player player, int itemId) {
        if (itemId == Items.MITHRIL_SEEDS)
            return false;
        return true;
    }
}
