package io.Odyssey.content.bosses.AvatarOfCreation;

import io.Odyssey.content.QuestTab;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class NoxiferBonus implements AvatarOfCreationBonus {
    @Override
    public void activate(Player player) {
        AvatarOfCreation.activeNoxiferSeed = true;
        AvatarOfCreation.NOXIFER_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@" + player.getDisplayNameFormatted() + " @bla@planted a Noxifer seed which" +
                " granted @red@1 hour of 2x Slayer points.");
        QuestTab.updateAllQuestTabs();
    }


    @Override
    public void deactivate() {
        updateObject(false);
        AvatarOfCreation.activeNoxiferSeed = false;
        AvatarOfCreation.NOXIFER_TIMER = 0;

    }

    @Override
    public boolean canPlant(Player player) {

        return true;
    }

    @Override
    public AvatarOfCreationBonusPlant getPlant() {
        return AvatarOfCreationBonusPlant.NOXIFER;
    }
}
