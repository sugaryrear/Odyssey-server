package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreationSpawner;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Worldevent extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		if (AvatarOfCreationSpawner.isSpawned()) {
			player.getPA().spellTeleport(3438, 3104, 0, false);
			player.setAvatarOfCreationDamageCounter(0);
		} else {
			player.sendMessage("@red@[World Event] @bla@There is currently no world event going on.");
		}
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Teles you to world event.");
	}
}
