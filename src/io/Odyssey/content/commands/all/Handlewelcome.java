package io.Odyssey.content.commands.all;


import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.content.tutorial.TutorialDialogue;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;

public class Handlewelcome extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        String[] args = input.split(" ");
        if (!c.completedTutorial) {
                        c.getPA().sendFrame248(3559, 3213);
            c.canChangeAppearance = true;
            TutorialDialogue.setInTutorial(c, true);
            c.getRights().remove(Right.IRONMAN);
            c.getRights().remove(Right.ULTIMATE_IRONMAN);
            c.getRights().remove(Right.HC_IRONMAN);
        }
       // int npcid = Integer.parseInt(args[0]);
     //   Server.getDropManager().openForPacket(c, npcid);
        //  c.sendMessage("%s", Server.getWorld().getExamineRepository().object(objid));



    }
}
