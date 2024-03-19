package io.Odyssey.content.minigames.Raids3;
import io.Odyssey.content.party.PartyFormAreaController;
import io.Odyssey.content.party.PlayerParty;
import io.Odyssey.model.entity.player.Boundary;

import java.util.Set;

public class Raids3PartyFormAreaController extends PartyFormAreaController {
    @Override
    public String getKey() {
        return Raids3Party.TYPE;
    }

    @Override
    public Set<Boundary> getBoundaries() {
        return Set.of(Boundary.RAIDS3_LOBBY_ENTRANCE);
    }

    @Override
    public PlayerParty createParty() {
        return new Raids3Party();
    }
}
