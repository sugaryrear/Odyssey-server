package io.Odyssey.content.commands.admin;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.logging.player.EmptyInventoryLog;

/**
 * Empty the inventory of the player.
 * 
 * @author Emiel
 */
public class Empty extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		Server.getLogging().write(new EmptyInventoryLog(c, c.getItems().getInventoryItems()));
		c.getPA().removeAllItems();
		c.sendMessage("You empty your inventory.");
	}
}
