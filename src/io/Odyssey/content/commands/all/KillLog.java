package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.combat.stats.MonsterKillLog;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class KillLog extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        MonsterKillLog.openInterface(player);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Opens the kill log.");
    }

}
