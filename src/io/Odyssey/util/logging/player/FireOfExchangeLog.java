package io.Odyssey.util.logging.player;

import java.util.Set;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.logging.PlayerLog;

public class FireOfExchangeLog extends PlayerLog {

    private final GameItem gameItem;

    public FireOfExchangeLog(Player player, GameItem gameItem) {
        super(player);
        this.gameItem = gameItem;
    }

    @Override
    public Set<String> getLogFileNames() {
        return Set.of("fire_of_exchange");
    }

    @Override
    public String getLoggedMessage() {
        return "Burned " + gameItem;
    }
}
