package io.Odyssey.content.bosses.nex.attacks;

import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.Projectile;
import io.Odyssey.model.ProjectileBase;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

import java.util.List;

public class IceBarrage {
    public IceBarrage(Player player, List<Player> players) {
        IceBarrage(player, players);
    }

    void IceBarrage(Player player, List<Player> players) {
        for (Player possibleTargets:
                players) {
            if(player.getPosition().getAbsDistance(possibleTargets.getPosition()) <= 3) {
                possibleTargets.gfx100(369);
                int dam;
                if(possibleTargets.protectingMagic())
                    dam = Misc.random(10);
                else
                    dam = Misc.random(10);
                possibleTargets.appendDamage(dam, Hitmark.HIT);
                possibleTargets.freezeDelay = 15;
            }

        }
    }
}
