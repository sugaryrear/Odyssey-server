package io.Odyssey.model.entity.player.mode;

import io.Odyssey.content.skills.Skill;

public enum ModeType {
	STANDARD,
	IRON_MAN,
	ULTIMATE_IRON_MAN,
	OSRS,
	HC_IRON_MAN,
	ROGUE,
	ROGUE_HARDCORE_IRONMAN,
	ROGUE_IRONMAN,
	GROUP_IRONMAN,
	EVENT_MAN;
	;

	public double getExperienceRate(Skill skill) {
	//	System.out.println("me: "+this.getFormattedName());
		switch (this) {
			case STANDARD:
			case IRON_MAN:
			case ULTIMATE_IRON_MAN:
			case HC_IRON_MAN:
			case GROUP_IRONMAN:
				return skill.getExperienceRate();
			case ROGUE_HARDCORE_IRONMAN:
				return skill.getExperienceRate() + (skill.getExperienceRate() * 0.5d);

				//everything below is not used
			case OSRS:
				return 4d;

			case ROGUE:
			case ROGUE_IRONMAN:
				return 5d;
			default:
				throw new IllegalStateException("No xp rate defined for " + toString());
		}
	}
	public boolean isStandardRate(Skill skill) {
		switch (this) {
			case STANDARD:
			case IRON_MAN:
			case ULTIMATE_IRON_MAN:
			case HC_IRON_MAN:
			case GROUP_IRONMAN:
				return true;
			case ROGUE:
			case ROGUE_HARDCORE_IRONMAN:
			case ROGUE_IRONMAN:
				return true;
			default:
				return false;
		}
	}
	public String getFormattedName() {
		switch (this) {
			case STANDARD:
				return "Standard";
			case IRON_MAN:
				return "Ironman";
			case ULTIMATE_IRON_MAN:
				return "Ultimate Ironman";
			case OSRS:
				return "OSRS";
			case HC_IRON_MAN:
				return "Hardcore Ironman";
			case ROGUE:
				return "Rogue";
			case ROGUE_HARDCORE_IRONMAN:
				return "Legends";
			case ROGUE_IRONMAN:
				return "Rogue Ironman";
			case GROUP_IRONMAN:
				return "Group Ironman";
			default:
				throw new IllegalStateException("No format option for: " + this);
		}
	}
}
