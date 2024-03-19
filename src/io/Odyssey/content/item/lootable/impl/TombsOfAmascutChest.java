package io.Odyssey.content.item.lootable.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosspoints.BossPoints;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.item.lootable.LootRarity;
import io.Odyssey.content.item.lootable.Lootable;
import io.Odyssey.content.leaderboards.LeaderboardType;
import io.Odyssey.content.leaderboards.LeaderboardUtils;
import io.Odyssey.model.Items;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.util.Misc;

public class TombsOfAmascutChest implements Lootable {


    private static final Map<LootRarity, List<GameItem>> items = new HashMap<>();

    static {
        items.put(LootRarity.COMMON, Arrays.asList(
                new GameItem(Items.DEATH_RUNE, 600),
                new GameItem(Items.BLOOD_RUNE, 600),
                new GameItem(Items.COAL_NOTED, 600),
                new GameItem(Items.GOLD_ORE_NOTED, 360),
                new GameItem(Items.ADAMANTITE_ORE_NOTED, 150),
                new GameItem(Items.RUNITE_ORE_NOTED, 72),
                new GameItem(Items.GRIMY_CADANTINE_NOTED, 60),
                new GameItem(Items.GRIMY_AVANTOE_NOTED, 48),
                new GameItem(Items.GRIMY_TOADFLAX_NOTED, 44),
                new GameItem(Items.GRIMY_KWUARM_NOTED, 43),
                new GameItem(Items.GRIMY_IRIT_LEAF_NOTED, 40),
                new GameItem(Items.GRIMY_RANARR_WEED_NOTED, 36),
                new GameItem(Items.GRIMY_SNAPDRAGON_NOTED, 32),
                new GameItem(Items.GRIMY_LANTADYME_NOTED, 31),
                new GameItem(Items.GRIMY_DWARF_WEED_NOTED, 28),
                new GameItem(Items.GRIMY_TORSTOL_NOTED, 24),
                new GameItem(Items.BATTLESTAFF_NOTED, 18),
                new GameItem(Items.RUNE_BATTLEAXE_NOTED, 4),
                new GameItem(Items.RUNE_PLATEBODY_NOTED, 4),
                new GameItem(Items.RUNE_CHAINBODY_NOTED, 4),
                new GameItem(995, 7_500_000)
        ));

        items.put(LootRarity.RARE, Arrays.asList(
                new GameItem(Items.MASORI_MASK),
                new GameItem(Items.MASORI_MASK),
                new GameItem(Items.MASORI_MASK),

                new GameItem(Items.MASORI_MASK_F),
                new GameItem(Items.MASORI_MASK_F),
                new GameItem(Items.MASORI_MASK_F),

                new GameItem(Items.MASORI_BODY),
                new GameItem(Items.MASORI_BODY),
                new GameItem(Items.MASORI_BODY),

                new GameItem(Items.MASORI_BODY_F),
                new GameItem(Items.MASORI_BODY_F),
                new GameItem(Items.MASORI_BODY_F),

                new GameItem(Items.MASORI_CHAPS_F),
                new GameItem(Items.MASORI_CHAPS_F),
                new GameItem(Items.MASORI_CHAPS_F),

                new GameItem(Items.MASORI_CHAPS),
                new GameItem(Items.MASORI_CHAPS),
                new GameItem(Items.MASORI_CHAPS),

                new GameItem(Items.ELIDINIS_WARD),
                new GameItem(Items.ELIDINIS_WARD),
                new GameItem(Items.ELIDINIS_WARD),

                new GameItem(Items.ELIDINIS_WARD_F),
                new GameItem(Items.ELIDINIS_WARD_F),
                new GameItem(Items.ELIDINIS_WARD_F),

                new GameItem(Items.ELIDINIS_WARD_OR),
                new GameItem(Items.ELIDINIS_WARD_OR),
                new GameItem(Items.ELIDINIS_WARD_OR),

                new GameItem(Items.KERIS_PARTISAN),
                new GameItem(Items.KERIS_PARTISAN),
                new GameItem(Items.KERIS_PARTISAN),

                new GameItem(Items.KERIS_PARTISAN_OF_BREACHING),
                new GameItem(Items.KERIS_PARTISAN_OF_BREACHING),
                new GameItem(Items.KERIS_PARTISAN_OF_BREACHING),

                new GameItem(Items.KERIS_PARTISAN_OF_THE_SUN),
                new GameItem(Items.KERIS_PARTISAN_OF_THE_SUN),
                new GameItem(Items.KERIS_PARTISAN_OF_THE_SUN),

                new GameItem(Items.KERIS_PARTISAN_OF_CORRUPTION),
                new GameItem(Items.KERIS_PARTISAN_OF_CORRUPTION),
                new GameItem(Items.KERIS_PARTISAN_OF_CORRUPTION),

                new GameItem(Items.OSMUMTENS_FANG),
                new GameItem(Items.OSMUMTENS_FANG),
                new GameItem(Items.OSMUMTENS_FANG),

                new GameItem(Items.LIGHT_BEARER),
                new GameItem(Items.LIGHT_BEARER),
                new GameItem(Items.LIGHT_BEARER),
                new GameItem(Items.LIGHT_BEARER),
                new GameItem(Items.LIGHT_BEARER),

                new GameItem(Items.OSMUMTENS_FANG_OR),
                new GameItem(Items.OSMUMTENS_FANG_OR),

                new GameItem(Items.TUMEKENS_SHADOW_UNCHARGED)

        ));
    }

    public static ArrayList<GameItem> getRareDrops() {
        ArrayList<GameItem> drops = new ArrayList<>();
        List<GameItem> found = items.get(LootRarity.RARE);
        for(GameItem f : found) {
            boolean foundItem = false;
            for(GameItem drop : drops) {
                if (drop.getId() == f.getId()) {
                    foundItem = true;
                    break;
                }
            }
            if (!foundItem) {
                drops.add(f);
            }
        }
        return drops;
    }

    @Override
    public Map<LootRarity, List<GameItem>> getLoot() {
        return items;
    }

    public static List<GameItem> getRandomItems(boolean rollRares, int size) {
        List<GameItem> rewards = Lists.newArrayList();
        int rareChance = 4;
        if (AvatarOfCreation.activeKronosSeed) {
            rareChance = 5;
        }

        if (rollRares && Misc.trueRand(rareChance) == 0) {
            rewards.add(Misc.getRandomItem(items.get(LootRarity.RARE)));
        } else {
            for (int count = 0; count < 3; count++) {
                rewards.add(Misc.getRandomItem(items.get(LootRarity.COMMON)));
            }
        }
        return rewards;
    }

    public static boolean containsRare(List<GameItem> itemList) {
        return items.get(LootRarity.RARE).stream().anyMatch(rareItem -> itemList.stream().anyMatch(itemReward -> rareItem.getId() == itemReward.getId()));
    }

    /**
     * Reward items that are generated when the treasure room is initialised.
     */
    public static void rewardItems(Player player, List<GameItem> rewards) {
        BossPoints.addManualPoints(player, "Tombs Of Amascut");

        PetHandler.roll(player, PetHandler.Pets.LIL_ZIK);
        player.getEventCalendar().progress(EventChallenge.COMPLETE_TOB);
       LeaderboardUtils.addCount(LeaderboardType.TOB, player, 1);
        Achievements.increase(player, AchievementType.TOB, 1);
        if (AvatarOfCreation.activeKronosSeed == true) {
            player.sendMessage("@red@The @gre@Kronos seed@red@ doubles your chances!" );
        }
        player.getItems().addItem(995, 500_000 + Misc.random(1_000_000));
        List<GameItem> rareItemList = items.get(LootRarity.RARE);
        for (GameItem reward : rewards) {
            if (rareItemList.stream().anyMatch(rareItem -> reward.getId() == rareItem.getId())) {
                if (!player.getDisplayName().equalsIgnoreCase("thimble") && !player.getDisplayName().equalsIgnoreCase("top hat")) {
                    PlayerHandler.executeGlobalMessage("@pur@" + player.getDisplayNameFormatted() + " received a drop: "
                            + ItemDef.forId(reward.getId()).getName() + " x " + reward.getAmount() + " from Tombs Of Amascut.");
                }
                player.getCollectionLog().handleDrop(player, Npcs.BABA, rewards.get(0).getId(), 1);
            }
        }

        for (GameItem item : rewards) {
            player.getInventory().addAnywhere(new ImmutableItem(item.getId(), item.getAmount()));
        }

        player.getTobContainer().displayRewardInterface(rewards);
    }

    /**
     * To be removed but kept for now.
     */
    @Override
    public void roll(Player player) {}
}

