package io.Odyssey.content.bosses.AvatarOfCreation;

import io.Odyssey.content.QuestTab;
import io.Odyssey.content.wogw.Wogw;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

import java.util.concurrent.TimeUnit;

public class ConsecrationBonus implements AvatarOfCreationBonus {
    @Override
    public void activate(Player player) {
        Wogw.PC_POINTS_TIMER += TimeUnit.HOURS.toMillis(1) / 600;
        PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @blu@" + player.getDisplayNameFormatted() + " @bla@sprouted the Consecration and it is granting 1 hr of +5 PC points!");
        QuestTab.updateAllQuestTabs();
    }

    @Override
    public void deactivate() {
        updateObject(false);
        AvatarOfCreation.activeConsecrationSeed = false;
        AvatarOfCreation.CONSECRATION_TIMER = 0;
    }

    @Override
    public boolean canPlant(Player player) {
        return true;
    }

    @Override
    public AvatarOfCreationBonusPlant getPlant() {
        return AvatarOfCreationBonusPlant.CONSECRATION;
    }
}
