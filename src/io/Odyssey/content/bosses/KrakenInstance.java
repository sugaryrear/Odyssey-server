package io.Odyssey.content.bosses;

import io.Odyssey.content.instances.InstanceConfiguration;
import io.Odyssey.content.instances.impl.LegacySoloPlayerInstance;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 12/5/19
 *
 */
public class KrakenInstance extends LegacySoloPlayerInstance {

	public KrakenInstance(Player player, Boundary boundary) {
		super(InstanceConfiguration.CLOSE_ON_EMPTY_RESPAWN, player, boundary);
	}

	@Override
	public void onDispose() { }
}
