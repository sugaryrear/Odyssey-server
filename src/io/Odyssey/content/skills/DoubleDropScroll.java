package io.Odyssey.content.skills;

import io.Odyssey.content.minigames.bonus.DoubleDrop;
import io.Odyssey.model.entity.player.ClientGameTimer;
import io.Odyssey.model.entity.player.Player;

import java.util.concurrent.TimeUnit;


/**
 * @author Aaron Whittle
 * @date Sep 22th 2019
 */

public class DoubleDropScroll {

    /**
     * Convert the amount of hours to milliseconds, divide by 600 to get the tick amount.
     */
    private static final long TIME = TimeUnit.HOURS.toMillis(1) / 600;

    public static void openScroll(Player player) {
//        if(DoubleDrop.isDoubleDrop()){
//            player.sendMessage("@red@Bonus XP Weekend is @gre@active@red@, use it another day!");
//            return;
//        } else
            if (player.ddScroll) {
            player.sendMessage("You already have the double drops bonus active.");
            return;
        }

        player.ddScroll= true;
        player.ddScrollTicks = TIME;
    }

    /**
     * Gives the player one hour of double drops.
     */
    public static void giveDoubleDrops(Player player) {
        player.xpScroll = true;
        player.xpScrollTicks = TIME;
        player.getPA().sendGameTimer(ClientGameTimer.DOUBLES_DROPS, TimeUnit.MINUTES, 60);
        player.getQuestTab().updateInformationTab();
    }


}

