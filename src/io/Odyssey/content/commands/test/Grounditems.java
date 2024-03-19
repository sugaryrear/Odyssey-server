package io.Odyssey.content.commands.test;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

import java.util.Optional;

public class Grounditems extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
       int amount = Integer.parseInt(input);
       Server.itemHandler.createGroundItem(player, 4151, player.getX(), player.getY(), player.getHeight(), amount);
    }

    public Optional<String> getDescription() {
        return Optional.of("Add ground items under you [::grounditems 20]");
    }
}
