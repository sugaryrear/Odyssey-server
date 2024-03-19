package io.Odyssey.content.commands.owner;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.save.backup.PlayerSaveBackup;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class Saveall extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		Server.getIoExecutorService().submit(() -> {
			try {
				c.addQueuedAction(plr -> plr.sendMessage("Saving.."));
				File file = PlayerSaveBackup.backup(LocalDateTime.now());
				c.addQueuedAction(plr -> plr.sendMessage("Saved all games and created backup at: " + file.getName() + "."));
			} catch (IOException e) {
				e.printStackTrace();
				c.addQueuedAction(plr -> plr.sendMessage("Error when saving all accounts."));
			}
		});
	}

}
