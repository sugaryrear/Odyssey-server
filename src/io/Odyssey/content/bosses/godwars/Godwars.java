package io.Odyssey.content.bosses.godwars;

import java.util.HashMap;
import java.util.Map;

import io.Odyssey.Server;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreationSpawner;
import io.Odyssey.content.bosses.nex.Nex;
import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.PathFinder;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerAssistant;
import io.Odyssey.util.Misc;

public class Godwars {

	private static int KC_REQUIRED;
	public static final int KEY_ID = 11942;

	public static final Boundary GODWARS_AREA = new Boundary(2819, 5255, 2942, 5375);

	private final Player player;
	//public Map<God, Integer> killcount;

	public Godwars(Player player) {
		this.player = player;
	//	initialize();
	}

	/**
	 * Sets all killcount values to 0.
	 */
	public void initialize() {
//		killcount = new HashMap<>();
//		for (God god : God.values()) {
//			killcount.put(god, 0);
//		}
	}

	/**
	 * Handles entering a boss room.
	 * 
	 * @param god The god to which the room belongs to.
	 */
	public void enterBossRoom(God god) {
//			if (player.amDonated < 25) {//non donators
//				KC_REQUIRED = 20;
//			} else if (player.amDonated >= 25 && player.amDonated <= 99) {//regular
//				KC_REQUIRED = 15;
//			} else if (player.amDonated >= 100 && player.amDonated <= 249) { //extreme only
//				KC_REQUIRED = 10;
//			} else if (player.amDonated >= 250 && player.amDonated <= 499) { //legendary
//				KC_REQUIRED = 8;
//			} else if (player.amDonated >= 250 && player.amDonated <= 999) { //diamond club
//				KC_REQUIRED = 6;
//			} else if (player.amDonated >= 1000) { //onyx
//				KC_REQUIRED = 5;
//			}
		if (player.amDonated < 25) {//non donators
			KC_REQUIRED = 10;//10 put 0 just for testing
		} else {
			KC_REQUIRED = 0;
		}
			int kcforwhichgod = god == God.ARMADYL ? player.gwdkc[0] : god == God.SARADOMIN ? player.gwdkc[1] : god == God.BANDOS ? player.gwdkc[2] : god == God.ZAMORAK ? player.gwdkc[3] : player.gwdkc[4];
		if (kcforwhichgod >= KC_REQUIRED) {
			if(god == God.ARMADYL){
				player.gwdkc[0]-=KC_REQUIRED;
			} else 			if(god == God.SARADOMIN){
				player.gwdkc[1]-=KC_REQUIRED;
			}else 			if(god == God.BANDOS){
				player.gwdkc[2]-=KC_REQUIRED;
			}else 			if(god == God.ZAMORAK){
				player.gwdkc[3]-=KC_REQUIRED;
			}else 			if(god == God.ZAROS){
				player.gwdkc[4]-=KC_REQUIRED;
			}
			//killcount.put(god, killcount.get(god) - KC_REQUIRED);

		} else if (player.getItems().playerHasItem(KEY_ID)) {
			player.getItems().deleteItem(KEY_ID, 1);
		} else {
			player.sendMessage("You need to kill " + (KC_REQUIRED - kcforwhichgod + " more " + Misc.capitalizeJustFirst(god.name()) + " creatures before you can enter."));
			//player.sendMessage("You can also buy a Ecumenical Key from the general store for quick entry!");
			return;
		}
		int previousHeight = player.heightLevel;

		switch (god) {
		case SARADOMIN:
			player.getPA().movePlayer(2907, 5265, getInstanceHeight());
			break;
		case ZAMORAK:
			player.getPA().movePlayer(2925, 5331, getInstanceHeight() + 2);
			break;
		case BANDOS:
			player.getPA().movePlayer(2864, 5354, getInstanceHeight() + 2);
			break;
		case ARMADYL:
			player.getPA().movePlayer(2839, 5296, getInstanceHeight() + 2);
			break;
			//this is only for public mode.
			case ZAROS:
//				if(player.getInstance() != null){
//					PathFinder.getPathFinder().findRouteNoclip(player,2908, player.absY, true, 0, 0);
//
//					player.getInstance().dispose();
//
//					return;
//				}

				//tricky because you might be in an instance!
				//if(player.heightLevel == 0){
//					if(player.absX >= 2908){//walking out of nex is free
//						PathFinder.getPathFinder().findRouteNoclip(player,2908, player.absY, true, 0, 0);
//						player.getPA().movePlayer(player.absX, player.absY, 0);
//					} else {
//						PathFinder.getPathFinder().findRouteNoclip(player,2908, player.absY, true, 0, 0);
//						player.getPA().movePlayer(player.absX, player.absY, 0);
//					}
					if(player.absX > 2909) {//leaving is all good
						PathFinder.getPathFinder().findRouteNoclip(player, 2908, player.absY, true, 0, 0);
					//	player.getPA().movePlayer(2908,5203, 0);
						return;
					}
				PathFinder.getPathFinder().findRouteNoclip(player, player.absX+3, player.absY, true, 0, 0);

				//	player.getPA().movePlayer(player.absX, player.absY, 0);
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						new Nex().enter(player);
						container.stop();

					}
					@Override
					public void onStopped() {

					}
				}, 3);

//						if (!NexNPC.isSpawned()) {
//							c.sendMessage("@red@You cannot do this right now.");
//							return;
//						}



					//	if (NexNPC.nexnpc.currentPhase == null)
						//	PathFinder.getPathFinder().findRouteNoclip(player, 2910, player.absY, true, 0, 0);
//else
//	player.sendMessage("Nex is fighting someone right now.");
//
//				break;

		}
//		if (player.heightLevel != previousHeight) {
//			Server.itemHandler.reloadItems(player);
//		}
	}

	/**
	 * Returns the height level of the instance which the player should be teleported to.
	 * 
	 * @return The height level of the instance.
	 */
	private int getInstanceHeight() {
		if (player.getMode().isGroupIronman()) {
			return 8;
		} else if (player.getMode().isIronmanType()) {
			return 4;
		} else {
			return 0;
		}
	}

	/**
	 * Increases the amount of minions slain of a certain god.
	 * 
	 * @param god The god of which the killcount should be increased.
	 */
	public void increaseKillcount(God god) {
		//System.out.println("god: "+god.name());
	//	killcount.put(god, killcount.get(god) + 1);
		if(god == God.ARMADYL)
			player.gwdkc[0] +=1;
		else if(god == God.SARADOMIN){
			player.gwdkc[1] +=1;
		}
		else if(god == God.BANDOS){
			player.gwdkc[2] +=1;
		}
		else if(god == God.ZAMORAK){
			player.gwdkc[3] +=1;
		}
		else if(god == God.ZAROS){
			player.gwdkc[4] +=1;
		}
	}

	public void increaseKillcountByTeleportationDevice(God god, int amount) {
	//	killcount.put(god, killcount.get(god) + amount);
	}

	/**
	 * Updates the killcount values on the interface.
	 */
	public void drawInterface() {
		PlayerAssistant assistant = player.getPA();
		assistant.sendFrame126(Integer.toString(player.gwdkc[0]), 70217);//armadyl
		assistant.sendFrame126(Integer.toString(player.gwdkc[1]), 70218);//saradomin
		assistant.sendFrame126(Integer.toString(player.gwdkc[2]), 70219);//bandos
		assistant.sendFrame126(Integer.toString(player.gwdkc[3]), 70220);//zamorak
		assistant.sendFrame126(Integer.toString(player.gwdkc[4]), 70221);//zaros
	}
}
