package io.Odyssey.content.combat.magic;

import io.Odyssey.model.Items;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.ImmutableItem;
import io.Odyssey.util.Misc;

public class AccursedScepter {
    private static final int MAX_CHARGES = 20_000;
    public static final int COMBAT_SPELL_INDEX = -1;

    public static boolean useItem(Player player, int item1, int item2) {
        if (item1 == Items.REVENANT_ETHER && item2 == Items.ACCURSED_SCEPTRE || item2 == Items.REVENANT_ETHER && item1 == Items.ACCURSED_SCEPTRE) {
            charge(player);
            return true;
        }

        return false;
    }

    public static boolean clickItem(Player player, int itemId, int option) {
        if (itemId == Items.ACCURSED_SCEPTRE) {
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
        player.sendMessage("Your staff has " + Misc.insertCommas(player.getAccursedCharge()) + " charges remaining.");
    }

    private static void charge(Player player) {
        if (player.getItems().playerHasItem(Items.ACCURSED_SCEPTRE)) {
            int ether = player.getItems().getItemAmount(Items.REVENANT_ETHER);
            int currentCharges = player.getAccursedCharge();

            if (ether == 0) {
                player.sendMessage("You don't have any revenant ether!");
                return;
            }

            if (currentCharges >= MAX_CHARGES) {
                player.sendMessage("You have already stored 20,000 charges, you can't store any more!");
                return;
            }

            int chargesToAdd = ether;
            if (currentCharges + ether > MAX_CHARGES) {
                chargesToAdd = MAX_CHARGES - currentCharges;
            }

            if (player.getItems().playerHasItem(Items.REVENANT_ETHER, chargesToAdd)) {
                player.getItems().deleteItem(Items.REVENANT_ETHER, chargesToAdd);
                player.setAccursedCharge(player.getAccursedCharge() + chargesToAdd);
                player.sendMessage("You've added " + Misc.insertCommas(chargesToAdd) + " to your staff, you now have " + Misc.insertCommas(player.getAccursedCharge()) + " charges.");
            }
        }
    }

    private static void uncharge(Player player) {
        if (player.getItems().playerHasItem(Items.ACCURSED_SCEPTRE)) {
            if (player.getAccursedCharge() <= 2) {
                player.sendMessage("Your staff doesn't have any charges!");
                return;
            }

            if (player.getInventory().hasRoomInInventory(new ImmutableItem(Items.REVENANT_ETHER, player.getAccursedCharge()))) {
                player.getItems().addItem(Items.REVENANT_ETHER, player.getAccursedCharge());
                player.setAccursedCharge(0);
                player.sendMessage("You uncharge your staff.");
            } else {
                player.sendMessage("You don't have enough space in your inventory to uncharge your staff.");
            }
        }
    }

}

