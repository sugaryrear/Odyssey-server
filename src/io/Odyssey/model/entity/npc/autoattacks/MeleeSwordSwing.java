package io.Odyssey.model.entity.npc.autoattacks;

import io.Odyssey.content.combat.npc.NPCAutoAttackBuilder;
import io.Odyssey.model.Animation;
import io.Odyssey.model.CombatType;

public class MeleeSwordSwing extends NPCAutoAttackBuilder {

    public MeleeSwordSwing(int maxHit) {
        setAttackDelay(4);
        setMaxHit(maxHit);
        setAnimation(new Animation(451));
        setCombatType(CombatType.MELEE);
    }
}
