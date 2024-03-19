package io.Odyssey.content.tasks.rewards;



import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.Misc;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.Odyssey.model.Items.*;
public class TaskReward {

    private static final List<GameItem> COMMON = Arrays.asList(


            new GameItem(IRON_BAR_NOTED,100),
            new GameItem(RANARR_WEED_NOTED,80),
            new GameItem(UNCUT_ONYX_NOTED,2),
            new GameItem(2510,200),
            new GameItem(RUNITE_BAR+1,50),
            new GameItem(BLACK_DRAGON_LEATHER_NOTED,30)
    );

    private static final List<GameItem> UNCOMMON = Arrays.asList(
            new GameItem(RAW_MANTA_RAY+1,100)
//            new GameItem(ABYSSAL_DAGGER),
//            //  new Item(STAFF_OF_THE_DEAD),
//            //     new Item(TOXIC_BLOWPIPE),
//            //  new Item(TOXIC_STAFF_OF_THE_DEAD),
//            //  new Item(ARMADYL_CROSSBOW),
//            //     new Item(SERPENTINE_HELM),
//            //  new Item(DINHS_BULWARK),
//            new GameItem(NEITIZNOT_FACEGUARD),
//            new GameItem(DRAGONFIRE_WARD),
//            new GameItem(SARADOMIN_GODSWORD),
//            new GameItem(ZAMORAK_GODSWORD),
//            new GameItem(BANDOS_GODSWORD)
    );

    private static final List<GameItem> RARE = Arrays.asList(
            new GameItem(SILK+1,100)
//            new GameItem(HARMONISED_ORB),
//            new GameItem(ELDRITCH_ORB),
//            new GameItem(VOLATILE_ORB),
//            // new Item(NIGHTMARE_STAFF),
//            new GameItem(ELDER_MAUL),
//            new GameItem(MORRIGANS_COIF),
//            new GameItem(MORRIGANS_LEATHER_BODY),
//            new GameItem(MORRIGANS_LEATHER_CHAPS),
//            new GameItem(ZURIELS_HOOD),
//            new GameItem(ZURIELS_ROBE_TOP),
//            new GameItem(ZURIELS_ROBE_BOTTOM),
//            new GameItem(STATIUSS_FULL_HELM),
//            new GameItem(STATIUSS_PLATEBODY),
//            new GameItem(STATIUSS_PLATELEGS),
//            new GameItem(STATIUSS_WARHAMMER),
//            new GameItem(VESTAS_CHAINBODY),
//            new GameItem(VESTAS_PLATESKIRT),
//            new GameItem(VESTAS_SPEAR),
//            new GameItem(VESTAS_LONGSWORD),
//            new GameItem(DRAGON_CLAWS),
//            new GameItem(ARMADYL_GODSWORD),
//            new GameItem(HEAVY_BALLISTA),
//            new GameItem(AMULET_OF_TORTURE),
//            new GameItem(NECKLACE_OF_ANGUISH),
//            new GameItem(RING_OF_SUFFERING),
//            new GameItem(TORMENTED_BRACELET),
//            new GameItem(12877, 1),
//            new GameItem(12883, 1),
//            new GameItem(12875, 1),
//            new GameItem(12873, 1),
//            new GameItem(12879, 1),
//            new GameItem(12881, 1)
    );

    public static List<GameItem> getPossibleRewards() {

        List<GameItem> l3 = new ArrayList<GameItem>();
        l3.addAll(COMMON);
        l3.addAll(UNCOMMON);
        l3.addAll(RARE);

        return new ArrayList<>(l3);
    }

    public static void reward(Player player) {
        var bmReward = 15_000;
        List<GameItem> items;
        if (Misc.rollDie(100, 1)) {
            items = RARE;
        } else if (Misc.rollDie(50, 1)) {
            items = UNCOMMON;
        } else {
            items = COMMON;
        }
        items = COMMON;
        GameItem item = Misc.randomElement(items);
//        if (item.getValue() >= 30000 && !player.getUsername().equalsIgnoreCase("Box test")) {
//            boolean amOverOne = item.getAmount() > 1;
//            String amtString = amOverOne ? "x " + Utils.format(item.getAmount()) + "" : Utils.getAOrAn(item.name());
//            String msg = "<img=1081> <col=AD800F>" + player.getUsername() + " has received " + amtString + "<shad=0> " + item.name() + "</shad>!";
//            World.getWorld().sendWorldMessage(msg);
//        }

//        var blood_reaper = player.hasPetOut("Blood Reaper pet");
//        if(blood_reaper) {
//            int extraBM = bmReward * 10 / 100;
//            bmReward += extraBM;
//        }
player.getItems().addItem(item);
     //   player.inventory().addOrBank(new Item(BLOOD_MONEY, bmReward), item);
    }
}
