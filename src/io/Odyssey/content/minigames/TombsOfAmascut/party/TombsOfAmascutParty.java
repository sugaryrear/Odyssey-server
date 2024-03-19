package io.Odyssey.content.minigames.TombsOfAmascut.party;

import io.Odyssey.content.party.PartyInterface;
import io.Odyssey.content.party.PlayerParty;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class TombsOfAmascutParty extends PlayerParty {

    public static final String TYPE = "TombsOfAmascutParty";

    public TombsOfAmascutParty() {
        super(TYPE, 5);
    }

    @Override
    public boolean canJoin(Player invitedBy, Player invited) {
        if (invited.getPA().calculateTotalLevel() < invited.getMode().getTotalLevelForTombsOfAmascut()) {
            invited.sendStatement("You need " + Misc.insertCommas(invited.getMode().getTotalLevelForTombsOfAmascut()) + " total level to compete.");
            invitedBy.sendMessage(invited.getDisplayNameFormatted() + " doesn't have the requirements to compete.");
            return false;
        }

        return true;
    }

    @Override
    public void onJoin(Player player) {
        PartyInterface.refreshOnJoinOrLeave(player, this);
    }

    @Override
    public void onLeave(Player player) {
        PartyInterface.refreshOnJoinOrLeave(player, this);
    }
}
