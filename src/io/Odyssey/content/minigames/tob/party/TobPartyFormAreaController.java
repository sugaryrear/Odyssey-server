package io.Odyssey.content.minigames.tob.party;

import io.Odyssey.content.minigames.tob.TobConstants;
import io.Odyssey.content.party.PartyFormAreaController;
import io.Odyssey.content.party.PlayerParty;
import io.Odyssey.model.entity.player.Boundary;

import java.util.Set;

public class TobPartyFormAreaController extends PartyFormAreaController {

    @Override
    public String getKey() {
        return TobParty.TYPE;
    }

    @Override
    public Set<Boundary> getBoundaries() {
        return Set.of(TobConstants.TOB_LOBBY);
    }

    @Override
    public PlayerParty createParty() {
        return new TobParty();
    }
}
