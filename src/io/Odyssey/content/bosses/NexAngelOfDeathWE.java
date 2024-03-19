package io.Odyssey.content.bosses;

import io.Odyssey.Server;
import io.Odyssey.content.combat.Hitmark;
import io.Odyssey.model.ForceMovement;
import io.Odyssey.model.StillGraphic;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.Entity;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.timers.TickTimer;
import io.Odyssey.util.Misc;

import java.util.ArrayList;

public class NexAngelOfDeathWE extends NPC {

    public static final int NPC_ID = 11670;
    // NexAngelOfDeath current attack phase
    public static int phase = -1;

    public static NPC NexAngelOfDeathWE;
    public static NPC fumus, umbra, cruor, glacie;

    /**
     * Checks the healer stage to avoid multiple spawns
     */
    public static int stage;

    public NexAngelOfDeathWE(int npcId, Position position) {
        super(npcId, position);
        NexAngelOfDeathWE = this;
    }

    enum NexAngelOfDeathWEPhase {
        SMOKE("Fill my soul with smoke!", 0), SHADOW("Darken my shadow!", 1), ZAROS("NOW, THE POWER OF ZAROS!", 2);

        String shout;
        int phase;

        NexAngelOfDeathWEPhase(String shout, int phase) {
            this.shout = shout;
            this.phase = phase;
        }

        public String getShout() {
            return shout;
        }

        public static NexAngelOfDeathWEPhase getPhase() {
            for (NexAngelOfDeathWEPhase n : NexAngelOfDeathWEPhase.values()) {
                return n;
            }
            return null;
        }
    }

    public static NexAngelOfDeathWEPhase phases = NexAngelOfDeathWEPhase.SMOKE;
    // set the first phase

    public static int currentAttack = -1;

    // NexAngelOfDeath charge positions  N S E W
    public static final Position[] chargePosition = {new Position(3744, 3987), new Position(3769, 3987), new Position(3757, 3975), new Position(3757, 3999)};

    public static boolean finishedShadows;
    // deadly spots
    public static ArrayList<Position> deadlySpots = new ArrayList<>();
    // the players position
    public static ArrayList<Position> deadlyPosition = new ArrayList<>();

    // NexAngelOfDeath heal final phase
    public static boolean NexAngelOfDeathWEHealed;

    // spawned minions yet?
    public static boolean spawnedMinions, spawnedReavers;

    // timer for wrath to end
    public static TickTimer wrathTimer = new TickTimer();
    public static TickTimer wrathDMGTimer = new TickTimer();

    @Override
    public int modifyDamage(Player player, int damage) {
        super.modifyDamage(player, damage);
        if (player.getPosition().getDistance(this.getPosition()) > 8) return 0;
        return damage;
    }

    @Override
    public void process() {
        PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX)).forEach(p -> {
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
    public void onDeath() {
    }

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

        if ((phase == 0 && Misc.random(1, 200) >= 190) || (phase == 1 && Misc.random(1, 50) >= 40) ||
                // always spawn last phase if they haven't already
                phase == 2) {
            if (!spawnedMinions) {
                spawnedMinions = true;
                System.out.println("here teleporting them back?");
                // spawn minions
                fumus = NPCSpawning.spawnNpc(11283, 3745, 3999, 0, 0, 25);
                umbra = NPCSpawning.spawnNpc(11284, 3745, 3975, 0, 0, 25);
                cruor = NPCSpawning.spawnNpc(11285, 3769, 3975, 0, 0, 25);
                glacie = NPCSpawning.spawnNpc(11286, 3769, 3999, 0, 0, 25);
            }
        }

        if (n.getHealth().getCurrentHealth() >= 2000) {
            phase = 0;
        } else if (n.getHealth().getCurrentHealth() >= 1500 && n.getHealth().getCurrentHealth() <= 1800) {
            phase = 1;

            // spawn NexAngelOfDeath reavers
            if (!spawnedReavers) {
                spawnedReavers = true;
                NPCSpawning.spawnNpc(11294, 3749, 3996, 0, 0, 30);
                NPCSpawning.spawnNpc(11294, 3748, 3979, 0, 0, 30);
                NPCSpawning.spawnNpc(11294, 3766, 3979, 0, 0, 30);
                NPCSpawning.spawnNpc(11294, 3765, 3996, 0, 0, 30);
            }
        } else if (n.getHealth().getCurrentHealth() >= 680 && n.getHealth().getCurrentHealth() <= 1500) {
            phase = 2;
        }

        if (currentAttack == 0) {
            switch (phase) {
                case 0:
                    phases = NexAngelOfDeathWEPhase.SMOKE;
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
                                    n.teleport(new Position(3757, 3987));  //3768, 3987
                                }
                                case 11: {
                                    ForceMovement forceMovement = new ForceMovement(n, 2, new Position(3757, 3987), chargePosition[Misc.random(3)], 15, 30);
                                    forceMovement.startForceMovement();
                                    n.startAnimation(819);
                                    PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX)).forEach(p -> {
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
                    phases = NexAngelOfDeathWEPhase.SHADOW;
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
                                    PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.NEX)).forEach(p -> {
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
                    phases = NexAngelOfDeathWEPhase.ZAROS;
                    n.forceChat(phases.getShout());

                    if (!NexAngelOfDeathWEHealed) {
                        n.appendHeal(500, Hitmark.HEAL_PURPLE);
                        NexAngelOfDeathWEHealed = true;
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