package io.Odyssey.content.commands.all;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Objexamine extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        String[] args = input.split(" ");
        int objid = Integer.parseInt(args[0]);
       // System.out.println("obj: "+objid);
//        if (args.length == 3) {
//            c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
//        } else if (args.length == 2) {
//            c.getPA().movePlayer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), c.heightLevel);
//        }
        c.sendMessage("%s", Server.getWorld().getExamineRepository().object(objid));
    }
}
