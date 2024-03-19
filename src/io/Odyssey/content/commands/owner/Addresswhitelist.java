package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.net.login.RS2LoginProtocol;

public class Addresswhitelist extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        RS2LoginProtocol.ADDRESS_WHITELIST.add(input);
        player.sendMessage("Add character to address whitelist: " + input);
    }
}
