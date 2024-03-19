package io.Odyssey.model.cycleevent.impl.curses;

import io.Odyssey.Server;
import io.Odyssey.content.SkillcapePerks;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;

public class SapWarrior extends Event<Player> {

    public SapWarrior(String signature,Player attachment,int ticks) {
        super(signature,attachment,ticks);
    }

    @Override
    public void execute() {

        //System.out.println("here execute");
        if (attachment == null || attachment.isDead || attachment.isDisconnected() || attachment.getSession() == null) {
            super.stop();
            return;
        }
        if (attachment.underAttackByPlayer > 0 || attachment.underAttackByNpc > 0) {

        if (attachment.prayerActive[CombatPrayer.SAP_WARRIOR] == false) {
            attachment.sapAttack = 10;
            attachment.sapStrength = 10;
            attachment.sapDefence = 10;
            super.stop();
            return;
        }
        if (attachment.sapDefence < 20)
            attachment.sapDefence++;
            if (attachment.sapAttack < 20)
                attachment.sapAttack++;
            if (attachment.sapStrength < 20)
                attachment.sapStrength++;

           // attachment.sendMessage("increase!");

    } else {
            attachment.sapAttack = 10;
            attachment.sapStrength = 10;
            attachment.sapDefence = 10;
        }
    }

}
