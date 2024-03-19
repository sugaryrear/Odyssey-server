package io.Odyssey.content.worldevent.impl;

import io.Odyssey.content.bosses.AngelofdeathSpawner;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreation;
import io.Odyssey.content.bosses.AvatarOfCreation.AvatarOfCreationSpawner;
import io.Odyssey.content.bosses.NexAngelOfDeath;
import io.Odyssey.content.commands.Command;
import io.Odyssey.content.commands.all.Worldevent;
import io.Odyssey.content.worldevent.WorldEvent;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.entity.player.broadcasts.Broadcast;

import java.util.List;

public class AngelofdEathWorldEvent implements WorldEvent { // done
    @Override
    public void init() {
        AngelofdeathSpawner.spawnNPC();
    }

    @Override
    public void dispose() {
        if (!isEventCompleted()) {
            NexAngelOfDeath.rewardPlayers(false);
        }
    }

    @Override
    public boolean isEventCompleted() {
        return !AngelofdeathSpawner.isSpawned();
    }

    @Override
    public String getCurrentStatus() {
        return "World Event: @gre@Angel of death";
    }

    @Override
    public String getEventName() {
        return "Angel of death";
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
        new Broadcast("@red@angel of death world boss has spawned, use ::worldevent to fight!").addTeleport(new Position(3892, 2506, 0)).copyMessageToChatbox().submit();
    }
}
