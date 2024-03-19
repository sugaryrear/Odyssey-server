package io.Odyssey.model.entity.player.packets;

import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;

public class MapRegionChange implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.checkforregionunlocks();
	}

}
