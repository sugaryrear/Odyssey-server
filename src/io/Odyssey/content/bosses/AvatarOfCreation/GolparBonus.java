package io.Odyssey.content.bosses.AvatarOfCreation;

import io.Odyssey.content.QuestTab;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class GolparBonus implements AvatarOfCreationBonus {
    @Override
    public void activate(Player player) {
        AvatarOfCreation.activeGolparSeed = true;
        AvatarOfCreation.GOLPAR_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@" + player.getDisplayNameFormatted() + " @bla@planted a Golpar seed which" +
                " granted @red@1 hour of 2x bonus loot including:");
        PlayerHandler.executeGlobalMessage("@red@                   Crystal keys, Coin bags, Resource boxes and Clues!");
        QuestTab.updateAllQuestTabs();
    }


    @Override
    public void deactivate() {
        updateObject(false);
        AvatarOfCreation.activeGolparSeed = false;
        AvatarOfCreation.GOLPAR_TIMER = 0;

    }

    @Override
    public boolean canPlant(Player player) {

        return true;
    }

    @Override
    public AvatarOfCreationBonusPlant getPlant() {
        return AvatarOfCreationBonusPlant.GOLPAR;
    }
}
