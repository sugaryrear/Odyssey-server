package io.Odyssey.util.logging.player;

import io.Odyssey.content.minigames.TombsOfAmascut.instance.TombsOfAmascutInstance;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.logging.PlayerLog;

import java.util.Set;
import java.util.stream.Collectors;

public class DiedAtTombsOfAmascutLog extends PlayerLog {

    private final TombsOfAmascutInstance instance;

    public DiedAtTombsOfAmascutLog(Player player, TombsOfAmascutInstance instance) {
        super(player);
        this.instance = instance;
    }

    @Override
    public Set<String> getLogFileNames() {
        return Set.of("died_at_tombs_of_amascut");
    }

    @Override
    public String getLoggedMessage() {
        String players = "";
        if (instance != null) {
            players = instance.getPlayers().stream().map(Player::getLoginNameLower).collect(Collectors.joining(", "));
        }
        return "Died at tombs of amascut with " + players;
    }
}
