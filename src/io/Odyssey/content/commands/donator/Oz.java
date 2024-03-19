package io.Odyssey.content.commands.donator;

import java.util.Optional;

import io.Odyssey.Configuration;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;

public class Oz extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        if (c.inTrade || c.inDuel || c.getPosition().inWild()) {
            return;
        }
        if (c.getPosition().inClanWars() || c.getPosition().inClanWarsSafe()) {
            c.sendMessage("@cr10@This player is currently at the pk district.");
            return;
        }
        if (c.amDonated <= 999 && !c.getRights().isOrInherits(Right.ADMINISTRATOR)) {
            c.sendMessage("@red@You need legendary donator to do this command");
            return;
        }
        c.getPA().startTeleport(Configuration.ONYX_ZONE_TELEPORT, "modern", false);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.of("Teleports you to legendary zone.");
    }

}


