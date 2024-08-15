package io.Odyssey.model.entity.npc.pets;

import java.util.ArrayList;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ItemAssistant;


@SuppressWarnings("serial")
public class PakYak extends ArrayList<GameItem> {

    /**
     * The player that has lost items
     */
    private final Player player;
    public boolean depositopen;

    /**
     * Creates a new class for managing lost items by a single player
     *
     * @param player the player who lost items
     */
    public PakYak(final Player player) {
        this.player = player;
    }

    /**
     * Stores the players items into a list and deletes their items
     */
    public void store(int itemId, int amount) {
        player.getItems().deleteItem2(itemId,amount);


        add(new GameItem(itemId,amount));


        player.sendMessage("You added: "+ItemAssistant.getItemName(itemId)+". ");


        player.sendMessage("You currently have: ");
        for (GameItem item : this) {
            player.sendMessage(item.getAmount()+"x "+ItemAssistant.getItemName(itemId));
        }


//        for (int i = 0; i < player.playerItems.length; i++) {
//            if (player.playerItems[i] < 1) {
//                continue;
//            }
//            add(new GameItem(player.playerItems[i] - 1, player.playerItemsN[i]));
//        }
//        for (int i = 0; i < player.playerEquipment.length; i++) {
//            if (player.playerEquipment[i] < 1) {
//                continue;
//            }
//            add(new GameItem(player.playerEquipment[i], player.playerEquipmentN[i]));
//        }
//        player.getItems().deleteEquipment();
//        player.getItems().deleteAllItems();
    }
    public void openDepositMode() {
//        if (!canUseLootingBag()) {
//            player.sendMessage("You cannot do this right now.");
//            return;
//        }
//        if (!player.getItems().playerHasItem(LOOTING_BAG) && !player.getItems().playerHasItem(LOOTING_BAG_OPEN)) {
//            return;
//        }
//        if (player.getPosition().inClanWars() || player.getPosition().inClanWarsSafe()) {
//            return;
//        }
//        if (Boundary.isIn(player, Boundary.TOURNAMENT_LOBBIES_AND_AREAS)) {
//            player.sendMessage("You cannot do this right now.");
//            return;
//        }
//        if (!player.getPosition().inWild()) {
//            player.sendMessage("You can only do this in the wilderness.");
//            return;
//        }
      //  reset();
       // depositopen = true;
        updateItemContainers();
        player.getPA().sendTabAreaOverlayInterface(95000);

     //   setDepositInterfaceOpen(true);
    }

    int DEPOSIT_INTERFACE_ID = 95448;//the ID of the itemContainer!
    int WITHDRAW_INTERFACE_ID = 95349;//the ID of the itemContainer!
    private void updateItemContainers() {
        player.getItems().sendInventoryInterface(DEPOSIT_INTERFACE_ID);
        player.getItems().sendItemContainer(WITHDRAW_INTERFACE_ID, this);
        player.getPA().showInterface(95342);
        //updateTotalCost();
    }
    public boolean handleClickItem(int id, int amount, int interfaceid) {

        if(interfaceid == 95448){//depositing into
            if (player.getItems().playerHasItem(id, amount)){
                store(id, amount);
                updateItemContainers();
                return true;
            }

        }
        if(interfaceid == 95349){//withdrawing from
            System.out.println("here withdrawing!");
//            if (player.getItems().playerHasItem(id, amount)){
//                store(id, amount);
//                updateItemContainers();
//                return true;
//            }

        }

return false;
    }
    public void retain() {
//        int price = player.getRechargeItems().hasItem(13144) && player.getRechargeItems().useItem(13144) || player.getRechargeItems().hasItem(13143) && player.getRechargeItems().useItem(13143) ? -1 : 500_000;
//        if (!player.getItems().playerHasItem(995, price)) {
//            player.talkingNpc = 2040;
//            player.getDH().sendNpcChat("You need at least 500,000GP to claim your items.");
//            return;
//        }
//        for (GameItem item : this) {
//            if (player.getMode().isUltimateIronman()) {
//                if (!player.getItems().addItem(item.getId(), item.getAmount())) {
//                    player.sendMessage("<col=CC0000>1x " + ItemAssistant.getItemName(item.getId()) + " has been dropped on the ground.</col>");
//                    Server.itemHandler.createGroundItem(player, item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount());
//                }
//            } else {
//                player.getItems().sendItemToAnyTabOrDrop(new BankItem(item.getId(), item.getAmount()), player.getX(), player.getY());
//            }
//        }
//        clear();
//        player.getItems().deleteItem2(995, price);
//        player.talkingNpc = 2040;
//        if (player.getMode().isUltimateIronman()) {
//            player.getDH().sendNpcChat("You have retained all of your lost items for 500,000GP.", "Your items are in your inventory.",
//                    "@red@If there was not enough space, they were dropped.");
//        } else {
//            player.getDH().sendNpcChat("You have retained all of your lost items for 500,000GP.", "Your items are in your bank.");
//        }
//        player.nextChat = -1;
    }

}
