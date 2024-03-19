package io.Odyssey.model.entity.player.packets.objectoptions;

import io.Odyssey.Server;
import io.Odyssey.content.dialogue.impl.OutlastLeaderboard;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;

import static io.Odyssey.model.shops.ShopAssistant.mageshop;

public class ObjectOptionFour {
	
	public static void handleOption(final Player c, int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		
		if (c.getRights().isOrInherits(Right.OWNER) && c.debugMessage)
			c.sendMessage("Clicked Object Option 4:  "+objectType+"");

		if (OutlastLeaderboard.handleInteraction(c, objectType, 4))
			return;

		switch (objectType) {
			case 12202:
			c.sendMessage("There are currently "+Boundary.getPlayersInBoundary(Boundary.FALADOR_MOLE_LAIR)+" players inside the mole lair.");

				break;
			case 12309://culinaromancer chest
				if(c.getQuesting().getQuestList().get(2).isQuestCompleted()){
					c.getShops().openShop(156);
				}
				break;
		case 31858:
		case 29150:
			c.setSidebarInterface(6, 938);
			c.playerMagicBook = 0;
			c.sendMessage("You feel a drain on your memory.");
			break;
		case 8356://streehosidius
			c.getPA().movePlayer(1679, 3541, 0);
			break;
		}
	}

}
