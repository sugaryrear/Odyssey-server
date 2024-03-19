package io.Odyssey.content.combat.effects.damageeffect.impl.bolts;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.effects.damageeffect.DamageBoostingEffect;
import io.Odyssey.content.combat.range.RangeData;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class SapphireBoltSpecial implements DamageBoostingEffect {

	@Override
	public void execute(Player attacker, Player defender, Damage damage) {
		int change = Misc.random(damage.getAmount());
		damage.setAmount(change);
		RangeData.createCombatGraphic(defender, 751, false);

		if (attacker.playerAttackingIndex > 0) {
			defender.playerLevel[5] -= 2;
			defender.getPA().refreshSkill(5);
			defender.sendMessage("Your prayer has been lowered!");
			attacker.playerLevel[5] += 2;
			if (attacker.playerLevel[5] >= attacker.getPA().getLevelForXP(attacker.playerXP[5])) {
				attacker.playerLevel[5] = attacker.getPA().getLevelForXP(attacker.playerXP[5]);
			}
			attacker.getPA().refreshSkill(5);
		}
	}

	@Override
	public void execute(Player attacker, NPC defender, Damage damage) {

		RangeData.createCombatGraphic(defender, 751, false);
	}

	@Override
	public boolean isExecutable(Player operator) {
		return RangeData.boltSpecialAvailable(operator, Items.SAPPHIRE_BOLTS_E, Items.SAPPHIRE_DRAGON_BOLTS);
	}

	@Override
	public double getMaxHitBoost(Player attacker, Entity defender) {
		return 0;
	}

}