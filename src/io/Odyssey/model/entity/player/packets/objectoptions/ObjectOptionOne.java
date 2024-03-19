package io.Odyssey.model.entity.player.packets.objectoptions;

import com.google.common.collect.Lists;
import io.Odyssey.Server;
import io.Odyssey.content.Obelisks;
import io.Odyssey.content.SkillcapePerks;
import io.Odyssey.content.achievement_diary.impl.*;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreationSpawner;
import io.Odyssey.content.bosses.Cerberus;
import io.Odyssey.content.bosses.Kraken;
import io.Odyssey.content.bosses.Vorkath;
import io.Odyssey.content.bosses.godwars.God;
import io.Odyssey.content.bosses.hydra.AlchemicalHydra;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.weapon.WeaponDataConstants;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.dialogue.impl.CrystalCaveEntryDialogue;
import io.Odyssey.content.dialogue.impl.FireOfDestructionDialogue;
import io.Odyssey.content.dialogue.impl.OutlastLeaderboard;
import io.Odyssey.content.dialogue.impl.SkillingPortalDialogue;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.item.lootable.impl.*;
import io.Odyssey.content.minigames.Raids3.Raids3;
import io.Odyssey.content.minigames.Raids3.Raids3Party;
import io.Odyssey.content.minigames.barrows.Barrows;
import io.Odyssey.content.minigames.barrows.BarrowsPuzzleDisplay;
import io.Odyssey.content.minigames.barrows.TunnelDoor;
import io.Odyssey.content.minigames.barrows.TunnelDoors;
import io.Odyssey.content.minigames.inferno.Inferno;
import io.Odyssey.content.minigames.pest_control.PestControl;
import io.Odyssey.content.minigames.pk_arena.Highpkarena;
import io.Odyssey.content.minigames.pk_arena.Lowpkarena;
import io.Odyssey.content.minigames.raids.CoxParty;
import io.Odyssey.content.minigames.raids.Raids;
import io.Odyssey.content.minigames.warriors_guild.AnimatedArmour;
import io.Odyssey.content.skills.FlaxPicking;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.content.skills.agility.impl.rooftop.RooftopArdougne;
import io.Odyssey.content.skills.agility.impl.rooftop.RooftopSeers;
import io.Odyssey.content.skills.agility.impl.rooftop.RooftopVarrock;
import io.Odyssey.content.skills.hunter.Hunter;
import io.Odyssey.content.skills.runecrafting.AbyssalHandler;
import io.Odyssey.content.skills.runecrafting.RuneCraftingActions;
import io.Odyssey.content.skills.runecrafting.Runecrafting;
import io.Odyssey.content.skills.smithing.CannonballSmelting;
import io.Odyssey.content.skills.thieving.Thieving.Stall;
import io.Odyssey.content.skills.woodcutting.Tree;
import io.Odyssey.content.skills.woodcutting.Woodcutting;
import io.Odyssey.content.tournaments.ViewingOrb;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.content.wilderness.SpiderWeb;
import io.Odyssey.model.Items;
import io.Odyssey.model.collisionmap.ObjectDef;
import io.Odyssey.model.collisionmap.RegionProvider;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.collisionmap.doors.Doors;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.entity.player.mode.ModeType;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanBank;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanGroup;
import io.Odyssey.model.entity.player.mode.group.GroupIronmanRepository;
import io.Odyssey.model.entity.player.packets.ClickObject;
import io.Odyssey.model.entity.player.packets.objectoptions.impl.*;
import io.Odyssey.model.entity.player.save.PlayerSave;
import io.Odyssey.model.items.EquipmentSet;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.GroundItem;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.model.lobby.LobbyManager;
import io.Odyssey.model.lobby.LobbyType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;
import io.Odyssey.model.multiplayersession.duel.DuelSessionRules.Rule;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Location3D;
import io.Odyssey.util.Misc;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static io.Odyssey.content.dailyrewards.DailyRewardsDialogue.DAILY_REWARDS_NPC;
import static io.Odyssey.content.tutorial.TutorialDialogue.TUTORIAL_NPC;
import static io.Odyssey.model.Items.*;

/*
 * @author Matt
 * Handles all first options for objects.
 */

public class ObjectOptionOne {

	static int[] barType = { 2363, 2361, 2359, 2353, 2351, 2349 };

	public static void handleOption(final Player c, int objectType, int obX, int obY) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}


		GlobalObject object = new GlobalObject(objectType, obX, obY, c.heightLevel);

		c.getPA().resetVariables();
		c.clickObjectType = 0;
		c.facePosition(obX, obY);
		c.boneOnAltar = false;
		Tree tree = Tree.forObject(objectType);
		AbyssalHandler.handleAbyssalTeleport(c, objectType);
		RaidObjects.clickObject1(c, objectType, obX, obY);
		Raids3Objects.clickObject1(c, objectType, obX, obY);
		if (tree != null) {
			Woodcutting.getInstance().chop(c, objectType, obX, obY);
			return;
		}

		if (OutlastLeaderboard.handleInteraction(c, objectType, 1))
			return;

		if (c.getGnomeAgility().gnomeCourse(c, objectType)) {
			return;
		}

		if (c.getWildernessAgility().wildernessCourse(c, objectType)) {
			return;
		}
		if (c.getBarbarianAgility().barbarianCourse(c, objectType)) {
			return;
		}
		if (c.getBarbarianAgility().barbarianCourse(c, objectType)) {
			return;
		}
		if (c.getAgilityShortcuts().agilityShortcuts(c, objectType)) {
			return;
		}

		if (RooftopSeers.execute(c, objectType)) {
			return;
		}
		if (c.getRooftopFalador().execute(c, objectType)) {
			return;
		}
		if (RooftopVarrock.execute(c, objectType)) {
			return;
		}
		if (RooftopArdougne.execute(c, objectType)) {
			return;
		}
		if (c.getRoofTopDraynor().execute(c, objectType)) {
			return;
		}
		if (c.getRooftopAlkharid().execute(c, objectType)) {
			return;
		}


		if (c.getRooftopPollnivneach().execute(c, objectType)) {
			return;
		}
		if (c.getRooftopRellekka().execute(c, objectType)) {
			return;
		}
		if (c.getLighthouse().execute(c, objectType)) {
			return;
		}
		Doors.getSingleton().handleDoor(object,objectType,obX,obY,c.heightLevel);
		ObjectDef def = ObjectDef.getObjectDef(objectType);
		if ((def != null ? def.name : null) != null && def.name.equalsIgnoreCase("bank chest")) {
			c.getPA().c.itemAssistant.openUpBank();
			c.inBank = true;
			return;
		}

		//System.out.println("name: "+def.name);

			if ((def != null ? def.name : null) != null && def.name.toLowerCase().contains("deposit")) {

			c.getDepositBox().openDepositBox();
			return;
		}

		final int[] HUNTER_OBJECTS = { 9373, 9377, 9379, 9375, 9348, 9380, 9385, 9344, 9345, 9383, 721 };
		if (IntStream.of(HUNTER_OBJECTS).anyMatch(id -> objectType == id)) {
			if (Hunter.pickup(c, object)) {
				return;
			}
			if (Hunter.claim(c, object)) {
				return;
			}
		}
		c.getMining().mine(objectType, new Location3D(obX, obY, c.heightLevel));
		Obelisks.get().activate(c, objectType);
		Runecrafting.execute(c, objectType);
		RuneCraftingActions.handleRuneCrafting(c, objectType);


		if (c.getRaidsInstance() != null && c.getRaidsInstance().handleObjectClick(c,objectType, obX, obY)) {
			c.objectDistance = 3;
			return;
		}

		if (c.getRaids3Instance() != null && c.getRaids3Instance().handleObjectClick(c,objectType, obX, obY)) {
			c.objectDistance = 3;
			return;
		}




		final int[] BARROWS_DOORS_EW = new int[] { 20687,20686, 20706, 20696, 20715, 20710, 20691, 20702,20683, 20700, 20681, 20701, 20682, 20703, 20684, 20687,20705,20687, 20706,
				20688, 20707, 20690, 20709, 20685, 20704, 20708, 20689, 20711, 20692, 20714, 20695, 20693, 20712, 20713, 20694, 20705};
		if (IntStream.of(BARROWS_DOORS_EW).anyMatch(id -> objectType == id)) {
			Barrows barrows = c.getBarrows();
			if (barrows == null) {
				System.out.println("null");
				return;
			}
			BarrowsPuzzleDisplay puzzle = c.getBarrows().getPuzzle();
			if (Boundary.isIn(c, Barrows.AROUND_CHEST_ROOM)) {
				if (puzzle != null)
					if (!puzzle.isSolved()) {
						//	int random = Misc.random(2);//fk it random click of any door.

						//if (random == 0) {
						puzzle.displayInterface(c);
						return;
						//}
					}
			}
			TunnelDoor door = TunnelDoors.getDoor(obX, obY);

			if (door == null) {
				System.out.println("here");
				return;
			}
			int targetX = obX;
			int targetY = obY;

			if (c.absX == obX) {
				if (door.getRotation() == TunnelDoors.Rotation.EAST) {//e
					targetX = obX - 1;
				} else if (door.getRotation() == TunnelDoors.Rotation.WEST) {//w
					targetX = obX + 1;
				}
			}
			if (c.absY == obY) {
				if (door.getRotation() == TunnelDoors.Rotation.NORTH) {//n
					targetY = obY + 1;
				} else if (door.getRotation() == TunnelDoors.Rotation.SOUTH) {//s
					targetY = obY - 1;
				}
			}

			int deltaX = targetX - c.absX;
			int deltaY = targetY - c.absY;
			//	c.getPA().walkTo(deltaX, deltaY);

			TunnelDoor door_totheright = null;
			TunnelDoor door_totheleft = null;
			TunnelDoor door_tothebottom = null;
			TunnelDoor door_tothetop = null;
			if (door.getRotation() == TunnelDoors.Rotation.SOUTH) {

				door_totheright = TunnelDoors.getDoor(obX + 1, obY);
				door_totheleft = TunnelDoors.getDoor(obX - 1, obY);
				if (door_totheright != null) {
					c.getPA().object(objectType, obX, obY, 0, 0);
					c.getPA().object(objectType + 19, obX + 1, obY, 2, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							//if (finalDoor_totheright != null) {
							c.getPA().object(objectType, obX, obY, 3, 0);
							c.getPA().object(objectType + 19, obX + 1, obY, 3, 0);
							//}
							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
				if (door_totheleft != null) {

					c.getPA().object(objectType, obX, obY, 2, 0);
					c.getPA().object(objectType - 19, obX - 1, obY, 0, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().object(objectType, obX, obY, 3, 0);
							c.getPA().object(objectType - 19, obX - 1, obY, 3, 0);

							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
			}
			if (door.getRotation() == TunnelDoors.Rotation.NORTH) {

				door_totheright = TunnelDoors.getDoor(obX + 1, obY);
				door_totheleft = TunnelDoors.getDoor(obX - 1, obY);
				if (door_totheright != null) {

					c.getPA().object(objectType, obX, obY, 0, 0);
					c.getPA().object(objectType - 19, obX + 1, obY, 2, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().object(objectType, obX, obY, 1, 0);
							c.getPA().object(objectType - 19, obX + 1, obY, 1, 0);

							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
				if (door_totheleft != null) {

					c.getPA().object(objectType, obX, obY, 2, 0);
					c.getPA().object(objectType + 19, obX - 1, obY, 0, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().object(objectType, obX, obY, 1, 0);
							c.getPA().object(objectType + 19, obX - 1, obY, 1, 0);

							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
			}
			if (door.getRotation() == TunnelDoors.Rotation.WEST) {

				door_tothebottom = TunnelDoors.getDoor(obX, obY - 1);
				door_tothetop = TunnelDoors.getDoor(obX, obY + 1);
				if (door_tothebottom != null) {

					c.getPA().object(objectType, obX, obY, 1, 0);
					c.getPA().object(objectType - 19, obX, obY - 1, 3, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().object(objectType, obX, obY, 2, 0);
							c.getPA().object(objectType - 19, obX, obY - 1, 2, 0);

							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
				if (door_tothetop != null) {

					c.getPA().object(objectType, obX, obY, 3, 0);
					c.getPA().object(objectType + 19, obX, obY + 1, 1, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().object(objectType, obX, obY, 2, 0);
							c.getPA().object(objectType + 19, obX, obY + 1, 2, 0);

							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
			}
			if (door.getRotation() == TunnelDoors.Rotation.EAST) {

				door_tothebottom = TunnelDoors.getDoor(obX, obY - 1);
				door_tothetop = TunnelDoors.getDoor(obX, obY + 1);
				if (door_tothebottom != null) {

					c.getPA().object(objectType, obX, obY, 1, 0);
					c.getPA().object(objectType + 19, obX, obY - 1, 3, 0);
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().object(objectType, obX, obY, 0, 0);
							c.getPA().object(objectType + 19, obX, obY - 1, 0, 0);

							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
				if (door_tothetop != null) {

					c.getPA().object(objectType, obX, obY, 3, 0);
					c.getPA().object(objectType - 19, obX, obY + 1, 1, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().object(objectType, obX, obY, 0, 0);
							c.getPA().object(objectType - 19, obX, obY + 1, 0, 0);

							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
			}
			c.getAgilityHandler().move(c, deltaX, deltaY, 0x333, -1);
		}
		if(objectType == 24306){
			if(c.absY >=3546){
				if (c.getAgilityHandler().hotSpot(c, 2854, 3546)) {
					c.getAgilityHandler().move(c, 0, -1, 819, -1);
					c.getWarriorsGuild().changedanimatordoor(objectType,obX,obY);
				}
			} else {
				if (c.getAgilityHandler().hotSpot(c, 2854, 3545)) {
					c.getAgilityHandler().move(c, 0, 1, 819, -1);
					c.getWarriorsGuild().changedanimatordoor(objectType,obX,obY);
				}
			}

		}

		if(objectType == 24309 && c.heightLevel == 0){
			if(c.absY >=3546){
				if (c.getAgilityHandler().hotSpot(c, 2855, 3546)) {
					c.getAgilityHandler().move(c, 0, -1, 819, -1);
					c.getWarriorsGuild().changedanimatordoor(objectType,obX,obY);
				}
			} else {
				if (c.getAgilityHandler().hotSpot(c, 2855, 3545)) {
					c.getAgilityHandler().move(c, 0, 1, 819, -1);
					c.getWarriorsGuild().changedanimatordoor(objectType,obX,obY);
				}
			}
		}
		Location3D location = new Location3D(obX, obY, c.heightLevel);

		int face = 0;
		WorldObject worldObject = ClickObject.getObject(c, objectType,obX,obY);
		if (worldObject != null) {
			face = worldObject.getFace();
		}
		switch (objectType) {

			case 44599:

				c.getDH().sendDialogues(1018, 4287);
				break;
			case 23609://look inside

				if (!c.getItems().playerHasItem(ROPE, 1)) {
					c.sendMessage("You need a rope to go down there.");
				} else {
					c.getItems().deleteItem(ROPE, 1);
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3507, 9494, 0, 2);
				}
				break;
			case 20134://task board
				c.getDH().sendDialogues(10000, 11);
				break;

			case 2119://slayer tower staircase

				if (obX == 3434 && obY == 3537 && c.heightLevel == 0)
					c.getPA().movePlayer(3433, 3537, 1);
				else if (obX == 3413 && obY == 3540 && c.heightLevel == 1)
					c.getPA().movePlayer(3417, 3540, 2);
				break;
			case 2120://slayer tower staircase

				if (obX == 3434 && obY == 3537 && c.heightLevel == 1)
					c.getPA().movePlayer(3438, 3537, 0);
				else if (obX == 3415 && obY == 3540 && c.heightLevel == 2)
					c.getPA().movePlayer(3412, 3540, 1);
				break;

			case 14106:
				if (obX == 2887 && obY == 9823) {
					c.getPA().movePlayer(2886, 9823, 0);

				}

				break;

			case 30192://ladder up from slayer tower basement

				c.getPA().movePlayer(3417, 3536, 0);

				break;
			case 30190:

				if (obX == 2881 && obY == 9825) {
					c.getPA().movePlayer(2881, 9825, 0);
				} else if (obX == 2904 && obY == 9813) {
					c.getPA().movePlayer(2906, 9813, 0);
				}
				break;
			case 2108://door into slayer tower
			case 2111:
				Server.getGlobalObjects().add(new GlobalObject(2108, 3429, 3535, 0, 2, 0));
				Server.getGlobalObjects().add(new GlobalObject(2111, 3428, 3535, 0, 0, 0));
				if (c.absY > obY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);


				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						Server.getGlobalObjects().add(new GlobalObject(2111, 3429, 3535, 0, 1, 0));
						Server.getGlobalObjects().add(new GlobalObject(2108, 3428, 3535, 0, 1, 0));


						container.stop();
					}

					@Override
					public void onStopped() {

					}
				}, 3);
				break;
			case 2102://door into slayer tower
			case 2104:
				Server.getGlobalObjects().add(new GlobalObject(2102, 3426, 3555, 1, 0, 0));
				Server.getGlobalObjects().add(new GlobalObject(2104, 3427, 3555, 1, 2, 0));
				if (c.absY > obY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1);


				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						Server.getGlobalObjects().add(new GlobalObject(2102, 3426, 3555, 1, 1, 0));
						Server.getGlobalObjects().add(new GlobalObject(2104, 3427, 3555, 1, 1, 0));


						container.stop();
					}

					@Override
					public void onStopped() {

					}
				}, 3);
				break;
//			case 1560:
//
////TODO: optimize this
//				GlobalObject originalgate = new GlobalObject(1560, obX, obY, 0, 0, 0);
//				GlobalObject totherightgate = new GlobalObject(1558, obX + 1, obY, 0, 3, 0);
//
//				GlobalObject originalgatenew = null;
//
//				GlobalObject totherightgatenew = null;
//
//
//				GlobalObject originalgateclose = new GlobalObject(-1, obX, obY, 0, 0, 0);
//				GlobalObject totherightgateclose = new GlobalObject(-1, obX - 1, obY, 0, 0, 0);
//
//				GlobalObject originalgatenewclose = new GlobalObject(1560, obX - 2, obY - 1, 0, 2, 0);
//				GlobalObject totherightgatenewclose = new GlobalObject(1558, obX - 2, obY, 0, 2, 0);
//
//				Optional<WorldObject> object_real1560 = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType, obX, obY, c.getHeight());
//				GlobalObject theobject1560 = object;
//				if(theobject1560 == null){
//					System.out.println("object 1560 is null");
//					return;
//				}
//				face = theobject1560.getFace();// rightdoor.get().face;
//				if (object_real1560.isPresent()) {
//					System.out.println("face: " + face);
//
//					//return;
//				}
//
//				if (face == 1) {//closing
//
//					theobject1560 = new GlobalObject(object_real1560.get().id, object_real1560.get().x, object_real1560.get().y, object_real1560.get().height, object_real1560.get().face, object_real1560.get().type);
//
//					Server.getGlobalObjects().add_door(theobject1560);
//					//Server.getGlobalObjects().remove(theobject1560);
//					//	Server.getGlobalObjects().updateObject(theobject1560, -1);
//
//					Optional<WorldObject> objclosetoleft = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType - 2, obX - 1, obY, c.getHeight());
//					GlobalObject objclosetoleft1560 = new GlobalObject(objclosetoleft.get().id, objclosetoleft.get().x, objclosetoleft.get().y, objclosetoleft.get().height, objclosetoleft.get().face, objclosetoleft.get().type);
//
//
//					Server.getGlobalObjects().add(objclosetoleft1560);
//					Server.getGlobalObjects().remove(objclosetoleft1560);
//					Server.getGlobalObjects().updateObject(objclosetoleft1560, -1);
//
//
//					//	Server.getGlobalObjects().add(totherightgateclose);
//					Server.getGlobalObjects().add(originalgatenewclose);
//					Server.getGlobalObjects().add(totherightgatenewclose);
//				} else if (face == 2) {//opening
//					originalgatenew = new GlobalObject(1560, obX + 2, obY + 1, 0, 1, 0);
//					totherightgatenew = new GlobalObject(1558, obX + 1, obY + 1, 0, 1, 0);
//					Server.getGlobalObjects().add(originalgatenew);
//					Server.getGlobalObjects().add(totherightgatenew);
//
//					Optional<WorldObject> object_real_totheright1560 = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType - 2, obX, obY + 1, c.getHeight());
//
//					GlobalObject theobject_closing1560 = new GlobalObject(object_real_totheright1560.get().id, object_real_totheright1560.get().x, object_real_totheright1560.get().y, object_real_totheright1560.get().height, object_real_totheright1560.get().face, object_real_totheright1560.get().type);
//
//					Server.getGlobalObjects().add(theobject1560);
//					Server.getGlobalObjects().remove(theobject1560);
//					Server.getGlobalObjects().updateObject(theobject1560, -1);
//
//					Server.getGlobalObjects().add(theobject_closing1560);
//					Server.getGlobalObjects().remove(theobject_closing1560);
//					Server.getGlobalObjects().updateObject(theobject_closing1560, -1);
//				} else if (face == 3) {
//					Optional<WorldObject> the1560gateface3 = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType, obX, obY, c.getHeight());
//
//					GlobalObject theobject_the1560gateface3 = new GlobalObject(the1560gateface3.get().id, the1560gateface3.get().x, the1560gateface3.get().y, the1560gateface3.get().height, the1560gateface3.get().face, the1560gateface3.get().type);
//					Server.getGlobalObjects().add(theobject_the1560gateface3);
//					Server.getGlobalObjects().remove(theobject_the1560gateface3);
//					Server.getGlobalObjects().updateObject(theobject_the1560gateface3, -1);
//
//
//					Optional<WorldObject> the1558gatetotherightof1560 = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType - 2, obX + 1, obY, c.getHeight());
//
//					GlobalObject theobject_the1558gatetotherightof1560 = new GlobalObject(the1558gatetotherightof1560.get().id, the1558gatetotherightof1560.get().x, the1558gatetotherightof1560.get().y, the1558gatetotherightof1560.get().height, the1558gatetotherightof1560.get().face, the1558gatetotherightof1560.get().type);
//					Server.getGlobalObjects().add(theobject_the1558gatetotherightof1560);
//					Server.getGlobalObjects().remove(theobject_the1558gatetotherightof1560);
//					Server.getGlobalObjects().updateObject(theobject_the1558gatetotherightof1560, -1);
//
//
//					GlobalObject thereplacementgatethatis1558 = new GlobalObject(1558, the1560gateface3.get().x, the1560gateface3.get().y, the1560gateface3.get().height, 0, the1560gateface3.get().type);
//					Server.getGlobalObjects().add(thereplacementgatethatis1558);
//					GlobalObject thereplacementgatethatis1560 = new GlobalObject(1560, the1560gateface3.get().x, the1560gateface3.get().y + 1, the1560gateface3.get().height, 0, the1560gateface3.get().type);
//					Server.getGlobalObjects().add(thereplacementgatethatis1560);
//
//				} else if (face == 0) {
//					Optional<WorldObject> the1560gateface0 = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType, obX, obY, c.getHeight());
//					GlobalObject theobject_the1560gateface0 = new GlobalObject(the1560gateface0.get().id, the1560gateface0.get().x, the1560gateface0.get().y, the1560gateface0.get().height, the1560gateface0.get().face, the1560gateface0.get().type);
//					Server.getGlobalObjects().add(theobject_the1560gateface0);
//					Server.getGlobalObjects().remove(theobject_the1560gateface0);
//					Server.getGlobalObjects().updateObject(theobject_the1560gateface0, -1);
//
//					Optional<WorldObject> the1558gatetothebottomof1560 = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType - 2, obX, obY + 1, c.getHeight());
//if(!the1558gatetothebottomof1560.isPresent()){return;}
//					GlobalObject theobject_the1558gatetothebottomof1560 = new GlobalObject(the1558gatetothebottomof1560.get().id, the1558gatetothebottomof1560.get().x, the1558gatetothebottomof1560.get().y, the1558gatetothebottomof1560.get().height, the1558gatetothebottomof1560.get().face, the1558gatetothebottomof1560.get().type);
//					Server.getGlobalObjects().add(theobject_the1558gatetothebottomof1560);
//					Server.getGlobalObjects().remove(theobject_the1558gatetothebottomof1560);
//					Server.getGlobalObjects().updateObject(theobject_the1558gatetothebottomof1560, -1);
//
//
//					GlobalObject thereplacementgatethatis1558 = new GlobalObject(1558, the1560gateface0.get().x , the1560gateface0.get().y + 1, the1560gateface0.get().height, 3, the1560gateface0.get().type);
//					Server.getGlobalObjects().add(thereplacementgatethatis1558);
//					GlobalObject thereplacementgatethatis1560 = new GlobalObject(1560, the1560gateface0.get().x+1, the1560gateface0.get().y + 1, the1560gateface0.get().height, 3, the1560gateface0.get().type);
//					Server.getGlobalObjects().add(thereplacementgatethatis1560);
//
//				}
//				break;
//			case 1558:
//
//				WorldObject gate1558 = ClickObject.getObject(c, objectType, obX, obY);
//				if (gate1558 != null) {
//					System.out.println("f i: " + gate1558.getFace());
//					//return;
//				}
//
//				Optional<WorldObject> object_real = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType, obX, obY, c.getHeight());
//				GlobalObject theobject = new GlobalObject(object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, object_real.get().type);
//
//
//				GlobalObject totherightgate1 = null;
//				//new GlobalObject(1558, obX + 1, obY, 0, 1, 0);
//				GlobalObject totherightgate2 = new GlobalObject(1560, obX + 2, obY, 0, 1, 0);
//
//				//	GlobalObject totheleftgate2 = new GlobalObject(-1, obX+1,obY, 0, 0, 0, 0);
//
//				GlobalObject originalgatenew2 = null;//new GlobalObject(1558, obX-1,obY, 0, 2, 0);
//				GlobalObject totheleftgatenew2 = null;//new GlobalObject(1560, obX-1,obY-1, 0, 2, 0);
//
//
//				if (theobject.getFace() == 1) {
//					Optional<WorldObject> object_real_closing = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType + 2, obX + 1, obY, c.getHeight());
//					GlobalObject theobject_closing = new GlobalObject(object_real_closing.get().id, object_real_closing.get().x, object_real_closing.get().y, object_real_closing.get().height, object_real_closing.get().face, object_real_closing.get().type);
//
//					Server.getGlobalObjects().add_door(theobject);
//					//	Server.getGlobalObjects().remove(theobject);
//					//Server.getGlobalObjects().updateObject(theobject, -1);
//
//
//					Server.getGlobalObjects().add(theobject_closing);
//					Server.getGlobalObjects().remove(theobject_closing);
//					Server.getGlobalObjects().updateObject(theobject_closing, -1);
//					originalgatenew2 = new GlobalObject(1558, obX - 1, obY, 0, 2, 0);
//					Server.getGlobalObjects().add(originalgatenew2);
//					totheleftgatenew2 = new GlobalObject(1560, obX - 1, obY-1, 0, 2, 0);
//					Server.getGlobalObjects().add(totheleftgatenew2);
//				} else {
//					Optional<WorldObject> object_real_belowit = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(objectType + 2, obX, obY - 1, c.getHeight());
//				if(!object_real_belowit.isPresent()){
//					System.out.println("null gate?");
//					return;
//				}
//					GlobalObject theobject_belowit = new GlobalObject(object_real_belowit.get().id, object_real_belowit.get().x, object_real_belowit.get().y, object_real_belowit.get().height, object_real_belowit.get().face, object_real_belowit.get().type);
//
//					Server.getGlobalObjects().add(theobject);
//					Server.getGlobalObjects().remove(theobject);
//					Server.getGlobalObjects().updateObject(theobject, -1);
//					totherightgate1 = new GlobalObject(1558, obX + 1, obY, 0, 1, 0);
//					Server.getGlobalObjects().add(theobject_belowit);
//					Server.getGlobalObjects().remove(theobject_belowit);
//					Server.getGlobalObjects().updateObject(theobject_belowit, -1);
//					Server.getGlobalObjects().add(totherightgate1);
//					Server.getGlobalObjects().add(totherightgate2);
//				}
//
//
//
//				break;
			case 17384://ladder chaos druid tower

				if (obX == 2560 && obY == 3356)
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2562, 9754, 0, 2);
				else if (obX == 2842 && obY == 3424)
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2842, 9823, 0, 2);
				else if (obX == 3116 && obY == 3452)
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3115, 9852, 0, 2);
				break;
			case 3443:
				c.getPA().movePlayer(3425, 3485, 0);
				break;
			case 3432:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3440, 9887, 0, 2);
				break;

			case 2641:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", c.absX, c.absY, 1, 2);
				break;
			case 17385://ladders
				if (Boundary.isIn(c, Boundary.EDGE_DUNG_LADDER)) {
					c.sendMessage("This area is currently closed.");
				} else if (Boundary.isIn(c, Boundary.EDGE_DUNG_ENTRANCE_LADDER)) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3084, 3501, 0, 2);
				} else if (Boundary.isIn(c, Boundary.FOE_DUNGEON)) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3183, 3493, 0, 2);
				} else if (obX == 2884 && obY == 9797) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 2884, 3396, 0, 2);
				} else if (obX == 2842 && obY == 9824) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 2841, 3423, 0, 2);
				} else if (obX == 3405 && obY == 9907) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3405, 3506, 0, 2);
				} else if (obX == 3116 && obY == 9852) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3117, 3452, 0, 2);
				} else if (obX == 3209 && obY == 9616) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3209, 3214, 0, 2);
				} else {//from chaos druid dung
					c.getPA().movePlayer(2563, 3356, 0);
				}

				break;
			case 9742://ladder from warriors guild basement
				if (obX == 2906 && obY == 9968)
					AgilityHandler.delayEmote(c, "CLIMB_UP", 2834, 3542, 0, 2);
				break;
			case 17026://ladder up chaos druid tower
				if (obX == 2560 && obY == 3356)
					c.getPA().movePlayer(2560, 3357, 1);
				break;
			case 16685://ladder down chaos druid tower
				if (obX == 2560 && obY == 3356)
					c.getPA().movePlayer(2560, 3357, 0);
				break;
			/**
			 * Aquiring god capes.
			 */
			case 2873:
				if (c.getItems().getItemCount(2412, false) == 0 && !Server.itemHandler.itemExists(c, 2412, c.getX(), c.getY() + 1, c.heightLevel)) {
					c.startAnimation(645);
					c.sendMessage("Saradomin blesses you with a cape.");

					Server.itemHandler.createGroundItem(c, 2412, c.getX(), c.getY() + 1, c.heightLevel, 1);

				} else {
					c.sendMessage("You already have a cape.");
				}
				break;
			case 2875:
				if (c.getItems().getItemCount(2413, false) == 0 && !Server.itemHandler.itemExists(c, 2413, c.getX(), c.getY() + 1, c.heightLevel)) {
					c.startAnimation(645);
					c.sendMessage("Guthix blesses you with a cape.");
					Server.itemHandler.createGroundItem(c, 2413, c.getX(), c.getY() + 1, c.heightLevel, 1);

				} else {
					c.sendMessage("You already have a cape");
				}
				break;
			case 2874:
				if (c.getItems().getItemCount(2414, false) == 0 && !Server.itemHandler.itemExists(c, 2414, c.getX(), c.getY() + 1, c.heightLevel)) {
					c.startAnimation(645);
					c.sendMessage("Zamorak blesses you with a cape.");

					Server.itemHandler.createGroundItem(c, 2414, 2516, 4720, c.heightLevel, 1);

				} else {
					c.sendMessage("You already have a cape.");
				}
				break;
			case 2879:
				c.getAgilityHandler().move(c, 0, -2, 6132, -1);
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						c.getPA().movePlayer(2542, 4718, 0);
						container.stop();
					}

					@Override
					public void onStopped() {

					}
				}, 3);
				break;
			case 2878://mage bank jumping into cape area
				if (c.maRound < 2) {
					c.sendMessage("@red@You must defeat Kolodion first.");
				} else {
					c.getAgilityHandler().move(c, 0, 2, 6132, -1);
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							c.getPA().movePlayer(2509, 4689, 0);
							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
				}
				break;

			case 23363://exit from revs:
				c.getPA().movePlayer(3126, 3832, 0);
				break;

			case 31559://exit from revs:
				c.getPA().movePlayer(3086, 3656, 0);
				break;

			case 24318://warriors guild door
				if ((c.playerLevel[0] + c.playerLevel[2] >= 130) || c.playerLevel[0] == 99 || c.playerLevel[2] == 99) {
					if (c.getAgilityHandler().hotSpot(c, 2877, 3546)) {

						c.getPA().object(24318, 2877, 3546, 1, 0);
						c.getAgilityHandler().move(c, -1, 0, 819, -1);
						//c.getPA().object(-1, 2877, 3546, 3, 0);
						CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {
//								if (c.disconnected) {
//									container.stop();
//									return;
//								}
								//c.getPA().object(-1, 2876, 3546, 5, 0);
								c.getPA().object(24318, 2877, 3546, 0, 0);

								container.stop();
							}

							@Override
							public void onStopped() {

							}
						}, 3);
					}
					if (c.getAgilityHandler().hotSpot(c, 2876, 3546)) {

						c.getPA().object(24318, 2877, 3546, 1, 0);
						c.getAgilityHandler().move(c, 1, 0, 819, -1);
						//	c.getPA().object(-1, 2877, 3546, 3, 0);
						CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {

								//c.getPA().object(-1, 2876, 3546, 5, 0);
								c.getPA().object(24318, 2877, 3546, 0, 0);

								container.stop();
							}

							@Override
							public void onStopped() {

							}
						}, 3);
					}
				} else {
					c.sendMessage("You need a combined Attack and Strength level of 130, or 99 in");
					c.sendMessage("either of those stats to enter the Warriors guild.");
				}
				break;
			case 31485:


				if (c.absX > 3604)
					c.getPA().movePlayer(3603, 10291, 0);
				else
					c.getPA().movePlayer(3607, 10290, 0);

				break;
			case 30849:
				c.getPA().movePlayer(3633, 10264, 0);
				break;
			case 30847:
				c.getPA().movePlayer(3633, 10260, 0);
				break;
			case 26370:

				c.startAnimation(828);
				if (obX == 2914 && obY == 5300)
					c.getPA().movePlayer(2912, 5300, 2);
				if (obX == 2881 && obY == 5311)
					c.getPA().movePlayer(2916, 3746, 0);
				if (obX == 2920 && obY == 5274)
					c.getPA().movePlayer(2920, 5276, 1);

//				if(location.equals(2914,5300))//from first saradomin rope
//					Chain.bound(null).runFn(1, () -> player.teleport(new Tile(2912, 5300, 2)));
//				else if(obj.tile().equals(2881,5311))
//					Chain.bound(null).runFn(1, () -> player.teleport(new Tile(2916, 3746, 0)));
//				else if(obj.tile().equals(2920,5274))
//					Chain.bound(null).runFn(1, () -> player.teleport(new Tile(2920, 5276, 1)));

				break;
			case 20973:
				c.getBarrows().useChest();
				break;


			case 20667:
			case 20668:
			case 20669:
			case 20670:
			case 20671:
			case 20672:
				c.getBarrows().moveUpStairs(new Position(obX, obY, c.heightLevel));
				break;


			case 20720:
			case 20721:
			case 20722:
			case 20770:
			case 20771:
			case 20772:
				c.getBarrows().spawnBrother(objectType);
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
			case GroupIronmanBank.OBJECT_ID:
				Optional<GroupIronmanGroup> group = GroupIronmanRepository.getGroupForOnline(c);
				group.ifPresentOrElse(it -> it.getBank().open(c),
						() -> c.sendMessage("This chest is only for group ironmen!"));
				break;
			case 29064:
				//LeaderboardInterface.openInterface(c);
				break;
			case 16666:
				c.getPA().movePlayer(3045, 10323, 0);
				break;
			case 16665:
				c.getPA().movePlayer(3044, 3927, 0);
				break;
			case 45135:
				if (c.absX == 3670) {
					c.getPA().movePlayer(3667, 5280, 0);
				} else if (c.absX == 3668) {
					c.getPA().movePlayer(3671, 5280, 0);
				} else if (c.absX == 3923) {
					c.getPA().movePlayer(3920, 5280, 0);
				} else if (c.absX == 3921) {
					c.getPA().movePlayer(3924, 5280, 0);
				} else if (c.absX == 3795) {
					c.getPA().movePlayer(3798, 5280, 0);
				} else if (c.absX == 3797) {
					c.getPA().movePlayer(3794, 5280, 0);
				}
				break;
			case 28686:
				c.objectDistance = 3;
				AgilityHandler.delayEmote(c, "CRAWL", 3808, 9744, 1, 2);
				break;
			case 34514:
				c.objectDistance = 3;
				AgilityHandler.delayEmote(c, "CRAWL", 1311, 3806, 0, 2);
				break;
			case 34359:
				c.objectDistance = 3;
				AgilityHandler.delayEmote(c, "CRAWL", 1312, 10188, 0, 2);
				break;
			case 4874:
			case 11730:
				c.getThieving().steal(Stall.Baker, location, face);
				//	c.objectDistance = 1;
				break;
			case 8929:
				//AgilityHandler.delayEmote(c, "CRAWL", 2394, 10300, 1,  2);
				//c.getDH().sendDialogues(792, 1158);
				if (c.getRights().contains(Right.REGULAR_DONATOR)) {
					c.getDH().sendOption2("Enter", "Dagannoth Kings");
					c.dialogueAction = 8929;
				} else {
					c.getPA().movePlayer(2442, 10147, 0);
				}


				//
				break;
			case 21306:
				c.getPA().movePlayer(2317, 3824, 0);
				break;
			case 21307:
				c.getPA().movePlayer(2317, 3831, 0);
				break;
			case 21308:
				c.getPA().movePlayer(2343, 3828, 0);
				break;

			case 8880:
				if (c.getItems().freeSlots() < 3) {
					c.sendMessage("You need at least three free slots for these tools.");
				} else {
					c.getItems().addItem(1755, 1);
					c.getItems().addItem(1265, 1);
					c.getItems().addItem(1351, 1);
				}
				break;

			case 7674:
				if (c.getItems().freeSlots() < 1) {
					c.sendMessage("You need at least one free slot to pick these berries.");
				} else {
					c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.PICK_POSION_BERRY);
					c.getItems().addItem(6018, 1);
				}
				break;
			case 21309:
				c.getPA().movePlayer(2343, 3821, 0);
				break;
			case 37730:
				c.getPA().movePlayer(3808, 9755, 1);
				c.sendMessage("You retreat from the nightmare");
				break;
			case 14843:
				c.getRooftopCanafis().execute(c, objectType);
				break;
			case 14845:
			case 14848:
			case 14846:
			case 14894:
			case 14847:
			case 14897:
			case 14844:
				c.getRooftopCanafis().execute(c, objectType);
				break;
			case 23555:
			case 23554:
				c.getWildernessAgility().wildernessCourse(c, objectType);
				break;
			case 10060:
			case 10061:
				c.getPA().c.itemAssistant.openUpBank();
				break;
			case 29333:
				if (c.getMode().isIronmanType()) {
					c.sendMessage("@red@You are not permitted to make use of this.");
					return;
				}
				Listing.openPost(c, false);
				break;
			case 20391:
				c.getPA().movePlayer(3284, 2808, 0);
				break;
			case 15477:
				c.sendMessage("The Construction skill is coming Soon.");
				break;
			case 2153:

				c.sendMessage("@bla@[@red@FoE@bla@]@blu@ Remember, any exchanges are @red@final@blu@, items will not be returned.");
				c.sendMessage("@bla@[@red@FoE@bla@] @blu@Click an item in your inventory to offer. Use the green arrow to confirm.");
				c.getItems().sendItemContainer(33403, Lists.newArrayList(new GameItem(4653, 1)));
				c.getPA().sendInterfaceSet(33400, 33404);
				c.getItems().sendInventoryInterface(33405);
				c.getPA().sendFrame126("@gre@" + c.exchangePoints, 33410);
				c.getPA().sendFrame126("@red@0", 33409);


				break;

			case 30018:
				c.getPA().movePlayer(1247, 3559, 0);
				c.setRaidsInstance(null);
				break;
			case 45579:
				c.getPA().movePlayer(1247, 3559, 0);
				c.setRaids3Instance(null);
				break;
			case 31623: //making forocious gloves
				if (c.getItems().playerHasItem(995, 15_000_000) && c.getItems().playerHasItem(22983) && c.getItems().playerHasItem(2347)) {
					c.startAnimation(898);
					c.getItems().deleteItem(22983, 1); //leather
					c.getItems().deleteItem(995, 15_000_000); //coins
					c.getItems().addItem(22981, 1); //ads forocious gloves
					c.sendMessage("@red@You have succesfully created forocious gloves.");
					return;
				}
				c.sendMessage("@red@You need a hammer, Hydra Leather, 15 million coins to do this.");
				break;

			case 43696: //charging cell
				if (c.getItems().playerHasItem(26878, 30) && c.getItems().playerHasItem(26882)) {
					c.startAnimation(883);
					c.getItems().deleteItem(26878, 30); //leather
					c.getItems().deleteItem(26882, 1); //coins
					c.getItems().addItem(26886, 1); //ads forocious gloves
					c.sendMessage("@blu@You charge a cell");
					return;
				}
				c.sendMessage("@red@You need a uncharged cell and guardian fragments");
				break;
			case 43708: //charging cell
				if (c.playerLevel[20] < 77) {
					c.sendMessage("You must have a smithing level of at least 77 to create blood runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(565, 250);
					c.getItems().addItem(7776, 4);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(75, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43704: //charging cell fire
				if (c.playerLevel[20] < 14) {
					c.sendMessage("You must have a smithing level of at least 14 to create fire runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(554, 240);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(15, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43711: //charging cell fire
				if (c.playerLevel[20] < 44) {
					c.sendMessage("You must have a smithing level of at least 44 to create nature runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(561, 245);
					c.getItems().addItem(7776, 2);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(23, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43703: //charging cell earth
				if (c.playerLevel[20] < 9) {
					c.sendMessage("You must have a smithing level of at least 9 to create earth runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(557, 240);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(7, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43702: //charging cell water
				if (c.playerLevel[20] < 5) {
					c.sendMessage("You must have a smithing level of at least 5 to create water runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(555, 240);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(5, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43710: //charging cell fire
				if (c.playerLevel[20] < 27) {
					c.sendMessage("You must have a smithing level of at least 27 to create cosmic runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(564, 245);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(13, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43701: //charging cell air
				if (c.playerLevel[20] < 1) {
					c.sendMessage("You must have a smithing level of at least 1 to create air runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(556, 750);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(2, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43705: //charging cell fire
				if (c.playerLevel[20] < 2) {
					c.sendMessage("You must have a smithing level of at least 2 to create mind runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(558, 600);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(3, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43709: //charging cell body
				if (c.playerLevel[20] < 20) {
					c.sendMessage("You must have a smithing level of at least 20 to create body runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(559, 421);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(8, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43706: //charging cell fire
				if (c.playerLevel[20] < 35) {
					c.sendMessage("You must have a smithing level of at least 35 to create chaos runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(562, 455);
					c.getItems().addItem(7776, 1);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(18, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43707: //charging cell death
				if (c.playerLevel[20] < 65) {
					c.sendMessage("You must have a smithing level of at least 65 to create death runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(560, 255);
					c.getItems().addItem(7776, 3);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(21, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 43712: //charging cell law
				if (c.playerLevel[20] < 54) {
					c.sendMessage("You must have a smithing level of at least 54 to create law runes");
					return;
				}
				if (c.getItems().playerHasItem(26886, 1)) {
					c.startAnimation(8973);
					c.getItems().deleteItem(26886, 1); //leather
					c.getItems().addItem(563, 247);
					c.getItems().addItem(7776, 2);
					c.sendMessage("@blu@You sacrifice your overcharged cell and receive runecrafting experience");
					c.getPA().addSkillXPMultiplied(19, Player.playerRunecrafting, true);
					return;
				}
				c.sendMessage("@red@You need a overcharged cell");
				break;
			case 42966: //making Torva armour with ancient forge
				if (c.getItems().playerHasItem(11834) && c.getItems().playerHasItem(2347)) {
					c.startAnimation(898);
					c.getItems().deleteItem(11834, 1); //tassets
					c.getItems().addItem(26394, 2); //bandos components
					c.sendMessage("@blu@You have succesfully broken down your Bandos armour into components.");
					return;
				}
				if (c.getItems().playerHasItem(11832) && c.getItems().playerHasItem(2347)) {
					c.startAnimation(898);
					c.getItems().deleteItem(11832, 1); //bandos chestplate
					c.getItems().addItem(26394, 3); //bandos components
					c.sendMessage("@blu@You have succesfully broken down your Bandos armour into components.");
					return;
				}

				if (c.getItems().playerHasItem(2347) && c.getItems().playerHasItem(26380) && c.getItems().playerHasItem(26394, 2)) {
					if (c.playerLevel[13] < 95) {
						c.sendMessage("You must have a smithing level of at least 95 to create Torva armour.");
						return;
					}
					c.startAnimation(898);
					c.getItems().deleteItem(26394, 1);
					c.getItems().deleteItem(26394, 1);
					c.getItems().deleteItem(26380, 1);
					c.getItems().addItem(26386, 1); //ads forocious gloves
					c.sendMessage("@blu@You have succesfully created Torva armour.");
					c.getPA().addSkillXPMultiplied(5000, Player.playerSmithing, true);
					return;

				}

				if (c.getItems().playerHasItem(2347) && c.getItems().playerHasItem(26376) && c.getItems().playerHasItem(26394, 1)) {
					if (c.playerLevel[13] < 95) {
						c.sendMessage("You must have a smithing level of at least 95 to create Torva armour.");
						return;
					}
					c.startAnimation(898);
					c.getItems().deleteItem(26376, 1);
					c.getItems().deleteItem(26380, 1);
					c.getItems().addItem(26382, 1); //ads forocious gloves
					c.sendMessage("@blu@You have succesfully created Torva armour.");
					c.getPA().addSkillXPMultiplied(5000, Player.playerSmithing, true);
					return;


				}

				if (c.getItems().playerHasItem(2347) && c.getItems().playerHasItem(26378) && c.getItems().playerHasItem(26394, 3)) {
					if (c.playerLevel[13] < 95) {
						c.sendMessage("You must have a smithing level of at least 95 to create Torva armour.");
						return;
					}
					c.startAnimation(898);
					c.getItems().deleteItem(26394, 1);
					c.getItems().deleteItem(26394, 1);
					c.getItems().deleteItem(26394, 1);
					c.getItems().deleteItem(26378, 1);
					c.getItems().addItem(26384, 1); //adds torva
					c.sendMessage("@blu@You have succesfully created Torva armour.");
					c.getPA().addSkillXPMultiplied(5000, Player.playerSmithing, true);
					return;
				}
				c.sendMessage("@red@You need a hammer, broken Torva, and Bandosian components to do this.");
				break;
			case 23955:
				AnimatedArmour.itemOnAnimator(c, AnimatedArmour.clickanimator(c));
				break;
//			case 30028:
//				if (c.getItems().freeSlots() < 3) {
//					c.getDH().sendStatement("@red@You need at least 3 free slots for safety");
//					return;
//				}
//				if (c.getItems().playerHasItem(Raids.COMMON_KEY, 1)) {
//					new RaidsChestCommon().roll(c);
//					return;
//				}
//				if (c.getItems().playerHasItem(Raids.RARE_KEY, 1)) {
//					new RaidsChestRare().roll(c);
//					return;
//				}
//				c.getDH().sendStatement("@red@You need either a rare or common key.");
//				break;
			case 35989:
				c.objectDistance = 13;
				if (!(System.currentTimeMillis() - c.chestDelay > 2000)) {
					c.getDH().sendStatement("Please wait before doing this again.");
					return;
				}

				if (c.getItems().freeSlots() < 3) {
					c.getDH().sendStatement("@red@You need at least 3 free slots for safety");
					return;
				}
				if (c.getItems().playerHasItem(23776, 1)) {
					new HunllefChest().roll(c);
					c.chestDelay = System.currentTimeMillis();
					return;
				}
				c.getDH().sendStatement("@red@You need Hunllef's key to unlock this chest.");
				break;
			case 12202:
				if (!c.getItems().playerHasItem(952)) {
					c.sendMessage("You need a spade to dig the whole.");
					return;
				}
				c.getPA().movePlayer(1761, 5186, 0);
				c.sendMessage("You digged a whole and landed underground.");
				break;

			case 3840:
				if (Boundary.isIn(c, Boundary.FALADOR_BOUNDARY)) {
					if (c.getItems().playerHasItem(1925)) {
						int amount = c.getItems().getItemAmount(1925);
						c.getItems().deleteItem2(1925, amount);
						c.getItems().addItem(6032, amount);
						c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.COMPOST_BUCKET, true, amount);
					}

				}
				break;

			case 190:
				c.canEnterAvatarOfCreation = true;
				c.objectDistance = 3;
				PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.AvatarOfCreation))
						.forEach(players -> {
							Player p = PlayerHandler.getPlayerByLoginName(players.getLoginName());
							if (p != null && !players.getLoginName().equalsIgnoreCase(c.getLoginName())) {
								if (p.getMacAddress().equalsIgnoreCase(c.getMacAddress())) {
									c.canEnterAvatarOfCreation = false;
								}
							}
						});
				if (!c.canEnterAvatarOfCreation) {
					c.sendMessage("You already have an active account inside AvatarOfCreation.");
					c.canEnterAvatarOfCreation = true;
					return;
				}
				if (Boundary.isIn(c, Boundary.AvatarOfCreation_ENTRANCE)) {
					if (c.playerLevel[19] < 50 || c.playerLevel[8] < 50 || c.playerLevel[14] < 50
							|| c.playerLevel[20] < 50 || c.playerLevel[12] < 50) {
						c.sendMessage("You need a level of 50 in Farming, Crafting, Firemaking, Runecrafting, Mining,");
						c.sendMessage("& Woodcutting to participate in this event. Use @red@::AvatarOfCreation @bla@to open the guide.");
						return;
					}
					if (AvatarOfCreationSpawner.isSpawned()) {
						c.canLeaveAvatarOfCreation = false;
						if (c.getRights().isOrInherits(Right.HC_IRONMAN)) {
							c.sendMessage("@bla@[@red@HC WARNING@bla] This area is not safe for HCs and teleports are not allowed!");
							c.sendMessage("@bla@[@red@HC WARNING@bla] You have been warned.");
						}
						c.sendMessage("@red@Gather tools from the crate box if you are ever missing any!");
						boolean axe = c.getItems().hasItemOnOrInventory(WeaponDataConstants.AXES);
						boolean pickaxe = c.getItems().hasItemOnOrInventory(WeaponDataConstants.PICKAXES);
						boolean chisel = c.getItems().playerHasItem(Items.CHISEL);

						if (!axe) {
							c.getItems().addItem(Items.BRONZE_AXE, 1);
						}
						if (!pickaxe) {
							c.getItems().addItem(Items.BRONZE_PICKAXE, 1);
						}
						if (!chisel) {
							c.getItems().addItem(Items.CHISEL, 1);
						}

						c.getPA().movePlayer(3067, 3499);
						return;
					} else {
						c.sendMessage("The AvatarOfCreation World Event is currently not active.");
						return;
					}
				} else if (Boundary.isIn(c, Boundary.AvatarOfCreation_EXIT)) {
					if (c.getItems().playerHasItem(9698) || c.getItems().playerHasItem(9699)
							|| c.getItems().playerHasItem(23778) || c.getItems().playerHasItem(23783)
							|| c.getItems().playerHasItem(9017)) {
						c.getItems().deleteItem2(9698, 28);
						c.getItems().deleteItem2(9699, 28);
						c.getItems().deleteItem2(23778, 28);
						c.getItems().deleteItem2(923783, 28);
						c.getItems().deleteItem2(9017, 28);
					}
					c.getPA().movePlayer(3070, 3499);
					return;
				}
				break;
			case 1967:
			case 1968:
				if (c.absY == 3493) {
					c.getPA().movePlayer(2466, 3491, 0);
				} else if (c.absY == 3491) {
					c.getPA().movePlayer(2466, 3493, 0);
				}
				break;

			case 1733:
				if (c.absY > 3920 && c.getPosition().inWild()){
					c.getPA().movePlayer(3045, 10323, 0);
				return;
		}
				if(c.playerLevel[6] <60){
					c.sendMessage("You need a magic level of 60.");
					return;
				}
				if(c.absX>= obX){
					c.getPA().movePlayer(c.absX-1, c.absY, 0);
				} else {
					c.getPA().movePlayer(c.absX+1, c.absY, 0);
				}

				break;
			case 1732:
				if(c.playerLevel[6] <60){
					c.sendMessage("You need a magic level of 60.");
					return;
				}
				if (c.absY == 3493) {
					c.getPA().movePlayer(2466, 3491, 0);
				} else if (c.absY == 3491) {
					c.getPA().movePlayer(2466, 3493, 0);
				}
				break;
			case 2884:
			case 16684:
			case 16683:
				if (c.absY == 3494 || c.absY == 3495 || c.absY == 3496|| c.absY == 2970) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", c.getX(), c.getY(), c.getHeight() + 1, 2);
				}
				break;
			case 16679:
				if (c.absY == 3494 || c.absY == 3495 || c.absY == 3496 || c.absY == 2971) {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", c.getX(), c.getY(), c.getHeight() - 1, 2);
				}
				//			case 16679:
				//				AgilityHandler.delayEmote(c, "CLIMB_DOWN", c.absX,c.absY,0, 2);
				//				break;
				break;
			case 7257:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3061,4985,1, 2);
				break;
			case 7259:
				break;
			case 36197:
				c.getPA().startTeleport(3077, 3485, 0, "modern", true);
				break;
			case 34727:
				c.getPA().startTeleport(3077, 3485, 0, "modern", true);
				break;
			case 21600:
				if (c.absY == 3802) {
					c.getPA().movePlayer(2326, 3801, 0);
				} else if (c.absY == 3801) {
					c.getPA().movePlayer(2326, 3802, 0);
				}
				break;
			case 31990:
				if (c.absY == 4054) {
					Vorkath.exit(c);
				} else if (c.absY == 4052) {
					Vorkath.enterInstance(c, 10);
				}
				break;
			case 34548://climbing over something.
				c.getPA().walkTo2(obX, obY - 3);
				c.facePosition(obX, obY);
				AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 3);
				c.startAnimation(3067);
				break;
			/*
			 * Cheers, ye boi Tutus <3
			 */
			case 34553:
			case 34554:
//				if (!c.getSlayer().getTask().isPresent()) {
//					c.sendMessage("You must have an active Hydra task to enter this cave...");
//					return;
//				}
//				if (!c.getSlayer().getTask().get().getPrimaryName().equals("hydra")
//						&& !c.getSlayer().getTask().get().getPrimaryName().equals("alchemical hydra")) {
//					c.sendMessage("You must have an active Hydra task to enter this cave...");
//					return;
//				} else {
								if(c.absX <=1355){
									new AlchemicalHydra(c);

				} else{
					c.getPA().movePlayerUnconditionally(1355, c.getY(), 0);
				}


			//	}
				break;
			case 15645:
				if(obX == 2590 && obY == 3089)
				c.getPA().movePlayer(2591,3092,1);
				if(obX == 2590 && obY == 3084)
					c.getPA().movePlayer(2590,3087,2);
				break;
			case 15648:
				if(c.heightLevel == 1)
					c.getPA().movePlayer(2591,3088,0);
				if(c.heightLevel == 2)
					c.getPA().movePlayer(2591,3083,1);
				break;
			/*
			 * End Tutus
			 */
			case 31561:
				if (c.absY <= obY - 2) {
					if (c.playerLevel[Skill.AGILITY.getId()] < 89) {
						c.sendMessage("You need 89 Agility to do this.");
						return;
					}
					c.getPA().walkTo2(obX, obY - 2);
					c.facePosition(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY + 2, 0, 4);
				} else if (c.absY >= obY + 2) {
					c.getPA().walkTo2(obX, obY + 2);
					c.facePosition(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY - 2, 0, 4);
					//east jump west
				} else if (c.absX >= obX + 2) {
					if (c.playerLevel[Skill.AGILITY.getId()] < 65) {
						c.sendMessage("You need 65 Agility to do this.");
						return;
					}
					c.getPA().walkTo2(obX, obX + 2);
					c.facePosition(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX - 2, obY, 0, 4);
					//west jump east
				} else if (c.absX <= obX - 2) {
					c.getPA().walkTo2(obX, obX - 2);
					c.facePosition(obX, obY);
					AgilityHandler.delayEmote(c, "JUMP", obX, obY, 0, 2);
					c.startAnimation(3067);
					AgilityHandler.delayEmote(c, "JUMP", obX + 2, obY, 0, 4);
				}
				break;
			case 23271:
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.absY == 3520) {
							WildernessDitch.wildernessDitchEnter(c);
							container.stop();
						} else if (c.absY == 3523) {
							WildernessDitch.wildernessDitchLeave(c);
							container.stop();
						}
					}

					@Override
					public void onStopped() {
					}
				}, 1);
				break;

			case 6282:
				c.sendMessage("@red@This Portal isn't Available for now!");
				break;
//			case 30169:
//				//if (Boundary.isIn(c, Boundary.TAVELRY)) {
//					AgilityHandler.delayEmote(c, "CLIMB_UP", 2884, 9798, 0, 2);
//				c.getPA().movePlayer(2884, 9798, 0);
//				//}
//
//				//	c.getPA().movePlayer(2884, 9798, 0);
//				break;
			case 16680:
		 if (Boundary.isIn(c, Boundary.TAVELRY)) {
			AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2884, 9798, 0, 2);
		}

			//	c.getPA().movePlayer(2884, 9798, 0);
				break;



			case 31858:
			case 29150:
//				int spellBook = c.playerMagicBook == 0 ? 1 : (c.playerMagicBook == 1 ? 2 : 0);
//				int interfaceId = c.playerMagicBook == 0 ? 838 : (c.playerMagicBook == 1 ? 29999 : 938);
//				String type = c.playerMagicBook == 0 ? "ancient" : (c.playerMagicBook == 1 ? "lunar" : "normal");
//
//				c.sendMessage("You switch spellbook to " + type + " magic.");
//				c.setSidebarInterface(6, interfaceId);
//				c.playerMagicBook = spellBook;
//				c.autocasting = false;
//				c.autocastId = -1;
//				c.getPA().resetAutocast();
				c.sendMessage("You switch to the lunar spellbook.");
				c.setSidebarInterface(6, 938);
				c.playerMagicBook = 0;
				break;


			case 6150:
			case 2097:
				if (c.getItems().playerHasItem(barType[0])) {
					c.getSmithingInt().showSmithInterface(barType[0]);
				} else if (c.getItems().playerHasItem(barType[1])) {
					c.getSmithingInt().showSmithInterface(barType[1]);
				} else if (c.getItems().playerHasItem(barType[2])) {
					c.getSmithingInt().showSmithInterface(barType[2]);
				} else if (c.getItems().playerHasItem(barType[3])) {
					c.getSmithingInt().showSmithInterface(barType[3]);
				} else if (c.getItems().playerHasItem(barType[4])) {
					c.getSmithingInt().showSmithInterface(barType[4]);
				} else if (c.getItems().playerHasItem(barType[5])) {
					c.getSmithingInt().showSmithInterface(barType[5]);
				} else {
					c.sendMessage("You don't have any bars.");
				}
				break;
			case 11846:
				if (c.combatLevel >= 100) {
					if (c.getY() > 5175) {
						Highpkarena.addPlayer(c);
					} else {
						Highpkarena.removePlayer(c, false);
					}
				} else if (c.combatLevel >= 80) {
					if (c.getY() > 5175) {
						Lowpkarena.addPlayer(c);
					} else {
						Lowpkarena.removePlayer(c, false);
					}
				} else {
					c.sendMessage("You must be at least level 80 to compete in events.");
				}
				break;
			case 2996:
				new VoteChest().roll(c);
				break;
			case 34660:
			case 34662:
				if (c.getItems().playerHasItem(23083, 1)) {
					//c.objectDistance = 3;
					new KonarChest().roll(c);
					return;
				} else {
					c.getDH().sendStatement("You need a @blu@Brimstone key@bla@ to open this.");				}
				break;
			case 34544: //Karuulm Rocks/Stone Bars (Intro)
				if (c.absX == 1320 && c.absY == 10205 || c.absX == 1320 && c.absY == 10206) {
					AgilityHandler.delayEmote(c, "JUMP", 1322, c.absY, 0, 2);
				} else if (c.absX == 1322 && c.absY == 10205 || c.absX == 1322 && c.absY == 10206) {
					AgilityHandler.delayEmote(c, "JUMP", 1320, c.absY, 0, 2);

				} if (c.absX == 1311 && c.absY == 10214 || c.absX == 1312 && c.absY == 10214) {
				AgilityHandler.delayEmote(c, "JUMP", 1311, 10216, 0, 2);
			} else if (c.absX == 1311 && c.absY == 10216 || c.absX == 1312 && c.absY == 10216) {
				AgilityHandler.delayEmote(c, "JUMP", 1312, 10214, 0, 2);

			} if (c.absX == 1303 && c.absY == 10206 || c.absX == 1303 && c.absY == 10205) {
				AgilityHandler.delayEmote(c, "JUMP", 1301, 10205, 0, 2);
			} else if (c.absX == 1301 && c.absY == 10206 || c.absX == 1301 && c.absY == 10205) {
				AgilityHandler.delayEmote(c, "JUMP", 1303, 10206, 0, 2);
			}
				break;
			case 34530:
				c.getPA().movePlayer(1334, 10205, 1);
				break;
			case 34531:
				c.getPA().movePlayer(1329, 10206, 0);
				break;
			case 12768:
				c.objectDistance = 3;
				c.sendMessage("@blu@Use @red@::Lt @blu@to see possible rewards!");
				if (c.getItems().freeSlots() < 3) {
					c.getDH().sendStatement("@red@You need at least 3 free slot to open this.");
					return;
				}
				if (c.getItems().playerHasItem(AvatarOfCreation.KEY, 1)) {
					new AVATAR_OF_CREATION().roll(c);
					c.getEventCalendar().progress(EventChallenge.OPEN_X_AvatarOfCreation_CHESTS);
					return;
				}
				c.getDH().sendStatement("@red@You need a AvatarOfCreation key to open this.");
				break;
			case 11845:
				if (c.combatLevel >= 100) {
					if (c.getY() < 5169) {
						Highpkarena.removePlayer(c, false);
					}
				} else if (c.combatLevel >= 80) {
					if (c.getY() < 5169) {
						Lowpkarena.removePlayer(c, false);
					}
				} else {
					c.sendMessage("You must be at least level 80 to compete in events.");
				}

				break;

			case 10068:
				if (c.getZulrahEvent().isStarting()) {
					c.sendMessage("Your Zulrah instance is about to start.");
				} else if (c.getZulrahEvent().isActive()) {
					c.getDH().sendStatement("It seems that a zulrah instance for you is already created.", "If you think this is wrong then please re-log.");
					c.nextChat = -1;
				} else {
					c.getZulrahEvent().initialize();
				}
				break;
			case 12941:
				PlayerAssistant.refreshSpecialAndHealth(c);
				break;
			case 31556:
				//rev entrance
				//check if they paid 100k fee
				if(!c.paidrevfee){
					c.getPA().movePlayer(3241, 10233);
				} else {
					c.getDH().sendDialogues(31556, -1);
				}

				break;
			case 31557:

				c.getPA().movePlayer(3075, 3653, 0);
				break;
			case 31555:

				if(!c.paidrevfee){
					c.getPA().movePlayer(3197, 10056, 0);
				} else {
					c.getDH().sendDialogues(31556, -1);
				}



				break;
			case 40386:

				if(!c.paidrevfee){
					c.getPA().movePlayer(3067, 10127, 0);
				} else {
					c.getDH().sendDialogues(31556, -1);
				}



				break;
			case 31558:
				c.getPA().movePlayer(3102,3654);
				break;
			case 31624:

				for (int skill = 0; skill < c.playerLevel.length; skill++) {
					if (skill == 3)
						continue;
					if (c.playerLevel[skill] < c.getLevelForXP(c.playerXP[skill])) {
						c.playerLevel[skill] += 8 + (c.getLevelForXP(c.playerXP[skill]));
						if (SkillcapePerks.PRAYER.isWearing(c) || SkillcapePerks.isWearingMaxCape(c))
							c.playerLevel[skill] += 5;
						if (c.playerLevel[skill] > c.getLevelForXP(c.playerXP[skill])) {
							c.playerLevel[skill] = c.getLevelForXP(c.playerXP[skill]);
						}
						if (Boundary.isIn(c, Boundary.DEMONIC_RUINS_BOUNDARY)) {
							c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.DEMONIC_RUINS);
						}
						c.getPA().refreshSkill(skill);
						c.getPA().setSkillLevel(skill, c.playerLevel[skill], c.playerXP[skill]);
					}
				}
				c.lastHealChest = System.currentTimeMillis();
				c.getPotions().doAllDivine();
				c.healEverything();
				c.getDH().sendItemStatement("Restored your HP, Prayer, Run Energy, Spec, and Divine Boosts!", 23685);
				c.nextChat =  -1;

				break;
			case 23709:

				//time = 20_000L;
//				if (System.currentTimeMillis() - c.lastHealChest < c.healchesttime) {
//					if (c.amDonated >= 1000) {
//						c.sendMessage("Your rank may only use this chest every 30 seconds.");
//					} else if (c.amDonated >= 250) {
//						c.sendMessage("Your rank may only use this chest every 1 minute.");
//					} else if (c.amDonated >= 300) {
//						c.sendMessage("Your rank may only use this chest every 1 minute and 30 seconds.");
//					} else if (c.amDonated >= 100) {
//						c.sendMessage("Your rank may only use this chest every 2 minutes.");
//					} else {
//						c.sendMessage("You may only use this chest every 3 minutes.");
//					}
//					return;
//				}



			//	c.lastHealChest = System.currentTimeMillis();
				c.getPA().sendSound(2674);
				c.healEverything();
				c.startAnimation(645);
			//	c.sendMessage("@blu@");
				//c.nextChat =  -1;


//
//				if (c.amDonated >= 1000) {
//					c.healchesttime = 30_000;
//				} else if (c.amDonated >= 250) {
//					c.healchesttime = 60_000;
//				} else if (c.amDonated >= 300) {
//					c.healchesttime = 90_000;
//				} else if (c.amDonated >= 100) {
//					c.healchesttime = 120_000;
//				} else {
//					c.healchesttime = 180_000;
//				}
				break;
			case 7811:
				if (!c.getPosition().inClanWarsSafe()) {
					return;
				}
				c.getShops().openShop(116);
				break;
			case 1206:
				if (AvatarOfCreation.ENOUGH_BURNED) {
					c.sendMessage("Enough essence has already been burned!");
					return;
				}
				c.facePosition(obX, obY);
				if (c.getLevelForXP(c.playerXP[19]) < 50) {
					c.sendMessage("You need a Farming level of 50 to pick these.");
					return;
				}
				if (c.getItems().freeSlots() < 1) {
					c.sendMessage("You have ran out of inventory space.");
					return;
				}
				c.startAnimation(827);
				c.getItems().addItem(9017, 1);


					c.getPA().addSkillXP(10, 19, true);

				break;


			case 4150:
				c.getPA().spellTeleport(2855, 3543, 0, false);
				break;
			case 23115:// from bobs
				c.getPA().spellTeleport(1644, 3673, 0, false);
				break;
			case 10251:
				c.getPA().spellTeleport(2525, 4776, 0, false);
				break;
			case 26756:
				break;

			case 27057:
				Overseer.handleBludgeon(c);
				break;

			case 14918:
				if (!c.getDiaryManager().getWildernessDiary().hasDoneAll()) {
					c.sendMessage("You must have completed the whole wilderness diary to use this shortcut.");
					return;
				}

				if (c.absY > 3808) {
					AgilityHandler.delayEmote(c, "JUMP", 3201, 3807, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "JUMP", 3201, 3810, 0, 2);
				}
				break;

			case 29728:
				if (c.absY > 3508) {
					AgilityHandler.delayEmote(c, "JUMP", 1722, 3507, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "JUMP", 1722, 3512, 0, 2);
				}
				break;

			case 28893:
				if (c.playerLevel[16] < 54) {
					c.sendMessage("You need an Agility level of 54 to pass this.");
					return;
				}
				if (c.absY > 10064) {
					AgilityHandler.delayEmote(c, "JUMP", 1610, 10062, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "JUMP", 1613, 10069, 0, 2);
				}
				break;

			case 27987: // scorpia
				if (c.absX == 1774) {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1769, 3849, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 1774, 3849, 0, 2);
				}
				break;

			case 27988: // scorpia
				if (c.absX == 1774) {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1769, 3849, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 1774, 3849, 0, 2);
				}
				break;

			case 27985:
				if (c.absY > 3872) {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1761, 3871, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 1761, 3874, 0, 2);
				}
				break;

			case 27984:
				if (c.absY > 3872) {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1761, 3871, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 1761, 3874, 0, 2);
				}
				break;

			case 29730:
				if (c.absX > 1604) {
					AgilityHandler.delayEmote(c, "JUMP", 1603, 3571, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "JUMP", 1607, 3571, 0, 2);
				}
				break;

			case 25014:
				if (Boundary.isIn(c, Boundary.PURO_PURO)) {
					c.getPA().startTeleport(2525, 2916, 0, "puropuro", false);
				} else {
					c.getPA().startTeleport(2592, 4321, 0, "puropuro", false);
				}
				break;

			case 4154:// lizexit
				c.getPA().movePlayer(1465, 3687, 0);
				break;
			case 4311:// Mining Guild Entrance
				if (c.absY == 3697) {
					c.getPA().movePlayer(2681, 3698, 0);
				} else if (c.absY == 3698) {
					c.getPA().movePlayer(2682, 3697, 0);
				}
				break;
			case 30366:// Mining Guild Entrance
				if (c.absX == 3043 && c.absY == 9730) {
					if (c.playerLevel[Player.playerMining] >= 60) {
						c.getPA().movePlayer(3043, 9729, 0);
					} else {
						c.sendMessage("You must have a mining level of 60 to enter.");
					}
				} else if (c.absX == 3043 && c.absY == 9729) {
					c.getPA().movePlayer(3043, 9730, 0);
				}
				break;

			case 30365:// Mining Guild Entrance
				if (c.absX == 3019 && c.absY == 9733) {
					if (c.playerLevel[Player.playerMining] >= 60) {
						c.getPA().movePlayer(3019, 9732, 0);
					} else {
						c.sendMessage("You must have a mining level of 60 to enter.");
					}
				} else if (c.absX == 3019 && c.absY == 9732) {
					c.getPA().movePlayer(3019, 9733, 0);
				}
				break;

			case 8356:
				c.getDH().sendDialogues(55874, 2200);
				break;
			/*
			 * draynor manor doors
			 */
			case 134:
				c.getPA().movePlayer(3108, 3354, 0);
				break;
			case 135:
				c.getPA().movePlayer(3109, 3354, 0);
				break;
			case 21597:
			case 21598:
			case 21599:
				c.getPA().movePlayer(2834, 5075, 0);
				break;

			case 11470:
				if (c.absY == 3357) {
					c.getPA().movePlayer(3109, 3358, 0);
				} else if (c.absY == 3368) {
					c.getPA().movePlayer(3106, 3369, 0);
				} else if (c.absY == 3364) {
					c.getPA().movePlayer(3103, 3363, 0);
				}
				break;
			case 21505:
			case 21507:
				if (c.absX == 2328) {
					c.getPA().movePlayer(2329, 3804, 0);
				} else if (c.absX == 2329) {
					c.getPA().movePlayer(2328, 3804, 0);
				}
				break;
			case 36556:
				if (Boundary.isIn(c, Boundary.ONYX_ZONE)) {
					if ((!c.getSlayer().getTask().isPresent() || !c.getSlayer().getTask().get().getPrimaryName().contains("crystalline")) && !c.getItems().playerHasItem(23951)) {
						c.sendMessage("@red@You must have a crystalline task to go in this cave.");
						c.getPA().closeAllWindows();
						return;
					}
					c.getPA().movePlayer(3225, 12445, 12);
				} else {
					c.start(new CrystalCaveEntryDialogue(c));
				}
				break;
			case 36691:
				c.objectDistance = 3;
				c.getPA().movePlayer(3271, 6051, 0);
				break;
			case 36692:
				c.objectDistance = 3;
				c.getPA().movePlayer(3216, 12441, c.getPosition().getHeight());
				break;
			case 36693:
				c.objectDistance = 3;
				c.getPA().movePlayer(3222, 12441, c.getPosition().getHeight());
				break;
			case 36694:
				c.objectDistance = 3;
				c.getPA().movePlayer(3232, 12420, c.getPosition().getHeight());
				break;
			case 36695:
				c.objectDistance = 3;
				c.getPA().movePlayer(3242, 12420, c.getPosition().getHeight());
				break;

			case 1549:
				if (!c.getItems().playerHasItem(1590)) {
					c.sendMessage("You need a @blu@Dusty key@bla@ to open this door.");
					return;
				}
				if(obX == 2892 && obY == 9826){ //dusty key gate
					int ytogoto = 0;
					int destinationfinal = 0;
					if(c.absY > 9825){
						ytogoto = 9826;
						destinationfinal = 9825;
					} else {
						ytogoto = 9895;
						destinationfinal = 9826;
					}

					PathFinder.getPathFinder().findRouteNoclip(c, c.absX, ytogoto, true, 0, 0);
					c.setNewWalkCmdIsRunning(false);
					int finalYtogoto = ytogoto;
					int finalDestinationfinal = destinationfinal;
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (c.getAgilityHandler().hotSpot(c, c.absX,finalYtogoto)) {
								c.getPA().object(1551, obX, obY, 0, 0);
								c.getPA().object(1549, obX+1, obY, 2, 0);
								PathFinder.getPathFinder().findRouteNoclip(c, c.absX ,finalDestinationfinal, true, 0, 0);
								c.setNewWalkCmdIsRunning(false);
								CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer container) {

										c.getPA().object(1549, obX, obY, 3, 0);
										c.getPA().object(1551, obX+1, obY, 3, 0);
										container.stop();


									}

								}, 3);
								container.stop();
							}

						}

					}, 1);
					return;
				}
				break;
			case 1551:
				if (!c.getItems().playerHasItem(1590)) {
					c.sendMessage("You need a @blu@Dusty key@bla@ to open this door.");
					return;
				}

				if(obX == 2893 && obY == 9826){ //dusty key gate
					int xtogoto = 0;
					int destinationfinal = 0;
					if(c.absY > 9825){
						xtogoto = 9826;
						destinationfinal = 9825;
					} else {
						xtogoto = 9825;
						destinationfinal = 9826;
					}
					int yl =2893;
					PathFinder.getPathFinder().findRouteNoclip(c, yl, xtogoto, true, 0, 0);
					int finalXtogoto = xtogoto;
					int finalDestinationfinal = destinationfinal;
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (c.getAgilityHandler().hotSpot(c, yl,finalXtogoto)) {
								c.getPA().object(1549, obX, obY, 2, 0);
								c.getPA().object(1551, obX-1, obY, 0, 0);
								PathFinder.getPathFinder().findRouteNoclip(c, yl ,finalDestinationfinal, true, 0, 0);
								c.setNewWalkCmdIsRunning(false);
								CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer container) {

										c.getPA().object(1551, obX, obY, 3, 0);
										c.getPA().object(1549, obX-1, obY, 3, 0);
										container.stop();


									}

								}, 3);
								container.stop();
							}

						}

					}, 1);
					return;
				}
				break;
//			case 1568:
//				if(obX == 2898 && obY == 9831){ //dusty key gate
//					if (!c.getItems().playerHasItem(1590)) {
//						c.sendMessage("You need a @blu@Dusty key@bla@ to open this door.");
//						return;
//					}
//					int xtogoto = 0;
//					int destinationfinal = 0;
//					if(c.absX > 2897){
//						xtogoto = 2898;
//						destinationfinal = 2897;
//					} else {
//						xtogoto = 2897;
//						destinationfinal = 2898;
//					}
//					int yl =9831;
//					PathFinder.getPathFinder().findRouteNoclip(c, xtogoto, yl, true, 0, 0);
//					int finalXtogoto = xtogoto;
//					int finalDestinationfinal = destinationfinal;
//					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//						@Override
//						public void execute(CycleEventContainer container) {
//							if (c.getAgilityHandler().hotSpot(c, finalXtogoto, yl)) {
//								c.getPA().object(1569, obX, obY, 3, 0);
//								c.getPA().object(1568, obX, obY+1, 1, 0);
//								PathFinder.getPathFinder().findRouteNoclip(c, finalDestinationfinal ,yl, true, 0, 0);
//								c.setNewWalkCmdIsRunning(false);
//								CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//									@Override
//									public void execute(CycleEventContainer container) {
//										//if (c.getAgilityHandler().hotSpot(c, finalXtogoto, 3451)) {
//										c.getPA().object(1568, obX, obY, 0, 0);
//										c.getPA().object(1569, obX, obY+1, 0, 0);
//										container.stop();
//
//										//	}
//
//									}
//
//									@Override
//									public void onStopped() {
//
//									}
//								}, 3);
//								container.stop();
//							}
//
//						}
//
//						@Override
//						public void onStopped() {
//
//						}
//					}, 1);
//					return;
//				}
//				WorldObject gate1568= ClickObject.getObject(c, objectType,obX,obY);
//				if (object != null) {
//					//System.out.println("f: "+gate1568.getFace());
//				}
//
//				//what was originally there
//				originalgate = new GlobalObject(1568, obX,obY, 0, 2, 0, 0);
//				GlobalObject tobottomgate = new GlobalObject(1569, obX,obY-1, 0, 2, 0, 0);
//
//				//closed originally
//				GlobalObject gate_open = new GlobalObject(1569, obX,obY, 0, 1, 0, 0);
//				GlobalObject tobottomgate_open = new GlobalObject(1568, obX,obY-1, 0, 3, 0, 0);
//
//
//
//
//				face = gate1568.getFace();
//				if(face == 2){//closing
//
//					Server.getGlobalObjects().add(gate_open);
//					Server.getGlobalObjects().add(tobottomgate_open);
//					if(c.absX> obX)
//						c.getPA().walkTo(-1,0);
//					else
//						c.getPA().walkTo(1,0);
//				}
//
//
//				break;
//			case 1569:
//				if(obX == 2898 && obY == 9832){ //dusty key gate
//					if (!c.getItems().playerHasItem(1590)) {
//						c.sendMessage("You need a @blu@Dusty key@bla@ to open this door.");
//						return;
//					}
//
//					int xtogoto = 0;
//					int destinationfinal = 0;
//					if(c.absX > 2897){
//						xtogoto = 2898;
//						destinationfinal = 2897;
//					} else {
//						xtogoto = 2897;
//						destinationfinal = 2898;
//					}
//					int yl =9832;
//					PathFinder.getPathFinder().findRouteNoclip(c, xtogoto, yl, true, 0, 0);
//					int finalXtogoto = xtogoto;
//					int finalDestinationfinal = destinationfinal;
//					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//						@Override
//						public void execute(CycleEventContainer container) {
//							if (c.getAgilityHandler().hotSpot(c, finalXtogoto, yl)) {
////								c.getPA().object(1569, obX, obY, 3, 0);
////								c.getPA().object(1568, obX, obY+1, 1, 0);
//
//								c.getPA().object(1568, obX, obY, 1, 0);
//								c.getPA().object(1569, obX, obY-1, 3, 0);
//
//
//								PathFinder.getPathFinder().findRouteNoclip(c, finalDestinationfinal ,yl, true, 0, 0);
//								c.setNewWalkCmdIsRunning(false);
//								CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//									@Override
//									public void execute(CycleEventContainer container) {
//										//if (c.getAgilityHandler().hotSpot(c, finalXtogoto, 3451)) {
//										c.getPA().object(1568, obX, obY-1, 0, 0);
//										c.getPA().object(1569, obX, obY, 0, 0);
//										container.stop();
//
//										//	}
//
//									}
//
//									@Override
//									public void onStopped() {
//
//									}
//								}, 3);
//								container.stop();
//							}
//
//						}
//
//						@Override
//						public void onStopped() {
//
//						}
//					}, 1);
//					return;
//				}
//				WorldObject gate1569 = ClickObject.getObject(c, objectType,obX,obY);
//				if (object != null) {
//					System.out.println("f: "+gate1569.getFace());
//				}
//
//				//what was originally there
//				originalgate = new GlobalObject(1569, obX,obY, 0, 2, 0, 0);
//				GlobalObject toupgate = new GlobalObject(1568, obX,obY+1, 0, 2, 0, 0);
//
//				//closed originally
//			gate_open = new GlobalObject(1568, obX,obY, 0, 3, 0, 0);
//				GlobalObject toupgate_open = new GlobalObject(1569, obX,obY+1, 0, 1, 0, 0);
//
//
//
//
//				face = gate1569.getFace();
//				if(face == 2){//closing
//
//					Server.getGlobalObjects().add(gate_open);
//					Server.getGlobalObjects().add(toupgate_open);
//					if(c.absX> obX)
//						c.getPA().walkTo(-1,0);
//					else
//						c.getPA().walkTo(1,0);
//				}
//
//				int finalFace = face;
//				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//					@Override
//					public void execute(CycleEventContainer container) {
//						if(finalFace == 2){//closing
//							Server.getGlobalObjects().add(originalgate);
//							Server.getGlobalObjects().add(toupgate);
//
//						}
//						container.stop();
//					}
//
//					@Override
//					public void onStopped() {
//
//					}
//				}, 3);
//				break;
			case 14910:
				if (c.absY == 3288) {
					c.getPA().walkTo(0, +1);
				} else if (c.absY == 3289) {
					c.getPA().walkTo(0, -1);
				}
				break;
			case 2144:

			case 2143://prison door
					if (!c.getItems().playerHasItem(1590)) {
					c.sendMessage("You need a @blu@Dusty key@bla@ to open this door.");
					return;
				}
					int goingtox  = 0;
				if(c.absX < 2889) {
					goingtox = 2889;
				} else {
					goingtox = 2888;
				}
				if(objectType == 2143){
					c.getPA().object(2144, obX, obY, 1, 0);
					c.getPA().object(2143, obX, obY-1, 3, 0);
				} else {
					c.getPA().object(2143, obX, obY, 3, 0);
					c.getPA().object(2144, obX, obY+1, 1, 0);
				}

					PathFinder.getPathFinder().findRouteNoclip(c, goingtox, c.absY, true, 0, 0);
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							//if (c.getAgilityHandler().hotSpot(c, finalXtogoto, 3451)) {
							if(objectType == 2143){
								c.getPA().object(2143, obX, obY, 0, 0);
								c.getPA().object(2144, obX, obY-1, 0, 0);
							} else {
								c.getPA().object(2144, obX, obY, 0, 0);
								c.getPA().object(2143, obX, obY+1, 0, 0);
							}


							container.stop();

							//	}

						}

						@Override
						public void onStopped() {

						}
					}, 3);

				break;
			case 2631://prison door
				if (!c.getItems().playerHasItem(1591)) {
					c.sendMessage("You need a @blu@Jail key@bla@ to open this door.");
					return;
				}
					c.getPA().object(2631, obX, obY, 2, 0);
				PathFinder.getPathFinder().findRouteNoclip(c, 2931, c.absY > 9689 ? 9689 : 9690 , true, 0, 0);
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {

							c.getPA().object(2631, obX, obY, 3, 0);



						container.stop();


					}

					@Override
					public void onStopped() {

					}
				}, 3);

				break;
			case 2116://barb agility doors
			case 2115:
				if (c.playerLevel[16] < 35) {
					c.sendMessage("You need an agility level of 35 to enter.");
					return;
				}
					if (c.getAgilityHandler().hotSpot(c, c.absX > 2545 ? 2546 : 2545, objectType == 2115 ? 3570 : 3569)) {


						//if(c.absX <= 2545){
						c.getPA().object(2116, 2545, 3570, 1, 0);
						c.getPA().object(2115, 2545, 3569, 3, 0);
						//}
						PathFinder.getPathFinder().findRouteNoclip(c, c.absX <= 2545 ? 2546 : 2545, c.absY, true, 0, 0);
						c.setNewWalkCmdIsRunning(false);
						CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
							@Override
							public void execute(CycleEventContainer container) {

								c.getPA().object(2115, 2545, 3570, 2, 0);
								c.getPA().object(2116, 2545, 3569, 2, 0);
								container.stop();

							}
						}, 3);
					}
				break;
//			case 1727:
//if(obX == 2935 && obY == 3451){ //members gate
//	int xtogoto = 0;
//	int destinationfinal = 0;
//	if(c.absX > 2935){
//		xtogoto = 2936;
//		destinationfinal = 2935;
//	} else {
//		xtogoto = 2935;
//		destinationfinal = 2936;
//	}
//	PathFinder.getPathFinder().findRouteNoclip(c, xtogoto, 3451, true, 0, 0);
//	int finalXtogoto = xtogoto;
//	int finalDestinationfinal = destinationfinal;
//	CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//		@Override
//		public void execute(CycleEventContainer container) {
//			if (c.getAgilityHandler().hotSpot(c, finalXtogoto, 3451)) {
//				c.getPA().object(-1, obX, obY, 0, 0);
//				c.getPA().object(-1, obX, obY-1, 0, 0);
//				c.getPA().object(1727, obX+1, obY, 1, 0);
//				c.getPA().object(1728, obX+1, obY-1, 3, 0);
//				PathFinder.getPathFinder().findRouteNoclip(c, finalDestinationfinal ,3451, true, 0, 0);
//				c.setNewWalkCmdIsRunning(false);
//				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//					@Override
//					public void execute(CycleEventContainer container) {
//						//if (c.getAgilityHandler().hotSpot(c, finalXtogoto, 3451)) {
//							c.getPA().object(1727, obX, obY, 2, 0);
//							c.getPA().object(1728, obX, obY-1, 2, 0);
//							c.getPA().object(-1, obX+1, obY, 1, 0);
//							c.getPA().object(-1, obX+1, obY-1, 3, 0);
//							container.stop();
//
//					//	}
//
//					}
//
//					@Override
//					public void onStopped() {
//
//					}
//				}, 3);
//
//			}
//
//		}
//
//		@Override
//		public void onStopped() {
//
//		}
//	}, 1);
//	return;
//}
//				WorldObject gate1727= ClickObject.getObject(c, objectType,obX,obY);
//				if (object != null) {
//					System.out.println("f: "+gate1727.getFace());
//				}
//
//				//what was originally there
//
//				//closed originally
//				GlobalObject openedgate;
//
//				GlobalObject originalgate2= new GlobalObject(1728, obX-1,obY, 0, 3, 0, 0);
//				//originally closed gates
//				GlobalObject originalgate_youclicked = new GlobalObject(1727, obX-1,obY, 0, 0, 0, 0);
//				GlobalObject originalgate_youclicked_totheleft= new GlobalObject(1728, obX,obY, 0, 2, 0, 0);
//
//				//the gate that 1728 turns to
//				GlobalObject originalgate_2 = new GlobalObject(1727, obX,obY, 0, 3, 0, 0);
//				//the gate that 1727 turns to when u click 1728
//
//
//
//				GlobalObject opened1727 = new GlobalObject(1727, obX,obY, 0, 0, 0, 0);
//
//				GlobalObject replacementof1727 = new GlobalObject(1728, obX,obY, 0, 3, 0, 0);
//				GlobalObject replacementsof1728 = new GlobalObject(1727, obX+1,obY, 0, 3, 0, 0);
//
//
//				GlobalObject gate1727closedface2 = new GlobalObject(1727, obX,obY, 0, 2, 0, 0);
//
//
//
//			//GlobalObject replacementof1727 = new GlobalObject(1727, obX,obY, 0, 0, 0, 0);
//				int x = 0;
//				int y = 0;
//				int x_to = 0;
//				int y_to =0;
//
//
//				GlobalObject thereplacementgate = null;
//				GlobalObject thereplacementgate_nextoit = null;
//				face = gate1727.getFace();
////				if(face == 1){//closing
////
////
////					Server.getGlobalObjects().add(gate_open);
////					Server.getGlobalObjects().add(toleftgate_open);
////					if(c.absY > obY)
////						c.getPA().walkTo(0, -1);
////					else
////						c.getPA().walkTo(0, 1);
////				}
//				if(face == 3){//3 means the gate is closed originally.
//			object = originalgate_2;
//
//			thereplacementgate = originalgate_youclicked;
//
//			thereplacementgate_nextoit = originalgate_youclicked_totheleft;
//				}
//
//				if(face == 0){//2 means the gate is open
//					object = opened1727;
//
//					thereplacementgate = replacementof1727;
//					thereplacementgate_nextoit = replacementsof1728;
//				}
//
//
//
//
//
//					Optional<WorldObject> objclosetoleft = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(object.getObjectId(),object.getX(),object.getY(),c.getHeight());//the object we clicked
//					GlobalObject objclosetoleft1560 = new GlobalObject(objclosetoleft.get().id, objclosetoleft.get().x, objclosetoleft.get().y, objclosetoleft.get().height, objclosetoleft.get().face, objclosetoleft.get().type);
//
//
//					Server.getGlobalObjects().add(objclosetoleft1560);
//					Server.getGlobalObjects().remove(objclosetoleft1560);
//					Server.getGlobalObjects().updateObject(objclosetoleft1560, -1);
//
//
//
//					Optional<WorldObject> objclosetoleft_nextoit = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(face == 3 ? 1728  : face == 2 ? 1728: objectType,face == 3 ? obX-1 : face == 2 ? obX+1 : obX,obY,c.getHeight());//the object next to it
//					GlobalObject objclosetoleft_ = new GlobalObject(objclosetoleft_nextoit.get().id, objclosetoleft_nextoit.get().x, objclosetoleft_nextoit.get().y, objclosetoleft_nextoit.get().height, objclosetoleft_nextoit.get().face, objclosetoleft_nextoit.get().type);
//
//
//					Server.getGlobalObjects().add(objclosetoleft_);
//					Server.getGlobalObjects().remove(objclosetoleft_);
//					Server.getGlobalObjects().updateObject(objclosetoleft_, -1);
//
//					Server.getGlobalObjects().add(thereplacementgate);
//					Server.getGlobalObjects().add(thereplacementgate_nextoit);
//
//				//}
//				break;
//			case 1728:
//				if(obX == 3008 && obY == 3850) {
//					if (c.getAgilityHandler().hotSpot(c, c.absX >= c.objectX ? c.objectX : c.objectX-1, c.objectY)) {
//
//						c.getPA().object(1727, 3008, 3850, 1, 0);
//						c.getPA().object(1728, 3008, 3849, 3, 0);
//						PathFinder.getPathFinder().findRouteNoclip(c, c.absX >= c.objectX ? c.objectX - 1 : c.objectX, 3850, true, 0, 0);
//
//						CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//							@Override
//							public void execute(CycleEventContainer container) {
//								c.getPA().object(1728, 3008, 3850, 0, 0);
//								c.getPA().object(1727, 3008, 3849, 0, 0);
//								container.stop();
//
//
//							}
//
//							@Override
//							public void onStopped() {
//
//							}
//						}, 3);
//
//					}
//					return;
//				}
//				if(obX == 2935 && obY == 3450){ //members gate
//					int xtogoto = 0;
//					int destinationfinal = 0;
//					if(c.absX > 2935){
//						xtogoto = 2936;
//						destinationfinal = 2935;
//					} else {
//						xtogoto = 2935;
//						destinationfinal = 2936;
//					}
//					PathFinder.getPathFinder().findRouteNoclip(c, xtogoto, 3450, true, 0, 0);
//					int finalXtogoto = xtogoto;
//					int finalDestinationfinal = destinationfinal;
//					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//						@Override
//						public void execute(CycleEventContainer container) {
//							if (c.getAgilityHandler().hotSpot(c, finalXtogoto, 3450)) {
//								c.getPA().object(-1, obX, obY, 0, 0);
//								c.getPA().object(-1, obX, obY+1, 0, 0);
//								c.getPA().object(1727, obX+1, obY+1, 1, 0);
//								c.getPA().object(1728, obX+1, obY, 3, 0);
//								PathFinder.getPathFinder().findRouteNoclip(c, finalDestinationfinal ,3450, true, 0, 0);
//								c.setNewWalkCmdIsRunning(false);
//								CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
//									@Override
//									public void execute(CycleEventContainer container) {
//										//if (c.getAgilityHandler().hotSpot(c, finalXtogoto, 3451)) {
//										c.getPA().object(1728, obX, obY, 2, 0);
//										c.getPA().object(1727, obX, obY+1, 2, 0);
//										c.getPA().object(-1, obX+1, obY+1, 1, 0);
//										c.getPA().object(-1, obX+1, obY, 3, 0);
//										container.stop();
//
//										//	}
//
//									}
//
//									@Override
//									public void onStopped() {
//
//									}
//								}, 3);
//
//							}
//
//						}
//
//						@Override
//						public void onStopped() {
//
//						}
//					}, 1);
//					return;
//				}
//				WorldObject gate1728= ClickObject.getObject(c, objectType,obX,obY);
//				if (object != null) {
//				//	System.out.println("f: "+gate1728.getFace());
//				}
////				the1727gateface2_toadd = new GlobalObject(1728, obX,obY+1, 0, 1, 0, 0);
////			 the1728gateface2_toadd = new GlobalObject(1727, obX,obY, 0, 3, 0, 0);
////
////				 the1727gateface2_youclicked = new GlobalObject(1728, obX,obY, 0, 2, 0, 0);
////				 the1728gateface2_belowit = new GlobalObject(1727, obX,obY+1, 0, 2, 0, 0);
////
////
////
////				//what was originally there
////				originalgate = new GlobalObject(1727, obX-1,obY, 0, 1, 0, 0);
////			 toleftgate = new GlobalObject(1728, obX,obY, 0, 1, 0, 0);
////
////				//closed originally
////			gate_open = new GlobalObject(1727, obX,obY, 0, 2, 0, 0);
////			toleftgate_open = new GlobalObject(1728, obX-1,obY, 0, 0, 0, 0);
////
////
////			//face 3 originally closed gates
//				originalgate_2 = new GlobalObject(1728, obX,obY, 0, 3, 0, 0);
//
//
//				originalgate_youclicked = new GlobalObject(1727, obX,obY, 0, 0, 0, 0);
//				originalgate_youclicked_totheleft = new GlobalObject(1728, obX+1,obY, 0, 2, 0, 0);
//
//				openedgate = new GlobalObject(1728, obX,obY, 0, 2, 0, 0);
//
//				originalgate = new GlobalObject(1727, obX,obY, 0, 3, 0, 0);
//				originalgate2 = new GlobalObject(1728, obX-1,obY, 0, 3, 0, 0);
//				face = gate1728.getFace();
//		 thereplacementgate = null;
//	thereplacementgate_nextoit = null;
////				if(face == 1){//closing
////
////					Server.getGlobalObjects().add(gate_open);
////					Server.getGlobalObjects().add(toleftgate_open);
////					if(c.absY > obY)
////					c.getPA().walkTo(0, -1);
////					else
////						c.getPA().walkTo(0, 1);
////				}
//				if(face == 3){//opening the gate
//					object = originalgate_2;
//					//theoriginalgateyouclicked_nextoit = originalgatetotheleft_2;
//					thereplacementgate = originalgate_youclicked;
//					thereplacementgate_nextoit = originalgate_youclicked_totheleft;
//
//				}
//				if(face == 2){//closing the gate
//					object = openedgate;
//					//theoriginalgateyouclicked_nextoit = originalgatetotheleft_2;
//					thereplacementgate = originalgate;
//					thereplacementgate_nextoit = originalgate2;
//
//				}
//			 objclosetoleft = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(object.getObjectId(),object.getX(),object.getY(),c.getHeight());//the object we clicked
//		objclosetoleft1560 = new GlobalObject(objclosetoleft.get().id, objclosetoleft.get().x, objclosetoleft.get().y, objclosetoleft.get().height, objclosetoleft.get().face, objclosetoleft.get().type);
//				Server.getGlobalObjects().add(objclosetoleft1560);
//				Server.getGlobalObjects().remove(objclosetoleft1560);
//				Server.getGlobalObjects().updateObject(objclosetoleft1560, -1);
//
//
//
//			 objclosetoleft_nextoit = c.getRegionProvider().get(c.absX, c.absY).getWorldObject(face == 3 ? 1727 : face == 2 ? 1727 : objectType,face == 3 ? obX+1 : face == 2 ? obX-1: obX,obY,c.getHeight());//the object next to it
//			objclosetoleft_ = new GlobalObject(objclosetoleft_nextoit.get().id, objclosetoleft_nextoit.get().x, objclosetoleft_nextoit.get().y, objclosetoleft_nextoit.get().height, objclosetoleft_nextoit.get().face, objclosetoleft_nextoit.get().type);
//
//
//				Server.getGlobalObjects().add(objclosetoleft_);
//				Server.getGlobalObjects().remove(objclosetoleft_);
//				Server.getGlobalObjects().updateObject(objclosetoleft_, -1);
//
//				Server.getGlobalObjects().add(thereplacementgate);
//				Server.getGlobalObjects().add(thereplacementgate_nextoit);
//				break;
			case 10439:
			case 7814:
				PlayerAssistant.refreshHealthWithoutPenalty(c);
				break;
			case 2670:
				if (!c.getItems().playerHasItem(1925) || !c.getItems().playerHasItem(946)) {
					c.sendMessage("You must have an empty bucket and a knife to do this.");
					return;
				}
				c.getItems().deleteItem(1925, 1);
				c.getItems().addItem(1929, 1);
				c.sendMessage("You cut the cactus and pour some water into the bucket.");
				c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.CUT_CACTUS);
				break;
			// Carts Start
			case 7029:
				TrainCart.handleInteraction(c);
				break;


			case 43700:// Rift portal to npcs
				boolean enter = c.getY() > 9483;
				if (c.playerLevel[16] < 75) {
					c.sendMessage("You need an agility level of 75 to pass the barrier.");
					return;
				}
				c.getPA().movePlayer(3615, enter ? 9482 : 9484, c.getPosition().getHeight());
				break;
			case 26419://godwars hole
				if (!c.getItems().playerHasItem(954, 1)){
					c.sendMessage("You need a rope to go down.");
					return;
				}
				//c.getPA().checkObjectSpawn(26418, 2917, 3745, 0, 10);
				c.getItems().deleteItem2(954, 1);
				c.learnedGWDentrance = true;
				c.sendMessage("@blu@You learned the entrance to the godwars dungeon!");
				Server.getGlobalObjects().checkGWD(c);
				break;
			case 26418:
				c.getPA().movePlayer(2881, 5310, 2);
				c.sendMessage("You climb down into the godwars dungeon.");
				break;
			case 42009:// entrance to lizardman shaman
				c.getPA().movePlayer(1476, 3691, 0);
				c.startAnimation(746);
				c.sendMessage("You tumble into a lizard pit");
				break;
			case 29322:// entrance to wintertodt
				c.getPA().movePlayer(1630, 3981, 0);
				c.sendMessage("You enter Wintertodt");
				break;

			case 28837:
				c.getDH().sendDialogues(193193, -1);
				break;
			// Carts End
			case 10321:
				c.getPA().spellTeleport(1752, 5232, 0, false);
				c.sendMessage("Welcome to the Giant Mole cave, try your luck for a granite maul.");
				break;
			case 1294:
				c.getDH().tree = "stronghold";
				c.getDH().sendDialogues(65, -1);
				break;

			case 1293:
				c.getDH().tree = "village";
				c.getDH().sendDialogues(65, -1);
				break;

			case 1295:
				c.getDH().tree = "grand_exchange";
				c.getDH().sendDialogues(65, -1);
				break;

			case 2073:
				c.getItems().addItem(1963, 1);
				c.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.PICK_BANANAS);
				break;

			case 20877:
				AgilityHandler.delayFade(c, "CRAWL", 2712, 9564, 0, "You crawl into the entrance.",
						"and you end up in a dungeon.", 3);
				break;
			case 20878:
				AgilityHandler.delayFade(c, "CRAWL", 1571, 3659, 0, "You crawl into the entrance.",
						"and you end up in a dungeon.", 3);
				break;
			case 16675:
				AgilityHandler.delayEmote(c, "CLIMB_UP", 2445, 3416, 1, 2);
				break;
			case 16677:
				AgilityHandler.delayEmote(c, "CLIMB_UP", 2445, 3416, 0, 2);
				break;

			case 6434:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3118, 9644, 0, 2);
				break;

			case 11441:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2856, 9570, 0, 2);
				break;

			case 18969:
				AgilityHandler.delayEmote(c, "CLIMB_UP", 2857, 3167, 0, 2);
				break;

			case 11835:
				AgilityHandler.delayFade(c, "CRAWL", 2480, 5175, 0, "You crawl into the entrance.",
						"and you end up in Tzhaar City.", 3);
				break;
			case 11836:
				AgilityHandler.delayFade(c, "CRAWL", 1212, 3540, 0, "You crawl into the entrance.",
						"and you end up back on Mt. Quidamortem.", 3);
				break;

			case 155:
			case 156:
				AgilityHandler.delayEmote(c, "BALANCE", 3096, 3359, 0, 2);
				break;
			case 160:
				AgilityHandler.delayEmote(c, 2140, 3098, 3357, 0, 2);
				break;

			case 23568:
				c.getPA().movePlayer(2704, 3205, 0);
				break;

			case 23569:
				c.getPA().movePlayer(2709, 3209, 0);
				break;

			case 17068:
				if (c.playerLevel[Player.playerAgility] < 8 || c.playerLevel[Player.playerStrength] < 19
						|| c.playerLevel[Player.playerRanged] < 37) {
					c.sendMessage(
							"You need an agility level of 8, strength level of 19 and ranged level of 37 to do this.");
					return;
				}
				AgilityHandler.delayEmote(c, "JUMP", 3253, 3180, 0, 2);
				c.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.RIVER_LUM_SHORTCUT);
				break;

			case 16465:
//				if (!c.getDiaryManager().getDesertDiary().hasCompletedSome("ELITE")) {
//					c.sendMessage("You must have completed all tasks in the desert diary to do this.");
//					return;
//				}
				if (c.playerLevel[Player.playerAgility] < 82) {
					c.sendMessage("You need an agility level of at least 82 to squeeze through here.");
					return;
				}
				c.sendMessage("You squeeze through the crevice.");
				if (c.absX == 3506 && c.absY == 9505)
					c.getPA().movePlayer(3500, 9510, 2);
				else if (c.absX == 3500 && c.absY == 9510)
					c.getPA().movePlayer(3506, 9505, 2);
				break;

			case 2147:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3104, 9576, 0, 2);
				break;
			case 2148:
				AgilityHandler.delayEmote(c, "CLIMB_UP", 3105, 3162, 0, 2);
				break;
			case 11668:
			case 1579:
				if (obX == 3405 && obY == 3507)
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3405,9905, 0, 2);
					else
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3097, 9868, 0, 2);
				break;
			case 10042:
				if (obX == 2833 && obY == 3542)
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2906, 9970, 0, 2);
				break;
			case 10043://warriors guild dragon degender door
					c.getWarriorsGuildbasement().handleDoor();
				break;
			case 33318:
				c.start(new FireOfDestructionDialogue(c, -1));
				break;
			case 25938:
			case 11794:
				if (Boundary.isIn(c, Boundary.EDGEVILLE_EXTENDED)) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", c.absX, c.absY, 1, 2);
				}
				break;
			case 25939:
			case 11795:
				if (Boundary.isIn(c, Boundary.EDGEVILLE_EXTENDED)) {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", c.absX, c.absY, 0, 2);
				}
				break;

			case 27785:
				c.getDH().sendDialogues(70300, -1);
				break;
			case 36082: //exit portal to prif
				c.getPA().movePlayer(3228, 6116, 0);
				break;
			case 36081: //prif portal to gauntlet lobby
				if (c.playerLevel[16] < 75) {
					c.sendMessage("You must have a agility level of at least 75 to enter The Gauntlet");
					return;
				}
				c.getPA().movePlayer(3032, 6126, 1);
				break;
			case 35965: // exit portal from inside gauntlet
				c.getPA().movePlayer(3032, 6126, 1);
				break;
			case 30282:
				Consumer<Player> wave0 = p -> {
					Inferno.startinfernowalkingto(c,0);
				};
				Consumer<Player> wavelast = p -> {
					Inferno.startinfernowalkingto(c,68);

				};
				c.start(new DialogueBuilder(c)
						.setNpcId(TUTORIAL_NPC)
						.option(new DialogueOption("Start from wave 0", wave0),
								new DialogueOption("Start at last wave (1M gp)", wavelast))
				);
				break;
			case 30266:
				if (c.usedFc == true) {

					                if (c.getX() > obX && c.getY() == obY) {
										PathFinder.getPathFinder().findRouteNoclip(c, c.getX()-2, obY, true, 0, 0);
					                   // c.getMovementQueue().interpolate(player.tile().x - 2, obj.tile().y, MovementQueue.StepType.FORCED_WALK);
					                } else if (c.getX() < obX && c.getY() == obY) {
										PathFinder.getPathFinder().findRouteNoclip(c, c.getX()+2, obY, true, 0, 0);

										// c.getMovementQueue().interpolate(player.tile().x + 2, obj.tile().y, MovementQueue.StepType.FORCED_WALK);

					                } else if (c.getX() == obX && c.getY() < obY) {
										PathFinder.getPathFinder().findRouteNoclip(c, c.getX(), obY+2, true, 0, 0);
					                  //  c.getMovementQueue().interpolate(player.tile().x, obj.tile().y + 2, MovementQueue.StepType.FORCED_WALK);
					                } else if (c.getX() == obX &&c.getY()> obY) {
										PathFinder.getPathFinder().findRouteNoclip(c, c.getX()+2, obY-2, true, 0, 0);
					                  //  c.getMovementQueue().interpolate(player.tile().x, obj.tile().y - 2, MovementQueue.StepType.FORCED_WALK);
					                }
					c.setNewWalkCmdIsRunning(false);
				//	c.getPA().movePlayer(2495, 5174, 0);
					//PathFinder.getPathFinder().findRouteNoclip(c, c.absX, ytogoto, true, 0, 0);
					//c.setNewWalkCmdIsRunning(false);
				} else if (c.getItems().hasAnywhere(6570)) {
				//	c.getItems().deleteItem(6570, 1);
					c.usedFc = true;
					//c.getPA().movePlayer(2495, 5174, 0);
					//PathFinder.getPathFinder().findRouteNoclip(c, c.absX, ytogoto, true, 0, 0);
					if (c.getX() > obX && c.getY() == obY) {
						PathFinder.getPathFinder().findRouteNoclip(c, c.getX()-2, obY, true, 0, 0);
						// c.getMovementQueue().interpolate(player.tile().x - 2, obj.tile().y, MovementQueue.StepType.FORCED_WALK);
					} else if (c.getX() < obX && c.getY() == obY) {
						PathFinder.getPathFinder().findRouteNoclip(c, c.getX()+2, obY, true, 0, 0);

						// c.getMovementQueue().interpolate(player.tile().x + 2, obj.tile().y, MovementQueue.StepType.FORCED_WALK);

					} else if (c.getX() == obX && c.getY() < obY) {
						PathFinder.getPathFinder().findRouteNoclip(c, c.getX(), obY+2, true, 0, 0);
						//  c.getMovementQueue().interpolate(player.tile().x, obj.tile().y + 2, MovementQueue.StepType.FORCED_WALK);
					} else if (c.getX() == obX &&c.getY()> obY) {
						PathFinder.getPathFinder().findRouteNoclip(c, c.getX()+2, obY-2, true, 0, 0);
						//  c.getMovementQueue().interpolate(player.tile().x, obj.tile().y - 2, MovementQueue.StepType.FORCED_WALK);
					}
					c.setNewWalkCmdIsRunning(false);
				} else {
					c.sendMessage("@red@You must have a fire cape on you or in your bank to enter the inner city.");
					return;
				}
				break;

			case 28894:
			case 28895:
			case 28898:
			case 28897:
			case 28896: // catacomb exits
				c.getPA().movePlayer(3090, 3488, 0);
				c.sendMessage("You return to the statue.");
				break;

			case 27777:
				c.getPA().movePlayer(1781, 3412, 0);
				//c.sendMessage("Welcome to the CrabClaw Isle, try your luck for a tentacle or Trident of the Seas!.");
				break;
			case 3828:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3484, 9510, 2, 2);
				break;

			case 3829:
				AgilityHandler.delayEmote(c, "CLIMB_UP", 3226, 3109, 0, 2);
				c.sendMessage("You find the light of day outside of the tunnel!");
				break;
			case 3832:
				c.getPA().movePlayer(3510, 9496, 2);
				break;
			case 44994:
				c.getDH().sendDialogues(44994, 4_287);
				break;
			case 4031://shantay pass gate
				if (c.absY >= 3117) {
				//	c.getDH().sendDialogues(4645, 4642);
					if (!c.getItems().playerHasItem(SHANTAY_PASS)) {
						c.sendMessage("You need a @blu@Shantay Pass@bla@ to enter the desert.");
						return;
					}
					c.getAgilityHandler().move(c, 0, -3, 819, -1);
				} else {
					c.getAgilityHandler().move(c, 0, 3, 819, -1);
				}
				break;
//			case 4031:
//				if (c.absY == 3117) {
//					if (EquipmentSet.DESERT_ROBES.isWearing(c)) {
//						c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.PASS_GATE_ROBES);
//					} else {
//						c.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.PASS_GATE);
//					}
//					c.getPA().movePlayer(c.absX, 3115);
//				} else {
//					c.getPA().movePlayer(c.absX, 3117);
//				}
//				break;

			case 7122:
				if (c.absX == 2564 && c.absY == 3310)
					c.getPA().movePlayer(2563, 3310);
				else if (c.absX == 2563 && c.absY == 3310)
					c.getPA().movePlayer(2564, 3310);
				break;

			case 24958:
				if (c.getDiaryManager().getVarrockDiary().hasCompleted("EASY")) {
					if (c.absX == 3143 && c.absY == 3443)
						c.getPA().movePlayer(3143, 3444);
					else if (c.absX == 3143 && c.absY == 3444)
						c.getPA().movePlayer(3143, 3443);
				} else {
					c.sendMessage("You must have completed all easy tasks in the varrock diary to enter.");
					return;
				}
				break;

			case 10045:
				if (c.getDiaryManager().getVarrockDiary().hasCompleted("EASY")) {
					if (c.absX == 3143 && c.absY == 3452)
						c.getPA().movePlayer(3144, 3452);
					else if (c.absX == 3144 && c.absY == 3452)
						c.getPA().movePlayer(3143, 3452);
				} else {
					c.sendMessage("You must have completed all easy tasks in the varrock diary to enter.");
					return;
				}
				break;

			case 11780:
				if (c.getDiaryManager().getVarrockDiary().hasCompleted("HARD")) {
					if (c.absX == 3255)
						c.getPA().movePlayer(3256, c.absY);
					else
						c.getPA().movePlayer(3255, c.absY);
				} else {
					c.sendMessage("You must have completed all hard tasks in the varrock diary to enter.");
					return;
				}
				break;
			case 1805:
				if (c.getDiaryManager().getVarrockDiary().hasCompleted("EASY")) {
					c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.CHAMPIONS_GUILD);
					if (c.absY == 3362)
						c.getPA().movePlayer(c.absX, 3363);
					else
						c.getPA().movePlayer(c.absX, 3362);
				} else {
					c.sendMessage("You must have completed all easy tasks in the varrock diary to enter.");
					return;
				}
				break;

			case 538:
				c.getPA().movePlayer(2280, 10016, 0);
				break;

			case 537:
				Kraken.init(c);
				break;
			case 5946:
				AgilityHandler.delayEmote(c, "CLIMB_UP", 3168, 3172, 0, 2);
				//c.getPA().movePlayer(3168, 3172, 0);
				break;
			case 5947:
				AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3169, 9571, 0, 2);
				break;
			case 30177:
				AgilityHandler.delayEmote(c, "CRAWL", 2276, 9988, 0, 2);
				break;
			case 30178:
				c.getPA().movePlayer(2280,3610,0);
				break;
			case 6462: // Ice gate
			case 6461:
				c.getPA().movePlayer(2852, 3809, 2);
				break;

			case 6456: // Ice ledge
				c.getPA().movePlayer(2855, c.absY, 1);
				break;

			case 6455: // Ice ledge (Bottom)
				if (c.absY >= 3804)
					c.getPA().movePlayer(2837, 3803, 1);
				else
					c.getPA().movePlayer(2837, 3805, 0);
				break;

			case 677:
				int z = c.getMode().isIronmanType() ? 6 : 2;
				if (c.absX == 2974)
					c.getPA().movePlayer(2970, 4384, z);
				else
					c.getPA().movePlayer(2974, 4384, z);
				break;

			case 13641: // Teleportation Device
				c.getDH().sendDialogues(63, -1);
				break;
			case 26741:
				c.objectDistance = 13;
				c.objectXOffset = 13;
				c.objectYOffset = 13;
				ViewingOrb.clickObject(c);
				break;
			case 23104:
				if (!(c.getSlayer().getTask().isPresent())) {
					c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
					return;
				}
				if (c.getSlayer().getTask().get().getPrimaryName().equals("hellhound") && (c.getKonarSlayerLocation() == "stronghold cave" || c.getKonarSlayerLocation() == "taverly dungeon")) {
					c.sendMessage("Your Konar task does not permit access here.");
					return;
				}
				if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && (!c.getSlayer().getTask().get().getPrimaryName().equals("hellhound") && !c.getSlayer().getTask().get().getPrimaryName().equals("cerberus")))) {
					c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
					return;
				}

				if (System.currentTimeMillis() - c.cerbDelay > 5000) {
					if (!c.getSlayer().getTask().isPresent()) {
						c.sendMessage("You must have an active cerberus or hellhound task to enter this cave...");
						return;
					}
//					if (!c.getSlayer().isCerberusRoute()) {
//						c.sendMessage("You have bought Route into cerberus cave. please wait till you will be teleported.");
//						Cerberus.init(c);
//						c.cerbDelay = System.currentTimeMillis();
//						return;
//					}

					if (c.playerLevel[Skill.SLAYER.getId()] < 91) {
						c.sendMessage("You need a slayer level of 91 to enter.");
						return;
					}

					if (Server.getEventHandler().isRunning(c, "cerb")) {
						c.sendMessage("You're about to fight start the fight, please wait.");
						return;
					}

					Cerberus.init(c);

					c.cerbDelay = System.currentTimeMillis();
				} else {
					c.sendMessage("Please wait a few seconds between clicks.");
				}
				break;

			case 21772:
				if (!Boundary.isIn(c, Boundary.CERBERUS_ROOM_WEST)) {
					return;
				}
				c.getPA().movePlayer(1309, 1250, 0);
				break;

			case 28900:
				DarkAltar.handleDarkTeleportInteraction(c);
				break;
			case 28925:
				DarkAltar.handlePortalInteraction(c);
				break;

			case 23105:
				c.appendDamage(5, Hitmark.HIT);
				if (c.absY == 1241) {
					c.getPA().walkTo(0, +2);
				} else {
					c.moveTo(Cerberus.EXIT);
				}
				break;

			case 31925:
				c.getPA().startLeverTeleport(3828, 3893, 0,0);
				break;


			case 13642: // Lectern
				c.getDH().sendDialogues(10, -1);
				break;
			case 30169:
				int playersinkings = Boundary.getPlayersInBoundary(Boundary.DAGANNOTH_KINGS);
			//	if(playersinkings>0)
			//	c.sendMessage("There are currently "+playersinkings+" players in the dagannoth kings lair.");
				//else
					c.sendMessage("There are currently "+playersinkings+" players in the dagannoth kings lair.");
				break;
			case 8930:
				c.getPA().movePlayer(2545, 10143, 0);
				break;

			case 10177: // Dagganoth kings ladder

				//dialog opt
				c.getDH().sendOption2("Climb up", "Climb down");
				c.dialogueAction = 10177;
				break;

			case 10193:
				AgilityHandler.delayEmote(c, "CLIMB_UP", 2545,10143,0, 2);
				break;

			case 10194://ladder to waterbirth island
					if(c.getQuesting().getQuestList().get(1).isQuestCompleted()){
						AgilityHandler.delayEmote(c, "CLIMB_UP", 2512,4656,0, 2);
				} else {
						c.sendMessage("You must have completed @blu@Horror From the Deep@bla@ to purchase this.");
					}


				break;
			case 10195:
				c.getPA().movePlayer(1809, 4405, 2);
				break;

			case 10196:
				c.getPA().movePlayer(1807, 4405, 3);
				break;

			case 10197:
				c.getPA().movePlayer(1823, 4404, 2);
				break;

			case 10198:
				c.getPA().movePlayer(1825, 4404, 3);
				break;

			case 10199:
				c.getPA().movePlayer(1834, 4388, 2);
				break;

			case 10200:
				c.getPA().movePlayer(1834, 4390, 3);
				break;

			case 10201:
				c.getPA().movePlayer(1811, 4394, 1);
				break;

			case 10202:
				c.getPA().movePlayer(1812, 4394, 2);
				break;

			case 10203:
				c.getPA().movePlayer(1799, 4386, 2);
				break;

			case 10204:
				c.getPA().movePlayer(1799, 4388, 1);
				break;

			case 10205:
				c.getPA().movePlayer(1796, 4382, 1);
				break;

			case 10206:
				c.getPA().movePlayer(1796, 4382, 2);
				break;

			case 10207:
				c.getPA().movePlayer(1800, 4369, 2);
				break;

			case 10208:
				c.getPA().movePlayer(1802, 4370, 1);
				break;

			case 10209:
				c.getPA().movePlayer(1827, 4362, 1);
				break;

			case 10210:
				c.getPA().movePlayer(1825, 4362, 2);
				break;

			case 10211:
				c.getPA().movePlayer(1863, 4373, 2);
				break;

			case 10212:
				c.getPA().movePlayer(1863, 4371, 1);
				break;

			case 10213:
				c.getPA().movePlayer(1864, 4389, 1);
				break;
			case 3831:
				c.getPA().movePlayer(2900,4449,0);
				break;
			case 10214:
				c.getPA().movePlayer(1864, 4387, 2);
				break;

			case 10215:
				c.getPA().movePlayer(1890, 4407, 0);
				break;

			case 10216:
				c.getPA().movePlayer(1890, 4406, 1);
				break;

			case 10217:
				c.getPA().movePlayer(1957, 4373, 1);
				break;

			case 10218:
				c.getPA().movePlayer(1957, 4371, 0);
				break;

			case 10219:
				c.getPA().movePlayer(1824, 4379, 3);
				break;

			case 10220:
				c.getPA().movePlayer(1824, 4381, 2);
				break;

			case 10221:
				c.getPA().movePlayer(1838, 4375, 2);
				break;

			case 10222:
				c.getPA().movePlayer(1838, 4377, 3);
				break;

			case 10223:
				c.getPA().movePlayer(1850, 4386, 1);
				break;

			case 10224:
				c.getPA().movePlayer(1850, 4387, 2);
				break;

			case 10225:
				c.getPA().movePlayer(1932, 4378, 1);
				break;

			case 10226:
				c.getPA().movePlayer(1932, 4380, 2);
				break;

			case 10227:
				if (c.getX() == 1961 && c.getY() == 4392)
					c.getPA().movePlayer(1961, 4392, 2);
				else
					c.getPA().movePlayer(1932, 4377, 1);
				break;

			case 10228:
				c.getPA().movePlayer(1961, 4393, 3);
				break;

			case 10229:
				c.getPA().movePlayer(1912, 4367, 0);
				break;

			/**
			 * Dagannoth king entrance
			 */
			case 10230:
				c.getPA().movePlayer(2899, 4449, 4);
				break;

			case 8958:
				//check if tile is occupied lol OR if ground item exists hahahaha

		if (c.getX() >= 2491) {

			//c.getPA().sendPlayerObjectAnimation(2491, 10162, 2349, 10, 1);

			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (c.getAgilityHandler().hotSpot(c, obX, obY)) {
						c.getAgilityHandler().move(c, obX-1, obY, 0x333, -1);
						container.stop();
					}

					c.getAgilityHandler().move(c, obX, obY, 0x333, -1);
				//	c.setForceMovement(obX-3, obY, 0, 200, "WEST", 819);
				//	container.stop();
				}

				@Override
				public void onStopped() {

				}
			}, 2);
				} else {

			if (c.getY() == 10162) {//u are  clicking on it standing on southern one
				GroundItem item = Server.itemHandler.getGroundItem(c, 3695, 2490, 10164, c.heightLevel);
				boolean existsrock=false;
				if (item != null) {
					existsrock=true;

				}
				boolean playerexists=false;
				Optional<Player> playerOptional = PlayerHandler.getOptionalPlayerByCoords(2490,10164);
				if (playerOptional.isPresent()) {
					playerexists=true;
				}
				if (existsrock || playerexists ){
					c.getPA().sendPlayerObjectAnimation(2491, 10162, 2350, 10, 1);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							GlobalObject removeit = new GlobalObject(-1, 2491,10162, 0, 1, 10, 0);
							Server.getGlobalObjects().add(removeit);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10164, 0);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10163, 0);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10162, 0);
							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {

									GlobalObject additback = new GlobalObject(8958, 2491, 10162, 0, 1, 10, 0);
									Server.getGlobalObjects().add(additback);
									container.stop();
								}

								@Override
								public void onStopped() {

								}
							}, 15);
							//}
							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 5);
				} else {
					c.sendMessage("You need something heavy on the other tile to open the door.");
				}
			} else if (c.getY() == 10164) {//u are  clicking on it standing on northern one
				GroundItem item = Server.itemHandler.getGroundItem(c, 3695, 2490, 10162, c.heightLevel);
				boolean existsrock=false;
				if (item != null) {
					existsrock=true;

				}
				boolean playerexists=false;
				Optional<Player> playerOptional = PlayerHandler.getOptionalPlayerByCoords(2490,10162);
				if (playerOptional.isPresent()) {
					playerexists=true;
				}
				if (existsrock || playerexists ){
					c.getPA().sendPlayerObjectAnimation(2491, 10162, 2349, 10, 1);
					c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10164, 0);
					c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10163, 0);
					c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10162, 0);
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {

							GlobalObject removeit = new GlobalObject(-1, obX,obY, 0, 0, 0, 0);
							Server.getGlobalObjects().add(removeit);

							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {

									GlobalObject additback = new GlobalObject(8958, 2491, 10162, 0, 1, 0, 0);
									Server.getGlobalObjects().add(additback);

									container.stop();
								}

								@Override
								public void onStopped() {

								}
							}, 10);
							//}
							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 3);
					} else {
				c.sendMessage("You need something heavy on the other tile to open the door.");
			}
			}
		}
				//c.getPA().sendPlayerObjectAnimation(2491, 10162, 2348, 10, 1);
				break;
			case 8959:
				//check if tile is occupied lol OR if ground item exists hahahaha

				if (c.getX() >= 2491) {

					c.getPA().sendPlayerObjectAnimation(2491, 10146, 2349, 10, 1);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							c.setForceMovement(obX-2, obY, 0, 200, "WEST", 819);
							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 2);
				} else {

					if (c.getY() == 10146) {//u are  clicking on it standing on southern one
						GroundItem item = Server.itemHandler.getGroundItem(c, 3695, 2490, 10148, c.heightLevel);
						boolean existsrock=false;
						if (item != null) {
							existsrock=true;

						}
						boolean playerexists=false;
						Optional<Player> playerOptional = PlayerHandler.getOptionalPlayerByCoords(2490,10148);
						if (playerOptional.isPresent()) {
							playerexists=true;
						}
						if (existsrock || playerexists ){
							c.getPA().sendPlayerObjectAnimation(2491, 10146, 2350, 10, 1);

							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {

									GlobalObject removeit = new GlobalObject(-1, 2491,10146, 0, 1, 10, 0);
									Server.getGlobalObjects().add(removeit);
									c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10148, 0);
									c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10147, 0);
									c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10146, 0);
									CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
										@Override
										public void execute(CycleEventContainer container) {

											GlobalObject additback = new GlobalObject(8959, 2491, 10146, 0, 1, 10, 0);
											Server.getGlobalObjects().add(additback);
											container.stop();
										}

										@Override
										public void onStopped() {

										}
									}, 15);
									//}
									container.stop();
								}

								@Override
								public void onStopped() {

								}
							}, 5);
						} else {
							c.sendMessage("You need something heavy on the other tile to open the door.");
						}
					} else if (c.getY() == 10148) {//u are  clicking on it standing on northern one
						GroundItem item = Server.itemHandler.getGroundItem(c, 3695, 2490, 10146, c.heightLevel);
						boolean existsrock=false;
						if (item != null) {
							existsrock=true;

						}
						boolean playerexists=false;
						Optional<Player> playerOptional = PlayerHandler.getOptionalPlayerByCoords(2490,10146);
						if (playerOptional.isPresent()) {
							playerexists=true;
						}
						if (existsrock || playerexists ){
							c.getPA().sendPlayerObjectAnimation(2491, 10146, 2349, 10, 1);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10148, 0);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10147, 0);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10146, 0);
							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {

									GlobalObject removeit = new GlobalObject(-1, obX,obY, 0, 0, 0, 0);
									Server.getGlobalObjects().add(removeit);

									CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
										@Override
										public void execute(CycleEventContainer container) {

											GlobalObject additback = new GlobalObject(8958, 2491, 10146, 0, 1, 0, 0);
											Server.getGlobalObjects().add(additback);

											container.stop();
										}

										@Override
										public void onStopped() {

										}
									}, 10);
									//}
									container.stop();
								}

								@Override
								public void onStopped() {

								}
							}, 3);
						}
					}
				}
				break;
			case 8960:
				//check if tile is occupied lol OR if ground item exists hahahaha

				if (c.getX() >= 2491) {

					c.getPA().sendPlayerObjectAnimation(2491, 10130, 2349, 10, 1);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							c.setForceMovement(obX-2, obY, 0, 200, "WEST", 819);
							container.stop();
						}

						@Override
						public void onStopped() {

						}
					}, 2);
				} else {

					if (c.getY() == 10130) {//u are  clicking on it standing on southern one
						GroundItem item = Server.itemHandler.getGroundItem(c, 3695, 2490, 10132, c.heightLevel);
						boolean existsrock=false;
						if (item != null) {
							existsrock=true;

						}
						boolean playerexists=false;
						Optional<Player> playerOptional = PlayerHandler.getOptionalPlayerByCoords(2490,10132);
						if (playerOptional.isPresent()) {
							playerexists=true;
						}
						if (existsrock || playerexists ){
							c.getPA().sendPlayerObjectAnimation(2491, 10130, 2350, 10, 1);

							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {

									GlobalObject removeit = new GlobalObject(-1, 2491,10130, 0, 1, 10, 0);
									Server.getGlobalObjects().add(removeit);
									c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10132, 0);
									c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10131, 0);
									c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10130, 0);
									CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
										@Override
										public void execute(CycleEventContainer container) {

											GlobalObject additback = new GlobalObject(8960, 2491, 10130, 0, 1, 10, 0);
											Server.getGlobalObjects().add(additback);
											container.stop();
										}

										@Override
										public void onStopped() {

										}
									}, 15);
									//}
									container.stop();
								}

								@Override
								public void onStopped() {

								}
							}, 5);
						} else {
							c.sendMessage("You need something heavy on the other tile to open the door.");
						}
					} else if (c.getY() == 10132) {//u are  clicking on it standing on northern one
						GroundItem item = Server.itemHandler.getGroundItem(c, 3695, 2490, 10130, c.heightLevel);
						boolean existsrock=false;
						if (item != null) {
							existsrock=true;

						}
						boolean playerexists=false;
						Optional<Player> playerOptional = PlayerHandler.getOptionalPlayerByCoords(2490,10130);
						if (playerOptional.isPresent()) {
							playerexists=true;
						}
						if (existsrock || playerexists ){
							c.getPA().sendPlayerObjectAnimation(2491, 10130, 2349, 10, 1);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10132, 0);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10131, 0);
							c.getRegionProvider().removeClip(RegionProvider.NPC_TILE_FLAG,2491,10130, 0);
							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {

									GlobalObject removeit = new GlobalObject(-1, obX,obY, 0, 0, 0, 0);
									Server.getGlobalObjects().add(removeit);

									CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
										@Override
										public void execute(CycleEventContainer container) {

											GlobalObject additback = new GlobalObject(8960, 2491, 10130, 0, 1, 0, 0);
											Server.getGlobalObjects().add(additback);

											container.stop();
										}

										@Override
										public void onStopped() {

										}
									}, 10);
									//}
									container.stop();
								}

								@Override
								public void onStopped() {

								}
							}, 3);
						}
					}
				}
				break;
			case 26724:
				if (c.playerLevel[Skill.AGILITY.getId()] < 72) {
					c.sendMessage("You need an agility level of 72 to cross over this mud slide.");
					return;
				}
				if (c.getX() == 2427 && c.getY() == 9767) {
					c.getPA().movePlayer(2427, 9762);
				} else if (c.getX() == 2427 && c.getY() == 9762) {
					c.getPA().movePlayer(2427, 9767);
				}
				break;
			case 535:
				if (obX == 3722 && obY == 5798) {
					if (c.getMode().isIronmanType()) {
						c.getPA().movePlayer(3677, 5775, 4);
					} else {
						c.getPA().movePlayer(3677, 5775, 0);
					}
				}
				break;

			case 536:
				if (obX == 3678 && obY == 5775) {
					c.getPA().movePlayer(3723, 5798);
				}
				break;

			case 26720:
				if (obX == 2427 && obY == 9747) {
					if (c.getX() == 2427 && c.getY() == 9748) {
						c.getPA().movePlayer(2427, 9746);
					} else if (c.getX() == 2427 && c.getY() == 9746) {
						c.getPA().movePlayer(2427, 9748);
					}
				} else if (obX == 2420 && obY == 9750) {
					if (c.getX() == 2420 && c.getY() == 9751) {
						c.getPA().movePlayer(2420, 9749);
					} else if (c.getX() == 2420 && c.getY() == 9749) {
						c.getPA().movePlayer(2420, 9751);
					}
				} else if (obX == 2418 && obY == 9742) {
					if (c.getX() == 2418 && c.getY() == 9741) {
						c.getPA().movePlayer(2418, 9743);
					} else if (c.getX() == 2418 && c.getY() == 9743) {
						c.getPA().movePlayer(2418, 9741);
					}
				} else if (obX == 2357 && obY == 9778) {
					if (c.getX() == 2358 && c.getY() == 9778) {
						c.getPA().movePlayer(2356, 9778);
					} else if (c.getX() == 2356 && c.getY() == 9778) {
						c.getPA().movePlayer(2358, 9778);
					}
				} else if (obX == 2388 && obY == 9740) {
					if (c.getX() == 2389 && c.getY() == 9740) {
						c.getPA().movePlayer(2387, 9740);
					} else if (c.getX() == 2387 && c.getY() == 9740) {
						c.getPA().movePlayer(2389, 9740);
					}
				} else if (obX == 2379 && obY == 9738) {
					if (c.getX() == 2380 && c.getY() == 9738) {
						c.getPA().movePlayer(2378, 9738);
					} else if (c.getX() == 2378 && c.getY() == 9738) {
						c.getPA().movePlayer(2380, 9738);
					}
				}
				break;

			case 26721:
				if (obX == 2358 && obY == 9759) {
					if (c.getX() == 2358 && c.getY() == 9758) {
						c.getPA().movePlayer(2358, 9760);
					} else if (c.getX() == 2358 && c.getY() == 9760) {
						c.getPA().movePlayer(2358, 9758);
					}
				}
				if (obX == 2380 && obY == 9750) {
					if (c.getX() == 2381 && c.getY() == 9750) {
						c.getPA().movePlayer(2379, 9750);
					} else if (c.getX() == 2379 && c.getY() == 9750) {
						c.getPA().movePlayer(2381, 9750);
					}
				}
				break;

			case 154:
				if (obX == 2356 && obY == 9783) {
					if (c.playerLevel[Skill.SLAYER.getId()] < 93) {
						c.sendMessage("You need a slayer level of 93 to enter into this crevice.");
						return;
					}
					c.getPA().movePlayer(3748, 5761, 0);
				}
				break;
			case 47077:
	AgilityHandler.delayEmote(c, "CRAWL", 3423, 10193, 0, 2);
			break;
			case 47140:
				AgilityHandler.delayEmote(c, "CRAWL", 3358, 10316, 0, 2);
				break;
			case 9706:
				if (obX == 3104 && obY == 3956) {
					c.getPA().startLeverTeleport(3105, 3951, 0,0);
				}
				break;

			case 9707:
				if (obX == 3105 && obY == 3952) {
					c.getPA().startLeverTeleport(3105, 3956, 0,0);
				}
				break;
			case 3610:
				if (obX == 3550 && obY == 9694) {
					c.getPA().startTeleport(3565, 3308, 0, "modern", false);
					//c.getPA().movePlayer(3565, 3308, 0);
				}
				break;

			case 26504:
				if (c.absX == 2909 && c.absY == 5265) {
					c.getGodwars().enterBossRoom(God.SARADOMIN);
				}
				if (c.absX == 2907 && c.absY == 5265) {
					c.getPA().movePlayer(2909, 5265);
				}
				break;

			case 26505:
				if (c.absX == 2925 &&c.absY == 5333) {
					c.getGodwars().enterBossRoom(God.ZAMORAK);
				}
				if (c.absX == 2925 &&c.absY == 5331) {
					c.getPA().movePlayer(2925, 5333);
				}
				break;
			case 26503:
				if (c.absX == 2862 && c.absY == 5354) {
					c.getGodwars().enterBossRoom(God.BANDOS);
				}
				if (c.absX == 2864 && c.absY == 5354) {
					c.getPA().movePlayer(2862, 5354);
				}
				break;


			case 21578: // Stairs up
			case 10:
				if (!c.getRights().isOrInherits(Right.DIAMOND_CLUB)) {
					c.sendMessage("You must be an ULTRA DONATOR to enter the top floor.");
					return;
				}
				if (c.heightLevel == 0) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 3372, 9645, 1, 2);
				} else {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 3372, 9645, 0, 2);
				}
				break;
			case 26502:
				if (c.absX == 2839 && c.absY == 5294) {
					c.getGodwars().enterBossRoom(God.ARMADYL);
				}
				if (c.absX == 2839 && c.absY == 5296) {
					c.getPA().movePlayer(2839, 5294);
				}
				break;
				case 42967:
					//if()
//					if(c.absX < 2909)
//					c.getGodwars().enterBossRoom(God.ZAROS);
//					else
//						c.getPA().walkTo(-, -3);
					c.getGodwars().enterBossRoom(God.ZAROS);
					break;
			case 172:
			case 170:
				c.objectDistance = 3;
				c.objectXOffset = 3;
				c.objectYOffset = 3;
				new CrystalChest().roll(c);
				break;

			case 4873:
			case 26761:
				c.getPA().startLeverTeleport(3153, 3923, 0,0);
				c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.WILDERNESS_LEVER);
				break;
			case 2492:
			case 15638:
			case 7479:
				c.getPA().startTeleport(3088, 3504, 0, "modern", false);
				break;
			case 11803:
				if (!c.getRights().isOrInherits(Right.ONYX_CLUB)) {
					c.sendMessage("@red@You must be apart of the Onyx club to enter this Slayer dungeon!");
					break;
				} else
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 2372, 4231, 0, 2);
				c.sendMessage("<img=7> Welcome to the Onyx only slayer cave.");
				break;

			case 2156:
				AgilityHandler.delayEmote(c, "RABBIT_HOP", 2386, 4264, 0, 2);
				break;
			case 17387:
				if (c.getRights().isOrInherits(Right.ONYX_CLUB)) {
					AgilityHandler.delayEmote(c, "CLIMB_UP", 2056, 3578, 0, 2);
				}
				break;
			case 25824:
				c.facePosition(obX, obY);
				c.getDH().sendDialogues(40, -1);
				break;

			case 5097:
			case 21725:
				c.getPA().movePlayer(2636, 9510, 2);
				break;
			case 5098:
			case 21726:
				c.getPA().movePlayer(2636, 9517, 0);
				break;
			case 5094:
			case 21722:
				c.getPA().movePlayer(2643, 9594, 2);
				break;
			case 5096:
			case 21724:
				c.getPA().movePlayer(2649, 9591, 0);
				break;
			case 2320:
			case 23566:
				if (c.absY == 9964 || c.absY == 9963) {
					c.getPA().movePlayer(3120, 9970, 0);
				} else if (c.absY == 9969 || c.absY == 9970) {
					c.getPA().movePlayer(3120, 9963, 0);
				}
				break;
			case 26518:
				if (c.playerLevel[3] < 70) {
					c.sendMessage("You need at least 70 hp to traverse the icy river.");
	return;
				}
//				if(c.absY >= 5344){
//
//				}

				c.setForceMovement(obX,c.absY >= 5344 ? 5332  : 5345, 0, 50, "NORTH", 6993);//crawl thru

if(c.absY <= 5335){
	CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
		@Override
		public void execute(CycleEventContainer container) {
			//if (finalDoor_totheright != null) {
			if (c.getAgilityHandler().hotSpot(c, obX, 5345)) {
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("The evil of the place drains your prayer...");
				container.stop();
			}

		}

		@Override
		public void onStopped() {

		}
	}, 1);
}


//				if (player.hp() < 70 || player.skills().xpLevel(Skills.HITPOINTS) < 70) {
//					player.message("Without at least 70 Hitpoints, you would never survive the icy water."); // TODO correct message
//					return false;
//				}
//
//				if (player.tile().y >= 5344) {
//					Chain.bound(null).runFn(1, () -> {
//						// Go to the right spot if we're not there
//						if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
//							player.smartPathTo(obj.tile().transform(0, 0, 0));
//						}
//					}).then(1, () -> {
//						//   player.teleport(2885, 5343, 2);
//						player.graphic(68);
//						player.looks().render(6993, 6993, 6993, 6993, 6993, 6993, 6993);
//						player.getMovementQueue().clear();
//						player.getMovementQueue().interpolate(new Tile(2885,5332), MovementQueue.StepType.FORCED_WALK);
//					}).then(6, () -> {
//						player.looks().resetRender();
//						// player.teleport(2885, 5332, 2);
//						player.message("Dripping, you climb out of the water.");
//					});
//				} else {
//					Chain.bound(null).runFn(1, () -> {
//						// Go to the right spot if we're not there
//						if (!player.tile().equals(obj.tile().transform(0, 0, 0))) {
//							player.smartPathTo(obj.tile().transform(0, 0, 0));
//						}
//					}).then(1, () -> {
//
////                        player.teleport(2885, 5334, 2);
//						player.graphic(68);
//						player.looks().render(6993, 6993, 6993, 6993, 6993, 6993, 6993);
////                     //   player.getMovementQueue().step(player.tile().x, 5345, MovementQueue.StepType.FORCED_WALK);
//						player.getMovementQueue().clear();
//						player.getMovementQueue().interpolate(new Tile(2885,5345), MovementQueue.StepType.FORCED_WALK);
//					}).then(6, () -> {
//						//   player.teleport(2885, 5345, 2);
//						if (player.skills().level(Skills.PRAYER) > 0) {
//							player.message("Dripping, you climb out of the water.");
//							player.message("The extreme evil of this area leaves your Prayer drained.");
//							player.skills().setLevel(Skills.PRAYER, 0);
//							player.looks().resetRender();
//
//
//						} else {
//							player.message("Dripping, you climb out of the water.");
//						}
//					});
//				}
//				return true;

		break;
		//first rope to saradomin
			case 26561:
				if(!c.firstropesaradomingwd){
					if(!c.getItems().playerHasItem(ROPE, 1)) {
						c.sendMessage("You need a rope to go down there.");
						return;
					}
					c.getItems().deleteItem2(ROPE, 1);
					c.firstropesaradomingwd = true;
					c.sendMessage("You attach the rope to the rock.");
					Server.getGlobalObjects().checkGWD(c);
				}

				break;


			case 42931:
			case 42932:
				c.getPA().movePlayer(2883,5280,2);
				break;
			case 42840:
			case 42841:
				if(!c.hasunlockedfrozendoor){
					if(!c.getItems().playerHasItem(26356, 1)) {
						c.sendMessage("You need a @blu@Frozen key@bla@ to unlock this door.");
						return;
					}
					c.getItems().deleteItem2(26356, 1);
					c.hasunlockedfrozendoor = true;
					c.sendMessage("@blu@You have unlocked the Zarosian encampment.");
					return;
				}
				c.getPA().movePlayer(2902,5202,0);
				break;
			case 42933:

				PathFinder.getPathFinder().findRouteNoclip(c,c.absX<2862 ? 2861 : 2862, obY, true, 0, 0);

				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(container.getTotalTicks() > 30){
							//System.out.println("stopped.");
							container.stop();
							return;
						}
				//		System.out.println("here stil? total: "+container.getTotalTicks());
						if (c.getAgilityHandler().hotSpot(c,  c.absX<2862 ? 2861 : 2862,obY)) {
							c.getPA().object(42933, obX,obY, 1, 10);
							PathFinder.getPathFinder().findRouteNoclip(c,c.absX<2862 ? 2862 : 2861, obY, true, 0, 0);
							c.setNewWalkCmdIsRunning(false);
							container.stop();
							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {
									if(container.getTotalTicks() > 30){
									//	System.out.println("stopped.");
										container.stop();
										return;
									}
								//	System.out.println("here stil? total: "+container.getTotalTicks());
									c.getPA().object(42933, obX,obY, 0, 10);
									container.stop();


								}

							}, 3);

						}

					}

				}, 1);

				break;
			case 42934:

				PathFinder.getPathFinder().findRouteNoclip(c,c.absX<=2899 ? 2898 : 2900, obY, true, 0, 0);

				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(container.getTotalTicks() > 30){
							//System.out.println("stopped.");
							container.stop();
							return;
						}
						//		System.out.println("here stil? total: "+container.getTotalTicks());
						if (c.getAgilityHandler().hotSpot(c,  c.absX<=2899 ? 2898 : 2900,obY)) {
							c.getPA().object(42934, obX,obY, 1, 10);
							PathFinder.getPathFinder().findRouteNoclip(c,c.absX<=2899 ? 2900 : 2898, obY, true, 0, 0);
							c.setNewWalkCmdIsRunning(false);
							container.stop();
							CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
								@Override
								public void execute(CycleEventContainer container) {
									if(container.getTotalTicks() > 30){
										//	System.out.println("stopped.");
										container.stop();
										return;
									}
									//	System.out.println("here stil? total: "+container.getTotalTicks());
									c.getPA().object(42934, obX,obY, 2, 10);
									container.stop();


								}

							}, 3);

						}

					}

				}, 1);

				break;
			case 26374:
				if (c.playerLevel[16] < 70) {
					c.sendMessage("You need an Agility level of 70 to climb down.");
					return;
				}
				c.getPA().movePlayer(obX,obY+2,1);
				Server.getGlobalObjects().checkGWD(c);

				break;
			case 26562:
				if(!c.secondropesaradomingwd){
					if(!c.getItems().playerHasItem(ROPE, 1)) {
						c.sendMessage("You need a rope to go down there.");
						return;
					}
					c.getItems().deleteItem2(ROPE, 1);
					c.secondropesaradomingwd = true;
					c.sendMessage("You attach the rope to the rock.");
					Server.getGlobalObjects().checkGWD(c);
				}

				break;
			case 26380://armadyl
				String weaponName = ItemAssistant.getItemName(c.playerEquipment[Player.playerWeapon]).toLowerCase();
if(!weaponName.contains("bow")){
	c.sendMessage("You need to be wearing some sort of crossbow.");
	return;
}
if(!c.getItems().playerHasItem(MITH_GRAPPLE)){
	c.sendMessage("You need a @blu@Mithril grapple@bla@.");
	return;
}
				if (c.playerLevel[4] < 70) {
					c.sendMessage("You need a ranged level of 70 to pass this obstacle.");
					return;
				}
				c.startAnimation(6068);//grapple anim
				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						//if (finalDoor_totheright != null) {
						if (c.absY >= obY) {
							c.getPA().movePlayer(2871,5269,2);
						} else  {
							c.getPA().movePlayer(2871,5279,2);

						}
						c.startAnimation(-1);//lets reset the anim
						//}
						container.stop();
					}

				}, 5);

					break;

			case 26461://gwd room to bandos enclave
				if(c.absX > 2850)
				if (!(c.getItems().playerHasItem(2347) && c.playerLevel[c.playerStrength] >= 70)) {
					c.sendMessage("You need a hammer and 70 Strength to get past this door.");
					return;
				}
				c.getPA().object(26461, c.objectX, c.objectY, 1, 0);
				if(c.absX > 2850)
					c.getAgilityHandler().move(c, -1, 0, 819, -1);
				else
					c.getAgilityHandler().move(c, 1, 0, 819, -1);


				CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if (c.disconnected) {
							container.stop();
							return;
						}
						c.getPA().object(26461, c.objectX, c.objectY, 0, 0);

						container.stop();
					}

				}, 3);

				break;
			case 26760:
				if (c.absX == 3184 && c.absY == 3945) {
					c.getDH().sendDialogues(631, -1);
				} else if (c.absX == 3184 && c.absY == 3944) {
					c.getPA().movePlayer(3184, 3945, 0);
				}
				break;
			case 19206:
				//	if (c.absX == 1502 && c.absY == 3838) {
				//	c.getDH().sendDialogues(63100, -1);
				//	} else if (c.absX == 1502 && c.absY == 3840) {
				//		c.getPA().movePlayer(1502, 3838, 0);
				//	}
				break;
			case 9326:
				if (c.playerLevel[16] < 62) {
					c.sendMessage("You need an Agility level of 62 to pass this.");
					return;
				}
				if (c.absX < 2769) {
					c.getPA().movePlayer(2775, 10003, 0);
				} else {
					c.getPA().movePlayer(2768, 10002, 0);
				}
				break;
			case 4496:
			case 4494:
				if (c.heightLevel == 2) {
					c.getPA().movePlayer(3412, 3540, 1);
				} else if (c.heightLevel == 1) {
					c.getPA().movePlayer(3418, 3540, 0);
				}
				break;
			case 9319:
				if (c.heightLevel == 0)
					c.getPA().movePlayer(c.absX, c.absY, 1);
				else if (c.heightLevel == 1)
					c.getPA().movePlayer(c.absX, c.absY, 2);
				break;

			case 9320:
				if (c.heightLevel == 1)
					c.getPA().movePlayer(c.absX, c.absY, 0);
				else if (c.heightLevel == 2)
					c.getPA().movePlayer(c.absX, c.absY, 1);
				break;
			case 4493:
				if (c.heightLevel == 0) {
					c.getPA().movePlayer(c.absX - 5, c.absY, 1);
				} else if (c.heightLevel == 1) {
					c.getPA().movePlayer(c.absX + 5, c.absY, 2);
				}
				break;

			case 4495:
				if (c.heightLevel == 1 && c.absY > 3538 && c.absY < 3543) {
					c.getPA().movePlayer(c.absX + 5, c.absY, 2);
				} else {
					c.sendMessage("I can't reach that!");
				}
				break;
			case 2623:
				if (!c.getItems().playerHasItem(1590)) {
					c.sendMessage("You need a @blu@Dusty key@bla@ to open this door.");
					return;
				}
				c.getPA().object(2623, 2924,9803, 0, 0);
					PathFinder.getPathFinder().findRouteNoclip(c, c.absX>2923 ? 2923 : 2924, 9803, true, 0, 0);

					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (c.getAgilityHandler().hotSpot(c,  c.absX>2923 ? 2924 : 2923,9803)) {
								c.getPA().object(2623, 2924,9803, 0, 0);
								c.setNewWalkCmdIsRunning(false);
								container.stop();
							}

						}

					}, 1);

				break;
			case 15644:
			case 15641:
			case 24306:
			case 24309:
				if (c.heightLevel == 2) {
					// if(Boundary.isIn(c, WarriorsGuild.WAITING_ROOM_BOUNDARY) &&
					// c.heightLevel == 2) {
					c.getWarriorsGuild().handleDoor();

					return;
					// }
				}

				break;
			case 15653:
				if (c.absY == 3546) {
					if (c.absX == 2877)
						c.getPA().movePlayer(c.absX - 1, c.absY, 0);
					else if (c.absX == 2876)
						c.getPA().movePlayer(c.absX + 1, c.absY, 0);
					c.facePosition(obX, obY);
				}
				break;

			case 18987: // Kbd ladder
				c.getPA().movePlayer(3069, 10255, 0);
				break;
			case 1817:
				System.out.println("face: "+object.getFace()+" ");
								WorldObject lever = ClickObject.getObject(c, objectType,obX,obY);
				if (lever != null) {
				System.out.println("f: "+lever.getFace());
				}
				c.getPA().startLeverTeleport(3067, 10253, 0,lever.getFace());
				break;

			case 18988:
				c.getPA().movePlayer(3017, 3850, 0);
				break;

			case 24303:
				c.getPA().movePlayer(2840, 3539, 2);
				break;
			case 16673:


				if (obX == 3205 && obY == 3208)
					c.getPA().movePlayer(3205, 3209, 1);
				if (obX == 2898 && obY == 3428)
					c.getPA().movePlayer(2897, 3429, 0);
				break;
			case 16671:
				if (obX == 3204 && obY == 3207)
					c.getPA().movePlayer(3205,3209, 1);

				if (obX == 2839 && obY == 3537)
					c.getPA().movePlayer(2840, 3539, 1);

				if (obX == 2898 && obY == 3428)
					c.getPA().movePlayer(2898, 3427, 1);
				break;

			// Jewelery oven
//			case 2643:
//			case 14888:
//				if (!c.getItems().playerHasItem(Items.RING_MOULD) && !c.getItems().playerHasItem(Items.AMULET_MOULD)
//						&& !c.getItems().playerHasItem(Items.NECKLACE_MOULD)) {
//					if (c.getItems().playerHasItem(Items.BRACELET_MOULD)) {
//						BraceletMaking.craftBraceletDialogue(c);
//					}
//				} else {
//					JewelryMaking.mouldInterface(c);
//				}
//				break;

			case 878:
				c.getDH().sendDialogues(613, -1);
				break;

			case 1734:
				if (c.absY > 9000 && c.getPosition().inWild())
					c.getPA().movePlayer(3044, 3927, 0);
				break;
			case 2466:
				if (c.absY > 3920 && c.getPosition().inWild())
					c.getPA().movePlayer(1622, 3673, 0);
				break;
			case 2467:
				c.getPA().spellTeleport(2604, 3154, 0, false);
				c.sendMessage("This is the dicing area. Place a bet on designated hosts.");
				break;
			case 28851:// wcgate
				if (c.playerLevel[8] < 60) {
					c.sendMessage("You need a Woodcutting level of 60 to enter the Woodcutting Guild.");
					return;
				} else {
					c.getPA().movePlayer(1657, 3505, 0);
				}
				break;
			case 28852:// wcgate
				if (c.playerLevel[8] < 60) {
					c.sendMessage("You need a Woodcutting level of 60 to enter the Woodcutting Guild.");
					return;
				} else {
					c.getPA().movePlayer(1657, 3504, 0);
				}
				break;
			case 2309:
				if (c.getX() == 2998 && c.getY() == 3916) {
					c.getAgility().doWildernessEntrance(c, 2998, 3916, false);
				}
				if (c.absX == 2998 && c.absY == 3917) {
					c.getPA().movePlayer(2998, 3916, 0);
				}
				break;
			case 1766:
				if (c.getPosition().inWild() && c.absX == 3069 && c.absY == 10255) {
					c.getPA().movePlayer(3017, 3850, 0);
				}
				break;
			case 1765:
				if (c.getPosition().inWild() && c.absY >= 3847 && c.absY <= 3860) {
					c.getPA().movePlayer(3069, 10255, 0);
				}
				break;



			case 2114:
				if (Boundary.isIn(c, new Boundary(3433, 3536, 3438, 3539))) {
					c.getPA().movePlayer(3433, 3537, 1);
				}
				break;


			case 7108:
			case 7111:
				if (c.absX == 2907 || c.absX == 2908) {
					if (c.absY == 9698) {
						c.getPA().walkTo(0, -1);
					} else if (c.absY == 9697) {
						c.getPA().walkTo(0, +1);
					}
				}
				break;


//			case 2102:
//			case 2104:
//				if (c.heightLevel == 1) {
//					if (c.absX == 3426 || c.absX == 3427) {
//						if (c.absY == 3556) {
//							c.getPA().walkTo(0, -1);
//						} else if (c.absY == 3555) {
//							c.getPA().walkTo(0, +1);
//						}
//					}
//				}
//				break;

			case 1597:
			case 1596:
				// case 7408:
				// case 7407:
				if (c.absY < 9000) {
					if (c.absY > 3903) {
						c.getPA().movePlayer(c.absX, c.absY - 1, 0);
					} else {
						c.getPA().movePlayer(c.absX, c.absY + 1, 0);
					}
				} else if (c.absY > 9917) {
					c.getPA().movePlayer(c.absX, c.absY - 1, 0);
				} else {
					c.getPA().movePlayer(c.absX, c.absY + 1, 0);
				}
				break;

			case 24600:
				c.getDH().sendDialogues(500, -1);
				break;

			case 14315:
				PestControl.addToLobby(c);
				break;

			case 14314:
				PestControl.removeFromLobby(c);
				break;

			case 14235:
			case 14233:
				if (c.objectX == 2670) {
					if (c.absX <= 2670) {
						c.absX = 2671;
					} else {
						c.absX = 2670;
					}
				}
				if (c.objectX == 2643) {
					if (c.absX >= 2643) {
						c.absX = 2642;
					} else {
						c.absX = 2643;
					}
				}
				if (c.absX <= 2585) {
					c.absY += 1;
				} else {
					c.absY -= 1;
				}
				c.updateController(); // Doing this because above it manually sets x/y coordinate
				c.getPA().movePlayer(c.absX, c.absY, 0);
				break;

			case 245:
				c.getPA().movePlayer(c.absX, c.absY + 2, 2);
				break;
			case 246:
				c.getPA().movePlayer(c.absX, c.absY - 2, 1);
				break;
			case 272:
				if (c.absY == 3956 || c. absY == 3957) {
					c.getPA().movePlayer(3018, 3958, 1);
					break;
				} else {
					c.getPA().movePlayer(c.absX, c.absY, 1);
				}
				break;
			case 273:
				if (c.absY == 3956 || c. absY == 3957) {
					c.getPA().movePlayer(3018, 3958, 0);
				} else {
					c.getPA().movePlayer(c.absX, c.absY, 0);
				}
				break;
			/* Godwars Door */
			/*
			 * case 26426: // armadyl if (c.absX == 2839 && c.absY == 5295) {
			 * c.getPA().movePlayer(2839, 5296, 2);
			 * c.sendMessage("@blu@May the gods be with you."); } else {
			 * c.getPA().movePlayer(2839, 5295, 2); } break; case 26425: // bandos if
			 * (c.absX == 2863 && c.absY == 5354) { c.getPA().movePlayer(2864, 5354, 2);
			 * c.sendMessage( "@blu@May the gods be with you."); } else {
			 * c.getPA().movePlayer(2863, 5354, 2); } break; case 26428: // bandos if
			 * (c.absX == 2925 && c.absY == 5332) { c.getPA().movePlayer(2925, 5331, 2);
			 * c.sendMessage("@blu@May the gods be with you."); } else {
			 * c.getPA().movePlayer(2925, 5332, 2); } break; case 26427: // bandos if
			 * (c.absX == 2908 && c.absY == 5265) { c.getPA().movePlayer(2907, 5265, 0);
			 * c.sendMessage("@blu@May the gods be with you."); } else {
			 * c.getPA().movePlayer(2908, 5265, 0); } break;
			 */

			case 5960://lever at magebank
				c.getPA().startLeverTeleport(3090, 3956, 0,0);
				break;
			case 5959:
			if (c.absX != 3089) {
					c.getPA().startLeverTeleport(2539, 4712, 0,0);
				}
				break;
			case 1814:
				if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
					c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.WILDERNESS_LEVER);
				}
				c.getPA().startLeverTeleport(3153, 3923, 0,0);
				break;
			case 1535:
				if (c.absX == 2564 && c.absY == 3310) {
					c.getPA().movePlayer(2563, 3310, 0);
				} else if (c.absX == 2563 && c.absY == 3310) {
					c.getPA().movePlayer(2674, 9479, 0);
				}
				break;
			case 14929:
				c.getPA().movePlayer(2712, 3472, 3);
			case 1815:
				c.getPA().startLeverTeleport(2564, 3310, 0,0);
				break;
			case 1816:
			//	System.out.println("face: "+object.getFace()+" ");
		lever = ClickObject.getObject(c, objectType,obX,obY);
				if (lever != null) {
					System.out.println("f: "+lever.getFace());
				}

				c.getPA().startLeverTeleport(2271, 4680, 0,lever.getFace());
				c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.KBD_LAIR);
				break;
			/* Start Brimhavem Dungeon */
			case 5083:
				c.getPA().movePlayer(2713, 9564, 0);
				c.sendMessage("You enter the dungeon.");
				break;

			case 5103:
				if (c.absX == 2691 && c.absY == 9564) {
					c.getPA().movePlayer(2689, 9564, 0);
				} else if (c.absX == 2689 && c.absY == 9564) {
					c.getPA().movePlayer(2691, 9564, 0);
				}
				break;

			case 5106:
			case 21734:
				if (c.absX == 2674 && c.absY == 9479) {
					c.getPA().movePlayer(2676, 9479, 0);
				} else if (c.absX == 2676 && c.absY == 9479) {
					c.getPA().movePlayer(2674, 9479, 0);
				}
				break;
			case 5105:
			case 21733:
				if (c.absX == 2672 && c.absY == 9499) {
					c.getPA().movePlayer(2674, 9499, 0);
				} else if (c.absX == 2674 && c.absY == 9499) {
					c.getPA().movePlayer(2672, 9499, 0);
				}
				break;

			case 5107:
			case 21735:
				if (c.absX == 2693 && c.absY == 9482) {
					c.getPA().movePlayer(2695, 9482, 0);
				} else if (c.absX == 2695 && c.absY == 9482) {
					c.getPA().movePlayer(2693, 9482, 0);
				}
				break;

			case 21731:
				if (c.absX == 2691) {
					c.getPA().movePlayer(2689, 9564, 0);
				} else if (c.absX == 2689) {
					c.getPA().movePlayer(2691, 9564, 0);
				}
				break;

			case 5104:
			case 21732:
				if (c.absX == 2683 && c.absY == 9568) {
					c.getPA().movePlayer(2683, 9570, 0);
				} else if (c.absX == 2683 && c.absY == 9570) {
					c.getPA().movePlayer(2683, 9568, 0);
				}
				break;

			case 5100:
				if (c.absY <= 9567) {
					c.getPA().movePlayer(2655, 9573, 0);
				} else if (c.absY >= 9572) {
					c.getPA().movePlayer(2655, 9566, 0);
				}
				break;
			case 21728:
				if (c.playerLevel[16] < 34) {
					c.sendMessage("You need an Agility level of 34 to pass this.");
					return;
				}
				if (c.absY == 9566) {
					AgilityHandler.delayEmote(c, "CRAWL", 2655, 9573, 0, 2);
				} else {
					AgilityHandler.delayEmote(c, "CRAWL", 2655, 9566, 0, 2);
				}
				break;

			case 5099:
			case 21727:
				if (c.playerLevel[16] < 34) {
					c.sendMessage("You need an Agility level of 34 to pass this.");
					return;
				}
				if (c.objectX == 2698 && c.objectY == 9498) {
					c.getPA().movePlayer(2698, 9492, 0);
				} else if (c.objectX == 2698 && c.objectY == 9493) {
					c.getPA().movePlayer(2698, 9499, 0);
				}
				break;
			case 5088:
			case 20882:
				if (c.playerLevel[16] < 30) {
					c.sendMessage("You need an Agility level of 30 to pass this.");
					return;
				}
				c.getPA().movePlayer(2687, 9506, 0);
				break;

			case 6097:
				c.getPA().showInterface(22931);
				break;
			case 5090:
			case 20884:
				if (c.playerLevel[16] < 30) {
					c.sendMessage("You need an Agility level of 30 to pass this.");
					return;
				}
				c.getPA().movePlayer(2682, 9506, 0);
				break;

			case 16511:
				if (c.playerLevel[16] < 51) {
					c.sendMessage("You need an agility level of at least 51 to squeeze through.");
					return;
				}
				if (c.absX == 3149 && c.absY == 9906) {
					c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.OBSTACLE_PIPE);
					c.getPA().movePlayer(3155, 9906, 0);
				} else if (c.absX == 3155 && c.absY == 9906) {
					c.getPA().movePlayer(3149, 9906, 0);
				}
				break;

		//	case 5110:
			case 21738:
				if (c.playerLevel[16] < 12) {
					c.sendMessage("You need an Agility level of 12 to pass this.");
					return;
				}
				c.getPA().movePlayer(2647, 9557, 0);
				break;
			//case 5111:
			case 21739:
				if (c.playerLevel[16] < 12) {
					c.sendMessage("You need an Agility level of 12 to pass this.");
					return;
				}
				c.getPA().movePlayer(2649, 9562, 0);
				break;
			case 27362:// lizardmen
				if (c.absY > 3688) {
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1454, 3690, 0, 2);
					c.sendMessage("You climb down into Shayzien Assault.");
				} else
					AgilityHandler.delayEmote(c, "CLIMB_DOWN", 1477, 3690, 0, 2);
				c.sendMessage("You climb down into Lizardman Camp.");
				break;
			case 4155:// zulrah
				c.getPA().movePlayer(2200, 3055, 0);
				c.sendMessage("You climb down.");
				break;
			case 4152:
				c.start(new SkillingPortalDialogue(c));
				break;
			case 5084:
				c.getPA().movePlayer(2744, 3151, 0);
				c.sendMessage("You exit the dungeon.");
				break;
			/* End Brimhavem Dungeon */
			case 6481:
				c.getPA().movePlayer(3233, 9315, 0);
				break;

			case 1553:
				if (c.absX == 3252 && c.absY == 3267) {
					c.getPA().movePlayer(3253, 3266, 0);
				}
				if (c.absX == 3253 && c.absY == 3267) {
					c.getPA().movePlayer(3252, 3266, 0);
				}
				break;
			case 3044:
			case 24009:
			case 26300:
			case 16469:
			case 14838:
			case 2030:
				c.objectDistance = 1;
				if (CannonballSmelting.isSmeltingCannonballs(c)) {
					CannonballSmelting.smelt(c);
				} else {
					c.getSmithing().sendSmelting();
				}
				break;
			/*
			 * case 2030: if (c.absX == 1718 && c.absY == 3468) {
			 * c.getSmithing().sendSmelting(); } else { c.getSmithing().sendSmelting(); }
			 * break;
			 */
			case 881:
				/**
				 * Varrock sewers replacement object
				 */
				worldObject.replace(882);
			break;
						case 882:
							AgilityHandler.delayEmote(c, "CLIMB_UP", 3237, 9859, 0, 2);

							break;
			case 11806:
				/**
				 * Exit varrock sewers
				 */
				AgilityHandler.delayEmote(c, "CLIMB_UP", 3236, 3458, 0, 2);
				break;

			/* AL KHARID */
			case 2883:
			case 2882:
				c.getDH().sendDialogues(1023, 925);
				break;
			// case 2412:
			// Sailing.startTravel(c, 1);
			// break;
			// case 2414:
			// Sailing.startTravel(c, 2);
			// break;
			// case 2083:
			// Sailing.startTravel(c, 5);
			// break;
			// case 2081:
			// Sailing.startTravel(c, 6);
			// break;
			// case 14304:
			// Sailing.startTravel(c, 14);
			// break;
			// case 14306:
			// Sailing.startTravel(c, 15);
			// break;

			case 2213:
			case 24101:
			case 3045:
			case 14367:
			case 3193:
			case 10517:
			case 11402:
			case 26972:
			case 4483:
			case 25808:
			case 11744:
			case 12309:
			case 10058:
			case 2693:
			case 21301:
			case 6943:
			case 3194:
			case 46223:
				c.getPA().c.itemAssistant.openUpBank();
				break;
			case 13287:
				if (!c.getMode().isBankingPermitted() && (c.getItems().playerHasItem(8868)) && !c.unlockedUltimateChest) {
					c.getItems().deleteItem2(8868, 1);
					c.unlockedUltimateChest = true;
					PlayerSave.saveGame(c);
					c.sendMessage("You have permanently unlocked the UIM storage chest.");
				}
				if (!c.getMode().isBankingPermitted()) {
					c.inUimBank = true;
					c.getItems().deleteItem2(8866, 1);
					c.inBank = true;
					c.getPA().c.itemAssistant.openUpBank();
				} else {
					c.sendMessage("This bank is only for Ultimate Ironman to use.");
				}
				break;

			case 4004:
				c.getWogwContributeInterface().open();
				break;
			case 3506:

				WorldObject gate3506 = ClickObject.getObject(c, objectType,obX,obY);
				face = gate3506.getFace();
				if(face == 3){//this means its "_ _" oriented gate
					PathFinder.getPathFinder().findRouteNoclip(c, obX, c.absY >= obY ? obY : obY-1, true, 0, 0);
					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (c.getAgilityHandler().hotSpot(c, obX, c.absY >= obY ? obY : obY-1)) {
								c.getPA().object(3506, obX-1, obY, 0, 0);
								c.getPA().object(3507, obX, obY, 2, 0);
								PathFinder.getPathFinder().findRouteNoclip(c, obX ,c.absY >= obY ? obY-1 : obY, true, 0, 0);
								c.setNewWalkCmdIsRunning(false);
								//todo: stop all containers like this if you are too far far from the obx, oby
								CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer container) {
										c.getPA().object(3507, obX-1, obY, 3, 0);
										c.getPA().object(3506, obX, obY, 3, 0);
										container.stop();

									}

									@Override
									public void onStopped() {

									}
								}, 3);
								container.stop();
							}

						}

						@Override
						public void onStopped() {

						}
					}, 1);
				}

				break;
			case 3507:

				WorldObject gate3507 = ClickObject.getObject(c, objectType,obX,obY);
				face = gate3507.getFace();
				if(face == 3){//this means its "_ _" oriented gate
					//if(c.absY >= obY){
						PathFinder.getPathFinder().findRouteNoclip(c, obX, c.absY >= obY ? obY : obY-1, true, 0, 0);
//					}
//					else {
//						PathFinder.getPathFinder().findRouteNoclip(c, obX, obY+1, true, 0, 0);
//					}



					CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
						@Override
						public void execute(CycleEventContainer container) {
							if (c.getAgilityHandler().hotSpot(c, obX, c.absY >= obY ? obY : obY-1)) {
								c.getPA().object(3506, obX, obY, 0, 0);
								c.getPA().object(3507, obX+1, obY, 2, 0);
								PathFinder.getPathFinder().findRouteNoclip(c, obX ,c.absY >= obY ? obY-1 : obY, true, 0, 0);
								c.setNewWalkCmdIsRunning(false);
								CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
									@Override
									public void execute(CycleEventContainer container) {
										c.getPA().object(3507, obX, obY, 3, 0);
										c.getPA().object(3506, obX+1, obY, 3, 0);
										container.stop();

									}

									@Override
									public void onStopped() {

									}
								}, 3);
								container.stop();
							}

						}

						@Override
						public void onStopped() {

						}
					}, 1);
				}

				break;
			case 11665:
				if (c.absX == 2658)
					c.getPA().movePlayer(2659, 3437, 0);
				c.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.RANGING_GUILD);
				if (c.absX == 2659)
					c.getPA().movePlayer(2657, 3439, 0);
				break;

			/**
			 * Entering the Fight Caves.
			 */
			case 11833:
				if (Boundary.getPlayersInBoundary(Boundary.FIGHT_CAVE) >= 50) {
					c.sendMessage("There are too many people using the fight caves at the moment. Please try again later");
					return;
				}
				c.getDH().sendDialogues(633, -1);
				break;
			case 38003:
				if (Boundary.getPlayersInBoundary(Boundary.TOKKUL_PIT1) >= 50) {
					c.sendMessage("There are too many people using the Tokkul pits at the moment. Please try again later");
					return;
				}
				c.getDH().sendDialogues(950, -1);
				break;


			/**
			 * Clicking on the Ancient Altar.
			 */
			case 6552:
				if (c.getPosition().inWild()) {
					return;
				}
				c.autocasting = false;
				c.autocastId = -1;
				c.getPA().resetAutocast();
				if (c.absY == 9312) {
				}
				PlayerAssistant.switchSpellBook(c);
				break;

			/**
			 * c.setSidebarInterface(6, 1151); Recharing prayer points.
			 */
			case 20377:

				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {

					c.startAnimation(645);
					c.playerLevel[5] = (c.getPA().getLevelForXP(c.playerXP[5]) + (int)(c.getPA().getLevelForXP(c.playerXP[5]) * 0.15));
					c.sendMessage("You recharge your prayer points.");
					c.getPA().refreshSkill(5);
					c.getPA().sendSound(2674);
				} else {
					c.sendMessage("You already have full prayer points.");
				}
				break;
			case 61:
				if (c.getPosition().inWild()) {
					return;
				}
				if (c.absY >= 3508 && c.absY <= 3513) {
					if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
						if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)
								&& c.getDiaryManager().getVarrockDiary().hasCompleted("HARD")) {
							if (c.prayerActive[25]) {
								c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.PRAY_WITH_PIETY);
							}
						}
						c.startAnimation(645);
						c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
						c.sendMessage("You recharge your prayer points.");
						c.getPA().refreshSkill(5);
						c.getPA().sendSound(2674);
					} else {
						c.sendMessage("You already have full prayer points.");
					}
				}
				break;
			case 29776:
//				if (Raids.isMissingRequirements(c)) {
//					return;
//				}
////so IF YOU CREATE YOUR OWN PARTY
//				if (c.inParty(CoxParty.TYPE)) {
//					c.getParty().openStartActivityDialogue(c, "Chambers of Xeric", Boundary.RAIDS_LOBBY_ENTRANCE::in, list -> new Raids().startRaid(list, true));
//					return;
//				}
////or you can join the lobby :)
//				if (Boundary.isIn(c, Boundary.RAIDS_LOBBY_ENTRANCE)) {
//					LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC).ifPresent(lobby -> lobby.attemptJoin(c));
//					return;
//				}
//				if  (Boundary.isIn(c, Boundary.RAIDS_LOBBY)) {
//					LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC)
//							.ifPresent(lobby -> lobby.attemptLeave(c));
//					c.getPA().movePlayer(1247, 3559, 0);
//					return;
//				}
//				LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC)
//						.ifPresent(lobby -> lobby.attemptJoin(c));
//
//				//c.sendMessage("Please wait for next update as we are reworking Olm.");
//				break;
//			case 44596:
//				if (Raids3.isMissingRequirements(c)) {
//					return;
//				}
//
//				if (c.inParty(Raids3Party.TYPE)) {
//					c.getParty().openStartActivityDialogue(c, "Chambers of Xeric", Boundary.RAIDS3_LOBBY_ENTRANCE::in, list -> new Raids3().startRaids3(list, true));
//					return;
//				}
//
//				if (Boundary.isIn(c, Boundary.RAIDS3_LOBBY_ENTRANCE)) {
//					LobbyManager.get(LobbyType.RAIDS_3).ifPresent(lobby -> lobby.attemptJoin(c));
//					return;
//				}
//				if  (Boundary.isIn(c, Boundary.RAIDS3_LOBBY)) {
//					LobbyManager.get(LobbyType.RAIDS_3)
//							.ifPresent(lobby -> lobby.attemptLeave(c));
//					c.getPA().movePlayer(1247, 3559, 0);
//					return;
//				}
//				LobbyManager.get(LobbyType.RAIDS_3)
//						.ifPresent(lobby -> lobby.attemptJoin(c));
//
//				//c.sendMessage("Please wait for next update as we are reworking Olm.");
//				break;
			case 29777:
				if (Raids.isMissingRequirements(c)) {
					return;
				}
//so IF YOU CREATE YOUR OWN PARTY
				if (c.inParty(CoxParty.TYPE)) {
					c.getParty().openStartActivityDialogue(c, "Chambers of Xeric", Boundary.RAIDS_LOBBY_ENTRANCE::in, list -> new Raids().startRaid(list, true));
					return;
				}
//or you can join the lobby :)
				if (Boundary.isIn(c, Boundary.RAIDS_LOBBY_ENTRANCE)) {
					LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC).ifPresent(lobby -> lobby.attemptJoin(c));
					return;
				}
				if  (Boundary.isIn(c, Boundary.RAIDS_LOBBY)) {
					LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC)
							.ifPresent(lobby -> lobby.attemptLeave(c));
					c.getPA().movePlayer(1247, 3559, 0);
					return;
				}
				LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC)
						.ifPresent(lobby -> lobby.attemptJoin(c));

				//c.sendMessage("Please wait for next update as we are reworking Olm.");
				break;
					//break;
			case 30396: //Raids Lobbies
//				if (Boundary.isIn(c, Boundary.XERIC_LOBBY_ENTRANCE)) {
//					LobbyManager.get(LobbyType.TRIALS_OF_XERIC)
//							.ifPresent(lobby -> lobby.attemptJoin(c));
//					c.getPA().movePlayer(3033, 6060, 0);
//
//					break;
//				}
//				if  (Boundary.isIn(c, Boundary.XERIC_LOBBY)) {
//					LobbyManager.get(LobbyType.TRIALS_OF_XERIC)
//							.ifPresent(lobby -> lobby.attemptLeave(c));
//					c.getPA().movePlayer(1247, 3559, 0);
//					break;
//				}
//				if (Boundary.isIn(c, Boundary.RAIDS_LOBBY_ENTRANCE)) {
//					LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC)
//							.ifPresent(lobby -> lobby.attemptJoin(c));
//					break;
//				}
//				if  (Boundary.isIn(c, Boundary.RAIDS_LOBBY)) {
//					LobbyManager.get(LobbyType.CHAMBERS_OF_XERIC)
//							.ifPresent(lobby -> lobby.attemptLeave(c));
//					break;
//				}
				System.out.println("LOBBY OBJECT JOIN FAILURE! NO CONDITION MET!");
				c.sendMessage("This Lobby is not yet in use! New minigame coming soon!");
				break;

			case 410:
				if (c.getPosition().inWild()) {
					return;
				}
				if (c.playerLevel[5] == c.getPA().getLevelForXP(c.playerXP[5])) {
					c.sendMessage("You already have full prayer points.");
					return;
				}
				if (Boundary.isIn(c, Boundary.TAVERLY_BOUNDARY)) {
					if (c.getItems().isWearingItem(5574) && c.getItems().isWearingItem(5575)
							&& c.getItems().isWearingItem(5576)) {
						c.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.ALTAR_OF_GUTHIX);
					}
				}
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().sendSound(2674);
				c.getPA().refreshSkill(5);
				break;
			case 29941:
			case 27501:
				if (c.getPosition().inWild()) {
					return;
				}
				if (c.playerLevel[5] == c.getPA().getLevelForXP(c.playerXP[5])) {
					c.sendMessage("You already have full prayer points.");
					return;
				}
				if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)) {
					if (c.prayerActive[23]) {
						c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.PRAY_WITH_SMITE);
					}
				}
				if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
					if (c.prayerActive[25]) {
//						if (!c.getDiaryManager().getArdougneDiary().hasCompleted("MEDIUM")) {
//							c.sendMessage("You must have completed all the medium tasks in the Ardougne diary to do this.");
//							return;
//						}
						c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.PRAY_WITH_CHIVALRY);
					}
				}
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().sendSound(2674);
				c.getPA().refreshSkill(5);
				c.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.PRAY_AT_ALTAR);
				break;
			case 409:
			case 7812:
			case 6817:
			case 14860:
				if (c.getPosition().inWild()) {
					return;
				}
				if (c.playerLevel[5] == c.getPA().getLevelForXP(c.playerXP[5])) {
					c.sendMessage("You already have full prayer points.");
					return;
				}
				if (Boundary.isIn(c, Boundary.VARROCK_BOUNDARY)) {
					if (c.prayerActive[23]) {
						c.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.PRAY_WITH_SMITE);
					}
				}
				if (Boundary.isIn(c, Boundary.ARDOUGNE_BOUNDARY)) {
					if (c.prayerActive[25]) {
						if (!c.getDiaryManager().getArdougneDiary().hasCompleted("MEDIUM")) {
							c.sendMessage("You must have completed all the medium tasks in the ardougne diary to do this.");
							return;
						}
						c.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.PRAY_WITH_CHIVALRY);
					}
				}
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().sendSound(2674);
				c.getPA().refreshSkill(5);
				break;

			case 411:
				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
					if (c.getPosition().inWild()) {
						c.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.WILDERNESS_ALTAR);
					}
					c.startAnimation(645);
					c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
					c.sendMessage("You recharge your prayer points.");
					c.getPA().sendSound(2674);
					c.getPA().refreshSkill(5);
				} else {
					c.sendMessage("You already have full prayer points.");
				}
				break;

			case 14896:
				c.facePosition(obX, obY);
				FlaxPicking.getInstance().pick(c, new Location3D(obX, obY, c.heightLevel));
				break;

			case 412:
				if (c.getPosition().inWild()) {
					return;
				}
				if (c.getMode().isIronmanType()) {
					c.sendMessage("Your game mode prohibits use of this altar.");
					return;
				}
				// if (c.absY >= 3504 && c.absY <= 3507) {
//				if (c.specAmount < 10.0) {
//					if (c.specRestore > 0) {
//						int seconds = ((int) Math.floor(c.specRestore * 0.6));
//						c.sendMessage("You have to wait another " + seconds + " seconds to use this altar.");
//						return;
//					}
//					if (c.getRights().isOrInherits(Right.ONYX_CLUB)) {
//						c.specRestore = 120;
//						c.specAmount = 10.0;
//						c.getItems().addSpecialBar(c.playerEquipment[Player.playerWeapon]);
//						c.sendMessage("Your special attack has been restored. You can restore it again in 3 minutes.");
//					} else {
//						c.specRestore = 240;
//						c.specAmount = 10.0;
//						c.getItems().addSpecialBar(c.playerEquipment[Player.playerWeapon]);
//						c.sendMessage("Your special attack has been restored. You can restore it again in 6 minutes.");
//					}
//				}
				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
					c.startAnimation(645);
					c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
					c.sendMessage("You recharge your prayer points.");
					c.getPA().sendSound(2674);
					c.getPA().refreshSkill(5);
				} else {
					c.sendMessage("You already have full prayer points.");
				}
				// }
				break;

			case 26366: // Godwars altars
			case 26365:
			case 26364:
			case 26363:
				if (c.getPosition().inWild()) {
					return;
				}
				if (c.gwdAltarTimer > 0) {
					int seconds = ((int) Math.floor(c.gwdAltarTimer * 0.6));
					c.sendMessage("You have to wait another " + seconds + " seconds to use this altar.");
					return;
				}
				if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
					c.startAnimation(645);
					c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
					c.sendMessage("You recharge your prayer points.");
					c.getPA().sendSound(2674);
					c.gwdAltarTimer = 600;
					c.getPA().refreshSkill(5);
				} else {
					c.sendMessage("You already have full prayer points.");
				}
				break;


			/**
			 * Obelisks in the wilderness.
			 */
			case 14829:
			case 14830:
			case 14827:
			case 14828:
			case 14826:
			case 14831:

				break;

			/**
			 * Clicking certain doors.
			 */
			case 1516:
			case 1519:
				if (c.objectY == 9698) {
					if (c.absY >= c.objectY)
						c.getPA().walkTo(0, -1);
					else
						c.getPA().walkTo(0, 1);
					break;
				}

			case 11737:
				if (!c.getRights().isOrInherits(Right.ONYX_CLUB)) {
					return;
				}
				c.getPA().movePlayer(3365, 9641, 0);
				break;


			case 5126:
			case 2100:
				if (c.absY == 3554)
					c.getPA().walkTo(0, 1);
				else
					c.getPA().walkTo(0, -1);
				break;

			case 1759:
				if (c.objectX == 2884 && c.objectY == 3397)
					c.getPA().movePlayer(c.absX, c.absY + 6400, 0);
				break;
			case 1557:
			case 7169:
				if ((c.objectX == 3106 || c.objectX == 3105) && c.objectY == 9944) {
					if (c.getY() > c.objectY)
						c.getPA().walkTo(0, -1);
					else
						c.getPA().walkTo(0, 1);
				} else {
					if (c.getX() > c.objectX)
						c.getPA().walkTo(-1, 0);
					else
						c.getPA().walkTo(1, 0);
				}
				break;
			case 2558:
				c.sendMessage("This door is locked.");
				break;

			case 9294:
				if (c.absX < c.objectX) {
					c.getPA().movePlayer(c.objectX + 1, c.absY, 0);
				} else if (c.absX > c.objectX) {
					c.getPA().movePlayer(c.objectX - 1, c.absY, 0);
				}
				break;

			case 9293:
				if (c.absX < c.objectX) {
					c.getPA().movePlayer(2892, 9799, 0);
				} else {
					c.getPA().movePlayer(2886, 9799, 0);
				}
				break;

			case 10529:
			case 10527:
				if (c.absY <= c.objectY)
					c.getPA().walkTo(0, 1);
				else
					c.getPA().walkTo(0, -1);
				break;
			case 30176:
				AgilityHandler.delayEmote(c, "CRAWL", 2404,9415,0, 2);
				break;
			case 534:
				AgilityHandler.delayEmote(c, "CRAWL", 2412,3060,0, 2);
				break;
			case 34858:
				if (c.absY > obX){
					new DialogueBuilder(c).statement("@red@You will not be able to exit back once you enter.").option(
							new DialogueOption("Enter", plr -> plr.getPA().walkTo(0, -1)),
							new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
					).send();//
			} else {

				}



				c.sendMessage("You manage your way through the web.");
//				if (c.absY == 9912)
//					c.getPA().walkTo(0, -1);
//				else if (c.absY == 9911)
//					c.getPA().walkTo(0, 1);
				break;

			case 34898:
			case SpiderWeb.OBJECT_ID:
				SpiderWeb.slash(c, object);
				break;

//			case 7407:
//				GlobalObject gate;
//				gate = new GlobalObject(objectType, obX, obY, c.heightLevel, 2, 0, 50, 7407);
//				Server.getGlobalObjects().add(gate);
//				break;
//
//			case 7408:
//				GlobalObject secondGate;
//				secondGate = new GlobalObject(objectType, obX, obY, c.heightLevel, 0, 0, 50, 7408);
//				Server.getGlobalObjects().add(secondGate);
//				break;

			/**
			 * Forfeiting a duel.
			 */
			case 3203:
				DuelSession session = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(c,
						MultiplayerSessionType.DUEL);
				if (Objects.isNull(session)) {
					return;
				}
				if (!Boundary.isIn(c, Boundary.DUEL_ARENA)) {
					return;
				}
				if (session.getRules().contains(Rule.FORFEIT)) {
					c.sendMessage("You are not permitted to forfeit the duel.");
					return;
				}
				break;

		}
	}

}