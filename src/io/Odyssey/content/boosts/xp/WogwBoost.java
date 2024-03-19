package io.Odyssey.content.boosts.xp;

import io.Odyssey.content.boosts.PlayerSkillWrapper;
import io.Odyssey.content.wogw.Wogw;
import io.Odyssey.util.Misc;

public class WogwBoost extends ExperienceBooster {
    @Override
    public String getDescription() {
        return "+50% XP Rate (" + Misc.cyclesToDottedTime((int) Wogw.EXPERIENCE_TIMER) + ")";
    }

    @Override
    public boolean applied(PlayerSkillWrapper playerSkillWrapper) {
        return Wogw.EXPERIENCE_TIMER > 0;
    }
}
