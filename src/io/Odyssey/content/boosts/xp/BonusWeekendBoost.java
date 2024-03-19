package io.Odyssey.content.boosts.xp;

import io.Odyssey.content.bonus.DoubleExperience;
import io.Odyssey.content.boosts.PlayerSkillWrapper;

public class BonusWeekendBoost extends ExperienceBooster {
    @Override
    public String getDescription() {
        return "+50% XP Bonus Weekend";
    }

    @Override
    public boolean applied(PlayerSkillWrapper playerSkillWrapper) {
        return DoubleExperience.isDoubleExperience();//DoubleExperience.isDoubleExperience();
    }
}
