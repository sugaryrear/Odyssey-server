package io.Odyssey.content.bosses.nex.attacks;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;

import java.util.List;

public class Containment {
    public Containment(NPC npc, List<Player> targets) {
        Containment(npc, targets);
    }

    void Containment(NPC npc, List<Player> targets) {
        Boundary iceBounds = new Boundary(npc.getX() - 2, npc.absY - 3, npc.getX() + 3, npc.getY() + 2);

        for(int y = -3; y < 4; y++) {
            Server.getGlobalObjects().add(new GlobalObject(42943, npc.absX + -3, npc.absY + y, 0, 0, 10, 6, -1));
            Server.getGlobalObjects().add(new GlobalObject(42943, npc.absX + 3, npc.absY + y, 0, 0, 10, 6, -1));
            Server.getGlobalObjects().add(new GlobalObject(42943, npc.absX + y, npc.absY + -3, 0, 0, 10, 6, -1));
            Server.getGlobalObjects().add(new GlobalObject(42943, npc.absX + y, npc.absY + 3, 0, 0, 10, 6, -1));
        }


        for (Player player:
                targets) {
            if(iceBounds.in(player)) {
                player.appendDamage(Misc.random(1, 10), Hitmark.HIT);
            }
        }
    }
}