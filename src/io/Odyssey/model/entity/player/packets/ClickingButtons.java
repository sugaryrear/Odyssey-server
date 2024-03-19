package io.Odyssey.model.entity.player.packets;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.PlayerEmotes;
import io.Odyssey.content.QuestTab;
import io.Odyssey.content.UimStorageChest;
import io.Odyssey.content.boosts.XPBoostInformation;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.cheatprevention.CheatEngineBlock;
import io.Odyssey.content.combat.magic.CombatSpellData;
import io.Odyssey.content.combat.magic.MagicRequirements;
import io.Odyssey.content.combat.magic.NonCombatSpellData;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.combat.melee.QuickPrayers;
import io.Odyssey.content.combat.weapon.WeaponData;
import io.Odyssey.content.dialogue.*;
import io.Odyssey.content.displayname.ChangeDisplayName;
import io.Odyssey.content.help.HelpDatabase;
import io.Odyssey.content.item.lootable.LootableInterface;
import io.Odyssey.content.itemskeptondeath.ItemsKeptOnDeathInterface;
import io.Odyssey.content.leaderboards.LeaderboardInterface;
import io.Odyssey.content.minigames.barrows.CorrectFirstClickButton;
import io.Odyssey.content.newteleport.SpecificTeleport;
import io.Odyssey.content.polls.PollTab;
import io.Odyssey.content.preset.PresetManager;
import io.Odyssey.content.pricechecker.PriceChecker;
import io.Odyssey.content.skills.Cooking;
import io.Odyssey.content.skills.crafting.*;
import io.Odyssey.content.skills.crafting.CraftingData.tanningData;
import io.Odyssey.content.skills.slayer.SlayerRewardsInterface;
import io.Odyssey.content.skills.smithing.Smelting;
import io.Odyssey.content.teleportation.newest.CityTeleports;
import io.Odyssey.content.tournaments.TourneyManager;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.content.tutorial.TutorialDialogue;
import io.Odyssey.content.vote_panel.VotePanelInterface;
import io.Odyssey.model.Items;
import io.Odyssey.model.Spell;
import io.Odyssey.model.cycleevent.impl.HomeTeleportEvent;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanBank;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanGroup;
import io.Odyssey.model.entity.player.packets.dialogueoptions.*;
import io.Odyssey.model.items.ContainerUpdate;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.bank.BankItem;
import io.Odyssey.model.items.bank.BankTab;
import io.Odyssey.model.multiplayersession.MultiplayerSession;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.model.multiplayersession.duel.DuelSessionRules;
import io.Odyssey.model.multiplayersession.flowerpoker.FlowerPokerSession;
import io.Odyssey.model.multiplayersession.trade.TradeSession;
import io.Odyssey.model.shops.ShopAssistant;
import io.Odyssey.util.Misc;
import io.Odyssey.util.logging.player.ClickButtonLog;
import org.apache.commons.lang3.Range;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.Odyssey.model.shops.ShopAssistant.*;

/**
 * Clicking most buttons
 *
 */
public class ClickingButtons implements PacketType {
	public static final Range<Integer> TELEPORT_BUTTONS = Range.between(88101-65536, (88101-65536)+30);
	public static final Range<Integer> FAVORITE_BUTTONS = Range.between( (88101+30+30+30)-65536,  (88101+30+30+30+30)-65536);
	@Override
	public void processPacket(final Player c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);
		int realButtonId = c.getInStream().readUnsignedWord();
		if (c.debugMessage) {
			c.sendMessage("actionbutton: " + actionButtonId + ", DialogueID: " + c.dialogueAction + ", real id: " + realButtonId);
		}
		if (c.isDead || c.getHealth().getCurrentHealth() <= 0) {
			return;
		}
		if (c.getQuesting().clickButton(realButtonId)) {
			return;
		}
		//barrows button click
		CorrectFirstClickButton.handleButton(c, actionButtonId);
		if (c.isFping()) {
			/**
			 * Cannot do action while fping
			 */
			return;
		}

		//Server.getLogging().write(new ClickButtonLog(c, actionButtonId, false));

		if (c.isIdle) {
			if (c.debugMessage)
				c.sendMessage("You are no longer in idle mode.");
			c.isIdle = false;
		}
		if (c.getModeSelection().clickButton(actionButtonId))
			return;

		if (TELEPORT_BUTTONS.contains(realButtonId)) {//88101-65632
			if (TutorialDialogue.inTutorial(c)) {

				return;
			}

			if (System.currentTimeMillis() - c.teleinterfaceDelay < c.teleinterfaceLength) {
				long timeleft = System.currentTimeMillis() - c.teleinterfaceDelay;
				long realtimeleft = c.teleinterfaceLength - timeleft;
				String s = Misc.howMuchTimeLeft_teleint(realtimeleft);
				s =  s+ " until you can teleport again.";
				c.sendMessage(s);

				return;
			}

			List<SpecificTeleport> thespecificteleport = c.getnewteleInterface().thespecificteleport.stream().filter(t -> t.button == realButtonId+65536).collect(Collectors.toList());
			c.getnewteleInterface().confirmdialog(thespecificteleport.get(0));
		}

		if (FAVORITE_BUTTONS.contains(realButtonId)) {

			List<SpecificTeleport> thespecificteleport = c.getnewteleInterface().thespecificteleport.stream().filter(t -> t.favoritebutton == realButtonId+65536).collect(Collectors.toList());
//for(SpecificTeleport favs :c.getnewfavs()){
//	System.out.println("favs: "+favs.text);
//			}
//
//System.out.println("d: "+thespecificteleport.get(0).text);
			if(c.getnewfavs().stream().anyMatch(b -> b.text.equalsIgnoreCase(thespecificteleport.get(0).text))) {
				List<SpecificTeleport> toremove = c.getnewfavs().stream().filter(b -> b.text.equalsIgnoreCase(thespecificteleport.get(0).text)).collect(Collectors.toList());
				SpecificTeleport specifictoremove = toremove.get(0);
				c.getnewfavs().remove(specifictoremove);
				c.getPA().sendChangeSprite(realButtonId+65536, (byte) 0);

				//System.out.println("d: "+thespecificteleport.get(0).text);
				if (c.getnewteleInterface().category == 0)
					c.getnewteleInterface().drawInterface(88005);
				return;
			}

			SpecificTeleport telewereadding = thespecificteleport.get(0);
			c.getnewfavs().add(new SpecificTeleport(telewereadding.button,telewereadding.tile,telewereadding.text,telewereadding.description,true,telewereadding.favoritebutton,telewereadding.regionunlock));
			c.getPA().sendChangeSprite(realButtonId+65536, (byte) 1);



		}
//		if (c.getMovementState().isLocked())
//			return;
		if (c.getBattlePass().clickButton(actionButtonId)) {
			return;
		}
		if(c.getPortalTeleports().handleButton(realButtonId)) {
			return;
		}
		if(c.getPortalTeleports().handleTabClick(realButtonId)) {
			return;
		}
		if (c.getLootingBag().handleButton(actionButtonId)) {
			return;
		}
		if (c.getDepositBox().handleButton(realButtonId)) {
			return;
		}
		if (c.getPrestige().prestigeClicking(actionButtonId)) {
			return;
		}
		if (TourneyManager.getSingleton().handleActionButtons(c, actionButtonId)) {
			return;
		}

		if (c.getExpLock().ExpLockClicking(actionButtonId)) {
			return;
		}
		if (PollTab.handleActionButton(c, actionButtonId)) {
			return;
		}
		if (c.getRunePouch().handleButton(actionButtonId)) {
			return;
		}
		if (c.getInterfaceEvent().isActive()) {
			c.getInterfaceEvent().clickButton(actionButtonId);
			return;
		}
		if (c.getSlayer().onActionButton(actionButtonId)) {
			return;
		}
		if (PresetManager.getSingleton().handleActionButtons(c, actionButtonId)) {
			return;
		}
		if (VotePanelInterface.handleActionButton(c, actionButtonId)) {
			return;
		}
		if (c.getCollectionLog().handleActionButtons(c, actionButtonId)) {
			return;
		}
		if (c.getQuestTab().handleHelpTabActionButton(actionButtonId)) {
			return;
		}
		if (SlayerRewardsInterface.clickButton(c, actionButtonId)) {
			return;
		}
//		if (LootableInterface.button(c, actionButtonId)) {
//			return;
//		}
		if (c.attacking.clickWeaponTabButton(actionButtonId)) {
			return;
		}
		if (c.getModeSelection().clickButton(actionButtonId)) {
			return;
		}
		if (c.getNotificationsTab().clickButton(actionButtonId)) {
			return;
		}

		if (LeaderboardInterface.handleButtons(c, realButtonId)) {
			return;
		}

		Listing.postButtons(c, actionButtonId);
		/** mailbox buttons  **/
		if (actionButtonId >= 177_157 && actionButtonId <= 177_157+20) {
		//	System.out.println("here");
			c.handlemailboxclick(actionButtonId);
			return;
		}
		/** Drop Manager Buttons **/
		if (actionButtonId >= 128240 && actionButtonId <= 129113) {
			Server.getDropManager().select(c, actionButtonId);
			return;
		}

		if (c.getTabHandler().handleTabSwitch(actionButtonId)) {
			return;
		}

		if (actionButtonId == 166027) {
			c.sendMessage("[@red@Warning@bl2@] You may experience lag while using anti-aliasing");
			return;
		}
		if (actionButtonId >= 232182 && actionButtonId <= 233022) {
			HelpDatabase.getDatabase().view(c, actionButtonId);
			HelpDatabase.getDatabase().delete(c, actionButtonId);
			return;
		}
		if (actionButtonId == 15040) {
			c.getDH().sendDialogues(68, -1);
			return;
		}

		if (actionButtonId == 15041) {
			Server.getDropManager().open2(c);
			return;
		}
		if (actionButtonId == 166023) {
			c.getPA().removeAllWindows();
			return;
		}
		c.getPestControlRewards().click(actionButtonId);
		if (c.getTitles().click(actionButtonId)) {
			return;
		}
		if (c.battlestaffDialogue) {
			BattlestaveMaking.craftBattlestave(c, actionButtonId);
		}
		if (c.craftDialogue) {
			LeatherMaking.craftLeather(c, actionButtonId);
		}
		if (c.braceletDialogue) {
			BraceletMaking.craftBracelet(c, actionButtonId);
		}
		for (tanningData t : tanningData.values()) {
			if (actionButtonId == t.getButtonId(actionButtonId)) {
				Tanning.tanHide(c, actionButtonId);
			}
		}

		DuelSession duelSession = null;
		c.getFletching().select(actionButtonId);
		GlassBlowing.glassBlowing(c, actionButtonId);
		PlayerEmotes.performEmote(c, actionButtonId);
		QuickPrayers.clickButton(c, actionButtonId);
		dialogueOption(c, actionButtonId);
		makeOptions(c, actionButtonId);


		if (c.getQuestTab().handleActionButton(realButtonId)) {
			return;
		}
		switch(realButtonId) {


			case 62354://switching to curses
				c.getDH().sendDialogues(20377, -1);
				break;
			case 62357://switching to curses
				c.getDH().sendDialogues(62357, -1);
				break;

			case 14568://upgrade now
//				if(c.instatspanel)
//					return;
				c.sendMessage("changetoupgradenow##1");
				c.getPA().upgradenow();
				break;
			case 14671://statistics close button
			//case 14564:
				//bring it into focus
				c.getPA().sendFrame70(0,0, 82021);
				c.instatspanel = true;
				break;

			case 14571://statistics
				c.getPA().updatepoints();
				c.getPA().sendFrame70(0,0, 82021);
				break;
			case 16488:
				c.getPA().sendFrame70(0,-1000, 82021);
				break;
			case 14668://
				if (c.getDiscordUser() > 0) {
					c.sendMessage("Your account is already linked to the discord.");
				} else {
					c.startDiscordIntegration();

				}

				break;
			case 48472:// ? button in upgrade now interface
				int category = c.getRights().isOrInherits(Right.REGULAR_DONATOR) ? 1 : c.getRights().isOrInherits(Right.EXTREME_DONOR) ? 2 : 0;
				c.getPA().openmembershipbenefits(category);
				break;

			case 47669://reg donor
				c.getPA().openmembershipbenefits(0);
				break;
			case 47670://extreme donor
				c.getPA().openmembershipbenefits(1);
				break;
			case 47671://legendary donor
				c.getPA().openmembershipbenefits(2);
				break;

			case 47672://diamond donor
				c.getPA().openmembershipbenefits(3);
				break;
			case 47673://onyx donor
				c.getPA().openmembershipbenefits(4);
				break;
			case 14569:
//				if(c.instatspanel)
//					return;
				c.getPA().openmembershipbenefits(0);
				break;


			case 14570:
//				if(c.instatspanel)
//					return;
c.openmailbox();
				break;
				case 47466:
c.sendMessage("changetomembershipbenefits##0");
				break;
			case 14669:
				new DialogueBuilder(c).option(
						new DialogueOption("Hiscores", plr -> plr.getPA().sendFrame126("https://Runescape.com/hiscores/index.php", 12000)),
						new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
				).send();//					PathFinder.getPathFinder().findRouteNoclip(c, c.absX, ytogoto, true, 0, 0);
				c.setNewWalkCmdIsRunning(false);
				break;
			case 14667:
				new DialogueBuilder(c).option(
						new DialogueOption("Store", plr -> plr.getPA().sendFrame126("https://Runescape.com/store/index.php", 12000)),
						new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
				).send();
				break;
			case 14666:
				new DialogueBuilder(c).option(
						new DialogueOption("Website", plr -> plr.getPA().sendFrame126("https://Runescape.com/", 12000)),
						new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
				).send();
				break;
			case 14670:
				new DialogueBuilder(c).option(
						new DialogueOption("Zodian Wiki", plr -> plr.getPA().sendFrame126("https://Runescape.com/", 12000)),
						new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
				).send();
				break;
			case 14621:
				new DialogueBuilder(c).option(
						new DialogueOption("Update Archive", plr -> plr.getPA().sendFrame126("https://Runescape.com/", 12000)),
						new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
				).send();
				break;

			case 14620:
				c.getPA().displaylatestupdates();
				break;
			case 14661:
				ChangeDisplayName.clickChangeName(c);
				break;
			case 65018://first button of multiple button shop
				if (IntStream.of(mageshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(mageshop[0]);
				}
				if (IntStream.of(rangeshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(rangeshop[0]);
				}
				if (IntStream.of(meleeshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(meleeshop[0]);
				}
				break;
			case 65021://second button of multiple button shop
				if (IntStream.of(mageshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(mageshop[1]);
				}
				if (IntStream.of(rangeshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(rangeshop[1]);
				}
				if (IntStream.of(meleeshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(meleeshop[1]);
				}
				break;
			case 65024://third button of multiple button shop
				if (IntStream.of(mageshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(mageshop[2]);
				}
				if (IntStream.of(rangeshop).anyMatch(id -> id == c.myShopId)) {
					c.getShops().openShop(rangeshop[2]);
				}
//				System.out.println("shop: "+c.myShopId);
//				c.getShops().openShop(c.myShopId);

				break;
			case 13368://view time played


				long milliseconds = (long) c.playTime * 600;
				long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
				long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
				String time = days + " days, " + hours + " hrs";


				if(!c.showingtimeplayed){
					c.getPA().sendString(32578,"Time played: @gre@"+ time);
					c.showingtimeplayed = true;
				} else {
					c.getPA().sendString(32578, "Time played: Click to reveal.");
					c.showingtimeplayed = false;
				}
				break;

			case 34467://notification
				if(c.notification_broadcast)
					c.getPA().sendConfig(c.NOTIFICATION_BROADCASTS,1);
				else
					c.getPA().sendConfig(c.NOTIFICATION_BROADCASTS,0);

				c.notification_broadcast = !c.notification_broadcast;
				if(c.notification_broadcast)
					c.sendMessage("You will now see broadcasts.");
				else
					c.sendMessage("You will no longer see broadcasts.");
				break;

			case 34468://bck to wrench tab from notification
				c.setSidebarInterface(11, 42500); // wrench tab
				break;

			case 1514://world tele
//				if(TutorialDialogue.inTutorial(c))
//					return;
				c.getnewteleInterface().open();
				c.stopMovement();
				break;
			case 14675://hiscores
				c.sendMessage("@red@Opening Hiscores");
				c.getPA().sendFrame126("https://runescape.com/hiscores", 12000);
				break;
			case 1513://wiki search
				String header = "search wiki";
				c.getPA().sendEnterString(header, (pl, otherName) -> {
					pl.sendMessage("@red@Opening wiki ");
					pl.getPA().sendFrame126("https://oldschool.runescape.wiki/w/"+ otherName.replace(" ", "_"), 12000);

				});
				break;
			case 14565://clicking middle panel of player panel
				//c.updatePlayerPanel();
				break;
			case 14673:
				c.getDH().sendDialogues(10500, 1);

				break;
			case 14623:
				c.getPA().displaylatestupdates();
				break;

			case 54763://claim button task manager
				c.getTaskMasterManager().claimReward();
				break;
			case 14677:
				c.getDH().sendDialogues(10200, 1);
				break;

			case 10001:

				c.getSI().attackComplex(1);
				c.getSI().selected = 0;

				break;
			case 10004:

				c.getSI().strengthComplex(1);
				c.getSI().selected = 1;

				break;
			case 10007:

				c.getSI().defenceComplex(1);
				c.getSI().selected = 2;

				break;
			case 10010:

				c.getSI().rangedComplex(1);
				c.getSI().selected = 3;

				break;
			case 10013:

				c.getSI().prayerComplex(1);
				c.getSI().selected = 4;

				break;
			case 10016:

				c.getSI().magicComplex(1);
				c.getSI().selected = 5;

				break;
			case 10002:

				c.getSI().hitpointsComplex(1);
				c.getSI().selected = 7;

				break;
			case 10019: // runecrafting
				c.getSI().runecraftingComplex(1);
				c.getSI().selected = 6;
				break;
			case 10005: // agility
				c.getSI().agilityComplex(1);
				c.getSI().selected = 8;
				break;
			case 10008: // herblore
				c.getSI().herbloreComplex(1);
				c.getSI().selected = 9;
				break;
			case 10011: // thieving
				c.getSI().thievingComplex(1);
				c.getSI().selected = 10;
				break;
			case 10014: // crafting
				c.getSI().craftingComplex(1);
				c.getSI().selected = 11;
				break;
			case 10017: // fletching
				c.getSI().fletchingComplex(1);
				c.getSI().selected = 12;
				break;
			case 10020:// slayer
				c.getSI().slayerComplex(1);
				c.getSI().selected = 13;
				break;
			case 10003:// mining
				c.getSI().miningComplex(1);
				c.getSI().selected = 14;
				break;
			case 10006: // smithing
				c.getSI().smithingComplex(1);
				c.getSI().selected = 15;
				break;
			case 10009: // fishing
				c.getSI().fishingComplex(1);
				c.getSI().selected = 16;
				break;
			case 10012: // cooking
				c.getSI().cookingComplex(1);
				c.getSI().selected = 17;
				break;
			case 10015: // firemaking
				c.getSI().firemakingComplex(1);
				c.getSI().selected = 18;
				break;
			case 10018: // woodcut
				c.getSI().woodcuttingComplex(1);
				c.getSI().selected = 19;
				break;
			case 10021: // farming
				c.getSI().farmingComplex(1);
				c.getSI().selected = 20;
				break;
			case 10023: // hunter
				c.getSI().hunterComplex(1);
				c.getSI().selected = 21;
				break;
			case 10022: // construction
				c.getSI().constructionComplex(1);
				c.getSI().selected = 22;
				break;
			case 8846: // tab 1
				c.getSI().menuCompilation(1);
				break;

			case 8823: // tab 2
				c.getSI().menuCompilation(2);
				break;

			case 8824: // tab 3
				c.getSI().menuCompilation(3);
				break;

			case 8827: // tab 4
				c.getSI().menuCompilation(4);
				break;

			case 8837: // tab 5
				c.getSI().menuCompilation(5);
				break;

			case 8840: // tab 6
				c.getSI().menuCompilation(6);
				break;

			case 8843: // tab 7
				c.getSI().menuCompilation(7);
				break;

			case 8859: // tab 8
				c.getSI().menuCompilation(8);
				break;

			case 8862: // tab 9
				c.getSI().menuCompilation(9);
				break;

			case 8865: // tab 10
				c.getSI().menuCompilation(10);
				break;

			case 15303: // tab 11
				c.getSI().menuCompilation(11);
				break;

			case 15306: // tab 12
				c.getSI().menuCompilation(12);
				break;
			case 15309: // tab 13
				c.getSI().menuCompilation(13);
				break;

			case 59006:
				c.setSidebarInterface(8, 50651);
				break;
			case 59007:
				c.setSidebarInterface(8, 50650);
				break;
			case 8812:
			case 22466:
			case 84071:
			case 41105:
			case 183155:
			case 39503:
				c.getPA().closeAllWindows();
				break;
			case 88005-65536://favorite
			case 88006-65536:
			case 88007-65536:
			case 88008-65536:
			case 88009-65536:
			case 88010-65536:
			case 88011-65536:
			case 88012-65536:
			case 88013-65536:
				c.getnewteleInterface().drawInterface(realButtonId+65536);
				break;
			//normal
			case 1164: //varrock
			case 1167: //lumb
			case 1170: //fally
			case 1174: //cammy
			case 1540: //ardy
			case 1541: //watchtower
			case 7455: //trollheim
				//case 32649: //kourend
				//ancients
			case 13035:
			case 13045:
			case 13053:
			case 13061:
			case 13069:
			case 13079:
			case 13087:
			case 13095:
				//lunar
			case 30064: //moonclan
			case 30083: //ourania
			case 30106: //waterbirth
			case 30138: //barbarian
			case 30162: //khazard
			case 30226: //fishing guild
			case 30250: //catherby
			case 20266: //ice plateu
			case 18574://arceeus library
				CityTeleports.teleport(c, realButtonId);
				//c.getPortalTeleports().openInterface();
				//	c.getNewteleInterface().open();
				break;
		}
		switch (actionButtonId) {
			case 108003:
				if (System.currentTimeMillis() - c.logoutDelay > 10000) {
					PriceChecker.open(c);
				} else {
					c.sendMessage("I can't open this in combat..");
				}
				break;
			case 111131:
				if (c.xInterfaceId == 43933 || c.isChecking) {
					PriceChecker.clearConfig(c);
					c.getPA().closeAllWindows();
				}
				break;

			case 92125:
				XPBoostInformation.openInformationInterface(c);
				break;
			case 21004:
			case 31200:
			case 59106:
				c.getPA().closeAllWindows();
				break;
			case 27203:
//				if (!c.getRights().isOrInherits(Right.REGULAR_DONATOR)) {
//					c.sendMessage("@red@You must be a Member to use the Teleport Button");
//				} else
					c.getnewteleInterface().open();
				break;
			case 108012: // call follower
				if (c.petSummonId > 0) {
					Arrays.stream(NPCHandler.npcs).filter(npc -> npc != null && npc.spawnedBy == c.getIndex()).forEach(npc -> {
						if (PetHandler.isPet(npc.getNpcId())) {
							npc.teleport(c.getAdjacentPosition());
						}
					});
				} else {
					c.sendMessage("You don't have a pet.");
				}
				break;
			case 132111: //abyssal demon
				c.getPA().startTeleport(1671, 10087, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132112: //ankou
				c.getPA().startTeleport(1642, 9996, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132113://black demon
				c.getPA().startTeleport(1721, 10085, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132114://bronze dragon
				c.getPA().startTeleport(1648, 10098, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132115://brutal black dragon
				c.getPA().startTeleport(1615, 10093, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132116://brutal blue dragon
				c.getPA().startTeleport(1635, 10075, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132117://brutal red dragon
				c.getPA().startTeleport(1614, 10074, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132118://cyclops
				c.getPA().startTeleport(1650, 10019, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132119://dagannoth
				c.getPA().startTeleport(1666, 9997, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132120://dust devil
				c.getPA().startTeleport(1715, 10025, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132121://deviant spectre
				c.getPA().startTeleport(1607, 10011, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132122://fire giant
				c.getPA().startTeleport(1632, 10058, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132123://ghost
				c.getPA().startTeleport(1662, 10024, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132124://greater demon
				c.getPA().startTeleport(1685, 10086, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132125://greater nech
				c.getPA().startTeleport(1701, 10080, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132126://hellhound
				c.getPA().startTeleport(1646, 10065, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132127://hill giant
				c.getPA().startTeleport(1653, 10037, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132128://iron dragon
				c.getPA().startTeleport(1665, 10089, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132129://king sand crab
				c.sendMessage("This NPC is currently unavailable in the Catacombs.");//done
				break;
			case 132130://magic axe
				c.getPA().startTeleport(1640, 10035, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132131://moss giant
				c.getPA().startTeleport(1689, 10033, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132132://mutated bloodveld
				c.getPA().startTeleport(1677, 10074, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132133://possessed pickaxe
				c.getPA().startTeleport(1640, 10035, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132134://steel dragon
				c.getPA().startTeleport(1608, 10054, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132135://shade
				c.getPA().startTeleport(1607, 10028, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132136://skeleton
				c.getPA().startTeleport(1639, 10046, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132137://twisted banshee
				c.getPA().startTeleport(1617, 9997, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132138://warped jelly
				c.getPA().startTeleport(1687, 9996, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 132139://dark beast
				c.getPA().startTeleport(1621, 10060, 0, "MODERN", false);//done
				c.getPA().closeAllWindows();
				break;
			case 185152:
				c.getCollectionLog().openInterface(c);
				break;
			case 19236:
				if (c.getOutStream() != null) {
					c.getOutStream().createFrame(248);
					c.getOutStream().writeWordA(54000);
					c.getOutStream().writeUnsignedWord(5065);
					c.flushOutStream();
				}
				break;
			case 111133:
				PriceChecker.depositall(c);
				break;
			case 57057:
				c.getBH().teleportToTarget();
				break;
			case 183156:

//				if (!(c.getSuperMysteryBox().canMysteryBox) || !(c.getBlackAodLootChest().canMysteryBox) || !(c.getNormalMysteryBox().canMysteryBox) ||
//						!(c.getUltraMysteryBox().canMysteryBox) || !(c.getFoeMysteryBox().canMysteryBox) ||
//						!(c.getYoutubeMysteryBox().canMysteryBox)
//				) {
//					c.getPA().showInterface(47000);
//					c.sendMessage("@red@[WARNING] @blu@Please do not interrupt or you @red@WILL@blu@ lose items! @red@NO REFUNDS");
//					return;
//				}

				switch(c.boxCurrentlyUsing) {
					case 787:
						c.getResourceBoxLarge().spin();
						break;
					case 29973:
						c.getYoutubeMysteryBox().spin();
						break;
					case 29971: //ultra rare
						c.getUltraMysteryBox().spin();
						break;
					case 29981:
						c.getNormalMysteryBox().spin();
						break;
					case 29980:
						c.getSuperMysteryBox().spin();
						break;
					case 30109:
						c.getBlackAodLootChest().spin();
						break;
					case 6199:
						c.getNormalMysteryBox().spin();
						break;
					case 29972:
						c.getFoeMysteryBox().spin();
						break;
				}
				break;

			case 130131:
				c.getPA().removeAllWindows();
				c.sendMessage("@bla@You currently have @red@"+c.exchangePoints +"@bla@ Exchange Points.");
				c.sendMessage("@bla@Examine pets or use @red@::foepets@bla@ to learn more about the FoE pets.");
				c.getShops().openShop(171);
				break;
			case 130133:
				if (!Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS)) {
					c.getPA().removeAllWindows();
					c.getDH().sendDialogues(130135, 7456); //asks if you want to delete your item in foe or not
				}
				break;

			/*
			 * case 166056: c.getPA().showInterface(53000); break;
			 */
			case 75007:
				c.sendMessage("Please use the normal enchant spells to enchant bolts.");
				break;
			case 90077:
				c.getPA().showInterface(37700);
				break;

			case 113234:// Players online
				c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
				break;

			case 226158:
				if (c.inBank == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inBank == false) {
					CheatEngineBlock.BankAlert(c);
					return;
				}
				c.placeHolders = !c.placeHolders;
				c.getPA().sendChangeSprite(58014, c.placeHolders ? (byte) 1 : (byte) 0);
				break;

			// Close interface for drop checker
			case 152109:
			case 128234:
			case 186122://lookback
			case 174194:
				c.getPA().removeAllWindows();
				break;

			case 242150:
			case 154078:
				c.getPA().closeAllWindows();
				break;
//
//			case 183155:
//				c.getNormalMysteryBox().canMysteryBox = false;
//				c.inDonatorBox = true;
//				c.getPA().closeAllWindows();
//				break;
			case 185150:
				c.getPA().sendFrame126(Configuration.DONATOR_BENEFITS_LINK, 12000);
				break;
			case 185151:
			case 166048:
				c.getTitles().display();
				break;

			case 148137:
				c.getPA().sendInterfaceHidden(1, 38020);
				c.getPA().sendChangeSprite(38006, (byte) 1);
				c.getPA().sendChangeSprite(38007, (byte) 1);
				c.getPA().sendChangeSprite(38008, (byte) 1);
				c.sendMessage("You decided to end your donation to the well of goodwill.");
				break;

			case 23132:
				c.setSidebarInterface(1, 3917); // Skilltab > 3917
				//c.setSidebarInterface(2, 638); // 638
				c.setSidebarInterface(2, QuestTab.INTERFACE_ID);
				c.setSidebarInterface(3, 3213);
				c.setSidebarInterface(4, 1644);
				c.setSidebarInterface(5, 5608);
				switch (c.playerMagicBook) {
					case 0:
						c.setSidebarInterface(6, 938); // modern
						break;

					case 1:
						c.setSidebarInterface(6, 838); // ancient
						break;

					case 2:
						c.setSidebarInterface(6, 29999); // ancient
						break;
				}
				c.setSidebarInterface(7, 18128);
				c.setSidebarInterface(8, 5065);
				c.setSidebarInterface(9, 5715);
				c.setSidebarInterface(10, 2449);
				c.setSidebarInterface(11, 23000); // wrench tab
				c.setSidebarInterface(12, 147); // run tab
				c.setSidebarInterface(0, 2423);
				// if (c.playerEquipment[c.playerRing] == 7927) {
				// c.getItems().deleteEquipment(c.playerEquipment[c.playerRing], c.playerRing);
				// c.getItems().addItem(7927,1);
				// }
				c.playerStandIndex = 808;
				c.morphed = false;
				c.isNpc = false;
				c.setUpdateRequired(true);
				c.appearanceUpdateRequired = true;
				break;

			case 19136:
				QuickPrayers.toggle(c);
				c.getPA().sendConfig(197, 1);
				break;
			case 19137:
				for (int p = 0; p < CombatPrayer.PRAYER.length; p++) { // reset prayer glows
					if (c.prayerActive[p]) {
						c.sendMessage("You need to deactivate your active prayers before doing this.");
						return;
					}
				}
				if(c.usingcurseprayers){
					c.sendMessage("@red@Coming soon!");
					return;
				}
				c.isSelectingQuickprayers = true;
				c.setSidebarInterface(5, 17200);
				break;

			case 114093:
				c.setSidebarInterface(2, 29265); // 29265
				break;
			case 114083:
				c.setSidebarInterface(2, 638);
				break;

			case 97136:
				if (System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
//				if (c.inBank == false) {
//					CheatEngineBlock.BankAlert(c);
//					return;
//				}
				c.inPresets = true;
				//if (c.getRights().contains(Right.OWNER)) {
				PresetManager.getSingleton().open(c);
				//c.sendMessage("@red@Presets are being fixed, will be back soon."); // its sending this message even though its slashed out
				break;

			/**
			 * Dialogue Handling
			 */



			case 255255:
				c.sendMessage("You reset your experience counter.");
				c.setExperienceCounter(0L);
				break;

			case 135114:
			case 92122:
			case 118026:
				if (c.getPosition().inClanWars() || c.getPosition().inClanWarsSafe()) {
					c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
					return;
				}
				c.getBH().teleportToTarget();
				break;

			case 4135:
			case 62005:
				NonCombatSpellData.attemptDate(c, actionButtonId);
				break;

			case 55095: // Destroy item yes
				c.getPA().removeAllWindows();
				if (c.destroyItem == null || c.destroyItem.getItemId() == -1 || c.isDead)
					return;

				if (!c.getItems().isItemInInventorySlot(c.destroyItem.getItemId(), c.destroyItem.getItemSlot()))
					return;

				int itemId = c.destroyItem.getItemId();
				switch (c.destroyItem.getType()) {
					case DESTROY:
						if (ItemDef.forId(itemId).isStackable())
							return;
						c.getItems().deleteItem(c.destroyItem.getItemId(), c.destroyItem.getItemSlot(), 1);
						break;
					case DROP:
						DropItem.dropItem(c, c.destroyItem.getItemId(), c.destroyItem.getItemSlot());
						break;
					case LOW_ALCH:
					case HIGH_ALCH:
						c.usingMagic = true;
						c.getPA().alchemy(itemId, c.destroyItem.getType() == DestroyType.HIGH_ALCH ? "high" : "low");
						break;
				}

				c.destroyItem = null;
				break;
			case 55096: // Destroy item no
				c.getPA().removeAllWindows();
				c.destroyItem = null;
				break;

			/*
			 * case 191109: c.getAchievements().currentInterface = 0;
			 * c.getAchievements().drawInterface(0); break;
			 *
			 * case 191110: c.getAchievements().currentInterface = 1;
			 * c.getAchievements().drawInterface(1); break;
			 *
			 * case 191111: c.getAchievements().currentInterface = 2;
			 * c.getAchievements().drawInterface(2); break;
			 */

			case 250002:
			case 140244:
			case 141088:
			case 148122:
				c.getPA().closeAllWindows();
				break;
			case 24150:
				c.getPA().closeAllWindows();
				break;

			case 20174:
				if (System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (c.inBank == false) {
					CheatEngineBlock.BankAlert(c);
					return;
				}
				c.clickDelay = System.currentTimeMillis();
				c.getPA().closeAllWindows();
				c.getBankPin().openInterface();
				break;

			case 226162:
				if (System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				if (!c.inBank) {
					GroupIronmanBank.bankAll(c, false);
					return;
				}

				if (c.inBank == false) {
					CheatEngineBlock.BankAlert(c);
					return;
				}
				if (c.getPA().viewingOtherBank) {
					c.getPA().resetOtherBank();
					return;
				}
				if (!c.isBanking)
					return;

				for (int slot = 0; slot < c.playerItems.length; slot++) {
					if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
						c.getItems().addToBank(c.playerItems[slot] - 1, c.playerItemsN[slot], false);
					}
				}
				c.getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
				c.getItems().queueBankContainerUpdate();
				c.getItems().resetTempItems();
				break;

			case 226170:
				if (System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				if (!c.inBank) {
					GroupIronmanBank.bankAll(c, true);
					return;
				}

				if (c.inBank == false) {
					CheatEngineBlock.BankAlert(c);
					return;
				}
				if (c.getPA().viewingOtherBank) {
					c.getPA().resetOtherBank();
					return;
				}
				if (!c.isBanking)
					return;
				if (c.getBankPin().requiresUnlock()) {
					c.isBanking = false;
					c.getBankPin().open(2);
					return;
				}
				if (!c.getMode().isBankingPermitted()) {
					c.sendMessage("You cannot do that with a storage chest.");
					return;
				}
				for (int slot = 0; slot < c.playerEquipment.length; slot++) {
					if (c.playerEquipment[slot] > 0 && c.playerEquipmentN[slot] > 0) {
						if (c.getItems().addEquipmentToBank(c.playerEquipment[slot], slot, c.playerEquipmentN[slot],
								false)) {
							c.getItems().equipItem(-1, 0, slot);
						} else {
							c.sendMessage("Your bank is full.");
							break;
						}
					}
				}
				c.getPA().resetAutocast();
				c.getItems().addContainerUpdate(ContainerUpdate.INVENTORY);
				c.getItems().queueBankContainerUpdate();
				c.getItems().resetTempItems();
				break;

			case 226186:
			case 226198:
			case 226209:
			case 226220:
			case 226231:
			case 226242:
			case 226253:
			case 227008:
			case 227019:
				if (c.inBank == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}
				if (!c.isBanking) {
					c.getPA().removeAllWindows();
					return;
				}
				if (c.getBankPin().requiresUnlock()) {
					c.isBanking = false;
					c.getBankPin().open(2);
					return;
				}
				int tabId = actionButtonId == 226186 ? 0
						: actionButtonId == 226198 ? 1
						: actionButtonId == 226209 ? 2
						: actionButtonId == 226220 ? 3
						: actionButtonId == 226231 ? 4
						: actionButtonId == 226242 ? 5
						: actionButtonId == 226253 ? 6
						: actionButtonId == 227008 ? 7
						: actionButtonId == 227019 ? 8 : -1;
				if (tabId <= -1 || tabId > 8)
					return;
				c.previousTab = c.getBank().getCurrentBankTab().getTabId();
				BankTab tab = c.getBank().getBankTab(tabId);
				if (tab.getTabId() == c.getBank().getCurrentBankTab().getTabId())
					return;
				if (tab.size() <= 0 && tab.getTabId() != 0) {
					c.sendMessage("Drag an item into the new tab slot to create a tab.");
					return;
				}
				c.getBank().setCurrentBankTab(tab);
				c.getPA().c.itemAssistant.openUpBank();
				break;

			case 226197:
			case 226208:
			case 226219:
			case 226230:
			case 226241:
			case 226252:
			case 227007:
			case 227018:
				if (c.getPA().viewingOtherBank) {
					c.getPA().resetOtherBank();
					return;
				}
				if (!c.isBanking) {
					c.getPA().removeAllWindows();
					return;
				}
				if (c.getBankPin().requiresUnlock()) {
					c.isBanking = false;
					c.getBankPin().open(2);
					return;
				}
				tabId = actionButtonId == 226197 ? 1
						: actionButtonId == 226208 ? 2
						: actionButtonId == 226219 ? 3
						: actionButtonId == 226230 ? 4
						: actionButtonId == 226241 ? 5
						: actionButtonId == 226252 ? 6
						: actionButtonId == 227007 ? 7
						: actionButtonId == 227018 ? 8 : -1;
				tab = c.getBank().getBankTab(tabId);
				if (tab == null || tab.getTabId() == 0 || tab.size() == 0) {
					c.sendMessage("You cannot collapse this tab.");
					return;
				}
//			if (tab.size() + c.getBank().getBankTab()[0].size() >= Configuration.BANK_TAB_SIZE) {
//				c.sendMessage("You cannot collapse this tab. The contents of this tab and your");
//				c.sendMessage("main tab are greater than " + Configuration.BANK_TAB_SIZE + " unique items.");
//				return;
//			}
				for (BankItem item : tab.getItems()) {
					if (!c.getMode().isBankingPermitted()) {
						if (!UimStorageChest.isStorageItem(c, item.getId())) {
							c.sendMessage("Your game mode prohibits use of the banking system.");
							return;
						}
					}
					c.getBank().getBankTab()[0].add(item);
				}
				tab.getItems().clear();
				c.getBank().openTab(0);
				c.getPA().c.itemAssistant.openUpBank();
				break;

			case 226185:
			case 226196:
			case 226207:
			case 226218:
			case 226229:
			case 226240:
			case 226251:
			case 227006:
			case 227017:
				if (c.getPA().viewingOtherBank) {
					c.getPA().resetOtherBank();
					return;
				}
				if (!c.isBanking) {
					c.getPA().removeAllWindows();
					return;
				}
				if (c.getBankPin().requiresUnlock()) {
					c.isBanking = false;
					c.getBankPin().open(2);
					return;
				}
				tabId = actionButtonId == 226185 ? 0
						: actionButtonId == 226196 ? 1
						: actionButtonId == 226207 ? 2
						: actionButtonId == 226218 ? 3
						: actionButtonId == 226229 ? 4
						: actionButtonId == 226240 ? 5
						: actionButtonId == 226251 ? 6
						: actionButtonId == 227006 ? 7
						: actionButtonId == 227017 ? 8 : -1;
				tab = c.getBank().getBankTab(tabId);
				long value = 0;
				if (tab == null || tab.size() == 0)
					return;
				for (BankItem item : tab.getItems()) {
					long tempValue = item.getId() - 1 == 995 ? 1 : ShopAssistant.getItemShopValue(item.getId() - 1);
					value += tempValue * item.getAmount();
				}

				c.sendMessage("<col=255>The total net worth of tab " + tab.getTabId() + " is </col><col=600000>"
						+ Misc.insertCommas(String.valueOf(value)) + " gp</col>.");

				if (tabId == 0) {
					value = 0;
					for (BankTab tabIt : c.getBank().getBankTab()) {
						for (BankItem item : tabIt.getItems()) {
							long tempValue = item.getId() - 1 == 995 ? 1 : ShopAssistant.getItemShopValue(item.getId() - 1);
							value += tempValue * item.getAmount();
						}
					}

					c.sendMessage("<col=255>The total net worth of all tabs is </col><col=600000>"
							+ Misc.insertCommas(String.valueOf(value)) + " gp</col>.");
				}
				break;

			case 22024:
			case 86008:
				c.getPA().c.itemAssistant.openUpBank();
				break;
			case 140162:
				c.getPA().removeAllWindows();
				break;
			/** End Achievement Interface - Grant **/
			// case 113248: //Spawntab
			// c.getPA().sendFrame171(0, 36200);
			// c.setSidebarInterface(2, 36200); //638
			// break;
			// case 141112:
			// c.setSidebarInterface(2, 638); //638
			// c.getPA().sendFrame171(1, 36200);
			// c.getPA().sendFrame126("Name", 36202);
			// c.getPA().sendFrame126("Amount", 36205);
			// break;

			case 164034:
			case 164035:
			case 164036:
			case 164037:
				int index = actionButtonId - 164034;
				String[] removed = c.getSlayer().getRemoved();
				if (index < 0 || index > removed.length - 1) {
					return;
				}
				if (removed[index].isEmpty()) {
					c.sendMessage("There is no task in this slot that is being blocked.");
					return;
				}
				removed[index] = "";
				c.getSlayer().setRemoved(removed);
				c.getSlayer().updateCurrentlyRemoved();
				break;

			case 164028:
				c.getSlayer().cancelTask();
				break;
			case 164029:
				c.getSlayer().removeTask();
				break;

			case 162030:
			case 164018:
			case 160042:
				c.getPA().removeAllWindows();
				break;
			case 251246:
				c.getPA().removeAllWindows();
				for (int i = 0; i < 12; i++) {
					c.getPA().itemOnInterface(-1, -1, 64503, i);
				}
				break;

			case 114121:
				c.getDiaryManager().getVarrockDiary().display();
				break;
			case 114122:
				c.getDiaryManager().getArdougneDiary().display();
				break;
			case 114123:
				c.getDiaryManager().getDesertDiary().display();
				break;
			case 114124:
				c.getDiaryManager().getFaladorDiary().display();
				break;
			case 114125:
				c.getDiaryManager().getFremennikDiary().display();
				break;
			case 114126:
				c.getDiaryManager().getKandarinDiary().display();
				break;
			case 114127:
				c.getDiaryManager().getKaramjaDiary().display();
				break;
			case 114128:
				c.getDiaryManager().getLumbridgeDraynorDiary().display();
				break;
			case 114129:
				c.getDiaryManager().getMorytaniaDiary().display();
				break;
			case 114130:
				c.getDiaryManager().getWesternDiary().display();
				break;
			case 114134:
				c.getDiaryManager().getWildernessDiary().display();
				break;
			case 40196:
				break;
			case 39243:
				c.forcedChat("My KDR is [Kills: "+c.killcount+ " Deaths "+c.deathcount+"]");
				break;
			case 39241:
			/*long milliseconds = (long) c.playTime * 600;
			long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
			long hours = TimeUnit.MILLISECONDS.toHours(milliseconds - TimeUnit.DAYS.toMillis(days));
			String time = days + " days, " + hours + " hours.";
			c.forcedChat("I've played Sovark for a total of : " + time);*/
				break;

			case 113240:
				//c.forcedChat("I currently have: " + c.pkp + " PK Points.");
				break;
			case 113241:
				//	c.forcedChat("I currently have: " + c.donatorPoints + " Donator Points.");
				break;
			case 113242:
				//c.forcedChat("I currently have: " + c.votePoints + " Vote Points.");
				break;
			case 113243:
				//c.forcedChat("I currently have: " + c.pcPoints + " PC Points.");
				break;
			case 185154: // view the forums
				c.getPA().sendFrame126(Configuration.WEBSITE, 12000);
				break;
			case 185155: // Discord
				c.getPA().sendFrame126(Configuration.DISCORD_INVITE, 12000);
				break;
			case 185156: // Store
				c.getPA().sendFrame126(Configuration.STORE_LINK, 12000);
				break;
			case 185157: // in-game rules
				c.getPA().sendFrame126(Configuration.RULES_LINK, 12000);
				break;
			case 185158: // forum guides
				c.getPA().sendFrame126(Configuration.GUIDES_LINK, 12000);
				break;
			case 113244:
				c.forcedChat("I currently have: " + c.getArenaPoints() + " Mage Arena Points.");
				break;
			case 113246:
				c.sendMessage("@blu@I currently have: " + c.getSlayer().getConsecutiveTasks() + " consecutive slayer tasks.");
				break;
			case 39244:
				break;
			case 56129:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 21;
				c.sendMessage("lampbox##21");
				c.sendMessage("You select Construction");
				break;
			case 56131:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 22;
				c.sendMessage("lampbox##22");
				c.sendMessage("You select Hunter");
				break;
			case 10252:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 0;
				c.sendMessage("lampbox##0");
				c.sendMessage("You select Attack");
				break;
			case 10253:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 2;
				c.sendMessage("You select Strength");
				c.sendMessage("lampbox##1");
				break;
			case 10254:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 4;
				c.sendMessage("You select Ranged");
				c.sendMessage("lampbox##2");
				break;
			case 10255:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 6;
				c.sendMessage("You select Magic");
				c.sendMessage("lampbox##3");
				break;
			case 11000:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 1;
				c.sendMessage("You select Defence");
				c.sendMessage("lampbox##4");
				break;
			case 116181:
				c.getPA().closeAllWindows();
				break;
			case 11001:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 3;
				c.sendMessage("You select Hitpoints");
				c.sendMessage("lampbox##5");
				break;
			case 11002:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 5;
				c.sendMessage("You select Prayer");
				c.sendMessage("lampbox##6");
				break;
			case 11003:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 16;
				c.sendMessage("You select Agility");
				c.sendMessage("lampbox##7");
				break;
			case 11004:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 15;
				c.sendMessage("You select Herblore");
				c.sendMessage("lampbox##8");
				break;
			case 11005:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 17;
				c.sendMessage("You select Thieving");
				c.sendMessage("lampbox##9");
				break;
			case 11006:
				if (c.inLamp == false && c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 12;
				c.sendMessage("You select Crafting");
				c.sendMessage("lampbox##10");
				break;
			case 11007:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 20;
				c.sendMessage("You select Runecrafting");
				c.sendMessage("lampbox##11");
				break;
			case 47002:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 18;
				c.sendMessage("You select Slayer");
				c.sendMessage("lampbox##19");
				break;
			case 54090:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 19;
				c.sendMessage("You select Farming");
				c.sendMessage("lampbox##20");
				break;
			case 11008:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 14;
				c.sendMessage("You select Mining");
				c.sendMessage("lampbox##12");
				break;
			case 11009:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 13;
				c.sendMessage("You select Smithing");
				c.sendMessage("lampbox##13");
				break;
			case 11010:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 10;
				c.sendMessage("You select Fishing");
				c.sendMessage("lampbox##14");
				break;
			case 11011:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 7;
				c.sendMessage("You select Cooking");
				c.sendMessage("lampbox##15");
				break;
			case 11012:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 11;
				c.sendMessage("You select Firemaking");
				c.sendMessage("lampbox##16");
				break;
			case 11013:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 8;
				c.sendMessage("You select Woodcutting");
				c.sendMessage("lampbox##17");
				break;
			case 11014:
				if (c.inLamp == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
					return;
				}

				c.antiqueItemResetSkillId = 9;
				c.sendMessage("You select Fletching");
				c.sendMessage("lampbox##18");
				break;
			case 11015:
//				if (c.inLamp == false && AvatarOfCreation.activeKronosSeed == false && System.currentTimeMillis() - c.clickDelay <= 2200) {
//					return;
//				}
//				if (c.getItems().playerHasItem(13148)) {
//					if (c.getItems().freeEquipmentSlots() != 14) {
//						c.sendMessage("Please take off all equipment before doing this.");
//						return;
//					}
//					if (c.antiqueItemResetSkillId == 3) {
//						c.sendMessage("@red@You cannot reset your hitpoints level.");
//						return;
//					}
//					c.playerLevel[c.antiqueItemResetSkillId] = 1;
//					c.playerXP[c.antiqueItemResetSkillId] = c.getPA().getXPForLevel(1) + 1;
//					c.getPA().refreshSkill(c.antiqueItemResetSkillId);
//					c.getPA().setSkillLevel(c.antiqueItemResetSkillId, c.playerLevel[c.antiqueItemResetSkillId], c.playerXP[c.antiqueItemResetSkillId]);
//					c.getItems().deleteItem(13148, 1);
//					c.sendMessage("@red@You have reset your skill of choice.");
//					c.getPA().closeAllWindows();
//					return;
//				}
//
//				if (!c.getItems().playerHasItem(2528) && (!c.getItems().playerHasItem(Items.DARK_RELIC) && (!c.getItems().playerHasItem(13148)))) {
//					return;
//				}


				if (c.usingLamp) {
					if (c.getItems().playerHasItem(2528) && c.normalLamp && !c.antiqueLamp) {
						c.usingLamp = false;
						c.inLamp = false;


							c.getPA().addSkillXP(c.playerLevel[c.antiqueItemResetSkillId] * 10, c.antiqueItemResetSkillId, true);



						c.getItems().deleteItem(2528, 1);
						c.sendMessage("The lamp mysteriously vanishes...");

						c.getPA().closeAllWindows();
					}



				}
				break;

			/*
			 * case 28172: if (c.expLock == false) { c.expLock = true; c.sendMessage(
			 * "Your experience is now locked. You will not gain experience.");
			 * c.getPA().sendFrame126( "@whi@EXP: @gre@LOCKED", 7340); } else { c.expLock =
			 * false; c.sendMessage(
			 * "Your experience is now unlocked. You will gain experience.");
			 * c.getPA().sendFrame126( "@whi@EXP: @gre@UNLOCKED", 7340); } break;
			 */
			case 28215:
				if (c.getSlayer().getTask().isPresent()) {
					c.sendMessage("You do not have a task, please talk with a slayer master!");
				} else {
					c.forcedChat("I must slay another " + c.getSlayer().getTaskAmount() + " "
							+ c.getSlayer().getTask().get().getPrimaryName() + ".");
				}
				break;
			case 185149:
				c.getPA().showInterface(39500);
				break;
			case 15147:// Bronze, 1
				Smelting.startSmelting(c, "bronze", "ONE", "FURNACE");
				break;
			case 15146:// Bronze, 5
				Smelting.startSmelting(c, "bronze", "FIVE", "FURNACE");
				break;
			case 10247:// Bronze, 10
				Smelting.startSmelting(c, "bronze", "TEN", "FURNACE");
				break;
			case 9110:// Bronze, 28
				Smelting.startSmelting(c, "bronze", "ALL", "FURNACE");
				break;
			case 15151:// Iron, 1
				Smelting.startSmelting(c, "iron", "ONE", "FURNACE");
				break;
			case 15150:// Iron, 5
				Smelting.startSmelting(c, "iron", "FIVE", "FURNACE");
				break;
			case 15149:// Iron, 10
				Smelting.startSmelting(c, "iron", "TEN", "FURNACE");
				break;
			case 15148:// Iron, 28
				Smelting.startSmelting(c, "iron", "ALL", "FURNACE");
				break;
			case 15155:// silver, 1
				Smelting.startSmelting(c, "silver", "ONE", "FURNACE");
				break;
			case 15154:// silver, 5
				Smelting.startSmelting(c, "silver", "FIVE", "FURNACE");
				break;
			case 15153:// silver, 10
				Smelting.startSmelting(c, "silver", "TEN", "FURNACE");
				break;
			case 15152:// silver, 28
				Smelting.startSmelting(c, "silver", "ALL", "FURNACE");
				break;
			case 15159:// steel, 1
				Smelting.startSmelting(c, "steel", "ONE", "FURNACE");
				break;
			case 15158:// steel, 5
				Smelting.startSmelting(c, "steel", "FIVE", "FURNACE");
				break;
			case 15157:// steel, 10
				Smelting.startSmelting(c, "steel", "TEN", "FURNACE");
				break;
			case 15156:// steel, 28
				Smelting.startSmelting(c, "steel", "ALL", "FURNACE");
				break;
			case 15163:// gold, 1
				Smelting.startSmelting(c, "gold", "ONE", "FURNACE");
				break;
			case 15162:// gold, 5
				Smelting.startSmelting(c, "gold", "FIVE", "FURNACE");
				break;
			case 15161:// gold, 10
				Smelting.startSmelting(c, "gold", "TEN", "FURNACE");
				break;
			case 15160:// gold, 28
				Smelting.startSmelting(c, "gold", "ALL", "FURNACE");
				break;
			case 29017:// mithril, 1
				Smelting.startSmelting(c, "mithril", "ONE", "FURNACE");
				break;
			case 29016:// mithril, 5
				Smelting.startSmelting(c, "mithril", "FIVE", "FURNACE");
				break;
			case 24253:// mithril, 10
				Smelting.startSmelting(c, "mithril", "TEN", "FURNACE");
				break;
			case 16062:// mithril, 28
				Smelting.startSmelting(c, "mithril", "ALL", "FURNACE");
				break;
			case 29022:// addy, 1
				Smelting.startSmelting(c, "adamant", "ONE", "FURNACE");
				break;
			case 29021:// addy, 5
				Smelting.startSmelting(c, "adamant", "FIVE", "FURNACE");
				break;
			case 29019:// addy, 10
				Smelting.startSmelting(c, "adamant", "TEN", "FURNACE");
				break;
			case 29018:// addy, 28
				Smelting.startSmelting(c, "adamant", "ALL", "FURNACE");
				break;
			case 29026:// rune, 1
				Smelting.startSmelting(c, "rune", "ONE", "FURNACE");
				break;
			case 29025:// rune, 5
				Smelting.startSmelting(c, "rune", "FIVE", "FURNACE");
				break;
			case 29024:// rune, 10
				Smelting.startSmelting(c, "rune", "TEN", "FURNACE");
				break;
			case 29023:// rune, 28
				Smelting.startSmelting(c, "rune", "ALL", "FURNACE");
				break;

			/*
			 * case 58025: case 58026: case 58027: case 58028: case 58029: case 58030: case
			 * 58031: case 58032: case 58033: case 58034:
			 * c.getBankPin().pinEnter(actionButtonId); break;
			 */

			case 53152:
				Cooking.getAmount(c, 1);
				break;
			case 53151:
				Cooking.getAmount(c, 5);
				break;
			case 53150:
				Cooking.getAmount(c, 10);
				break;
			case 53149:
				Cooking.getAmount(c, 28);
				break;

			case 94051:
			case 93202:
			case 89061:
			case 24010:
			case 93225:
			case 93233:
			case 93209:
			case 93240:
				if (c.autoRet == 0) {
					c.autoRet = 1;
				} else {
					c.autoRet = 0;
				}
				c.getPA().sendConfig(172, c.autoRet);
				break;
			case 108005:

				c.getPA().sendFrame248(15106, 3213);
				break;
			case 108006: // items kept on death
				ItemsKeptOnDeathInterface.open(c);
				break;

			case 59004:
				c.getPA().removeAllWindows();
				break;

			case 26010:
				c.getPA().resetAutocast();
				break;
			case 166012:
				//	c.getPA().startTeleport(Configuration.RESPAWN_X, Configuration.RESPAWN_Y, Configuration.RESPAWN_Z, "modern", false);
				break;
			case 166013:
				//c.getPA().sendFrame126(Configuration.STORE_LINK, 12000);
				break;

//			case 94047:
//			case 1093:
//			case 1094:
//			case 1097:
//				if (c.autocasting) {
//					c.getPA().resetAutocast();
//					if (c.debugMessage) {
//						c.sendMessage("Reset autocast");
//					}
//				} else {
//					c.getPA().openGameframeTab(6);
//					c.sendMessage("Right-click spells to autocast.");
//				}
//				break;
			case 1093:
			case 1094:
			case 1097:
			case 94047:
				if (c.autocastId > 0) {
					c.getPA().resetAutocast();
				} else {
					if (c.playerMagicBook == 1) {
						if (c.playerEquipment[c.playerWeapon] == 4675 || c.playerEquipment[c.playerWeapon] == 6914
								|| c.playerEquipment[c.playerWeapon] == 21006
								|| c.playerEquipment[c.playerWeapon] == 12904|| c.playerEquipment[c.playerWeapon] == 22296) {
							c.setSidebarInterface(0, 1689);
						} else {
							c.sendMessage("You can't autocast ancients without a proper staff.");
						}
					} else if (c.playerMagicBook == 0) {
						if (c.playerEquipment[c.playerWeapon] == 4170 || c.playerEquipment[c.playerWeapon] == 21255) {
							c.setSidebarInterface(0, 1829);//12050
						} else {
							c.setSidebarInterface(0, 1829);
						}
					}
				}
				c.defensiveauto= actionButtonId == 94047;
				break;
			case 7212://cancel in autocast
				//c.setSidebarInterface(0,29767);
				c.getPA().resetAutocast();
				// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			//	c.getItems().sendWeapon(c.playerEquipment[Player.playerWeapon]);
			c.getPA().sendChangeSprite(18583, (byte) 0);
				c.getPA().sendFrame126("None",18584);
//				if (c.playerEquipment[c.playerWeapon] == 24424) {
//					c.getPA().sendFrame171(0, 7800);
//					c.getItems().specialAmount(c.playerEquipment[c.playerWeapon] , c.specAmount, 7812);
//				}
				break;
			/* VENG */
			case 118098:
				c.getPA().castVengeance();
				break;

			// Dueling
			case 26065:
			case 26040:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.FORFEIT);
				break;

			case 26066: // no movement
			case 26048:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				if (!duelSession.getRules().contains(DuelSessionRules.Rule.FORFEIT)) {
					duelSession.toggleRule(c, DuelSessionRules.Rule.FORFEIT);
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_MOVEMENT);
				break;

			case 26069: // no range
			case 26042:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_RANGE);
				break;

			case 26070: // no melee
			case 26043:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_MELEE);
				break;

			case 26071: // no mage
			case 26041:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_MAGE);
				break;

			case 26072: // no drinks
			case 26045:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_DRINKS);
				break;

			case 26073: // no food
			case 26046:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_FOOD);
				break;

			case 26074: // no prayer
			case 26047:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_PRAYER);
				break;

			case 26076: // obsticals
			case 26075:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.OBSTACLES);
				break;

			case 2158: // fun weapons
			case 2157:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				if (duelSession.getRules().contains(DuelSessionRules.Rule.WHIP_AND_DDS)) {
					duelSession.toggleRule(c, DuelSessionRules.Rule.WHIP_AND_DDS);
					return;
				}
				if (!DuelSessionRules.Rule.WHIP_AND_DDS.getReq().get().meets(c)
						&& !duelSession.getRules().contains(DuelSessionRules.Rule.NO_SPECIAL_ATTACK)) {
					c.getPA().sendString("You must have a whip and dragon dagger to select this.", 6684);
					return;
				}
				if (!DuelSessionRules.Rule.WHIP_AND_DDS.getReq().get().meets(duelSession.getOther(c))) {
					c.getPA().sendString("Your opponent does not have a whip and dragon dagger.", 6684);
					return;
				}
				if (duelSession.getStage().getStage() != MultiplayerSessionStage.OFFER_ITEMS) {
					c.sendMessage("You cannot change rules whilst on the second interface.");
					return;
				}
				duelSession.getRules().reset();
				for (DuelSessionRules.Rule rule : DuelSessionRules.Rule.values()) {
					index = rule.ordinal();
					if (index == 3 || index == 8 || index == 10 || index == 14) {
						continue;
					}
					duelSession.toggleRule(c, rule);
				}
				break;

			case 30136: // sp attack
			case 30137:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_SPECIAL_ATTACK);
				break;

			case 53245: // no helm
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_HELM);
				break;

			case 53246: // no cape
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_CAPE);
				break;

			case 53247: // no ammy
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_AMULET);
				break;

			case 53249: // no weapon
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_WEAPON);
				break;

			case 53250: // no body
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_BODY);
				break;

			case 53251: // no shield
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_SHIELD);
				break;

			case 53252: // no legs
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_LEGS);
				break;

			case 53255: // no gloves
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_GLOVES);
				break;

			case 53254: // no boots
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_BOOTS);
				break;

			case 53253: // no rings
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_RINGS);
				break;

			case 53248: // no arrows
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(duelSession)) {
					return;
				}
				duelSession.toggleRule(c, DuelSessionRules.Rule.NO_ARROWS);
				break;

			case 26018:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (System.currentTimeMillis() - c.getDuel().getLastAccept() < 1000) {
					return;
				}
				c.getTrade().setLastAccept(System.currentTimeMillis());
				if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
					duelSession.accept(c, MultiplayerSessionStage.OFFER_ITEMS);
				}
				break;

			case 25120:
				duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.DUEL);
				if (System.currentTimeMillis() - c.getDuel().getLastAccept() < 1000) {
					return;
				}
				c.getTrade().setLastAccept(System.currentTimeMillis());
				if (Objects.nonNull(duelSession) && duelSession instanceof DuelSession) {
					duelSession.accept(c, MultiplayerSessionStage.CONFIRM_DECISION);
				}
				break;


			case 4169: // god spell charge

				if (System.currentTimeMillis() - c.godSpellDelay < 300000L) {
					c.sendMessage("You still feel the charge in your body!");
					return;
				}
				c.usingMagic = true;
				if (!MagicRequirements.checkMagicReqs(c,48,true)) {
					return;
				}

				c.godSpellDelay = System.currentTimeMillis();
				//	c.godSpellLength = Config.GOD_SPELL_CHARGE;
				c.sendMessage("You feel charged with a magical power!");
				c.gfx100(CombatSpellData.MAGIC_SPELLS[48][3]);
				c.startAnimation(CombatSpellData.MAGIC_SPELLS[48][2]);
				c.usingMagic = false;


				break;


			case 154:
				System.out.println("Setting cape: " + c.playerEquipment[Player.playerCape]);
				if (c.playerEquipment[Player.playerCape] == -1) {
					c.sendMessage("You must be wearing a skillcape in order to do this emote.");
					return;
				}
				PlayerEmotes.performSkillcapeAnimation(c, new GameItem(c.playerEquipment[Player.playerCape]));
				break;
			case 166011:
			case 152:
				if (Boundary.isIn(c, Boundary.ICE_PATH) || c.getRunEnergy() < 1) {
					c.updateRunningToggled(false);
					return;
				}
				c.updateRunningToggled(!c.isRunningToggled());
				break;

//			case 48176:
//				c.acceptAid = !c.acceptAid;
//				c.getPA().setConfig(427, c.acceptAid ? 1 : 0);
//				break;

			case 9154:
				c.attemptLogout();
				break;

			case 226146:
				if (!c.swaping) {
					c.sendMessage("You can't disable this, please select the other option!");
					c.getPA().sendConfig(304, 1);
					return;
				}
				c.getPA().sendConfig(305, 0);
				c.swaping = false;
				break;

			case 73241:
				if (c.swaping) {
					c.sendMessage("You can't disable this, please select the other option!");
					c.getPA().sendConfig(305, 1);
					return;
				}
				c.getPA().sendConfig(304, 0);
				c.swaping = true;
				break;

			case 226154:
				c.getPA().sendConfig(117, 0);
				c.takeAsNote = false;
				break;

			case 73245:
				c.getPA().sendConfig(116, 0);
				c.takeAsNote = true;
				break;
			// home teleports
			case 4171:
			case 117048:
			case 84237:
			case 75010:
			case 72133:
				c.getPA().hometele();


				break;




			case 166056:
				c.getPA().showInterface(53000);
				break;

			case 166028:
				//c.getPA().showInterface(39000);
				break;
			/**
			 * Prayers *
			 */
			case 21233: // thick skin
				CombatPrayer.activatePrayer(c, 0);
				break;
			case 21234: // burst of str
				CombatPrayer.activatePrayer(c, 1);
				break;
			case 21235: // charity of thought
				CombatPrayer.activatePrayer(c, 2);
				break;
			case 77100: // range
				CombatPrayer.activatePrayer(c, 3);
				break;
			case 77102: // mage
				CombatPrayer.activatePrayer(c, 4);
				break;
			case 21236: // rockskin
				CombatPrayer.activatePrayer(c, 5);
				break;
			case 21237: // super human
				CombatPrayer.activatePrayer(c, 6);
				break;
			case 21238: // improved reflexes
				CombatPrayer.activatePrayer(c, 7);
				break;
			case 21239: // hawk eye
				CombatPrayer.activatePrayer(c, 8);
				break;
			case 21240:
				CombatPrayer.activatePrayer(c, 9);
				break;
			case 21241: // protect Item
				CombatPrayer.activatePrayer(c, 10);
				break;
			case 77104: // 26 range
				CombatPrayer.activatePrayer(c, 11);
				break;
			case 77106: // 27 mage
				CombatPrayer.activatePrayer(c, 12);
				break;
			case 21242: // steel skin
				CombatPrayer.activatePrayer(c, 13);
				break;
			case 21243: // ultimate str
				CombatPrayer.activatePrayer(c, 14);
				break;
			case 21244: // incredible reflex
				CombatPrayer.activatePrayer(c, 15);
				break;
			case 21245: // protect from magic
				CombatPrayer.activatePrayer(c, 16);
				break;
			case 21246: // protect from range
				CombatPrayer.activatePrayer(c, 17);
				break;
			case 21247: // protect from melee
				CombatPrayer.activatePrayer(c, 18);
				break;
			case 77109: // 44 range
				CombatPrayer.activatePrayer(c, 19);
				break;
			case 77111: // 45 mystic
				CombatPrayer.activatePrayer(c, 20);
				break;
			case 2171: // retrui
				CombatPrayer.activatePrayer(c, 21);
				break;
			case 2172: // redem
				CombatPrayer.activatePrayer(c, 22);
				break;
			case 2173: // smite
				CombatPrayer.activatePrayer(c, 23);
				break;
			case 153233: // preserve
				CombatPrayer.activatePrayer(c, 24);
				break;
			case 77113: // chiv
				CombatPrayer.activatePrayer(c, 25);
				break;
			case 77115: // piety
				CombatPrayer.activatePrayer(c, 26);
				break;
			case 153236: // rigour
				CombatPrayer.activatePrayer(c, 27);
				break;

			case 153239: // augury
				CombatPrayer.activatePrayer(c, 28);
				break;

			case 132175: // curses - protect item
				CombatPrayer.activatePrayer(c, 29);
				break;

			case 132177: // curses - sap warrior
				CombatPrayer.activatePrayer(c, 30);
				break;
			case 132179: // curses - sap range
				CombatPrayer.activatePrayer(c, 31);
				break;
			case 132181: // curses - sap mage
				CombatPrayer.activatePrayer(c, 32);
				break;
			case 132183: // curses - sap spirit
				CombatPrayer.activatePrayer(c, 33);
				break;
			case 132185: // berserker
				CombatPrayer.activatePrayer(c, 34);
				break;
			case 132187: //curses - Deflect Summoning
				CombatPrayer.activatePrayer(c, 35);
				break;
			case 132189: //  curses - Deflect magic
				CombatPrayer.activatePrayer(c, 36);
				break;
			case 132191: // curses - Deflect missiles
				CombatPrayer.activatePrayer(c, 37);
				break;
			case 132193: // curses - Deflect melee
				CombatPrayer.activatePrayer(c, 38);
				break;
			case 132195: // curses - leech attack
				CombatPrayer.activatePrayer(c, 39);
				break;
			case 132197: // curses -leech ranged
				CombatPrayer.activatePrayer(c, 40);
				break;
			case 132199: // curses - leech m
				CombatPrayer.activatePrayer(c, 41);
				break;
			case 132201: // curses - leech d
				CombatPrayer.activatePrayer(c, 42);
				break;
			case 132203: // curses - leech e
				CombatPrayer.activatePrayer(c, 43);
				break;
			case 132205: // curses -  leech energy
				CombatPrayer.activatePrayer(c, 44);
				break;
			case 132207: // curses - leech spec
				CombatPrayer.activatePrayer(c, 45);
				break;
			case 132209: // curses - wrath
				CombatPrayer.activatePrayer(c, 46);
				break;
			case 132211: // curses - SS
				CombatPrayer.activatePrayer(c, 47);
				break;
			case 132213: // curses - turmoil

				CombatPrayer.activatePrayer(c, 48);
				break;
			case 13092://accept trade
				//if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				//	c.sendMessage("You are not trading!");
				//	return;
				//}

				MultiplayerSession multiSession = Server.getMultiplayerSessionListener().getMultiplayerSession(c);

				if (Objects.isNull(multiSession)) {
					return;
				}

				if (Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS)) {
					c.sendMessage("You cannot do this right now.");
					return;
				}
				if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
					return;
				}

				if (multiSession instanceof TradeSession || multiSession instanceof FlowerPokerSession) {
					c.getTrade().setLastAccept(System.currentTimeMillis());
					multiSession.accept(c, MultiplayerSessionStage.OFFER_ITEMS);
				}

			/*if (Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS)) {
				c.sendMessage("You cannot do this right now.");
				return;
			}
			if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
				return;
			}

			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.TRADE).accept(c, MultiplayerSessionStage.OFFER_ITEMS);*/
				break;

			case 13218://accept trade 2
				multiSession = Server.getMultiplayerSessionListener().getMultiplayerSession(c);

				if (Objects.isNull(multiSession)) {
					return;
				}

				if (Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS)) {
					c.sendMessage("You cannot do this right now.");
					return;
				}
				if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
					return;
				}

				if (multiSession instanceof TradeSession || multiSession instanceof FlowerPokerSession) {
					c.getTrade().setLastAccept(System.currentTimeMillis());
					multiSession.accept(c, MultiplayerSessionStage.CONFIRM_DECISION);
				}

			/*if (!Server.getMultiplayerSessionListener().inSession(c, MultiplayerSessionType.TRADE)) {
				c.sendMessage("You are not trading!");
				return;
			}
			if (Boundary.isIn(c, Boundary.TOURNAMENT_LOBBIES_AND_AREAS)) {
				c.sendMessage("You cannot do this right now.");
				return;
			}
			if (System.currentTimeMillis() - c.getTrade().getLastAccept() < 1000) {
				return;
			}
			c.getTrade().setLastAccept(System.currentTimeMillis());
			Server.getMultiplayerSessionListener().getMultiplayerSession(c, MultiplayerSessionType.TRADE).accept(c,
					MultiplayerSessionStage.CONFIRM_DECISION);*/
				break;

			case 125011: // Click agree
				if (!c.ruleAgreeButton) {
					c.ruleAgreeButton = true;
					c.getPA().sendConfig(701, 1);
				} else {
					c.ruleAgreeButton = false;
					c.getPA().sendConfig(701, 0);
				}
				break;
			case 125003:// Accept
				if (c.ruleAgreeButton) {
					c.getPA().showInterface(3559);
				} else if (!c.ruleAgreeButton) {
					c.sendMessage("You need to agree before you can carry on.");
				}
				break;
			case 125006:// Decline
				c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
				break;
			/* End Rules Interface Buttons */
			/* Player Options */
			case 74176:
			case 166055:
				if (!c.mouseButton) {
					c.mouseButton = true;
					c.getPA().sendConfig(500, 1);
					c.getPA().sendConfig(170, 1);
				} else if (c.mouseButton) {
					c.mouseButton = false;
					c.getPA().sendConfig(500, 0);
					c.getPA().sendConfig(170, 0);
				}
				break;
			case 74184:
			case 166046:
			case 3189:
				if (!c.splitChat) {
					c.splitChat = true;
					c.getPA().sendConfig(502, 1);
					c.getPA().sendConfig(287, 1);
				} else {
					c.splitChat = false;
					c.getPA().sendConfig(502, 0);
					c.getPA().sendConfig(287, 0);
				}
				break;
//		case 74180:
//		case 166045:
//			if (!c.chatEffects) {
//				c.chatEffects = true;
//				c.getPA().sendConfig(501, 1);
//				c.getPA().sendConfig(171, 0);
//			} else {
//				c.chatEffects = false;
//				c.getPA().sendConfig(501, 0);
//				c.getPA().sendConfig(171, 1);
//			}
//			break;
//			case 162080:
//				if (!c.shiftDrop) {
//					c.shiftDrop = true;
//					c.getPA().sendConfig(501, 1);
//					c.getPA().sendConfig(171, 0);
//				} else {
//					c.shiftDrop = false;
//					c.getPA().sendConfig(501, 0);
//					c.getPA().sendConfig(171, 1);
//				}
//				break;
			case 120248:
				c.slayerpartner();
				break;
			case 74188:
			case 166010:
				if (!c.acceptAid) {
					c.acceptAid = true;
					//c.getPA().sendConfig(503, 1);
					c.getPA().sendConfig(427, 1);
				} else {
					c.acceptAid = false;
					//c.getPA().sendConfig(503, 0);
					c.getPA().sendConfig(427, 0);
				}
				break;
			case 74192:
				c.updateRunningToggled(!c.isRunningToggled());
				break;
			case 74201:// brightness1
				c.getPA().sendConfig(505, 1);
				c.getPA().sendConfig(506, 0);
				c.getPA().sendConfig(507, 0);
				c.getPA().sendConfig(508, 0);
				c.getPA().sendConfig(166, 1);
				break;
			case 74203:// brightness2
				c.getPA().sendConfig(505, 0);
				c.getPA().sendConfig(506, 1);
				c.getPA().sendConfig(507, 0);
				c.getPA().sendConfig(508, 0);
				c.getPA().sendConfig(166, 2);
				break;

			case 74204:// brightness3
				c.getPA().sendConfig(505, 0);
				c.getPA().sendConfig(506, 0);
				c.getPA().sendConfig(507, 1);
				c.getPA().sendConfig(508, 0);
				c.getPA().sendConfig(166, 3);
				break;

			case 74205:// brightness4
				c.getPA().sendConfig(505, 0);
				c.getPA().sendConfig(506, 0);
				c.getPA().sendConfig(507, 0);
				c.getPA().sendConfig(508, 1);
				c.getPA().sendConfig(166, 4);
				break;
			case 74206:// area1
				c.getPA().sendConfig(509, 1);
				c.getPA().sendConfig(510, 0);
				c.getPA().sendConfig(511, 0);
				c.getPA().sendConfig(512, 0);
				break;
			case 74207:// area2
				c.getPA().sendConfig(509, 0);
				c.getPA().sendConfig(510, 1);
				c.getPA().sendConfig(511, 0);
				c.getPA().sendConfig(512, 0);
				break;
			case 74208:// area3
				c.getPA().sendConfig(509, 0);
				c.getPA().sendConfig(510, 0);
				c.getPA().sendConfig(511, 1);
				c.getPA().sendConfig(512, 0);
				break;
			case 74209:// area4
				c.getPA().sendConfig(509, 0);
				c.getPA().sendConfig(510, 0);
				c.getPA().sendConfig(511, 0);
				c.getPA().sendConfig(512, 1);
				break;
			case 43092:
				c.startAnimation(0x558);
				break;
			/*
			 * case 72254: //c.startAnimation(3866); break; /* END OF EMOTES
			 */

			case 24017:
				c.getPA().resetAutocast();
				// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
				//c.getItems().sendWeapon(c.playerEquipment[Player.playerWeapon]);
				// c.setSidebarInterface(0, 328);
				// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 938 :
				// c.playerMagicBook == 1 ? 838 : 938);
				break;
		}

		if (c.isAutoButton(actionButtonId)) {
			if (Arrays.stream(CombatSpellData.DISABLE_AUTOCAST_STAFFS).anyMatch(staff -> c.getItems().isWearingItem(staff, Player.playerWeapon))) {
				c.sendMessage("You can't autocast with this staff.");
				return;
			}
			Spell spell = Spell.foractionbuttonId(actionButtonId);
			if (spell == null || !spell.isAutocastable()) {
				c.sendMessage("You can't autocast this spell.");
			} else {
				if (c.getCombatConfigs().getWeaponData() == WeaponData.STAFF || c.getCombatConfigs().getWeaponData() == WeaponData.SOTD) {
					if (c.getSpellBook() != spell.getSpellBook()) {
						c.sendMessage("You have to be using " + spell.getSpellBook() + " magics to autocast that spell.");
					} else {
						for (int spellIndex = 0; spellIndex < CombatSpellData.MAGIC_SPELLS.length; spellIndex++) {
							if (CombatSpellData.MAGIC_SPELLS[spellIndex][0] == spell.getId()) {
								if (c.autocasting && c.autocastId == spellIndex && c.autocastingDefensive == c.defensiveauto) {
									c.getPA().resetAutocast();
									c.debug("Reset autocast");
									return;
								} else if (MagicRequirements.checkMagicReqs(c, spellIndex, false) && AutocastSpell.verifyStaff(c, spell)) {
									c.autocasting = true;
									c.autocastingDefensive = c.defensiveauto;
									c.autocastId = spellIndex;
									c.assignAutocast(actionButtonId);
								//	updateConfig(c, spell);

									if (c.debugMessage) {
										c.sendMessage("Set autocast to index: "+c.autocastId+" " + spell.getId()+" def: "+c.autocastingDefensive);
									}

							return ;
								}

							}
						}

						//c.sendMessage("You can't autocast this spell.");
						//return;
					}
				} else {
					c.sendMessage("You can't autocast without a staff!");
				//	return;
				}
			}
			//c.assignAutocast(realButtonId);
		}
	}

	private static void dialogueOption(Player c, int buttonId) {
		switch (buttonId) {
			case 9167:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_1);
					return;
				}
				OptionHandler.handleOptions(c, 1);
				ThreeOptions.handleOption1(c);
				break;
			case 9168:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_2);
					return;
				}
				OptionHandler.handleOptions(c, 2);
				ThreeOptions.handleOption2(c);
				break;
			case 9169:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_3);
					return;
				}
				OptionHandler.handleOptions(c, 3);
				ThreeOptions.handleOption3(c);
				break;
			case 9157:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_1);
					return;
				}
				OptionHandler.handleOptions(c, 1);
				TwoOptions.handleOption1(c);
				break;

			case 9158:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_2);
					return;
				}
				OptionHandler.handleOptions(c, 2);
				TwoOptions.handleOption2(c);
				break;

			case 9178:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_1);
					return;
				}
				OptionHandler.handleOptions(c, 1);
				FourOptions.handleOption1(c);
				break;

			case 9179:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_2);
					return;
				}
				OptionHandler.handleOptions(c, 2);
				FourOptions.handleOption2(c);
				break;

			case 9180:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_3);
					return;
				}
				OptionHandler.handleOptions(c, 3);
				FourOptions.handleOption3(c);
				break;

			case 9181:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_4);
					return;
				}
				OptionHandler.handleOptions(c, 4);
				FourOptions.handleOption4(c);
				break;

			case 9190:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_1);
					return;
				}
				OptionHandler.handleOptions(c, 1);
				FiveOptions.handleOption1(c);
				break;

			case 9191:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_2);
					return;
				}
				OptionHandler.handleOptions(c, 2);
				FiveOptions.handleOption2(c);
				break;

			case 9192:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_3);
					return;
				}
				OptionHandler.handleOptions(c, 3);
				FiveOptions.handleOption3(c);
				break;

			case 9193:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_4);
					return;
				}
				OptionHandler.handleOptions(c, 4);
				FiveOptions.handleOption4(c);
				break;

			case 9194:
				if (c.getDialogueBuilder() != null) {
					c.getDialogueBuilder().getCurrent().handleAction(c, DialogueAction.OPTION_5);
					return;
				}
				OptionHandler.handleOptions(c, 5);
				FiveOptions.handleOption5(c);
				break;
		}
	}



	private static void makeOptions(Player player, int actionButtonId) {
		if (player.getDialogueBuilder() != null) {
			Optional<DialogueActionButton> dialogueActionOptional = DialogueConstants.BUTTONS.stream().filter(button
					-> Arrays.stream(button.getButtonIds()).anyMatch(buttonId -> actionButtonId == buttonId)).findFirst();
			dialogueActionOptional.ifPresent(action -> player.getDialogueBuilder().dispatchAction(action.getAction()));
		}
	}
}
