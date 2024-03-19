package io.Odyssey.content.item.lootable.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import io.Odyssey.content.item.lootable.ItemLootable;
import io.Odyssey.content.item.lootable.LootRarity;
import io.Odyssey.content.item.lootable.MysteryBoxLootable;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.GameItemVariableAmount;

public class ResourceBoxLarge extends MysteryBoxLootable {

    /**
     * Constructs a new mystery box to handle item receiving for this player and this player alone
     *
     * @param player the player
     */
    public ResourceBoxLarge(Player player) {
        super(player);
    }

    @Override
    public int getItemId() {
        return 787;
    }
    /**
     * A map containing a List of {@link GameItem}'s that contain items relevant to their LootRarity.
     */
    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();


        static {
            items.put(LootRarity.COMMON, //50% chance
                    Arrays.asList(     new GameItem(3050, 5, 7), //grimy lantadyme
                            new GameItem(212, 5, 7), //grimy dwarf weed
                            new GameItem(214, 5, 7), //grimy torstol
                new GameItem(3052, 5, 7),  //lantadyme potion (unf)
                new GameItem(216, 5, 7),  //dwarf weed potion (unf)
                new GameItem(3003, 5, 7), //torstol potion (unf)
                            new GameItem(454, 7, 10), //coal
                            new GameItem(448, 7, 10), //mithril ore
                            new GameItem(450, 7, 10), //adamantite ore
                            new GameItem(452, 7, 10),  //runite ore
                            new GameItem(2360,2, 5),  //mithril bar
                            new GameItem(2362,2, 5), //adamantite bar
                            new GameItem(2364,2, 5), //runite bar
                            new GameItem(1516, 7, 10),  //yew logs
                            new GameItem(1514, 7, 10),  //magic logs
                            new GameItem(19670, 7, 10),  //redwood logs

                            new GameItem(Items.LIMPWURT_ROOT_NOTED, 5, 10),
                            new GameItem(Items.RED_SPIDERS_EGGS_NOTED, 10, 15),
                            new GameItem(Items.MORT_MYRE_FUNGUS_NOTED, 10, 15),
                            new GameItem(Items.CRUSHED_NEST_NOTED, 2, 4)));

            items.put(LootRarity.UNCOMMON,//40% Chance
                    Arrays.asList(
                            new GameItem(1620, 5, 7), //uncut ruby
                            new GameItem(1618, 5, 7), //uncut diamond
                            new GameItem(1632, 5, 7), //uncut dragonstone
                            new GameItem(7945, 7, 10),  //raw monkfish
                            new GameItem(3143, 7, 10), //raw karamwban
                            new GameItem(384, 7, 10),  //raw shark
                            new GameItem(390, 7, 10)) //raw manta ray

            );

            items.put(LootRarity.RARE,//8% chance
                    Arrays.asList(
                            new GameItem(1620, 5, 7), //uncut ruby
                            new GameItem(1618, 5, 7), //uncut diamond
                            new GameItem(1632, 5, 7), //uncut dragonstone
                            new GameItem(7945, 7, 10),  //raw monkfish
                            new GameItem(3143, 7, 10), //raw karamwban
                            new GameItem(384, 7, 10),  //raw shark
                            new GameItem(390, 7, 10))); //raw manta ray
        }


    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }


}
