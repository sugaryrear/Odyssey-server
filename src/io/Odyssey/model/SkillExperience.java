package io.Odyssey.model;

import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class SkillExperience {

    private final Skill skill;
    private final int experience;

    public SkillExperience(Player player, Skill skill) {
        this(skill, player.getExperience(skill));
    }

    public SkillExperience(Skill skill, int experience) {
        this.skill = skill;
        this.experience = experience;
    }

    @Override
    public String toString() {
        return "SkillExperience{" +
                "skill=" + skill.toString() +
                ", experience=" + Misc.insertCommas(experience) +
                '}';
    }

    public int skillId() {
        return getSkill().getId();
    }

    public Skill getSkill() {
        return skill;
    }

    public int getExperience() {
        return experience;
    }
}
