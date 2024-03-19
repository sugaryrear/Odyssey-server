package io.Odyssey.util.logging.player;

import java.util.Set;

import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.logging.PlayerLog;

public class ClickObjectLog extends PlayerLog {

    private final WorldObject globalObject;
    private final int option;

    public ClickObjectLog(Player player, WorldObject globalObject, int option) {
        super(player);
        this.globalObject = globalObject;
        this.option = option;
    }

    @Override
    public Set<String> getLogFileNames() {
        return Set.of("objects_clicked");
    }

    @Override
    public String getLoggedMessage() {
        return "Clicked object " + globalObject + " option " + option;
    }
}
