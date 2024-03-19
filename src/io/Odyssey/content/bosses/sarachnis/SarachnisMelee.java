package io.Odyssey.content.bosses.sarachnis;

import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.content.combat.npc.NPCCombatAttackHit;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.entity.player.Player;

import java.util.function.Consumer;
import java.util.function.Function;

public class SarachnisMelee implements Function<SarachnisNpc, NPCAutoAttack> {

    @Override
    public NPCAutoAttack apply(SarachnisNpc sarachnis) {
        Consumer<NPCCombatAttackHit> onDamage = t -> {
            if (t.getCombatHit().missed())
                return;
            if (t.getVictim().isPlayer()) {
                Player player = (Player) t.getVictim();
                if (!CombatPrayer.isPrayerOn(player, CombatPrayer.PROTECT_FROM_MELEE)) {
                t.getNpc().appendDamage(5, Hitmark.HEAL_PURPLE);
                 t.getNpc().getHealth().increase(10);
                }
            }
        };
        Consumer<NPCCombatAttack> onAttack = t -> {
            sarachnis.attackCounter++;
        };
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(8147))
                .setCombatType(CombatType.MELEE)
                .setSelectAutoAttack(attack -> attack.getNpc().distance(attack.getVictim().getPosition()) == 1)
                .setMaxHit(31)
                .setHitDelay(5)
                .setAttackDelay(5)
                .setDistanceRequiredForAttack(1)
                .setOnHit(onDamage)
                .setOnAttack(onAttack)
                .setSelectAutoAttack(npcCombatAttack -> npcCombatAttack.getNpc().distance(npcCombatAttack.getVictim().getPosition()) <= 1)
                .setPrayerProtectionPercentage(npcCombatAttack -> 0.2d)
                .createNPCAutoAttack();
    }
}