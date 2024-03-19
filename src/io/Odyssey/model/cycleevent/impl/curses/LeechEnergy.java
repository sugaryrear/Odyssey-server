package io.Odyssey.model.cycleevent.impl.curses;


import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;

public class LeechEnergy extends Event<Player> {

    public LeechEnergy(String signature,Player attachment,int ticks) {
        super(signature,attachment,ticks);
    }
    @Override
    public void execute() {

        //  System.out.println("here execute");
        if (attachment == null || attachment.isDead || attachment.isDisconnected() || attachment.getSession() == null) {
            super.stop();
            return;
        }
        if (!attachment.prayerActive[CombatPrayer.LEECH_ENERGY]) {

            super.stop();
            return;
        }
        if (attachment.getRunEnergy() > 10000) {
            return;
        }
        attachment.setRunEnergy(attachment.getRunEnergy() + 100, true);
    }

}



