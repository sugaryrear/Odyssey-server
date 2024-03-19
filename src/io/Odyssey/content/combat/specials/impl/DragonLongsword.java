package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class DragonLongsword extends Special {

	public DragonLongsword() {
		super(2.5, 1.0, 1.25, new int[] { 1305 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx100(248);
		player.startAnimation(1058);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}
}