package io.Odyssey.util.logging.player;

import io.Odyssey.model.SlottedItem;
import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.entity.player.Position;
import io.Odyssey.model.items.GameItem;
import io.Odyssey.util.Misc;
import io.Odyssey.util.logging.PlayerLog;

import java.util.List;
import java.util.Set;

public class DeathItemsHeld extends PlayerLog {

    private final List<SlottedItem> inventory;
    private final List<SlottedItem> equipment;
    private final Position position;

    public DeathItemsHeld(Player player, List<SlottedItem> inventory, List<SlottedItem> equipment) {
        super(player);
        this.inventory = inventory;
        this.equipment = equipment;
        this.position = player.getPosition();
    }

    @Override
    public Set<String> getLogFileNames() {
        return Set.of("unsafe_death");
    }

    @Override
    public String getLoggedMessage() {
        return Misc.replaceBracketsWithArguments("{} held on death inventory={}, equipment={}", position, inventory, equipment);
    }
}
