package io.Odyssey.content.commands.admin;

import io.Odyssey.Configuration;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Toggles whether the Duel Arena is enabled or not.
 * 
 * @author Emiel
 */
public class Duelarena extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		Configuration.NEW_DUEL_ARENA_ACTIVE = !Configuration.NEW_DUEL_ARENA_ACTIVE;
		c.sendMessage("The duel arena is currently " + (Configuration.NEW_DUEL_ARENA_ACTIVE ? "Enabled" : "Disabled") + ".");
	}
}
