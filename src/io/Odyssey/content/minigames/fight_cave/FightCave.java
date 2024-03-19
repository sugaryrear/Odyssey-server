package io.Odyssey.content.minigames.fight_cave;

import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.achievement_diary.impl.KaramjaDiaryEntry;
import io.Odyssey.content.bosses.bryophyta.BryophytaNPC;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.items.ItemAssistant;
import io.Odyssey.util.Misc;

/**
 *
 * @author Jason http://www.rune-server.org/members/jason
 * @date Oct 17, 2013
 */
public class FightCave {

	private final Player player;
	private int killsRemaining;

	private NPC jad;

	public FightCave(Player player) {
		this.player = player;
	}

	public void spawn() {
		final int[][] type = Wave.getWaveForType(player);
		if(player.waveId >= type.length && player.fightCavesWaveType > 0 && Boundary.isIn(player, Boundary.FIGHT_CAVE)) {
			stop();
			return;
		}

		if(type == Wave.LEVEL_1 || (player.waveId+1 == type.length))
			player.getDH().sendDialogues(944, 7690);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer event) {
				if(player == null) {
					event.stop();
					return;
				}
				if(!Boundary.isIn(player, Boundary.FIGHT_CAVE)) {
					player.waveId = 0;
					player.fightCavesWaveType = 0;
					event.stop();
					return;
				}
				if(player.waveId >= type.length && player.fightCavesWaveType > 0) {
					onStopped();
					event.stop();
					return;
				}
				if(player.waveId != 0 && player.waveId < type.length) {
					if(type == Wave.LEVEL_1)
						player.sendMessage("Tztok-Jad will spawn soon.", 255);
					else
						player.sendMessage("You are now on wave "+(player.waveId + 1)+" of "+type.length+".", 255);


				}
				setKillsRemaining(type[player.waveId].length);
				for(int i = 0; i < getKillsRemaining(); i++) {
					int npcType = type[player.waveId][i];
					int index = Misc.random(Wave.SPAWN_DATA.length - 1);
					int x = (type == Wave.LEVEL_1 ? 2406 : Wave.SPAWN_DATA[index][0]);
					int y = (type == Wave.LEVEL_1 ? 5100 : Wave.SPAWN_DATA[index][1]);
					//	NPC npc = null;
					if(type == Wave.LEVEL_1){
						jad = new JadNPC(Npcs.TZTOK_JAD, new Position(x, y, player.getIndex() * 4), player);
						jad.attackEntity(player);
						//return;
					} else{

						NPCSpawning.spawnNpcOld(player, npcType, x, y, player.getIndex() * 4,
								1, Wave.getHp(npcType), Wave.getMax(npcType), Wave.getAtk(npcType), Wave.getDef(npcType), true, false);

					}
				}

				event.stop();
			}

			@Override
			public void onStopped() {


			}
		}, 16);
	}


	public void leaveGame() {
		if (System.currentTimeMillis() - player.fightCaveLeaveTimer < 15000) {
			player.sendMessage("You cannot leave yet, wait a couple of seconds and try again.");
			return;
		}
		killAllSpawns();
		player.sendMessage("You have left the Fight Cave minigame.");
		player.getPA().movePlayer(2438, 5168, 0);
		player.fightCavesWaveType = 0;
		player.waveId = 0;
	}

	public void create(int type) {
		player.getPA().removeAllWindows();
		player.getPA().movePlayer(2413, 5117, player.getIndex() * 4);
		player.fightCavesWaveType = type;
		player.sendMessage("Welcome to the Fight Cave minigame. Your first wave will start soon.", 255);
		player.waveId = 0;
		player.fightCaveLeaveTimer = System.currentTimeMillis();
		spawn();
	}
	private void despawnMinions() {
		NPCHandler.despawn(Npcs.YT_HURKOT, player.heightLevel);
	}
	public void stop() {
		reward();
		despawnMinions();
		player.getPA().movePlayer(2438, 5168, 0);
		player.getDH().sendItemStatement("Congratulations for finishing the Fight Caves!", FIRE_CAPE);
		player.waveInfo[player.fightCavesWaveType - 1] += 1;
		player.fightCavesWaveType = 0;
		player.waveId = 0;
		player.nextChat = 0;
		player.setRunEnergy(10000, true);
		killAllSpawns();
	}

	public void handleDeath() {

		player.getPA().movePlayer(2438, 5168, 0);
		player.getDH().sendStatement("Unfortunately you died on wave " + player.waveId + ". Better luck next time.");
		player.nextChat = 0;
		player.fightCavesWaveType = 0;
		player.waveId = 0;
		killAllSpawns();
	}

	public void killAllSpawns() {
		despawnMinions();
		for (int i = 0; i < NPCHandler.npcs.length; i++) {
			NPC npc = NPCHandler.npcs[i];
			if (npc != null) {
				if (NPCHandler.isFightCaveNpc(npc)) {
					if (NPCHandler.isSpawnedBy(player, npc)) {
						npc.unregister();
					}
				}
			}
		}
	}

	public void gamble() {
		if (!player.getItems().playerHasItem(FIRE_CAPE)) {
			player.sendMessage("You do not have a firecape.");
			return;
		}
		player.getItems().deleteItem(FIRE_CAPE, 1);

		if (Misc.random(100) == 67) {
			if (player.getItems().getItemCount(13225, true) == 0 && player.petSummonId != 13225) {
				PlayerHandler.executeGlobalMessage("[@red@PET@bla@] @cr20@<col=255> " + player.getDisplayName() + "</col> received a pet from <col=255>TzTok-Jad</col>.");
				player.getItems().addItemUnderAnyCircumstance(13225, 1);
				player.getDH().sendDialogues(74, 2180);
			}
		} else {
			player.getDH().sendDialogues(73, 2180);
		}
	}

	private static final int[] REWARD_ITEMS = { 6571, 6528, 11128, 6523, 6524, 6525, 6526, 6527, 6568, 6523, 6524, 6525, 6526, 6527, 6568 };

	public static final int FIRE_CAPE = 6570;

	public static final int TOKKUL = 6529;

	public void reward() {
		Achievements.increase(player, AchievementType.FIGHT_CAVES_ROUNDS, 1);
		switch (player.fightCavesWaveType) {
			case 1:
				player.getItems().addItemUnderAnyCircumstance(FIRE_CAPE, 1);
				break;
			case 2:
				player.getItems().addItemUnderAnyCircumstance(FIRE_CAPE, 1);
				break;
			case 3:
				player.getDiaryManager().getKaramjaDiary().progress(KaramjaDiaryEntry.COMPLETE_FIGHT_CAVES);
				player.getEventCalendar().progress(EventChallenge.COMPLETE_A_63_WAVE_FIGHT_CAVES);
				//	int item = REWARD_ITEMS[Misc.random(REWARD_ITEMS.length - 1)];
				player.getItems().addItemUnderAnyCircumstance(FIRE_CAPE, 1);
				//	player.getItems().addItemUnderAnyCircumstance(item, 1);
				//PlayerHandler.executeGlobalMessage(player.getDisplayName() + " has completed 63 waves of jad and received " + ItemAssistant.getItemName(item) + ".");
				break;
		}
		player.getItems().addItemUnderAnyCircumstance(TOKKUL, (10000 * player.fightCavesWaveType) + Misc.random(5000));
	}

	public int getKillsRemaining() {
		return killsRemaining;
	}

	public void setKillsRemaining(int remaining) {
		this.killsRemaining = remaining;
	}

}
