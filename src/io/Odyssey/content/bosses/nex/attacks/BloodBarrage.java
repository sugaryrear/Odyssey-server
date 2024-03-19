package io.Odyssey.content.bosses.nex.attacks;

import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

import java.util.List;

public class BloodBarrage {
    public BloodBarrage(NPC npc, Player player, List<Player> players, int damage) {
        if(players == null || player == null)
            return;
        BloodBarrage(npc, player, players, damage);
    }

    public BloodBarrage(CycleEvent cycleEvent, Player randomTarget, List<Player> targets, int random) {
    }

    void BloodBarrage(NPC npc, Player player, List<Player> players, int damage) {
        for (Player possibleTargets:
                players) {
            if(player.getPosition().getAbsDistance(possibleTargets.getPosition()) <= 3) {
                possibleTargets.gfx100(377);
                int dam;
                if(possibleTargets.protectingMagic())
                    dam = Misc.random(15);
                else
                    dam = Misc.random(33);
                npc.appendHeal(dam, Hitmark.HEAL_PURPLE);
                possibleTargets.appendDamage(dam, Hitmark.HIT);
            }

        }
        npc.appendHeal(damage, Hitmark.HEAL_PURPLE);
    }
}
