package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Refreshskill extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		c.getPA().refreshSkill(Integer.parseInt(input));
	}

}
