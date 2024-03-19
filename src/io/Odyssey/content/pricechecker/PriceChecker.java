package io.Odyssey.content.pricechecker;

import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class PriceChecker {

    private static int getFramesForSlot[][] = {{0, 28353}, {1, 28356},
            {2, 28359}, {3, 28362}, {4, 28365}, {5, 28368},
            {6, 28371}, {7, 28374}, {8, 28377}, {9, 28380},
            {10, 28383}, {11, 28386}, {12, 28389}, {13, 28392},
            {14, 28395}, {15, 28398}, {16, 28401}, {17, 28404},
            {18, 28407}, {19, 28410}, {20, 28413}, {21, 28416},
            {22, 28419}, {23, 28422}, {24, 28425}, {25, 28428},
            {26, 28431}, {27, 28434}};

    public static int arraySlot(Player c, int[] array, int target) {
        int spare = -1;
        for (int x = 0; x < array.length; x++) {
            if (array[x] == target && c.getItems().isStackable(target)) {
                return x;
            } else if (spare == -1 && array[x] <= 0) {
                spare = x;
            }
        }
        return spare;
    }

    public static void clearConfig(Player c) {
        for (int x = 0; x < c.price2.length; x++) {
            if (c.price2[x] > 0)
                withdrawItem(c, c.price2[x], x, c.priceN[x]);
        }

        c.getItems().resetItems(5064);
        c.getItems().resetTempItems();
    }

    public static void depositItem(Player c, int id, int amount) {
        int slot = arraySlot(c, c.price2, id);
//        for (int j = 0; j < Config.ITEM_SELLABLE.length; j++) {
//            if (id == Config.ITEM_SELLABLE[j]) {
//                c.sendMessage("This item is untradeable.");
//                return;
//            }
//        }
       if(!ItemDef.forId(id).isTradable()){
           c.sendMessage("This item is untradeable.");
           return;
       }

        if (c.getItems().getItemAmount(id) < amount) {
            amount = c.getItems().getItemAmount(id);
        }
        if (slot == -1) {
            c.sendMessage("The price-checker is currently full.");
            return;
        }
        if (!c.getItems().isStackable(id)) {
            amount = 1;
        }
        if (!c.getItems().playerHasItem(id, amount)) {
            return;
        }
        c.getItems().deleteItem2(id, amount);
        if (c.price2[slot] != id) {
            c.price2[slot] = id;
            c.priceN[slot] = amount;
        } else {
            c.price2[slot] = id;
            c.priceN[slot] += amount;
        }
        c.total += c.getShops().getItemShopValue(id) * amount;
        updateChecker(c);
    }


    public static void open(Player c) {
        c.isChecking = true;
        c.total = 0;
        c.getPA().sendFrame126("Total value: " + Misc.insertCommasToNumber(String.valueOf(c.total)), 28351);

        updateChecker(c);
        resetFrames(c);
        sendInventoryItems(c);
        c.getPA().sendFrame248(43933, 5063);
      //  System.out.println("here?");
      //  c.getPA().sendFrame248(43933, 5063);
    }

    public static void resetFrames(Player c) {
        for (int x = 0; x < 28; x++) {
            if (c.price2[x] <= 1) {
                setFrame(c, x, getFramesForSlot[x][1], c.price2[x], c.priceN[x],
                        false);
            }
        }
    }

    private static void setFrame(Player player, int slotId, int frameId,
                                 int itemId, int amount, boolean store) {
        int totalAmount = player.getShops().getItemShopValue(itemId) * amount;
        String total = Misc.insertCommasToNumber(Integer.toString(totalAmount));
        if (!store) {
            player.getPA().sendFrame126("", frameId);
            player.getPA().sendFrame126("", frameId + 1);
            player.getPA().sendFrame126("", frameId + 2);
            return;
        }
        if (player.getItems().isStackable(itemId)) {
            player.getPA().sendFrame126(amount + " x " + Misc.insertCommasToNumber(Integer.toString(player.getShops().getItemShopValue(itemId))), frameId);
            player.getPA().sendFrame126("= " + total, frameId + 1);
            player.getPA().sendFrame126("", frameId + 2);
        } else {
            player.getPA()
                    .sendFrame126(
                            ""
                                    + Misc.insertCommasToNumber(Integer
                                    .toString(player.getShops()
                                            .getItemShopValue(itemId)))
                                    + "", frameId);
            player.getPA().sendFrame126("", frameId + 1);
            player.getPA().sendFrame126("", frameId + 2);
        }
    }
    public static void sendInventoryItems(Player player) {
        player.getItems().resetItems(5064);

        player.getItems().resetTempItems();
    }

    public static void updateChecker(Player c) {
        sendInventoryItems(c);
        for (int x = 0; x < 28; x++) {
            if (c.priceN[x] <= 0) {
                c.getPA().itemOnInterface(-1, -1, 28546, x);
            } else {
                c.getPA().itemOnInterface(c.price2[x], c.priceN[x], 28546, x);
                c.getPA().sendFrame126("", 28352);

                for (int frames = 0; frames < getFramesForSlot.length; frames++) {
                    if (x == getFramesForSlot[frames][0]) {
                        setFrame(c, x, getFramesForSlot[frames][1], c.price2[x],
                                c.priceN[x], true);
                    }
                }
            }
        }
        c.getPA().sendFrame126("Total value: " + Misc.insertCommasToNumber(String.valueOf(c.total < 0 ? 0 : c.total)), 28351);
    }

    public static void withdrawItem(Player c, int removeId, int slot, int amount) {
        if (!c.isChecking)
            return;
        if (c.price2[slot] != removeId) {
            return;
        }
        if (!c.getItems().isStackable(c.price2[slot]))
            amount = 1;
        if (amount > c.priceN[slot] && c.getItems().isStackable(c.price2[slot]))
            amount = c.priceN[slot];
        if (c.price2[slot] >= 0 && c.getItems().freeSlots() > 0) {
            c.getItems().addItem(c.price2[slot], amount);
            if (c.getItems().isStackable(c.price2[slot])
                    && c.getItems().playerHasItem(c.price2[slot], amount)) {
                c.priceN[slot] -= amount;
                c.price2[slot] = c.priceN[slot] <= 0 ? 0 : c.price2[slot];
            } else {
                c.priceN[slot] = 0;
                c.price2[slot] = 0;
            }
        }
        c.total -= c.getShops().getItemShopValue(removeId) * amount;
        for (int frames = 0; frames < getFramesForSlot.length; frames++) {
            if (slot == getFramesForSlot[frames][0]) {
                if (c.priceN[slot] >= 1) {
                    setFrame(c, slot, getFramesForSlot[frames][1],
                            c.price2[slot], c.priceN[slot], true);
                } else {
                    setFrame(c, slot, getFramesForSlot[frames][1],
                            c.price2[slot], c.priceN[slot], false);
                }
            }
        }
        updateChecker(c);
    }

    public static void depositall(Player c) {
        for (int slot = 0; slot < c.playerItems.length; slot++) {
            if (c.playerItems[slot] > 0 && c.playerItemsN[slot] > 0) {
                depositItem(c, c.playerItems[slot] - 1, c.playerItemsN[slot]);
                //c.getItems().addToBank(c.playerItems[slot] - 1, c.playerItemsN[slot], false);
            }
        }        // TODO Auto-generated method stub

    }
}