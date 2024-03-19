package io.Odyssey.content.item.lootable;

import java.util.List;
import java.util.Map;

import io.Odyssey.model.entity.player.Player;
import io.Odyssey.model.items.GameItem;

public interface Lootable {

    Map<LootRarity, List<GameItem>> getLoot();

    void roll(Player player);

}
