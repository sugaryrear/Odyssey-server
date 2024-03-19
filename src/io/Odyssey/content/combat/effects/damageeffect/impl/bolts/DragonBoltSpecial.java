package io.Odyssey.content.combat.effects.damageeffect.impl.bolts;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.effects.damageeffect.DamageBoostingEffect;
import io.Odyssey.content.combat.range.RangeData;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class DragonBoltSpecial implements DamageBoostingEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		if (defender.antifireDelay > 0 || defender.getItems().isWearingAnyItem(11283, 11284, 1540)) {
			return;
		}
		int change = Misc.random((int) (damage.getAmount() * 1.45));
		damage.setAmount(change);
		RangeData.createCombatGraphic(defender, 756, false);
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {
		if (defender.getDefinition().getName() != null && defender.getDefinition().getName().toLowerCase().contains("dragon")) {
			return;
		}
		attacker.ignoreDefence = true;
		RangeData.createCombatGraphic(defender, 756, false);
	}

	@Override
	public boolean isExecutable(Player operator) {
		return RangeData.boltSpecialAvailable(operator, Items.DRAGONSTONE_BOLTS_E, Items.DRAGONSTONE_DRAGON_BOLTS_E);
	}

	@Override
	public double getMaxHitBoost(Player attacker, Entity defender) {
		return 0.45;
	}

}
