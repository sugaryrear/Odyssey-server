package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class DragonSword extends Special {

	public DragonSword() {
		super(4, 1.25, 1.25, new int[] { 21009 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx100(1369);
		player.startAnimation(7515);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {
		if (target instanceof Player) {
			if (damage.getAmount() > 0) {
				CombatPrayer.resetOverHeads((Player) target);
			}
		}

	}

}
