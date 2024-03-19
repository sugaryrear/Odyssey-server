package io.Odyssey.content.dialogue.impl;

import java.util.Arrays;
import java.util.Optional;

import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.entity.player.Player;

public class ClaimDonatorScrollDialogue extends DialogueBuilder {

    public static boolean clickScroll(Player player, int itemId) {
        Optional<DonationScroll> scrollOptional = Arrays.stream(DonationScroll.values()).filter(scroll -> scroll.itemId == itemId).findFirst();
        if (scrollOptional.isPresent()) {
            player.start(new ClaimDonatorScrollDialogue(player, scrollOptional.get()));
            return true;
        } else {
            return false;
        }
    }

    private static final int NPC_ID = Npcs.PARTY_PETE;
    private final DonationScroll scroll;

    public ClaimDonatorScrollDialogue(Player player, DonationScroll scroll) {
        super(player);
        this.scroll = scroll;
        setNpcId(NPC_ID);
        npc("Are you sure you want to claim this scroll?", "You will claim $" + scroll.donationAmount + " in credits.");
        option(new DialogueOption("Yes, claim $" + scroll.donationAmount + " scroll.", p -> claim()),
                new DialogueOption("Nevermind", p -> p.getPA().closeAllWindows()));
    }

    private void claim() {
        if (!getPlayer().getItems().playerHasItem(scroll.itemId))
            return;
        getPlayer().getItems().deleteItem(scroll.itemId, 1);
        getPlayer().gfx100(263);
        getPlayer().donatorPoints += scroll.donationAmount;//the actual points u can spend stuff
        getPlayer().amDonated += scroll.donationAmount;//just a tally of how much total you have donated!
      //  getPlayer().start(new DialogueBuilder(getPlayer()).setNpcId(NPC_ID).npc("Thank you for donating! " + scroll.donationAmount + "$ has been added to your total credit."));
        getPlayer().updateRank();
        getPlayer().getPA().closeAllWindows();
        getPlayer().getDonationRewards().increaseDonationAmount(scroll.donationAmount);
         if (getPlayer().amDonated >= 25 && getPlayer().amDonated <= 99) {
            getPlayer().sendMessage("@bla@Your next donator rank is @blu@Extreme Donator");
        } else if (getPlayer().amDonated >= 100 && getPlayer().amDonated <= 249) {
            getPlayer().sendMessage("@bla@Your next donator rank is @red@Legendary Donator");
        } else if (getPlayer().amDonated >= 250 && getPlayer().amDonated <= 499) {
            getPlayer().sendMessage("@bla@Your next donator rank is @whi@Diamond Club");
        } else if (getPlayer().amDonated >= 250 && getPlayer().amDonated <= 999) {
            getPlayer().sendMessage("@bla@Your next donator rank is @pur@Oynx Club");
        }
    }
    
    public enum DonationScroll {
        FIVE(13190, 5),
        TEN(2403, 10),
        TWENTY_FIVE(2396, 25),
        FIFTY(786, 50),
        ONE_HUNDRED(761, 100),
        TWO_FIFTY(607, 250),
        FIVE_HUNDRED(608, 500)
        ;

        private final int itemId;
        private final int donationAmount;

        DonationScroll(int itemId, int donationAmount) {
            this.itemId = itemId;
            this.donationAmount = donationAmount;
        }

        public int getItemId() {
            return itemId;
        }

        public int getDonationAmount() {
            return donationAmount;
        }
    }
}
