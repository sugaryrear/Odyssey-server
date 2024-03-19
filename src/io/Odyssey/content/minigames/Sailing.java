package io.Odyssey.content.minigames;

import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.lock.CompleteLock;

public class Sailing {

	private static final int[][] TRAVEL_DATA = { {}, // 0 - Null
			{ 2834, 3335, 15 }, // 1 - From Port Sarim to Entrana
			{ 3048, 3234, 15 }, // 2 - From Entrana to Port Sarim
			{ 2853, 3237, 12 }, // 3 - From Port Sarim to Crandor
			{ 2834, 3335, 13 }, // 4 - From Crandor to Port Sarim
			{ 2956, 3146, 9 }, // 5 - From Port Sarim to Karajama
			{ 3029, 3217, 8 }, // 6 - From Karajama to Port Sarim
			{ 2772, 3234, 3 }, // 7 - From Ardougne to Brimhaven
			{ 2683, 3271, 3 }, // 8 - From Brimhaven to Ardougne
			{ 2998, 3043, 23 }, // 9 - From Port Khazard to Ship Yard
			{ 2676, 3170, 23 }, // 10 - From Ship Yard to Port Khazard
			{ 2998, 3043, 17 }, // 11 - From Cairn Island to Ship Yard
			{ 2659, 2676, 12 }, // 12 - From Port Sarim to Pest Control
			{ 3041, 3202, 12 }, // 13 - From Pest Control to Port Sarim
			{ 2763, 2956, 10 }, // 14 - To Cairn Isle from Feldip Hills
			{ 2551, 3759, 20}, // 15 - To Waterbirth from Relleka
			{ 2620, 3686, 20}, //16 - To Relleka from Waterbirth
			{ 2277, 4036, 20}, //17 - To Ungael from Relleka
			{ 2640, 3694, 20}, //18 - To Relleka from Ungael
	};

//	public static void startTravel(final Player player, final int i) {
//		player.getPA().showInterface(3281);
//		player.getPA().sendConfig(75, i);
//		//player.getPA().movePlayer(1, 1, 0);
//		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
//			@Override
//			public void execute(CycleEventContainer c) {
//				player.getPA().movePlayer(getX(i), getY(i), 0);
//				c.stop();
//			}
//		}, getTime(i) - 400);
//		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
//			@Override
//			public void execute(CycleEventContainer c) {
//				player.getPA().sendConfig(75, -1);
//				player.getPA().closeAllWindows();
//				c.stop();
//			}
//		}, getTime(i));
//	}

	public static boolean checkForCash(Player c) {
		if (!c.getItems().playerHasItem(995, 1000)) {
			c.talkingNpc = 2437;
			c.getDH().sendNpcChat("You need 1000 coins to to travel on this ship!");
			c.nextChat = 0;
			return false;
		}
		c.getItems().deleteItem(995, 1000);
		c.sendMessage("You're free to go and pay the 1000 coins.");
		return true;
	}
public static boolean checkForCoins(Player c) {
	if (!c.getItems().playerHasItem(995, 30)) {
		c.talkingNpc = 381;
		c.getDH().sendNpcChat("You need 30 coins to to travel on this ship!");
		c.nextChat = 0;
		return false;
	}
	c.getItems().deleteItem(995, 30);
	c.sendMessage("You're free to go and pay the 30 coins.");
	return true;
}

	public static boolean searchForAlcohol(Player c) {
//		for (int element : ItemConstants.ALCOHOL_RELATED_ITEMS) {
//			if (c.getItemAssistant().playerHasItem(element, 1)) {
//				c.getDialogueHandler().sendNpcChat1("You can't bring intoxicating items to Asgarnia!", c.npcType, NpcHandler.getNpcListName(c.npcType));
//				c.nextChat = 0;
//				return false;
//			}
//		}
		c.sendMessage(
				"You're clean of any possible alcohol.");
		return true;
	}

	public static boolean quickSearch(Player c) {
//		for (int element : ItemConstants.COMBAT_RELATED_ITEMS) {
//			if (c.getItemAssistant().playerHasItem(element, 1) || c.getItemAssistant().playerHasEquipped(element)) {
//				c.getDialogueHandler().sendNpcChat2("Grr! I see you brought some illegal items! Get", "out of my sight immediately!", 657, NpcHandler.getNpcListName(c.npcType));
//				c.nextChat = 0;
//				return false;
//			}
//		}
		c.sendMessage("You're clean of any possible weapons.");
		return true;
	}
	public static void startTravel(final Player c, final int i) {
		if (i == 1) {// entrana check
			if (!quickSearch(c)) {
				return;
			}
		}
		if (i == 2) {// entrana check
			if (!searchForAlcohol(c)) {
				return;
			}
		}
		if (i == 7 || i == 8) {
			if (!checkForCoins(c)) {
				return;
			}
		}
		if (i == 15) {
			if (!checkForCash(c)) {
				return;
			}
		}
		c.lock(new CompleteLock());

		c.getPA().showInterface(3281);
		c.getPA().sendConfig(75, i);
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				c.getPlayerAssistant().movePlayer(getX(i), getY(i), 0);
				container.stop();
			}
		}, getTime(i) - 1);

		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				c.addQueuedAction(Player::unlock);
				c.getPA().sendConfig(75, -1);
				c.getPA().closeAllWindows();
				c.getDH().sendStatement("You arrive safely.");
				c.nextChat = -1;
				container.stop();
			}
		}, getTime(i));

	}
	public static int getX(int i) {
		return TRAVEL_DATA[i][0];
	}

	public static int getY(int i) {
		return TRAVEL_DATA[i][1];
	}

	public static int getTime(int i) {
		return TRAVEL_DATA[i][2];
	}

}