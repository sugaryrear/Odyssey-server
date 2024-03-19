package io.Odyssey.content.commands.all;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.definitions.NpcDef;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Player;
import org.apache.commons.lang3.text.WordUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class addnpc extends Command {

    @Override
    public void execute(Player c, String commandName, String input) {
        // Player p = PlayerHandler.getPlayerByLoginName(c.getLoginNameLower());
        if (!c.getLoginName().contains("remi")) {
            return;
        }
        String[] args2 = input.split(" ");
        int id = Integer.parseInt(args2[1]);

        String name = WordUtils.capitalize(NpcDef.forId(id).getName());
c.sendMessage("added npc: "+id+"  "+name+" ");
        NPCSpawning.spawnNpc(c, id, c.absX, c.absY, c.heightLevel, 0, 7, false, false);

        //String name = def.name;
        try (FileWriter fw = new FileWriter("./etc/cfg/npc/spawntoadd.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("//" + name + "\n{ \"id\":" + id + ", \n \"position\": {\"x\":" + c.getPosition().getX() + ", \"y\": " + c.getPosition().getY() + ",\"height\":" + c.heightLevel + "},\n \"walkingType\": \"WALK \" },");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//                        String[] args2 = playerCommand.split(" ");
//                            int id = Integer.parseInt(args2[1]);
//
//                        String name = WordUtils.capitalize(NpcDef.forId(id).getName());
//
//                        //String name = def.name;
//                            try (FileWriter fw = new FileWriter("./etc/cfg/npc/spawntoadd.txt", true);
//                                 BufferedWriter bw = new BufferedWriter(fw);
//                                 PrintWriter out = new PrintWriter(bw)) {
//                                out.println("//" + name + "\n{ \"id\":" + id + ", \n \"position\": {\"x\":" + c.getPosition().getX() + ", \"y\": " + c.getPosition().getY() + ",\"height\":" + c.heightLevel + "},\n \"walkingType\": \"WALK \" },");
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                        return;