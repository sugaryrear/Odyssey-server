package io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.AmuletOfTheDamnedEffect;
import io.Odyssey.content.combat.magic.items.Degrade;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.EquipmentSet;
import io.Odyssey.util.Misc;

/**
 * @author Arthur Behesnilian 12:55 PM
 */
public class KarilEffect implements AmuletOfTheDamnedEffect {

    /**
     * The singleton instance of the Amulet of the damned effect for Karils
     */
    public static AmuletOfTheDamnedEffect INSTANCE = new KarilEffect();

    @Override
    public boolean hasExtraRequirement(Player player) {
        return EquipmentSet.KARIL.isWearingBarrows(player)
                && Misc.isLucky(25);
    }

    @Override
    public void useEffect(Player player, Entity other, Damage damage) {
        if (damage.getAmount() < 2) {
            return;
        }

        int halfDamage = (int) Math.ceil((double) damage.getAmount() / 2);
        Damage secondDamage = new Damage(other, halfDamage, player.hitDelay, player.playerEquipment, Hitmark.HIT, CombatType.RANGE);
        player.getDamageQueue().add(secondDamage);

        Degrade.degrade(player, Degrade.DegradableItem.AMULETS_OF_THE_DAMNED);

        /*if(Server.isDebug()) {
            player.debug("Karil's Effect with AOD: Appended Damage:" + secondDamage);
        }*/
    }
}
