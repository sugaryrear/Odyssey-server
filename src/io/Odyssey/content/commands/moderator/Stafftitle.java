package io.Odyssey.content.commands.moderator;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Changes the title of the player to their default staff title.
 * 
 * @author Emiel
 */
public class Stafftitle extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		c.sendMessage("You will now get your staff title instead. Relog for changes to take effect.");
		c.keepTitle = false;
		c.killTitle = false;
	}
}
