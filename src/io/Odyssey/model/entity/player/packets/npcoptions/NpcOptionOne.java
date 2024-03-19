package io.Odyssey.model.entity.player.packets.npcoptions;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.PetCollector;
import io.Odyssey.content.achievement_diary.impl.KandarinDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.LumbridgeDraynorDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.VarrockDiaryEntry;
import io.Odyssey.content.bosses.nightmare.NightmareActionHandler;
import io.Odyssey.content.bosses.nightmare.NightmareLostItems;
import io.Odyssey.content.dailyrewards.DailyRewardsDialogue;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.dialogue.impl.IronmanNpcDialogue;
import io.Odyssey.content.dialogue.impl.MacDialogue;
import io.Odyssey.content.dialogue.impl.MonkChaosAltarDialogue;
import io.Odyssey.content.dialogue.impl.PineAwayDialogue;
import io.Odyssey.content.minigames.Sailing;
import io.Odyssey.content.minigames.inferno.Inferno;
import io.Odyssey.content.minigames.tob.TobConstants;
import io.Odyssey.content.miniquests.magearenaii.dialogue.KolodionDialogue;
import io.Odyssey.content.referral.EnterReferralDialogue;
import io.Odyssey.content.skills.Fishing;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.content.skills.crafting.Tanning;
import io.Odyssey.content.skills.farming.FarmingTeleport;
import io.Odyssey.content.skills.hunter.impling.Impling;
import io.Odyssey.content.skills.mining.Mineral;
import io.Odyssey.content.skills.thieving.Thieving;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.content.worldevent.WorldEventContainer;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.definitions.NpcDef;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.npc.pets.Probita;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.entity.player.mode.group.GroupIronman;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanDialogue;
import io.Odyssey.model.entity.player.mode.group.contest.GroupIronmanContest;
import io.Odyssey.model.shops.RegionLockedNpcs;
import io.Odyssey.util.Location3D;
import io.Odyssey.util.Misc;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static io.Odyssey.content.tutorial.TutorialDialogue.TUTORIAL_NPC;
import static io.Odyssey.model.Npcs.*;

/*
 * @author Matt //dogshit
 * Handles all first options on non playable characters.
 */

public class NpcOptionOne {

	public static void handleOption(Player player, int npcType) {
		if (Server.getMultiplayerSessionListener().inAnySession(player)) {
			return;
		}
		player.clickNpcType = 0;
		player.clickedNpcIndex = player.npcClickIndex;
		player.npcClickIndex = 0;

		/*
		 * if(Fishing.fishingNPC(c, npcType)) { Fishing.fishingNPC(c, 1, npcType);
		 * return; }
		 */
		if (PetHandler.isPet(npcType)) {
			if (Objects.equals(PetHandler.getOptionForNpcId(npcType), "first")) {
				if (PetHandler.pickupPet(player, npcType, true))
					return;
			}
		}

		if (NightmareActionHandler.clickNpc(player, npcType, 1)) {
			return;
		}

		NPC npc = NPCHandler.npcs[player.clickedNpcIndex];

		NpcDef npcDefinition = NpcDef.forId(npcType);
		if(npcDefinition != null)
			if(npcDefinition.getName() != null && npcDefinition.getName().toLowerCase().contains("banker") || npcDefinition.getName().toLowerCase().contains("reis")  || npcDefinition.getName().toLowerCase().contains("ket-yil")|| npcDefinition.getName().toLowerCase().contains("ket-zuh") || npcDefinition.getName().toLowerCase().contains("jade"))
				player.getDH().sendDialogues(1013, npcType);

		RegionLockedNpcs location = RegionLockedNpcs.getEnumByNpcId(npcType);

		if (location != null) {
			if (!player.unlockedallteles) {
				if (!player.regionsunlocked.containsKey(location.region)) {
					player.sendMessage("You need to discover " + Region.regionnames.get(location.region) + " before " + npcDefinition.getName() + " will talk to you.");
					return;
				}
			}
		}


		switch (npcType) {
			case LISA://
				List<String> events = WorldEventContainer.getInstance().getWorldEventStatuses();
//				for (String event : events) {
//					getPA().sendFrame126("@or1@- " + event, 15259);
//				}
				player.start(new DialogueBuilder(player).npc(npcType,
						"The next tournament will start:",
						events.get(0)));
				break;
			case LIEVE_MCCRACKEN://

				player.start(new DialogueBuilder(player).npc(npcType,
						"The Kraken inside may only be attacked while ",
						"assigned on a slayer task."));
				break;
			case BREIVE://

				player.start(new DialogueBuilder(player).npc(npcType,
						"The Smoke devils inside may only be attacked while",
						"assigned on a slayer task."));
				break;
//			case 8131://
//
//				//player.getDH().sendDialogues(9414, npcType);
//				player.start(new DialogueBuilder(player).npc(npcType,
//						"You can start the Nightmare by disturbing it.",
//						"If it is currently fighting a group you will need","to wait until the encounter ends.").npc("If you die you can purchase your","lost items from me for "+ NightmareLostItems.price+" gp."));
//				break;
			case SQUIRE://SQUIRE TRAVEL PORT SARIM - PEST CONTROL
				new DialogueBuilder(player).option(
						new DialogueOption(player.absY > 3199 ? "Void Knight Outpost" :"Port Sarim", plr -> Sailing.startTravel(plr,plr.absY > 3199 ? 12:13)),
						new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
				).send();

				break;
			case 8131://torfin travel ungael/relleka
				new DialogueBuilder(player).option(
						new DialogueOption(player.absY < 3739 ? "Ungael" :"Relleka", plr -> Sailing.startTravel(plr,plr.absY < 3739 ? 17:18)),
						new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
				).send();

							break;
			case 4642: //shantay
				player.getDH().sendDialogues(4642, npcType);
				break;
			case 2583:
				player.getDH().sendOption2("Get me out of here!", "I need a small pouch");
				player.dialogueAction = 2584;
				break;
			case 687:
				player.getShops().openShop(113);
				break;
			case 15571:
				player.getShops().openShop(213);
				break;
			case 326://genie
				if (npc.spawnedBy == player.getIndex()){
					player.getGenie().pickupgenie(npc);
				} else {
					player.sendMessage("not your random event.");
				}
				break;
			case 15565:
			case 15573:
			case 15572:
				if (npc.spawnedBy == player.getIndex() && player.hasPetSpawned) {
					boolean hasstoragepet = PetHandler.hasstoragepetout(player);
					if(hasstoragepet){
						player.getDH().sendOption2(player.yakname != null ? "Whats your name?" : "Set name", "How old are you?");
						player.dialogueAction = 155_655;
						//player.getPakYak().openPakYak();
					}
//					if(hasstoragepet){

				} else {
					player.sendMessage("not your pet.");
				}
				break;
			case 5937:
				player.getDH().sendDialogues(5937, npcType);
				break;
			case 7672:
				player.getDH().sendDialogues(7672, 7672);
				break;

			case 8402:
				player.getDH().sendDialogues(8402, 8402);
				break;
			case 4407:
				player.getDH().sendDialogues(4407, 4407);
				break;
			case 1788:
				player.getDH().sendOption2("I need more powerful runes.", "Cancel");
				player.dialogueAction = 15570;
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
//			case 276:
//
//				player.getDH().sendDialogues(10000, npcType);
//
//				break;
		case 8184:
			player.moveTo(TobConstants.LOBBY_TELEPORT_POSITION);
			break;
		case Npcs.JAMES:
			player.start(new PineAwayDialogue(player, npc));
			break;
		case FarmingTeleport.NPC:
			player.start(new FarmingTeleport(player, npc));
			break;
		case DailyRewardsDialogue.DAILY_REWARDS_NPC:
			int totalReq = (300);
			if (player.totalLevel > totalReq) {
				player.start(new DailyRewardsDialogue(player));
			} else {
				player.sendMessage("@red@ You need a total level of 500 to start collecting your daily reward!");
			}
			break;

		case 3400:
			player.getEventCalendar().openCalendar();
			break;

		case 2111:
			player.getShops().openShop(176);
			break;
		case 7240:
			player.getShops().openShop(91);
			break;
		case 2850:
			GroupIronmanContest.openInterface(player);
			player.sendMessage("Find out more info about the contest at ::topic 281");
			break;
		case 8686:
			player.getShops().openShop(90);
			break;
		case 5513:
			player.getDH().sendDialogues(80, 5513);
			break;
		case 954:
			player.getDH().sendDialogues(832, 954);
			break;
			case 2149:
			case 2150:
			case 2151:
			case 2148:
			case 1600:
			case 2633:
				player.getPA().c.itemAssistant.openUpBank();
				break;
		case 5293:
			player.getShops().openShop(175);
			break;
		case 386:
			player.getDH().sendDialogues(782, 386);
			break;
		case 8208:
			PetCollector.petCollectorDialogue(player);
			break;
		case 6599:
			player.getDH().sendDialogues(6599, npcType);
			break;
		case 3510:
				if (Boundary.isIn(player, Boundary.LONE_SURVIVER_AREA) || Boundary.isIn(player, Boundary.LUMBRIDGE_OUTLAST_LOBBY)); {
				player.getShops().openShop(147);
			}
			break;
		case 1013:
			player.getShops().openShop(130);
			break;
		case 5984:
			player.getShops().openShop(176);
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
		case 5082:
			player.getDH().sendNpcChat("Enter the portal at your own risk!");
			break;
		case 13: // Referral npc
			player.start(new EnterReferralDialogue(player));
			break;
			case 1603:
				player.getDH().sendDialogues(150, npcType);
				break;
		case 555:
			player.getShops().openShop(122);
			player.sendMessage("@red@ You can sell me anything you want!");
			break;
		case 9011:
			player.getShops().openShop(77);
			player.sendMessage("@red@ Please type in ::vote to go to the site. And do ::voted to receive them!");
			player.sendMessage("@red@ Thank you for supporting " + Configuration.SERVER_NAME + "!");
			break;
		case 1011:
			player.getDH().sendNpcChat1(
					"@or2@Gamble 10 firecapes at a chance of an infernal cape.", 1011,
					"Item Gambler");
		    	break;
		case 9168:
			player.getShops().openShop(123);
			break;
		case 4921:
			player.getShops().openShop(124);
			break;

		
		case 1035:
			player.getDH().sendDialogues(300, npcType);
			break;
		case 7690:
			//sacrifice fcape?


			Inferno.tzhaarketkehdialogue(player);
			//Inferno.startInferno(player, Inferno.getDefaultWave());
			break;

			case 2184:
				player.getShops().openShop(29);
				break;
			case 2183:
				//player.getShops().openShop(241);
				break;
		case 1143:
			player.getShops().openShop(83);
			break;

		case 1909:
			player.getDH().sendDialogues(900, 1909);
			break;
		case 2989:
			player.getDH().sendDialogues(1427, 2989);
			break;
		case 3306:
			player.getDH().sendDialogues(1577, -1);
			break;
		case 7520:
			player.getDH().sendDialogues(850, 7520);
			break;
		/**
		 * Doomsayer
		 */
		case 6773:
			if (!player.pkDistrict) {
				player.sendMessage("You cannot do this right now.");
				return;
			}
			player.getDH().sendDialogues(800, 6773);
			break;
		// Zeah Throw Aways
		case 2200:
			player.getDH().sendDialogues(55873, 2200);
			break;
		case 3189:
			player.getDH().sendDialogues(11929, 3189);
			break;
		case 5998:
			if (player.amDonated <= 1) {
				player.getDH().sendDialogues(5998, 5998);
			} else {
				player.getDH().sendDialogues(5999, 5998);
			}
			break;
		case 4062:
			player.getDH().sendDialogues(55875, 4062);
			break;
		case 4321:
			player.getDH().sendDialogues(145, 4321);
			break;
		case 6882:
			player.getDH().sendDialogues(500, 6882);
			break;
		case 6877:
			player.getDH().sendDialogues(55877, 6877);
			break;
		case 4409:
			player.getDH().sendDialogues(55876, 4409);
			break;
		case 6982:
			player.getDH().sendDialogues(55868, 6982);
			break;
		case 6947:
		case 6948:
		case 6949:
			player.getDH().sendDialogues(55872, 6947);
			break;
		case 6998:
			player.getDH().sendDialogues(55871, 6998);
			break;
		case 7001:
			player.getDH().sendDialogues(55869, 7001);
			break;
		case 6999:
			player.getDH().sendDialogues(55870, 6999);
			break;
		case 6904:
			player.getDH().sendDialogues(55864, 6904);
			break;
		case 6906:
			player.getDH().sendDialogues(55865, 6904);
			break;
		case 6908:
			player.getDH().sendDialogues(55866, 6904);
			break;
		case 6910:
			player.getDH().sendDialogues(55867, 6904);
			break;
		// End Zeah Throw Aways
		case 1503:
			player.getDH().sendDialogues(88393, 1503);
			break;
		case 1504:
			player.getDH().sendDialogues(88394, 1504);
			break;

		case 6774:
			player.getDH().sendDialogues(800, 6773);
			break;
			case FILAMINA:
				player.getDonationRewards().openInterface();
				break;

		case 822:
			Consumer<Player> option1 = p -> {
				if (p.getItems().playerHasItem(11286) && p.getItems().playerHasItem(1540)
						&& p.getItems().playerHasItem(995, 1_250_000)) {
					p.getItems().deleteItem(11286, 1);
					p.getItems().deleteItem(1540, 1);
					p.getItems().deleteItem(995, 1_250_000);
					p.getItems().addItem(11283, 1);
					//player.votePoints -= 5;
					//player.getQuestTab().updateInformationTab();
					p.getDH().sendItemStatement("Oziach successfully bound your dragonfire shield.", 11283);
				} else {
					p.getDH().sendNpcChat1("Come back with a shield, visage and 5M Gold!", 6987, "Oziach");
				}
			};
			Consumer<Player> option2 = p -> {
				p.getPA().closeAllWindows();

			};

			player.start(new DialogueBuilder(player)
					.setNpcId(6987)
					.option(new DialogueOption("Create Dragonfire shield for 1.25M gold", option1),
							new DialogueOption("Start at last wave (1M gp)", option2))
			);

			break;
		case 306://Runescape guide
			player.getDH().sendDialogues(710, 306);
			break;
		case 2897:
			if (player.getMode().isIronmanType()) {
				player.sendMessage("@red@You are not permitted to make use of this.");	
				return;
			}
			Listing.openPost(player, false);
			break;
			case 3226://woodsman tutor
				player.getDH().sendDialogues(10700, npcType);
				break;
		case 7716:
			if (player.playerLevel[Skill.MINING.getId()] >= 92) {
				player.getPA().startTeleport(3021, 9716, 0, "modern", false);
				player.sendMessage("@blu@Welcome to the Amethyst Mine.");
				return;
			}
			player.sendMessage("@red@You need a mining level of 92 to teleport to the Amethyst Mine.");
			break;
		case 9014:
				player.getPA().startTeleport(3272, 6052, 0, "modern", false);
				player.sendMessage("@blu@Welcome to crystal slayer, use ::crystal for details.");
			break;
		case 7303:
		player.start(new DialogueBuilder(player).npc(npcType,
					"I will trade a full set of clue scrolls for a master one.",
					"Just hand them over."));
			player.sendMessage("I will trade a full set of clue scrolls with a master one.");

			break;

			case 15580:
				player.getDH().sendNpcChat2("Use Zammy Spear on me to get Hasta, cost 10m", "Use Hasta on me get Zammy Spear, cost 5m", 2914,
						"Otto Godblessed");
				break;
		case 2914:
			player.getDH().sendNpcChat2("Use Zammy Spear on me to get Hasta, cost 10m", "Use Hasta on me get Zammy Spear, cost 5m", 2914,
					"Otto Godblessed");
			break;

		case 1635:
		case 1636:
		case 1637:
		case 1638:
		case 1639:
		case 1640:
		case 1641:
		case 1642:
		case 1643:
		case 1654:
		case 7302:
			Impling.catchImpling(player, npc);
			break;

		case 17: // Rug merchant 
			player.getDH().sendDialogues(837, 17);
			break;
		case 5520:
			player.getDiaryManager().getDesertDiary().claimReward();
			break;
		case 5519:
			player.getDiaryManager().getArdougneDiary().claimReward();
			break;
		case 5790:
			player.getDiaryManager().getKaramjaDiary().claimReward();
			break;
		case 5525:
			player.getDiaryManager().getVarrockDiary().claimReward();
			break;
		case 5523:
			player.getDiaryManager().getLumbridgeDraynorDiary().claimReward();
			break;
		case 5524:
			player.getDiaryManager().getFaladorDiary().claimReward();
			break;
		case 5521:
			player.getDiaryManager().getMorytaniaDiary().claimReward();
			break;
		case 5514:
			player.getDiaryManager().getWildernessDiary().claimReward();
			break;
		case 5517:
			player.getDiaryManager().getKandarinDiary().claimReward();
			break;
		case 5526:
			player.getDiaryManager().getFremennikDiary().claimReward();
			break;
		case 5518:
			player.getDiaryManager().getWesternDiary().claimReward();
			break;

		case 3936:
			player.getDH().sendNpcChat1("Right click on me and i will take you on-board.", 3936, "Sailor");
			break;

		case 1896:
			if (player.getItems().playerHasItem(995, 30)) {
				player.getItems().deleteItem(995, 30);
				player.getItems().addItem(36, 1);
				player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.BUY_CANDLE);
			} else {
				player.sendMessage("You need 30 coins to purchase a candle.");
				return;
			}
			break;
			case 9400:
				player.getDH().sendDialogues(3454, 9400);
				break;
		case 6586:
			player.getDH().sendNpcChat1("No shirt, Sherlock", 6586, "Sherlock");
			player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.SHERLOCK);
			break;

		case 5036:
			if (player.getItems().playerHasItem(225) || player.getItems().playerHasItem(223)) {
				player.sendMessage("The Apothecary takes your ingredients and creates a strength potion.");
				player.getItems().deleteItem(225, 1);
				player.getItems().deleteItem(223, 1);
				player.getItems().addItem(115, 1);
				player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.APOTHECARY_STRENGTH);
			} else {
				player.sendMessage("You must have limpwurt root and red spiders' eggs to do this.");
				return;
			}
			break;

		case 5906:
			Probita.hasInvalidPet(player);
			break;

		case 3500:
			player.getDH().sendDialogues(64, npcType);
			break;

		case 5870:
			player.getDH().sendDialogues(105, npcType);
			break;
			case 7284://gertrude
				player.getDH().sendDialogues(7284, npcType);
				break;

		case 7283:
			player.getDH().sendDialogues(105, npcType);
			break;

		case Npcs.MAC:
			player.start(new MacDialogue(player, true));
			break;

		case 3307: // Combat instructor
			player.getDH().sendDialogues(1390, npcType);
			break;

		case 394:
			player.getDH().sendDialogues(669, npcType);
			break;
		case GroupIronman.GROUP_FORM_NPC:
			player.start(new GroupIronmanDialogue(player));
			break;
		case Npcs.ADAM:
			player.start(new IronmanNpcDialogue(player, npc));
			break;
		case 7995:
			player.start(new MonkChaosAltarDialogue(player, npc));
			break;
		// Noting Npc At Skill Area
		case 905:
			player.talkingNpc = 905;
			player.getDH().sendNpcChat("Hello there, I can note your resources.",
					"I charge @red@25%@bla@ of the yield, this @red@does not apply to donators@bla@.",
					"Use any resource item obtained in this area on me.");
			player.nextChat = -1;
			break;
			case 7205:
				player.getDH().sendDialogues(7205, npcType);
				break;
		case 2033:
			player.getDH().sendDialogues(637, npcType);
			break;

		case 6866:
			player.getShops().openShop(82);
			player.sendMessage("You currently have @red@" + player.getShayPoints() + " @bla@Assault Points!");
			break;

		case 6601:
			NPC golem = npc;
			if (golem != null) {
				player.getMining().mine(golem, Mineral.RUNE,
						new Location3D(golem.getX(), golem.getY(), golem.heightLevel));
			}
			break;
		case 1850:
			player.getShops().openShop(112);
			break;
		case 2581:
			player.getDH().sendDialogues(629, npcType);
			break;
//		case 4305:
//			player.getThieving().steal(Thieving.Pickpocket.DRUNKEN_DWARF, npc);
//			break;
			case 3208:
				player.getShops().openShop(168);
				break;
		case 3894:
			player.getShops().openShop(26);
			break;
		case 3220:
			player.getShops().openShop(25);
			break;
		case 637:
			player.getShops().openShop(6);
			break;
			case 6875:
				player.specRestore = 120;
				player.specAmount = 10.0;
				player.setRunEnergy(10000, true);
				player.getItems().addSpecialBar(player.playerEquipment[Player.playerWeapon]);
				player.playerLevel[5] = player.getPA().getLevelForXP(player.playerXP[5]);
				player.getHealth().removeAllStatuses();
				player.getHealth().reset();
				player.getPA().refreshSkill(5);
				player.getDH().sendItemStatement("Restored your HP, Prayer, Run Energy, and Spec", 4049);
				player.nextChat =  -1;
				break;
			case 732:
			player.getShops().openShop(16);
			break;
			case 627:
				player.getShops().openShop(113);
				break;
		case 2949:
			player.getPestControlRewards().showInterface();
			break;
		case 2461:
			player.getWarriorsGuild().handleDoor();
			break;
		case 7663:
			player.getDH().sendDialogues(3299, npcType);
			break;
		case 402:// slayer
			if(player.combatLevel<20){
				player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of 20.",402,"Mazchna");
				return;
			}
			player.getDH().sendDialogues(3300, npcType);
			break;
		case 401://turael
//			if(player.combatLevel<20){
//				player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of 20.",402,"Mazchna");
//				return;
//			}
			player.getDH().sendDialogues(3300, npcType);
			break;
	
		case 405:
			if(player.combatLevel<100){
				player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of at least 100.",402,"Duradel");
				return;
			}
			if (player.playerLevel[18] < 50) {
				player.getDH().sendNpcChat1("You must have a slayer level of at least 50 weakling.", 490, "Duradel");
				return;
			}
			player.getDH().sendDialogues(3300, npcType);
			break;
		case 8623:
		case 6797: // Nieve
				player.getDH().sendDialogues(3300, npcType);
			break;

			case 403://vannaka
				if(player.combatLevel<40){
					player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of at least 40.",403,"Vannaka");
					return;
				}

				player.getDH().sendDialogues(3300, npcType);
				break;
			case 404://Chaeldar
				if(player.combatLevel<70){
					player.getDH().sendNpcChat2("Do not waste my time peasent.","You need a Combat level of at least 70.",404,"Chaeldar");
					return;
				}

				player.getDH().sendDialogues(3300, npcType);
				break;

		case 308:
			player.getDH().sendDialogues(550, npcType);
			break;

		case 1308:
			player.getDH().sendDialogues(538, npcType);
			break;
		case Npcs.BOSS_POINT_SHOP:
			player.getShops().openShop(121);
			player.sendMessage("You currently have " + Misc.insertCommas(player.bossPoints) + " Boss points.");
			break;
		case Npcs.PERDU:
			player.getDH().sendDialogues(619, npcType);
			break;
		case 4306:
			player.getShops().openSkillCape();
			break;
		case 1933:
			player.getShops().openMillCape();
			break;
		case 3341:
			player.getDH().sendDialogues(2603, npcType);
			break;
		case 5919:
			player.getDH().sendDialogues(14400, npcType);
			break;
		case 603:
			player.getDH().sendDialogues(11955, npcType);
			break;
		case 8761:
			player.getDH().sendDialogues(10955, npcType);
			break;
			case SLIEVE:
				player.getDH().sendDialogues(7653, npcType);
				break;
			case VELRAK_THE_EXPLORER:
				player.getDH().sendOption2("Know any interesting places to explore?", "Cancel");
				player.dialogueAction = 4925;
					break;
			case WYSON_THE_GARDENER:
				player.getDH().sendDialogues(3195, npcType);
				break;
		case 8605:
			if (player.playerLevel[18] >= 95) {
			player.getDH().sendDialogues(11155, npcType);
			break;
			}
			player.sendMessage("You need a slayer level of 95 to get a task from me.");
			break;

		case 3789:
			player.getShops().openShop(75);
			break;
			case 4928:
				player.clickNpcType = 1;
				Fishing.attemptdata(player, 17);
				break;
			case 7676://infernal eel in mor-ui-rek
				player.clickNpcType = 1;
				Fishing.attemptdata(player, 18);
				break;
		// FISHING
		case 3913: // NET + BAIT
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 1);
			break;
		case 3317: // net, karwas bait
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 14);
			break;
		case 4712: // karaws
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 15);
			break;
		case 1524:
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 11);
			break;
		case 3417: // TROUT
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 4);
			break;
		case 3657: // lobster
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 8);
			break;
		case 635:
			Fishing.attemptdata(player, 13); // DARK CRAB FISHING
			break;
		case 6825: // Anglerfish
			player.clickNpcType = 1;
			Fishing.attemptdata(player, 16);
			break;
		case 1520: // LURE
		case 310:
		case 314:
		case 317:
		case 318:
		case 328:
		case 331:
			Fishing.attemptdata(player, 9);
			break;

		case 944:
			player.getDH().sendOption5("Hill Giants", "Hellhounds", "Lesser Demons", "Chaos Dwarf", "-- Next Page --");
			player.teleAction = 7;
			break;

		case 559:
			player.getShops().openShop(16);
			break;
		case 5809:
			Tanning.sendTanningInterface(player);
			break;



		case 1599:
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
		case 567: // Banker
		case 766: // Banker
		case 1036: // Banker
		case 1360: // Banker
		case 2163: // Banker
		case 2164: // Banker
		case 2354: // Banker
		case 2355: // Banker
		case 2568: // Banker
		case 2569: // Banker
		case 2570: // Banker
			player.getPA().c.itemAssistant.openUpBank();
			break;
		case 1986:
			player.getDH().sendDialogues(2244, player.npcType);
			break;

		case 2108:
			player.getShops().openShop(77);
			break;
		case 1785:
			player.getShops().openShop(8);
			break;

		case 1860:
			player.getShops().openShop(47);
			break;

		case 519:
			player.getShops().openShop(48);
			break;

		case 548:
			player.getDH().sendDialogues(69, player.npcType);
			break;

		case 2258:
			player.getDH().sendOption2("Teleport me to Runecrafting Abyss.", "I want to stay here, thanks.");
			player.dialogueAction = 2258;
			break;

		case 532:
			player.getShops().openShop(47);
			break;
			case 2813:
			player.getShops().openShop(2);
			break;

		case 7913:
			if (player.getMode().isIronmanType()) {
				player.getShops().openShop(41);
			} else {
				player.sendMessage("You must be an Iron Man to access this shop.");
			}
			break;
		case 7769:
			player.getShops().openShop(2);
			break;

			default:
				player.getDH().sendDialogues(1337, npcType);

		

		/*
		 * case 198: c.getShops().openSkillCape(); break;
		 */

		/**
		 * Make over mage.
		 */

		}
	}

}
