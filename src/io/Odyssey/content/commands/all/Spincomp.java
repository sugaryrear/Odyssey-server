package io.Odyssey.content.commands.all;


import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Spincomp extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        String[] args = input.split(" ");
      //  int npcid = Integer.parseInt(args[0]);
        if(c.boxusing == 6199)
        c.getNormalMysteryBox().reward();
        else
            c.getResourceBoxLarge().reward();
        //  c.sendMessage("%s", Server.getWorld().getExamineRepository().object(objid));
      //  c.boxusing = 0;

    }
}
