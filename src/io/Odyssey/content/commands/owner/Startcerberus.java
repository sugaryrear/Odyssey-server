package io.Odyssey.content.commands.owner;

import io.Odyssey.content.bosses.Cerberus;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Update the shops.
 * 
 * @author Emiel
 *
 */
public class Startcerberus extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		Cerberus.init(player);
	}
}
