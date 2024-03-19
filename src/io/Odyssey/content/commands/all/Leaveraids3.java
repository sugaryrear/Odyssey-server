package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.minigames.Raids3.Raids3;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;

public class Leaveraids3 extends Commands {

    @Override
    public void execute(Player player, String commandName, String input) {
        Raids3 raids3Instance = player.getRaids3Instance();
        if(raids3Instance != null) {
            player.sendMessage("@blu@You are now leaving the raid...");
            if (Boundary.isIn(player, Boundary.FULL_RAIDS3)) {
                raids3Instance.leaveGame(player);
            } else {
                player.sendMessage("Please re-log and report this issue to staff...");
            }
        } else {
            player.sendMessage("@red@You need to be in a raid to do this...");
        }
    }
    @Override
    public Optional<String> getDescription() {
        return Optional.of("Forces you to leave raids.");
    }

}
