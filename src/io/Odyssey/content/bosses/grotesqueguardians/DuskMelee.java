package io.Odyssey.content.bosses.grotesqueguardians;

import io.Odyssey.content.bosses.mimic.MimicNpc;
import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.Npcs;

import java.util.function.Consumer;
import java.util.function.Function;

public class DuskMelee implements Function<GrotesqueGuardianNpc, NPCAutoAttack> {

    @Override
    public NPCAutoAttack apply(GrotesqueGuardianNpc nightmare) {
        int anim = 7785;
        if (nightmare.getInstance() != null) {
            if (((GrotesqueInstance) nightmare.getInstance()).phase == 4) {
                anim = 7800;
            }
        }
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(anim))
                .setCombatType(CombatType.MELEE)
                .setMaxHit(15)
                .setHitDelay(2)
                .setAttackDelay(6)
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