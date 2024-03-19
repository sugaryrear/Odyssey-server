package io.Odyssey.content.boosts.xp;

import io.Odyssey.content.boosts.PlayerSkillWrapper;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.util.Misc;

public class AttasBoost extends ExperienceBooster {
    @Override
    public String getDescription() {
        return "+50% XP (" + Misc.cyclesToDottedTime((int) AvatarOfCreation.ATTAS_TIMER) + ")";
    }

    @Override
    public boolean applied(PlayerSkillWrapper playerSkillWrapper) {
//        return AvatarOfCreation.ATTAS_TIMER > 0;
        return false;
    }
}
