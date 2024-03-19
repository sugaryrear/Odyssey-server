package io.Odyssey.content.bosses;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.Odyssey.Configuration;
import io.Odyssey.Server;
import io.Odyssey.content.achievement.AchievementType;
import io.Odyssey.content.achievement.Achievements;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.content.event.eventcalendar.EventChallenge;
import io.Odyssey.content.miniquests.magearenaii.MageArenaII;
import io.Odyssey.content.miniquests.magearenaii.npcs.type.MageArenaBossType;
import io.Odyssey.model.*;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.model.timers.TickTimer;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;
import org.apache.commons.lang3.Range;


public class NexAngelOfDeath extends NPC {

    // NexAngelOfDeath current attack phase
    public static int phase = -1;

    public static NPC NexAngelOfDeath;
    public static NPC fumus, umbra, cruor, glacie;

    /**
     * Checks the healer stage to avoid multiple spawns
     */
    public static int stage;

    public NexAngelOfDeath(int npcId, Position position) {
        super(npcId, position);
        NexAngelOfDeath = this;
        getBehaviour().setWalkHome(false);
        getBehaviour().setAggressive(true);

        getBehaviour().setRespawn(true);
    }

    enum NexAngelOfDeathPhase {
        SMOKE("Fill my soul with smoke!", 0),
        SHADOW("Darken my shadow!", 1),
        ZAROS("NOW, THE POWER OF ZAROS!", 2);

        String shout;
        int phase;

        NexAngelOfDeathPhase(String shout, int phase) {
            this.shout = shout;
            this.phase = phase;
        }

        public String getShout() {
            return shout;
        } // so this part here

        public static io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase getPhase() {
            for (io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase n : io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase.values()) {
                return n;
            }
            return null;
        }
    }


    // set the first phase
    public static io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase phases = io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase.SMOKE;

    public static int currentAttack = -1;

    // NexAngelOfDeath charge positions  N S E W
    public static final Position[] chargePosition = {new Position(2925, 5214), new Position(2925, 5194),
            new Position(2933, 5203), new Position(2916, 5203)};

    public static boolean finishedShadows;
    // deadly spots
    public static ArrayList<Position> deadlySpots = new ArrayList<>();
    // the players position
    public static ArrayList<Position> deadlyPosition = new ArrayList<>();

    // NexAngelOfDeath heal final phase
    public static boolean NexAngelOfDeathHealed;

    // spawned minions yet?
    public static boolean spawnedMinions, spawnedReavers;

    // timer for wrath to end
    public static TickTimer wrathTimer = new TickTimer();
    public static TickTimer wrathDMGTimer = new TickTimer();

    @Override
    public int modifyDamage(Player player, int damage) {
        super.modifyDamage(player, damage);
        if (player.getPosition().getDistance(this.getPosition()) > 8)
            return 0;
        return damage;
    }

    @Override
    public void process() {
        PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX))
                .forEach(p -> {
                    // standing on deadly spots
                    for (Position pos : deadlySpots) {
                        if (p.getPosition().equals(pos) && finishedShadows) {
                            // make sure we're not dead
                            if (p != null || !p.isDead) {
                                p.appendDamage(Misc.random(10, 35), Hitmark.HIT);
                            }
                        }
                    }

                    // standing on wrath
                    if (wrathDMGTimer.isFinished()) {
                        for (Position pos : wrathLocations) {
                            if (p.getPosition().equals(pos)) {
                                // make sure we're not dead
                                if (p != null || !p.isDead && !wrathTimer.isFinished()) {
                                    p.appendDamage(45, Hitmark.HIT);
                                }
                            }
                        }
                    }


                });


        // removing deadly shadows
        if (finishedShadows) {
            deadlySpots.removeAll(deadlySpots);
            deadlyPosition.removeAll(deadlyPosition);
            finishedShadows = false;
        }

        if (wrathTimer.isFinished()) {
            wrathLocations.removeAll(wrathLocations);
        }
    }

    @Override
    public boolean canBeAttacked(Entity entity) {
        return true;
    }

    @Override
    public boolean canBeDamaged(Entity entity) {
        return true;
    }

    @Override
    public boolean isFreezable() {
        return false;
    }

    public static ArrayList<Position> wrathLocations = new ArrayList<>();

    @Override
    public void onDeath() { }

    // NexAngelOfDeath attacks
    public static void attack(NPC n) {
        if (n == null || n.isDead) {
            return;
        }
        // reset the phase after 2
        if (phase >= 2) {
            phase = 0;
        }

        currentAttack++;

        // add a new phase after 20 attacks
        if (currentAttack >= 20) {
            // reset attacks
            currentAttack = 0;
        }

        if ((phase == 0 && Misc.random(1, 200) >= 190) ||
                (phase == 1 && Misc.random(1, 50) >= 40) ||
                // always spawn last phase if they haven't already
                phase == 2) {
            if (!spawnedMinions) {
                spawnedMinions = true;
                //System.out.println("here teleporting them back?");
                // spawn minions
                fumus = NPCSpawning.spawnNpc(11283, 2925, 5211, 0, 0, 25);

                fumus.getBehaviour().setRespawn(false);
                umbra = NPCSpawning.spawnNpc(11284, 2925, 5195, 0, 0, 25);
                umbra.getBehaviour().setRespawn(false);
                cruor = NPCSpawning.spawnNpc(11285, 2933, 5203, 0, 0, 25);
                cruor.getBehaviour().setRespawn(false);
                glacie = NPCSpawning.spawnNpc(11286, 2916, 5203, 0, 0, 25);
                glacie.getBehaviour().setRespawn(false);
            }
        }

        if (n.getHealth().getCurrentHealth() >= 2000) {
            phase = 0;
        } else if (n.getHealth().getCurrentHealth() >= 1500 && n.getHealth().getCurrentHealth() <= 1800) {
            phase = 1;

            // spawn NexAngelOfDeath reavers
            if (!spawnedReavers) {
                spawnedReavers = true;
                NPCSpawning.spawnNpc(11294, 2925, 5211, 0, 0, 30);
                NPCSpawning.spawnNpc(11294, 2925, 5195, 0, 0, 30);
                NPCSpawning.spawnNpc(11294, 2933, 5203, 0, 0, 30);
                NPCSpawning.spawnNpc(11294, 2916, 5203, 0, 0, 30);
            }
        } else if (n.getHealth().getCurrentHealth() >= 680 && n.getHealth().getCurrentHealth() <= 1500) {
            phase = 2;
        }

        if (currentAttack == 0) {
            switch (phase) {
                case 0:
                    phases = io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase.SMOKE;
                    n.forceChat(phases.getShout());

                    CycleEventHandler.getSingleton().addEvent(n, new CycleEvent() {
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (ticks >= 22) {
                                container.stop();
                            }
                            switch (ticks++) {
                                case 5: {
                                    n.forceChat("There is... NO ESCAPE!");
                                    n.teleport(new Position(2925, 5203));  //3768, 3987
                                }
                                case 11: {
                                    ForceMovement forceMovement = new ForceMovement(n, 2, new Position(2925, 5203), chargePosition[Misc.random(3)], 15, 30);
                                    forceMovement.startForceMovement();
                                    n.startAnimation(9483);
                                    PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX))
                                            .forEach(p -> {
                                                if (p.getPosition().deepCopy().withinDistance(n.getPosition().deepCopy(), 5)) {
                                                    p.appendDamage(25, Hitmark.HIT);
                                                }
                                            });
                                }
                            }
                        }
                    }, 2);

                    break;
                case 1:
                    phases = io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase.SHADOW;
                    n.forceChat(phases.getShout());
                    CycleEventHandler.getSingleton().addEvent(n, new CycleEvent() {
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer container) {
                            switch (ticks++) {
                                case 4: {
                                    n.forceChat("Fear the shadow!");
                                }
                                case 6: {
                                    PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX))
                                            .forEach(p -> {
                                                // add each players location
                                                deadlyPosition.add(p.getPosition().deepCopy());
                                                // send gfx to players location
                                                for (Position deadlyLocation : deadlyPosition) {
                                                    Server.playerHandler.sendStillGfx(new StillGraphic(383, deadlyLocation));
                                                    deadlySpots.add(deadlyLocation);
                                                }
                                            });
                                }
                                case 30: {
                                    container.stop();
                                }
                            }
                        }

                        @Override
                        public void onStopped() {
                            finishedShadows = true;
                        }
                    }, 2);
                    break;
                case 2:
                    phases = io.Odyssey.content.bosses.NexAngelOfDeath.NexAngelOfDeathPhase.ZAROS;
                    n.forceChat(phases.getShout());

                    if (!NexAngelOfDeathHealed) {
                        n.appendHeal(500, Hitmark.HEAL_PURPLE);
                        NexAngelOfDeathHealed = true;
                    }
                    break;
            }
        }
    }


    public static void rewardPlayers(boolean eventCompleted) {
        AngelofdeathSpawner.despawn();
        PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX)).forEach(p -> {
            if (!eventCompleted) {
                p.sendMessage("@blu@AngelOfDeath event was ended before she was killed!");
            } else {
                if (p.getNexAngelOfDeathDamageCounter() >= 200) {
                    p.sendMessage("@blu@AngelOfDeath has been killed!");
                } else {
                    p.sendMessage("@blu@You were not active enough to receive a reward.");
                }
            }
        });
    }
}

