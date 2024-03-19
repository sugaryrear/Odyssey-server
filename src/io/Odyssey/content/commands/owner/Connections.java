package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.net.ChannelHandler;

public class Connections extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
       player.sendMessage("There are currently {} active connections.", "" + ChannelHandler.getActiveConnections());
    }
}
