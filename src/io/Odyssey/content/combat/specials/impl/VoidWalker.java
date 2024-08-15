package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.cycleevent.impl.StaffOfTheDeadEvent;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.util.Misc;

public class VoidWalker extends Special {

    public VoidWalker() {
        super(2.5, 20.00, 7.25, new int[] { 27_690 });
    }

    @Override
    public void activate(Player player, Entity target, Damage damage) {

        player.startAnimation(1378);
        target.startGraphic(new Graphic(2363));

    }

    @Override
    public void hit(Player player, Entity target, Damage damage) {
        if (target instanceof Player) {
            player.usingMagic = true;
            if (damage.getAmount() > 0) {
                player.playerLevel[5] += (damage.getAmount() / 2);
                if (player.playerLevel[5] > 120) {
                    player.playerLevel[5] = 120;
                    player.getPA().refreshSkill(5);
                }
                player.getPA().refreshSkill(5);
            }
        } else {
            player.usingMagic = true;
            if (damage.getAmount() > 0) {
                player.playerLevel[5] += (damage.getAmount() / 2);
                if (player.playerLevel[5] > 120) {
                    player.playerLevel[5] = 120;
                    player.getPA().refreshSkill(5);
                }
                player.getPA().refreshSkill(5);
            }
        }
        player.usingMagic = false;
    }

}
