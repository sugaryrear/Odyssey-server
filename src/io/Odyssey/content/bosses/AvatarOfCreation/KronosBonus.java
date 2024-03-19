package io.Odyssey.content.bosses.AvatarOfCreation;

import java.util.concurrent.TimeUnit;

import io.Odyssey.content.QuestTab;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

public class KronosBonus implements AvatarOfCreationBonus {
    @Override
    public void activate(Player player) {
        AvatarOfCreation.activeKronosSeed = true;
        AvatarOfCreation.KRONOS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@" + player.getDisplayNameFormatted() + " @bla@planted a Kronos seed which" +
                " granted @red@1 hour of double Raids 1 keys");
        PlayerHandler.executeGlobalMessage("@red@                   and doubled chances at ToB!");
        QuestTab.updateAllQuestTabs();
    }


    @Override
    public void deactivate() {
        updateObject(false);
        AvatarOfCreation.activeKronosSeed = false;
        AvatarOfCreation.KRONOS_TIMER = 0;

    }

    @Override
    public boolean canPlant(Player player) {

        return true;
    }

    @Override
    public AvatarOfCreationBonusPlant getPlant() {
        return AvatarOfCreationBonusPlant.KRONOS;
    }
}
