package io.Odyssey.content.minigames.Raids3;

import io.Odyssey.content.party.PartyInterface;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.content.party.PlayerParty;

public class Raids3Party extends PlayerParty {

    public static final String TYPE = "Raids3 Party";

    public Raids3Party() {
        super(TYPE, 100);
    }

    @Override
    public boolean canJoin(Player invitedBy, Player invited) {
        if (Raids3.isMissingRequirements(invited)) {
            invitedBy.sendMessage("That player doesn't have the requirements to play Chambers of Xeric.");
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
