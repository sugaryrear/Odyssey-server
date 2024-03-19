package io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.AmuletOfTheDamnedEffect;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.EquipmentSet;

/**
 * @author Arthur Behesnilian 1:10 PM
 */
public class VeracsEffect implements AmuletOfTheDamnedEffect {

    /**
     * The singleton instance of the Amulet of the damned effect for Veracs
     */
    public static final AmuletOfTheDamnedEffect INSTANCE = new VeracsEffect();

    @Override
    public boolean hasExtraRequirement(Player player) {
        return EquipmentSet.VERAC.isWearingBarrows(player);
    }

    @Override
    public void useEffect(Player player, Entity other, Damage damage) {
        // Effects happen inline elsewhere
    }
}
