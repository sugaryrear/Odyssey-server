package io.Odyssey.model.entity.player.packets.objectoptions;

import io.Odyssey.Server;
import io.Odyssey.content.bosses.nex.Nex;
import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.dialogue.impl.OutlastLeaderboard;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.model.collisionmap.ObjectDef;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.*;

/*
 * @author Matt
 * Handles all 3rd options for objects.
 */

public class ObjectOptionThree {

	public static void handleOption(final Player c, int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		// c.sendMessage("Object type: " + objectType);
		ObjectDef def = ObjectDef.getObjectDef(objectType);
		if ((def != null ? def.name : null) != null && def.name.toLowerCase().contains("bank")) {
			Listing.openPost(c, false);
			return;
		}
		if (c.getRights().isOrInherits(Right.OWNER) && c.debugMessage)
			c.sendMessage("Clicked Object Option 3:  "+objectType+"");

		if (OutlastLeaderboard.handleInteraction(c, objectType, 3))
			return;

		switch (objectType) {
			case 12309:
				Listing.openPost(c, false);
				break;
			case 42967:
			case 30169:
				int playersinnex = Boundary.getPlayersInBoundary(Boundary.NEX);
					c.sendMessage("There are currently "+playersinnex+" players inside.");

				break;
			case 23609://look inside
				int playersinkq = Boundary.getPlayersInBoundary(Boundary.KALPHITE_QUEEN);

					c.sendMessage("There are currently "+playersinkq+" players in the Kalphite Queen lair.");
			//	break;
			//	c.getPA().movePlayer(3507, 9494, 0);
				break;
			case 10177: // Dagganoth kings ladder climb down
				if (obX == 2546 && obY == 10143)
				AgilityHandler.delayEmote(c, "CLIMB_UP", 1798,4407,3, 2);
				//dialog opt
				//	c.getPA().movePlayer(2900, 4449, 0);
				break;



			case 16672://going up

				if(c.objectX == 2839 && c.objectY == 3537) { //warriors guild
					AgilityHandler.delayEmote(c, "CLIMB_UP", 2840, 3539, 2, 2);
				}
				if(c.objectX == 3204 && c.objectY == 3207) { //lumbridge
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3205, 3209, 2, 2);
				}
				if(c.objectX == 3204 && c.objectY == 3229) { //lumbridge
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3205, 3228, 2, 2);
				}

				break;

		case 31858:
		case 29150:
			c.sendMessage("You switch to the lunar spellbook.");
			c.setSidebarInterface(6, 29999);
			c.playerMagicBook = 2;
			break;

		case 29776:
		case 44596:
		case 29734:
		case 10777:
		//case 29879:
			c.objectDistance = 4;

			break;
		case 2884:
		case 16684:
		case 16683:
			if (c.absY == 3494 || c.absY == 3495 || c.absY == 3496) {
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", c.getX(), c.getY(), c.getHeight() - 1, 2);
			}
			break;
		case 29333:
			if (c.getMode().isIronmanType()) {
				c.sendMessage("@red@You are not permitted to make use of this.");			}
			Listing.collectMoney(c);
			
			break;
		case 6448:
			if (c.getMode().isIronmanType()) {
				Listing.openPost(c, false);	
			}
			c.sendMessage("@red@You cannot enter the trading post on this mode.");
			break;
		case 8356://streexerics
			c.getPA().movePlayer(1311, 3614, 0);
			break;
		case 7811:
			if (!c.getPosition().inClanWarsSafe()) {
				return;
			}
			c.getDH().sendDialogues(818, 6773);
			break;
		}
	}

}
