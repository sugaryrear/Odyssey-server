package io.Odyssey.model.cycleevent.impl.curses;


import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;

public class LeechMagic extends Event<Player> {

    public LeechMagic(String signature,Player attachment,int ticks) {
        super(signature,attachment,ticks);
    }
    @Override
    public void execute() {

        if (attachment == null || attachment.isDead || attachment.isDisconnected() || attachment.getSession() == null) {
            super.stop();
            return;
        }
        if (!attachment.prayerActive[CombatPrayer.LEECH_MAGIC]) {
            attachment.leechMagic= 0;
            attachment.leechMagic_enemy= 0;
            attachment.getPA().sendString(99686,"0%");
            super.stop();
            return;
        }

        if (attachment.leechMagic < 10)
            attachment.leechMagic++;
        if (attachment.underAttackByPlayer > 0 || attachment.underAttackByNpc > 0) {


            if (attachment.leechMagic_enemy < 25)
                attachment.leechMagic_enemy++;



        } else {
            attachment.leechMagic = 5;
            attachment.leechMagic_enemy = 10;
        }
        attachment.getPA().sendString(99686,attachment.leechMagic+"%");
    }

}

