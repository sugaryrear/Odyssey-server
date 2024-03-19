package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.collisionmap.Region;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Color;

/**
 * Open the forums in the default web browser.
 * 
 * @author Emiel
 */
public class Ls extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		if (Region.regionnames.containsKey(12600))
			if (!c.regionsunlocked.containsKey(12600)) {//issue for existing players :/ oh well xd
				c.sendMessage("You must discover "+ Color.DARK_GREEN.wrap(Region.regionnames.get(12600)+" to use this teleport."));
				return;
			}
		c.sendMessage("@red@[ls]@blu@ Bank your items and enter the portal to join the tournament! Good Luck!");
    	c.getPA().spellTeleport(3141, 3634, 0, false);
}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Quick teleport to Lone Survivor teleport.");
	}

}
