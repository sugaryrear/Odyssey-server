package io.Odyssey.content.teleportation.newest.ui.ui;

import io.Odyssey.model.entity.player.Player;

public interface TeleportInfo {

	public void send(Player player);
	
	public static void reset(Player player) {
		player.getPA().sendNPCOnInterfaceReset(63116);
		player.getPA().sendFrame126("", 63230);
		player.getPA().sendFrame126("", 63231);
		player.getPA().sendFrame126("", 63232);
	}
}
