package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.range.Arrow;
import io.Odyssey.content.combat.range.RangeData;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.content.combat.weapon.RangedWeaponType;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;

public class Bofa extends Special {

    public Bofa() {
        // bofa
        super(9.5, 1.0, 1.0, new int[] { 25865});
    }

    @Override
    public void activate(Player player, Entity target, Damage damage) {
        int projectile = Arrow.matchesMaterial(player.playerEquipment[Player.playerArrows], Arrow.DRAGON) ? 1099 : 1101;
        player.startAnimation(426);
        player.gfx100(RangeData.getRangeStartGFX(player));
        if (player.playerAttackingIndex > 0 && target instanceof Player) {
            RangeData.fireProjectilePlayer(player, (Player) target, 50, 100, 155, 60, 31, 53, 25);
        } else if (player.npcAttackingIndex > 0 && target instanceof NPC) {
            RangeData.fireProjectileNpc(player, (NPC) target, 50, 100, 155, 60, 31, 53, 25);
        }
        player.getItems().deleteArrow();
        player.getItems().deleteArrow();
    }

    @Override
    public void hit(Player player, Entity target, Damage damage) {

    }

}
