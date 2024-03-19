package io.Odyssey.content.boosts.other;

import io.Odyssey.content.boosts.BoostType;
import io.Odyssey.content.boosts.Booster;
import io.Odyssey.model.entity.player.Player;

public abstract class GenericBoost implements Booster<Player> {
    @Override
    public BoostType getType() {
        return BoostType.GENERIC;
    }
}
