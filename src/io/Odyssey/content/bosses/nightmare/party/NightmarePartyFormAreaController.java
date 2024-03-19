package io.Odyssey.content.bosses.nightmare.party;

import io.Odyssey.content.bosses.nightmare.NightmareConstants;
import io.Odyssey.content.party.PartyFormAreaController;
import io.Odyssey.content.party.PlayerParty;
import io.Odyssey.model.entity.player.Boundary;

import java.util.Set;

public class NightmarePartyFormAreaController extends PartyFormAreaController {

    @Override
    public String getKey() {
        return NightmareParty.TYPE;
    }

    @Override
    public Set<Boundary> getBoundaries() {
        return Set.of(NightmareConstants.LOBBY_BOUNDARY);
    }

    @Override
    public PlayerParty createParty() {
        return new NightmareParty();
    }
}
