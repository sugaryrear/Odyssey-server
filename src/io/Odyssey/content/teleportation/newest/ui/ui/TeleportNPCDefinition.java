package io.Odyssey.content.teleportation.newest.ui.ui;

import io.Odyssey.model.entity.player.Player;

public enum TeleportNPCDefinition {
	
	ROCK_CRAB(5935, 1800, 75, 0),
	COW(2790, 1200, 50, 150),
	CHICKEN(2805, 750, 45, 150),
	YAK(5816, 1200, 50, 150),
	BANDIT(690, 1800, 90, 150),
	ELF_WARRIOR(3428, 1800, 90, 150),
	DAGANNOTH(3185, 1800, 90, 150),
	MITHRIL_DRAGON(2919, 3500, 90, 150),
	SMOKE_DEVIL(498, 1800, 90, 150),
	RUNE_DRAGON(8031, 2750, 90, 150),
	DEMONIC_GORILLA(7144, 1800, 90, 150),
	CAVE_KRAKEN(492, 1800, 90, 150),
	SLAYER_TOWER(4005, 1600, 90, 150),
	CATACOMBS(7286, 5000, 90, 150),
	NIEVES_CAVE(487, 1400, 90, 150),
	RELLEKA_DUNGEON(410, 1500, 50, 150),
	TALVERY_DUNGEON(2005, 2300, 90, 150),
	BRIMHAVEN_DUNGEON(2048, 3400, 90, 150),
	WYVERN_DUNGEON(465, 1750, 90, 150),
	ANCIENT_WYVERN(7795, 2500, 90, 150),
	
	GOD_WARS(2215, 4000, 90, 150),
	ZULRAH(2044, 4600, 90, 150),
	VORKATH(8060, 4800, 90, 150),
	SHAMAN(7573, 4000, 90, 150),
	CERBERUS(5862, 4000, 90, 150),
	KRAKEN(492, 1800, 90, 150),
	SIRE(5886, 5000, 90, 150),
	THERMO_SMOKE(499, 3000, 90, 150),
	CORP(319, 4100, 90, 150),
	VENENATIS(6504, 3000, 90, 150),
	SCORPIA(6615, 3000, 90, 150),
	CALLISTO(6609, 2600, 90, 150),
	NEX(11278, 2250, 0, 150),
	CHAOS_FANATIC(6619, 1800, 90, 150),
	CRAZY_ARCH(6618, 1800, 90, 150),
	VETION(6611, 3400, 90, 150),
	CHAOS_ELE(2054, 3000, 90, 150),
	KBD(239, 3200, 90, 150),
	KALPHITE_QUEEN(963, 3200, 90, 150),
	DAGANNOTH_KINGS(2266, 3000, 90, 150),
	DAGANNOTH_MOTHER(988, 3000, 90, 150),
	BARRELCHEST(600, 3500, 90, 150),
	MOLE(5779, 2500, 90, 150),
	HYDRA_DUNGEON(8609, 3300, 90, 150),
	RAIDS1(7542, 4600, 90, 150),
	RAIDS2(8359, 5500, 100, 150),

	TOMBSOFAMASCUT(11652, 1200, 100, 150),

	TOKKUL_PIT(11345, 1200, 100, 150),
	TRIALS(7562, 1750, 90, 150),
	VOID_CHAMPION(6014, 3300, 90, 150),
	NIGHTMARE(9470, 1800, 90, 150),
	SOLAK(3407, 4000, 90, 150),
	WILDY_STRYKEWYRM(3822, 3800, 90, 150),
	ICE_STRYKEWYRM(3830, 3800, 90, 150),
	CAVE_HORRORS(1047, 1600, 90, 150),
	THE_GAUNTLET(9048, 1800, 90, 150),
	STARTER_DUNGEON(3396, 1200, 0, 150),
	ANGEL_OF_DEATH(11299, 2250, 0, 150),
	THE_INFERNO(7706, 2250, 0, 150),
	JUNGLE_WYRM(11401, 2250, 0, 150),
	DESERT_WYRM(11402, 2250, 0, 150),
	;
	
	
	private TeleportNPCDefinition(int npcId, int modelZoom, int rotation1, int rotation2) {
		this.npcId = npcId;	
		this.modelZoom = modelZoom;
		this.rotation1 = rotation1;
		this.rotation2 = rotation2;
	}
	private int modelZoom, rotation1, rotation2;
	private int npcId;
	
	public int getModelZoom() {
		return modelZoom;
	}
	public int getRotation1() {
		return rotation1;
	}
	public int getRotation2() {
		return rotation2;
	}
	public int getNpcId() {
		return npcId;
	}
	
	public void sendNpcOnInterface(Player player) {
		System.out.println("sendNpcOnInterface 2 npcId:"+npcId);
		player.getPA().sendInterfaceModelSettings(63116, modelZoom, rotation1, rotation2);
		player.getPA().sendNPCOnInterface(63116, npcId);
		player.getPA().flush();
	}

	public static void reset(Player player) {
		System.out.println("reset");
		player.getPA().sendNPCOnInterfaceReset(63116);
	}

}
