package io.Odyssey.content.item.lootable.impl;

import io.Odyssey.content.item.lootable.LootRarity;
import io.Odyssey.content.item.lootable.MysteryBoxLootable;
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
public class FoeMysteryBox extends MysteryBoxLootable {

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
					new GameItem(20784),//dclaw
					new GameItem(21034),//dex
					new GameItem(21079),//arcane
					new GameItem(21006), //kodai
					new GameItem(21015), //din
					new GameItem(21018),//ances hat
						new GameItem(21021),//ances top
						new GameItem(21024), //ances bott
						new GameItem(21004),//elder
						new GameItem(22322),//avern
						new GameItem(22326),//just helm
						new GameItem(22327),//jusr body
						new GameItem(22328),//just legs
						new GameItem(3464, 10),//comm keys
						new GameItem(995,100000000)






						));
			items.put(LootRarity.UNCOMMON, //50% chance
					Arrays.asList(
							new GameItem(22323),///sang staff
							new GameItem(22324),//ghrazi
							new GameItem(21012), //dhcb
							new GameItem(24664), //twist hat
							new GameItem(24666), //twist top
							new GameItem(24668) //twist bott

					));

			items.put(LootRarity.RARE,//8% chance
					Arrays.asList(
							new GameItem(20997),//tbow
							new GameItem(22325)//scythe

						));
		}

    /**
	 * Constructs a new myster box to handle item receiving for this player and this player alone
	 *
	 * @param player the player
	 */
	public FoeMysteryBox(Player player) {
		super(player);
	}

	@Override
	public int getItemId() {
		return 29973;
	}//29973

	@Override
	public Map<LootRarity, List<GameItem>> getLoot() {
		return items;
	}
}