package io.Odyssey.content.commands.all;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

import java.util.Optional;


public class Droptable extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        Server.getDropManager().openDefault(c);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Opens the drop table.");
    }

}
