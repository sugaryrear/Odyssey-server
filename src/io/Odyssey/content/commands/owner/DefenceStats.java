package io.Odyssey.content.commands.owner;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class DefenceStats extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        player.setPrintDefenceStats(!player.isPrintDefenceStats());
        player.sendMessage("Combat defence messages are now " + Misc.booleanToString(player.isPrintDefenceStats()) + ".");
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Prints out combat defence stats while in combat.");
    }
}
