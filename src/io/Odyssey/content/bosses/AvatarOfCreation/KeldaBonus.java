package io.Odyssey.content.bosses.AvatarOfCreation;

import io.Odyssey.content.QuestTab;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class KeldaBonus implements AvatarOfCreationBonus {
    @Override
    public void activate(Player player) {
        AvatarOfCreation.activeKeldaSeed = true;
        AvatarOfCreation.KELDA_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@" + player.getDisplayNameFormatted() + " @bla@planted a Kelda seed which" +
                " granted @red@1 hour of 2x Larren's keys.");
        QuestTab.updateAllQuestTabs();
    }


    @Override
    public void deactivate() {
        updateObject(false);
        AvatarOfCreation.activeKeldaSeed = false;
        AvatarOfCreation.KELDA_TIMER = 0;

    }

    @Override
    public boolean canPlant(Player player) {

        return true;
    }

    @Override
    public AvatarOfCreationBonusPlant getPlant() {
        return AvatarOfCreationBonusPlant.KELDA;
    }
}
