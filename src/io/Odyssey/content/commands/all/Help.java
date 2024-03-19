package io.Odyssey.content.commands.all;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Opens the help interface.
 * 
 * @author Emiel
 */
public class Help extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		c.getPA().closeAllWindows();
		//c.getPA().showInterface(59525);
		List<String> lines = Lists.newArrayList();
		lines.add("<col=017291>~ Commands ~");
		lines.add("::home 1");
		lines.add("::home 2 ");
		lines.add("::home 3 ");
		lines.add("::home 4");
		c.getPA().openQuestInterfaceNew("Commands", lines);
	}

	@Override
	public Optional<String> getDescription() {
		return Optional.of("Creates a help ticket");
	}

}
