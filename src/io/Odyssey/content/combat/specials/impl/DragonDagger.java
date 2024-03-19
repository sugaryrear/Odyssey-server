package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.core.HitDispatcher;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class DragonDagger extends Special {

	public DragonDagger() {
		super(2.5, 1.15, 1.15, new int[] { 1215, 1231, 5680, 5698 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx100(252);
		player.startAnimation(1062);
		HitDispatcher.getHitEntity(player, target).playerHitEntity(CombatType.MELEE, SecondSpecialHit.DRAGON_DAGGER_HIT_2);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
