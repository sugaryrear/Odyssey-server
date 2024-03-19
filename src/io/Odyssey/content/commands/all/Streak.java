package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Changes the password of the player.
 * 
 * @author Emiel
 *
 */
public class Streak extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		c.sendMessage("@blu@ You have @red@"+c.getSlayer().getConsecutiveTasks()+" @blu@consecutive tasks.");
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Tells you your slayer consecutive tasks.");
	}
}
