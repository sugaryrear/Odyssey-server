package io.Odyssey.content.commands.owner;

import io.Odyssey.Configuration;
import io.Odyssey.content.commands.Command;
import io.Odyssey.model.entity.player.Player;

public class Set extends Command {

	@Override
	public void execute(Player c, String commandName, String input) {
		String[] args = input.split(" ");
		
		switch (args[0]) {
			case "refer":
				c.usedReferral = false;
				c.sendMessage("reset referral");
				break; // loot beams work on all accounts, not sure about the change I made though, idk if its dangerous
		
		case "":
			c.sendMessage("Usage: ::set minions or slayer");
			break;

		case "slayer":
			c.getSlayer().setPoints(Integer.parseInt(args[1]));
			c.getQuestTab().updateInformationTab();
			c.sendMessage("Slayer points set to: "+ Integer.parseInt(args[1]));
			break;
			
		case "dp":
			c.donatorPoints += Integer.parseInt(args[1]);
			c.sendMessage("Amount of donator points added: "+ Integer.parseInt(args[1]));
			c.getQuestTab().updateInformationTab();
			break;
			
		case "pkp":
			c.pkp += Integer.parseInt(args[1]);
			c.getQuestTab().updateInformationTab();
			c.sendMessage("Amount of pk points added: "+ Integer.parseInt(args[1]));
			break;
			
		case "players":
			Configuration.PLAYERMODIFIER = Integer.parseInt(args[1]);
			c.sendMessage("Player Count Modifier: +"+ Integer.parseInt(args[1]));
			break;
		
		}
	}
}
