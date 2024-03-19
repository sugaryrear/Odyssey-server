package io.Odyssey.content.commands.helper;

import io.Odyssey.content.commands.Command;
import io.Odyssey.content.help.HelpDatabase;
import io.Odyssey.model.entity.player.Player;

/**
 * Opens an interface containing all help tickets.
 * 
 * @author Emiel
 */
public class Helpdb extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		HelpDatabase.getDatabase().openDatabase(c);
	}
}
