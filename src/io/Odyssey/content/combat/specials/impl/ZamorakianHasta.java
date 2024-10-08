package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class ZamorakianHasta extends Special {

	public ZamorakianHasta() {
		super(2.5, 1.00, 1.00, new int[] { 1249, 1263, 5716, 5730, 11824, 11889 });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.startAnimation(405);
		player.gfx100(253);
		if (target instanceof Player) {
			Player other = (Player) target;
			if (player.playerAttackingIndex > 0) {//if attacking a player
				other.getPA().getSpeared(player.absX, player.absY, 1);
				other.gfx0(80);
			}
		}
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {

	}

}
