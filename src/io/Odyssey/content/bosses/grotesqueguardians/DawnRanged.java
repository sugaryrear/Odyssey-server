package io.Odyssey.content.bosses.grotesqueguardians;

import io.Odyssey.Server;
import io.Odyssey.content.bosses.mimic.MimicNpc;
import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.ProjectileBase;
import io.Odyssey.model.ProjectileBaseBuilder;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

import java.util.function.Consumer;
import java.util.function.Function;

public class DawnRanged implements Function<GrotesqueGuardianNpc, NPCAutoAttack> {

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(2)
                .setSpeed(30)
                .setStartHeight(90)
                .setProjectileId(1444)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(GrotesqueGuardianNpc dawn) {
        return new NPCAutoAttackBuilder()
                .setAnimation(new Animation(7770))
                .setCombatType(CombatType.RANGE)
                .setMaxHit(15)
                .setHitDelay(2)
                .setAttackDelay(6)
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
