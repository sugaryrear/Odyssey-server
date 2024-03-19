package io.Odyssey.content.boosts.other;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class CelastrusBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "x2 Brimstone Keys (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.CELASTRUS_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return AvatarOfCreation.CELASTRUS_TIMER > 0;
    }
}
