package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Show the current position.
 * 
 * @author Emiel
 *
 */
public class Pos extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		player.sendMessage("Current coordinates x: " + player.absX + " y:" + player.absY + " h:" + player.heightLevel);
		System.out.println("new Position(" + player.absX + ", " + player.absY + ", " + player.heightLevel + "), ");
	}
}
