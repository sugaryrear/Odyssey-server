package io.Odyssey.content.worldevent.impl;

import java.util.List;

import io.Odyssey.content.commands.Command;
import io.Odyssey.content.commands.all.Ls;
import io.Odyssey.content.tournaments.TourneyManager;
import io.Odyssey.content.worldevent.WorldEvent;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.entity.player.broadcasts.Broadcast;

public class TournamentWorldEvent implements WorldEvent {

    private final TourneyManager tourney = TourneyManager.getSingleton();

    @Override
    public void init() {
        tourney.openLobby();
    }

    @Override
    public void dispose() {
        tourney.endGame();
    }

    @Override
    public boolean isEventCompleted() {
        return !tourney.isLobbyOpen() && !tourney.isArenaActive();
    }

    @Override
    public String getCurrentStatus() {
        return tourney.getTimeLeft();
    }

    @Override
    public String getEventName() {
        return "Tournament";
    }

    @Override
    public String getStartDescription() {
        return "starts";
    }

    @Override
    public Class<? extends Command> getTeleportCommand() {
        return Ls.class;
    }

    @Override
    public void announce(List<Player> players) {
        new Broadcast("@red@A " + tourney.getTournamentType() + " tournament will begin soon, type ::ls to join!").addTeleport(new Position(3142, 3637, 0)).copyMessageToChatbox().submit();
    }
}
