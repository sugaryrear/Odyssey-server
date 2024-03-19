package io.Odyssey.content.boosts.other;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class NoxiferBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "x2 Slayer Points (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.NOXIFER_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return AvatarOfCreation.NOXIFER_TIMER > 0;
    }
}
