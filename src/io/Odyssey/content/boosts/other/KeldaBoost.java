package io.Odyssey.content.boosts.other;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class KeldaBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "x2 Larren's Keys (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.KELDA_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return AvatarOfCreation.KELDA_TIMER > 0;
    }
}
