package io.Odyssey.content.bosses.AvatarOfCreation;

import java.util.concurrent.TimeUnit;

import io.Odyssey.Configuration;
import io.Odyssey.content.QuestTab;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

public class IasorBonus implements AvatarOfCreationBonus {
    @Override
    public void activate(Player player) {
        Configuration.DOUBLE_DROPS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @blu@" + player.getDisplayNameFormatted() + " @bla@sprouted the Iasor and it is granting 1 hr of double drops!");
        QuestTab.updateAllQuestTabs();
    }

    @Override
    public void deactivate() {
        updateObject(false);
        AvatarOfCreation.activeIasorSeed = false;
        AvatarOfCreation.IASOR_TIMER = 0;
    }

    @Override
    public boolean canPlant(Player player) {
        return true;
    }

    @Override
    public AvatarOfCreationBonusPlant getPlant() {
        return AvatarOfCreationBonusPlant.IASOR;
    }
}
