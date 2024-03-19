package io.Odyssey.content.commands.owner;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.net.login.RS2LoginProtocol;
import io.Odyssey.punishments.PunishmentType;

public class ManualUnNetBan extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        Server.getPunishments().removeWithMessage(player, PunishmentType.NET_BAN, input);
        Server.getPunishments().removeWithMessage(player, PunishmentType.MAC_BAN, input);
    }
}
