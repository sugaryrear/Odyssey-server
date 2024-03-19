package io.Odyssey.model.entity.npc;


import io.Odyssey.Server;
import io.Odyssey.content.bosses.NexAngelOfDeath;
import io.Odyssey.content.bosses.NexAngelOfDeathWE;
import io.Odyssey.content.instances.InstancedArea;
import io.Odyssey.model.CombatType;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.cycleevent.impl.HealingEverySecsEvent;
import io.Odyssey.model.definitions.NpcDef;
import io.Odyssey.model.definitions.NpcStats;
import io.Odyssey.model.entity.npc.pets.PetHandler;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NPCSpawning {

    private static final Logger logger = LoggerFactory.getLogger(NPCSpawning.class);

    private static void log(NPC npc) {
        logger.debug("Spawned {}.", npc);
    }

    public static NpcStats getStats(int hp, int attack, int defence) {
        return NpcStats.builder().setHitpoints(hp).setAttackLevel(attack).setRangeLevel(attack).setMagicLevel(attack).setDefenceLevel(defence).createNpcStats();
    }

    public static NPC spawnNpcOld(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence, boolean attackPlayer, boolean headIcon) {
        return spawnNpc(c, npcType, x, y, heightLevel, WalkingType, maxHit, attackPlayer, headIcon, getStats(HP, attack, defence));
    }

    public static NPC spawnNpcOld(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack, int defence) {
        return spawnNpc(npcType, x, y, heightLevel, WalkingType, maxHit, getStats(HP, attack, defence));
    }

    public static NPC spawnNpc(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit, boolean attackPlayer, boolean headIcon) {
        return spawnNpc(c, npcType, x, y, heightLevel, WalkingType, maxHit, attackPlayer, headIcon, null);
    }
    public static NPC spawnNpcRandomEvent(Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
                                    int attack, int defence) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < NPCHandler.maxNPCs; i++) {
            if (NPCHandler.npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc3");
            return null;
        }
        NpcDef definition = NpcDef.forId(npcType);
        NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.walkingType = 0;
       // newNPC.getHealth().setMaximum(HP);
        newNPC.getHealth().reset();
        newNPC.maxHit = maxHit;
       // newNPC.attack = attack;
       // newNPC.defence = defence;
        newNPC.spawnedBy = c.getIndex();
        newNPC.underAttack = true;
        newNPC.facePlayer(c.getIndex());
        newNPC.gfx0(86);
     //   newNPC.set
        CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (c.disconnected) {
                    container.stop();
                    return;
                }
                newNPC.forceChat("Hello "+c.getDisplayName()+"!");
                container.stop();
            }
            @Override
            public void onStopped() {

            }
        }, 2);

      //  if (summonFollow) {
            newNPC.summoner = true;
            newNPC.summonedBy = c.getIndex();
            c.hasFollower = true;
      // }
     //   if (attackPlayer) {
            newNPC.underAttack = true;
            newNPC.setPlayerAttackingIndex(c.getIndex());
       // }
        NPCHandler.npcs[slot] = newNPC;
        return newNPC;
    }
    /**
     * Summon npc, barrows, etc
     */
    public static NPC spawnNpc(final Player c, int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit, boolean attackPlayer, boolean headIcon, NpcStats npcStats) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < NPCHandler.maxNPCs; i++) {
            if (NPCHandler.npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.err.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc");
            return null;
        }
        NpcDef definition = NpcDef.forId(npcType);
        NPC newNPC;
        if (npcStats == null) {
            newNPC = new NPC(slot, npcType, definition);
        } else {
            newNPC = new NPC(slot, npcType, definition, npcStats);
        }
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.getSize();
        newNPC.heightLevel = heightLevel;

        newNPC.walkingType = WalkingType;
        newNPC.maxHit = maxHit;
        newNPC.spawnedBy = c.getIndex();
       // newNPC.setAttackType(CombatType.MELEE);
        if (c.getInstance() != null) {
            c.getInstance().add(newNPC);
        }
        newNPC.facePosition(x,y+1);//face north when spawned
        newNPC.getRegionProvider().addNpcClipping(newNPC);
        if (headIcon)
            c.getPA().drawHeadicon(1, slot);
        if (attackPlayer) {
            newNPC.underAttack = true;
            newNPC.setPlayerAttackingIndex(c.getIndex());
            c.underAttackByPlayer = slot;
            c.underAttackByNpc = slot;
        }
        NPCHandler.npcs[slot] = newNPC;
        if (newNPC.getNpcId() == 1605) {
            newNPC.forceChat("You must prove yourself... now!");
            newNPC.gfx100(86);
        }
        if (newNPC.getNpcId() == 1606) {
            newNPC.forceChat("This is only the beginning, you can\'t beat me!");
            newNPC.gfx100(86);
        }
        if (newNPC.getNpcId() == 1607) {
            newNPC.forceChat("Foolish mortal, I am unstoppable.");
        }
        if (newNPC.getNpcId() == 1608) {
            newNPC.forceChat("Now you feel it... The dark energy.");
        }
        if (newNPC.getNpcId() == 1609) {
            newNPC.forceChat("Aaaaaaaarrgghhhh! The power!");
        }


        if(newNPC.getNpcId() >= 2450 && newNPC.getNpcId() <= 2456)
            newNPC.forceChat("I'm ALIVE!");
       // log(newNPC);
        return newNPC;
    }

    public static NPC spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit) {
        return spawnNpc(npcType, x, y, heightLevel, WalkingType, maxHit, null);
    }

    public static NPC spawnNpc(int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit, NpcStats npcStats) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < NPCHandler.maxNPCs; i++) {
            if (NPCHandler.npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
            return null;
        }
        NpcDef definition = NpcDef.forId(npcType);
        NPC newNPC;
        if (npcStats == null) {
            newNPC = new NPC(slot, npcType, definition);
        } else {
            newNPC = new NPC(slot, npcType, definition, npcStats);
        }
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.getRegionProvider().addNpcClipping(newNPC);
        newNPC.walkingType = WalkingType;
        newNPC.maxHit = maxHit;
        NPCHandler.npcs[slot] = newNPC;
        log(newNPC);
        return newNPC;
    }

    public static NPC spawn(int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit, boolean attackPlayer) {
        return spawn(npcType, x, y, heightLevel, WalkingType, maxHit, attackPlayer, null);
    }

    public static void spawn(NPC npc) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < NPCHandler.maxNPCs; i++) {
            if (NPCHandler.npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
            return;
        }

        NpcDef definition = NpcDef.forId(npc.getNpcId());
        NPCHandler.npcs[slot] = npc;
        log(npc);
    }

    public static NPC spawn(int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit, boolean attackPlayer, NpcStats npcStats) {
        // first, search for a free slot
        int slot = -1;
        for (int i = 1; i < NPCHandler.maxNPCs; i++) {
            if (NPCHandler.npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
            return null;
        }
        NpcDef definition = NpcDef.forId(npcType);
        NPC newNPC;
        if (npcStats != null) {
            newNPC = new NPC(slot, npcType, definition, npcStats);
        } else {
            newNPC = new NPC(slot, npcType, definition);
        }
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.getRegionProvider().addNpcClipping(newNPC);
        newNPC.walkingType = WalkingType;
        newNPC.maxHit = maxHit;
        NPCHandler.npcs[slot] = newNPC;
        log(newNPC);
        return newNPC;
    }

    public static void spawnPet(Player player, int npcId, int x, int y, int z, int maxHit, boolean attackPlayer, boolean headIcon, boolean summonFollow) {
        int slot = -1;
        for (int i = 1; i < NPCHandler.maxNPCs; i++) {
            if (NPCHandler.npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc3");
            return;
        }
        NpcDef definition = NpcDef.forId(npcId);
        NPC newNPC = new NPC(slot, npcId, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = z;
        newNPC.walkingType = 0;
        newNPC.maxHit = maxHit;
        newNPC.spawnedBy = player.getIndex();
        newNPC.underAttack = true;
        newNPC.isPet = true;
        newNPC.facePlayer(player.getIndex());
        if (headIcon)
            player.getPA().drawHeadicon(1, slot);
        if (summonFollow) {
            newNPC.summoner = true;
            newNPC.summonedBy = player.getIndex();
            player.hasFollower = true;
        }
        if (attackPlayer) {
            newNPC.underAttack = true;
            newNPC.setPlayerAttackingIndex(player.getIndex());
        }
        NPCHandler.npcs[slot] = newNPC;

    }
//public static void checkforhealingpet(Player player, int npcid){
//    if (!PetHandler.hasHealingPet(player)){
//     //   System.out.println("not spawning a healing pet");
//        return;
//    }
//    int ticks = 10; // default i guess
//
////    switch(player.petSummonId){
////        case 6633://zilly
////            ticks = 5;
////            break;
////    }
//    if(Server.getEventHandler().isRunning(player, "healing_pets_event")){
//        Server.getEventHandler().stop(player, "healing_pets_event");
//
//    }
//        //should heal task ?
////System.out.println("headk pet");
//    Server.getEventHandler().submit(new HealingEverySecsEvent(player, ticks));
//}
//


    /**
     * Spawn a new npc on the world
     *  @param npcType
     *            the npcType were spawning
     * @param x
     *            the x coordinate were spawning on
     * @param y
     *            the y coordinate were spawning on
     * @param heightLevel
     *            the heightLevel were spawning on
     * @param WalkingType
     *            the WalkingType were setting
     * @param maxHit
     *            the maxHit were setting
     */
    public static NPC newNPC(int npcIndex, int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit) {
        NpcDef definition = NpcDef.forId(npcType);
        NPC newNPC = new NPC(npcIndex, npcType, definition);
        if (npcType == 11282) {
            new NexAngelOfDeath(npcType, new Position(x, y, heightLevel));
        }


        newNPC.absX = x;
        newNPC.absY = y;  // what does that mean ? whats it mean ?
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
    //    System.out.println("x; "+x+"height: "+heightLevel);
        newNPC.getRegionProvider().addNpcClipping(newNPC);
        newNPC.walkingType = WalkingType;
        newNPC.maxHit = maxHit;
        newNPC.resetDamageTaken();
        NPCHandler.npcs[npcIndex] = newNPC;
        log(newNPC);
        return newNPC;
    }

    public static void finishNpcConstruction(NPC npc, int WalkingType, int maxHit) {
        npc.walkingType = WalkingType;
        npc.maxHit = maxHit;
        npc.resetDamageTaken();
    }

    public static NPC spawnNpc(InstancedArea instance, int npcType, int x, int y, int heightLevel, int WalkingType, int maxHit) {
        int slot = -1;
        for (int i = 1; i < NPCHandler.maxNPCs; i++) {
            if (NPCHandler.npcs[i] == null) {
                slot = i;
                break;
            }
        }
        if (slot == -1) {
            System.out.println("Cannot find any available slots to spawn npc into + npchandler @spawnNpc - line 2287");
            return null;
        }
        NpcDef definition = NpcDef.forId(npcType);
        NPC newNPC = new NPC(slot, npcType, definition);
        newNPC.absX = x;
        newNPC.absY = y;
        newNPC.makeX = x;
        newNPC.makeY = y;
        newNPC.heightLevel = heightLevel;
        newNPC.getRegionProvider().addNpcClipping(newNPC);
        newNPC.walkingType = WalkingType;
        newNPC.maxHit = maxHit;
        if (instance != null) {
            instance.add(newNPC);
        }
        NPCHandler.npcs[slot] = newNPC;
        log(newNPC);
        return newNPC;
    }
}
