package io.Odyssey.content.bosses.nex.attacks;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;

import java.util.List;

public class ShadowSmash {
    public ShadowSmash(List<Player> targets) {
        ShadowSmash(targets);
    }

    void ShadowSmash(List<Player> targets) {
        for (Player player:
                targets) {
            if(player != null) {
                Position shadowPos = player.getPosition();
                Server.getGlobalObjects().add(new GlobalObject(42942, player.getX(), player.getY(), 0, 0, 10, 6, -1));
                CycleEventHandler.getSingleton().addEvent(new Object(), new CycleEvent() {
                    @Override
                    public void execute(CycleEventContainer container) {
                        if(container.getTotalTicks() == 6 && player.getPosition().getAbsDistance(shadowPos) == 0) {
                            player.appendDamage(Misc.random(10, 20), Hitmark.HIT);
                        }
                    }
                }, 1);
            }
        }
    }
}