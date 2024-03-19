package io.Odyssey.content.item.lootable.unref;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.Misc;

/**
 * Revamped a simple means of receiving a random item based on chance.
 *
 * @author Jason MacKeigan
 * @date Oct 29, 2014, 1:43:44 PM
 */
public class CoinBagSmall extends CycleEvent {

	/**
	 * The item id of the PvM Casket required to trigger the event
	 */
	public static final int MYSTERY_BOX = 10832; //Casket

	/**
	 * The player object that will be triggering this event
	 */
	private final Player player;

	/**
	 * Constructs a new PvM Casket to handle item receiving for this player and this player alone
	 *
	 * @param player the player
	 */
	public CoinBagSmall(Player player) {
		this.player = player;
	}

	/**
	 *
	 */
	public void openall() {
		if (player.isDisconnected() || Objects.isNull(player)) {
			return;
		}
		int coins = 3000 + Misc.random(7500);
		int amount = player.getItems().getItemAmount(10832);

		if (Misc.random(1000) == 0) {
			int rewardAllGpAmount = (coins);
			player.getItems().addItem(995, rewardAllGpAmount);
			player.sendMessage("@red@You dig deeper and find a hidden pocket of " + Misc.formatCoins(rewardAllGpAmount) + " coins!");
			player.getItems().deleteItem(10832, amount);
		} else {
			int rewardAllGpAmount = (coins);
			player.getItems().deleteItem(10832, amount);
			player.getItems().addItem(995, rewardAllGpAmount);
			player.sendMessage("You receive " + Misc.formatCoins(rewardAllGpAmount) + " coins!");


		}
	}
	public void open() {
		if (System.currentTimeMillis() - player.lastMysteryBox < 1200) {
			return;
		}
		if (player.getItems().freeSlots() < 1) {
			player.sendMessage("You need at least one free slots to open a Coin Bag.");
			return;
		}
		if (!player.getItems().playerHasItem(MYSTERY_BOX)) {
			player.sendMessage("You need Coin Bag to do this.");
			return;
		}
		player.getItems().deleteItem(MYSTERY_BOX, 1);
		player.lastMysteryBox = System.currentTimeMillis();
		CycleEventHandler.getSingleton().stopEvents(this);
		CycleEventHandler.getSingleton().addEvent(this, this, 2);
	}

	/**
	 * Executes the event for receiving the mystery box
	 */
	@Override
	public void execute(CycleEventContainer container) {
		if (player.isDisconnected() || Objects.isNull(player)) {
			container.stop();
			return;
		}
		int coins = 3000 + Misc.random(7500);

		if (Misc.random(1000) == 0) {
			int rewardAmount = (coins);
			player.getItems().addItem(995, rewardAmount);
			player.sendMessage("@red@You dig deeper and find a hidden pocket of coins!");
			player.sendMessage("You receive " + Misc.formatCoins(rewardAmount) + " coins!");
		} else {
			int rewardAmount = coins;
			player.getItems().addItem(995, rewardAmount);
			player.sendMessage("You receive " + Misc.formatCoins(rewardAmount) + " coins!");
		}
		container.stop();
	}

}