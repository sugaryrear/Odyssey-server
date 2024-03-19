package io.Odyssey.content.minigames.inferno;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import io.Odyssey.Server;
import io.Odyssey.content.dialogue.DialogueBuilder;
import io.Odyssey.content.dialogue.DialogueExpression;
import io.Odyssey.content.dialogue.DialogueOption;
import io.Odyssey.content.minigames.rfd.DisposeTypes;
import io.Odyssey.content.minigames.tob.TobConstants;
import io.Odyssey.content.minigames.tob.TobRoom;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.definitions.NpcDef;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCHandler;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;
import io.Odyssey.model.world.objects.GlobalObject;
import io.Odyssey.util.Misc;
import io.Odyssey.util.logging.player.DiedAtTobLog;

import static io.Odyssey.model.Items.FIRE_CAPE;
import static io.Odyssey.model.Items.LIGHTHOUSE_KEY;

public class Inferno extends Tzkalzuk {

    private static final int DEFAULT_WAVE = 0;
public static void startinfernowalkingto(Player player, int wave){
    player.getPA().closeAllWindows();
if(!player.hasSacrificedFcape){
    player.sendMessage("You need to sacrifice a @blu@Fire cape@bla@ to start the Inferno.");
    return;
}
    if (!player.getItems().playerHasItem(995, 1000000)) {
        player.sendMessage("You need to pay 1M gold to start the Inferno from the last wave.");
        return;
    }
    if(wave == 68)
        player.getItems().deleteItem(995,1000000);


        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {


            @Override
            public void execute(CycleEventContainer container) {
                int cycle = container.getTotalTicks();
// startInferno(player,0);

                if (cycle == 1) {
                    player.setForceMovement(2496, 5126, 0, 85, "NORTH", 6723);
                    player.getAgilityHandler().stopEmote(player);
                } else if (cycle == 2) {

                } else if (cycle == 3) {

                } else if (cycle == 4) {
                    player.getPA().sendScreenFade("", 1, 7);
                } else if (cycle == 8) {

                    //   player.getPA().sendScreenFade("Inferno loading...", -1, 5);

                } else if (cycle == 9) {
                    // player.facePosition(player.absX, player.absY+2);
                    startInferno(player, wave);
                    container.stop();
                } else if (cycle == 10) {

                } else if (cycle == 11) {

                } else if (cycle == 17) {

                    //   player.sendMessage("error");
                } else if (cycle >= 21) {
                    // player.getPA().resetCamera();

                    //	player.sendMessage("Stop");
                    //   container.stop();
                }
                //    container.stop();
            }

        }, 1);
    }



    public static void startInferno(Player player, int wave) {
        new Inferno(player, Boundary.INFERNO).create(wave);
    }

    public static void moveToExit(Player player) {
        player.setTeleportToX(EXIT.getX());
        player.setTeleportToY(EXIT.getY());
        player.heightLevel = 0;
    }

    public static int getDefaultWave() {
        return DEFAULT_WAVE;
    }

    public static void reset(Player player) {
        Inferno inferno = player.getInferno();
        if (inferno != null) {
            inferno.remove(player);
        }
    }

    public static void gamble(Player player) {
        if (!player.getItems().playerHasItem(21295)) {
            player.sendMessage("You do not have an infernal cape.");
            return;
        }

        player.getItems().deleteItem(21295, 1);

        if (Misc.random(25) == 0) {
            PlayerHandler.executeGlobalMessage("[@red@PET@bla@] @cr20@<col=255> " + player.getDisplayName() + "</col> received a pet from <col=255>TzKal-Zuk</col>.");
            player.getItems().addItemUnderAnyCircumstance(21291, 1);
            player.getDH().sendDialogues(74, 7690);
        } else {
            player.getDH().sendDialogues(73, 7690);
            return;
        }
    }

    private int infernoWaveId = 50;
    private int infernoWaveType = 50;
    private int killsRemaining;
    boolean started;

    private final Walls walls = new Walls();

    public Inferno(Player player, Boundary boundary) {
        super(player, boundary);
    }

    public void create(int wave) {
        player.getPA().removeAllWindows();
        player.getPA().movePlayer(2271, 5329, getHeight());
        add(player);
        setInfernoWaveType(wave);
        setInfernoWaveId(wave);
        walls.setDefaultWallAttributes();
        player.infernoLeaveTimer = System.currentTimeMillis();
        walls.createWalls(player, this);
        spawn();
        player.sendMessage("Welcome to the Inferno. Your first wave will start soon. Please wait...", 255);
    }

    public void leaveGame() {
        killAllSpawns();
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, getHeight()).setInstance(this));
        Server.getGlobalObjects().remove(30354, 2270, 5333, getHeight(), this);//it wont remove it doesnt exist lol
        Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, getHeight()).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2259, 5349, getHeight(), this);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, getHeight()).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2278, 5349, getHeight(), this);
        walls.killWalls();
        player.sendMessage("You have left the Inferno minigame.");
        player.getPA().movePlayer(2497, 5116, 0);
        setInfernoWaveType(50);
        setInfernoWaveId(50);
        walls.resetWallAttributes();
        dispose();
    }

    public void stop() {
        player.getItems().addItemUnderAnyCircumstance(TOKKUL, (10 * getInfernoWaveType()) + Misc.random(50));
        player.getPA().movePlayer(2497, 5116, 0);
        player.getDH().sendStatement("Congratulations for finishing the Inferno!");
        player.waveInfo[getInfernoWaveType() - 1] += 1;
        reset(player);
        player.nextChat = 0;
        player.setRunEnergy(10000, true);
        killAllSpawns();
        player.getInferno().zukDead = false;
    }

    @Override
    public boolean handleDeath(Player player) {
        int wave = getInfernoWaveId() + 1;
        player.getPA().movePlayer(2497, 5116, 0);
        player.getDH().sendStatement("Unfortunately you died on wave " + wave + ". Better luck next time.");
        player.nextChat = 0;
        reset(player);
        killAllSpawns();
        return true;
    }

//    public void handleDeath() {
//        int wave = getInfernoWaveId() + 1;
//        player.getPA().movePlayer(2497, 5116, 0);
//        player.getDH().sendStatement("Unfortunately you died on wave " + wave + ". Better luck next time.");
//        player.nextChat = 0;
//        reset(player);
//        killAllSpawns();
//    }

    /**
     * Disposes of the content by moving the player and finalizing and or removing any left over content.
     *
     * @param dispose the type of dispose
     */
    public final void end(DisposeTypes dispose) {
        if (player == null) {
            return;
        }

        if (dispose == DisposeTypes.COMPLETE) {
            NPCHandler.kill(InfernoWaveData.TZKAL_ZUK, getHeight());
            NPCHandler.kill(InfernoWaveData.ANCESTRAL_GLYPH, getHeight());
            for (NPC kill : player.getInferno().kill) {
                if (kill != null) {
                    kill.setDead(true);
                }
            }
            player.getItems().addItemUnderAnyCircumstance(TOKKUL, (10000 * getInfernoWaveType()) + Misc.random(5000));
            player.getItems().addItemUnderAnyCircumstance(21295, 1);
            PlayerHandler.executeGlobalMessage("@cr10@@red@" + player.getDisplayName() + " has defeated the Inferno.");

            player.nextChat = 0;
            player.setRunEnergy(10000, true);
            player.getInferno().zukDead = true;
        } else if (dispose == DisposeTypes.INCOMPLETE) {
            NPCHandler.kill(InfernoWaveData.TZKAL_ZUK, getHeight());
            NPCHandler.kill(InfernoWaveData.ANCESTRAL_GLYPH, getHeight());
            if (player.getInferno() != null) {
                for (NPC kill : player.getInferno().kill) {
                    if (kill != null) {
                        kill.setDead(true);
                    }
                }
            }
        }

        reset(player);
        moveToExit(player);
    }
public static void tzhaarketkehdialogue(Player player) {
    Consumer<Player> startinferno = p -> {

        p.getPA().closeAllWindows();

    };
    Consumer<Player> nevermind = p -> {

        p.getPA().closeAllWindows();

    };
    Consumer<Player> sacrficefirecape = p -> {

    if(p.getItems().playerHasItem(FIRE_CAPE)){
        p.getPA().closeAllWindows();
        p.getItems().deleteItem(FIRE_CAPE,1);
        p.hasSacrificedFcape =true;
        player.start(gettzhaarketkehdialogue(p)
                        .statement("You hand over your cape to TzHaar-Ket-Keh.")
                .npc(DialogueExpression.ANGER_1, "Give it your best shot JalYt-Ket-Xo-"+p.getDisplayName())

        );
    } else {
        p.sendMessage("You need a @blu@Fire cape@bla@ to sacrifice.");
    }

    };
    Consumer<Player> godownthere = p -> {

        if(p.hasSacrificedFcape){
            p.start(gettzhaarketkehdialogue(p).npc(DialogueExpression.ANGER_1, "Sure let me know when you're ready!")
                            .option(
            new DialogueOption("Start Inferno (wave 0)",startinferno),
                    new DialogueOption("Cancel", nevermind))

            );

        } else {
            p.start(gettzhaarketkehdialogue(p).npc(DialogueExpression.ANGER_1, "Hmm...maybe for lets say..","a @blu@Fire cape@bla@?")
                    .option(
                            new DialogueOption("Yes, I'd like to sacrifice it.",sacrficefirecape),
                            new DialogueOption("No, I'd like to keep it.", nevermind))

            );

        }

    };

    player.start(gettzhaarketkehdialogue(player)
            .player("Hello Wow what has happened here?", "That doesn't look good!.")
            .npc(DialogueExpression.ANGER_1, "We pushed it too far!","JalYt would not understand. The memories, we needed them.","Now big Inferno.")
            .option(
                    new DialogueOption("Can I go down there?",godownthere),
                    new DialogueOption("Nevermind", nevermind))


    );
}
    private static DialogueBuilder gettzhaarketkehdialogue(Player player) {
        DialogueBuilder builder = new DialogueBuilder(player);
        builder.setNpcId(Npcs.TZHAAR_KET_KEH);
        return builder;
    }

    @Override
    public void onDispose() {
        end(DisposeTypes.INCOMPLETE);
    }

    public void spawn() {
        final int[][] type = InfernoWaveData.LEVEL;
        if (getInfernoWaveId() >= type.length && getInfernoWaveType() > 0 && Boundary.isIn(player, Boundary.INFERNO)) {
            if (player.getInferno().zukDead)
                stop();
            return;
        }

        final Inferno instance = this;
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer event) {
                if (player == null) {
                    event.stop();
                    return;
                }
                if (!Boundary.isIn(player, Boundary.INFERNO)) {
                    reset(player);
                    event.stop();
                    return;
                }
                if (getInfernoWaveId() >= type.length && getInfernoWaveType() > 0) {
                    onStopped();
                    event.stop();
                    return;
                }
                if (getInfernoWaveId() != 66 && getInfernoWaveId() != 67 && getInfernoWaveId() != 68) {
                    walls.createWalls(player, instance);
                }
                started = true;

                if (getInfernoWaveId() != 0 && getInfernoWaveId() < type.length)
                    player.sendMessage("@red@You are now on wave " + (getInfernoWaveId() + 1) + " of " + type.length + ".", 255);
                if (getInfernoWaveId() == 68) {
                    initiateTzkalzuk();
                    //so IF it exists do this:

// Optional<WorldObject> object_real = player.getRegionProvider().get(x, y).getWorldObject(id, x, y, z);
//        GlobalObject theobject = new GlobalObject( object_real.get().id, object_real.get().x, object_real.get().y, object_real.get().height, object_real.get().face, 0);
//        Server.getGlobalObjects().add_door(theobject);

                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, getHeight(), instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, getHeight(), instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2278, 5349, getHeight(), instance);
                    walls.killWalls();
                }
                if (getInfernoWaveId() == 66) {
                    setKillsRemaining(1);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, getHeight(), instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, getHeight(), instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2278, 5349, getHeight(), instance);
                    walls.killWalls();
                    NPC jad = NPCSpawning.spawnNpcOld(player, InfernoWaveData.JALTOK_JAD, 2271 + Misc.random(-4, 4), 5342 + Misc.random(-4, 4), getHeight(), 1, InfernoWaveData.getHp(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getMax(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getAtk(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getDef(InfernoWaveData.JALTOK_JAD), false, false);
                    jad.setInstance(Inferno.this);
                    JadCombat.start(jad, player);
                }
                if (getInfernoWaveId() == 67) {
                    setKillsRemaining(3);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30354, 2270, 5333, getHeight(), instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2259, 5349, getHeight(), instance);
                    Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, getHeight()).setInstance(instance));
                    Server.getGlobalObjects().remove(30355, 2278, 5349, getHeight(), instance);
                    walls.killWalls();
                    CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
                        NPC jad1;
                        NPC jad2;
                        NPC jad3;
                        int ticks;

                        @Override
                        public void execute(CycleEventContainer container) {
                            if (ticks == 0) {
                                jad1 = NPCSpawning.spawnNpcOld(player, InfernoWaveData.JALTOK_JAD, 2263, 5341, getHeight(), 1, InfernoWaveData.getHp(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getMax(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getAtk(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getDef(InfernoWaveData.JALTOK_JAD), false, false);
                                jad1.setInstance(player.getInstance());
                                JadCombat.start(jad1, player);
                            }
                            if (ticks == 2) {
                                jad2 = NPCSpawning.spawnNpcOld(player, InfernoWaveData.JALTOK_JAD, 2269, 5347, getHeight(), 1, InfernoWaveData.getHp(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getMax(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getAtk(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getDef(InfernoWaveData.JALTOK_JAD), false, false);
                                jad2.setInstance(player.getInstance());
                                JadCombat.start(jad2, player);
                            }
                            if (ticks == 4) {
                                jad3 = NPCSpawning.spawnNpcOld(player, InfernoWaveData.JALTOK_JAD, 2276, 5341, getHeight(), 1, InfernoWaveData.getHp(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getMax(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getAtk(InfernoWaveData.JALTOK_JAD), InfernoWaveData.getDef(InfernoWaveData.JALTOK_JAD), false, false);
                                jad3.setInstance(player.getInstance());
                                JadCombat.start(jad3, player);
                                container.stop();
                            }
                            ticks++;
                        }
                    }, 1);
                }

                if (getInfernoWaveId() < 66) {
                    setKillsRemaining(type[getInfernoWaveId()].length + 3);
                }

                if (getInfernoWaveId() < 66) {
                    for (int i = 0; i < getKillsRemaining() - 3; i++) {
                        final int npcType = type[getInfernoWaveId()][i];
                        int npcSize = NpcDef.forId(npcType).getSize();
                        int startX = 2271;
                        int startY = 5342;
                        do {
                            startX += Misc.random(-5, 5);
                            startY += Misc.random(-5, 5);
                        } while(player.getRegionProvider().isOccupiedByNpc(npcSize, startX, startY, player.getHeight()));
                        final int hp = InfernoWaveData.getHp(npcType);
                        final int maxhit = InfernoWaveData.getMax(npcType);
                        final int atk = InfernoWaveData.getAtk(npcType);
                        final int def = InfernoWaveData.getDef(npcType);
                        NPC spawn = NPCSpawning.spawnNpcOld(player, npcType, startX, startY, getHeight(), 1, hp, maxhit, atk, def, true, false);
                        spawn.setInstance(player.getInstance());
                    }
                }
                event.stop();
            }

            @Override
            public void onStopped() {
            }
        }, 16);

        if (getInfernoWaveId() < 66) {
            walls.walls(player, this, type);
        }
    }

    public void killAllSpawns() {
        for (int i = 0; i < NPCHandler.npcs.length; i++) {
            NPC npc = NPCHandler.npcs[i];
            if (npc != null) {
                if (NPCHandler.isInfernoNpc(npc)) {
                    if (NPCHandler.isSpawnedBy(player, npc)) {
                        npc.unregister();
                    }
                }
            }
        }
        Server.getGlobalObjects().add(new GlobalObject(-1, 2270, 5333, getHeight()).setInstance(this));
        Server.getGlobalObjects().remove(30354, 2270, 5333, getHeight(), this);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2259, 5349, getHeight()).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2259, 5349, getHeight(), this);
        Server.getGlobalObjects().add(new GlobalObject(-1, 2278, 5349, getHeight()).setInstance(this));
        Server.getGlobalObjects().remove(30355, 2278, 5349, getHeight(), this);
        walls.killWalls();
        walls.killNibs();
    }



    public int getKillsRemaining() {
        return killsRemaining;
    }

    public void setKillsRemaining(int remaining) {
        this.killsRemaining = remaining;
    }

    public int getInfernoWaveId() {
        return infernoWaveId;
    }

    public void setInfernoWaveId(int infernoWaveId) {
        this.infernoWaveId = infernoWaveId;
    }

    public int getInfernoWaveType() {
        return infernoWaveType;
    }

    public void setInfernoWaveType(int infernoWaveType) {
        this.infernoWaveType = infernoWaveType;
    }
}