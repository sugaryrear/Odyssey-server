package io.Odyssey.content.commands.admin;

import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

public class Givecollection extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		String[] args = input.split("-");
		if (args.length < 4) {
			player.sendMessage("Syntax: ::givecollection-name-npcid-itemid-amount");
			return;
		}
		String playerName = args[0];
		int npcId = Integer.parseInt(args[1]);
		int itemId = Integer.parseInt(args[2]);
		int amount = Integer.parseInt(args[3]);
		Player p = PlayerHandler.getPlayerByDisplayName(playerName);
		if (p != null) {
			p.getCollectionLog().handleDrop(p, npcId, itemId, amount);
			p.sendMessage(player.getDisplayName() + " has given you a collection item.");
			player.sendMessage("You have give " + p.getDisplayName() + " a collection item.");
		}
	}

}
