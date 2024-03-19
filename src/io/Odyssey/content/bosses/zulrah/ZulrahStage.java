package io.Odyssey.content.bosses.zulrah;

import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.entity.player.Player;

public abstract class ZulrahStage extends CycleEvent {

	protected Zulrah zulrah;

	protected Player player;

	public ZulrahStage(Zulrah zulrah, Player player) {
		this.zulrah = zulrah;
		this.player = player;
	}

}
