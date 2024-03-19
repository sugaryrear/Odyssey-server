package io.Odyssey.content.minigames.tob;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.tob.instance.TobInstance;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;

public class TobBoss extends NPC {

    public TobBoss(int npcId, Position position, InstancedArea instancedArea) {
        super(npcId, position);
        instancedArea.add(this);
        getBehaviour().setRespawn(false);
        getBehaviour().setAggressive(true);
    }

    public void onDeath() {
        Entity killer = calculateKiller();
        if (getInstance() != null) {
            getInstance().getPlayers().forEach(plr -> {
                int points = 4;
                if (killer != null && killer.equals(plr)) {
                    points += 2;
                }
                ((TobInstance) plr.getInstance()).getMvpPoints().award(plr, points);
                ((TobInstance) plr.getInstance()).getFoodRewards().award(plr, points);
            });
        }
    }

}
