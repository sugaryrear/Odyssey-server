package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.core.HitDispatcher;
import io.Odyssey.content.combat.range.Arrow;
import io.Odyssey.content.combat.range.RangeData;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;

public class HandCannon extends Special {

    public HandCannon() {

        super(5.5, 1.0, 1.3, new int[] { 30051 });
    }

    @Override
    public void activate(Player player, Entity target, Damage damage) {
        int projectile = Arrow.matchesMaterial(player.playerEquipment[Player.playerArrows], Arrow.HAND_CANNON_SHOT) ? 2029 : 2030;
        player.startAnimation(9111);
        player.gfx100(RangeData.getRangeStartGFX(player));
        if (player.playerAttackingIndex > 0 && target instanceof Player) {
            player.startGraphic(new Graphic(2030));
            RangeData.fireProjectilePlayer(player, (Player) target, 50, 100, projectile, 60, 31, 53, 25);
            player.startGraphic(new Graphic(2030));
            RangeData.fireProjectilePlayer(player, (Player) target, 50, 100, projectile, 60, 31, 63, 25);
            HitDispatcher.getHitEntity(player, target).playerHitEntity(CombatType.RANGE, SecondSpecialHit.HAND_CANNON_2);
        } else if (player.npcAttackingIndex > 0 && target instanceof NPC) {
            player.startGraphic(new Graphic(2030));
            RangeData.fireProjectileNpc(player, (NPC) target, 50, 100, projectile, 60, 31, 53, 25);
            player.startGraphic(new Graphic(2030));
            RangeData.fireProjectileNpc(player, (NPC) target, 50, 100, projectile, 60, 31, 63, 25);
            HitDispatcher.getHitEntity(player, target).playerHitEntity(CombatType.RANGE, SecondSpecialHit.HAND_CANNON_2);
        }
        player.getItems().deleteArrow();
        player.getItems().deleteArrow();
        player.getItems().deleteArrow();
        player.getItems().deleteArrow();
    }

    @Override
    public void hit(Player player, Entity target, Damage damage) {

    }

}
