package io.Odyssey.content.bosses.nex.attacks;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;

public class BloodSacrifice {
    public BloodSacrifice(NPC npc, Player target) {
        if(target == null)
            return;
        BloodSacrifice(npc, target);
    }

    void BloodSacrifice(NPC npc, Player target) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                target.gfx100(1);
                target.sendMessage("You have been chosen as a sacrifice, RUN 7 TILES AWAY!");
                if(container.getTotalTicks() == 7 && target.getPosition().getAbsDistance(npc.getPosition()) < 7) {
                    int newPrayerPoints = (int) (target.prayerPoint * 0.67);
                    target.prayerPoint = newPrayerPoints;
                    int damage = Misc.random(30, 50);
                    npc.appendHeal(damage, Hitmark.HEAL_PURPLE);
                    target.appendDamage(damage, Hitmark.HIT);
                }
            }
        }, 1);
    }
}
