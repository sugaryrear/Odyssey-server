package io.Odyssey.content.bosses.AvatarOfCreation;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.Odyssey.Server;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.model.Items;
import io.Odyssey.model.definitions.NpcStats;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;

import static io.Odyssey.content.combat.Hitmark.HIT;


public class AvatarOfCreation {

    private static final List<AvatarOfCreationBonus> bonuses = Lists.newArrayList(new AttasBonus(), new IasorBonus(), new KronosBonus(),
            new BuchuBonus(), new CelastrusBonus(), new GolparBonus(), new KeldaBonus(), new NoxiferBonus(), new ConsecrationBonus());
    public static final int AvatarOfCreation_PLANTER_OBJECT = 33983;
    public static final int[] AvatarOfCreation_RARE_SEEDS = {
            AvatarOfCreationBonusPlant.KRONOS.getItemId(),
            AvatarOfCreationBonusPlant.IASOR.getItemId(),
            AvatarOfCreationBonusPlant.ATTAS.getItemId(),
            AvatarOfCreationBonusPlant.KELDA.getItemId(),
            AvatarOfCreationBonusPlant.NOXIFER.getItemId(),
            AvatarOfCreationBonusPlant.BUCHU.getItemId(),
            AvatarOfCreationBonusPlant.CELASTRUS.getItemId(),
            AvatarOfCreationBonusPlant.GOLPAR.getItemId(),
            AvatarOfCreationBonusPlant.CONSECRATION.getItemId()
    };
    /**
     * Variables
     */
    public static final int[] AvatarOfCreation_GROW_PHASE_OBJECTS = {33726, 33727, 33728, 33729};
    public static final int FINAL_OBJECT_ID = 33730;
    public static final int KEY = 22374;

    public static final int NPC_ID = 10520;

    public static final int X = 3438;
    public static final int Y = 3104;
    public static final int SPAWN_ANIMATION = 8442;
    public static final int DEATH_ANIMATION = 8845;

    public static final int AttackAnimation = 8844;
    public static final int MAGIC_ANIMATION = 8844;

    public static final int RANGE_PROJECTILE = 8844;
    public static final int MAGIC_PROJECTILE = 8844;
    public static final int SPECIAL_PROJECTILE = 8844;

    public static final int SPECIAL_HIT_GFX = 8844;

    public static final int ESSENCE_REQUIRED = 200;
    public static final int TOXIC_GEM_EFFECT = 30;

    public static int TOXIC_GEM_AMOUNT = 0;
    public static int AvatarOfCreation_DEFENCE = 3000;

    public static int TOTAL_ESSENCE_BURNED = 0;

    public static boolean ENOUGH_BURNED = false;
    public static boolean isWeak = false;


    /**
     * AvatarOfCreation rewards player
     * @param eventCompleted
     */
    public static void rewardPlayers(boolean eventCompleted) {
        TOTAL_ESSENCE_BURNED = 0;
        TOXIC_GEM_AMOUNT = 0;
        AvatarOfCreation_DEFENCE = 3000;
        ENOUGH_BURNED = false;
        isWeak = false;
        AvatarOfCreationSpawner.despawn();
        Server.getGlobalObjects().add(new GlobalObject(FINAL_OBJECT_ID, X, Y,
                0, 1, 10, -1, -1)); // West - Empty Altar
        PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.AvatarOfCreation))
                .forEach(p -> {
                    if (!eventCompleted) {
                        p.sendMessage("@blu@AvatarOfCreation event was ended before she was killed!");
                        p.canLeaveAvatarOfCreation = true;
                        p.getPA().startTeleport2(3087, 3491, 0);
                        p.setAvatarOfCreationDamageCounter(0);
                        deleteEventItems(p);
                    } else {
                        if (p.getAvatarOfCreationDamageCounter() >= 200) {
                            p.sendMessage("@blu@AvatarOfCreation has been killed!");
                            p.sendMessage("@blu@Harvest AvatarOfCreation with an axe to receive your reward!");
                            p.canLeaveAvatarOfCreation = true;
                            p.canHarvestAvatarOfCreation = true;
                            p.setAvatarOfCreationDamageCounter(0);
                            p.getEventCalendar().progress(EventChallenge.OBTAIN_X_AvatarOfCreation_EVENT_KEYS);
                            //	LeaderboardUtils.addCount(LeaderboardType.AvatarOfCreation, p, 1);
                            Achievements.increase(p, AchievementType.AvatarOfCreation, 1);
                            deleteEventItems(p);
                        } else {
                            p.sendMessage("@blu@You were not active enough to receive a reward.");
                            p.canLeaveAvatarOfCreation = true;
                            p.getPA().startTeleport2(3087, 3491, 0);
                            p.setAvatarOfCreationDamageCounter(0);
                            deleteEventItems(p);
                        }
                    }
                });
    }
    /**
     * AvatarOfCreation Boss Event Mechanics
     */

    public static boolean clickObject(Player player, int objectId) {
        if (objectId == AvatarOfCreation_PLANTER_OBJECT) {
            List<AvatarOfCreationBonus> list = bonuses.stream().filter(bonus -> player.getItems().playerHasItem(bonus.getPlant().getItemId())).collect(Collectors.toList());

            if (list.isEmpty()) {
                player.sendMessage("You need a AvatarOfCreation seed to plant here.");
            } else if (list.size() > 1) {
                player.sendMessage("You have too many @gre@AvatarOfCreation seeds@bla@ please bring only 1 seed type.");
            } else {
                plant(player, (AvatarOfCreation) list.get(0));
            }
        }

        return false;
    }

    private static void plant(Player player, AvatarOfCreation AvatarOfCreationBonus) {
        if (!player.getItems().playerHasItem(AvatarOfCreationBonus.getPlant().getItemId())) {
            player.sendMessage("You don't have the seed.");
            player.getPA().removeAllWindows();
            return;
        }

        if (AvatarOfCreationBonus.canPlant(player)) {
            player.getItems().deleteItem(AvatarOfCreationBonus.getPlant().getItemId(), 1);
            AvatarOfCreationBonus.activate(player);
            AvatarOfCreationBonus.updateObject(true);
//            if (!player.getMode().isOsrs() && !player.getMode().is5x()) {
//                player.getPA().addSkillXP(250000 , Skill.FARMING.getId(), true);
//            } else {
                player.getPA().addSkillXP(50000 , Skill.FARMING.getId(), true);
          //  }
           // player.getItems().addItemUnderAnyCircumstance(Items.AvatarOfCreation_KEY, 5);
            player.getItems().addItemUnderAnyCircumstance(21046, 5);
        }
    }

    private void updateObject(boolean b) {
    }

    private void activate(Player player) {
    }

    private boolean canPlant(Player player) {
     return true;
    }

    private AvatarOfCreationBonusPlant getPlant() {
        return null;
    }

    public static void deleteEventItems (Player c) {
        if (c.getItems().playerHasItem(9698)
                || c.getItems().playerHasItem(9699)
                || c.getItems().playerHasItem(23778)
                || c.getItems().playerHasItem(23783)
                || c.getItems().playerHasItem(9017)) {
            c.getItems().deleteItem2(9698, 28);
            c.getItems().deleteItem2(9699, 28);
            c.getItems().deleteItem2(23778, 28);
            c.getItems().deleteItem2(923783, 28);
            c.getItems().deleteItem2(9017, 28);
        }
    }

    /**
     * AvatarOfCreation Seeds Bonus Time Handling
     */

    public static long ATTAS_TIMER, KRONOS_TIMER, IASOR_TIMER, GOLPAR_TIMER, BUCHU_TIMER, NOXIFER_TIMER, KELDA_TIMER, CELASTRUS_TIMER, CONSECRATION_TIMER;
    public static boolean activeAttasSeed = false;
    public static boolean activeKronosSeed = false;
    public static boolean activeIasorSeed = false;
    public static boolean activeBuchuSeed = false;
    public static boolean activeNoxiferSeed = false;
    public static boolean activeGolparSeed = false;
    public static boolean activeKeldaSeed = false;
    public static boolean activeCelastrusSeed = false;
    public static boolean activeConsecrationSeed = false;
    public static int chosenSkillid;

    public static String getSaveFile() {
        return Server.getSaveDirectory() + "AvatarOfCreation_seed_bonuses.txt";
    }

    public static void init() {
        try {
            File f = new File(getSaveFile());
            if (!f.exists()) {
                Preconditions.checkState(f.createNewFile());
            }
            Scanner sc = new Scanner(f);
            int i = 0;
            while(sc.hasNextLine()){
                i++;
                String line = sc.nextLine();
                String[] details = line.split("=");
                String amount = details[1];

                switch (i) {
                    case 1:
                        ATTAS_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 2:
                        KRONOS_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 3:
                        IASOR_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 4:
                        KELDA_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 5:
                        CELASTRUS_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 6:
                        NOXIFER_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 7:
                        BUCHU_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 8:
                        GOLPAR_TIMER = (int) Long.parseLong(amount);
                        break;
                    case 9:
                        CONSECRATION_TIMER = (int) Long.parseLong(amount);
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String activeAnimaBonus() {
        if (AvatarOfCreation.ATTAS_TIMER > 0) {
            return "Anima: @gre@Attas [Bonus XP]";
        }
        if (AvatarOfCreation.KRONOS_TIMER > 0) {
            return "Anima: @gre@Kronos [x2 Raids 1 Keys]";
        }
        if (AvatarOfCreation.IASOR_TIMER > 0) {
            return "Anima: @gre@Iasor [+10% DR]";
        }
        return "Anima: @red@None";
    }

}

