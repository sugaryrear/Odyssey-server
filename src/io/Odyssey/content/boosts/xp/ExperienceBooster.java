package io.Odyssey.content.boosts.xp;

import io.Odyssey.content.boosts.BoostType;
import io.Odyssey.content.boosts.Booster;
import io.Odyssey.content.boosts.PlayerSkillWrapper;

public abstract class ExperienceBooster implements Booster<PlayerSkillWrapper> {

    @Override
    public BoostType getType() {
        return BoostType.EXPERIENCE;
    }

}
