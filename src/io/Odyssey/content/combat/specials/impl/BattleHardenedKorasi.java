package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Animation;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class BattleHardenedKorasi extends Special {

    public BattleHardenedKorasi() {
        super(2.5, 9.15, 4.00, new int[] { 30_062 });
    }

    public void activate(Player player, Entity target, Damage damage) {
        player.gfx100(1731);
        player.startAnimation(9489);
        target.startAnimation(new Animation(3170));
        target.startAnimation(new Animation(3170));
        target.startGraphic(new Graphic(1732));


    }

    @Override
    public void hit(Player player, Entity target, Damage damage) {
        if (damage.getAmount() < 1) {
            return;
        }
        if (damage.getAmount() < 21) {
            player.getHealth().increase(10);
            player.replenishSkill(Skill.HITPOINTS.getId(), 20);
        } else {
            player.getHealth().increase(damage.getAmount() / 2);
            player.replenishSkill(Skill.PRAYER.getId(), damage.getAmount() / 4);
        }
    }

}