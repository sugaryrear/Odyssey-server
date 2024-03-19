package io.Odyssey.util.logging.player;

import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.logging.PlayerLog;

import java.util.Set;
import java.util.stream.Collectors;

public class CompletedTombsOfAmascutLog extends PlayerLog {

    private final InstancedArea instance;

    public CompletedTombsOfAmascutLog(Player player, InstancedArea instance) {
        super(player);
        this.instance = instance;
    }

    @Override
    public Set<String> getLogFileNames() {
        return Set.of("completed tombs of amascut");
    }

    @Override
    public String getLoggedMessage() {
        String players = "";
        if (instance != null) {
            players = instance.getPlayers().stream().map(Player::getLoginNameLower).collect(Collectors.joining(", "));
        }
        return "Completed tombs of amascut with " + players;
    }
}
