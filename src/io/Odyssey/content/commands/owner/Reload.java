package io.Odyssey.content.commands.owner;

import java.io.IOException;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.commands.Command;
import io.Odyssey.content.dailyrewards.DailyRewardContainer;
import io.Odyssey.content.fireofexchange.FireOfExchangeBurnPrice;
import io.Odyssey.content.referral.ReferralCode;

import io.Odyssey.model.definitions.ItemDef;
import io.Odyssey.model.definitions.ShopDef;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.world.ShopHandler;

/**
 * Reloading certain objects by {String input}
 * 
 * @author Matt
 */

public class Reload extends Command {

	@Override
	public void execute(Player player, String commandName, String input) {
		switch (input) {
		
		case "":
			player.sendMessage("@red@Usage: ::reload doors, drops, items, objects, shops or npcs");
			break;

			case "dailyrewards":
				try {
					DailyRewardContainer.load();
					player.sendMessage("Loaded daily rewards.");
				} catch (Exception e) {
					player.sendMessage("Error loading daily rewards, check the server output!");
					e.printStackTrace();
				}
				break;


		case "referralcodes":
			try {
				ReferralCode.load();
				player.sendMessage("Loaded referral codes.");
			} catch (Exception e) {
				player.sendMessage("Error loading referrals, check the server output!");
				e.printStackTrace();
			}
			break;

		case "doors":
//			try {
//
//				player.sendMessage("@blu@Reloaded DoubleDoors.");
//			} catch (IOException e) {
//				e.printStackTrace();
//				player.sendMessage("@blu@Unable to reload doors, check console.");
//			}
			break;

		case "drops":
			try {
				Server.getDropManager().read();
				player.sendMessage("@blu@Reloaded Drops.");
			} catch (Exception e) {
				player.sendMessage("@red@Error reloading drops!");
				e.printStackTrace();
			}

			break;

		case "items":
			try {
				ItemDef.load();
				player.sendMessage("@blu@Reloaded Items.");
			} catch (Exception e) {
				player.sendMessage("@blu@Unable to reload items, check console.");
				e.printStackTrace();
			}
			break;

		case "objects":
			try {
				Server.getGlobalObjects().reloadObjectFile(player);
				player.sendMessage("@blu@Reloaded Objects.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case "shops":
			try {
				Server.shopHandler = new ShopHandler();
				ShopDef.load();
				ShopHandler.load();
				FireOfExchangeBurnPrice.createBurnPriceShop();
				player.sendMessage("@blu@Reloaded Shops");
			} catch (Exception e) {
				player.sendMessage("Error occurred, check console.");
				e.printStackTrace();
			}
			break;

		case "npcs":
			Server.npcHandler = null;
			Server.npcHandler = new NPCHandler();
			player.sendMessage("@blu@Reloaded NPCs");
			break;
			
		case "punishments":
			try {
				Server.getPunishments().initialize();
				player.sendMessage("@blu@Reloaded Punishments.");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case "looting":
			Configuration.BAG_AND_POUCH_PERMITTED = !Configuration.BAG_AND_POUCH_PERMITTED;
			player.sendMessage(""+(Configuration.BAG_AND_POUCH_PERMITTED ? "Enabled" : "Disabled" +"") + " bag and pouch.");
			break;

		}
	}

}
