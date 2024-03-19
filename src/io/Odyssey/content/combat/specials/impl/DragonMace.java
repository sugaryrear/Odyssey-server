package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class DragonMace extends Special {

	public DragonMace() {
		super(2.5, 1.25, 1.50, new int[] { 1434 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(1060);
		player.gfx100(251);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
