package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.content.item.lootable.LootableInterface;
import io.Odyssey.model.entity.player.Player;

/**
 * Open the mbox in the default web browser.
 * 
 * @author Noah
 */
public class Prizes extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		LootableInterface.openInterface(c);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Opens the loot table interface.");
	}

}
