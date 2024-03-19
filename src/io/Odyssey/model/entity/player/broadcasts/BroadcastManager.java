package io.Odyssey.model.entity.player.broadcasts;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.PlayerHandler;

import java.util.Objects;

public class BroadcastManager {

    public static Broadcast[] broadcasts = new Broadcast[1000];

    public static void removeBroadcast(int index) {
        if (broadcasts[index] != null) {
            broadcasts[index] = null;
        }
    }

    public static void addIndex(Broadcast broadcast) {
        int index = getFreeIndex();

        if (index == -1) {
            System.err.println("Error adding index.. broadcast list is full!");
            return;
        }

        broadcast.setIndex(index);

        broadcasts[index] = broadcast;

        PlayerHandler.getPlayers().stream().filter(Objects::nonNull).forEach(p -> {
            if(p.notification_broadcast){
                if (broadcast.sendChatMessage)
                    p.sendMessage(broadcast.getMessage());
                p.getPA().sendBroadCast(broadcasts[index]);
            }


        });
    }
    public static void addIndex_justforme(Broadcast broadcast, Player player) {
        int index = getFreeIndex();

        if (index == -1) {
            System.err.println("Error adding index.. broadcast list is full!");
            return;
        }

        broadcast.setIndex(index);

        broadcasts[index] = broadcast;

        //PlayerHandler.getPlayers().stream().filter(Objects::nonNull).forEach(p -> {
            if(!player.notification_broadcast)
                return;
            if (broadcast.sendChatMessage)
                player.sendMessage(broadcast.getMessage());
            player.getPA().sendBroadCast(broadcasts[index]);
       // });
    }
    public static int getFreeIndex() {
        for (int i = 0; i < broadcasts.length; i++) {
            Broadcast broadcast = broadcasts[i];
            if (broadcast == null)
                return i;
        }
        return -1;
    }
}
