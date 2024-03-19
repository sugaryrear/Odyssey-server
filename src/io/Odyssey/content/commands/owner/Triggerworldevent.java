package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.content.worldevent.WorldEventContainer;
import io.Odyssey.model.entity.player.Player;

public class Triggerworldevent extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        WorldEventContainer.getInstance().setTriggerImmediateEvent(true);
        player.sendMessage("Triggering next world event, please allow up to 30 seconds.");
    }
}
