package io.Odyssey.content.commands.moderator;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

public class Isnull extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		Player other = PlayerHandler.getPlayerByDisplayName(input);

		if (other == null) {
			player.sendMessage("The player '" + input + "' is null.");
			return;
		}

		player.sendMessage("Information about... " + input);
		player.sendMessage("Disconnected: " + other.isDisconnected());
		player.sendMessage("Session null: " + (other.getSession() == null));
		player.sendMessage("Session connected: " + other.getSession().isActive());
	}

}
