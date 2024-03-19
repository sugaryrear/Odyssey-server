package io.Odyssey.content.skills.woodcutting;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.util.Misc;

public class BirdNest {


    private final static int[][] TREE_CHANCE = {
            {5312, 0, 203}, {5283, 204, 349}, {5313, 350, 492},
            {5284, 493, 599}, {5285, 600, 679}, {5286, 680, 751},
            {5314, 752, 810}, {5287, 811, 864},  {5288, 865, 907},
            {5289, 908, 939}, {5315, 940, 963}, {5290, 964, 985},
            {5316, 986, 996}, {5317, 996, 1000},
    };

    private final static int[][] RING_CHANCE = {
            {1637, 0, 398}, {1635, 399, 775},
            {1639, 776, 901}, {1641, 902, 992},
            {1643, 993, 1001}
    };

    private final static int[][] WYSON_CHANCE = {
            {5318, 14, 0, 1739}, {5320, 3, 1740, 2763},
            {5322, 6, 2764, 3708}, {5324, 9, 3709, 4559},
            {5319, 11, 4560, 5368}, {5100, 2, 5369, 6166},
            {5321, 2, 6167, 6958}, {5323, 3, 6959, 7693},
            {5312, 1, 7694, 7965}, {5295, 1, 7966, 8151},
            {5313, 1, 8152, 8317}, {5314, 1, 8318, 8415},
            {5315, 1, 8416, 8481}, {5317, 1, 8482, 8491},
            {5316, 1, 8492, 8499}
    };

    public static void handleBirdNest(Player player, int itemId, int nestType) {
        player.sendMessage("You search the nest...");
        player.getItems().replaceItem(player,itemId, 5075);
        switch (nestType) {
            case 0:
                int randomTree = Misc.random(1000);
                for (int i = 0; i < TREE_CHANCE.length; i++) {
                    if (randomTree >= TREE_CHANCE[i][1] && randomTree <= TREE_CHANCE[i][2]) {
                        player.getItems().addItem(TREE_CHANCE[i][0], 1);
                        player.sendMessage("You find a " + ItemAssistant.getItemName(TREE_CHANCE[i][0]) + ".");
                    }
                }
                break;
            case 1:
                int randomRing = Misc.random(1001);
                for (int i = 0; i < RING_CHANCE.length; i++) {
                    if (randomRing >= RING_CHANCE[i][1] && randomRing <= RING_CHANCE[i][2]) {
                        player.getItems().addItem(RING_CHANCE[i][0], 1);
                        player.sendMessage("You find a " + ItemAssistant.getItemName(RING_CHANCE[i][0]) + ".");
                    }
                }
                break;
            case 2:
                int randomWyson = Misc.random(8499);
                for (int i = 0; i < WYSON_CHANCE.length; i++) {
                    if (randomWyson >= WYSON_CHANCE[i][2] && randomWyson <= WYSON_CHANCE[i][3]) {
                        player.getItems().addItem(WYSON_CHANCE[i][0], WYSON_CHANCE[i][1]);
                        player.sendMessage("You find a " + ItemAssistant.getItemName(WYSON_CHANCE[i][0]) + ".");
                    }
                }
                break;
        }
    }


}
