package io.Odyssey.content.boosts.other;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class GolparBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "x2 Bonus Loot (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.GOLPAR_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return AvatarOfCreation.GOLPAR_TIMER > 0;
    }
}
