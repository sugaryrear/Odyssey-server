package io.Odyssey.content.commands.owner;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.punishments.PunishmentType;

public class ManualUnNetMute extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        Server.getPunishments().removeWithMessage(player, PunishmentType.NET_MUTE, input);
    }
}
