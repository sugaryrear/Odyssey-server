package io.Odyssey.content.skills.thieving;

import com.google.common.collect.Lists;
import io.Odyssey.Server;
import io.Odyssey.content.SkillcapePerks;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.achievement_diary.impl.ArdougneDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.FaladorDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.LumbridgeDraynorDiaryEntry;
import io.Odyssey.content.achievement_diary.impl.WesternDiaryEntry;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.collisionmap.PathChecker;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.lock.CompleteLock;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Location3D;
import io.Odyssey.util.Misc;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static io.Odyssey.model.Items.*;

/**
 * A representation of the thieving skill. Support for both object and npc actions will be supported.
 *
 * @author Jason MacKeigan
 * @date Feb 15, 2015, 7:12:14 PM
 */
public class Thieving {

	private static final int[] rogueOutfit = { 5553, 5554, 5555, 5556, 5557 };

	/**
	 * The managing player of this class
	 */
	private final Player player;

	/**
	 * The last interaction that player made that is recorded in milliseconds
	 */
	private long lastInteraction;

	/**
	 * The constant delay that is required inbetween interactions
	 */
	private static final long INTERACTION_DELAY = 1_500L;

	/**
	 * The stealing animation
	 */
	private static final int ANIMATION = 881;

	/**
	 * Constructs a new {@link Thieving} object that manages interactions between players and stalls, as well as players and non playable characters.
	 *
	 * @param player the visible player of this class
	 */
	public Thieving(final Player player) {
		this.player = player;
	}

	/**
	 * A method for stealing from a stall
	 *
	 * @param stall the stall being stolen from
	 * @param location the location of the stall
	 */
	public void steal(Stall stall, Location3D location, int face) {
		if (System.currentTimeMillis() - lastInteraction < INTERACTION_DELAY) {
			//player.sendMessage("You must wait a few more seconds before you can steal again.");
			return;
		}
		if (player.getItems().freeSlots() == 0) {
			player.sendMessage("You need at least one free slot to steal from this.");
			return;
		}

		if (player.playerLevel[Skill.THIEVING.getId()] < stall.level) {
			player.sendMessage("You need a thieving level of " + stall.level + " to steal from this.");
			return;
		}
		if (Misc.random(200) == 0 && player.getInterfaceEvent().isExecutable()) {
			player.getInterfaceEvent().execute();
			return;
		}
		if (Misc.random(200) == 0) {
			player.getItems().addRandomBox(787);
			return;
		}
		player.getEventCalendar().progress(EventChallenge.THIEVE_X_STALLS);
		switch (stall) {
			case Baker:
				List<NPC> npcs = getNearbyGuards(player,5418);
				if (!npcs.isEmpty()) {
					for (NPC local : npcs) {
						local.randomWalk = false;
						local.forceChat("Hey! Get your hands off there!");
						local.attackEntity(player);

					}
					return;
				}
				//PathChecker.raycast(player, entity, true)
				///player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.TEA_STALL);
				break;
			case Ore:
			case Tzhaar_gem:
			npcs = getNearbyGuards(player,5418);
				if (!npcs.isEmpty()) {
					for (NPC local : npcs) {
						local.randomWalk = false;
						local.forceChat("Hey! Get your hands off there!");
						local.attackEntity(player);

					}
					return;
				}

				///player.getDiaryManager().getVarrockDiary().progress(VarrockDiaryEntry.TEA_STALL);
				break;
//			case Crafting:
//				if (Boundary.isIn(player, Boundary.ARDOUGNE_BOUNDARY)) {
//					player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.STEAL_CAKE);
//				}
//				break;
//			case Magic:
//				if (Boundary.isIn(player, Boundary.ARDOUGNE_BOUNDARY)) {
//					player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.STEAL_GEM_ARD);
//				}
//				if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
//					player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.STEAL_GEM_FAL);
//				}
//				break;
			case Scimitar:
				break;
			case Fur:
				if (Boundary.isIn(player, Boundary.ARDOUGNE_BOUNDARY)) {
					player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.STEAL_FUR);
				}
				break;
			//case Gold:
			default:
				break;
		}
		player.facePosition(location.getX(), location.getY());

		GameItem item = stall.getRandomItem();
		ItemDef definition = ItemDef.forId(item.getId());
		int petRate = player.skillingPetRateScroll ? (int) (stall.petChance * .75) : stall.petChance;
		if (Misc.random(petRate) == 20 && player.getItems().getItemCount(20663, false) == 0 && player.petSummonId != 20663) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] <col=255>" + player.getDisplayName() + "</col> now goes hand in hand with a <col=CC0000>Rocky</col> pet!");
			player.getItems().addItemUnderAnyCircumstance(20663, 1);
			player.getCollectionLog().handleDrop(player, 5, 20663, 1);
		}

		player.startAnimation(ANIMATION);
		player.getItems().addItem(item.getId(), item.getAmount());
//		if(stall == Stall.Scimitar)
//			if(Misc.random(1000) == 0){
//				player.getItems().addItem(FROZEN_KEY_PIECES[Misc.random(FROZEN_KEY_PIECES.length-1)], 1);
//				player.sendMessage("You receive a piece of the Frozen key!");
//			}


		player.getPA().addSkillXPMultiplied((int) (stall.experience * (1 + (getRoguesPieces() * 0.12))), Skill.THIEVING.getId(), true);
		player.sendMessage("You steal a " + definition.getName() + " from the stall.");
		Achievements.increase(player, AchievementType.THIEV, 1);
		lastInteraction = System.currentTimeMillis();
		if (Misc.random(stall.depletionProbability) == 1) {

Server.getGlobalObjects().add(new GlobalObject((player.objectId == 4875 || player.objectId  == 4878 || player.objectId  == 4876 || player.objectId  == 4877 ? 620 : (player.objectId  == 30279 || player.objectId  == 30280) ? 30278 : 634), location.getX(), location.getY(), 0, (face == 0 ? 0 : face == 3 ? 3 : face == 2 ? 2 : 1), 10, 10, player.objectId ));

		}
	}

	public List<NPC> getNearbyGuards(Player player,int npcId) {
		//TODO: optimize this to make it "faster"
		List<NPC> possibleTargets = Arrays.stream(NPCHandler.npcs).filter(npc -> npc != null && !npc.isDead
				&& npc.distance(player.getPosition()) <= 6         && npc.getNpcId() == npcId
				&& player.attacking.attackEntityCheck(npc, false)
				&& PathChecker.raycast(player, npc, false)).collect(Collectors.toList());
		return possibleTargets.stream().limit(1).collect(Collectors.toList());
	}


	private static final int[] FROZEN_KEY_PIECES = { 26358,
			26360,
			26362,
			26364};
	public int getRoguesPieces() {
		int pieces = 0;
		for (int aRogueOutfit : rogueOutfit) {
			if (player.getItems().isWearingItem(aRogueOutfit)) {
				pieces++;
			}
		}
		return pieces;
	}
	public enum Pickpocket {

		MAN(1, 8.0, 422, 5, 1, 52000, "man's",
				new LootTable().addTable(1,
						new LootItem(995, 1,3, 1) //Coins
				)),
		FARMER(10, 14.5, 433, 5, 1, 43500, "farmer's",
				new LootTable().addTable(1,
						new LootItem(995,5, 6), //Coins
						new LootItem(5318, 1, 1) //Potato seed
				)),
		HAM(15, 18.5, 433, 4, 1, 43500, "H.A.M member's",
				new LootTable().addTable(1,
						new LootItem(882, 16, 60), //Coins
						new LootItem(1351, 1, 1), //Coins
						new LootItem(1265, 1, 1), //Coins
						new LootItem(1349, 1, 1), //Coins
						new LootItem(1267, 1, 1), //Coins
						new LootItem(886, 20, 1), //Coins
						new LootItem(1353, 1, 1), //Coins
						new LootItem(1207, 1, 1), //Coins
						new LootItem(1129, 1, 1), //Coins
						new LootItem(4302, 1, 1), //Coins
						new LootItem(4298, 1, 1), //Coins
						new LootItem(4300, 1, 1), //Coins
						new LootItem(4304, 1, 1), //Coins
						new LootItem(4306, 1, 1), //Coins
						new LootItem(4308, 1, 1), //Coins
						new LootItem(4310, 1, 1), //Coins
						new LootItem(995,1, 1), //Coins
						new LootItem(319, 1, 1), //Coins
						new LootItem(2138, 1, 1), //Coins
						new LootItem(453, 1, 1), //Coins
						new LootItem(440, 1, 1), //Coins
						new LootItem(1739, 1, 1), //Coins
						new LootItem(314, 5, 1), //Coins
						new LootItem(1734, 6, 1), //Coins
						new LootItem(1733, 1, 1), //Coins
						new LootItem(1511, 1, 1), //Coins
						new LootItem(686, 1, 1), //Coins
						new LootItem(697, 1, 1), //Coins
						new LootItem(1625, 1, 1), //Coins
						new LootItem(1627, 1, 1), //Coins
						new LootItem(199, 5, 1), //Coins
						new LootItem(201, 6, 1), //Coins
						new LootItem(203, 1, 1) //Coins
				)),
		WARRIOR(25, 26.0, 386, 5, 2, 39000, "warrior's",
				new LootTable().addTable(1,
						new LootItem(995,18, 1) //Coins
				)),
		ROGUE(32, 35.5, 422, 5, 2, 34500, "rogue's",
				new LootTable().addTable(1,
						new LootItem(995,50, 10), //Coins
						new LootItem(556, 8, 5),  //Air runes
						new LootItem(1933, 1, 4), //Jug of wine
						new LootItem(1219, 1, 3), //Iron dagger(p)
						new LootItem(1523, 1, 1)  //Lockpick
				)),
		MASTER_FARMER(38, 43.0, 386, 5, 3, 27540, "master farmer's",
				new LootTable().addTable(1,
						new LootItem(5318, 1, 4, 8), //Potato seed
						new LootItem(5319, 1, 3, 5), //Onion seed
						new LootItem(5324, 1, 3, 5), //Cabbage seed
						new LootItem(5322, 1, 2, 5), //Tomato seed
						new LootItem(5320, 1, 2, 5), //Sweetcorn seed
						new LootItem(5096, 1, 5), //Marigold seed
						new LootItem(5097, 1, 5), //Rosemary seed
						new LootItem(5098, 1, 5), //Nasturtium seed
						new LootItem(5291, 1, 5), //Guam seed
						new LootItem(5292, 1, 5), //Marrentill seed
						new LootItem(5293, 1, 5), //Tarromin seed
						new LootItem(5294, 1, 5), //Harralander seed
						new LootItem(5323, 1, 3), //Strawberry seed
						new LootItem(5321, 1, 3), //Watermelon seed
						new LootItem(5100, 1, 3), //Limpwurt seed
						new LootItem(5295, 1, 2), //Ranarr seed
						new LootItem(5296, 1, 2), //Toadflax seed
						new LootItem(5297, 1, 2), //Irit seed
						new LootItem(5298, 1, 1), //Avantoe seed
						new LootItem(5299, 1, 1), //Kwuarm seed
						new LootItem(5300, 1, 1), //Snapdragon seed
						new LootItem(5301, 1, 1), //Cadantine seed
						new LootItem(5302, 1, 1), //Lantadyme seed
						new LootItem(5303, 1, 1), //Dwarf weed seed
						new LootItem(5304, 1, 1)  //Torstol seed
				)),
		GUARD(40, 46.8, 386, 5, 2, 23000, "guard's",
				new LootTable().addTable(1,
						new LootItem(995,30, 1) //Coins
				)),
		BANDIT(53, 79.5, 422, 5, 3, 23000, "bandit's",
				new LootTable().addTable(1,
						new LootItem(995,3, 8), //Coins
						new LootItem(175, 1, 3),  //Antipoison
						new LootItem(1523, 1, 1)  //Lockpick
				)),
		KNIGHT(55, 84.3, 386, 5, 3, 19000, "knight's",
				new LootTable().addTable(1,
						new LootItem(995,3, 1) //Coins
				)),
		PALADIN(70, 151.75, 386, 5, 3, 12000, "paladin's",
				new LootTable().addTable(1,
						new LootItem(995,3, 6)//Coins						new LootItem(562, 2, 3)   //Chaos runes
				)),
		GNOME(75, 198.5, 201, 5, 1, 11540, "gnome's",
				new LootTable().addTable(1,
						new LootItem(995,3, 16), //Coins
						new LootItem(5321, 3, 8),   //Watermelon seed
						new LootItem(5100, 1, 8),   //Limpwurt seed
						new LootItem(5295, 1, 7),   //Ranarr seed
						new LootItem(5296, 1, 7),   //Toadflax seed
						new LootItem(5297, 1, 7),   //Irit seed
						new LootItem(5298, 1, 7),   //Avantoe seed
						new LootItem(5299, 1, 7),   //Kwuarm seed
						new LootItem(5300, 1, 7),   //Snapdragon seed
						new LootItem(5301, 1, 7),   //Cadantine seed
						new LootItem(5302, 1, 6),   //Lantadyme seed
						new LootItem(5303, 1, 5),   //Dwarf weed seed
						new LootItem(5304, 1, 4),   //Torstol seed
						new LootItem(5312, 1, 4),   //Acorn
						new LootItem(5313, 1, 3),   //Willow seed
						new LootItem(5314, 1, 4),   //Maple seed
						new LootItem(5315, 1, 1),   //Yew seed
						new LootItem(5283, 1, 9),   //Apple tree seed
						new LootItem(5284, 1, 8),   //Banana tree seed
						new LootItem(5285, 1, 7),   //Orange tree seed
						new LootItem(5286, 1, 6),   //Curry tree seed
						new LootItem(5287, 1, 3),   //Pineapple seed
						new LootItem(5288, 1, 2)    //Papaya tree seed
				)),
		HERO(80, 275.0, 386, 6, 4, 9700, "hero's",
				new LootTable().addTable(1,
						new LootItem(995,60, 16),  //Coins
						new LootItem(565, 1, 5),  //Blood rune
						new LootItem(560, 2, 5),  //Death runes
						new LootItem(1933, 1, 2), //Jug of wine
						new LootItem(569, 1, 2),  //Fire orb
						new LootItem(444, 1, 2),  //Gold ore
						new LootItem(1617, 1, 1)  //Uncut diamond
				)),
		ELF(85, 353.0, 422, 6, 5, 8500, "elf's",
				new LootTable().addTable(1,
						new LootItem(995,60, 16), //Coins
						new LootItem(561, 3, 5),  //Nature runes
						new LootItem(560, 2, 5),  //Death runes
						new LootItem(1933, 1, 2), //Jug of wine
						new LootItem(569, 1, 2),  //Fire orb
						new LootItem(444, 1, 2),  //Gold ore
						new LootItem(1617, 1, 1)  //Uncut diamond
				)),
		TZHAAR_HUR(90, 103.0, 2609, 6, 5, 7700, "tzhaar-hur's",
				new LootTable().addTable(1,
						new LootItem(1755, 1, 6),                    //Chisel
						new LootItem(2347, 1, 5),                    //Hammer
						new LootItem(1935, 1, 5),                    //Jug
						new LootItem(946, 1, 2),                     //Knife
						new LootItem(1931, 1, 2),                    //Pot
						new LootItem(6529, 1, 16, 2),  //Tokkul
						new LootItem(1623, 1, 1),                    //Uncut Sapphire
						new LootItem(1619, 1, 1)                     //Uncut Ruby
				));

		public final int levelReq, stunAnimation, stunSeconds, stunDamage, petOdds;
		private final String name, identifier;
		public final double exp;
		public final LootTable lootTable;

		Pickpocket(int levelReq, double exp, int stunAnimation, int stunSeconds, int stunDamage, int petOdds, String identifier, LootTable lootTable) {
			this.levelReq = levelReq;
			this.exp = exp;
			this.stunAnimation = stunAnimation;
			this.stunSeconds = stunSeconds;
			this.stunDamage = stunDamage;
			this.petOdds = petOdds;
			this.name = identifier.replace("'s", "");
			this.identifier = identifier;
			this.lootTable = lootTable;
		}
	}
	private static boolean successful(Player player, Pickpocket pickpocket) {
		return Misc.random(100) <= chance(player, pickpocket.levelReq);
	}

	/**
	 * A method for pick pocketing npc's
	 *
	 * @param pickpocket the pickpocket type
	 * @param npc the npc being pick pocketed
	 */
	public void steal(Pickpocket pickpocket, NPC npc) {
		if (System.currentTimeMillis() - lastInteraction < INTERACTION_DELAY) {
			//player.sendMessage("You must wait a few more seconds before you can steal again.");
			return;
		}
		if (player.getItems().freeSlots() == 0) {
			player.sendMessage("You need at least one free slot to steal from this npc.");
			return;
		}
		if (player.playerLevel[Skill.THIEVING.getId()] < pickpocket.levelReq) {
			player.sendMessage("You need a thieving level of " + pickpocket.levelReq + " to steal from this npc.");
			return;
		}
		if (Misc.random(200) == 0 && player.getInterfaceEvent().isExecutable()) {
			player.getInterfaceEvent().execute();
			return;
		}
		/**
		 * Incorporate chance for failure
		 */

			if (!successful(player, pickpocket)) {
			player.gfx100(80);
			player.startAnimation(404);

			npc.forceChat("What do you think you're doing?");
			switch (pickpocket) {
				case TZHAAR_HUR:
					if (!player.getItems().isWearingItem(ICE_GLOVES)){
						player.appendDamage(4, Hitmark.HIT);
						player.getPA().refreshSkill(3);
						player.sendMessage("Without @blu@Ice gloves@bla@ you get burnt.");
					}


					break;
				default:
					break;
			}
			npc.facePlayer(player.getIndex());
			npc.startAnimation(NPCHandler.getAttackEmote(npc));
			lastInteraction = System.currentTimeMillis();
			player.lock(new CompleteLock());
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (player.disconnected) {
						container.stop();
						return;
					}
					player.addQueuedAction(Player::unlock);
					container.stop();
				}


			}, 5);
			return;
		}
		switch (pickpocket) {
			case FARMER:
				if (Boundary.isIn(player, Boundary.ARDOUGNE_BOUNDARY)) {
					player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.PICKPOCKET_ARD);
				}
				if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
					player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.PICKPOCKET_MASTER_FARMER_FAL);
				}
				if (Boundary.isIn(player, Boundary.DRAYNOR_BOUNDARY)) {
					player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.PICKPOCKET_FARMER_DRAY);
				}
				break;
			case MAN:
				if (Boundary.isIn(player, Boundary.FALADOR_BOUNDARY)) {
					player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.PICKPOCKET_MAN);
				}
				if (Boundary.isIn(player, Boundary.LUMRIDGE_BOUNDARY)) {
					player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.PICKPOCKET_MAN_LUM);
				}
				break;
			case GNOME:
				player.getDiaryManager().getWesternDiary().progress(WesternDiaryEntry.PICKPOCKET_GNOME);
				break;
			case HERO:
				player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.PICKPOCKET_HERO);
				break;
//			case MENAPHITE_THUG:
//				player.getDiaryManager().getDesertDiary().progress(DesertDiaryEntry.PICKPOCKET_THUG);
//				break;
			default:
				break;

		}

		player.facePosition(npc.getX(), npc.getY());
		player.startAnimation(ANIMATION);
		GameItem item =pickpocket.lootTable.rollItem();
		boolean maxCape = SkillcapePerks.THIEVING.isWearing(player) || SkillcapePerks.isWearingMaxCape(player);
		if (item != null) {
			player.getItems().addItem(item.getId(), maxCape ? item.getAmount()*2 : item.getAmount());
		} else {
			player.sendMessage("You were unable to find anything useful.");
		}
		int petRate = player.skillingPetRateScroll ? (int) (pickpocket.petOdds * .75) : pickpocket.petOdds;
		if (Misc.random(petRate) == 20 && player.getItems().getItemCount(20663, false) == 0 && player.petSummonId != 20663) {
			PlayerHandler.executeGlobalMessage("[<col=CC0000>News</col>] @cr20@ <col=255>" + player.getDisplayName() + "</col> now goes hand in hand with a <col=CC0000>Rocky</col> pet!");
			player.getItems().addItemUnderAnyCircumstance(20663, 1);
			player.getCollectionLog().handleDrop(player, 5, 20663, 1);
		}
		Achievements.increase(player, AchievementType.THIEV, 1);
		player.getPA().addSkillXPMultiplied((int) (pickpocket.exp * (1 + (getRoguesPieces() * 0.12))), Skill.THIEVING.getId(), true);
		lastInteraction = System.currentTimeMillis();
	}


	private static int chance(Player player, int levelReq) {
		int slope = 2;
		int chance = 60; //Starts at a 60% chance
		int thievingLevel = player.playerLevel[17];
		int requiredLevel = levelReq;

		if (player.getItems().isWearingItem(GLOVES_OF_SILENCE))
			chance += 5;
		if (SkillcapePerks.isWearingMaxCape(player))
			chance *= 1.1;
		if (thievingLevel > levelReq)
			chance += (thievingLevel - requiredLevel) * slope;
		return Math.min(chance, 95); //Caps at 95%
	}
	private enum Rarity {
		ALWAYS(0), COMMON(5), UNCOMMON(10), RARE(15), VERY_RARE(25);

		/**
		 * The rarity
		 */
		private final int rarity;

		/**
		 * Creates a new rarity
		 *
		 * @param rarity the rarity
		 */
		Rarity(int rarity) {
			this.rarity = rarity;
		}
	}

	public enum Stall {
		Baker( 5, 16, 5, 45000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(CHOCOLATE_SLICE, 1)));
				put(Rarity.COMMON, Arrays.asList(new GameItem(CHOCOLATE_SLICE),new GameItem(BREAD),new GameItem(CAKE)));
			}
		}),

		Tea(5, 16, 10, 45000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(712, 1)));
			}
		}),
		Silk(20, 24, 10, 45000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(712, 1)));
			}
		}),
		Spice(65, 67, 10, 43000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(712, 1)));
			}
		}),
		Silver(50, 2, 2, 40000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(2961, 1)));
			}
		}),
		Gem(75, 80, 10, 38000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(1613, 1)));
			}
		}),
		Scimitar(90, 100, 10, 36500,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(1993, 1)));
			}
		}),
		Fur( 35, 47, 10, 40000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(712, 1)));
			}
		}),

		Tzhaar_gem( 75, 160, 10, 40000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(1607, 1)));
				put(Rarity.UNCOMMON, Arrays.asList(new GameItem(1605),new GameItem(1603)));

				put(Rarity.RARE, Arrays.asList(new GameItem(1601)));
				put(Rarity.VERY_RARE, Collections.singletonList(new GameItem(1631)));
			}
		}),
		Ore( 82, 180, 10, 40000,new HashMap<Rarity, List<GameItem>>() {
			{
				put(Rarity.ALWAYS, Arrays.asList(new GameItem(453, 1)));
				put(Rarity.UNCOMMON, Arrays.asList(new GameItem(447)));

				put(Rarity.RARE, Arrays.asList(new GameItem(440)));
				put(Rarity.VERY_RARE, Collections.singletonList(new GameItem(451)));
			}
		});

		/**
		 * The experience gained in thieving from a single stall thieve
		 */
		private final double experience;

		/**
		 * The probability that the stall will deplete
		 */
		private final int depletionProbability;

		/**
		 * The level required to steal from the stall
		 */
		private final int level;

		/**
		 * The chance of receiving a pet
		 */
		private final int petChance;

		private Map<Rarity, List<GameItem>> items = new HashMap<>();

		GameItem getRandomItem() {
			for (Entry<Rarity, List<GameItem>> entry : items.entrySet()) {
				final Rarity rarity = entry.getKey();

				if (rarity == Rarity.ALWAYS) {
					continue;
				}
				final List<GameItem> items = entry.getValue();

				if (items.isEmpty()) {
					continue;
				}

				if (RandomUtils.nextInt(1, rarity.rarity) == 1) {
					return Misc.getItemFromList(items).randomizedAmount();
				}
			}

			List<GameItem> always = items.getOrDefault(Rarity.ALWAYS, Lists.newArrayList());

			if (!always.isEmpty()) {
				return Misc.getItemFromList(always).randomizedAmount();
			}

			return null;
		}

		/**
		 * Constructs a new {@link Stall} object with a single parameter, {@link GameItem} which is the item received when interacted with.
		 *
		 *
		 */
		Stall(int level, int experience, int depletionProbability, int petChance, Map<Rarity, List<GameItem>> items) {
			this.level = level;
			this.experience = experience;
			this.depletionProbability = depletionProbability;
			this.petChance = petChance;
			this.items = items;
		}
	}

}