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

public class BlackAodLootChest extends MysteryBoxLootable {

    /**
     * A map containing a List of {@link GameItem}'s that contain items relevant to their rarity.
     */
    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    /**
     * Stores an array of items into each map with the corresponding rarity to the list
     */

    static {
        items.put(LootRarity.COMMON,//50% chance
                Arrays.asList(
                        new GameItem(12873),//guthan set

                        new GameItem(10340)//3rd age robe bottom
                ));

        items.put(LootRarity.UNCOMMON,//40% Chance
                Arrays.asList(
                        new GameItem(30087),
                        new GameItem(30089),//3rd age helm
                        new GameItem(30088)//3rd age robe bottom
                )
        );

        items.put(LootRarity.RARE,//10% chance  // think it matters that the chest were trying has name and the others have id number ?
                Arrays.asList(
                        new GameItem(Items.BLACK_AOD_BOOTS),//AGS
                        new GameItem(Items.BLACK_AOD_CAPE),//AGS
                        new GameItem(Items.DEATHSTALKER_GODSWORD),//AGS
                        new GameItem(Items.AOD_GOD_SWORD),//AGS
                        new GameItem(Items.AOD_WAND),//AGS
                        new GameItem(Items.BLACK_AOD_GLOVES)));//ring of wealth (i)
    }
    /**
     * Constructs a new myster box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public BlackAodLootChest(Player player) {
        super(player);
    }

    @Override
    public int getItemId() {
        return 30109;
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;

    }
}
