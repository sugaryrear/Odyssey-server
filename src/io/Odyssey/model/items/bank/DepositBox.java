package io.Odyssey.model.items.bank;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import io.Odyssey.Server;
import io.Odyssey.content.lootbag.LootingBagItem;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

import io.Odyssey.model.multiplayersession.MultiplayerSessionFinalizeType;
import io.Odyssey.model.multiplayersession.MultiplayerSessionStage;
import io.Odyssey.model.multiplayersession.MultiplayerSessionType;
import io.Odyssey.model.multiplayersession.duel.DuelSession;

import static io.Odyssey.model.Items.LOOTING_BAG;


public class DepositBox {

    public Player player;
    public List<LootingBagItem> items;

    public int selectedItem = -1;
    public int selectedSlot = -1;

    public DepositBox(Player player) {
        this.player = player;
        items = new ArrayList<>();
    }

    /**
     * Checks wether or not a player is allowed to configure the safe box
     * @return
     */
    public boolean configurationPermitted() {
        if (player.getBankPin().requiresUnlock()) {
            player.getBankPin().open(2);
            return false;
        }
//        if (player.getTutorial().isActive()) {
//        player.getTutorial().refresh();
//        return false;
//        }
        DuelSession duelSession = (DuelSession) Server.getMultiplayerSessionListener().getMultiplayerSession(player,
                MultiplayerSessionType.DUEL);
        if (Objects.nonNull(duelSession) && duelSession.getStage().getStage() > MultiplayerSessionStage.REQUEST
                && duelSession.getStage().getStage() < MultiplayerSessionStage.FURTHER_INTERATION) {
            player.sendMessage("Your actions have declined the duel.");
            duelSession.getOther(player).sendMessage("The challenger has declined the duel.");
            duelSession.finish(MultiplayerSessionFinalizeType.WITHDRAW_ITEMS);
            return false;
        }
        if (Server.getMultiplayerSessionListener().inSession(player, MultiplayerSessionType.TRADE)) {
            player.sendMessage("You must decline the trade to start walking.");
            return false;
        }
        if (player.isStuck) {
            player.isStuck = false;
            player.sendMessage("@red@You've disrupted stuck command, you will no longer be moved home.");
            return false;
        }
        return true;
    }

    public boolean handleButton(int buttonId) {
        if (buttonId == 35002) {
            closedepositbox();
            return true;
        }
        if (buttonId == 42109) {
            depositall();
            return true;
        }
        if (buttonId == 42110) {
            depositEquipment();
            return true;
        }
        return false;
    }

    /**
     * Opens the check looting bag interface
     */
    public void openDepositBox() {
        items.clear();
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        onClose();
        sendItems();

        player.getItems().resetItems(5064);
        player.getItems().resetTempItems();
        player.getPA().sendFrame248(35000, 5063);

        player.inSafeBox = true;
    }

    public void removeMultipleItemsFromBag(int id, int amount) {
        if (amount >= Integer.MAX_VALUE) {
            amount = countItems(id);
        }
//			if (!player.inClanWarsSafe() && id != 13307) {
//				player.sendMessage("You are only able to withdraw blood money from here.");
//				return;
//			}
        int count = 0;
        while (containsItem(id)) {
            if (!withdraw(id, amount)) {
                return;
            }
            if (player.getItems().isStackable(id)) {
                count += amount;
            } else {
                count++;
            }
            if (count >= amount) {
                return;
            }
        }
    }

    public boolean containsItem(int id) {
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public int findIndexInLootBag(int id) {
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                return items.indexOf(item);
            }
        }
        return -1;
    }

    /**
     * Handles withdrawal from the lootingbag
     * @param id		The id of the item being withdrawn
     * @param amount	The amount of the item being withdrawn
     */
    public boolean withdraw(int id, int amount) {
        int index = findIndexInLootBag(id);
        int amountToAdd = 0;
//			if (!player.inClanWarsSafe() && id != 13307) {
//				player.sendMessage("You are only able to withdraw blood money from here.");
//				return false;
//			}
        if (items.size() <= 0) {
            return false;
        }
        if (index == -1) {
            return false;
        }
        LootingBagItem item = items.get(index);
        if (item == null) {
            return false;
        }
        if (item == null || item.getId() <= 0 || item.getAmount() <= 0 || player.getItems().freeSlots() <= 0) {
            return false;
        }
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return false;
        }
        if (player.getItems().getItemCount(id, false) + amount >= Integer.MAX_VALUE || player.getItems().getItemCount(id, false) + amount <= 0) {
            return false;
        }
        if ((items.get(items.indexOf(item)).getAmount()) > amount) {
            amountToAdd = amount;
            items.get(items.indexOf(item)).incrementAmount(-amount);
        } else {
            amountToAdd = item.getAmount();
            items.remove(index);
        }
        player.getPA().sendFrame126("" + (items.size()) + "/"+ player.safeBoxSlots, 35005);
        player.getItems().addItem(item.getId(), amountToAdd);
        sendItems();
        sendInventoryItems();
        return true;
    }

    /**
     * Handles depositing of items into the looting bag
     * @param id		The id of the item being deposited
     * @param amount	The amount of the item being deposited
     */
    public void deposit(int id, int amount, boolean atonce) {
        if (amount >= Integer.MAX_VALUE) {
            amount = player.getItems().getItemCount(id, false);
        }

        int bagSpotsLeft = player.safeBoxSlots - items.size();
        boolean stackable = player.getItems().isStackable(id);
        boolean bagContainsItem = containsItem(id);
        if (amount > bagSpotsLeft && !stackable) {
            if (!(stackable && bagContainsItem)) {
                amount = bagSpotsLeft;
            }
        }
        if (!player.getItems().playerHasItem(id)) {
            return;
        }

        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (countItems(id) + amount >= Integer.MAX_VALUE || countItems(id) + amount <= 0) {
            return;
        }

        //  player.getPA().sendFrame126("" + (items.size() + 1) + "/"+ player.safeBoxSlots, 35005);
        //     System.out.println("id: "+id);
        //   player.getItems().sendItemToAnyTab(id, amount);//adds it to the bankj
        player.getItems().addToBankdepositbox(id,amount,false,false);
        //add here!
       // player.getItems().deleteItem2(id,amount);
        addItemToList(id,amount);
//        List<Integer> amounts = player.getItems().deleteItemAndReturnAmount(id, amount);
//
//        int count = 0;
//        for (int amt : amounts) {
//            if (!addItemToList(id, amt, true)) {
//            break;
//            }
//        count++;
//            if (count >= amount) {
//            break;
//            }
//        }
        // addItemToList(id, amount);


       // if(!atonce){
            resetItems();
            sendItems();
            sendInventoryItems();
      //  }

    }



    public void depositLootingBag(int id, int amount) {
        if (amount >= Integer.MAX_VALUE) {
            amount = player.getItems().getItemCount(id, false);
        }

        int bagSpotsLeft = player.safeBoxSlots - items.size();
        boolean stackable = player.getItems().isStackable(id);
        boolean bagContainsItem = containsItem(id);
        if (amount > bagSpotsLeft && !stackable) {
            if (!(stackable && bagContainsItem)) {
                amount = bagSpotsLeft;
            }
        }

        if (items.size() >= player.safeBoxSlots) {
            if (player.safeBoxSlots < 50) {
                player.sendMessage("The safe-box cannot hold anymore items, purchase more by clicking the +.");
                return;
            } else {
                player.sendMessage("The safe-box cannot hold anymore items.");
                return;
            }
        }
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (countItems(id) + amount >= Integer.MAX_VALUE || countItems(id) + amount <= 0) {
            return;
        }

        player.getPA().sendFrame126("" + (items.size() + 1) + "/"+ player.safeBoxSlots, 35005);
        List<Integer> amounts = player.getItems().deleteItemAndReturnAmount(id, amount);

        int count = 0;
        for (int amt : amounts) {
            if (!addItemToListLootingBag(id, amt)) {//what actually adds it to the array
                break;
            }
            count++;
            if (count >= amount) {
                break;
            }
        }

        resetItems();
        sendItems();
        sendInventoryItems();


    }

    public int countItems(int id) {
        int count = 0;
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                count += item.getAmount();
            }
        }
        return count;
    }

    public void addItemToList(int id, int amount) {
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                if (item.getAmount() + amount >= Integer.MAX_VALUE) {
                    return ;
                }
                if (player.getItems().isStackable(id)) {
                    item.incrementAmount(amount);
                    return ;
                }
            }
        }
        items.add(new LootingBagItem(id, amount));
        return;
    }



    public boolean addItemToListLootingBag(int id, int amount) {
        for (LootingBagItem item : items) {
            if (item.getId() == id) {
                if (item.getAmount() + amount >= Integer.MAX_VALUE) {
                    return false;
                }
                if (player.getItems().isStackable(id)) {
                    item.incrementAmount(amount);
                    return true;
                }
            }
        }
        items.add(new LootingBagItem(id, amount));
        sendItems();
        player.getItems().addItemToBankOrDrop(id, amount);
        return true;
    }
    /**
     * Closing the looting bag and resetting
     */
    public void closedepositbox() {
        player.setSidebarInterface(3, 3213);
        player.getPA().closeAllWindows();
    }

    public void onClose() {
        player.inSafeBox = false;
    }

    public void depositall() {
        if (System.currentTimeMillis() - player.lastBankDeposit < 5000)
            return;

        player.lastBankDeposit = System.currentTimeMillis();

        for (int slot = 0; slot < player.playerItems.length; slot++) {
            if (player.playerItems[slot] > 0 && player.playerItemsN[slot] > 0) {

                deposit(player.playerItems[slot] - 1, player.playerItemsN[slot],true);
            }
        }
        resetItems();
        sendItems();
        sendInventoryItems();

    }

    public void depositEquipment() {
        for (int slot = 0; slot < player.playerEquipment.length; slot++) {
            if (player.playerEquipment[slot] > 0 && player.playerEquipmentN[slot] > 0) {
                deposit(player.playerEquipment[slot], player.playerEquipmentN[slot], true);
//                    if (player.getItems().addEquipmentToSafeBox(player.playerEquipment[slot], slot, player.playerEquipmentN[slot], false)) {
//                        player.getItems().equipItem(-1, 0, slot);
//                        } else {
//                        player.sendMessage("Your bank is full.");
//                        break;
//                    }

                //deposit(player.playerEquipment[slot], player.playerEquipmentN[slot]);
            }
        }
    }

    public void depositAllLootBagtoSafebox() {
        if (!player.getItems().playerHasItem(LOOTING_BAG)) {
            player.sendMessage("You do not have a looting bag in your inventory.");

            return;
        }
        if (!configurationPermitted()) {
            player.sendMessage("You cannot do this right now.");
            return;
        }
        if (player.wildLevel >= 30 && player.getPosition().inWild()) {//check for resource area?
            player.sendMessage("You cannot do this above level 30 wilderness.");
            return;
        }

        if (!player.getPosition().inBank()) {
            //  player.sendMessage("You must be at a bank to do this, or purchase the @red@Loot Bag Anywhere @bla@perk in order to");
            player.sendMessage("bank items from anywhere.");

            return;
        }
        if (System.currentTimeMillis() - player.lastBankDeposit < 1000)
            return;
        player.lastBankDeposit = System.currentTimeMillis();
        if (items.isEmpty()) {
            player.sendMessage("You don't have any items in your lootbag.");
            return;
        }
        for (Iterator<LootingBagItem> iterator = items.iterator(); iterator.hasNext(); ) {
            LootingBagItem item = iterator.next();
            if (item == null) {
                continue;
            }
            if (item.getId() <= 0 || item.getAmount() <= 0) {
                continue;
            }
            player.getDepositBox().addItemToListLootingBag(item.getId(), item.getAmount());
            // player.getItems().addItemToBank(item.getId(), item.getAmount());
            //player.sendMessage("Completed.");
            iterator.remove();
        }
        // updateTotalCost();
        //  sendItems();
    }
    public void sendItems() {
        final int START_ITEM_INTERFACE = 35007;
        for (int i = 0; i < player.safeBoxSlots; i++) {
            int id = 0;
            int amt = 0;

            if (i < items.size()) {
                LootingBagItem item = items.get(i);
                if (item != null) {
                    id = item.getId();
                    amt = item.getAmount();
                }
            }

            if (id <= 0) {
                id = -1;
            }

            player.getPA().itemOnInterface(id, amt, START_ITEM_INTERFACE, i);
        }
    }

    public void sendInventoryItems() {
        player.getItems().resetItems(5064);

        player.getItems().resetTempItems();
    }

    @SuppressWarnings("unused")
    private String getShortAmount(int amount) {
        if (amount <= 1) {
            return "";
        }
        String amountToString = "" + amount;
        if (amount > 1000000000) {
            amountToString = "@gre@" + (amount / 1000000000) + "B";
        } else if (amount > 1000000) {
            amountToString = "@gre@" + (amount / 1000000) + "M";
        } else if (amount > 1000) {
            amountToString = "@whi@" + (amount / 1000) + "K";
        }
        return amountToString;
    }

    private void resetItems() {
        player.getItems().resetItems(3214);

        player.getPA().requestUpdates();
    }

}
