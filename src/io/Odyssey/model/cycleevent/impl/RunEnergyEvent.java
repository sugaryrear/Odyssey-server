package io.Odyssey.model.cycleevent.impl;

import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.Player;

public class RunEnergyEvent extends Event<Player> {

	/**
	 * The maximum amount of ticks that we have to wait for our run to regenerate
	 */
	private static final int MAXIMUM_TICKS = 12;

	/**
	 * The minimum amount of ticks that we have to wait for our run to regenerate
	 */
	private static final int MINIMUM_TICKS = 10;

	/**
	 * The amount of agility levels that impact run energy regeneration
	 */
	private static final int INTERVAL = 99 / (MAXIMUM_TICKS - MINIMUM_TICKS);

	/**
	 * The amount of ticks that must pass before energy can be restored
	 */
	private int ticksRequired;

	public RunEnergyEvent(Player attachment, int ticks) {
		super(attachment, ticks);
	}

	@Override
	public void execute() {
		if (attachment == null || attachment.isDisconnected() || attachment.getSession() == null) {
			super.stop();
			return;
		}
		if (attachment.getRunEnergy() > 10000 || attachment.getRunningDistanceTravelled() > 0) {
			return;
		}
	//	if (super.getElapsedTicks() >= ticksRequired) {
		int howmuchenergyrecovered = (attachment.playerLevel[attachment.playerAgility] / 6 ) + 8;
			attachment.setRunEnergy(attachment.getRunEnergy() + howmuchenergyrecovered, true);
		//	ticksRequired = super.getElapsedTicks() + updateTicksRequired();
	//	}
	}

	private int updateTicksRequired() {
		int level = Integer.min(99, attachment.playerLevel[Skill.AGILITY.getId()]);
		int reduction = level < INTERVAL ? 0 : level / INTERVAL;
		return Integer.max(MINIMUM_TICKS, MAXIMUM_TICKS - reduction);
	}

}
