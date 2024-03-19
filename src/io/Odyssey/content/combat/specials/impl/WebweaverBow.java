package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.range.Arrow;
import io.Odyssey.content.combat.range.RangeData;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.Graphic;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.Player;

public class WebweaverBow extends Special {

    public WebweaverBow() {
        super(2.5, 20.00, 7.25, new int[] { 27_655 });
    }
    @Override
    public void activate(Player player, Entity target, Damage damage) {
        player.usingMagic = true;
        player.startAnimation(9964);
        player.startGraphic(new Graphic(2354));
        target.startGraphic(new Graphic(2355,10));
        player.usingMagic = false;
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
