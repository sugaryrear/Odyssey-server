package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

/**
 * Empty the inventory of the player.
 * 
 * @author Emiel
 */
public class Empty extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		if (Boundary.isIn(c, Boundary.AvatarOfCreation)) {
			c.sendMessage("You cannot use that command here.");
			return;
		}
		c.getDH().sendDialogues(450,-1);
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Empty your inventory");
	}
}
