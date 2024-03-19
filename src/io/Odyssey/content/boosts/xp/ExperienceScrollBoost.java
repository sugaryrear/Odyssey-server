package io.Odyssey.content.boosts.xp;

import io.Odyssey.content.boosts.PlayerSkillWrapper;

public class ExperienceScrollBoost extends ExperienceBooster {
    @Override
    public String getDescription() {
        return "Bonus +50% XP";
    }

    @Override
    public boolean applied(PlayerSkillWrapper p) {
        return p.getPlayer().xpScroll;
    }
}
