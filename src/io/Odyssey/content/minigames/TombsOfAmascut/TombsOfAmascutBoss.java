package io.Odyssey.content.minigames.TombsOfAmascut;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.TombsOfAmascut.instance.TombsOfAmascutInstance;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;

public class TombsOfAmascutBoss extends NPC {

    public TombsOfAmascutBoss(int npcId, Position position, InstancedArea instancedArea) {
        super(npcId, position);
        instancedArea.add(this);
        getBehaviour().setRespawn(false);
        getBehaviour().setAggressive(npcId == 11693 ? false : true);
    }

    public void onDeath() {
        Entity killer = calculateKiller();
        if (getInstance() != null) {
            getInstance().getPlayers().forEach(plr -> {
                int points = 4;
                if (killer != null && killer.equals(plr)) {
                    points += 2;
                }
                ((TombsOfAmascutInstance) plr.getInstance()).getMvpPoints().award(plr, points);
                ((TombsOfAmascutInstance) plr.getInstance()).getFoodRewards().award(plr, points);
            });
        }
    }

}
