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


public class RevenantMaledictus extends NPC { // used the nex script for the rev boss

    public static final int REVENANT_MALEDICTUS_ID = 11246;
    // nex current attack phase
    public static int phase = -1;
    public static NPC revenantmaledictus;
    public static final Position SPAWN_POSITION = new Position(3225, 10132, 0);

    /**
     * Checks the healer stage to avoid multiple spawns
     */
    public static int stage;
    public RevenantMaledictus(int npcId, Position position) {
        super(npcId, position);
        getBehaviour().setWalkHome(false);
        getBehaviour().setAggressive(true);

        getBehaviour().setRespawn(true);
        revenantmaledictus = this;
    }

    enum RevenantMaledictusPhase {
        SMOKE("GRAAAAA", 0),
        SHADOW("I hope you get pk'd!", 1),
        ZAROS("NOW, THE POWER OF Surge all up in you!", 2);

        String shout;
        int phase;

        RevenantMaledictusPhase(String shout, int phase) {
            this.shout = shout;
            this.phase = phase;
        }

        public String getShout() {
            return shout;
        }

        public static RevenantMaledictusPhase getPhase() {
            for (RevenantMaledictusPhase n : RevenantMaledictusPhase.values()) {
                return n;
            }
            return null;
        }
    }


    // set the first phase
    public static RevenantMaledictusPhase phases = RevenantMaledictusPhase.SMOKE;

    public static int currentAttack = -1;

    public static boolean finishedShadows;
    // deadly spots
    public static ArrayList<Position> deadlySpots = new ArrayList<>();
    // the players position
    public static ArrayList<Position> deadlyPosition = new ArrayList<>();

    // nex heal final phase
    public static boolean revenantmaledictusHealed;

    @Override
    public int modifyDamage(Player player, int damage) {
        super.modifyDamage(player, damage);
        if (player.getPosition().getManhattanDistance(this.getPosition()) > 8)
            return 0;
        return damage;
    }
    public static TickTimer wrathTimer = new TickTimer();
    public static TickTimer wrathDMGTimer = new TickTimer();
    @Override
    public void process() {
        PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.REVENANT_MALEDICTUS))
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
                });


        // removing deadly shadows
        if (finishedShadows) {
            deadlySpots.removeAll(deadlySpots);
            deadlyPosition.removeAll(deadlyPosition);
            finishedShadows = false;
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
        }
        if (n.getHealth().getCurrentHealth() >= 2000) {
            phase = 0;
        } else if (n.getHealth().getCurrentHealth() >= 1500 && n.getHealth().getCurrentHealth() <= 1800) {
            phase = 1;
            ;
        } else if (n.getHealth().getCurrentHealth() >= 680 && n.getHealth().getCurrentHealth() <= 1500) {
            phase = 2;
        }

        if (currentAttack == 0) {
            switch (phase) {
                case 0:
                    phases = RevenantMaledictusPhase.SMOKE;
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
                                    //n.forceChat("My house Bitch!");
                                    n.teleport(new Position(3234, 10203));
                                }
                            }
                        }
                    }, 2);

                    break;
                case 1:
                    phases = RevenantMaledictusPhase.SHADOW;
                    n.forceChat(phases.getShout());
                    CycleEventHandler.getSingleton().addEvent(n, new CycleEvent() {
                        int ticks = 0;

                        @Override
                        public void execute(CycleEventContainer container) {
                            switch (ticks++) {
                                case 4: {
                                 //   n.forceChat("I hope you get smoked noob!");
                                }
                                case 6: {
                                    PlayerHandler.nonNullStream().filter(p -> Boundary.isIn(p, Boundary.REVENANT_MALEDICTUS))
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
                    phases = RevenantMaledictusPhase.ZAROS;
                    n.forceChat(phases.getShout());

                    if (!revenantmaledictusHealed) {
                        n.appendHeal(500, Hitmark.HEAL_PURPLE);
                        revenantmaledictusHealed = true;
                    }
                    break;
            }
        }
    }
}
