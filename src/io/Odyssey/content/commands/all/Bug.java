package io.Odyssey.content.commands.all;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.content.commands.owner.Pos;
import io.Odyssey.model.StillGraphic;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.util.discord.Discord;

import java.util.Optional;

public class Bug extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		if (input == null) {
			c.sendMessage("Please redo your message.");
			return;
		}
		Discord.writeBugMessage(c.getDisplayName() + ": " + input);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Report a bug");
	}
}
