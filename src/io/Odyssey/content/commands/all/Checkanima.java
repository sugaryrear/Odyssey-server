package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Opens the vote page in the default web browser.
 * 
 * @author Emiel
 */
public class Checkanima extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		c.sendMessage("" + AvatarOfCreation.activeAnimaBonus());
	}


	@Override
	public Optional<String> getDescription() {
		return Optional.of("Tells you if the anima patch is active.");
	}

}

