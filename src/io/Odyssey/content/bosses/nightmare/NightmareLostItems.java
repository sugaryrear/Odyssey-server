package io.Odyssey.content.bosses.nightmare;


import io.Odyssey.Server;
import io.Odyssey.content.combat.death.PlayerDeath;
import io.Odyssey.content.combat.pvp.PkpRewards;
import io.Odyssey.content.itemskeptondeath.ItemsLostOnDeath;
import io.Odyssey.content.itemskeptondeath.ItemsLostOnDeathList;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.model.items.bank.BankItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("serial")
public class NightmareLostItems extends ArrayList<GameItem> {

    /**
     * The player that has lost items
     */
    private final Player player;

    /**
     * Creates a new class for managing lost items by a single player
     *
     * @param player the player who lost items
     */
    public NightmareLostItems(final Player player) {
        this.player = player;
    }

    /**
     * Stores the players items into a list and deletes their items
     */
    public void store() {


        //   Player playerKiller = killer != null && killer.isPlayer() ? killer.asPlayer() : null;
//create list
        ItemsLostOnDeathList itemsLostOnDeathList = ItemsLostOnDeath.generateModified(player);


        //delete ALL the items
        player.getItems().deleteAllItems();

        //list of all lost items
        List<GameItem> lostItems = itemsLostOnDeathList.getLost();


        lostItems.forEach(item ->   add(new GameItem(item.getId(),item.getAmount())));


        player.getPA().movePlayer(3808,9744,1);
        PlayerDeath.onRespawn(player);

        //add the kept items after u respwan - nah
//        for (GameItem item : itemsLostOnDeathList.getKept()) {
//            if (player.getItems().hasRoomInInventory(item.getId(), item.getAmount())) {//very good way of doing "if you have space in your inventory"
//                player.getItems().addItem(item.getId(), item.getAmount());
//            } else {
//                player.getItems().sendItemToAnyTab(item.getId(), item.getAmount());
//            }
//        }
        player.talkingNpc = 9413;
        player.getDH().sendNpcChat("It looks like Nightmare beat you.", "I'll give you your items back for "+price+" GP.",
                "Talk to me when you're ready.");
        player.nextChat = -1;

    }

    public void whatitemsdoihave() {
        player.getPA().closeAllWindows();
        for (GameItem item : this) {
            player.sendMessage(item.getAmount()+"x " + ItemAssistant.getItemName(item.getId()));

        }

    }
    public static int price =  100_000;
    public void retain() {

        if (!player.getItems().playerHasItem(995, price)) {
            player.talkingNpc = 9414;
            player.getDH().sendNpcChat("You need at least "+price+" gp to claim your items.");
            player.nextChat = -1;
            return;
        }
        for (GameItem item : this) {
            if (player.getMode().isUltimateIronman()) {
                if (!player.getItems().addItem(item.getId(), item.getAmount())) {
                    player.sendMessage("<col=CC0000>1x " + ItemAssistant.getItemName(item.getId()) + " has been dropped on the ground.</col>");
                    Server.itemHandler.createGroundItem(player, item.getId(), player.getX(), player.getY(), player.heightLevel, item.getAmount());
                }
            } else {
                player.getItems().sendItemToAnyTabOrDrop(new BankItem(item.getId(), item.getAmount()), player.getX(), player.getY());
            }
        }
        clear();
        player.getItems().deleteItem2(995, price);
        player.talkingNpc = 9414;
        if (player.getMode().isUltimateIronman()) {
            player.getDH().sendNpcChat("You have retained all of your lost items.", "Your items are in your inventory.",
                    "@red@If there was not enough space, they were dropped.");
        } else {
            player.getDH().sendNpcChat("You have retained all of your lost items for "+price+" gp.", "Your items are in your bank.");
        }
        player.nextChat = -1;
    }

}
