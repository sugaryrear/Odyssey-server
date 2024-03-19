package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.content.tradingpost.Listing;
import io.Odyssey.model.entity.player.Player;

public class Post extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		Listing.openPost(c, false);
	}
}
