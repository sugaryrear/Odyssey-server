package io.Odyssey.content.boosts.other;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class KronosBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "x2 Raids Keys (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.KRONOS_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return AvatarOfCreation.KRONOS_TIMER > 0;
    }
}
