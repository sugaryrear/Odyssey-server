package io.Odyssey.content.randomevents;

import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.npc.NPC;
import io.Odyssey.model.entity.npc.NPCSpawning;
import io.Odyssey.model.entity.player.Player;

public class Genie {

    Player player;

    public Genie(Player player) {
        this.player = player;
    }
    public  void spawnGenie() {

        int offsetX = 0;
        int offsetY = 0;
        if (player.getRegionProvider().getClipping(player.getX() - 1, player.getY(), player.heightLevel, -1, 0)) {
            offsetX = -1;
        } else if (player.getRegionProvider().getClipping(player.getX() + 1, player.getY(), player.heightLevel, 1, 0)) {
            offsetX = 1;
        } else if (player.getRegionProvider().getClipping(player.getX(), player.getY() - 1, player.heightLevel, 0, -1)) {
            offsetY = -1;
        } else if (player.getRegionProvider().getClipping(player.getX(), player.getY() + 1, player.heightLevel, 0, 1)) {
            offsetY = 1;
        }

     //   PlayerSave.saveGame(player);
     //   player.genieEvent = true;
//        Server.npcHandler.spawnNpcRandomEvent(player, 326, player.absX + offsetX, player.absY + offsetY,
//                player.heightLevel, 0, 0, 0, 0, 0, true, false, true);
        NPCSpawning.spawnNpcRandomEvent(player, 326,  player.absX + offsetX, player.absY + offsetY, player.getHeightLevel(), 1, 0, 0,
                0,0);



    }
    public void pickupgenie(NPC npc) {
         npc.startAnimation(863);
        npc.gfx0(86);
        npc.forceChat("cya!");
        player.getItems().addItemUnderAnyCircumstance(2528,1);

        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                if (player.disconnected) {
                    container.stop();
                    return;
                }

                //var npc = NPCHandler.npcs[player.clickedNpcIndex];
                npc.unregister();
                npc.processDeregistration();
//                for (int i = 0; i < NPCHandler.npcs.length; i++) {
//                    if (NPCHandler.npcs[i] != null) {
//
//                        if (NPCHandler.isSpawnedBy(player, NPCHandler.npcs[i]) && NPCHandler.npcs[i].npcType == 326) {
//                            NPCHandler.npcs[i].absX = 0;
//                            NPCHandler.npcs[i].absY =0;
//                            NPCHandler.npcs[i] = null;
//
//                        }
//                    }
//                }
                container.stop();
            }
            @Override
            public void onStopped() {

            }
        }, 3);

    }
}
