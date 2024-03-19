package io.Odyssey.model.entity.player.packets;

import io.Odyssey.Server;
import io.Odyssey.content.dailyrewards.DailyRewards;
import io.Odyssey.content.party.PartyInterface;
import io.Odyssey.content.wildwarning.WildWarning;
import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.logging.player.ClickButtonLog;

import java.util.concurrent.TimeUnit;

/**
 * Since button clicking is messed up and sends a weird id some new things
 * were colliding with old things. This is an attempt to fix that with a cheap hack.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
public class ClickingButtonsNew implements PacketType {

    public static final int CLICKING_BUTTONS_NEW = 184;

    @Override
    public void processPacket(Player c, int packetType, int packetSize) {
        if (c.getMovementState().isLocked())
            return;
        if (c.isFping()) {
            /**
             * Cannot do action while fping
             */
            return;
        }
        int buttonId = c.getInStream().readUnsignedWord();
       // Server.getLogging().write(new ClickButtonLog(c, buttonId, true));

        if (c.debugMessage) {
            c.sendMessage("ClickingButtonsNew: " + buttonId + ", DialogueID: " + c.dialogueAction);
        }


        switch(buttonId){


    case 13365://collection log
        c.getCollectionLog().openInterface(c);
        break;
            case 13367://drop table
                Server.getDropManager().openDefault(c);
                break;
        }
        if (c.getEventCalendar().handleButton(buttonId)) {
            return;
        }

        if (buttonId == DailyRewards.CLAIM_BUTTON) {
            c.getDailyRewards().claim();
        }

        if (c.getWogwContributeInterface().clickButton(buttonId)) {
            return;
        }

        if (c.getQuesting().clickButton(buttonId)) {
            return;
        }

//        if (c.getTeleportInterface().clickButton(buttonId)) {
//            return;
//        }

        if (c.getAchievements().clickButton(buttonId)) {
            return;
        }

        if (c.getDiaryManager().clickButton(buttonId)) {
            return;
        }

        if (PartyInterface.handleButton(c, buttonId))
            return;
        if (WildWarning.handleButtonClick(c, buttonId))
            return;
    }
}
