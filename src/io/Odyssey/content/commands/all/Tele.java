package io.Odyssey.content.commands.all;


import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Right;

public class Tele extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
       // Player p = PlayerHandler.getPlayerByLoginName(c.getLoginNameLower());

        if (!c.getRights().hasStaffPosition()) {
            return;
        }
        String[] args = input.split(" ");
        if (args.length == 3) {
            c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
        } else if (args.length == 2) {
            c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), c.heightLevel);
        }
    }
}
