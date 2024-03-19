package io.Odyssey.model.entity.player.packets.objectoptions;

import io.Odyssey.Server;
import io.Odyssey.content.achievement_diary.impl.FaladorDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.VarrockDiaryEntry;
import io.Odyssey.content.bosses.godwars.God;
import io.Odyssey.content.bosses.grotesqueguardians.GrotesqueInstance;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosses.nex.Nex;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.dialogue.impl.OutlastLeaderboard;
import io.Odyssey.content.fireofexchange.FireOfExchangeBurnPrice;
import io.Odyssey.content.item.lootable.impl.AVATAR_OF_CREATION;
import io.Odyssey.content.item.lootable.impl.RaidsChestCommon;
import io.Odyssey.content.minigames.raids.Raids;
import io.Odyssey.content.skills.FlaxPicking;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.skills.hunter.Hunter;
import io.Odyssey.content.skills.smithing.CannonballSmelting;
import io.Odyssey.content.skills.thieving.Thieving.Stall;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.model.Items;
import io.Odyssey.model.collisionmap.ObjectDef;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.PathFinder;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.entity.player.packets.ClickObject;
import io.Odyssey.model.entity.player.packets.objectoptions.impl.DarkAltar;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Location3D;
import io.Odyssey.util.Misc;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

import static io.Odyssey.model.Items.ROPE;

/*
 * @author Matt
 * Handles all 2nd options for objects.
 */

public class ObjectOptionTwo {

	public static void handleOption(final Player c, int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		c.clickObjectType = 0;
		GlobalObject object = new GlobalObject(objectType, obX, obY, c.heightLevel);
		Location3D location = new Location3D(obX, obY, c.heightLevel);
		ObjectDef def = ObjectDef.getObjectDef(objectType);
		 if ((def != null ? def.name : null) != null && def.name.toLowerCase().contains("bank") && !Boundary.isIn(c, Boundary.OURIANA_ALTAR)) {
			c.getPA().c.itemAssistant.openUpBank();
			c.inBank = true;
			return;
		}

		if (c.getRights().isOrInherits(Right.OWNER) && c.debugMessage)
			c.sendMessage("Clicked Object Option 2:  "+objectType+"");

		if (OutlastLeaderboard.handleInteraction(c, objectType, 2))
			return;

		int face = 0;
						WorldObject worldObject = ClickObject.getObject(c, objectType,obX,obY);
				if (worldObject != null) {
						face = worldObject.getFace();
				}
		switch (objectType) {
			case 42967:
				if(!c.getRights().isOrInherits(Right.REGULAR_DONATOR))
				if(c.gwdkc[4] < 10){
					c.sendMessage("You need a Zarosian forces killcount of at least 10 to start a private Nex instance.");
					return;
				}
				c.start(new DialogueBuilder(c).statement("@red@Warning! You're about to enter an instanced area, anything left on",
                                        "@red@the ground when you leave will be lost. Would you like to continue?").

                                option("Are you sure you wish to start?",
                                        new DialogueOption("Yes", player -> {

                                            new Nex().enter(player);


                                        }),
                                        new DialogueOption("Cancel", plr -> {
                                            plr.getPA().closeAllWindows();
                                        })));
				break;

			case 26372:
				if (c.playerLevel[16] < 70) {
					c.sendMessage("You need an Agility level of 70 to climb down.");
					return;
				}
				c.getPA().movePlayer(obX+2,obY,1);
				Server.getGlobalObjects().checkGWD(c);

				break;
			case 26376:
				if (c.playerLevel[16] < 70) {
					c.sendMessage("You need an Agility level of 70 to climb down.");
					return;
				}
				c.getPA().movePlayer(obX,obY-1,0);
				Server.getGlobalObjects().checkGWD(c);

				break;

			case 23609://look inside
				int playersinkq = Boundary.getPlayersInBoundary(Boundary.KALPHITE_QUEEN);

				c.sendMessage("There are currently "+playersinkq+" players in the Kalphite Queen lair.");
//			c.getPA().movePlayer(3507, 9494, 0);
//				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2545,3742, 0, 2);
//
//				if(c.getItems().playerHasItem(995, 50)) {
//					c.getItems().deleteItem(995, 50);
//					c.getItems().addItem(ROPE,1);
//					c.getDH().sendDialogues(8355, 954);
//				} else {
//					c.sendMessage("You do not have enough gp.");
//				}
				break;

			case 3831:
				if (c.getSlayer().getTask().isPresent()) {
					if(!c.getSlayer().getTask().get().getPrimaryName().contains("dagannoth")){
						c.sendMessage("You need a dagannoth task to go down there.");
			return;
					}
				}


				break;
			case 10177: // Dagganoth kings ladder climb up opt
				AgilityHandler.delayEmote(c, "CLIMB_UP", 2545,3742, 0, 2);
				//dialog opt
				//	c.getPA().movePlayer(2900, 4449, 0);
				break;
			case 20377://altar to switch between prayer books
				c.getDH().sendDialogues(20377, -1);
				break;
			case 20134://task board
				c.getTaskMasterManager().open();
				break;

			case 16672://going up


						if(c.objectX == 2839 && c.objectY == 3537) { //warriors guild
							AgilityHandler.delayEmote(c, "CLIMB_UP", 2840, 3539, 2, 2);
						}
						if(c.objectX == 3204 && c.objectY == 3207) { //lumby

							AgilityHandler.delayEmote(c, "CLIMB_UP", 3205, 3209, 2, 2);

						}
						if(c.objectX == 3204 && c.objectY == 3229) { //lumby
							AgilityHandler.delayEmote(c, "CLIMB_UP", 3205, 3228, 2, 2);

						}

				break;

			case 11726:// Open Door @ Magic Hut
				if (c.getItems().hasItemOnOrInventory(Items.LOCKPICK)) {
					int pX = c.getX();
					int pY = c.getY();
					int yOffset = pY >= obY ? -1 : 1;
					if (obX == 3190 && obY == 3957 || obX == 3191 && obY == 3963) {
						c.sendMessage("You attempt to pick the lock...");
						boolean isLucky = Misc.isLucky(50);
						if (isLucky)
							c.moveTo(c.getPosition().translate(0, yOffset));
						else
							c.sendMessage("You fail to pick the lock!");
					}
				} else {
					c.sendMessage("You need a lockpick to pick this lock.");
				}
				break;
			case 2153:
				FireOfExchangeBurnPrice.openExchangeRateShop(c);
				break;
			case 31681:
				if (!c.gargoyleStairsUnlocked) {
					int[] keys = {275 /* old key */, GrotesqueInstance.GROTESQUE_GUARDIANS_KEY};
					OptionalInt heldKey = Arrays.stream(keys).filter(key -> c.getItems().playerHasItem(key)).findFirst();//nice.
					heldKey.ifPresentOrElse(key -> {
						c.getItems().deleteItem(key, 1);
						c.gargoyleStairsUnlocked = true;
						c.sendMessage("The gate is now open.");
					}, () -> c.sendMessage("You need a key to go through here."));
					return;
				}

				new GrotesqueInstance().enter(c);
				break;
			case 721:
				Hunter.resetTrap(c, object);
				break;
		case 31858:
		case 29150:
			c.playerMagicBook = 1;
			c.setSidebarInterface(6, 838);
			c.sendMessage("An ancient wisdom fills your mind.");
			c.getPA().resetAutocast();
			break;
		case 1295:
				c.getPA().movePlayer(3088, 3505, 0);
				c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.GRAND_TREE_TELEPORT);
				break;
		case 34660:
		case 34662:
			c.getPA().movePlayer(1309, 3786, 0);
			break;
		case 34553:
		case 34554:
			c.getDH().sendStatement("Alchemical hydra is in developement.");
			break;
		case 10060:
		case 10061:
			if (c.getMode().isIronmanType()) {
				c.sendMessage("@red@You are not permitted to make use of this.");
				return;
			}
			Listing.openPost(c, false);
			break;
		case 2884:
		case 16684:
		case 16683:
			if (c.absY == 3494 || c.absY == 3495 || c.absY == 3496) {
				AgilityHandler.delayEmote(c, "CLIMB_UP", c.getX(), c.getY(), c.getHeight() + 1, 2);
			}
			break;
		case 29333:
			if (c.getMode().isIronmanType()) {
				c.sendMessage("@red@You are not permitted to make use of this.");			}
			Listing.collectMoney(c);
			
			break;
		case 28900:
			DarkAltar.handleRechargeInteraction(c);
			break;
		case 29776:
		case 44596:
		case 29734:
		case 10777:
	//	case 29879:
			c.objectDistance = 4;

			break;
//		case 30028:
//			 if (c.getItems().freeSlots() < 3) {
//					c.getDH().sendStatement("@red@You need at least 3 free slots for safety");
//					return;
//				}
//			 if (c.getItems().playerHasItem(Raids.COMMON_KEY, 1)) {
//				 new RaidsChestCommon().roll(c);
//				 return;
//			 }
//			 if (c.getItems().playerHasItem(Raids.RARE_KEY, 1)) {
//				 new RaidsChestRare().roll(c);
//				 return;
//			 }
//			c.getDH().sendStatement("@red@You need either a rare or common key.");
//			break;
		case 7811:
			if (!c.getPosition().inClanWarsSafe()) {
				return;
			}
			c.getShops().openShop(115);
			break;
		/**
		 * Iron Winch - peek
		 */
		case 23104:
			c.getDH().sendDialogues(110, 5870);
			break;
			
		case 2118:
			c.getPA().movePlayer(3434, 3537, 0);
			break;

		case 2114:
			c.getPA().movePlayer(3433, 3537, 1);
			break;
		case 25824:
			c.facePosition(obX, obY);
			c.getDH().sendDialogues(40, -1);
			break;
		case 26260:
			c.getDH().sendDialogues(55874, -1);
			break;
		case 14896:
			c.facePosition(obX, obY);
			FlaxPicking.getInstance().pick(c, new Location3D(obX, obY, c.heightLevel));
			break;
			case 4874:
		case 11730:
			c.getThieving().steal(Stall.Baker, location, face);
			//c.objectDistance = 1;
			break;
			case 30278:
				c.getThieving().steal(Stall.Ore, location, face);
				//c.objectDistance = 1;
				break;
			case 30280:
				c.getThieving().steal(Stall.Tzhaar_gem, location, face);
				//c.objectDistance = 1;
				break;
		case 4877:
		case 11731:
			if (Boundary.isIn(c, Boundary.FALADOR_BOUNDARY)) {
				c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.STEAL_GEM_FAL);
			//	c.getThieving().steal(Stall.Magic, location);
				c.objectDistance = 1;
				return;
			}
			//c.getThieving().steal(Stall.Magic, location);
			break;
		case 11729:
			c.getThieving().steal(Stall.Silk, location,face);
			c.objectDistance = 1;
			break;
		case 4876:
			//c.getThieving().steal(Stall.General, location);
			c.objectDistance = 1;
			break;
		case 4878:
		//	c.getThieving().steal(Stall.Scimitar, location);
			c.objectDistance = 1;
			break;
		case 4875:
		//	c.getThieving().steal(Stall.Food, location);
			c.objectDistance = 1;
			break;
		case 11734:
		//	c.getThieving().steal(Stall.Silver, location);
			break;
		case 11732:
		//	c.getThieving().steal(Stall.Fur, location);
			break;
		case 11733:
		//	c.getThieving().steal(Stall.Spice, location);
			break;
		case 29165:
		//	c.getThieving().steal(Stall.Gold, location);
			break;
		case 6162:
			//c.getThieving().steal(Stall.LZ_GOLD, location);
			break;

			
		case 2558:
		case 8356://streequid
			c.getPA().movePlayer(1255, 3568, 0);
			break;
		case 2557:
			if (System.currentTimeMillis() - c.lastLockPick < 1000 || c.freezeTimer > 0) {
				return;
			}
			c.lastLockPick = System.currentTimeMillis();
			if (c.getItems().playerHasItem(1523, 1)) {

				if (Misc.random(10) <= 2) {
					c.sendMessage("You fail to pick the lock.");
					break;
				}
				if (c.objectX == 3044 && c.objectY == 3956) {
					if (c.absX == 3045) {
						c.getPA().walkTo(-1, 0);
					} else if (c.absX == 3044) {
						c.getPA().walkTo(1, 0);
					}

				} else if (c.objectX == 3038 && c.objectY == 3956) {
					if (c.absX == 3037) {
						c.getPA().walkTo(1, 0);
					} else if (c.absX == 3038) {
						c.getPA().walkTo(-1, 0);
					}
				} else if (c.objectX == 3041 && c.objectY == 3959) {
					if (c.absY == 3960) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 3959) {
						c.getPA().walkTo(0, 1);
					}
				} else if (c.objectX == 3191 && c.objectY == 3963) {
					if (c.absY == 3963) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 3962) {
						c.getPA().walkTo(0, 1);
					}
				} else if (c.objectX == 3190 && c.objectY == 3957) {
					if (c.absY == 3957) {
						c.getPA().walkTo(0, 1);
					} else if (c.absY == 3958) {
						c.getPA().walkTo(0, -1);
					}
				}
			} else {
				c.sendMessage("I need a lockpick to pick this lock.");
			}
			break;
		case 7814:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 838);
				c.sendMessage("An ancient wisdom fills your mind.");
				c.getPA().resetAutocast();
			} else if (c.playerMagicBook == 1) {
				c.sendMessage("You switch to the lunar spellbook.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.getPA().resetAutocast();
			} else if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 938);
				c.playerMagicBook = 0;
				c.sendMessage("You feel a drain on your memory.");
				c.getPA().resetAutocast();
			}
			break;
		case 17010:
			if (c.playerMagicBook == 0) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 838);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 1) {
				c.sendMessage("You switch spellbook to lunar magic.");
				c.setSidebarInterface(6, 29999);
				c.playerMagicBook = 2;
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			if (c.playerMagicBook == 2) {
				c.setSidebarInterface(6, 938);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
				break;
			}
			break;
		/*
		 * One stall that will give different amount of money depending on your thieving level, also different amount of xp.
		 */
		case 2781:
		case 26814:
		case 11666:
		case 3044:
		case 16469:
		case 2030:
		case 24009:
		case 26300:
			c.objectDistance = 1;
			if (CannonballSmelting.isSmeltingCannonballs(c)) {
				CannonballSmelting.smelt(c);
			} else {
				c.getSmithing().sendSmelting();
			}
			break;
			
			
			/**
		 * Opening the bank.
		 */
		case 24101:
		case 14367:
		case 11758:
		case 10517:
		case 26972:
		case 25808:
		case 11744:
		case 11748:
		case 24347:
		case 16700:
			c.inBank = true;
			c.getPA().c.itemAssistant.openUpBank();
			break;

		}
	}
}
