package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.Configuration;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Open the forums in the default web browser.
 * 
 * @author Emiel
 */
public class Forums extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		c.getPA().sendFrame126(Configuration.WEBSITE, 12000);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Opens a web page with our forums");
	}

}
