package io.Odyssey.content.teleportation.newest;

import io.Odyssey.Configuration;
import io.Odyssey.content.achievement_diary.impl.*;
import io.Odyssey.content.combat.magic.CombatSpellData;
import io.Odyssey.content.combat.magic.MagicRequirements;
import io.Odyssey.model.entity.player.Player;

public class CityTeleports {
	
	public enum TeleportData {
		
		//Regular
		VARROCK(61,1164, 25, new int[] { 554, 1, 556, 3, 563, 1 }, Configuration.VARROCK_X, Configuration.VARROCK_Y, 63, 35),
		LUMBRIDGE(62,1167, 31, new int[] { 557, 1, 556, 3, 563, 1 }, Configuration.LUMBY_X, Configuration.LUMBY_Y, 64, 41),
		FALADOR(63,1170, 37, new int[] { 555, 1, 556, 3, 563, 1 }, Configuration.FALADOR_X, Configuration.FALADOR_Y, 65, 48),
		CAMELOT(64,1174, 45, new int[] { 556, 5, 563, 1, -1, -1 }, Configuration.CAMELOT_X, Configuration.CAMELOT_Y, 66, 55.5),
		ARDOUGNE(65,1540, 51, new int[] { 555, 2, 563, 2, -1, -1 }, Configuration.ARDOUGNE_X, Configuration.ARDOUGNE_Y, 67, 61),
		WATCHTOWER(66,1541, 58, new int[] { 557, 2, 563, 2, -1, -1 }, Configuration.WATCHTOWER_X, Configuration.WATCHTOWER_Y, 68, 68),
		TROLLHEIM(67,7455, 61, new int[] { 554, 2, 563, 2, -1, -1 }, Configuration.TROLLHEIM_X, Configuration.TROLLHEIM_Y, 69, 68),
		
		//Ancients
		PADDEWWA(68,13035, 54, new int[] { 563, 2, 554, 1, 556, 1 }, Configuration.PADDEWWA_X, Configuration.PADDEWWA_Y, 70, 64),
		SENNTISTEN(69,13045, 60, new int[] { 563, 2, 566, 1, -1, -1 }, Configuration.SENNTISTEN_X, Configuration.SENNTISTEN_Y, 71, 70),
		KHARYRLL(70,13053, 66, new int[] { 563, 2, 565, 1, -1, -1 }, Configuration.KHARYRLL_X, Configuration.KHARYRLL_Y, 72, 76),
		LASSAR(71,13061, 72, new int[] { 563, 2, 555, 4, -1, -1 }, Configuration.LASSAR_X, Configuration.LASSAR_Y, 73, 82),
		DAREEYAK(72,13069, 78, new int[] { 563, 2, 554, 3, 556, 2 }, Configuration.DAREEYAK_X, Configuration.DAREEYAK_Y, 74, 88),
		CARRALLANGAR(74,13079, 84, new int[] { 563, 2, 566, 2, -1, -1 }, Configuration.CARRALLANGAR_X, Configuration.CARRALLANGAR_Y, 75, 94),
		ANNAKARL(75,13087, 90, new int[] { 563, 2, 565, 2, -1, -1 }, Configuration.ANNAKARL_X, Configuration.ANNAKARL_Y, 76, 100),
		GHORROCK(76,13095, 96, new int[] { 563, 2, 555, 8, -1, -1 }, Configuration.GHORROCK_X, Configuration.GHORROCK_Y, 77, 1016),
		
		//Lunar
		MOONCLAN(77,30064, 69, new int[] { 9075, 2, 563, 1, 557, 2 }, Configuration.MOONCLAN_X, Configuration.MOONCLAN_Y, 78, 66),
		OURANIA(78,30083, 71, new int[] { 9075, 2, 563, 1, 557, 6 }, Configuration.OURANIA_X, Configuration.OURANIA_Y, 79, 69),
		WATERBIRTH(79,30106, 72, new int[] { 9075, 2, 563, 1, 555, 1 }, Configuration.WATERBIRTH_X, Configuration.WATERBIRTH_Y, 80, 71),
		BARBARIAN(80,30138, 75, new int[] { 9075, 2, 563, 2, 554, 3 }, Configuration.BARBARIAN_X, Configuration.BARBARIAN_Y, 81, 76),
		KHAZARD(81,30162, 78, new int[] { 9075, 2, 563, 2, 555, 4 }, Configuration.KHAZARD_X, Configuration.KHAZARD_Y, 82, 80),
		FISHING_GUILD(82,30226, 85, new int[] { 9075, 3, 563, 3, 555, 10 }, Configuration.FISHING_GUILD_X, Configuration.FISHING_GUILD_Y, 83, 89),
		CATHERBY(83,30250, 87, new int[] { 9075, 3, 563, 3, 555, 10 }, Configuration.CATHERBY_X, Configuration.CATHERBY_Y, 84, 92),
		ICE_PLATEU(84,30266, 89, new int[] { 9075, 3, 563, 3, 555, 8 }, Configuration.ICE_PLATEU_X, Configuration.ICE_PLATEU_Y, 85, 96),
		ARCEEUS_LIBRARY(102,18574, 6, new int[] { 9075, 3, 563, 3, 555, 8 }, Configuration.ARCEEUS_LIBRARY_X, Configuration.ARCEEUS_LIBRARY_Y, 68, 16);
		
		private final int buttonId;
		private final int level;
		private final int[] runes;
		private final int x;
		private final int y;
		private final int magicid;
		private final int spellId;
		private final double xp;
		
		TeleportData(int magicid, int buttonId, int level, int[] runes, int x, int y, int spellId, double xp) {
			this.magicid = magicid;
			this.buttonId = buttonId;
			this.level = level;
			this.runes = runes;
			this.x = x;
			this.y = y;
			this.spellId = spellId;
			this.xp = xp;
		}


		public int getButtonId() {
			return buttonId;
		}

		public int getLevel() {
			return level;
		}

		public int[] getRunes() {
			return runes;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
		
		public int getSpellId() {
			return spellId;
		}
		
		public double getXP() {
			return xp;
		}
		
	}
	
	public static void teleport(Player player, int buttonId) {
		//player.sendMessage("buttonId: "+buttonId);
		player.usingMagic = true;
		for (TeleportData data : TeleportData.values()) {
			if (data.getButtonId() == buttonId) {
				if (System.currentTimeMillis() - player.lastTeleport < 5000) {
					return;	
				}

				int rune4 = CombatSpellData.MAGIC_SPELLS[data.magicid][0];
				System.out.println("id: "+rune4);
				if (!MagicRequirements.checkMagicReqs(player, data.magicid, true)) {
					return;
				}
				player.getPA().spellTeleport(data.getX(), data.getY(), 0, false);
				player.getPA().addSkillXP((int) data.getXP(), 6, true);
				player.lastTeleport = System.currentTimeMillis();
				switch (data) {
				case LUMBRIDGE:
				player.getDiaryManager().getLumbridgeDraynorDiary().progress(LumbridgeDraynorDiaryEntry.LUMBRIDGE_TELEPORT);
					break;
				
				case CAMELOT:
					player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CAMELOT_TELEPORT);
					break;
				
				case ARDOUGNE:
					player.getDiaryManager().getArdougneDiary().progress(ArdougneDiaryEntry.TELEPORT_ARDOUGNE);
					break;
					
				case FALADOR:
					player.getDiaryManager().getFaladorDiary().progress(FaladorDiaryEntry.TELEPORT_TO_FALADOR);
					break;
					
				case TROLLHEIM:
				//	player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.TROLLHEIM_TELEPORT);
					break;
					
				case WATERBIRTH:
				//	player.getDiaryManager().getFremennikDiary().progress(FremennikDiaryEntry.WATERBIRTH_TELEPORT);
					break;
					
				case CATHERBY:
					player.getDiaryManager().getKandarinDiary().progress(KandarinDiaryEntry.CATHERY_TELEPORT);
					break;
					
				case GHORROCK:
				//	player.getDiaryManager().getWildernessDiary().progress(WildernessDiaryEntry.GHORROCK);
					break;
				
				default:
					break;
				}
			}
		}
	}
}
