package io.Odyssey.content.combat.formula.rework;


import io.Odyssey.Server;
import io.Odyssey.content.combat.effects.damageeffect.impl.amuletofthedamned.impl.ToragsEffect;
import io.Odyssey.content.combat.melee.CombatPrayer;
import io.Odyssey.content.commands.owner.SetAccuracyBonus;
import io.Odyssey.content.combat.magic.items.PvpWeapons;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Bonus;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.Items;
import io.Odyssey.model.definitions.ItemStats;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.npc.stats.NpcCombatDefinition;
import io.Odyssey.model.entity.npc.stats.NpcCombatSkill;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.EquipmentSet;
import io.Odyssey.util.Misc;

/**
 * @author Arthur Behesnilian 12:09 PM
 */
public class RangeCombatFormula implements CombatFormula {

    /**
     * The singleton instance for our standard Range combat formula
     */
    public static RangeCombatFormula STANDARD = new RangeCombatFormula();

    private int maxhit;

    public RangeCombatFormula() {
        this(-1);
    }

    public RangeCombatFormula(int maxHit) {
        this.maxhit = maxHit;
    }

    @Override
    public double getAccuracy(Entity attacker, Entity defender, double specialAttackMultiplier,
                              double defenceMultiplier) {
        double attack = getAttackRoll(attacker, defender, specialAttackMultiplier);
        int defence = (int) ((double) getDefenceRoll(attacker, defender) * defenceMultiplier);
        if(attacker.isPlayer()){
            if (Server.getEventHandler().isRunning(attacker.asPlayer(), "saprange")) {
                double howmuchtodecrease = (attacker.asPlayer().sapDefence * 0.10);
                defence -=howmuchtodecrease*defence;
            }
        }
        double accuracy;
        if (attack > defence) {
            accuracy = 1.0 - (defence + 2.0) / (2.0 * (attack + 1.0));
        } else {
            accuracy = attack / (2.0 * (defence + 1));
        }
        if(attacker.isPlayer() && defender.isPlayer())
            attacker.asPlayer().isranging = true;
        return accuracy;
    }

    private int getAttackRoll(Entity attacker, Entity defender, double specialAttackMultiplier) {
        double effectiveRangeLevel = attacker.isPlayer() ?
                getEffectiveRangeAttackLevel(attacker.asPlayer()) : getEffectiveRangeAttackLevel(attacker.asNPC());

        if (attacker.isPlayer() && defender.isNPC()) {
            effectiveRangeLevel += SetAccuracyBonus.RANGE_ATTACK - 8;
        }
        if(defender.isPlayer()){
            if (Server.getEventHandler().isRunning(defender.asPlayer(), "saprange")) {
                double howmuchtodecrease = (defender.asPlayer().sapAttack * 0.10);
                effectiveRangeLevel -= (effectiveRangeLevel) *  howmuchtodecrease;
            }
        }
        if(defender.isPlayer()){
            if (Server.getEventHandler().isRunning(defender.asPlayer(), "leechranged")) {
                double howmuchtodecrease = (defender.asPlayer().leechRanged_enemy * 0.10);
                effectiveRangeLevel -= (effectiveRangeLevel) * howmuchtodecrease;
            }
        }
        double equipmentRangeBonus = getEquipmentAttackRangeBonus(attacker);

        double maxRoll = effectiveRangeLevel * (equipmentRangeBonus + 64.0);
        if (attacker.isPlayer()) {
            maxRoll = applyAttackSpecials(attacker.asPlayer(), defender, maxRoll, specialAttackMultiplier);
            maxRoll = Math.floor(maxRoll);

            if (defender.isNPC()) {
                maxRoll *= getNpcAttackMultiplier(attacker.asPlayer(), defender.asNPC());
                maxRoll = Math.floor(maxRoll);
            }
        }

        return (int) (maxRoll);
    }

    private double getNpcAttackMultiplier(Player attacker, NPC defender) {
        double multiplier = 1.0;

        // Slayer Helmet boost
        if (attacker.getSlayer().hasSlayerHelmBoost(defender, CombatType.RANGE)) {
            multiplier += 0.1666;
        } else {
            // Salve Amulet boosts
            multiplier += getSalveAmuletMultiplier(attacker, defender);
        }

        // DHCB Multiplier
        if (attacker.getItems().isWearingItem(Items.DRAGON_HUNTER_CROSSBOW, Player.playerWeapon))
            if (defender.isDragon())
                multiplier += 0.3;

        if (attacker.getPosition().inWild() && defender.getPosition().inWild()) {
            if (PvpWeapons.activateEffect(attacker, attacker.getItems().getWeapon())) {
                multiplier += 0.5;
            }
        }

        return multiplier;
    }

    private double getEffectiveRangeAttackLevel(Player player) {
        double effectiveLevel = Math.floor(player.playerLevel[Skill.RANGED.getId()] *
                getPrayerAttackMultiplier(player));

        switch (player.getCombatConfigs().getWeaponMode().getAttackStyle()) {
            case ACCURATE:
                effectiveLevel += 3.0;
                break;
        }

        effectiveLevel += 8;

        if (player.fullVoidRange()) {
            effectiveLevel *= 1.1;
            effectiveLevel = Math.floor(effectiveLevel);
        }
//        if (Server.getEventHandler().isRunning(player, "leechranged")) {
//            double howmuchtoincrease = (player.leechRanged * 0.10);
//            effectiveLevel+= effectiveLevel*howmuchtoincrease;
//        }
        return Math.floor(effectiveLevel);
    }

    private double getPrayerAttackMultiplier(Player player) {
        if (CombatPrayer.isPrayerOn(player, CombatPrayer.SHARP_EYE))
            return 1.05;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.HAWK_EYE))
            return 1.1;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.EAGLE_EYE))
            return 1.15;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.RIGOUR))
            return 1.2;
        else
            return 1.0;
    }

    private double getEffectiveRangeAttackLevel(NPC npc) {
        NpcCombatDefinition definition = npc.getCombatDefinition();
        if (definition == null) {
            return SetAccuracyBonus.RANGE_ATTACK;
        }

        double effectiveLevel = definition.getLevel(NpcCombatSkill.RANGE);
        return effectiveLevel + SetAccuracyBonus.RANGE_ATTACK;
    }

    private double getEquipmentAttackRangeBonus(Entity attacker) {
        return attacker.getBonus(Bonus.ATTACK_RANGED);
    }

    private double applyAttackSpecials(Player attacker, Entity defender, double base,
                                       double specialAttackMultiplier) {
        double hit = base;

        hit *= getEquipmentMultiplier(attacker);
        hit = Math.floor(hit);

        if (specialAttackMultiplier == 1.0) {
            double multiplier = 1.0;

            // Twisted Bow Effect
            if (attacker.getItems().isWearingItem(Items.TWISTED_BOW, Player.playerWeapon)) {
                if (defender.isNPC()) {
                    double damageCap = 140.0;
                    int magicLevel = 0;
                    if (defender.isPlayer())
                        magicLevel = defender.asPlayer().playerLevel[Skill.MAGIC.getId()];
                    else {
                        NpcCombatDefinition definition = defender.asNPC().getCombatDefinition();
                        if (definition != null) {
                            magicLevel = definition.getLevel(NpcCombatSkill.MAGIC);
                        }
                    }
                    multiplier += getTwistedBowAccuracyBoost(magicLevel, Boundary.isIn(attacker, Boundary.XERIC));
                }
            }
            hit *= multiplier;
            hit = Math.floor(hit);
        } else {
            hit *= specialAttackMultiplier;
            hit = Math.floor(hit);
        }

        return hit;
    }

    private double getEquipmentMultiplier(Player attacker) {
        return 1.0;
    }


    private int getDefenceRoll(Entity attacker, Entity defender) {
        double effectiveDefenceLevel = defender.isPlayer() ? getEffectiveDefenceLevel(defender.asPlayer()) : getEffectiveDefenceLevel(defender.asNPC());


        int equipmentDefenceBonus = getEquipmentDefenceBonus(defender);

        double maxRoll = effectiveDefenceLevel * (equipmentDefenceBonus + 64.0);
        maxRoll = applyDefenceSpecials(defender, maxRoll);

        return (int) maxRoll;
    }

    private double applyDefenceSpecials(Entity defender, double base) {
        double hit = base;

        if (defender.isPlayer()) {
            Player player = defender.asPlayer();
            if (EquipmentSet.TORAG.isWearingBarrows(player) && EquipmentSet.AMULET_OF_THE_DAMNED.isWearing(player)) {
                hit *= ToragsEffect.modifyDefenceLevel(player);
                hit = Math.floor(hit);
            }
        }

        return hit;
    }

    private int getEquipmentDefenceBonus(Entity defender) {
        return defender.getBonus(Bonus.DEFENCE_RANGED);
    }

    private double getEffectiveDefenceLevel(Player player) {
        double effectiveLevel = Math.floor(player.playerLevel[Skill.RANGED.getId()] *
                getPrayerDefenceMultiplier(player));

        switch (player.getCombatConfigs().getWeaponMode().getAttackStyle()) {
            case DEFENSIVE:
                effectiveLevel += 3.0;
                break;
            case CONTROLLED:
                effectiveLevel += 1.0;
                break;
        }
        effectiveLevel += 8;

        return Math.floor(effectiveLevel);
    }

    private double getPrayerDefenceMultiplier(Player player) {
        if (CombatPrayer.isPrayerOn(player, CombatPrayer.THICK_SKIN))
            return 1.05;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.ROCK_SKIN))
            return 1.10;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.STEEL_SKIN))
            return 1.15;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.CHIVALRY))
            return 1.20;
        else if (CombatPrayer.isPrayerOn(
                player,
                CombatPrayer.PIETY, CombatPrayer.RIGOUR, CombatPrayer.AUGURY
        ))
            return 1.25;
        else
            return 1.0;
    }

    private double getEffectiveDefenceLevel(NPC npc) {
        NpcCombatDefinition definition = npc.getCombatDefinition();
        if (definition == null)
            return 8;

        return definition.getLevel(NpcCombatSkill.DEFENCE) + 8.0;
    }

    @Override
    public int getMaxHit(Entity attacker, Entity defender, double specialAttackMultiplier,
                         double specialPassiveMultiplier) {

        int base = 0;

        if (maxhit != -1) {
            base = maxhit;
        } else {
            double effectiveRangedLevel = attacker.isPlayer() ?
                    this.getEffectiveRangeLevel(attacker.asPlayer(), defender) : getEffectiveRangeLevel(attacker.asNPC());
            double equipmentBonus = getEquipmentRangeBonus(attacker);

            if (attacker.isPlayer()) {
                Player player = attacker.asPlayer();
                if (player.getItems().isWearingItem(25865) &&
                        player.getItems().isWearingItem(Items.CRYSTAL_BODY) &&
                        player.getItems().isWearingItem(Items.CRYSTAL_HELM) &&
                        player.getItems().isWearingItem(Items.CRYSTAL_LEGS)) {
                    specialAttackMultiplier += 0.3;
                }
                if (player.getItems().isWearingItem(25865) &&
                        player.getItems().isWearingItem(23842) &&
                        player.getItems().isWearingItem(23845) &&
                        player.getItems().isWearingItem(23845)) {
                    specialAttackMultiplier += 0.3;
                }
                if(player.getItems().isWearingItem(8813) && player.getItems().isWearingItem(8814)){
                    specialAttackMultiplier += 0.15;
                }
                if(player.getItems().isWearingItem(23995) &&
                        player.getItems().isWearingItem(Items.CRYSTAL_BODY) &&
                        player.getItems().isWearingItem(Items.CRYSTAL_HELM) &&
                        player.getItems().isWearingItem(Items.CRYSTAL_LEGS)){
                    specialAttackMultiplier += 0.3;

                }
                if (player.getItems().isWearingItem(8815)&&
                        player.getItems().isWearingItem(23842) &&
                        player.getItems().isWearingItem(23845) &&
                        player.getItems().isWearingItem(23845)) {
                    specialAttackMultiplier += 0.3;
                }




                // If wearing blowpipe take the stored dart range bonus instead
                if (player.getItems().isWearingItem(Items.TOXIC_BLOWPIPE, Player.playerWeapon)) {
                    int dartId = player.getToxicBlowpipeAmmo();
                    ItemStats stats = ItemStats.forId(dartId);
                    equipmentBonus += stats.getEquipment().getRstr();
                }
            }
            base = (int) Math.floor(1.3 + (effectiveRangedLevel / 10.0) + (equipmentBonus / 80.0) + ((effectiveRangedLevel * equipmentBonus) / 640.0));
        }

        base = applyRangedSpecials(attacker, defender, base, specialAttackMultiplier, specialPassiveMultiplier);

        return (int) Math.floor(base);
    }

    private double getEffectiveRangeLevel(Player player, Entity target) {
        double effectiveLevel = Math.floor(player.playerLevel[Skill.RANGED.getId()]
                * getPrayerRangedMultiplier(player));

        if (target.isNPC()) {
            effectiveLevel *= getNpcMultipliers(player, target.asNPC());
            effectiveLevel = Math.floor(effectiveLevel);
        }

        switch (player.getCombatConfigs().getWeaponMode().getAttackStyle()) {
            case ACCURATE:
                effectiveLevel += 3.0;
                break;
        }

        effectiveLevel += 8.0;

        if (player.fullEliteVoidRange()) {
            effectiveLevel *= 1.125;
            effectiveLevel = Math.floor(effectiveLevel);
        } else if (player.fullVoidRange()) {
            effectiveLevel += 1.1;
            effectiveLevel = Math.floor(effectiveLevel);
        }

        return Math.floor(effectiveLevel);
    }

    private double getPrayerRangedMultiplier(Player player) {
        if (CombatPrayer.isPrayerOn(player, CombatPrayer.SHARP_EYE))
            return 1.05;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.HAWK_EYE))
            return 1.1;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.EAGLE_EYE))
            return 1.15;
        else if (CombatPrayer.isPrayerOn(player, CombatPrayer.RIGOUR))
            return 1.23;
        else
            return 1.0;
    }

    private double getEffectiveRangeLevel(NPC npc) {
        NpcCombatDefinition definition = npc.getCombatDefinition();
        if (definition == null)
            return 8;

        return definition.getLevel(NpcCombatSkill.RANGE) + 8.0;
    }

    private double getEquipmentRangeBonus(Entity attacker) {
        return attacker.getBonus(Bonus.RANGED_STRENGTH);
    }

    private int applyRangedSpecials(Entity attacker, Entity defender, int base, double specialAttackMultiplier,
                                    double specialPassiveMultiplier) {
        double hit = base;

        Player player = attacker.isPlayer() ? attacker.asPlayer() : null;
        if (player != null) {
            hit *= getEquipmentMultiplier(player);
            hit = Math.floor(hit);
        }

        if (player != null && specialAttackMultiplier == 1.0) {
            double multiplier = 1.0;

            if (player.getItems().isWearingItem(Items.TWISTED_BOW, Player.playerWeapon)||player.getItems().isWearingItem(Items.TWISTED_BOW_L, Player.playerWeapon)||player.getItems().isWearingItem(Items.TWISTED_BOW_P, Player.playerWeapon)) {
                if (defender.isNPC()) {
                    int magicLevel = 1;
                    if (defender.isPlayer()) {
                        magicLevel = defender.asPlayer().playerLevel[Skill.DEFENCE.getId()];
                    } else {
                        NpcCombatDefinition definition = defender.asNPC().getCombatDefinition();
                        if (definition != null) {
                            magicLevel = definition.getLevel(NpcCombatSkill.MAGIC);
                        }
                    }
                    multiplier = getTwistedBowDamageBoost(magicLevel, Boundary.isIn(player, Boundary.XERIC)); // do I add them their too ? no
                }
            }
            hit *= multiplier;
            hit = Math.floor(hit);
        } else {
            hit *= specialAttackMultiplier;
            hit = Math.floor(hit);
        }

        if (defender.isPlayer()) {
            Player target = defender.asPlayer();
            if (CombatPrayer.isPrayerOn(target, CombatPrayer.PROTECT_FROM_RANGED)) {
                hit *= 0.6D;
                hit = Math.floor(hit);
            }
        }

        if (specialAttackMultiplier == 1.0 && player != null) {
            hit = applyPassiveMultiplier(player, defender, hit);
            hit = Math.floor(hit);
        } else {
            hit *= specialPassiveMultiplier;
            hit = Math.floor(hit);
        }

        hit *= getDamageDealMultiplier(attacker);
        hit = Math.floor(hit);

        hit *= getDamageTakenMultiplier(defender);
        hit = Math.floor(hit);

        return (int) hit;
    }

    private double getNpcMultipliers(Player attacker, NPC defender) {
        double multiplier = 1.0;
        if (attacker.fasterCluesScroll == true)
            multiplier += 0.15;

        // Slayer Helmet boost
        if (attacker.getSlayer().hasSlayerHelmBoost(defender, CombatType.RANGE)) {
            multiplier += 0.1666;
        } else {
            // Salve Amulet boosts
            multiplier += getSalveAmuletMultiplier(attacker, defender);
        }

        // DHCB Multiplier
        if (attacker.getItems().isWearingItem(Items.DRAGON_HUNTER_CROSSBOW, Player.playerWeapon))
            if (defender.isDragon())
                multiplier += 0.25;

        if (attacker.getPosition().inWild() && defender.getPosition().inWild()) {
            if (PvpWeapons.activateEffect(attacker, attacker.getItems().getWeapon())) {
                multiplier += 0.5;
            }
        }

        // Pet Boosts
        boolean hasDarkRangePet = PetHandler.hasDarkRangePet(attacker);
        boolean hasRangePet = PetHandler.hasRangePet(attacker);
        if (hasDarkRangePet)
            multiplier += 0.10;
        else if (hasRangePet && Misc.isLucky(50))
            multiplier += 0.10;


        return multiplier;
    }

    private double applyPassiveMultiplier(Player player, Entity defender, double hit) {
        return hit;
    }

    private double getDamageDealMultiplier(Entity attacker) {
        return 1.0;
    }

    private double getDamageTakenMultiplier(Entity defender) {
        if (defender.isPlayer()) {
            Player player = defender.asPlayer();

            if (player.getCombatItems().elyProc())
                return 0.75;

        }

        return 1.0;
    }

    public static double getTwistedBowAccuracyBoost(int magicLevel, boolean cox) {
        if (magicLevel > (cox ? 350 : 250))
            magicLevel = (cox ? 350 : 250);
        double boost = 140 + ((3d * magicLevel - 10d) / 100d) - (Math.pow(3d * magicLevel / 10d - 100d, 2) / 100d);
        return (Math.min(boost, 140) / 100);
    }

    public static double getTwistedBowDamageBoost(int magicLevel, boolean cox) {
        if (magicLevel > (cox ? 350 : 250))
            magicLevel = (cox ? 350 : 250);
        double boost = 250 + ((3d * magicLevel - 14d) / 100d) - (Math.pow((3d * magicLevel / 10d) - 140d, 2) / 100d);
        return (Math.min(boost, cox ? 350 : 250) / 100);
    }
    public static double getTwistedBowLDamageBoost(int magicLevel, boolean cox) {
        if (magicLevel > (cox ? 350 : 250))
            magicLevel = (cox ? 350 : 250);
        double boost = 250 + ((3d * magicLevel - 14d) / 100d) - (Math.pow((3d * magicLevel / 10d) - 140d, 2) / 100d);
        return (Math.min(boost, cox ? 350 : 250) / 100);
    }
    public static double getTwistedBowPDamageBoost(int magicLevel, boolean cox) {
        if (magicLevel > (cox ? 350 : 250))
            magicLevel = (cox ? 350 : 250);
        double boost = 250 + ((3d * magicLevel - 14d) / 100d) - (Math.pow((3d * magicLevel / 10d) - 140d, 2) / 100d);
        return (Math.min(boost, cox ? 350 : 250) / 100);
    }
}
