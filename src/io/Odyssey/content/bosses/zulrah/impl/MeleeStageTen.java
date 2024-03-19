package io.Odyssey.content.bosses.zulrah.impl;

import io.Odyssey.content.bosses.zulrah.Zulrah;
import io.Odyssey.content.bosses.zulrah.ZulrahLocation;
import io.Odyssey.content.bosses.zulrah.ZulrahStage;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.entity.player.Player;

public class MeleeStageTen extends ZulrahStage {

	public MeleeStageTen(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead() || player == null || player.isDead
				|| zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		if (zulrah.getNpc().totalAttacks > 1 && zulrah.getNpc().attackTimer == 9) {
			player.getZulrahEvent().changeStage(11, CombatType.RANGE, ZulrahLocation.NORTH);
			zulrah.getNpc().totalAttacks = 0;
			zulrah.getNpc().setFacePlayer(true);
			container.stop();
			return;
		}
	}
}
