package io.Odyssey.model.lobby.impl;

import com.google.common.collect.Lists;
import io.Odyssey.Server;
//import io.Odyssey.content.minigames.TombsOfAmascut.TombsOfAmascut;
import io.Odyssey.model.cycleevent.CycleEvent;
import io.Odyssey.model.cycleevent.CycleEventContainer;
import io.Odyssey.model.cycleevent.CycleEventHandler;
import io.Odyssey.model.entity.player.Boundary;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Right;
import io.Odyssey.model.lobby.Lobby;
import io.Odyssey.util.Misc;

import java.util.List;

public class TombsOfAmascutLobby extends Lobby {

    public TombsOfAmascutLobby() {
    }

    @Override
    public void onJoin(Player player) {


        player.getPA().movePlayer((3551+ Misc.random(-3,3)), (5146+Misc.random(-3,3)));
        player.sendMessage("You are now in the lobby for TombsOfAmascut  .");
        String timeLeftString = formattedTimeLeft();
        player.sendMessage("TombsOfAmascut starts in: " + timeLeftString);
    }

    @Override
    public void onLeave(Player player) {
        player.getPA().movePlayer(3059, 9947, 0);
        player.sendMessage("@red@You have left the TombsOfAmascut.");
    }

    @Override
    public boolean canJoin(Player player) {
//        if (player.totalLevel < player.getMode().getTotalLevelNeededForTombsOfAmascut()) {
//            player.sendMessage("You need a total level of at least " + player.getMode().getTotalLevelNeededForTombsOfAmascut() + " to join this TombsOfAmascut!");
//            return false;
//        }
        if (player.getRights().isOrInherits(Right.OWNER)) {
            return true;
        }
        // Don't allow multi-logging outside of debug
        if (!Server.isDebug()) {
            boolean accountInLobby = getFilteredPlayers()
                    .stream()
                    .anyMatch(lobbyPlr -> lobbyPlr.getMacAddress().equalsIgnoreCase(player.getMacAddress()));
            boolean ipInLobby = getFilteredPlayers()
                    .stream()
                    .anyMatch(lobbyPlr -> lobbyPlr.getIpAddress().equalsIgnoreCase(player.getIpAddress()));
            if (accountInLobby || ipInLobby) {
                player.sendMessage("You already have an account in the lobby!");
                return false;
            }
        }

        return true;
    }

    @Override
    public long waitTime() {
        return Server.isDebug() ? 1000 : 5000;
    }

    @Override
    public int capacity() {
        return 22;
    }

    @Override
    public String lobbyFullMessage() {
        return "Chambers of TombsOfAmascut is currently full!";
    }

    @Override
    public boolean shouldResetTimer() {
        return this.getWaitingPlayers().isEmpty();
    }

    @Override
    public void onTimerFinished(List<Player> lobbyPlayers) {
        CycleEventHandler.getSingleton().addEvent(0, new Object(), new CycleEvent() {
            @Override
            public void execute(CycleEventContainer container) {
                container.stop();
                List<Player> tombsOfAmascutPlayers;
//				if (Server.getConfiguration().getServerState() != ServerState.DEBUG) {
//					Map<String, Player> macFilter = Maps.newConcurrentMap();
//					lobbyPlayers.forEach(plr -> macFilter.put(plr.getMacAddress(), plr));
//					raidPlayers = Lists.newArrayList(macFilter.values());
//					lobbyPlayers.stream().filter(plr -> !macFilter.values().contains(plr)).forEach(plr -> {
//						plr.sendMessage("You had a different account in this lobby, you will be added to the next one");
//						onJoin(plr);
//					});
//				} else {
                tombsOfAmascutPlayers = Lists.newArrayList(lobbyPlayers);
                //}

//                new TombsOfAmascut().startTombsOfAmascut(tombsOfAmascutPlayers, false);
            }
        }, 1);
    }
    @Override
    public void onTimerUpdate(Player player) {
        player.addQueuedAction(plr -> {
            String timeLeftString = formattedTimeLeft();
            plr.getPA().sendString("tombsOfAmascut begins in: @gre@" + timeLeftString, 6570);
            plr.getPA().sendString("", 6572);
            plr.getPA().sendString("", 6664);
            plr.getPA().walkableInterface(6673);
        });
    }

    @Override
    public Boundary getBounds() {
        return Boundary.TOMBS_OF_AMASCUT_LOBBY;
    }

}
