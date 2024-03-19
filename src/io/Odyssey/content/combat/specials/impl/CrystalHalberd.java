package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.core.HitDispatcher;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;

public class CrystalHalberd extends Special {

	public CrystalHalberd() {
		super(3.0, 1.00, 1.10, new int[] {Items.CRYSTAL_HALBERD });
	}

	@Override
	public void activate(Player player, Entity target, Damage damage) {
		player.gfx100(1232);
		player.startAnimation(1203);
	}

	@Override
	public void hit(Player player, Entity target, Damage damage) {
		if (target instanceof NPC) {
			NPC other = (NPC) target;
			if (other != null && player.npcAttackingIndex > 0) {
				if (player.goodDistance(player.getX(), player.getY(), other.getX(), other.getY(), other.getSize() + 2) && other.getSize() > 1) {
					HitDispatcher.getHitEntity(player, target).playerHitEntity(CombatType.MELEE, null);
				}
			}
		}
	}

}
