package io.Odyssey.model.entity.player.packets.npcoptions;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.achievement_diary.impl.ArdougneDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.DesertDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.FaladorDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.FremennikDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.VarrockDiaryEntry;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.skills.herblore.PotionDecanting;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.definitions.NpcDef;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

/*
 * @author Matt
 * Handles all 3rd options on non playable characters.
 */

public class NpcOptionThree {

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		player.clickNpcType = 0;
		player.clickedNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;

		if (PetHandler.isPet(npcType)) {
			if (PetHandler.getOptionForNpcId(npcType) == "three") {
				if (PetHandler.pickupPet(player, npcType, true))
					return;
			}
		}
		NpcDef npcDefinition = NpcDef.forId(npcType);
		if(npcDefinition != null)
			if(npcDefinition.getName() != null && npcDefinition.getName().toLowerCase().contains("banker")  || npcDefinition.getName().toLowerCase().contains("reis")  || npcDefinition.getName().toLowerCase().contains("ket-yil")|| npcDefinition.getName().toLowerCase().contains("ket-zuh") || npcDefinition.getName().toLowerCase().contains("jade"))
				Listing.openPost(player, false);
		NPC npc = NPCHandler.npcs[player.clickedNpcIndex];
		switch (npcType) {


			case 4642://shantay insta buy pass
				if(player.getItems().playerHasItem(995, 5)){
					player.getItems().addItem(1854, 1);
					player.getItems().deleteItem(995, 5);
				} else {
					player.sendMessage("You need 5 gp to purchase a shantay pass.");
				}
				break;

			case 15565:
			case 15573:
			case 15572:
				if (npc.spawnedBy == player.getIndex() && player.hasPetSpawned) {
					boolean hasstoragepet = PetHandler.hasstoragepetout(player);
					if(hasstoragepet){
						player.getPakYak().openPakYak();
					}

				} else {
					player.sendMessage("not your pet");
				}
				break;

//		case 6637:
//            if (player.getItems().freeSlots() < 1) {
//                player.sendMessage("Your inventory is full.");
//                return;
//            }
//            NPCHandler.npcs[player.clickedNpcIndex].absX = 0;
//            NPCHandler.npcs[player.clickedNpcIndex].absY = 0;
//            NPCHandler.npcs[player.clickedNpcIndex] = null;
//            player.petSummonId = -1;
//            player.hasFollower = false;
//           player.getItems().addItem(12654, 1);
//           break;
//       case 6638:
//            if (player.getItems().freeSlots() < 1) {
//                player.sendMessage("Your inventory is full.");
//                return;
//            }
//            NPCHandler.npcs[player.clickedNpcIndex].absX = 0;
//            NPCHandler.npcs[player.clickedNpcIndex].absY = 0;
//            NPCHandler.npcs[player.clickedNpcIndex] = null;
//            player.petSummonId = -1;
//            player.hasFollower = false;
//           player.getItems().addItem(12647, 1);
//           break;
			case 1600:
				if (player.getMode().isIronmanType()) {
					player.sendMessage("@red@You are not permitted to make use of this.");
					return;
				}
				Listing.openPost(player, false);
				break;
		case 8781:
			Server.getDropManager().search(player, "Donator Boss");
			break;
		case 1428:
			player.getPrestige().openShop();
			break;
		case 1909:
			player.getDH().sendDialogues(903, 1909);
			break;
		case 2897:
			player.getPA().c.itemAssistant.openUpBank();
			break;
		case 2989:
			player.getPrestige().openShop();
			break;
		case 4321:
			player.getShops().openShop(119);
			player.sendMessage("You currently have @red@"+player.bloodPoints+" @bla@Blood Money Points!");
			break;

		case 6773:
			player.isSkulled = true;
			player.skullTimer = Configuration.EXTENDED_SKULL_TIMER;
			player.headIconPk = 0;
			player.getPA().requestUpdates();
			player.sendMessage("@cr10@@blu@You are now skulled.");
			break;
		case 2200:
			player.getPA().c.itemAssistant.openUpBank();
			break;
		case 1306:
			if (player.getItems().isWearingItems()) {
				player.sendMessage("You must remove your equipment before changing your appearance.");
				player.canChangeAppearance = false;
			} else {
				player.getPA().showInterface(3559);
				player.canChangeAppearance = true;
			}
			break;
		case 17: //Rug merchant - Nardah
			player.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.TRAVEL_NARDAH);
			player.startAnimation(2262);
			AgilityHandler.delayFade(player, "NONE", 3402, 2916, 0, "You step on the carpet and take off...", "at last you end up in nardah.", 3);
			break;
		
		case 3936:
			AgilityHandler.delayFade(player, "NONE", 2310, 3782, 0, "You board the boat...", "And end up in Neitiznot", 3);
			player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.TRAVEL_NEITIZNOT);
			break;
			
		case 402:
		case 401:
		case 405:
			case 403:
			case 404:
		case 6797:
		case 7663:
		case 8761:
		case 5870:
			player.getShops().openShop(66);
		//	player.sendMessage("You currently have <col=a30027>" + Misc.insertCommas(player.getSlayer().getPoints()) + " </col>slayer points.");
			break;
		case 308:
			player.getDH().sendDialogues(548, 308);
			break;

		case 836:
			player.getShops().openShop(103);
			break;
		case Npcs.BOB_BARTER_HERBS:
			PotionDecanting.decantInventory(player);
			player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.POTION_DECANT);
			break;
		case 2581:
			if (Boundary.isIn(player, Boundary.VARROCK_BOUNDARY)) {
				player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.TELEPORT_ESSENCE_VAR);
			}
			if (Boundary.isIn(player, Boundary.ARDOUGNE_BOUNDARY)) {
				player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ESSENCE_ARD);
			}
			if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
				player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_ESSENCE_FAL);
			}
			player.teleToAbyss();
		//	player.getPA().startTeleport(2929, 4813, 0, "modern", false);
			break;
		}
	}

}
