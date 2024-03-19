package io.Odyssey.model.entity.player.packets;

import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class Report implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String player = Misc.longToReportPlayerName(c.inStream.readQWord2()).replaceAll("_", " ");
		byte rule = (byte) c.inStream.readUnsignedByte();
	}

}