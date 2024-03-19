package io.Odyssey.model.entity.player.packets.action;

import io.Odyssey.Server;
import io.Odyssey.content.preset.PresetManager;
import io.Odyssey.model.entity.player.PacketType;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.world.Clan;
import io.Odyssey.util.Misc;

public class EnterStringInput implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		String string = player.getInStream().readString();

		if (player.stringInputHandler != null) {
			player.stringInputHandler.handle(player, string);
			return;
		}

		if (player.documentGraphic) {
			player.sendMessage("Test: " + string);
			return;
		}
		
		if (player.viewingPresets) {
			PresetManager.getSingleton().updateName(player, string);
			return;
		}
		
		if (string != null && string.length() > 0) {
			if (player.clan == null) {
				Clan clan = Server.clanManager.getClan(string);
				if (clan != null) {
					clan.addMember(player);
				} else if (string.equalsIgnoreCase(player.getLoginName())) {
					Server.clanManager.create(player);
				} else {
					player.sendMessage(Misc.formatPlayerName(string) + " has not created a clan yet.");
				}
//				player.getPA().refreshSkill(21);
//				player.getPA().refreshSkill(22);
//				player.getPA().refreshSkill(23);
			}
		}
	}

}