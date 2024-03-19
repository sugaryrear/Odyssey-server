package io.Odyssey.model.cycleevent.impl;

import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.Health;
import io.Odyssey.model.entity.HealthStatus;

public class VenomResistanceEvent extends Event<Entity> {

	public VenomResistanceEvent(Entity attachment, int ticks) {
		super("venom_resistance_event", attachment, ticks);
	}

	@Override
	public void execute() {
		super.stop();
		if (attachment == null) {
			return;
		}
		Health health = attachment.getHealth();
		health.removeNonsusceptible(HealthStatus.VENOM);
	}

}
