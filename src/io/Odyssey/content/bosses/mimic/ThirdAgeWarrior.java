package io.Odyssey.content.bosses.mimic;

import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;

import java.util.function.Function;

public class ThirdAgeWarrior implements Function<MimicNpc, NPCAutoAttack> {

    @Override
    public NPCAutoAttack apply(MimicNpc nightmare) {
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(390))
                .setCombatType(CombatType.MELEE)
                .setMaxHit(18)
                .setHitDelay(2)
                .setAttackDelay(5)
                .setDistanceRequiredForAttack(1)
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.2d;
                    }
                })
                .createNPCAutoAttack();
    }
}