package io.Odyssey.model.cycleevent.impl;

import java.util.concurrent.TimeUnit;

import io.Odyssey.Configuration;
import io.Odyssey.content.bosses.AvatarOfCreation.*;
import io.Odyssey.content.wogw.Wogw;
import io.Odyssey.model.cycleevent.Event;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.util.Misc;

public class BonusApplianceEvent extends Event<Object> {
	
	/**
	 * The amount of time in game cycles (600ms) that the event pulses at
	 */
	private static final int INTERVAL = Misc.toCycles(1, TimeUnit.SECONDS);

	/**
	 * Creates a new event to cycle through messages for the entirety of the runtime
	 */
	public BonusApplianceEvent() {
		super("", new Object(), INTERVAL);
	}

	private void bonusExpiredMessage(String action) {
		PlayerHandler.executeGlobalMessage(Wogw.WOGW_MESSAGE_HEADER + "The Well of Goodwill is no longer granting " + action + ".");
	}

	@Override
	public void execute() {
		if (Wogw.EXPERIENCE_TIMER > 0) {
			Wogw.EXPERIENCE_TIMER--;
			if (Wogw.EXPERIENCE_TIMER == 1) {
				bonusExpiredMessage("bonus experience");
			}
		}
		if (Wogw.PC_POINTS_TIMER > 0) {
			Wogw.PC_POINTS_TIMER--;
			if (Wogw.PC_POINTS_TIMER == 1) {
				bonusExpiredMessage("bonus PC points");
			}
		}
//		if (Configuration.DOUBLE_DROPS_TIMER > 0) {
//			Configuration.DOUBLE_DROPS_TIMER--;
//			if (Configuration.DOUBLE_DROPS_TIMER == 1) {
//				bonusExpiredMessage("double drops");
//			}
//		}
		if (Wogw._20_PERCENT_DROP_RATE_TIMER > 0) {
			Wogw._20_PERCENT_DROP_RATE_TIMER--;
			if (Wogw._20_PERCENT_DROP_RATE_TIMER == 1) {
				bonusExpiredMessage("+20% drop rate");
			}
		}

		/**
		 * AvatarOfCreation Seeds
		 */
//		if (AvatarOfCreation.ATTAS_TIMER > 0) {
//			AvatarOfCreation.ATTAS_TIMER--;
//			if (AvatarOfCreation.ATTAS_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Attas plant is no longer granting XP!");
//				new AttasBonus().deactivate();
//			}
//		}
//		if (AvatarOfCreation.KRONOS_TIMER > 0) {
//			AvatarOfCreation.KRONOS_TIMER--;
//			if (AvatarOfCreation.KRONOS_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Kronos plant is no longer granting double Raids 1 keys!");
//				new KronosBonus().deactivate();
//			}
//		}
//		if (AvatarOfCreation.IASOR_TIMER > 0) {
//			AvatarOfCreation.IASOR_TIMER--;
//			if (AvatarOfCreation.IASOR_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Iasor plant is no longer granting drop rate bonus!");
//				new IasorBonus().deactivate();
//			}
//		}
//
//		if (AvatarOfCreation.GOLPAR_TIMER > 0) {
//			AvatarOfCreation.GOLPAR_TIMER--;
//			if (AvatarOfCreation.GOLPAR_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Golpar plant is no longer granting more loot!");
//				new GolparBonus().deactivate();
//			}
//		}
//		if (AvatarOfCreation.BUCHU_TIMER > 0) {
//			AvatarOfCreation.BUCHU_TIMER--;
//			if (AvatarOfCreation.BUCHU_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Buchu plant is no longer granting 2x Boss points!");
//				new BuchuBonus().deactivate();
//			}
//		}
//		if (AvatarOfCreation.KELDA_TIMER > 0) {
//			AvatarOfCreation.KELDA_TIMER--;
//			if (AvatarOfCreation.KELDA_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Kelda plant is no longer granting 2x Larren's Keys!");
//				new KeldaBonus().deactivate();
//			}
//		}
//		if (AvatarOfCreation.NOXIFER_TIMER > 0) {
//			AvatarOfCreation.NOXIFER_TIMER--;
//			if (AvatarOfCreation.NOXIFER_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Noxifer plant is no longer granting 2x Slayer points!");
//				new NoxiferBonus().deactivate();
//			}
//		}
//		if (AvatarOfCreation.CELASTRUS_TIMER > 0) {
//			AvatarOfCreation.CELASTRUS_TIMER--;
//			if (AvatarOfCreation.CELASTRUS_TIMER == 1) {
//				PlayerHandler.executeGlobalMessage("@bla@[@gre@AvatarOfCreation@bla@] @red@The Celastrus plant is no longer granting x2 Brimstone keys!");
//				new CelastrusBonus().deactivate();
//			}
//		}


	}
}
