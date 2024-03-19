package io.Odyssey.content.combat.magic;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.util.Misc;

public class TumekensShadow {

    public void activate(Player player, Entity target, Damage damage) {
        player.usingMagic = true;
        player.gfx100(2125);
    }

    private static final int MAX_CHARGES = 20_000;
    public static final int COMBAT_SPELL_INDEX = 99;

    public static boolean useItem(Player player, int item1, int item2) {
        if (item1 == Items.BLOOD_RUNE && item2 == Items.TUMEKENS_SHADOW || item2 == Items.BLOOD_RUNE && item1 == Items.TUMEKENS_SHADOW) {
            charge(player);
            return true;
        }

        return false;
    }

    public static boolean clickItem(Player player, int itemId, int option) {
        if (itemId == Items.TUMEKENS_SHADOW) {
            if (option == 5) {
                uncharge(player);
            } else if (option == 3) {
                charge(player);
            } else if (option == 2) {
                checkChargesRemaining(player);
            }
            return true;
        }
        return false;
    }

    public static void checkChargesRemaining(Player player) {
        player.sendMessage("Your staff has " + Misc.insertCommas(player.getTumekensShadowCharge()) + " charges remaining.");
    }

    private static void charge(Player player) {
        if (player.getItems().playerHasItem(Items.TUMEKENS_SHADOW)) {
            int bloods = player.getItems().getItemAmount(Items.BLOOD_RUNE);
            int currentCharges = player.getTumekensShadowCharge();

            if (bloods == 0) {
                player.sendMessage("You don't have any blood runes!");
                return;
            }

            if (currentCharges >= MAX_CHARGES) {
                player.sendMessage("You have already stored 20,000 charges, you can't store any more!");
                return;
            }

            int chargesToAdd = bloods;
            if (currentCharges + bloods > MAX_CHARGES) {
                chargesToAdd = MAX_CHARGES - currentCharges;
            }

            if (player.getItems().playerHasItem(Items.BLOOD_RUNE, chargesToAdd)) {
                player.getItems().deleteItem(Items.BLOOD_RUNE, chargesToAdd);
                player.setTumekensShadowCharge(player.getTumekensShadowCharge() + chargesToAdd);
                player.sendMessage("You've added " + Misc.insertCommas(chargesToAdd) + " to your staff, you now have " + Misc.insertCommas(player.getTumekensShadowCharge()) + " charges.");
            }
        }
    }

    private static void uncharge(Player player) {
        if (player.getItems().playerHasItem(Items.TUMEKENS_SHADOW)) {
            if (player.getTumekensShadowCharge() <= 2) {
                player.sendMessage("Your staff doesn't have any charges!");
                return;
            }

            if (player.getInventory().hasRoomInInventory(new ImmutableItem(Items.BLOOD_RUNE, player.getTumekensShadowCharge()))) {
                player.getItems().addItem(Items.BLOOD_RUNE, player.getTumekensShadowCharge());
                player.setTumekensShadowCharge(0);
                player.sendMessage("You uncharge your staff.");
            } else {
                player.sendMessage("You don't have enough space in your inventory to uncharge your staff.");
            }
        }
    }

}
