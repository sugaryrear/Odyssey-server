package io.Odyssey.content.minigames.TombsOfAmascut.party;
import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascutConstants;
import io.Odyssey.content.party.PlayerParty;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.content.party.PartyFormAreaController;

import java.util.Set;

public class TombsOfAmascutPartyFormAreaController extends PartyFormAreaController {
    @Override
    public String getKey() {
        return TombsOfAmascutParty.TYPE;
    }

    @Override
    public Set<Boundary> getBoundaries() {
        return Set.of(Boundary.TOMBS_OF_AMASCUT_LOBBY_ENTRANCE);
    }

    @Override
    public PlayerParty createParty() {
        return new TombsOfAmascutParty();
    }
}
