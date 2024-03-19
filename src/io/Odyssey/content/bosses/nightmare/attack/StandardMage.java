package io.Odyssey.content.bosses.nightmare.attack;

import java.util.function.Consumer;
import java.util.function.Function;

import io.Odyssey.content.bosses.nightmare.Nightmare;
import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.combat.npc.NPCCombatAttack;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.ProjectileBase;
import io.Odyssey.model.ProjectileBaseBuilder;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.util.Misc;

public class StandardMage implements Function<Nightmare, NPCAutoAttack> {

    // In the video I saw random projectiles
    // he had other players hidden
    public static Consumer<NPCCombatAttack> getProjectileThrow(ProjectileBase projectileBase) {
        return new Consumer<NPCCombatAttack>() {
            @Override
            public void accept(NPCCombatAttack npcCombatAttack) {
                int it = 0;
                for (Position border : npcCombatAttack.getNpc().getBorder()) {
                    if (++it % 2 == 0) {
                        Position delta = border.delta(npcCombatAttack.getNpc().getCenterPosition());
                        projectileBase.createTargetedProjectile(npcCombatAttack.getNpc(),
                                border.translate(-delta.getX() + Misc.trueRand(3), -delta.getY() + Misc.trueRand(3)))
                                .send(npcCombatAttack.getNpc().getInstance());
                    }
                }
            }
        };
    }

    private static ProjectileBase projectile() {
        return new ProjectileBaseBuilder()
                .setSendDelay(4)
                .setSpeed(15)
                .setStartHeight(90)
                .setProjectileId(1764)
                .createProjectileBase();
    }

    @Override
    public NPCAutoAttack apply(Nightmare nightmare) {
        return new NPCAutoAttackBuilder()
                .setSelectPlayersForMultiAttack(NPCAutoAttack.getDefaultSelectPlayersForAttack())
                .setProjectile(projectile())
                .setPrayerProtectionPercentage(new Function<NPCCombatAttack, Double>() {
                    @Override
                    public Double apply(NPCCombatAttack npcCombatAttack) {
                        return 0.3;
                    }
                })
                //.setOnAttack(getProjectileThrow(projectile()))
                .setEndGraphic(new Graphic(1765, Graphic.GraphicHeight.HIGH))
                .setAnimation(new Animation(8595))
                .setCombatType(CombatType.MAGE)
                .setMaxHit(30)
                .setHitDelay(4)
                .setAttackDelay(6)
                .setDistanceRequiredForAttack(16)
                .setMultiAttack(true)
                .createNPCAutoAttack();
    }
}
