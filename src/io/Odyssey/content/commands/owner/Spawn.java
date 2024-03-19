package io.Odyssey.content.commands.owner;

import io.Odyssey.content.ItemSpawner;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

import java.util.Optional;

public class Spawn extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        ItemSpawner.open(player);
    }

    public Optional<String> getDescription() {
        return Optional.of("Opens an interface to spawn items.");
    }
}
