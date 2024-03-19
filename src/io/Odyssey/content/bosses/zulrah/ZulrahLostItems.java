package io.Odyssey.content.bosses.zulrah;

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
public class ZulrahLostItems extends ArrayList<GameItem> {

    /**
     * The player that has lost items
     */
    private final Player player;

    /**
     * Creates a new class for managing lost items by a single player
     *
     * @param player the player who lost items
     */
    public ZulrahLostItems(final Player player) {
        this.player = player;
    }

    /**
     * Stores the players items into a list and deletes their items
     */
    public void store() {


     //   Player playerKiller = killer != null && killer.isPlayer() ? killer.asPlayer() : null;
//create list
        ItemsLostOnDeathList itemsLostOnDeathList = ItemsLostOnDeath.generateModified(player);

//                Server.getLogging().write(new DeathItemsHeld(c, c.getItems().getInventoryItems(), c.getItems().getEquipmentItems()),
//                        new DeathItemsKept(c, itemsLostOnDeathList.getKept()),
//                        new DeathItemsLost(c, itemsLostOnDeathList.getLost()));

        //delete ALL the items
     player.getItems().deleteAllItems();

     //list of all lost items
        List<GameItem> lostItems = itemsLostOnDeathList.getLost();

        // see which items that u had are untradeable
       // List<GameItem> untradeables = lostItems.stream().filter(it -> !it.getDef().isTradable()).collect(Collectors.toList());

        //todo: add dying with pak yak

        // Drop untradeable coins for killer, otherwise drop nothing
//        if (playerKiller != null) {
//            int coins = untradeables.stream().mapToInt(it -> it.getDef().getShopValue()).sum();
//            if (coins > 0)
//                lostItems.add(new GameItem(Items.COINS, coins));
//        }

        //lostItems.removeAll(untradeables);
       // untradeables.forEach(item -> c.getPerduLostPropertyShop().add(c, item));

      //  PlayerDeath.dropItemsForKiller(player, playerKiller, new GameItem(Items.BONES));
     lostItems.forEach(item ->   add(new GameItem(item.getId(),item.getAmount())));






        //   }

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
        player.getPA().movePlayer(2202, 3056, 0);
        PlayerDeath.onRespawn(player);
        //add the kept items after u respwan
        for (GameItem item : itemsLostOnDeathList.getKept()) {
            if (player.getItems().hasRoomInInventory(item.getId(), item.getAmount())) {//very good way of doing "if you have space in your inventory"
                player.getItems().addItem(item.getId(), item.getAmount());
            } else {
                player.getItems().sendItemToAnyTab(item.getId(), item.getAmount());
            }
        }
        player.talkingNpc = 2033;
        player.getDH().sendNpcChat("It looks like Zulrah beat you.", "I'll give you your items back for "+price+" GP.",
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
            player.talkingNpc = 2033;
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
        player.talkingNpc = 2033;
        if (player.getMode().isUltimateIronman()) {
            player.getDH().sendNpcChat("You have retained all of your lost items for 500,000GP.", "Your items are in your inventory.",
                    "@red@If there was not enough space, they were dropped.");
        } else {
            player.getDH().sendNpcChat("You have retained all of your lost items for "+price+" gp.", "Your items are in your bank.");
        }
        player.nextChat = -1;
    }

}
