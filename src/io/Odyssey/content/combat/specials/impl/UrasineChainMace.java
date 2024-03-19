package io.Odyssey.content.combat.specials.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.specials.Special;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;

public class UrasineChainMace extends Special
{
    public UrasineChainMace() {
        super(5.0, 20.0, 7.10, new int[] { 27660 });
    }

    @Override
    public void activate(Player player, Entity target, Damage damage) {
        player.gfx0(2342);
        player.startAnimation(9963);
    }

    @Override
    public void hit(Player player, Entity target, Damage damage) {

    }
}
