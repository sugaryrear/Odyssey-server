package io.Odyssey.content.bosses;


import java.util.*;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import io.Odyssey.Server;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.content.instances.impl.LegacySoloPlayerInstance;
import io.Odyssey.content.item.lootable.LootRarity;
import io.Odyssey.content.skills.agility.AgilityHandler;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.util.Misc;


public class Hunllef extends LegacySoloPlayerInstance {

	public static final int MELEE_PROTECT = 9021;
	public static final int RANGED_PROTECT = 9022;
	public static final int MAGE_PROTECT = 9023;

	public void rewardPlayers() {

	//	Entity killer = player.calculateKiller();
//
//		PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.HUNLLEF_BOSS_ROOM)).forEach(plr -> {
//
//			if (killer != null && killer.equals(plr)) {
//				System.out.println("heress");
//				getReward(plr);
//
//			}
//
//		});

//		if (Boundary.isIn(player, Boundary.HUNLLEF_CAVE)) {
//			Achievements.increase(player, AchievementType.HUNLLEF, 1);
//			player.getPA().spellTeleport(3029, 6126, 1, false);
//			player.getItems().addItemUnderAnyCircumstance(23776, 1);
//		}
		player.getPA().movePlayer(3031,6124,1);
		getReward();
		player.hunllefDead = false;
	}

	public static void start(Player c) {
		c.getPA().closeAllWindows();
		Hunllef instance = new Hunllef(c);
		Consumer<Player> start = (player) -> {
			c.resetDamageTaken();
			instance.add(c);
			c.getPA().closeAllWindows();
			respawn(instance);
		};

		AgilityHandler.delayFade(c, "CRAWL", 1884, 5655, instance.getHeight(), "You crawl into the cave.",
				"and end up at the Hunllef's lair", 1, start);
		//c.getItems().deleteItem(23951, 1);
	}

	public static void respawn(InstancedArea instance) {
		Hunllef hunllef = (Hunllef) instance;
		hunllef.npc = NPCSpawning.spawn(9021, 1887, 5655, instance.getHeight(), 1, 30, true);
		instance.add(hunllef.npc);
	}

	private Player player;
	private NPC npc;

	public Hunllef(final Player player) {
		super(player, Boundary.HUNLLEF_BOSS_ROOM);
		loadItems();
		this.player = player;
	}
	private final Map<LootRarity, List<GameItem>> items = new HashMap<>();
	private static final Map<LootRarity, List<GameItem>> itemz = new HashMap<>();

	public void loadItems() {
		if (items.isEmpty()) {
			items.put(LootRarity.COMMON, Arrays.asList(
					new GameItem(Items.DEATH_RUNE, 325),
					new GameItem(Items.BLOOD_RUNE, 170),
					new GameItem(Items.SOUL_RUNE, 227),
					new GameItem(Items.DRAGON_BOLTS_UNF, 90),
					new GameItem(Items.CANNONBALL, 298),
					new GameItem(Items.AIR_RUNE, 1365),
					new GameItem(Items.FIRE_RUNE, 1655),
					new GameItem(Items.WATER_RUNE, 1599),
					new GameItem(Items.ONYX_BOLTS_E, 29),
					new GameItem(Items.AIR_ORB_NOTED, 20),
					new GameItem(Items.UNCUT_RUBY_NOTED, 26),
					new GameItem(Items.UNCUT_DIAMOND_NOTED, 17),
					new GameItem(Items.WINE_OF_ZAMORAK_NOTED, 14),
					new GameItem(Items.COAL_NOTED, 95),
					new GameItem(Items.RUNITE_ORE_NOTED, 28),
					new GameItem(Items.SHARK, 3),
					new GameItem(Items.PRAYER_POTION4, 1),
					new GameItem(Items.SARADOMIN_BREW4, 2),
					new GameItem(Items.SUPER_RESTORE4, 1),
					new GameItem(Items.COINS, 26748)
			));

			items.put(LootRarity.RARE, Arrays.asList(
					new GameItem(25865),
					new GameItem(23995),
					new GameItem(23975),

					new GameItem(23971),
					new GameItem(23979)

			));
		}
	}

	static {
		itemz.put(LootRarity.COMMON, Arrays.asList(
				new GameItem(Items.DEATH_RUNE, 325),
				new GameItem(Items.BLOOD_RUNE, 170),
				new GameItem(Items.SOUL_RUNE, 227),
				new GameItem(Items.DRAGON_BOLTS_UNF, 90),
				new GameItem(Items.CANNONBALL, 298),
				new GameItem(Items.AIR_RUNE, 1365),
				new GameItem(Items.FIRE_RUNE, 1655),
				new GameItem(Items.WATER_RUNE, 1599),
				new GameItem(Items.ONYX_BOLTS_E, 29),
				new GameItem(Items.AIR_ORB_NOTED, 20),
				new GameItem(Items.UNCUT_RUBY_NOTED, 26),
				new GameItem(Items.UNCUT_DIAMOND_NOTED, 17),
				new GameItem(Items.WINE_OF_ZAMORAK_NOTED, 14),
				new GameItem(Items.COAL_NOTED, 95),
				new GameItem(Items.RUNITE_ORE_NOTED, 28),
				new GameItem(Items.SHARK, 3),
				new GameItem(Items.PRAYER_POTION4, 1),
				new GameItem(Items.SARADOMIN_BREW4, 2),
				new GameItem(Items.SUPER_RESTORE4, 1),
				new GameItem(Items.COINS, 26748)
		));

		itemz.put(LootRarity.RARE, Arrays.asList(

				new GameItem(25865),
				new GameItem(23995),
				new GameItem(23975),

				new GameItem(23971),
				new GameItem(23979)
		));
	}
	public static ArrayList<GameItem> getAllDrops() {
		ArrayList<GameItem> drops = new ArrayList<>();
		itemz.forEach((lootRarity, gameItems) -> {
			gameItems.forEach(g -> {
				if (!drops.contains(g)) {
					drops.add(g);
				}
			});
		});
		return drops;
	}
	public void getReward() {
		player.getItems().addItem(23866,100);
		for (GameItem randomItem : getRandomItems()) {
			if (player.getInventory().freeInventorySlots() < 1) {
				player.getInventory().addToBank(new ImmutableItem(randomItem));
				player.sendMessage(randomItem.getDef().getName() + ", Has been added to your bank!");
			} else {
				player.getInventory().addOrDrop(new ImmutableItem(randomItem));
				player.sendMessage("You receive: @blu@ "+randomItem.getDef().getName() + "@bla@.");
			}
			if (items.get(LootRarity.RARE).contains(randomItem)) {

				player.getCollectionLog().handleDrop(player, 9021, randomItem.getId(), randomItem.getAmount(), true);
			}
		}
	}
	public List<GameItem> getRandomItems() {
		List<GameItem> rewards = Lists.newArrayList();
		int rareChance = 100;

		if (Misc.random(1, rareChance) == 1) {
			rewards.add(Misc.getRandomItem(items.get(LootRarity.RARE)));
		} else {
			for (int count = 0; count < 2; count++) {
				rewards.add(Misc.getRandomItem(items.get(LootRarity.COMMON)));
			}
		}
		return rewards;
	}
	public static Map<LootRarity, List<GameItem>> getItems() {
		return itemz;
	}

	@Override
	public void onDispose() {

		if (npc != null) {
			if (npc.isDead()) {
				return;
			}
			npc.unregister();
		}
		if (player != null && player.getInstance() == this) {

			player.removeFromInstance();
		}
		player = null;
	}
}
