package io.Odyssey.content.worldevent.impl;

import java.util.List;

import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreationSpawner;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.commands.Command;
import io.Odyssey.content.commands.all.Worldevent;
import io.Odyssey.content.worldevent.WorldEvent;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.entity.player.broadcasts.Broadcast;

public class AvatarOfCreationWorldEvent implements WorldEvent {
    @Override
    public void init() {
        AvatarOfCreationSpawner.spawnNPC();
    }

    @Override
    public void dispose() {
        if (!isEventCompleted()) {
            AvatarOfCreation.rewardPlayers(false);
        }
    }

    @Override
    public boolean isEventCompleted() {
        return !AvatarOfCreationSpawner.isSpawned();
    }

    @Override
    public String getCurrentStatus() {
        return "World Event: @gre@AvatarOfCreation";
    }

    @Override
    public String getEventName() {
        return "Avatar Of Creation";
    }

    @Override
    public String getStartDescription() {
        return "spawns";
    }

    @Override
    public Class<? extends Command> getTeleportCommand() {
        return Worldevent.class;
    }

    @Override
    public void announce(List<Player> players) {
        new Broadcast("@red@Avatar Of Creation world boss has spawned, use ::worldevent to fight!").addTeleport(new Position(3072, 3499, 0)).copyMessageToChatbox().submit();
    }
}
