package io.Odyssey.content.bosses.nex.attacks;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;

import java.util.List;

public class IcePrison {
    public IcePrison(List<Player> targets) {
        IcePrison(targets);
    }

    void IcePrison(List<Player> targets) {
        Player target = targets.get(Misc.random(targets.size() - 1));
        Boundary iceBounds = new Boundary(target.getX() - 1, target.absY - 2, target.getX() + 2, target.getY() + 1);

        for(int y = -3; y < 4; y++) {
            Server.getGlobalObjects().add(new GlobalObject(42943, target.absX + -3, target.absY + y, 0, 0, 10, 6, -1));
            Server.getGlobalObjects().add(new GlobalObject(42943, target.absX + 3, target.absY + y, 0, 0, 10, 6, -1));
            Server.getGlobalObjects().add(new GlobalObject(42943, target.absX + y, target.absY + -3, 0, 0, 10, 6, -1));
            Server.getGlobalObjects().add(new GlobalObject(42943, target.absX + y, target.absY + 3, 0, 0, 10, 6, -1));

        }

        CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if(container.getTotalTicks() > 2) {
                    for (Player player :
                            targets) {
                        if (iceBounds.in(player)) {
                            player.appendDamage(Misc.random(10, 20), Hitmark.HIT);
                        }
                    }
                    container.stop();
                }
            }
        }, 1);

    }
}