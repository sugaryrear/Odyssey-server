package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.range.RangeData;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;

/**
 *
 * @author Jason MacKeigan
 * @date Apr 4, 2015, 2015, 11:44:39 PM
 */
public class ZaryteCrossbow extends Special {

    public ZaryteCrossbow() {
        super(4.0, 2.0, 1.0, new int[] { 26374 });
    }

    @Override
    public void activate(Player player, Entity target, Damage damage) {
        player.usingBow = true;
        player.startAnimation(9166);
        if (player.playerAttackingIndex > 0 && target instanceof Player) {
            RangeData.fireProjectilePlayer(player, (Player) target, 50, 70, 1995, 43, 31, 37, 10);
        } else if (player.npcAttackingIndex > 0 && target instanceof NPC) {
            RangeData.fireProjectileNpc(player, (NPC) target, 50, 70, 1995, 43, 31, 37, 10);
        }
    }

    @Override
    public void hit(Player player, Entity target, Damage damage) {

    }

}
