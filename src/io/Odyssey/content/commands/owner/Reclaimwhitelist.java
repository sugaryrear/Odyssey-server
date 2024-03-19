package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.content.commands.all.Reclaim;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

public class Reclaimwhitelist extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        String[] args = input.split("-");
        if (args.length < 2) {
            player.sendMessage("Invalid usage, ::reclaimwhitelist-player name-seconds for reclaim");
            return;
        }


        String name = args[0].toLowerCase();
        int seconds = Integer.parseInt(args[1]);
        Player other = PlayerHandler.getPlayerByDisplayName(name);
        if (other == null) {
            player.sendMessage("No player with name: " + name);
            return;
        }

        Reclaim.addReclaimWhitelist(other, seconds);
        player.sendMessage("Allowing " + name + " to reclaim for the next " + seconds + " seconds.");
    }
}
