package io.Odyssey.model;

import io.Odyssey.model.entity.player.Player;

public interface AmountInput {
    void handle(Player player, int amount);
}
