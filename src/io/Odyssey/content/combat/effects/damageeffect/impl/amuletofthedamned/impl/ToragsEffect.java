package io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.impl;

import io.Odyssey.content.combat.Damage;
import io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.AmuletOfTheDamnedEffect;
import io.Odyssey.content.combat.magic.items.Degrade;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.EquipmentSet;

/**
 * @author Arthur Behesnilian 1:22 PM
 */
public class ToragsEffect implements AmuletOfTheDamnedEffect {

    /**
     * The singleton instance of the Amulet of the damned effect for Torags
     */
    public static final AmuletOfTheDamnedEffect INSTANCE = new ToragsEffect();

    @Override
    public boolean hasExtraRequirement(Player player) {
        return EquipmentSet.TORAG.isWearingBarrows(player);
    }

    @Override
    public void useEffect(Player player, Entity other, Damage damage) {
        Degrade.degrade(player, Degrade.DegradableItem.AMULETS_OF_THE_DAMNED);
    }

    public static double modifyDefenceLevel(Player player) {
        int maxHp = player.getHealth().getMaximumHealth();
        int currentHp = player.getHealth().getCurrentHealth();
        int missingHp = maxHp - currentHp;
        double bonus = 1 + ((double) missingHp / 100);

        int defenceLevel = player.playerLevel[Skill.DEFENCE.getId()];

        ToragsEffect.INSTANCE.useEffect(player, null, null);
        return (double) defenceLevel * bonus;
    }

}
