package io.Odyssey.content.commands.helper;

import java.util.Optional;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.ConnectedFrom;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

/**
 * Forces a given player to log out.
 * 
 * @author Emiel
 */
public class Kick extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		Optional<Player> optionalPlayer = PlayerHandler.getOptionalPlayerByDisplayName(input);
		if (optionalPlayer.isPresent()) {
			Player c2 = optionalPlayer.get();
			if (Server.getMultiplayerSessionListener().inAnySession(c)) {
				c.sendMessage("The player is in a trade, or duel. You cannot do this at this time.");
				return;
			}
			c2.outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(c2);
			c2.forceLogout();
			ConnectedFrom.addConnectedFrom(c2, c2.connectedFrom);
			c.sendMessage("Kicked " + c2.getDisplayName());
		} else {
			c.sendMessage(input + " is not online. You can only kick online players.");
		}
	}
}
