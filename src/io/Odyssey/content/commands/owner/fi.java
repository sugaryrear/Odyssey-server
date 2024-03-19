package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.entity.player.Player;

/**
 * Send the item IDs of all matching items to the player.
 * 
 * @author Emiel
 *
 */
public class fi extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		if (input.length() < 3) {
			c.sendMessage("You must give at least 3 letters of input to narrow down the item.");
			return;
		}
		int results = 0;
		c.sendMessage("Searching: " + input);
		for (ItemDef def : ItemDef.getDefinitions().values()) {
			if (results == 200) {
				c.sendMessage("200 results have been found, the maximum number of allowed results. If you cannot");
				c.sendMessage("find the item, try and enter more characters to refine the results.");
				return;
			}
			if (def.getName().toLowerCase().contains(input.toLowerCase())) {
				c.sendMessage("@blu@" + def.getName().replace("_", " ") + " - " + def.getId());
				results++;
			}
		}
		c.sendMessage(results + " results found...");
	}
}
