package io.Odyssey.content.combat.magic.items;

import io.Odyssey.model.entity.player.Player;

import java.util.concurrent.TimeUnit;


public class CluescrollRateIncreaseScroll {

	private static final long TIME = TimeUnit.MINUTES.toMillis(60) / 600;

	public static void openScroll(Player player) {
		if (player.fasterCluesScroll) {
			player.sendMessage("You already have a bonus damage sigil active.");
			return;
		}

		player.fasterCluesScroll = true;
		player.fasterCluesTicks = TIME;
	}
	

}
	
