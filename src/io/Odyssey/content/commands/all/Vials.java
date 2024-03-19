package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.battlepass.BattlePass;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Show the current position.
 * 
 * @author Noah
 *
 */
public class Vials extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
	//	BattlePass.execute(player);
		
	}
	@Override
	public Optional<String> getDescription() {
		return Optional.of("Turns on and off break vials.");
	}
}
