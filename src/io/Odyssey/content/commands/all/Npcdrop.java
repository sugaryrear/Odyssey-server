package io.Odyssey.content.commands.all;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Npcdrop extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        String[] args = input.split(" ");
        int npcid = Integer.parseInt(args[0]);
        Server.getDropManager().openForPacket(c, npcid);
      //  c.sendMessage("%s", Server.getWorld().getExamineRepository().object(objid));

    }
}
