package io.Odyssey.content.bosses.AvatarOfCreation;

import java.util.concurrent.TimeUnit;

import io.Odyssey.content.QuestTab;
import io.Odyssey.content.bonus.DoubleExperience;
import io.Odyssey.content.wogw.Wogw;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

public class AttasBonus implements AvatarOfCreationBonus {
    @Override
    public void activate(Player player) {
        Wogw.EXPERIENCE_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] The Attas has sprouted and is granting 1 hours bonus xp!");
        QuestTab.updateAllQuestTabs();
    }

    @Override
    public void deactivate() {
        AvatarOfCreation.activeAttasSeed = false;
        AvatarOfCreation.ATTAS_TIMER = 0;
        updateObject(false);
    }

    @Override
    public boolean canPlant(Player player) {
        if (DoubleExperience.isDoubleExperience()) {
            player.sendMessage("This seed can't be planted during bonus experience.");
            return false;
        }
        return true;
    }

    @Override
    public AvatarOfCreationBonusPlant getPlant() {
        return AvatarOfCreationBonusPlant.ATTAS;
    }
}
