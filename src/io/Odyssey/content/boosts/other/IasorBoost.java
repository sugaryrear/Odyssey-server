package io.Odyssey.content.boosts.other;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class IasorBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "+10% Drop Rate (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.IASOR_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return AvatarOfCreation.IASOR_TIMER > 0;
    }
}
