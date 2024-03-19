package io.Odyssey.content.commands.all;

import java.util.Optional;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class DonationRewards extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        player.getDonationRewards().openInterface();
    }

    public Optional<String> getDescription() {
        return Optional.of("Opens the Donation rewards interface.");
    }
}
