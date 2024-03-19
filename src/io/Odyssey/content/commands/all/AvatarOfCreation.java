package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

/**
 * Teleport the player to the mage bank.
 *
 * @author Emiel
 */
public class AvatarOfCreation extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        c.getPA().sendFrame126("https://www.Odyssey.io/index.php?/topic/107-AvatarOfCreation-skilling-boss-guide/", 12000);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Opens up the AvatarOfCreation boss guide.");
    }

}
