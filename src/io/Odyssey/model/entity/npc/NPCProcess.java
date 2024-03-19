package io.Odyssey.model.entity.npc;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.bosses.*;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;

import io.Odyssey.content.bosses.hydra.AlchemicalHydra;
import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.content.bosses.wildypursuit.FragmentOfSeren;
import io.Odyssey.content.bosses.wildypursuit.TheUnbearable;
import io.Odyssey.content.combat.death.NPCDeath;
import io.Odyssey.content.minigames.TombsOfAmascut.bosses.Baba;
import io.Odyssey.content.minigames.inferno.AncestralGlyph;
import io.Odyssey.content.minigames.inferno.InfernoWaveData;
import io.Odyssey.content.minigames.rfd.DisposeTypes;
import io.Odyssey.content.tasks.impl.Tasks;
import io.Odyssey.model.Animation;
import io.Odyssey.model.Direction;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.StillGraphic;
import io.Odyssey.model.collisionmap.RegionProvider;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.definitions.AnimationLength;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.actions.NPCHitPlayer;
import io.Odyssey.model.entity.npc.actions.NpcAggression;
import io.Odyssey.model.entity.npc.data.RespawnTime;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.Odyssey.model.Npcs.FUMUS;
import static io.Odyssey.model.Npcs.GLACIES;

public class NPCProcess {

    private static final Logger logger = LoggerFactory.getLogger(NPCProcess.class);

    private final NPCHandler npcHandler;

    NPCProcess(NPCHandler npcHandler) {
        this.npcHandler = npcHandler;
    }

    private int i;
    private NPC npc;
    private int type;
    private AlchemicalHydra hydraInstance;

    public void process(int i) {
        this.i = i;
        if (NPCHandler.npcs[i] == null) {
            logger.debug("Trying to process null npc inde " + i);
            return;
        }
        npc = NPCHandler.npcs[i];
        type = NPCHandler.npcs[i].getNpcId();
        Optional<AlchemicalHydra> hydraInstance = NPCHandler.getHydraInstance(npc);
        this.hydraInstance = hydraInstance.orElse(null);
        processing();
    }

    private void processing() {
        if (npc.getInstance() != null) {
            npc.getInstance().tick(npc);
            if (npc.getInstance().isDisposed()) {
                logger.debug("NPC instance was disposed, unregistering {}", npc);
                npc.unregister();
                return;
            }
        }

        Player slaveOwner = (PlayerHandler.players[npc.summonedBy]);
        if (slaveOwner == null && npc.summoner) {
            npc.absX = 0;
            npc.absY = 0;
        }
        if (slaveOwner != null && slaveOwner.hasFollower && (!slaveOwner.goodDistance(npc.getX(), npc.getY(), slaveOwner.absX, slaveOwner.absY,
                15) || slaveOwner.heightLevel != npc.heightLevel) && npc.summoner) {
            npc.absX = slaveOwner.absX;
            npc.absY = slaveOwner.absY;
            npc.heightLevel = slaveOwner.heightLevel;

        }
        if (npc.actionTimer > 0) {
            npc.actionTimer--;
        }

        if (npc.freezeTimer > 0) {
            npc.freezeTimer--;
        }
        if (npc.hitDelayTimer > 0) {
            npc.hitDelayTimer--;
        }
        if (npc.hitDelayTimer == 1) {//where the application of damage happens when hitdelaytimer reaches 1. THATS WHY HIT DELAY TIMER CANT BE LESS THAN 2.
            npc.hitDelayTimer = 0;
            NPCHitPlayer.applyDamage(npc, npcHandler);
        }
        if (npc.attackTimer > 0) {
            npc.attackTimer--;
        }
        if (npc.getNpcId() == 7553) {//olm hands
            npc.walkingHome = true;
        }
        if (npc.getNpcId() == 7555) {//olm hands
            npc.walkingHome = true;
        }

        if (npc.getNpcId() == 10520) {
            npc.getBehaviour().isAggressive();
            npc.getBehaviour().setRespawn(false);
        }


        if (npc.getHealth().getCurrentHealth() > 0 && !npc.isDead()) {
            if (npc.getNpcId() == 11994) {
                if (npc.getHealth().getCurrentHealth() < (npc.getHealth().getMaximumHealth() / 2)
                        && !npc.spawnedMinions) {
                    NPC npc1 = NPCSpawning.spawnNpc(Npcs.GREATER_SKELETON_HELLHOUND, npc.getX() - 1, npc.getY(), 1, 1, 14);
                    NPC npc2 = NPCSpawning.spawnNpc(Npcs.GREATER_SKELETON_HELLHOUND, npc.getX() + 1, npc.getY(), 1, 1, 14);
                    if (npc1 != null && npc2 != null) {
                        npc1.getBehaviour().setAggressive(true);
                        npc2.getBehaviour().setAggressive(true);
                    }
                    npc.spawnedMinions = true;
                }
            }
            if (npc.getNpcId() == 11993 ) {
                if (npc.getHealth().getCurrentHealth() < (npc.getHealth().getMaximumHealth() / 2)
                        && !npc.spawnedMinions) {
                    NPC npc1 = NPCSpawning.spawnNpc(Npcs.SKELETON_HELLHOUNDZ, npc.getX() - 1, npc.getY(), 1, 1, 14);
                    NPC npc2 = NPCSpawning.spawnNpc(Npcs.SKELETON_HELLHOUNDZ, npc.getX() + 1, npc.getY(), 1, 1, 14);
                    if (npc1 != null && npc2 != null) {
                        npc1.getBehaviour().setAggressive(true);
                        npc2.getBehaviour().setAggressive(true);
                    }
                    npc.spawnedMinions = true;
                }
            }
        }
        if (npc.getNpcId() == 6600 && !npc.isDead()) {
            NPC runiteGolem = NPCHandler.getNpc(6600);
            if (runiteGolem != null && !runiteGolem.isDead()) {
                npc.setDead(true);
                npc.needRespawn = false;
                npc.actionTimer = 0;
            }
        }

        if (npc.getInstance() == null) { // Only delete summoned npcs when not inside an instance
            if (npc.spawnedBy > 0) { // delete summons npc
                Player spawnedBy = PlayerHandler.players[npc.spawnedBy];
                if (spawnedBy == null || spawnedBy.heightLevel != npc.heightLevel || spawnedBy.respawnTimer > 0
                        || !spawnedBy.goodDistance(npc.getX(), npc.getY(), spawnedBy.getX(),
                        spawnedBy.getY(),
                        NPCHandler.isFightCaveNpc(npc) ? 60 : NPCHandler.isTokkulPit1Npc(npc) ? 60 : NPCHandler.isSkotizoNpc(npc) ? 60 : 20)) {


                    npc.unregister();


                }
            }
        }

        if (npc.lastX != npc.getX() || npc.lastY != npc.getY()) {
            npc.lastX = npc.getX();
            npc.lastY = npc.getY();
        }

        if (hydraInstance != null) {
            hydraInstance.onTick();
        }

        // Inferno glyph movement
        if (npc.getNpcId() == InfernoWaveData.ANCESTRAL_GLYPH) {
            if (PlayerHandler.players[npc.spawnedBy] != null) {
                AncestralGlyph.handleMovement(PlayerHandler.players[npc.spawnedBy], npc);
            }
        }

        if (type == 6615) {
            if (npc.walkingHome) {
                npc.getHealth().setCurrentHealth(200);
            }
            Scorpia.spawnHealer();

        }
        if (type == 11246) {
            if (npc.walkingHome) {
                npc.getHealth().setCurrentHealth(2500);
            }

        }
//        if (type == 11278) {//nex reset?
//            if (NexNPC.nexnpc.targets.isEmpty()) {
//                System.out.println("here ging home?");
//              //  npc.getHealth().setCurrentHealth(2500);
//              //  npc.getInstance().dispose();
//                NexNPC.nexnpc.onDeath(false);
//            }
//
//        }
        if (type == 11402) {
            if (npc.walkingHome) {
                npc.getHealth().setCurrentHealth(2500);
            }

        }
        if (type == 11282) {
            if (npc.walkingHome) {
                npc.getHealth().setCurrentHealth(2500);
            }

        }
        if (type == 11670) {
            if (npc.walkingHome) {
                npc.getHealth().setCurrentHealth(2500);
            }

        }
//this is same as nex.. as world boss essentially
        if (type == Npcs.CORPOREAL_BEAST) {
            CorporealBeast.checkCore(npc);
            CorporealBeast.healWhenNoPlayers(npc);
        }

        if (type == 8026 || type == 8027) {
            npc.setFacePlayer(false);
        }
        if (type == 8028) {
            npc.setFacePlayer(true);
        }
        if ((type == 2042 || type == 2044) && npc.getHealth().getCurrentHealth() > 0) {
            Player player = PlayerHandler.players[npc.spawnedBy];
            if (player != null && player.getZulrahEvent().getNpc() != null
                    && npc.equals(player.getZulrahEvent().getNpc())) {
                int stage = player.getZulrahEvent().getStage();
                if (type == 2042) {
                    if (stage == 0 || stage == 1 || stage == 4 || stage == 9 && npc.totalAttacks >= 20
                            || stage == 11 && npc.totalAttacks >= 5) {
                        return;
                    }
                }
                if (type == 2044) {
                    if ((stage == 5 || stage == 8) && npc.totalAttacks >= 5) {
                        return;
                    }
                }
            }
        }

        NpcAggression.doAggression(npc, npcHandler);
        if (Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS)) {
            if (!npc.underAttack && NpcAggression.getCloseRandomPlayer(npc) <= 0) {
                npc.getHealth().reset();
            }
        }
        ///if (npcs[i].killerId <= 0) {
        if (System.currentTimeMillis() - npc.lastDamageTaken > 5000 && !npc.underAttack) {
            npc.underAttackBy = 0;
            npc.underAttack = false;
            npc.randomWalk = true;
        }
        if (System.currentTimeMillis() - npc.lastDamageTaken > 10000) {
            npc.underAttackBy = 0;
            npc.underAttack = false;
            npc.randomWalk = true;
        }
        // }

        if ((npc.getPlayerAttackingIndex() > 0 || npc.underAttack)
                && !npc.walkingHome
                && npcHandler.retaliates(npc.getNpcId())) {
            if (!npc.isDead()) {
                int p = npc.getPlayerAttackingIndex();
                if (PlayerHandler.players[p] != null) {
                    if (!npc.summoner) {
                        Player c = PlayerHandler.players[p];
                        if (c.getInferno() != null && c.getInferno().kill.contains(npc)) {
                            return;
                        }
                        npcHandler.followPlayer(npc, c.getIndex());
                        if (npc.attackTimer == 0) {

                            if (npc.getNpcAutoAttacks().isEmpty()) {
                                npcHandler.attackPlayer(c, npc);//ALL REGULAR ATTACKING BY AN NPC HANDLED HERE
                            } else {
                                npc.selectAutoAttack(c);
                                npc.attack(c, npc.getCurrentAttack());//ALL ATTACKS HANDLED BY STUFF THAT HAS NPCAUTOATTACK STUFF IN IT (MEH) HERE.
                            }
                        }
                    } else {
                        Player c = PlayerHandler.players[p];
                        if (c.absX == npc.absX && c.absY == npc.absY) {
                            npcHandler.stepAway(npc);
                            npc.randomWalk = false;
                            if (npc.getNpcId() == InfernoWaveData.JAL_NIB) {
                                return;
                            }
                            npc.facePlayer(c.getIndex());
                        } else {
                            if (c.getInferno() != null && c.getInferno().kill.contains(npc)) {
                                return;
                            }
                            npcHandler.followPlayer(npc, c.getIndex());
                        }
                    }
                } else {
                    npc.setPlayerAttackingIndex(0);
                    npc.underAttack = false;
                    npc.facePlayer(0);
                }
            }
        }


        // Random walking and walking home
        if ((!npc.underAttack) && !NPCHandler.isFightCaveNpc(npc) && !Boundary.isIn(npc, Boundary.GODWARS_BOSSROOMS) && !NPCHandler.isTokkulPit1Npc(npc) && npc.randomWalk && !npc.isDead() && npc.getBehaviour().isWalkHome()) {
            npc.facePlayer(0);
            npc.setPlayerAttackingIndex(0);
            // handleClipping(i);
            if (npc.spawnedBy == 0) {//when an npc moves too far from its spawn x,y  it will reset UNLESS its added to this list.
                if ((npc.absX > npc.makeX + Configuration.NPC_RANDOM_WALK_DISTANCE)
                        || (npc.absX < npc.makeX - Configuration.NPC_RANDOM_WALK_DISTANCE)
                        || (npc.absY > npc.makeY + Configuration.NPC_RANDOM_WALK_DISTANCE)
                        || (npc.absY < npc.makeY - Configuration.NPC_RANDOM_WALK_DISTANCE)
                        && npc.getNpcId() != 1635 && npc.getNpcId() != 1636 && npc.getNpcId() != 1637
                        && npc.getNpcId() != 1638 && npc.getNpcId() != 1639 && npc.getNpcId() != 1640
                        && npc.getNpcId() != 1641 && npc.getNpcId() != 1642 && npc.getNpcId() != 1643
                        && npc.getNpcId() != 1654 && npc.getNpcId() != 7302) {
                    npc.walkingHome = true;
                }
            }

            if (npc.walkingType >= 0) {
                switch (npc.walkingType) {
                    case 5:
                        npc.facePosition(npc.absX - 1, npc.absY);
                        break;
                    case 4:
                        npc.facePosition(npc.absX + 1, npc.absY);
                        break;
                    case 3:
                        npc.facePosition(npc.absX, npc.absY - 1);
                        break;
                    case 2:
                        npc.facePosition(npc.absX, npc.absY + 1);
                        break;
                }
            }

            if (npc.walkingType == 1 && (!npc.underAttack) && !npc.walkingHome) {//where the npc randomly walks around walkingtype == 1 = "walk"
                if (System.currentTimeMillis() - npc.getLastRandomWalk() > npc.getRandomWalkDelay()) {
                    int direction = Misc.trueRand(8);
                    int movingToX = npc.getX() + NPCClipping.DIR[direction][0];
                    int movingToY = npc.getY() + NPCClipping.DIR[direction][1];
                    if (npc.getNpcId() >= 1635 && npc.getNpcId() <= 1643 || npc.getNpcId() == 1654|| npc.getNpcId() == 5418
                            || npc.getNpcId() == 7302) {
                        NPCDumbPathFinder.walkTowards(npc, npc.getX() - 1 + Misc.random(3),
                                npc.getY() - 1 + Misc.random(3));
                    } else {
                        if (Math.abs(npc.makeX - movingToX) <= 1 && Math.abs(npc.makeY - movingToY) <= 1
                                && NPCDumbPathFinder.canMoveTo(npc, direction)) {
                            NPCDumbPathFinder.walkTowards(npc, movingToX, movingToY);
                        }
                    }
                    npc.setRandomWalkDelay(TimeUnit.SECONDS.toMillis(1 + Misc.random(2)));
                    npc.setLastRandomWalk(System.currentTimeMillis());
                }
            }
        }
        if (npc.walkingHome) {
            if (!npc.isDead()) {
                NPCDumbPathFinder.walkTowards(npc, npc.makeX, npc.makeY);
                if (npc.walkDirection == Direction.NONE) {
                    npc.teleport(npc.makeX, npc.makeY, npc.heightLevel);
                    if (npc.absX == npc.makeX && npc.absY == npc.makeY) {
                        npc.walkingHome = false;
                    }
                    return;
                }
                if (npc.absX == npc.makeX && npc.absY == npc.makeY) {
                    npc.walkingHome = false;
                }
            } else {
                npc.walkingHome = false;
            }
        }

        /**
         * Npc death
         */
        if (npc.isDead()) {
            processDeath();
        } else {
            npc.processMovement();
        }
    }

    private void processDeath() {
        if (npc.isDead()) {
            Player playerOwner = PlayerHandler.players[npc.spawnedBy];
            npc.getRegionProvider().removeNpcClipping(npc);

            if (npc.actionTimer == 0 && !npc.applyDead && !npc.needRespawn) {

                // Vorkath
                if (npc.getNpcId() == 8028) {
                    CycleEventHandler.getSingleton().addEvent(playerOwner, new CycleEvent() {
                        @Override
                        public void execute(CycleEventContainer container) {
                      //      int dropHead = Misc.random(50);
                          //  if (playerOwner.getNpcDeathTracker().getKc("vorkath") == 50) {
                                Server.itemHandler.createGroundItem(playerOwner, 2425, Vorkath.lootCoordinates[0],
                                        Vorkath.lootCoordinates[1], playerOwner.heightLevel, 1, playerOwner.getIndex());
                           // }
                            Vorkath.spawn(playerOwner);
                            container.stop();
                        }

                        @Override
                        public void onStopped() {
                        }
                    }, 9);
                }

                if (npc.getNpcId() == 9021 || npc.getNpcId() == 9022 || npc.getNpcId() == 9023) {
                    npc.startAnimation(8421);
                    npc.requestTransform(9024);
                    npc.getHealth().reset();
                    npc.setDead(true);
                    npc.forceChat("RAAAAARGHHHHH!");
                    npc.applyDead = true;
                }

                if (npc.getNpcId() == 6618) {
                    npc.forceChat("Ow!");
                }

                if (npc.getNpcId() == 8781) {
                    npc.forceChat("You will pay for this!");
                    npc.startAnimation(-1);
                    npc.gfx0(1005);
                }
                if  (npc.getNpcId() == 11670) {
                    NexAngelOfDeathWE.stage = 0;
                    NexAngelOfDeathWE.NexAngelOfDeathWEHealed = false;
                    NexAngelOfDeathWE.currentAttack = 0;
                    NexAngelOfDeathWE.phase = -1;
                    NexAngelOfDeathWE.spawnedMinions = false;
                    NexAngelOfDeath.spawnedReavers = false;
                }
//                if(npc.getNpcId() == 11246) {
//                    npc.forceChat("My house bitch!");
//                }
//                if(npc.getNpcId() == 10402) {
//                    npc.forceChat("Hit me with an ohhh yeaaaa!");
//                }
//                if(npc.getNpcId() == 11299) {
//                    npc.forceChat("Taste my nuts!");
//                }
//                if(npc.getNpcId() == 11670) {
//                    npc.forceChat("Taste my dick ass butt cheeks!");
//                }
                if (npc.getNpcId() == 11993) {
                    npc.requestTransform(11994);
                    npc.getHealth().reset();
                    npc.setDead(false);
                    npc.spawnedMinions = false;
                    npc.forceChat("Do it again!!");
                } else {
                    if (npc.getNpcId() == 11994) {
                        npc.setNpcId(11993);
                        npc.spawnedMinions = false;
                    }

                    if (npc.getNpcId() == 9024) {
                        npc.setNpcId(9021);
                    }

                    if (npc.getNpcId() == 1605) {
                        CycleEventHandler.getSingleton().addEvent(playerOwner, new CycleEvent() {
                            @Override
                            public void execute(CycleEventContainer container) {
                                NPCSpawning.spawnNpcOld(playerOwner, 1606, 3106, 3934, 0, 1, 30, 24, 70, 60, true, true);
                                container.stop();
                            }

                            @Override
                            public void onStopped() {
                            }
                        }, 5);
                    }

                    Player killer1 = PlayerHandler.players[npc.spawnedBy];

                    if(killer1 != null){

                        if (npc.getNpcId() == 1606) {
                            killer1.roundNpc=3;
                            killer1.spawned = false;
                            checkMa(killer1);
                        }
                        if (npc.getNpcId() == 1607) {
                            killer1.spawned = false;
                            checkMa(killer1);
                        }
                        if (npc.getNpcId() == 1608) {
                            killer1.spawned = false;
                            checkMa(killer1);
                        }

                        if (npc.getNpcId() == 1609) {
                            if (killer1 != null) {
                                killer1.spawned = false;
                                checkMa(killer1);
                            }
                        }
                    }

                    npc.actionTimer = AnimationLength.getFrameLength(npc.getDeathAnimation());
                    if (!"Dusk".equals(npc.getDefinition().getName())) { // Dusk animation length is long and we want it that way
                        if (npc.actionTimer > 20) {      // Fix for death animations being too long
                            npc.actionTimer = 20;
                        }
                    }

                    npc.setUpdateRequired(true);
                    npc.facePlayer(0);
                    Entity killer = npc.calculateKiller();

                    if (killer != null) {
                        npc.killedBy = killer.getIndex();
                    }

                    npc.freezeTimer = 0;
                    npc.applyDead = true;

                    if (npc.getNpcId() == 3118) {//fight cave split into 2.
                        NPCSpawning.spawnNpc(3120, npc.absX, npc.absY, playerOwner.heightLevel, 10, 15);
                        NPCSpawning.spawnNpc(3120, npc.absX, npc.absY + 1, playerOwner.heightLevel, 10, 15);
                    }

                    if (npc.getNpcId() == InfernoWaveData.JAL_AK && playerOwner.getInferno() != null) {
                        NPCSpawning.spawnNpc(playerOwner, InfernoWaveData.JAL_AKREK_KET, npc.absX,
                                npc.absY, playerOwner.heightLevel, 1,
                                InfernoWaveData.getMax(InfernoWaveData.JAL_AKREK_KET),
                                true, false);
                        NPCSpawning.spawnNpc(playerOwner, InfernoWaveData.JAL_AKREK_XIL, npc.absX,
                                npc.absY + 1, playerOwner.heightLevel, 1,
                                InfernoWaveData.getMax(InfernoWaveData.JAL_AKREK_XIL),
                                true, false);
                        NPCSpawning.spawnNpc(playerOwner, InfernoWaveData.JAL_AKREK_MEJ,
                                npc.absX + 1, npc.absY + 1, playerOwner.heightLevel, 1,
                                InfernoWaveData.getMax(InfernoWaveData.JAL_AKREK_MEJ),
                                true, false);
                        playerOwner.getInferno().setKillsRemaining(
                                playerOwner.getInferno().getKillsRemaining() + 3);
                    }

                    if (playerOwner != null) {
                        npcHandler.tzhaarDeathHandler(playerOwner, npc);
                        npcHandler.infernoDeathHandler(playerOwner, npc);
                        npcHandler.Tokkulpit1DeathHandler(playerOwner, npc);
                    }

                   // if (hydraInstance == null) {
                        npc.startAnimation(npc.getDeathAnimation());
                   // }


                    if (npc.getNpcId() == 963) {//funny way of kalphite queen transforming...just says its not dead then transforms into flying version.
                        npc.actionTimer = 0;
                        npc.setDead(false);
                        npc.requestTransform(965);
                        npc.getHealth().reset();
                        npc.applyDead = false;
                        npc.startAnimation(Animation.RESET_ANIMATION);
                    }
                    npcHandler.killedBarrow(i);// i = npc
                    npcHandler.resetPlayersInCombat(i);
                }
            } else if (npc.actionTimer == 0 && npc.applyDead && !npc.needRespawn) {
                int killerIndex = npc.killedBy;
                if (npc.getNpcId() == 11246 || npc.getNpcId() == 11282) {//could add hunllef here lol... see it just  drops it randomly like that.
                    System.out.println("NEX LOOT");
                    PlayerHandler.nonNullStream().filter(p -> npc.getNpcId() == 11282 ? Boundary.isIn(p, Boundary.NEX) : Boundary.isIn(p, Boundary.REVENANT_MALEDICTUS))
                            .forEach(plr -> {
                                System.err.println("giving loot to " + plr.getDisplayName());

                                NPCDeath.dropItemsFor(npc, plr, npc.getNpcId());

                            });
                } else {
                    NPCDeath.dropItems(npc);

                }
                if (npc.getNpcId() == 11282) {
                    NexAngelOfDeath.wrathTimer.setDuration(4);
                    NexAngelOfDeath.wrathDMGTimer.setDuration(4);
                    for (int x = npc.getX(); x < npc.getX() + 5; x++) {
                        for (int y = npc.getY(); y < npc.getY() + 5; y++) {
                            Server.playerHandler.sendStillGfx(new StillGraphic(2013, new Position(x, y)));
                            NexAngelOfDeath.wrathLocations.add(new Position(x, y));
                        }
                    }
                }
                if (npc.getNpcId() == 11670) {
                    NexAngelOfDeathWE.wrathTimer.setDuration(4);
                    NexAngelOfDeathWE.wrathDMGTimer.setDuration(4);
                    for (int x = npc.getX(); x < npc.getX() + 5; x++) {
                        for (int y = npc.getY(); y < npc.getY() + 5; y++) {
                            Server.playerHandler.sendStillGfx(new StillGraphic(2013, new Position(x, y)));
                            NexAngelOfDeathWE.wrathLocations.add(new Position(x, y));
                        }
                    }
                }
                npc.onDeath();

                if (killerIndex <= PlayerHandler.players.length - 1) {
                    Player target = PlayerHandler.players[npc.killedBy];

                    if (target != null) {
                        target.getSlayer().killTaskMonster(npc);
                        target.getBossTimers().death(npc);
                        target.getQuesting().handleNpcKilled(npc);
                    }
                }

                if (npc.getRaidsInstance() != null) {
                    Optional<Player> plrOpt = PlayerHandler.getOptionalPlayerByIndex(npc.killedBy);
                    npc.getRaidsInstance().handleMobDeath(plrOpt.orElse(null), type);
                }
                if (npc.getRaids3Instance() != null) {
                    Optional<Player> plrOpt = PlayerHandler.getOptionalPlayerByIndex(npc.killedBy);
                    npc.getRaids3Instance().handleMobDeath(plrOpt.orElse(null), type);
                }
                if (npc.inXeric()) {
                    Player killer = PlayerHandler.players[npc.killedBy];
                    npcHandler.xericDeathHandler(killer, npc);
                }
                npcHandler.appendBossKC(npc);
                npcHandler.handleGodwarsDeath(npc);
                npcHandler.handleDiaryKills(npc);
                npcHandler.appendtaskkc(npc);
                npc.getRegionProvider().removeNpcClipping(npc);
                npc.absX = npc.makeX;
                npc.absY = npc.makeY;
                npc.getHealth().reset();
                npc.startAnimation(0x328);


                /**
                 * Actions on certain npc deaths
                 */
                Skotizo skotizo = playerOwner != null ? playerOwner.getSkotizo() : null;
                switch (npc.getNpcId()) {
                    case 965:
                        npc.setNpcId(963);
                        break;
                        //when a rock crab dies it should respawn as the rocks.
                    case 100:
                        npc.setNpcId(101);
                        break;
                    case 8611:
                        npc.setNpcId(8610);
                        break;
                    case TheUnbearable.NPC_ID:
                        if (playerOwner != null) {
                            PlayerHandler.executeGlobalMessage("@red@[EVENT]@blu@ the wildy boss [@red@Unbearable@blu@] has been defeated!");
                        }
                        TheUnbearable.rewardPlayers();
                        break;

                    case AvatarOfCreation.NPC_ID:
                        PlayerHandler.executeGlobalMessage("@red@[EVENT]@blu@ the world boss [@gre@AvatarOfCreation@blu@] has been defeated!");
                        AvatarOfCreation.rewardPlayers(true);
                        break;

                    case FragmentOfSeren.NPC_ID:
                        if (playerOwner != null) {
                            PlayerHandler.executeGlobalMessage("@red@[EVENT]@blu@ the wildy boss [@red@Seren@blu@] has been defeated!");
                        }
                        FragmentOfSeren.rewardPlayers();
                        FragmentOfSeren.specialAmount = 0;
                        break;
                    case FragmentOfSeren.CRYSTAL_WHIRLWIND:
                        FragmentOfSeren.activePillars.remove(npc);
                        if (FragmentOfSeren.activePillars.size() == 0) {
                            FragmentOfSeren.isAttackable = true;
                        }
                        npc.unregister();
                        break;
                    case 3127:
                        playerOwner.getFightCave().stop();
                        break;

                    case Skotizo.SKOTIZO_ID:
                        skotizo.end();
                        break;

                    case InfernoWaveData.TZKAL_ZUK:
                        if (playerOwner.getInferno() != null) {
                            playerOwner.getInferno().end(DisposeTypes.COMPLETE);
                        }
                        break;

                    case Skotizo.AWAKENED_ALTAR_NORTH:
                        Server.getGlobalObjects().remove(28923, 1694, 9904, skotizo.getHeight()); // Remove
                        // North
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1694, 9904,
                                skotizo.getHeight(), 2, 10, -1, -1)); // North - Empty Altar
                        playerOwner.getPA().sendChangeSprite(29232, (byte) 0);
                        skotizo.altarCount--;
                        skotizo.northAltar = false;
                        skotizo.altarMap.remove(1);
                        break;
//                    case 6987://man
//                            playerOwner.getTaskMasterManager().increase(Tasks.MAN_FUCKER);
//
//                        break;
                    case Skotizo.AWAKENED_ALTAR_SOUTH:
                        Server.getGlobalObjects().remove(28923, 1696, 9871, skotizo.getHeight()); // Remove
                        // South
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1696, 9871,
                                skotizo.getHeight(), 0, 10, -1, -1)); // South - Empty Altar
                        playerOwner.getPA().sendChangeSprite(29233, (byte) 0);
                        skotizo.altarCount--;
                        skotizo.southAltar = false;
                        skotizo.altarMap.remove(2);
                        break;
                    case Skotizo.AWAKENED_ALTAR_WEST:
                        Server.getGlobalObjects().remove(28923, 1678, 9888, skotizo.getHeight()); // Remove
                        // West
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1678, 9888,
                                skotizo.getHeight(), 1, 10, -1, -1)); // West - Empty Altar
                        playerOwner.getPA().sendChangeSprite(29234, (byte) 0);
                        skotizo.altarCount--;
                        skotizo.westAltar = false;
                        skotizo.altarMap.remove(3);
                        break;
                    case Skotizo.AWAKENED_ALTAR_EAST:
                        Server.getGlobalObjects().remove(28923, 1714, 9888, skotizo.getHeight()); // Remove
                        // East
                        // -
                        // Awakened
                        // Altar
                        Server.getGlobalObjects().add(new GlobalObject(28924, 1714, 9888,
                                skotizo.getHeight(), 3, 10, -1, -1)); // East - Empty Altar
                        playerOwner.getPA().sendChangeSprite(29235, (byte) 0);
                        skotizo.altarCount--;
                        skotizo.eastAltar = false;
                        skotizo.altarMap.remove(4);
                        break;
                    case Skotizo.DARK_ANKOU:
                        skotizo.ankouSpawned = false;
                        break;

                    case 6615:
                        Scorpia.stage = 0;
                        break;
                    case 11246:
                        RevenantMaledictus.stage = 0;
                        RevenantMaledictus.revenantmaledictusHealed = false;
                        RevenantMaledictus.currentAttack = 0;
                        RevenantMaledictus.phase = -1;
                        break;
                    case 10402:
                        ColossalHydra.stage = 0;
                        ColossalHydra.colossalhydraHealed = false;
                        ColossalHydra.currentAttack = 0;
                        ColossalHydra.phase = -1;
                        break;
                    case 11282:
                        NexAngelOfDeath.stage = 0;
                        NexAngelOfDeath.NexAngelOfDeathHealed = false;
                        NexAngelOfDeath.currentAttack = 0;
                        NexAngelOfDeath.phase = -1;
                        NexAngelOfDeath.spawnedMinions = false;
                        NexAngelOfDeath.spawnedReavers = false;
                        NexAngelOfDeath.rewardPlayers(true);

                        break;
                    case 6600:
                        NPCSpawning.spawnNpc(6601, npc.absX, npc.absY, 0, 0, 0);
                        break;

                    case 6601:
                        NPCSpawning.spawnNpc(6600, npc.absX, npc.absY, 0, 0, 0);
                        npc.unregister();
                        NPC golem = NPCHandler.getNpc(6600);
                        if (golem != null) {
                            golem.actionTimer = 150;
                        }
                        break;
                    case 2256:

                        Optional<WorldObject> object_real = npc.getRegionProvider().get(npc.absX, npc.absY).getWorldObject(8967, 2545, 10145,0);
                        GlobalObject theobject = new GlobalObject(object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, object_real.get().type);
                        Server.getGlobalObjects().add(theobject);
                        Server.getGlobalObjects().remove(theobject);
                        Server.getGlobalObjects().updateObject(theobject, -1);
                      // Server.getGlobalObjects().add(new GlobalObject(-1, 2545, 10145, 0, 0, 10, -1, -1)); // West - Empty Altar
                        break;
                    case 2253:
                       object_real = npc.getRegionProvider().get(npc.absX, npc.absY).getWorldObject(8967, 2543, 10143,0);
                       theobject = new GlobalObject(object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, object_real.get().type);
                        Server.getGlobalObjects().add(theobject);
                        Server.getGlobalObjects().remove(theobject);
                        Server.getGlobalObjects().updateObject(theobject, -1);
                       // Server.getGlobalObjects().add(new GlobalObject(-1, 2543, 10143, 0));
                        break;
                    case 2250:
                        object_real = npc.getRegionProvider().get(npc.absX, npc.absY).getWorldObject(8967, 2545, 10141,0);
                        theobject = new GlobalObject(object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, object_real.get().type);
                        Server.getGlobalObjects().add(theobject);
                        Server.getGlobalObjects().remove(theobject);
                        Server.getGlobalObjects().updateObject(theobject, -1);
                       // Server.getGlobalObjects().add(new GlobalObject(-1, 2545, 10141, 0));
                        break;
                    case 5890:
                        NPCHandler.despawn(5916, 0);
                        break;
                    case 6768:
                        npc.unregister();
                        break;
                }

                if (npc.getNpcId() == Npcs.ABYSSAL_SIRE) {
                    NPCHandler.kill(Npcs.SPAWN, npc.getHeight());//weird but ok . like this is for uhhh. yeah. something that is ONCE. like this.
                }

                if (npc != null) {//respawning handled here
                    if (npc.getBehaviour().isRespawn() && (npc.getInstance() == null || npc.getInstance().getConfiguration().isRespawnNpcs())) {
                        npc.needRespawn = true;
                        npc.actionTimer = RespawnTime.get(npc);
                    } else {
                        npc.unregister();
                    }
                }
            } else if (npc.actionTimer == 0 && npc.needRespawn) {
                if (npc.getNpcId() == 1739 || npc.getNpcId() == 1740
                        || npc.getNpcId() == 1741 || npc.getNpcId() == 1742 || npc.getNpcId() >= FUMUS && npc.getNpcId() <= GLACIES) {
                    // Don't respawn
                    return;
                }

                if (playerOwner != null && !npc.getBehaviour().isRespawnWhenPlayerOwned()) {
                    npc.unregister();
                } else {
                    if (playerOwner != null && (playerOwner.properLogout || playerOwner.isDisconnected()))
                        return;
                    if (npc.getInstance() != null && npc.getInstance().isDisposed()) {
                        logger.debug("NPC was going to respawn but instance was disposed {}", npc);
                        return;
                    }

                    if (hydraInstance != null) {
                        hydraInstance.respawn();
                        npc.unregister();
                        return;
                    }

                    // Child respawns with parent ohhh.... nice.
                    if (npc.getParent() != null) {
                        return;
                    }

                    // here is where respawning is handled
                    if (npc.getChildren().stream().allMatch(child -> child.needRespawn)) {
                        npc.getChildren().forEach(child -> npcHandler.respawn(child.getIndex(), playerOwner));
                       // System.out.println("here? respawn?");
                        npcHandler.respawn(i, playerOwner);
                    }
                }
            }
        }
    }
    public void checkMa(Player c) {
        if (c.roundNpc == 111 && !c.spawned) {
            NPCSpawning.spawnNpcOld(c, 1606, 3106, 3934, 0, 1, 65, 14, 70, 60, true, true);
            // NPCSpawning.spawnNpcOld(player, 1605, 3106, 3934, 0, 1, 30, 17, 70, 60, true, true);

            c.roundNpc = 3;
            c.spawned = true;
        } else if (c.roundNpc == 3 && !c.spawned) {
            NPCSpawning.spawnNpcOld(c, 1607, 3106, 3934, 0, 1, 65, 15, 70, 60, true, true);
            c.roundNpc = 4;
            c.spawned = true;
        } else if (c.roundNpc == 4 && !c.spawned) {
            NPCSpawning.spawnNpcOld(c, 1608, 3106, 3934, 0, 1, 78, 15, 70, 60, true, true);
            c.roundNpc = 5;
            c.spawned = true;
        } else if (c.roundNpc == 5 && !c.spawned) {
            NPCSpawning.spawnNpcOld(c, 1609, 3106, 3934, 0, 1, 107, 19, 70, 105, true, true);
            c.roundNpc = 6;
            c.spawned = true;
        } else if (c.roundNpc == 6 && !c.spawned) {
            c.getPA().movePlayer(2541, 4716, 0);
            c.talkingNpc = 1603;
            c.getDH().sendNpcChat3("Congratulations you have proved your self worthy!","head through the waterfall and claim your god cape","The chamber guardian will sell you god staffs.",1603,"Kolodion");
            //   Achievements.increase(c, AchievementType.MAGE_ARENA, 1);
            c.roundNpc = 0;
            c.maRound = 2;
        }
    }
}
