package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Teleport the player to home.
 * 
 * @author Emiel
 */
public class Home extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		if (Server.getMultiplayerSessionListener().inAnySession(c)) {
			return;
		}
		if (c.getPosition().inClanWars() || c.getPosition().inClanWarsSafe()) {
			c.sendMessage("@cr10@You can not teleport from here, speak to the doomsayer to leave.");
			return;
		}
		c.getPA().hometele();
	//	System.out.println("name: "+c.getRights().getPrimary().getFormattedName());
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Teleports you to home area");
	}

}
