package io.Odyssey.content.commands.admin;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * LOOK MOM! I'M A SHIELD!
 * 
 * @author Emiel
 */
public class Shield extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		if (c.isNpc && c.npcId2 == 336) {
			c.isNpc = false;
		} else {
			c.npcId2 = 336;
			c.isNpc = true;
		}
		c.setUpdateRequired(true);
		c.appearanceUpdateRequired = true;
	}
}
