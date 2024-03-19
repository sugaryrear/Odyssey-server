package io.Odyssey.content.commands.owner;

import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Npclog extends Command {
    @Override
    public void execute(Player player, String commandName, String input) {
        player.sendMessage("Writing npc log..");
        Server.getIoExecutorService().submit(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("./logs/npc-log.txt"))) {
                for (int i = 0; i < NPCHandler.npcs.length; i++) {
                    NPC npc = NPCHandler.npcs[i];
                    if (npc != null) {
                        writer.write(Misc.insertCommas(i) + ": " + npc.toString());
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
