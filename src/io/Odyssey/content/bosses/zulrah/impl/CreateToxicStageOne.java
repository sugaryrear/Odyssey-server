package io.Odyssey.content.bosses.zulrah.impl;

import java.util.Arrays;

import io.Odyssey.content.bosses.zulrah.DangerousEntity;
import io.Odyssey.content.bosses.zulrah.DangerousLocation;
import io.Odyssey.content.bosses.zulrah.SpawnDangerousEntity;
import io.Odyssey.content.bosses.zulrah.Zulrah;
import io.Odyssey.content.bosses.zulrah.ZulrahLocation;
import io.Odyssey.content.bosses.zulrah.ZulrahStage;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Player;

public class CreateToxicStageOne extends ZulrahStage {

	public CreateToxicStageOne(Zulrah zulrah, Player player) {
		super(zulrah, player);
	}

	@Override
	public void execute(CycleEventContainer container) {
		if (container.getOwner() == null || zulrah == null || zulrah.getNpc() == null || zulrah.getNpc().isDead() || player == null || player.isDead
				|| zulrah.getInstancedZulrah() == null) {
			container.stop();
			return;
		}
		if (container.getTotalTicks() == 1) {
			zulrah.getNpc().setFacePlayer(false);
			CycleEventHandler.getSingleton().addEvent(player, new SpawnDangerousEntity(zulrah, player, Arrays.asList(DangerousLocation.values()),
							DangerousEntity.TOXIC_SMOKE, 40),1);
		} else if (container.getTotalTicks() >= 19) {
			zulrah.getNpc().totalAttacks = 0;
			zulrah.changeStage(2, CombatType.MELEE, ZulrahLocation.NORTH);
			container.stop();
		}
	}

}
