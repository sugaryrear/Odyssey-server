package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.net.login.LoginThrottler;

public class Clearloginattempts extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		LoginThrottler.clear();
		c.sendMessage("Cleared all login attempts.");
	}

}
