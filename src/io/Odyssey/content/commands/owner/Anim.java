package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Anim extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		int id = Integer.parseInt(input);
		player.startAnimation(id);
		player.sendMessage("Playing animation: " + id);
	}

}
