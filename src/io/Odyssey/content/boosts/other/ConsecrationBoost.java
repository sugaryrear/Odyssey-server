package io.Odyssey.content.boosts.other;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class ConsecrationBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "+5 PC Points (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.CONSECRATION_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return AvatarOfCreation.CONSECRATION_TIMER > 0;
    }
}
