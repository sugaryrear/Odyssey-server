package io.Odyssey.content.bosses;

import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Coordinate;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;

/**
 * 
 * @author Grant_ | www.rune-server.ee/members/grant_ | 12/5/19
 *
 */
public class Kraken {

	public static final int KRAKEN_ID = 494;
	private static final int MAX_DAMAGE = 30;
	private static final int PRICE = 20_000;


	public static void init(Player player) {
//		if (!(player.getSlayer().getTask().isPresent()) || (player.getSlayer().getTask().isPresent() && !player.getSlayer().getTask().get().getPrimaryName().equals("cave kraken"))) {
//
//			player.sendMessage("You must have an active kraken task in order to do this.");
//			return;
//		}

		if (!player.getSlayer().onTask("kraken")) {
			player.sendMessage("You must have an active kraken task in order to do this.");
			return;
		}
		new DialogueBuilder(player).option(
				new DialogueOption("Pay 20,000 Kraken Instance", plr -> initforsure(plr)),
				new DialogueOption("Cancel", plr -> plr.getPA().closeAllWindows())
		).send();

		KrakenInstance instance = new KrakenInstance(player, Boundary.KRAKEN_BOSS_ROOM);
		player.getPA().movePlayer(new Coordinate(2280, 10022, instance.getHeight()));
		NPC npc = NPCSpawning.spawnNpc(player, KRAKEN_ID, 2279, 10035, instance.getHeight(), -1, MAX_DAMAGE, true, false);
		npc.getBehaviour().setRespawnWhenPlayerOwned(true);
		instance.add(npc);
		instance.add(player);
	}

	private static void initforsure(Player player) {
		player.getPA().closeAllWindows();
		if(!player.getItems().playerHasItem(995,PRICE)){
			player.sendMessage("You need 20,000 gp to create a private Kraken instance");
			return;
		}
		player.getItems().deleteItem(995,PRICE);
		KrakenInstance instance = new KrakenInstance(player, Boundary.KRAKEN_BOSS_ROOM);
		player.getPA().movePlayer(new Coordinate(2280, 10022, instance.getHeight()));
		NPC npc = NPCSpawning.spawnNpc(player, KRAKEN_ID, 2279, 10035, instance.getHeight(), -1, MAX_DAMAGE, true, false);
		npc.getBehaviour().setRespawnWhenPlayerOwned(true);
		instance.add(npc);
		instance.add(player);
	}

}
