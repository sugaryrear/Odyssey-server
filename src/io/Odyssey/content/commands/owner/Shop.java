package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Open a specific shop.
 * 
 * @author Emiel
 *
 */
public class Shop extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		try {
			c.getShops().openShop(Integer.parseInt(input));
			c.sendMessage("You successfully opened shop #" + input + ".");
		} catch (IndexOutOfBoundsException ioobe) {
			c.sendMessage("Error. Correct syntax: ::shop shopid");
		}
	}
}
