package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class GraniteMaulSpecialAttack extends Special {

	public GraniteMaulSpecialAttack() {
		super(5.0, 1, 1, new int[] {Items.GRANITE_MAUL, Items.GRANITE_MAUL_OR });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {

	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
