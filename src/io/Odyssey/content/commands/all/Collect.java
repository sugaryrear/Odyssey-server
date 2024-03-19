package io.Odyssey.content.commands.all;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

import java.util.Optional;

public class Collect extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        player.getCollectionBox().collect(player);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Collect items in your collection box.");
    }
}
