package io.Odyssey.content.commands.owner;

import java.io.BufferedWriter;
import java.io.FileWriter;

import io.Odyssey.content.commands.Command;
import io.Odyssey.content.wogw.Wogwitems;
import io.Odyssey.model.entity.player.Player;

public class Dump extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		try {
			try (BufferedWriter coord=new BufferedWriter(new FileWriter("./WOGW.txt", true))) {
				int i=0;
				for (final Wogwitems.itemsOnWell t : Wogwitems.itemsOnWell.values()) {
					if (i==0)
						coord.write(t.getItemId()+"	0");
					else
						coord.write("	"+t.getItemId()+"	0");
					i++;
				}
			}
			}  catch (Exception e) {
				player.sendMessage("Invalid Format. ::dump");
			}
	}
}