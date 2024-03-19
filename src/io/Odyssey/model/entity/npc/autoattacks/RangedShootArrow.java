package io.Odyssey.model.entity.npc.autoattacks;

import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.ProjectileBaseBuilder;

public class RangedShootArrow extends NPCAutoAttackBuilder {

    public RangedShootArrow(int maxHit) {
        setAttackDelay(3);
        setMaxHit(maxHit);
        setAnimation(new Animation(426));
        setCombatType(CombatType.RANGE);
        setDistanceRequiredForAttack(10);
        setHitDelay(3);
        setStartGraphic(new Graphic(19, Graphic.GraphicHeight.MIDDLE));
        setProjectile(new ProjectileBaseBuilder().setSendDelay(2).setProjectileId(11).createProjectileBase());
    }
}
