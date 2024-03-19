package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.Items;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class GraniteHammerSpecialAttack extends Special {

    public GraniteHammerSpecialAttack() {
        super(6.0, 1.5, 1.0, new int[] {Items.GRANITE_HAMMER});
    }

    @Override
    public void activate(Player player, Entity target, Damage damage) {
        player.gfx0(1450);
        player.startAnimation(1378);
    }

    @Override
    public void hit(Player player, Entity target, Damage damage) {
        damage.setAmount(damage.getAmount() + 5);
    }

}
