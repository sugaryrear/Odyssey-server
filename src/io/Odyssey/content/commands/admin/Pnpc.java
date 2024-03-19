package io.Odyssey.content.commands.admin;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Transform a given player into an npc.
 * 
 * @author Emiel
 *
 */
public class Pnpc extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		int npc = Integer.parseInt(input);
		
		if (npc > 13000) {
			c.sendMessage("Max npc id is: 13000");
			return;
		}
		
		c.npcId2 = npc;
		c.isNpc = true;
		c.setUpdateRequired(true);
		c.appearanceUpdateRequired = true;
	}
}
