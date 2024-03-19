package io.Odyssey.model.cycleevent.impl.curses;

import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;

public class SapRange extends Event<Player> {

    public SapRange(String signature,Player attachment,int ticks) {
        super(signature,attachment,ticks);
    }
    @Override
    public void execute() {

        if (attachment == null || attachment.isDead || attachment.isDisconnected() || attachment.getSession() == null) {
            super.stop();
            return;
        }
        if (attachment.underAttackByPlayer > 0 || attachment.underAttackByNpc > 0) {

            if (attachment.prayerActive[CombatPrayer.SAP_RANGER] == false) {
                attachment.sapRanged= 10;

                super.stop();
                return;
            }
            if (attachment.sapDefence < 20)
                attachment.sapDefence++;
            if (attachment.sapRanged < 20)
                attachment.sapRanged++;


        } else {
            attachment.sapRanged = 10;
            attachment.sapDefence = 10;
        }
    }

}

