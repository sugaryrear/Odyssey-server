package io.Odyssey.content.combat.core;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.Preconditions;
import io.Odyssey.content.bosses.NexAngelOfDeath;
import io.Odyssey.content.bosses.NexAngelOfDeathWE;
import io.Odyssey.content.bosses.Vorkath;
import io.Odyssey.content.bosses.hydra.HydraStage;
import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.content.bosses.wildypursuit.FragmentOfSeren;
import io.Odyssey.content.minigames.Raids3.Raids3;
import io.Odyssey.content.minigames.raids.Raids;
import io.Odyssey.content.minigames.warriors_guild.WarriorsGuild;
import io.Odyssey.content.skills.Skill;
import io.Odyssey.content.skills.slayer.Slayer;
import io.Odyssey.content.skills.slayer.SlayerMaster;
import io.Odyssey.content.skills.slayer.Task;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.PathFinder;
import io.Odyssey.model.entity.player.Player;

public class AttackNpcCheck {

    private static void sendCheckMessage(Player c, boolean sendMessages, String message) {
        if (sendMessages) {
            c.sendMessage(message);
        }
    }

    public static boolean check(Player c, Entity targetEntity, boolean sendMessages) {
        NPC npc = targetEntity.asNPC();
        int levelRequired;
        if (npc == null || npc.getHealth().getMaximumHealth() == 0) {
            return false;
        }
        if (!npc.canBeAttacked(c)) {
            return false;
        }

//        if (!PathFinder.getPathFinder().accessable(c, npc.getX(), npc.getY())) {
//            c.sendMessage("You cannot reach that!");
//            return false;
//        }

        // Inferno pillar
        if (npc.getNpcId() == 7710) {
            return false;
        }

        // Unpoked vorkath can't attack
        if (npc.getNpcId() == Vorkath.NPC_IDS[0]) {
            return false;
        }

        if (PetHandler.isPet(npc.getNpcId())) {
            return false;
        }

        if (!npc.getPosition().inMulti()) {//probably revenants shit here +1 mechanic
            //!npcs[i].getPosition().inMulti() && ((c.underAttackByPlayer > 0 && c.underAttackByNpc != i)
            //        || (c.underAttackByNpc > 0 && c.underAttackByNpc != i))

            if (!c.getPosition().inMulti() && (c.underAttackByPlayer > 0 && c.underAttackByNpc != npc.getIndex())
                    || (c.underAttackByNpc > 0 && c.underAttackByNpc != npc.getIndex() && npc.getIndex() != 1969 && npc.getIndex() != 7514)) {
                sendCheckMessage(c, sendMessages, "You are already in combat.");

                return false;
            }

            if (!Boundary.isIn(c, Boundary.OLM) && !Boundary.isIn(c, Boundary.RAIDS)) {
                if (npc.underAttackBy > 0 && npc.underAttackBy != c.getIndex()) {
                    sendCheckMessage(c, sendMessages, "This monster is already in combat.");
                    return false;
                }
            }
        }

        switch (npc.getNpcId()) {
            case 11282:
                if (NexAngelOfDeath.fumus != null || NexAngelOfDeath.umbra != null || NexAngelOfDeath.cruor != null || NexAngelOfDeath.glacie != null) {
                    if (!NexAngelOfDeath.fumus.isDeadOrDying() || !NexAngelOfDeath.umbra.isDeadOrDying() || !NexAngelOfDeath.cruor.isDeadOrDying() || !NexAngelOfDeath.glacie.isDeadOrDying()) {
                        // make sure they're at NexAngelOfDeath
                        if (Boundary.isIn(NexAngelOfDeath.fumus, Boundary.NEX) || Boundary.isIn(NexAngelOfDeath.umbra, Boundary.NEX) || Boundary.isIn(NexAngelOfDeath.cruor, Boundary.NEX) ||
                                Boundary.isIn(NexAngelOfDeath.glacie, Boundary.NEX)) {
                            npc.forceChat("Kill my minions before attacking me!");
                            return false;
                        }
                    }
                }
                return true;
            case 11670: // i can do all this if you want kk cya dude thank you bduddy !Q
                if (NexAngelOfDeathWE.fumus != null || NexAngelOfDeathWE.umbra != null || NexAngelOfDeathWE.cruor != null || NexAngelOfDeathWE.glacie != null) {
                    if (!NexAngelOfDeathWE.fumus.isDeadOrDying() || !NexAngelOfDeathWE.umbra.isDeadOrDying() || !NexAngelOfDeathWE.cruor.isDeadOrDying() || !NexAngelOfDeathWE.glacie.isDeadOrDying()) {
                        // make sure they're at NexAngelOfDeath
                        if (Boundary.isIn(NexAngelOfDeathWE.fumus, Boundary.angelOfDeathWorldevent) || Boundary.isIn(NexAngelOfDeathWE.umbra, Boundary.angelOfDeathWorldevent) || Boundary.isIn(NexAngelOfDeathWE.cruor, Boundary.angelOfDeathWorldevent) ||
                                Boundary.isIn(NexAngelOfDeathWE.glacie, Boundary.angelOfDeathWorldevent)) {
                            npc.forceChat("Kill my minions before attacking me!");
                            return false;
                        }
                    }
                }
                return true;

                //case 5890:
                case 7563:
                case 5916:
                case 2045:
                    return true; // Skip "I am already under attack" check
                case 8781:
                    if (!Boundary.isIn(c, Boundary.DONATOR_ZONE_BOSS)) {
                        sendCheckMessage(c, sendMessages, "You must be closer to the Queen in order to attack it.");
                        return false;
                    }
                    break;
            }

            if ((c.underAttackByPlayer > 0 || c.underAttackByNpc > 0) && c.underAttackByNpc != npc.getIndex() && !c.getPosition().inMulti()) {
                sendCheckMessage(c, sendMessages, "I am already under attack.");
                return false;
            }

            // Zulrah
            if (npc.getNpcId() >= 2042 && npc.getNpcId() <= 2044 || npc.getNpcId() == 6720) {
                if (c.getZulrahEvent().isTransforming()) {
                    return false;
                }
                if (c.getZulrahEvent().getStage() == 0) {
                    return false;
                }
            }

            // Hydra
            if (HydraStage.isHydra(npc.getNpcId())) {
                int x = c.absX;
                int y = c.absY;
                if ((x == 1356 || x == 1357) && y >= 10257 && y <= 10278
                        || (y == 10278 || y == 10277) && x >= 1358 && x <= 1377
                        || (x == 1377 || x == 1376) && y >= 10257 && y <= 10278
                        || (y == 10257 || y == 10258) && x >= 1356 && x <= 1377) {
                    sendCheckMessage(c, sendMessages, "@red@You can't hit Alchemical Hydra from this position!");
                    return false;
                }
            }
            if (c.playerEquipment[Player.playerWeapon] == 22547 || c.playerEquipment[Player.playerWeapon] == 22542 || c.playerEquipment[Player.playerWeapon] == 22552) {
                sendCheckMessage(c, sendMessages, "Your weapon needs more then 1000 charges to function properly.");
                return false;
            }

            if (c.playerEquipment[Player.playerWeapon] == 12904 && c.usingSpecial) {
                c.usingSpecial = false;
                c.getItems().updateSpecialBar();
                c.attacking.reset();
                return false;
            }
            if (npc.getNpcId() == FragmentOfSeren.NPC_ID && !FragmentOfSeren.isAttackable) {
                sendCheckMessage(c, sendMessages, "You can't attack her right now.");
                return false;
            }
            Preconditions.checkState(npc != null, "Npc is null.");
        if (Boundary.isIn(c, Boundary.TAVELRY_BLACK_DRAGON_TASK_AREA)){
         //   System.out.println("here black dr?");
 if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("black dragon"))) {

                c.getPA().movePlayer(2874, 9827, 1);
                c.getDH().sendDialogues(7653, 7653);
                return false;
            }
        }
        if (Boundary.isIn(c, Boundary.TAVELRY_BLUE_DRAGON_TASK_AREA)){
         //   System.out.println("here?");
            if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("blue dragon"))) {

                c.getPA().movePlayer(2905, 9815, 1);
                c.getDH().sendDialogues(7672, 7672);
                return false;
            }
        }
            Optional<Task> task = SlayerMaster.get(npc.getName().replaceAll("_", " ").toLowerCase());
            if (task.isPresent()) {
              //  if(c.heightLevel == 1 && c.absX > 2871 && c.absX < 2872){


            //   }

               // System.out.println("name: "+npc.getName().replaceAll("_", " "));
                int level = task.get().getLevel();
                if (c.playerLevel[Skill.SLAYER.getId()] < task.get().getLevel()) {
                    sendCheckMessage(c, sendMessages, "You need a slayer level of " + level + " to attack this npc.");
                    c.attacking.reset();
                    return false;
                }
            }
            if (npc.getNpcId() == 7544) {
                if (!Boundary.isIn(c, Boundary.TEKTON_ATTACK_BOUNDARY) && !Boundary.isIn(c, Boundary.XERIC)) {
                    sendCheckMessage(c, sendMessages, "You must be within tektons territory to attack him.");
                    return false;
                }
            }
            if (npc.getNpcId() == 7573) {
                if (!Boundary.isIn(c, Boundary.SHAMAN_BOUNDARY) && !Boundary.isIn(c, Boundary.XERIC)) {
                    sendCheckMessage(c, sendMessages, "You must be within the shaman attack boundries");
                    return false;
                }
            }
            if (npc.getNpcId() == 7554) {
                Raids raidInstance = c.getRaidsInstance();
                if (raidInstance != null) {
                    if (!raidInstance.rightHand || !raidInstance.leftHand) {
                        sendCheckMessage(c, sendMessages, "@red@Please destroy both hands before attacking The Great Olm.");
                        return false;
                    }
                }
            }
            if (npc.getNpcId() == 7554) {
                Raids3 raids3Instance = c.getRaids3Instance();
                if (raids3Instance != null) {
                    if (!raids3Instance.rightHand || !raids3Instance.leftHand) {
                        sendCheckMessage(c, sendMessages, "@red@Please destroy both hands before attacking The Great Olm.");
                        return false;
                    }
                }

            }
            if (npc.getNpcId() == 4922 || npc.getNpcId() == 5129 || npc.getNpcId() == 8918 || npc.getNpcId() == 7860) {
                if (!Boundary.isIn(c, Boundary.WILDERNESS)) {
                    sendCheckMessage(c, sendMessages, "You must be within this npc's original spawn location!");
                    return false;
                }
            }
            if (npc.getNpcId() == 499) {
                if (!c.getSlayer().onTask("thermonuclear smoke devil") && !c.getSlayer().onTask("smoke devil")) {
                    sendCheckMessage(c, sendMessages, "You do not have a " + npc.getName().replace("_", " ") + " task.");
                    return false;
                }
            }

            if (npc.getNpcId() == Npcs.CERBERUS) {
                if (!c.getSlayer().onTask("cerberus") && !c.getSlayer().onTask("hellhound")) {
                    sendCheckMessage(c, sendMessages, "You need a Cerberus or Hellhound task to fight Cerberus.");
                    return false;
                }

                if (c.getLevel(Skill.SLAYER) < 91) {
                    sendCheckMessage(c, sendMessages, "You need a Slayer level of 91 to fight Cerberus.");
                    return false;
                }
            }
//        if(npc.getNpcId() == 11728 && NexNPC.nexnpc.currentPhase == null) {
//            sendCheckMessage(c, sendMessages, "Nex is preparing...");
//            return false;
//        }
//        if(npc.getNpcId() == 11283 && !NexNPC.nexnpc.fumusStarted) {//very very very good way of linking one npc to another nice!!!
//            sendCheckMessage(c, sendMessages, "Fumus blocks all your incoming damage.");
//            return false;
//        }
//        if(npc.getNpcId() == 11284 && !NexNPC.nexnpc.umbraStarted) {
//            sendCheckMessage(c, sendMessages, "Umbra blocks all your incoming damage.");
//            return false;
//        }
//        if(npc.getNpcId() == 11285 && !NexNPC.nexnpc.cruorStarted) {
//            sendCheckMessage(c, sendMessages, "Cruor blocks all your incoming damage.");
//            return false;
//        }
//        if(npc.getNpcId() == 11286 && !NexNPC.nexnpc.glaciesStarted) {
//            sendCheckMessage(c, sendMessages, "Glacies blocks all your incoming damage.");
//            return false;
//        }

        if (npc.getNpcId() == 492 || npc.getNpcId() == 494) {
                if (!c.getSlayer().onTask("kraken")) {
                    sendCheckMessage(c, sendMessages, "You do not have a cave kraken task.");
                    return false;
                }
//                if (!c.getSlayer().getTask().isPresent() || !c.getSlayer().getTask().get().getPrimaryName().contains("kraken")) {
//                    sendCheckMessage(c, sendMessages, "You do not have a cave kraken task.");
//                    return false;
//                }
            }
            if (npc.getNpcId() == 8609) {
                if (c.playerLevel[18] < 95) {
                    sendCheckMessage(c, sendMessages, "You must have a slayer level of at least 95.");
                    return false;
                }
            }

//        if (npc.getNpcId() >= 5886 && npc.getNpcId() <= 5981 && npc.getNpcId() != 5944&& npc.getNpcId() != 5935) {//abyssal sire
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("abyssal demon") && !c.getSlayer().getTask().get().getPrimaryName().equals("abyssal sire"))) {
//                    sendCheckMessage(c, sendMessages, "You need an abyssal task to attack this monster.");
//                    return false;
//                }
//            }

//            if (npc.getNpcId() == 9026) {//rat
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline rat"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 9027) {//spider
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline spider"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 9028) {//bat
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline bat"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 9029) {//unicorn
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline unicorn"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 9030) {//scorpion
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline scorpion"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 9031) {//wolf
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline wolf"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 9032) {//bear
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline bear"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 9033) {//dragon
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline dragon"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//
//            }
//            if (npc.getNpcId() == 9034) {//darkbeast
//                if (!(c.getSlayer().getTask().isPresent()) || (c.getSlayer().getTask().isPresent() && !c.getSlayer().getTask().get().getPrimaryName().equals("crystalline dark beast"))) {
//                    sendCheckMessage(c, sendMessages, "The creature does not seem interested.");
//                    return false;
//                }
//            }
//            if (npc.getNpcId() == 11993) {
//                List<NPC> minion = Arrays.asList(NPCHandler.npcs);
//                if (minion.stream().filter(Objects::nonNull).anyMatch(n -> n.getNpcId() == 12107 && !n.isDead() && n.getHealth().getCurrentHealth() > 0)) {
//                    sendCheckMessage(c, sendMessages, "You must kill Vet'ions minions before attacking him.");
//                    return false;
//                }
//            }
        if (npc.getNpcId() == 11994) {
            List<NPC> minion = Arrays.asList(NPCHandler.npcs);
            if (minion.stream().filter(Objects::nonNull).anyMatch(n -> n.getNpcId() == 12108 && !n.isDead() && n.getHealth().getCurrentHealth() > 0)) {
                sendCheckMessage(c, sendMessages, "You must kill Vet'ions minions before attacking him.");
                return false;
            }
        }
            if (npc.getNpcId() != 5890 && npc.getNpcId() != 5916) {
                if ((c.underAttackByPlayer > 0 || c.underAttackByNpc > 0) && c.underAttackByNpc != npc.getIndex() && !c.getPosition().inMulti()) {
                    sendCheckMessage(c, sendMessages, "I am already under attack.");
                    return false;
                }
            }
            if (npc.spawnedBy != c.getIndex() && npc.spawnedBy > 0 && !Boundary.isIn(c, Boundary.XERIC)) {
                sendCheckMessage(c, sendMessages, "This monster was not spawned for you.");
                return false;
            }
            if (c.getX() == npc.getX() && c.getY() == npc.getY()) {
                c.getPA().walkTo(0, 1);
            }

            if (Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS) && !Boundary.isIn(c, Boundary.GODWARS_BOSSROOMS)) {
                sendCheckMessage(c, sendMessages, "You cannot attack that npc from outside the room.");
                return false;
            }
            int npcType = npc.getNpcId();
            if (npcType == 2463 || npcType == 2464) {
                if (Boundary.isIn(c, WarriorsGuild.CYCLOPS_BOUNDARY)) {
                    if (!c.getWarriorsGuild().isActive()) {
                        sendCheckMessage(c, sendMessages, "You cannot attack a cyclops without talking to kamfreena.");
                        return false;
                    }
                }
            }

        return true;
    }

}


