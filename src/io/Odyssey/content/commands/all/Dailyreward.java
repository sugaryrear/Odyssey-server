package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

public class Dailyreward extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
//        if (!Boundary.EDGEVILLE_PERIMETER.in(player)) {
//            player.sendMessage("You must be in Edgeville to use this command.");
//        } else {
            player.getDailyRewards().openInterface();
     //   }
    }

    public Optional<String> getDescription() {
        return Optional.of("Opens the daily reward interface .");
    }
}
