package io.Odyssey.content.bosses.nex;

import io.Odyssey.Server;
import io.Odyssey.content.bosses.nex.NexNPC;
import io.Odyssey.content.bosses.bryophyta.BryophytaNPC;
import io.Odyssey.content.commands.owner.Npc;
import io.Odyssey.content.instances.*;
import io.Odyssey.model.Npcs;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.collisionmap.WorldObject;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.player.*;
import io.Odyssey.model.world.objects.GlobalObject;

public class Nex extends InstancedArea {

    public static final int  KEY = 26_356;

    private static final InstanceConfiguration CONFIGURATION = new InstanceConfigurationBuilder()
            .setCloseOnPlayersEmpty(true)
            .setRespawnNpcs(false)
            .createInstanceConfiguration();

    public Nex() {
        super(CONFIGURATION, Boundary.NEX);
    }

    public void enter(Player player) {
        try {
            add(player);
            player.moveTo(new Position(player.absX, player.absY, getHeight()));
           NexNPC nex =  new NexNPC(11278, new Position(2925, 5203, getHeight()));
            this.add(nex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDispose() {
        getPlayers().forEach(this::remove);
    }


}

//package io.Odyssey.content.bosses.nex;
//
//import io.Odyssey.content.instances.InstanceConfiguration;
//import io.Odyssey.content.instances.impl.LegacySoloPlayerInstance;
//import io.Odyssey.model.entity.npc.NPC;
//import io.Odyssey.model.entity.player.Boundary;
//import io.Odyssey.model.entity.player.Player;
//import io.Odyssey.model.entity.player.Position;
//
///**
// *  handles instanced version of Nex
// */
//public class Nex extends LegacySoloPlayerInstance {
//
//    public static final int  KEY = 26_356;
//
//
//    private Player player;
//    public Nex(Player player, Boundary boundary) {
//        super(InstanceConfiguration.CLOSE_ON_EMPTY_RESPAWN, player, boundary);
//        this.player = player;
//    }
//
//    public void enter() {
//        try {
//            add(player);
//            player.getPA().movePlayer(player.absX, player.absY, getHeight());
//
//            NPC npc = new NexNPC(NexNPC.NEX, new Position(2925,5203, getHeight()));
//            add(npc);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
////
////    @Override
////    public boolean handleClickObject(Player player, WorldObject object, int option) {
////        switch (object.getId()) {
////            case 32535://TODO
////                /**
////                 * Leave
////                 */
////
////                this.dispose();
////                player.getPA().movePlayer(3174, 9900, 0);
////                player.sendMessage("Cautiously, you climb out of the damp cave.");
////                return true;
////        }
////        return false;
////    }
//
////    @Override
////    public void onDispose() {
////        if (npc != null) {
////            System.out.println("disposed.");
////            if (npc.isDead())
////                return;
////            npc.unregister();
////        }
////        if (player != null && player.getInstance() == this) {
////            player.removeFromInstance();
////        }
////        player = null;
////    }
////@Override
////public void onDispose() {
////    getPlayers().stream().forEach(plr -> {
////        remove(plr);
////    });
////}
//}