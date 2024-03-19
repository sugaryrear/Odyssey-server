package io.Odyssey.content.bosses.nex.attacks;

import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

import java.util.List;

public class Wrath {
    public Wrath(NPC npc, List<Player> targets) {
        Wrath(npc, targets);
    }

    void Wrath(NPC npc, List<Player> targets) {
        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (container.getTotalTicks() > 5) {
                    for (Player player :
                            targets) {
                        if(player.getPosition().getAbsDistance(npc.getPosition()) < 5) {
                            player.appendDamage(Misc.random(10, 40), Hitmark.HIT);
                        }
                    }
                    container.stop();
                }
            }
        }, 1);
    }


}
//                    NexAngelOfDeath.wrathTimer.setDuration(4);
//                    NexAngelOfDeath.wrathDMGTimer.setDuration(4);
//                    for (int x = npc.getX(); x < npc.getX() + 5; x++) {
//                        for (int y = npc.getY(); y < npc.getY() + 5; y++) {
//                            Server.playerHandler.sendStillGfx(new StillGraphic(2013, new Position(x, y)));
//                            NexAngelOfDeath.wrathLocations.add(new Position(x, y));