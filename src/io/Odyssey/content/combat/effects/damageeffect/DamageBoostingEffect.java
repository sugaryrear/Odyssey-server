package io.Odyssey.content.combat.effects.damageeffect;

import io.Odyssey.content.combat.effects.damageeffect.DamageEffect;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public interface DamageBoostingEffect extends DamageEffect {

    double getMaxHitBoost(Player attacker, Entity defender);

}
