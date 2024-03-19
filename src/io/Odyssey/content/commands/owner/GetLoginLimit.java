package io.Odyssey.content.commands.owner;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.net.login.LoginRequestLimit;

public class GetLoginLimit extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
       player.sendMessage("Login rate limit is set to {}", "" + LoginRequestLimit.MAX_LOGINS_PER_TICK);
    }
}
