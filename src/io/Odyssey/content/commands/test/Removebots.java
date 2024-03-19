package io.Odyssey.content.commands.test;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Right;

import java.util.Optional;

public class Removebots extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        if (!player.getRights().contains(Right.OWNER)) {
            player.sendMessage("Only owners can use this command.");
            return;
        }

        player.sendMessage("Logging out bots.");
        PlayerHandler.nonNullStream().forEach(plr -> {
            if (plr.isBot()) {
                plr.forceLogout();
            }
        });
    }

    public Optional<String> getDescription() {
        return Optional.of("Create a new setup, needs a name too!");
    }
}
