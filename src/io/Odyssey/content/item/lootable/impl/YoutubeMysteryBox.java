package io.Odyssey.content.item.lootable.impl;

import io.Odyssey.content.item.lootable.LootRarity;
import io.Odyssey.content.item.lootable.MysteryBoxLootable;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import QuickUltra.Rarity;

/**
 * Revamped a simple means of receiving a random item based on chance.
 * 
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class YoutubeMysteryBox extends MysteryBoxLootable {

	/**
	 * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
	 */
	private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

	/**
	 * Stores an array of items into each map with the corresponding rarity to the list
	 */
	static {
		items.put(LootRarity.COMMON, //50% chance
				Arrays.asList(
						new GameItem(19550),//ring of sufferin
						new GameItem(19547),//anguish
						new GameItem(19544),//tormented

						new GameItem(11785),//ACB
						new GameItem(21633),//ancient wyvern shield
						new GameItem(11832),//BCP
						new GameItem(11834),//TASSETS
						new GameItem(11828),//Arma Chest
						new GameItem(11830),//Arma Legs
						new GameItem(11802),//AGS
						new GameItem(21003),//Elder Maul
						new GameItem(19553),//Amulet of Torture
						new GameItem(19544),//tormented bracelet
						new GameItem(21000),//twisted buckler
						new GameItem(21034),//dex prayer scroll
						new GameItem(13576),//dragon warhammer
						new GameItem(21079),//arcane prayer scroll
						new GameItem(12902),//toxic staff of the dead

						new GameItem(13239),//primordial boots
						new GameItem(13235),//eternal boots
						new GameItem(13237),//pegasian boots)
						new GameItem(19550),//ring of sufferin
						new GameItem(19547),//anguish
						new GameItem(19544),//tormented

						new GameItem(11785),//ACB
						new GameItem(21633),//ancient wyvern shield
						new GameItem(11832),//BCP
						new GameItem(11834),//TASSETS
						new GameItem(11828),//Arma Chest
						new GameItem(11830),//Arma Legs
						new GameItem(11802),//AGS
						new GameItem(21003),//Elder Maul
						new GameItem(19553),//Amulet of Torture
						new GameItem(19544),//tormented bracelet
						new GameItem(21000),//twisted buckler
						new GameItem(21034),//dex prayer scroll
						new GameItem(13576),//dragon warhammer
						new GameItem(21079),//arcane prayer scroll
						new GameItem(12809),//saradomin blessed sword
						new GameItem(12902),//toxic staff of the dead
						new GameItem(12002),//occult necklace
						new GameItem(13239),//primordial boots
						new GameItem(13235),//eternal boots
						new GameItem(13237))//pegasian boots)

		);
		items.put(LootRarity.UNCOMMON, //50% chance
				Arrays.asList(
						new GameItem(19550),//ring of sufferin
						new GameItem(24271),
						new GameItem(19547),//anguish
						new GameItem(19544),//tormented
						new GameItem(12899),//toxic trident
						new GameItem(11785),//ACB
						new GameItem(21633),//ancient wyvern shield
						new GameItem(11832),//BCP
						new GameItem(11834),//TASSETS
						new GameItem(11828),//Arma Chest
						new GameItem(11830),//Arma Legs
						new GameItem(11802),//AGS
						new GameItem(21003),//Elder Maul
						new GameItem(19553),//Amulet of Torture
						new GameItem(19544),//tormented bracelet
						new GameItem(21000),//twisted buckler
						new GameItem(21034),//dex prayer scroll
						new GameItem(13576),//dragon warhammer
						new GameItem(21079),//arcane prayer scroll
						new GameItem(12902),//toxic staff of the dead
						new GameItem(12002),//occult necklace
						new GameItem(13239),//primordial boots
						new GameItem(13235),//eternal boots
						new GameItem(19550),//ring of sufferin
						new GameItem(19547),//anguish
						new GameItem(19544),//tormented

						new GameItem(11785),//ACB
						new GameItem(11832),//BCP
						new GameItem(11834),//TASSETS
						new GameItem(11828),//Arma Chest
						new GameItem(11830),//Arma Legs
						new GameItem(11802),//AGS
						new GameItem(21003),//Elder Maul
						new GameItem(19553),//Amulet of Torture
						new GameItem(19544),//tormented bracelet
						new GameItem(21034),//dex prayer scroll
						new GameItem(13576),//dragon warhammer
						new GameItem(21079),//arcane prayer scroll
						new GameItem(12902),//toxic staff of the dead
						new GameItem(13239),//primordial boots
						new GameItem(13235),//eternal boots
						new GameItem(21015),//dihn bulwark
						new GameItem(21006),//kodai wand
						new GameItem(21015),//dihn bulwark
						new GameItem(21006),//kodai wand
						new GameItem(20095),//ankou mask
						new GameItem(20104),//ankou leggings
						new GameItem(20098),//ankou top
						new GameItem(20107),//ankou socks
						new GameItem(20101),//ankou gloves
						new GameItem(761),//$25 scroll
						new GameItem(761)//$25 scroll
				));

		items.put(LootRarity.RARE,//8% chance
				Arrays.asList(

						new GameItem(11785),//ACB
						new GameItem(11832),//BCP
						new GameItem(11834),//TASSETS
						new GameItem(11828),//Arma Chest
						new GameItem(11830),//Arma Legs
						new GameItem(11802),//AGS
						new GameItem(21003),//Elder Maul
						new GameItem(19553),//Amulet of Torture
						new GameItem(19544),//tormented bracelet
						new GameItem(21034),//dex prayer scroll
						new GameItem(13576),//dragon warhammer
						new GameItem(21079),//arcane prayer scroll
						new GameItem(12902),//toxic staff of the dead
						new GameItem(13239),//primordial boots
						new GameItem(13235),//eternal boots
						new GameItem(21015),//dihn bulwark
						new GameItem(21006),//kodai wand
						new GameItem(21015),//dihn bulwark
						new GameItem(21006),//kodai wand
						new GameItem(20095),//ankou mask
						new GameItem(20104),//ankou leggings
						new GameItem(20098),//ankou top
						new GameItem(20107),//ankou socks
						new GameItem(20101),//ankou gloves
						new GameItem(761),//$25 scroll
						new GameItem(761)//$25 scroll
				));
	}

    /**
	 * Constructs a new myster box to handle item receiving for this player and this player alone
	 *
	 * @param player the player
	 */
	public YoutubeMysteryBox(Player player) {
		super(player);
	}

	@Override
	public int getItemId() {
		return 29973;
	}

	@Override
	public Map<LootRarity, List<GameItem>> getLoot() {
		return items;
	}
}