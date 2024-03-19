package io.Odyssey.content.bosses.mimic;

import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.ProjectileBase;
import io.Odyssey.model.ProjectileBaseBuilder;

import java.util.function.Function;

public class ThirdAgeRanger implements Function<MimicNpc, NPCAutoAttack> {

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(30)
                .setStartHeight(90)
                .setProjectileId(1120)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(MimicNpc nightmare) {
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(426))
                .setCombatType(CombatType.RANGE)
                .setMaxHit(31)
                .setHitDelay(3)
                .setAttackDelay(5)
                .setDistanceRequiredForAttack(24)
                .setMultiAttack(false)
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.3;
                    }
                })
                .setProjectile(projectile())
                .createNPCAutoAttack();
    }
}