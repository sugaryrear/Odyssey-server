package io.Odyssey.content.combat.effects.damageeffect.impl;

import java.util.Optional;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.effects.damageeffect.DamageEffect;
import io.Odyssey.model.entity.HealthStatus;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class TridentOfTheSwampEffect implements DamageEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		defender.getHealth().proposeStatus(HealthStatus.VENOM, damage.getAmount(), Optional.of(attacker));
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		defender.getHealth().proposeStatus(HealthStatus.VENOM, damage.getAmount(), Optional.of(attacker));
	}

	@Override
	public boolean isExecutable(Player operator) {
		return operator.getItems().isWearingItem(12899) && Misc.random(3) == 0;
	}

}
