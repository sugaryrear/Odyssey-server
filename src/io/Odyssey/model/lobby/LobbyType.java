package io.Odyssey.model.lobby;

import java.util.stream.Stream;
import io.Odyssey.model.lobby.impl.ChambersOfXericLobby;
import io.Odyssey.model.lobby.impl.Raids3Lobby;
import io.Odyssey.model.lobby.impl.TrialsOfXericLobby;
import io.Odyssey.model.lobby.impl.TombsOfAmascutLobby;


public enum LobbyType {
    CHAMBERS_OF_XERIC(ChambersOfXericLobby.class),TOMBS_OF_AMASCUT(TombsOfAmascutLobby.class), RAIDS_3(Raids3Lobby.class), TRIALS_OF_XERIC(TrialsOfXericLobby.class);

    LobbyType(Class<? extends Lobby> lobbyClass) {
        this.lobbyClass = lobbyClass;
    }

    private final Class<? extends Lobby> lobbyClass;

    public static Stream<LobbyType> stream() {
        // TODO Auto-generated method stub
        return Stream.of(values());
    }

    public Class<? extends Lobby> getLobbyClass() {
        return this.lobbyClass;
    }
}
