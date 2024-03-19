package io.Odyssey.util.logging.player;

import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.Misc;
import io.Odyssey.util.logging.PlayerLog;

import java.util.Set;

public class ClaimAchievementLog extends PlayerLog {

    private final Achievements.Achievement achievement;

    public ClaimAchievementLog(Player player, Achievements.Achievement achievement) {
        super(player);
        this.achievement = achievement;
    }

    @Override
    public Set<String> getLogFileNames() {
        return Set.of("claimed_achievement");
    }

    @Override
    public String getLoggedMessage() {
        return Misc.replaceBracketsWithArguments("Claimed achievement {}", achievement);
    }
}
