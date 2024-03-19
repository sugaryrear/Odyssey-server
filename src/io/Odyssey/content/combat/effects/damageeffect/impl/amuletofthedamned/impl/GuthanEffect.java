package io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.AmuletOfTheDamnedEffect;
import io.Odyssey.content.combat.magic.items.Degrade;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.EquipmentSet;

/**
 * @author Arthur Behesnilian 1:46 PM
 */
public class GuthanEffect implements AmuletOfTheDamnedEffect {

    /**
     * The singleton instance of the Amulet of the damned effect for Guthan
     */
    public static final AmuletOfTheDamnedEffect INSTANCE = new GuthanEffect();

    @Override
    public boolean hasExtraRequirement(Player player) {
        return EquipmentSet.GUTHAN.isWearingBarrows(player);
    }

    @Override
    public void useEffect(Player player, Entity other, Damage damage) {
        int maximumHealthCap = player.getHealth().getMaximumHealth() + 10;
        player.getHealth().increase(damage.getAmount(), maximumHealthCap);
        Degrade.degrade(player, Degrade.DegradableItem.AMULETS_OF_THE_DAMNED);
    }
}
