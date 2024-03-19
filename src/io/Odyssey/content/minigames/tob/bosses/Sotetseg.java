package io.Odyssey.content.minigames.tob.bosses;

import com.google.common.collect.Lists;
import io.Odyssey.content.combat.npc.NPCAutoAttack;
import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.minigames.tob.TobBoss;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.ProjectileBaseBuilder;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;

public class Sotetseg extends TobBoss {

    private final Animation ATTACK_ANIMATION = new Animation(8139);

    public Sotetseg(InstancedArea instancedArea) {
        super(Npcs.SOTETSEG_2, new Position(3278, 4326, instancedArea.getHeight()), instancedArea);

        setNpcAutoAttacks(Lists.newArrayList(
                new NPCAutoAttackBuilder()
                        .setAnimation(ATTACK_ANIMATION)
                        .setCombatType(CombatType.MELEE)
                        .setHitDelay(1)
                        .setMaxHit(45)
                        .setAttackDelay(5)
                        .createNPCAutoAttack(),
                getProjectileAttack(CombatType.MAGE),
                getProjectileAttack(CombatType.RANGE)
        ));
    }


    private NPCAutoAttack getProjectileAttack(CombatType type) {
        return new NPCAutoAttackBuilder()
                .setMultiAttack(true)
                .setSelectPlayersForMultiAttack(npcCombatAttack -> getInstance().getPlayers())
                .setAnimation(ATTACK_ANIMATION)
                .setCombatType(type)
                .setHitDelay(4)
                .setMaxHit(45)
                .setAttackDelay(5)
                .setDistanceRequiredForAttack(25)
                .setProjectile(new ProjectileBaseBuilder()
                        .setProjectileId(type == CombatType.MAGE ? 1606 : 1607)
                        .setSendDelay(2)
                        .setCurve(0)
                        .setStartHeight(43)
                        .setEndHeight(43)
                .createProjectileBase())
                .createNPCAutoAttack();
    }

    @Override
    public int getDeathAnimation() {
        return 8140;
    }
}
