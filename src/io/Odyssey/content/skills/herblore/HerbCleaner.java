package io.Odyssey.content.skills.herblore;

import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Items;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.SlottedItem;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.Misc;

/**
 * @author Chris | 7/5/21
 */
public class HerbCleaner {

    private static final int CLEAN_HERB_PRICE = 100;
    private static final int PRICE_WARN_AMOUNT = 20_000;

    public static int getPriceForInventory(Player player) {
        return player.getItems().getInventoryItems().stream().mapToInt(it -> {
            Herb herb = Herb.forNotedOrUnNotedGrimyHerb(it.getId());
            return herb != null ? CLEAN_HERB_PRICE * it.getAmount() : 0;
        }).sum();
    }

    public static void cleanHerbsFromInventory(Player player) {
        player.getPA().closeAllWindows();
        for (SlottedItem item : player.getItems().getInventoryItems()) {
            Herb herb = Herb.forNotedOrUnNotedGrimyHerb(item.getId());
            if (herb == null)
                continue;
            if (!cleanHerb(player, item, herb, false)) {
                break;
            }
        }
    }

    /**
     * @return if {@param warn} set to false, return true if potion was made successfully.
     *         If {@param warn} is true and dialogue sent, return true.
     */
    public static boolean cleanHerb(Player player, SlottedItem gameItem, Herb herb, boolean warn) {
        ItemDef usedItemDef = ItemDef.forId(gameItem.getId());

        int price = gameItem.getAmount() * CLEAN_HERB_PRICE;
        if (warn && price >= PRICE_WARN_AMOUNT) {
            String priceString = Misc.formatCoins(price);
            new DialogueBuilder(player).option(
                    "Spend " + priceString + "?",
                    new DialogueOption("Yes, spend " + priceString + " to clean these herbs.", plr -> clean(player, gameItem, usedItemDef, herb, price)),
                    new DialogueOption("No, that's too much!", plr -> plr.getPA().closeAllWindows())
            ).send();
            return true;
        }

        return clean(player, gameItem, usedItemDef, herb, price);
    }

    /**
     * @return true if the potion was made successfully.
     */
    private static boolean clean(Player player, SlottedItem gameItem, ItemDef usedItemDef, Herb herb, int price) {
        if (!player.getItems().playerHasItem(gameItem.getId(), gameItem.getAmount()))
            return false;

        if (player.getLevelForXP(player.playerXP[Player.playerHerblore]) < herb.getLevel()) {
            player.sendMessage("You need a Herblore level of " + herb.getLevel() + " to clean this herb.");
            player.getPA().closeAllWindows();
            return false;
        }

        if (!player.getItems().playerHasItem(Items.COINS, price)) {
            player.sendMessage("You don't have enough coins to do that!");
            return false;
        }

        int unfPotionId = herb.getClean();
        ItemDef unfPotionDef = ItemDef.forId(unfPotionId);

        GameItem unfPotionsItem = new GameItem(usedItemDef.isNoted() ? unfPotionDef.getNotedItemIfAvailable() : unfPotionDef.getId(), gameItem.getAmount());

        player.getItems().deleteItem(Items.COINS, price);
        player.getItems().deleteItem(gameItem.getId(), gameItem.getAmount());
        player.getItems().addItemUnderAnyCircumstance(unfPotionsItem.getId(), unfPotionsItem.getAmount());
        return true;
    }
}
