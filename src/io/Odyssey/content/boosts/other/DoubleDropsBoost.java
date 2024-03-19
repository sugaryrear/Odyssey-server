package io.Odyssey.content.boosts.other;

import io.Odyssey.Configuration;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class DoubleDropsBoost extends GenericBoost {
    @Override
    public String getDescription() {
        return "+100% Drop Rate (" + Misc.cyclesToDottedTime((int) Configuration.DOUBLE_DROPS_TIMER) + ")";
    }

    @Override
    public boolean applied(Player player) {
        return Configuration.DOUBLE_DROPS_TIMER > 0;
    }
}
