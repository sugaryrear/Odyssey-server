package io.Odyssey.content.commands.all;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.save.PlayerSave;
import io.Odyssey.model.entity.player.save.backup.PlayerSaveBackup;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Sends the player a message containing a list of all online players with a dice bag in their inventory.
 * 
 * @author Emiel
 */
public class Hosts extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		PlayerSave.saveAll();
	}

}
