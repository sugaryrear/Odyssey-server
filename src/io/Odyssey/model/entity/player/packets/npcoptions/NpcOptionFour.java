package io.Odyssey.model.entity.player.packets.npcoptions;

import io.Odyssey.Server;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.skills.slayer.SlayerRewardsInterface;
import io.Odyssey.content.skills.slayer.SlayerRewardsInterfaceData;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

/*
 * @author Matt
 * Handles all 4th options on non playable characters.
 */

public class NpcOptionFour {

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		if (PetHandler.isPet(npcType)) {
			if (PetHandler.getOptionForNpcId(npcType) == "fourth") {
				if (PetHandler.pickupPet(player, npcType, true))
					return;
			}
		}
		player.clickNpcType = 0;
		player.clickedNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;
		switch (npcType) {
		case 17: //Rug merchant - Sophanem
			player.startAnimation(2262);
			AgilityHandler.delayFade(player, "NONE", 3285, 2815, 0, "You step on the carpet and take off...", "at last you end up in sophanem.", 3);
			break;

		case 2581:
			player.getPA().startTeleport(3039, 4788, 0, "modern", false);
			player.teleAction = -1;
			break;

		case 402:
		case 401:
			case 403:
			case 404:
		case 405:
		case 6797:
		case 7663:
		case 8761:
		case 5870:
			SlayerRewardsInterface.open(player, SlayerRewardsInterfaceData.Tab.TASK);
			player.sendMessage("You have <col=a30027>" + Misc.insertCommas(player.getSlayer().getPoints()) + " </col>slayer points.");

			//player.getSlayer().handleInterface("buy");
			break;
			
		case 1501:
			player.getShops().openShop(23);
			break;
		case 5984:
			player.getShops().openShop(176);
			break;

		case 308:
			player.getDH().sendDialogues(545, npcType);
			break;
		}
	}

}
