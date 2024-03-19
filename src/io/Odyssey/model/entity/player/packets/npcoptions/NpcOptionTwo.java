package io.Odyssey.model.entity.player.packets.npcoptions;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.PlayerEmotes;
import io.Odyssey.content.achievement_diary.impl.*;
import io.Odyssey.content.bosses.nightmare.NightmareActionHandler;
import io.Odyssey.content.dailyrewards.DailyRewardsDialogue;
import io.Odyssey.content.dialogue.impl.IronmanNpcDialogue;
import io.Odyssey.content.leaderboards.LeaderboardInterface;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.minigames.TombsOfAmascut.instance.TombsOfAmascutInstance;
import io.Odyssey.content.minigames.inferno.Inferno;
import io.Odyssey.content.skills.Fishing;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.skills.thieving.Thieving;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.definitions.NpcDef;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.npc.pets.Probita;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerAssistant;
import io.Odyssey.model.shops.RegionLockedNpcs;
import io.Odyssey.model.shops.ShopAssistant;
import io.Odyssey.util.Color;
import io.Odyssey.util.Misc;

import java.util.concurrent.TimeUnit;

import static io.Odyssey.model.Npcs.*;

/*
 * @author Matt
 * Handles all 2nd options on non playable characters.
 */

public class NpcOptionTwo {

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		player.clickNpcType = 0;
		player.clickedNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;


		if (PetHandler.isPet(npcType)) {
			if (PetHandler.getOptionForNpcId(npcType) == "second") {
				if (PetHandler.pickupPet(player, npcType, true))
					return;
			}
		}
		NpcDef npcDefinition = NpcDef.forId(npcType);
		if(npcDefinition != null)
			if(npcDefinition.getName() != null && npcDefinition.getName().toLowerCase().contains("banker")  || npcDefinition.getName().toLowerCase().contains("reis") || npcDefinition.getName().toLowerCase().contains("ket-yil")  || npcDefinition.getName().toLowerCase().contains("ket-zuh")|| npcDefinition.getName().toLowerCase().contains("jade"))
				player.getPA().c.itemAssistant.openUpBank();

		if (NightmareActionHandler.clickNpc(player, npcType, 2)) {
			return;
		}

		RegionLockedNpcs location = RegionLockedNpcs.getEnumByNpcId(npcType);

		if (location != null) {

			if (!player.unlockedallteles) {
				if (!player.regionsunlocked.containsKey(location.region)) {
					player.sendMessage("You need to discover " + Region.regionnames.get(location.region) + " before using this store.");
					return;
				}
			}
}


	NPC npc = NPCHandler.npcs[player.clickedNpcIndex];

		switch (npcType) {
			case 11693:

				((TombsOfAmascutInstance) player.getInstance()).remove(player);
				player.moveTo(TombsOfAmascutConstants.FINISHED_TOMBS_OF_AMASCUT_POSITION);
				break;
			case 15580:
				LeaderboardInterface.openInterface(player);
				break;


			case 390://boss point store
				player.getShops().openShop(121);
				break;
			case SKULGRIMEN:
				player.getShops().openShop(148);
				break;
			case FILAMINA://aka donator store
				player.getShops().openShop(9);
				break;
			case GERRANT:
				player.getShops().openShop(22);
				break;
			case GEM_TRADER:
				player.getShops().openShop(145);
				break;
			case DAVON:
				player.getShops().openShop(146);
				break;
			case DROGO_DWARF:
				player.getShops().openShop(147);
				break;
			case URBI:
				player.getShops().openShop(128);
				break;
			case 3208:
				player.getShops().openShop(168);
				break;
			case WAYNE:
				player.getShops().openShop(127);
				break;
			case CASSIE:
				player.getShops().openShop(126);
				break;
			case LOUIE_LEGS:
				player.getShops().openShop(136);
				break;
			case FLYNN:
				player.getShops().openShop(137);
				break;
			case ZAFF:
				player.getShops().openShop(138);
				break;
			case BETTY:
				player.getShops().openShop(139);
				break;
			case FRENITA:
				player.getShops().openShop(140);
				break;
			case DOMMIK:
				player.getShops().openShop(141);
				break;
			case RANAEL:
				player.getShops().openShop(133);
				break;
			case PEKSA:
				player.getShops().openShop(135);
				break;
			case DAGA:
				player.getShops().openShop(131);
				break;			case JUKAT:
				player.getShops().openShop(132);
				break;
			case SHANTAY:
				player.getShops().openShop(173);
				break;
			case GAIUS:
				player.getShops().openShop(26);
				break;
			case JATIX:
				player.getShops().openShop(27);
				break;
			case 5937:
				player.getPA().movePlayer(2551,3755,0);
				player.getPA().closeAllWindows();
				break;
			case 15571:
				player.getShops().openShop(213);
				break;
				case 687:
				player.getShops().openShop(113);
				break;
			case 15581:
				player.getShops().openShop(210);
				break;
			case 2184:
				player.getShops().openShop(29);
				break;
			case 2183:
				player.getShops().openShop(241);
				break;
			case 7679:
				player.getShops().openShop(29);
				break;
			case 1400: //nulodion
				player.getShops().openShop(276);
				break;

			case 15570:
				player.getShops().openShop(291);
				break;
			case 1601:

				if(	player.maRound < 2) {
					player.sendMessage("@red@You must defeat Kolodion first.");
					return;
				}
				player.getShops().openShop(189);
				break;
			case 1602:

				if(	player.maRound < 2) {
					player.sendMessage("@red@You must defeat Kolodion first.");
					return;
				}
				player.getShops().openShop(190);
				break;
			case 2813:
				player.getShops().openShop(2);
				break;

//			case 276://task master
//				player.getTaskMasterManager().open();
//				break;
//				//		player.getDH().sendDialogues(10000, npcType);

		case DailyRewardsDialogue.DAILY_REWARDS_NPC:

				player.getDailyRewards().openInterface();

			break;
		case 326:
		case 327:
			   player.gfx100(1028);
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
		case 7240:
			player.getShops().openShop(91);
			break;
		case 8686:
			player.getShops().openShop(90);
			break;
		case 1011: //infernal gambler
		    if (player.getItems().playerHasItem(6570, 10)) {
		    	int InfernalChance = Misc.random(1000);
                if (InfernalChance == 975) {
                	player.getItems().deleteItem(6570, 10);
                    player.getItems().addItem(21295, 1);
                    player.sendMessage("@red@Congratulations! you have won a infernal cape."); 
                } else {
                	player.getItems().deleteItem(6570, 10);
        			player.sendMessage("@red@Unlucky! better luck next time.");	
        			return;
                }
        			player.sendMessage("@red@You dont have 10 firecapes to gamble.");	
		    }
		    	break;
		case 7690:
			Inferno.gamble(player);
			break;
		case Npcs.PERDU:
			player.getPerduLostPropertyShop().open(player);
			break;
		case 1909:
			player.getDH().sendDialogues(901, 1909);
			break;

		case 2989:
			player.getPrestige().openPrestige();
			break;

		case 3307:
			player.getPA().showInterface(37700);
			player.sendMessage("Set different colors for specific items for easier looting!");
			break;

		case 4321:
			int totalBlood = player.getItems().getItemAmount(13307);
			if (totalBlood >= 1) {
				player.getPA().exchangeItems(PlayerAssistant.PointExchange.BLOOD_POINTS, 13307, totalBlood);
			}
			break;

		case 822:
			player.getShops().openShop(81);
			break;

		case 7520:
			player.getDH().sendDialogues(855, 7520);
			break;
			case 2033:
				if (player.getZulrahLostItems().size() > 0) {
					player.getDH().sendDialogues(2033, 2033);
					player.nextChat = -1;
				} else {
					player.sendMessage("You don't have any lost items to collect.");
				}
				break;

			case 9459:
				if (player.getNightmareLostItems().size() > 0) {
					player.getDH().sendDialogues(9414, 9414);
					player.nextChat = -1;
				} else {
					player.sendMessage("You don't have any lost items to collect.");
				}
				break;
		case 6774:
			player.getShops().openShop(117);
			break;
		case 6773:
			if (!player.pkDistrict) {
				player.sendMessage("You cannot do this right now.");
				return;
			}
			break;

		case 4407:
			if (player.getLevelForXP(player.playerXP[4]) < 50) {
				player.sendMessage("@red@You need a ranged level of 50 to access this shop.");
				return;
			}
			player.getShops().openShop(19);
			break;



		case 17: // Rug merchant 
			player.getDH().sendDialogues(838, 17);
			break;
		case 2897:
			if (player.getMode().isIronmanType()) {
				player.sendMessage("@red@You are not permitted to make use of this.");			}
			Listing.collectMoney(player);
			break;
		case 3105:
			long milliseconds = (long) player.playTime * 600;
			long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
			long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
			String time = days + " days and " + hours + " hours.";
			player.getDH().sendNpcChat1("You've been playing " + Configuration.SERVER_NAME + " for " + time, 3105, "Hans");
			player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.HANS);
			break;
			case 2149:
			case 2150:
			case 2151:
			case 2148:

				if (player.getMode().isIronmanType()) {
					player.sendMessage("@red@You are not permitted to make use of this.");
					return;
				}
				Listing.openPost(player, false);
				break;
		case 3680://delay fade
			AgilityHandler.delayFade(player, "NONE", 2674, 3274, 0, "The sailor brings you onto the ship.",
					"and you end up in ardougne.", 3);
			player.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.SAIL_TO_ARDOUGNE);
			break;

		case 5034:
			player.getPA().startTeleport(2929, 4813, 0, "modern", false);
			player.getDiaryManager().getLumbridgeDraynorDiary()
					.progress(LumbridgeDraynorDiaryEntry.TELEPORT_ESSENCE_LUM);
			break;

		case 5906:
			Probita.cancellationOfPreviousPet(player);
			break;

		case 2180:
			player.getDH().sendDialogues(70, 2180);
			break;

		case 401:
		case 402:
			case 403:
			case 404:
		case 405:
		case 7663:
			player.getDH().sendDialogues(3304, npcType);
			break;
		case 6797: // Nieve
			if (player.fullVoidMelee()) {
				player.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.FULL_VOID);
			}
			if (player.getSlayer().getTask().isPresent()) {
				player.getDH().sendDialogues(3305, 6797);
			} else {
				player.getDH().sendDialogues(180, 6797);
			}
			break;
		case 8761: 
			if (player.getSlayer().getTask().isPresent()) {
				player.getDH().sendDialogues(3305, 8761);
			} else {
				player.getDH().sendDialogues(10955, npcType);
			}
			break;
		case 5870: 
			if (player.getSlayer().getTask().isPresent()) {
				player.getDH().sendDialogues(3305, 8761);
			} else {
				if (player.getLevel(Skill.SLAYER) < 91) {
					player.getDH().sendStatement("You need a Slayer level of 91 to kill these.");
					return;
				}
				if (player.getSlayer().getTask().isPresent()) {
					player.getDH().sendStatement("Please finish your current task first.");		
					return;
				}
				if (!player.getItems().playerHasItem(995, 3_000_000)) {
					player.getDH().sendStatement("Come back when you've got the 3m ready.");
					return;
				}
					player.getItems().deleteItem2(995, 3_000_000);
					player.getSlayer().createNewTask(5870, true);
					player.getDH().sendNpcChat("You have been assigned "+ player.getSlayer().getTaskAmount() + " " + player.getSlayer().getTask().get().getPrimaryName());
					player.nextChat = -1;
			}
			break;

		case 5919: // Grace
			player.getShops().openShop(18);
			break;
		case Npcs.ADAM:
			IronmanNpcDialogue.giveIronmanArmour(player, npc);
			break;
		case 6747:
			player.getShops().openShop(77);
			break;
		case 2581:
			player.getShops().openShop(143);
			break;
			case LOWE:
//				Integer data = ShopAssistant.REGION_SHOPS.SHOP_REGION_NPCS.get(npcType);
//				if (data != null) {
//					System.out.println("yes: "+data);

				player.getShops().openShop(123);
				break;
			case HURA:
				player.getShops().openShop(124);
				break;
			case OOBAPOHK:
				player.getShops().openShop(125);
				break;
			case CELYN:
				player.getShops().openShop(129);
				break;
			case BRIAN:
				player.getShops().openShop(130);
				break;
		case 3936:
			player.getDH().sendDialogues(459, -1);
			break;
//		case 6970:
//			player.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.PICKPOCKET_GNOME);
//			player.getThieving().steal(Thieving.Pickpocket.MAN, npc);
//			break;
			case 3292:
				player.getThieving().steal(Thieving.Pickpocket.WARRIOR, npc);
				break;
			case 5418:
				player.getThieving().steal(Thieving.Pickpocket.GUARD, npc);
				break;
				case 2161:
				player.getThieving().steal(Thieving.Pickpocket.TZHAAR_HUR, npc);
				break;
		case 3295: //for diary

			if (Boundary.isIn(player, Boundary.ARDOUGNE)) {
				player.getThieving().steal(Thieving.Pickpocket.HERO, npc);
				player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.PICKPOCKET_ARD);
				player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.PICKPOCKET_HERO);
			}
			break;
			case 3114:
				player.getThieving().steal(Thieving.Pickpocket.FARMER, npc);
				break;
			case 5730:
				if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
					player.getThieving().steal(Thieving.Pickpocket.FARMER, npc);
					break;
				}
				if (Boundary.isIn(player, Boundary.DRAYNOR_BOUNDARY)) {
					player.getThieving().steal(Thieving.Pickpocket.FARMER, npc);
					player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.PICKPOCKET_FARMER_DRAY);
					break;
				}
				player.getThieving().steal(Thieving.Pickpocket.FARMER, npc);
				player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.PICKPOCKET_FARMER_DRAY);
				break;
		case 6987:
			case 6990:
			player.getThieving().steal(Thieving.Pickpocket.MAN, npc);
			break;
//		case 3550:
//			player.getThieving().steal(Thieving.Pickpocket.MENAPHITE_THUG, npc);
//			break;
		case 6094:
			player.getThieving().steal(Thieving.Pickpocket.GNOME, npc);
			break;
		case 3106:
			player.getThieving().steal(Thieving.Pickpocket.HERO, npc);
			break;
		case 637:
			player.getShops().openShop(6);
			break;
		case 627:
			player.getShops().openShop(113);
			break;
		case 534:
			if (Boundary.isIn(player, Boundary.VARROCK_BOUNDARY)) {
				player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.DRESS_FOR_SUCESS);
			}
			player.getShops().openShop(114);
			break;
		case 732:
			player.getShops().openShop(16);
			break;

		case 308:
			player.getShops().openShop(80);
			break;
		case 6599:

			break;
		case 3341:
			PlayerAssistant.refreshSpecialAndHealth(player);
			break;


		case 3913: // BAIT + NET
			Fishing.attemptdata(player, 2);
			break;
		case 310:
		case 314:
		case 317:
		case 318:
		case 328:
		case 329:
		case 331:
		case 3417:
		case 6825:// BAIT + LURE
			Fishing.attemptdata(player, 6);
			break;
		case 3657:
		case 321:
		case 324:// SWORDIES+TUNA-CAGE+HARPOON
			Fishing.attemptdata(player, 7);
			break;
		case 1520:
		case 322:
		case 334: // NET+HARPOON
			Fishing.attemptdata(player, 10);
			break;
		case 532:
			player.getShops().openShop(47);
			break;
		case 1599:
			player.getShops().openShop(10);
			player.sendMessage("You currently have <col=a30027>" + Misc.insertCommas(player.getSlayer().getPoints()) + " </col>slayer points.");
			break;
		case 953: // Banker
		case 2574: // Banker
		case 166: // Gnome Banker
		case 1702: // Ghost Banker
		case 494: // Banker
		case 495: // Banker
		case 496: // Banker
		case 497: // Banker
		case 498: // Banker
		case 499: // Banker
		case 394:
		case 567: // Banker
		case 766:
		case 1036: // Banker
		case 1360: // Banker
		case 2163: // Banker
		case 2164: // Banker
		case 2354: // Banker
		case 2355: // Banker
		case 2568: // Banker
		case 2569: // Banker
		case 2570: // Banker
		case 2200:
			case 2118:
			case 1600:
			player.getPA().c.itemAssistant.openUpBank();
			break;

		case 1785:
			player.getShops().openShop(8);
			break;

		case 3796:
			player.getShops().openShop(6);
			break;

		case 1860:
			player.getShops().openShop(6);
			break;

		case 519:
			player.getShops().openShop(7);
			break;

		case 548:
			player.getDH().sendDialogues(69, player.npcType);
			break;

		case 2258:

			break;



		case 506:
			if (player.getMode().isIronmanType()) {
				player.getShops().openShop(41);
			} else {
				player.sendMessage("You must be an Iron Man to access this shop.");
			}
			break;
		case 507:
			player.getShops().openShop(2);
			break;


		}
	}

}
