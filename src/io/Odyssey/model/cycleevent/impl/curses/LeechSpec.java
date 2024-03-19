package io.Odyssey.model.cycleevent.impl.curses;

import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;

public class LeechSpec extends Event<Player> {

    public LeechSpec(String signature,Player attachment,int ticks) {
        super(signature,attachment,ticks);
    }
    @Override
    public void execute() {

        //  System.out.println("here execute");
        if (attachment == null || attachment.isDead || attachment.isDisconnected() || attachment.getSession() == null) {
            super.stop();
            return;
        }
        if (!attachment.prayerActive[CombatPrayer.LEECH_SPECIAL]) {
            super.stop();
            return;
        }
        if(attachment.specAmount >= 10.0){
            return;
        }
        attachment.specAmount+=1.0;
        attachment.getItems().updateSpecialBar();


    }

}


