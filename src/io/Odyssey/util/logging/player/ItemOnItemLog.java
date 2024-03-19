package io.Odyssey.util.logging.player;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.logging.PlayerLog;

import java.util.Set;

public class ItemOnItemLog extends PlayerLog {

    private final GameItem gameItem1;
    private final GameItem gameItem2;
    private final Position position;

    public ItemOnItemLog(Player player, GameItem gameItem1, GameItem gameItem2, Position position) {
        super(player);
        this.gameItem1 = gameItem1;
        this.gameItem2 = gameItem2;
        this.position = position;
    }

    @Override
    public Set<String> getLogFileNames() {
        return Set.of("item_on_item");
    }

    @Override
    public String getLoggedMessage() {
        return "Used " + gameItem1 + " on " + gameItem2 + " at " + position;
    }
}
